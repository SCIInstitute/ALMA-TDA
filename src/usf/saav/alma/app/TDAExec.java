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
package usf.saav.alma.app;

import java.io.IOException;

import nom.tam.fits.common.FitsException;
import usf.saav.alma.app.TDAInteractive.TreeDimension;
import usf.saav.alma.data.fits.FitsReader;
import usf.saav.alma.data.fits.FitsWriter;
import usf.saav.alma.data.fits.RawFitsReader;
import usf.saav.alma.data.fits.SafeFitsReader;
import usf.saav.alma.data.processors.Composite2D;
import usf.saav.alma.data.processors.Extract2DFrom3D;
import usf.saav.common.range.IntRange1D;
import usf.saav.scalarfield.ScalarField2D;

public class TDAExec {

	FitsReader fits = null;
	
	public String filename = null;
	public IntRange1D xr=null,yr=null,zr=null;
	public float simplification = 0;
	public TreeDimension treedim = TreeDimension.DIM_2D;
	public String  output  = null;

	
	
	public TDAExec() {	}


	public void loadFile( ){
		try {
			fits = new SafeFitsReader( new RawFitsReader(filename, true), true );
		} catch (IOException | FitsException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		if(xr==null) xr = fits.getAxesSize()[0].clone();
		if(yr==null) yr = fits.getAxesSize()[1].clone();
		if(zr==null) zr = fits.getAxesSize()[2].clone();
	}

	
	public void saveOutput( ){
		TDAProcessor2D  sliceProc = new TDAProcessor2D();
		TDAProcessor3D volProc   = new TDAProcessor3D();

		if( output == null ) return;
		if( treedim == TreeDimension.DIM_3D ){
			volProc.process( fits,  xr, yr, zr, simplification );
		}		
		try {
			FitsWriter fw = new FitsWriter( fits.getAxesSize()[0].length(), fits.getAxesSize()[1].length(), fits.getAxesSize()[2].length() );
			fw.open( fits, output );
			for(int d = 0; d < fits.getAxesSize()[2].length(); d++ ){
				
				ScalarField2D writeslice = fits.getSlice(d, 0);
				if( (treedim == TreeDimension.DIM_2D || treedim == TreeDimension.DIM_2D_STACK) && zr.inRange(d) ){
					sliceProc.process(fits, xr, yr, d, simplification);
					writeslice = new Composite2D(writeslice, sliceProc.ps2d, xr.start(), yr.start() );
				}
				if( treedim == TreeDimension.DIM_3D && zr.inRange(d) ){
					ScalarField2D modslice = new Extract2DFrom3D( volProc.ps3d, d-zr.start() );
					writeslice = new Composite2D(writeslice, modslice, xr.start(), yr.start() );
				}
				fw.writeSlice( writeslice );
				
			}
			fw.close();
		} catch (IOException | FitsException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	


	
	public static void main( String [] args ){
		
		CmdlineParser.printLicense();
		
		if( args.length == 0 ){
			CmdlineParser.printUsage();
			return;
		}
		
		TDAExec       exec = new TDAExec();
		CmdlineParser cmd  = new CmdlineParser( exec, args );
		if( cmd.interactive ){
			TDAInteractive.create( exec );
		}
		else{
			exec.loadFile( );
			exec.saveOutput( );
			CmdlineParser.printReproduce( exec );
		}
	}
	
	
}
