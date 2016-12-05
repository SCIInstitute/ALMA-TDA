/*
 *     ALMA TDA - Contour tree based simplification and visualization for ALMA
 *     data cubes.
 *     Copyright (C) 2016 PAUL ROSEN
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     You may contact the Paul Rosen at <prosen@usf.edu>.
 */
package usf.saav.alma.drawing.VolumeRendering;

import static org.jocl.CL.CL_ADDRESS_CLAMP_TO_EDGE;
import static org.jocl.CL.CL_ADDRESS_REPEAT;
import static org.jocl.CL.CL_FILTER_LINEAR;
import static org.jocl.CL.CL_FILTER_NEAREST;
import static org.jocl.CL.CL_FLOAT;
import static org.jocl.CL.CL_INTENSITY;
import static org.jocl.CL.CL_MEM_COPY_HOST_PTR;
import static org.jocl.CL.CL_MEM_READ_ONLY;
import static org.jocl.CL.CL_MEM_WRITE_ONLY;
import static org.jocl.CL.CL_RGBA;

import java.io.IOException;
import org.jocl.cl_image_format;
import org.jocl.cl_sampler;

import processing.core.PMatrix3D;
import usf.saav.alma.data.ScalarField3D;
import usf.saav.common.SystemX;
import usf.saav.common.jocl.joclController;
import usf.saav.common.jocl.joclDevice;
import usf.saav.common.jocl.joclException;
import usf.saav.common.jocl.joclImage;
import usf.saav.common.jocl.joclKernel;
import usf.saav.common.jocl.joclMemory;
import usf.saav.common.jocl.joclPlatform;
import usf.saav.common.mvc.ControllerComponent;
import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.mvc.swing.TGraphics;
import usf.saav.common.mvc.swing.TImage;
import usf.saav.common.types.Float4;

public class VolumeRenderer extends ControllerComponent.Default implements ViewComponent, ControllerComponent {

	private TransferFunction1D tf;
	private ScalarField3D    sf;
	private TGraphics papplet;

	private joclKernel   kernel;
	private joclPlatform platform;
	private joclDevice   device;

	private joclMemory d_invViewMatrix;

	boolean linearFiltering = true;
	private joclImage d_volumeArray;
	private joclImage d_transferFuncArray;
	private cl_sampler transferFuncSampler;
	private cl_sampler volumeSamplerLinear;
	private cl_sampler volumeSamplerNearest;
	private joclMemory d_output;
	TImage img;
	
	boolean needUpdate = false;

	PMatrix3D matrixView  = new PMatrix3D();
	PMatrix3D matrixModel = new PMatrix3D();

	int res;

	boolean rotate = true;



	public VolumeRenderer( TGraphics papplet, joclController jocl, int res ){
		this.papplet  = papplet;
		this.platform = jocl.getPlatform(0);
		this.res = res;

		try {
			init( SystemX.readFileContents( getClass().getResourceAsStream("/usf/saav/alma/drawing/VolumeRendering/volumeRender.cl") ) );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		matrixView.translate(0, 0, -4);
	}

	private void init( String [] program ){

		for(int d = 0; d < platform.getDeviceCount(); d++){
			joclDevice _device = platform.getDevice(d);
			if( device == null ) device = _device;
			else if( _device.isAccelerator() ) device = _device;
			else if( _device.isGPU() && device.isCPU() ) device = _device;
		}
		kernel   = device.buildProgram(program, "d_render");

		print_info_message(device.toString());

		// Create samplers for transfer function, linear interpolation and nearest interpolation
		try {
			d_invViewMatrix 	 = device.createBuffer( "d_invViewMatrix", CL_MEM_READ_ONLY, 12*4 );
			transferFuncSampler  = device.createSampler( true, CL_ADDRESS_CLAMP_TO_EDGE, CL_FILTER_LINEAR);
			volumeSamplerLinear  = device.createSampler( true, CL_ADDRESS_REPEAT, CL_FILTER_LINEAR);
			volumeSamplerNearest = device.createSampler( true, CL_ADDRESS_REPEAT, CL_FILTER_NEAREST);
			d_output = device.createBuffer( "d_invViewMatrix", CL_MEM_WRITE_ONLY, res*res*4 );
		} catch (joclException e) {
			e.printStackTrace();
		}
	}
	
	public void tfUpdate( ){
		needUpdate = true;
	}

	public void setTransferFunction( TransferFunction1D tf ){
		this.tf = tf;

		if( tf == null ){
			return;
		}
		
		tf.addModifiedCallback( this,  "tfUpdate" );

		// create transfer function texture
		float transferFunc[] = new float[tf.size()*4];
		for(int i = 0; i < tf.size(); i++){
			Float4 c = tf.get(i);
			transferFunc[4*i+0] = c.z;
			transferFunc[4*i+1] = c.y;
			transferFunc[4*i+2] = c.x;
			transferFunc[4*i+3] = c.w;
		}

		cl_image_format transferFunc_format = new cl_image_format();
		transferFunc_format.image_channel_order = CL_RGBA;
		transferFunc_format.image_channel_data_type = CL_FLOAT;

		try {
			d_transferFuncArray = device.createImage2D( CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, transferFunc_format, transferFunc.length/4, 1, transferFunc);
		} catch (joclException e) {
			e.printStackTrace();
		}

		needUpdate = true;
	}

	public void setVolume( ScalarField3D sf ){
		this.sf = sf;

		if( d_volumeArray != null ){
			d_volumeArray.release();
		}

		if( sf == null ){
			return;
		}

		float [] h_tempVolume = new float[sf.getSize()];
		for(int i = 0; i < sf.getSize(); i++ ){
			h_tempVolume[i] = sf.getValue(i);
		}

		// create 3D array and copy data to device
		cl_image_format volume_format = new cl_image_format();
		volume_format.image_channel_order = CL_INTENSITY;
		volume_format.image_channel_data_type = CL_FLOAT;

		try {
			d_volumeArray = device.createImage3D( CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
					volume_format, sf.getWidth(), sf.getHeight(), sf.getDepth(),
					h_tempVolume );
		} catch (joclException e) {
			e.printStackTrace();
		}
		needUpdate = true;
	}

	@Override
	public void update( ) {
		if( tf == null ) return;
		if( sf == null ) return;
		if( !needUpdate ) return;
		
		float [] invViewMatrix = new float[12];
		PMatrix3D matrixMVP = new PMatrix3D();
		matrixMVP.apply( matrixView );
		matrixMVP.apply( matrixModel );

		PMatrix3D imatrixMVP = new PMatrix3D(matrixMVP);
		imatrixMVP.invert();
		invViewMatrix[0] = imatrixMVP.m00; invViewMatrix[1] = imatrixMVP.m01; invViewMatrix[2]  = imatrixMVP.m02; invViewMatrix[3]  = imatrixMVP.m03;
		invViewMatrix[4] = imatrixMVP.m10; invViewMatrix[5] = imatrixMVP.m11; invViewMatrix[6]  = imatrixMVP.m12; invViewMatrix[7]  = imatrixMVP.m13;
		invViewMatrix[8] = imatrixMVP.m20; invViewMatrix[9] = imatrixMVP.m21; invViewMatrix[10] = imatrixMVP.m22; invViewMatrix[11] = imatrixMVP.m23;

		float transferFunc[] = new float[tf.size()*4];
		for(int i = 0; i < tf.size(); i++){
			Float4 c = tf.get(i);
			transferFunc[4*i+0] = c.z;
			transferFunc[4*i+1] = c.y;
			transferFunc[4*i+2] = c.x;
			transferFunc[4*i+3] = c.w;
		}

		try {
			d_invViewMatrix.enqueueWriteBuffer( false, invViewMatrix );
			d_transferFuncArray.EnqueueWriteImage( true, transferFunc );

			int arg = 0;
			kernel.setKernelArg( arg++, d_output );
			kernel.setKernelArg( arg++, res );
			kernel.setKernelArg( arg++, res );
			kernel.setKernelArg( arg++, tf.getOffset() );
			kernel.setKernelArg( arg++, tf.getScale() );
			kernel.setKernelArg( arg++, d_invViewMatrix );
			kernel.setKernelArg( arg++, d_volumeArray );
			kernel.setKernelArg( arg++, d_transferFuncArray );
			kernel.setKernelArg( arg++, linearFiltering ? volumeSamplerLinear : volumeSamplerNearest );
			kernel.setKernelArg( arg++, transferFuncSampler );
			kernel.enqueueNDRangeKernel( new long[]{res,res} );

			if( img == null ) img = papplet.createImage( res, res, TGraphics.RGB);
			d_output.enqueueReadBuffer( true, img.getPixels() );
			img.update();

		} catch (joclException e) {
			e.printStackTrace();
		}
		needUpdate = false;
	}

	@Override
	public void draw(TGraphics g) {
		if( !isEnabled() ) return;
		if( tf == null ) return;
		if( sf == null ) return;
		if( img == null ) return;

		if( rotate ){
			matrixModel.rotate( 0.02f, 0.1f, 1.0f, 0.0f);
			needUpdate = true;
		}

		g.hint( TGraphics.DISABLE_DEPTH_TEST );
		g.strokeWeight(2);
		g.stroke(0);
		g.fill(255);
		g.rect( winX.start(), winY.start(),winX.length(), winY.length() );

		g.imageMode(TGraphics.CENTER);
		g.image( img, winX.start()+winX.length()/2, winY.start()+winY.length()/2, winX.length(), winY.length() );

		g.hint( TGraphics.ENABLE_DEPTH_TEST );

	}

	@Override
	public void drawLegend(TGraphics g) {
		return;
	}

	@Override public boolean keyPressed(char key) {
		if( !isEnabled() ) return false;
		if( key == ' ' ){
			rotate = !rotate;
			return true;
		}
		return false;
	}
}
