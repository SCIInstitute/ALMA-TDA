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
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import processing.core.PApplet;
import usf.saav.alma.algorithm.topology.PersistenceSet;
import usf.saav.alma.app.AlmaGui.Dimension;
import usf.saav.alma.app.AlmaGui.ViewMode;
import usf.saav.alma.data.ScalarField2D;
import usf.saav.alma.data.ScalarField3D;
import usf.saav.alma.data.processors.Composite2D;
import usf.saav.alma.data.processors.Extract2DFrom3D;
import usf.saav.alma.data.processors.LayeredVolume;
import usf.saav.alma.data.processors.PersistenceSimplifier2D;
import usf.saav.alma.data.processors.PersistenceSimplifier3D;
import usf.saav.alma.data.processors.PersistenceSimplifierND;
import usf.saav.alma.util.ContourTreeThread;
import usf.saav.common.mvc.ControllerComponent;
import usf.saav.common.mvc.DefaultApp;
import usf.saav.common.range.IntRange1D;

public class AlmaController extends ControllerComponent.Subcontroller implements ControllerComponent {

	ExecutorService threadPool = Executors.newFixedThreadPool(4);
	
	ViewMode viewmode = ViewMode.SCALARFIELD;

	boolean need2DSrcRefresh = true;
	boolean need3DSrcRefresh = true;
	boolean needSimpRefresh = true;
	boolean need2DSimpSFRefresh = true;
	boolean need3DSimpSFRefresh = true;

	private DefaultApp<?,?,?> mvc;
	private AlmaModel model;

	public AlmaController( DefaultApp<?,?,?> mvc, AlmaModel _model, boolean verbose ) {
		super(verbose);
		this.mvc = mvc;
		this.model = _model;
	}


	public void setup( ){
		registerSubController( model.ssfv.getController(),  5 );
		registerSubController( model.vrv.getController(),  10 );

		model.x0.addMonitor(   this, "set2DSrcRefresh" );
		model.x0.addMonitor(   this, "set2DSimplificationRefresh");
		model.x0.addMonitor(   this, "set3DSrcRefresh" );
		model.x0.addMonitor(   this, "set3DSimplificationRefresh");
		
		model.y0.addMonitor(   this, "set2DSrcRefresh" );
		model.y0.addMonitor(   this, "set2DSimplificationRefresh");
		model.y0.addMonitor(   this, "set3DSrcRefresh" );
		model.y0.addMonitor(   this, "set3DSimplificationRefresh");
		
		model.curZ.addMonitor( this, "set2DSrcRefresh" );
		model.curZ.addMonitor( this, "set2DSimplificationRefresh" );
		
		model.z0.addMonitor( this, "set3DSrcRefresh" );
		model.z0.addMonitor( this, "set3DSimplificationRefresh");

		model.z1.addMonitor( this, "set3DSrcRefresh" );
		model.z1.addMonitor( this, "set3DSimplificationRefresh");

		model.zoom.addMonitor( this, "set2DSrcRefresh" );
		model.zoom.addMonitor( this, "set2DSimplificationRefresh");
		model.zoom.addMonitor( this, "set3DSrcRefresh" );
		model.zoom.addMonitor( this, "set3DSimplificationRefresh");
		
		model.gui.monBuildTree.addMonitor( this, "buildContourTree" );

		super.setup();
	}

	
	public void update() {
		
		if( need2DSrcRefresh  )   this.refresh2DSourceSF();
		if( need3DSrcRefresh  )   this.refresh3DSourceSF();
		if( needSimpRefresh )     this.refreshSimplification();
		if( need2DSimpSFRefresh ) this.refreshSimplifiedSF2D();
		if( need3DSimpSFRefresh ) this.refreshSimplifiedSF3D();
		
		need2DSrcRefresh    = false;
		need3DSrcRefresh    = false;
		needSimpRefresh     = false;
		need2DSimpSFRefresh = false;
		need3DSimpSFRefresh = false;
		
		super.update();
	}
	
	
	public void set2DSrcRefresh( ){ need2DSrcRefresh = true; }
	public void set3DSrcRefresh( ){ need3DSrcRefresh = true; }

	public void set2DSimplificationRefresh( ){ need2DSimpSFRefresh = true; }
	public void set3DSimplificationRefresh( ){ need3DSimpSFRefresh = true; }
	
	public void setSimplifyScalarField( ){
		model.psf_map.clear();
		needSimpRefresh = true;
	}

	
	public void buildContourTree( ){
		
		IntRange1D [] r = model.sel.getSelection();
		if( r == null ) return;
		
		model.sel.clearSelection();
		
		if( model.gui.monDim.get() == Dimension.DIM_2D ) {
			ContourTreeThread ctt = new ContourTreeThread( model.reader, r[0], r[1], model.curZ.get() );
			ctt.setCallback( this, "completedContourTree");
			model.ctt_map.put( model.curZ.get(), ctt );
			threadPool.submit( ctt );
		}
		if( model.gui.monDim.get() == Dimension.DIM_2D_STACK ) {
			for( int z = model.z0.get(); z <= model.z1.get(); z++ ){
				ContourTreeThread ctt = new ContourTreeThread( model.reader, r[0], r[1], z );
				ctt.setCallback( this, "completedContourTree");
				model.ctt_map.put( z, ctt );
				threadPool.submit( ctt );
			}
		}
		if( model.gui.monDim.get() == Dimension.DIM_3D ) {
			ContourTreeThread ctt = new ContourTreeThread( model.reader, r[0], r[1], new IntRange1D( model.z0.get(), model.z1.get() ) );
			ctt.setCallback( this, "completedContourTree");
			for( int z = model.z0.get(); z <= model.z1.get(); z++ ){
				model.ctt_map.put( z, ctt );
			}
			threadPool.submit( ctt );
		}

	}
	
	public void completedContourTree( ContourTreeThread ctt ){
		boolean stillProcessing = false;
		for( Entry<Integer,ContourTreeThread> e_ctt : model.ctt_map.entrySet() ){
			stillProcessing = stillProcessing || !e_ctt.getValue().isProcessingComplete();
		}
		
		if( !stillProcessing ){
			model.cur_ctt.set( model.ctt_map.containsKey( model.curZ.get() ) ? model.ctt_map.get( model.curZ.get() ) : null );;
			Set<PersistenceSet> pss = new HashSet<PersistenceSet>( );
			for( int z = model.z0.get(); z <= model.z1.get(); z++ ){
				if( model.ctt_map.containsKey(z) && model.ctt_map.get(z).getTree() != null ) 
					pss.add( model.ctt_map.get(z).getTree() );
			}
			model.pss.set( pss );
		}
	}

	
	private void refresh2DSourceSF( ){ 

		model.csCont.setPosition( model.ssfv.getView().getPosition() );

		float [] xy0 = model.csCont.getCoordinateSystemPosition( 0, 0 );
		float [] xy1 = model.csCont.getCoordinateSystemPosition(  model.ssfv.getView().getWidth(),  model.ssfv.getView().getHeight() );
		IntRange1D xr = new IntRange1D( (int)xy0[0], (int)xy1[0] );
		IntRange1D yr = new IntRange1D( (int)xy0[1], (int)xy1[1] );
		
		// Update 2D scalar field
		try {
			model.src_sf2d.set( model.reader.getSlice( xr, yr, model.curZ.get(), 0) );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void refresh3DSourceSF( ){
		
		model.csCont.setPosition( model.ssfv.getView().getPosition() );

		float [] xy0 = model.csCont.getCoordinateSystemPosition( 0, 0 );
		float [] xy1 = model.csCont.getCoordinateSystemPosition(  model.ssfv.getView().getWidth(),  model.ssfv.getView().getHeight() );
		IntRange1D xr = new IntRange1D( (int)xy0[0], (int)xy1[0] );
		IntRange1D yr = new IntRange1D( (int)xy0[1], (int)xy1[1] );

		// Update 3D scalar field
		LayeredVolume stack = new LayeredVolume( );
		for(int z = model.z0.get(); z<= model.z1.get(); z++){
			try{ 
				stack.addLayers( model.reader.getSlice( xr, yr, z, 0) );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		model.src_sf3d.set(stack);
		
	}

	
	private void refreshSimplification( ) {
		
		if( !model.ctt_map.containsKey( model.curZ.get() ) ) return;
		if( model.psf_map.containsKey( model.curZ.get() ) ) return;
		
		ContourTreeThread ctt = model.ctt_map.get( model.curZ.get() );
		if( ctt.getScalarField() == null || ctt.getTree() == null ) return;
		
		if( ctt.getScalarField() instanceof ScalarField2D ){
			PersistenceSimplifier2D psf = new PersistenceSimplifier2D( (ScalarField2D)ctt.getScalarField(), ctt.getTree(), ctt.getComponentList(), model.curZ.get(), false );
			psf.setCallback(this, "completedSimplification" );
			threadPool.execute( psf );
		}
		
		if( ctt.getScalarField() instanceof ScalarField3D ){
			PersistenceSimplifier3D psf = new PersistenceSimplifier3D( (ScalarField3D)ctt.getScalarField(), ctt.getTree(), ctt.getComponentList(), ctt.getZ(), false );
			psf.setCallback(this, "completedSimplification" );
			threadPool.execute( psf );
		}
		
		for( int z = model.z0.get(); z <= model.z1.get(); z++ ){
			if(  model.psf_map.containsKey(z) ) continue;
			if( !model.ctt_map.containsKey(z) ) continue;
			if( model.curZ.get() == z ) continue;
			ctt = model.ctt_map.get( z );
			if( ctt.getScalarField() == null || ctt.getTree() == null ) continue;
			if( ctt.getScalarField() instanceof ScalarField2D ){
				PersistenceSimplifier2D psf = new PersistenceSimplifier2D( (ScalarField2D)ctt.getScalarField(), ctt.getTree(), ctt.getComponentList(), z, false );
				psf.setCallback(this, "completedSimplification" );
				threadPool.execute( psf );
			}
		}
		
	}
	
	public void completedSimplification( PersistenceSimplifier2D pst ){
		model.psf_map.put( pst.getZ(), pst );
		need2DSimpSFRefresh = true;
		need3DSimpSFRefresh = true;
	}
	
	public void completedSimplification( PersistenceSimplifier3D pst ){
		for( int i = pst.getZ().start(); i <= pst.getZ().end(); i++ ){
			model.psf_map.put( i,  pst ); 
		}
		need2DSimpSFRefresh = true;
		need3DSimpSFRefresh = true;
	}
	
	
	private void refreshSimplifiedSF2D( ){ 
		
		model.cur_ctt.set( model.ctt_map.containsKey( model.curZ.get() ) ? model.ctt_map.get( model.curZ.get() ) : null );

		PersistenceSimplifierND psf = model.psf_map.containsKey( model.curZ.get() ) ? model.psf_map.get( model.curZ.get() ) : null;
		ContourTreeThread 		ctt = model.ctt_map.containsKey( model.curZ.get() ) ? model.ctt_map.get( model.curZ.get() ) : null;

		// Update 2D scalar field
		if( psf != null ){
			if( psf instanceof ScalarField2D ){
				float [] tmp = model.csCont.getWindowPosition( ctt.getX().start(), ctt.getY().start() );
				model.simp_sf2d.set( new Composite2D( model.src_sf2d.get(), (ScalarField2D)psf, (int)(tmp[0]/model.zoom.get()), (int)(tmp[1]/model.zoom.get()) ) );
			}
			if( psf instanceof ScalarField3D ){
				float [] tmp = model.csCont.getWindowPosition( ctt.getX().start(), ctt.getY().start() );
				int layer = ctt.getZ().clamptoRange(  model.curZ.get() ) - ctt.getZ().start();
				ScalarField2D tmp2d = new Extract2DFrom3D( (ScalarField3D)psf, layer );
				model.simp_sf2d.set( new Composite2D( model.src_sf2d.get(), tmp2d, (int)(tmp[0]/model.zoom.get()), (int)(tmp[1]/model.zoom.get()) ) );
			}
		}
		else{
			model.simp_sf2d.set( model.src_sf2d.get() );
		}
	}
	
	
	private void refreshSimplifiedSF3D( ){ 

		// Update 3D scalar field
		LayeredVolume stack = new LayeredVolume( );
		for(int z = model.z0.get(); z<= model.z1.get(); z++){
			PersistenceSimplifierND psf = model.psf_map.containsKey( z ) ? model.psf_map.get( z ) : null;
			ContourTreeThread 		ctt = model.ctt_map.containsKey( z ) ? model.ctt_map.get( z ) : null;

			ScalarField2D slice = model.src_sf3d.get().getLayer( z-model.z0.get() );
			if( psf == null || ctt == null || !model.gui.monShowSimp.get() ){
				stack.addLayers( slice );
			}
			else if( psf instanceof ScalarField2D ){
				float [] tmp = model.csCont.getWindowPosition( ctt.getX().start(), ctt.getY().start() );
				stack.addLayers( new Composite2D( slice, (ScalarField2D)psf, (int)(tmp[0]/model.zoom.get()), (int)(tmp[1]/model.zoom.get()) ) );
			}
			else if( psf instanceof ScalarField3D ){
				float [] tmp = model.csCont.getWindowPosition( ctt.getX().start(), ctt.getY().start() );
				int layer = ctt.getZ().clamptoRange(  model.curZ.get() ) - ctt.getZ().start();
				ScalarField2D tmp2d = new Extract2DFrom3D( (ScalarField3D)psf, layer );
				stack.addLayers( new Composite2D( slice, tmp2d, (int)(tmp[0]/model.zoom.get()), (int)(tmp[1]/model.zoom.get()) ) );
			}
		}
		model.simp_sf3d.set(stack);
	}

	
	@Override
	public boolean keyPressed( char key ){
		if( super.keyPressed(key) ) return true;
		
		switch( key ){
			case '+': model.curZ.incr(); return true;
			case '-': model.curZ.decr(); return true;
			case 's': mvc.saveImage(); 		 return true;
			case 'v': mvc.saveVideoToggle(); return true;
		}
		
		return false;
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "usf.saav.alma.app.AlmaTDADev" });
	}
}
