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
package usf.saav.alma.util;

import usf.saav.scalarfield.ScalarField1D;
import usf.saav.scalarfield.ScalarField2D;
import usf.saav.scalarfield.ScalarField3D;
import usf.saav.scalarfield.ScalarFieldND;
import usf.saav.alma.data.processors.Extract1Dfrom3D;
import usf.saav.alma.data.processors.Extract2DFrom3D;
import usf.saav.alma.data.processors.Subset1D;
import usf.saav.alma.data.processors.Subset2D;
import usf.saav.alma.data.processors.Subset3D;
import usf.saav.common.BasicObject;
import usf.saav.common.monitoredvariables.Callback;
import usf.saav.common.range.IntRange1D;
import usf.saav.mesh.ConnectedComponentMesh;
import usf.saav.mesh.Mesh;
import usf.saav.mesh.ScalarFieldMesh;
import usf.saav.topology.TopoTree;
import usf.saav.topology.contour.PseudoContourTree;

// TODO: Auto-generated Javadoc
/**
 * The Class ContourTreeThread.
 */
public class ContourTreeThread extends BasicObject implements Runnable {

	private IntRange1D rx = null, ry = null, rz = null;
	private ScalarFieldND sf = null;
	private PseudoContourTree ct = null;
	private ConnectedComponentMesh cl = null;

	private Callback cb = null;
	private boolean ready = false;

/**
 * Instantiates a new contour tree thread.
 *
 * @param fits the fits
 * @param rx the rx
 * @param ry the ry
 * @param z the z
 */
	/*
	public ContourTreeThread( ScalarField3D fits, int rx, int ry, IntRange1D rz ){
		this(fits,rx,ry,rz,true);
	}
	*/

/**
 * Instantiates a new contour tree thread.
 *
 * @param fits the fits
 * @param rx the rx
 * @param ry the ry
 * @param z the z
 * @param verbose the verbose
 */
	
	
	public ContourTreeThread( ScalarField3D fits, int rx, int ry, IntRange1D rz, boolean verbose ){
		super(verbose);

		this.rx = new IntRange1D(rx,rx);
		this.ry = new IntRange1D(ry,ry);
		this.rz = rz;
		
		sf = new Subset1D( new Extract1Dfrom3D( fits, rx, ry ), rz );

	}
		
	public ContourTreeThread( ScalarField3D fits, IntRange1D rx, IntRange1D ry, int z ){
		this(fits,rx,ry,z,true);
	}
	public ContourTreeThread( ScalarField3D fits, IntRange1D rx, IntRange1D ry, int z, boolean verbose ){
		super(verbose);

		this.rx = rx;
		this.ry = ry;
		this.rz = new IntRange1D(z,z);
		
		sf = new Subset2D( new Extract2DFrom3D( fits, z ), rx, ry );
	}
	

	/**
	 * Instantiates a new contour tree thread.
	 *
	 * @param fits the fits
	 * @param rx the rx
	 * @param ry the ry
	 * @param rz the rz
	 */
	public ContourTreeThread( ScalarField3D fits, IntRange1D rx, IntRange1D ry, IntRange1D rz ){
		this(fits,rx,ry,rz,true);
	}

	/**
	 * Instantiates a new contour tree thread.
	 *
	 * @param fits the fits
	 * @param rx the rx
	 * @param ry the ry
	 * @param rz the rz
	 * @param verbose the verbose
	 */
	public ContourTreeThread( ScalarField3D fits, IntRange1D rx, IntRange1D ry, IntRange1D rz, boolean verbose ){
		super(verbose);

		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		this.sf = new Subset3D( fits,rx,ry,rz );

	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if( sf == null ) return;
		
		

		if( sf instanceof ScalarField1D ) print_info_message("Building 1D Contour Tree of " + rx.length()*ry.length()*rz.length() + " elements");
		if( sf instanceof ScalarField2D ) print_info_message("Building 2D Contour Tree of " + rx.length()*ry.length()*rz.length() + " elements");
		if( sf instanceof ScalarField3D ) print_info_message("Building 3D Contour Tree of " + rx.length()*ry.length()*rz.length() + " elements");
		
		print_info_message( sf.getSize() );
		try{
		print_info_message( sf.getValue(0) );
		print_info_message( sf.getValue(1) );
		print_info_message( sf.getValue(2) );
		} catch (Exception e ){
			e.printStackTrace();
		}
		
		print_info_message("Constructing Mesh");
		cl = new ConnectedComponentMesh( new ScalarFieldMesh( sf ) );
		print_info_message("Constructing Tree");
		ct = new PseudoContourTree( cl );
		print_info_message("Contour Tree Construction Complete");

		ready = true;
		if( cb != null ){
			cb.call( this );
		}
	}


	public TopoTree       getTree( ){			   return ct; }
	public Mesh           getComponentList(){	   return cl; }
	public IntRange1D	  getX( ){				   return rx; }
	public IntRange1D	  getY( ){				   return ry; }
	public IntRange1D	  getZ( ){				   return rz; }
	public ScalarFieldND  getScalarField( ){       return sf; }
	public boolean        isProcessingComplete( ){ return ready; }

	/**
	 * Sets the callback.
	 *
	 * @param obj the obj
	 * @param func_name the func name
	 */
	public void setCallback( Object obj, String func_name ) {
		try {
			this.cb = new Callback( obj, func_name, ContourTreeThread.class );
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
}
