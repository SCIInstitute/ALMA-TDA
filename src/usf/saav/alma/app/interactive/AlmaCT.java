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
package usf.saav.alma.app.interactive;

import java.io.IOException;
import java.util.HashMap;

import usf.saav.alma.app.TDAInteractive.TreeDimension;
import usf.saav.alma.data.processors.Extract2DFrom3D;
import usf.saav.alma.drawing.ContourTreeDrawing;
import usf.saav.alma.drawing.LabelDrawing;
import usf.saav.alma.drawing.PersistenceDiagramDrawing;
import usf.saav.alma.util.CoordinateSystem;
import usf.saav.common.BasicObject;
import usf.saav.common.mvc.ControllerComponent;
import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.range.IntRange1D;
import usf.saav.mesh.ConnectedComponentMesh;
import usf.saav.mesh.ScalarFieldMesh;
import usf.saav.scalarfield.PersistenceSimplifier2D;
import usf.saav.scalarfield.PersistenceSimplifier3D;
import usf.saav.scalarfield.ScalarField2D;
import usf.saav.scalarfield.ScalarField3D;
import usf.saav.topology.TopoTree;
import usf.saav.topology.PseudoContourTree;

public class AlmaCT extends BasicObject {
	private ContourTreeDrawing		  ctv;
	private PersistenceDiagramDrawing pdd;
	private LabelDrawing			  ctLabel;

	private AlmaModel model;
	private HashMap<Integer,ScalarField2D> slice = new HashMap<Integer,ScalarField2D>();
	private HashMap<Integer,ConnectedComponentMesh> cl = new HashMap<Integer,ConnectedComponentMesh>();
	private HashMap<Integer,PseudoContourTree> ct = new HashMap<Integer,PseudoContourTree>();
	IntRange1D[] selRegion;

	
	private CTThread computeThread;
	HashMap<Integer,ScalarField2D> ps2d = new HashMap<Integer,ScalarField2D>();
	ScalarField3D vol;
	ConnectedComponentMesh vcl;
	private PseudoContourTree pct;
	int z0=0,z1=-1;
	private ControllerComponent controller;


	public AlmaCT( AlmaModel _model, ControllerComponent _controller, IntRange1D[] _selRegion ){
		super(true);		
		model     = _model;
		selRegion = _selRegion;
		controller = _controller;
		
		//model.monDim
		
		print_info_message( selRegion[0].toString() );
		print_info_message( selRegion[1].toString() );

		pdd     = new PersistenceDiagramDrawing();
		ctv     = new ContourTreeDrawing( model.monZ );
		ctLabel = new LabelDrawing.ComputingLabel() {
			@Override public void update( ){
				label = "Computing Contour Tree..." + getComputeString();
			}
		};
		
		//z = model.monZ.get();
		pdd.addPersistentSimplificationCallback( this, "updateSimplifiedImage" );
		pdd.addPersistentSimplificationCallback( model, "updateConfigSimplification" );
		model.monShowTree.addMonitor( ctv, "setEnabled" );

		computeThread = new CTThread();
		new Thread( computeThread ).start();
		
	}
	
	ScalarField2D getSlice( int slice ){
		if( ps2d.containsKey(slice) ) return ps2d.get(slice);
		return null;
	}
	
	class CTThread implements Runnable {
		public boolean stop = false;
		
		@Override public void run() {
			if( model.monDim.get() == TreeDimension.DIM_3D ){ 
				model.updateConfigRanges( selRegion[0], selRegion[1], new IntRange1D(z0,z1) );
				z0 = model.monZ0.get();
				z1 = model.monZ1.get();
				try {
					vol = model.fits.getVolume( selRegion[0], selRegion[1], new IntRange1D(z0,z1), 0 );
				} catch (IOException e) {
					e.printStackTrace();
				}
				if( stop ) return;
				
				print_info_message("Constructing Mesh");
				vcl = new ConnectedComponentMesh( new ScalarFieldMesh( vol ) );
				if( stop ) return;
				
				print_info_message("Constructing Tree");
				pct = new PseudoContourTree( vcl );
				print_info_message("Contour Tree Construction Complete");
				if( stop ) return;

				ctLabel.disable();
				
				pdd.setParameterizations( pct, pct );
				pdd.enable();
				
				ctv.setCoordinateSystem( model.csCont );
				ctv.setField( vol, pct, vcl, new IntRange1D(z0,z1) );
				ctv.setRegion( selRegion );
				ctv.enable();
			}
			else {
				if( model.monDim.get() == TreeDimension.DIM_2D ){ z0 = z1 = model.monZ.get(); }
				if( model.monDim.get() == TreeDimension.DIM_2D_STACK ){ 
					z0 = model.monZ0.get();
					z1 = model.monZ1.get(); 
				}
				model.updateConfigRanges( selRegion[0], selRegion[1], new IntRange1D(z0,z1) );
				
				for (int cz = z0; cz <= z1; cz++ ){
					try {
						slice.put( cz, model.fits.getSlice( selRegion[0], selRegion[1], cz, 0) );
					} catch (IOException e) {
						e.printStackTrace();
					}
					if( stop ) return;
					
					print_info_message("Constructing Mesh");
					cl.put( cz, new ConnectedComponentMesh( new ScalarFieldMesh( slice.get(cz) ) ) );
					if( stop ) return;
					
					print_info_message("Constructing Tree");
					ct.put( cz, new PseudoContourTree( cl.get(cz) ) );
					print_info_message("Contour Tree Construction Complete");
					if( stop ) return;
				}
				
				ctLabel.disable();
				
				pdd.setParameterizations( ct.get( model.monZ.get() ), ct.values().toArray( new TopoTree[ct.size()] ) );
				pdd.enable();
				
				ctv.setCoordinateSystem( model.csCont );
				ctv.setField( slice.get(model.monZ.get()), ct.get(model.monZ.get()), cl.get(model.monZ.get()), new IntRange1D( model.monZ.get() ) );
				ctv.setRegion( selRegion );
				ctv.enable();
			}
			

		}		
	}
	
	public void updateSimplifiedImage( ){
		for( int cz : ct.keySet() ){
			ps2d.put( cz, new PersistenceSimplifier2D( slice.get(cz), ct.get(cz), cl.get(cz), true ) );
			((AlmaGL.Controller)controller).setUpdateSF();
		}
		if( vol != null ){
			ScalarField3D sf3d = new PersistenceSimplifier3D(vol, pct, vcl, true);
			for(int z = 0; z < sf3d.getDepth(); z++ ){
				ps2d.put( z+z0, new Extract2DFrom3D( sf3d, z ) );
			}
			((AlmaGL.Controller)controller).setUpdateSF();
		}
	}
	
	void stop( ){
		if( computeThread != null ){
			computeThread.stop = true;
			computeThread = null;
		}
	}
	
	void registerViews( ViewComponent.Subview view ){
		view.registerSubView( ctv,   	20 );
		view.registerSubView( pdd,   	40 );
		view.registerSubView( ctLabel,	45 );
	}

	void unregisterViews( ViewComponent.Subview view ){
		view.unregisterSubView( ctv );
		view.unregisterSubView( pdd );
		view.unregisterSubView( ctLabel );
	}
	
	void registerControls( ControllerComponent.Subcontroller cont ){
		cont.registerSubController( pdd, 5 );
	}

	void unregisterControls( ControllerComponent.Subcontroller cont ){
		cont.unregisterSubController( pdd );
	}
	
	public void setCoordinateSystem( CoordinateSystem csc ){
		ctv.setCoordinateSystem( csc );
	}
	
	public void setPosition( int [] uvwh ){
		setPosition(uvwh[0], uvwh[1], uvwh[2], uvwh[3]);
	}

	public void setPosition( int u0, int v0, int w, int h ){
		ctv.setPosition(	 u0, v0, w, h );
		pdd.setPosition(   	 u0+10, v0+10, h/3, h/3 );
		ctLabel.setPosition( u0+10, v0+10, 20, 20 );
	}
	
	public void update(){
		if( pdd != null && ctv != null )
			ctv.setSelected( pdd.getSelected() );
			
	}
}
