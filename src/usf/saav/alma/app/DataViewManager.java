package usf.saav.alma.app;

import usf.saav.alma.app.PersistenceSimplificationManager.PS2D;
import usf.saav.alma.app.PersistenceSimplificationManager.PS3D;
import usf.saav.alma.app.views.AlmaGui;
import usf.saav.alma.app.views.AlmaGui.ViewMode;
import usf.saav.alma.app.views.SingleScalarFieldView;
import usf.saav.scalarfield.PersistenceSimplifier2D;
import usf.saav.scalarfield.PersistenceSimplifier3D;
import usf.saav.scalarfield.PersistenceSimplifierND;
import usf.saav.scalarfield.ScalarField2D;
import usf.saav.scalarfield.ScalarField3D;
import usf.saav.alma.data.processors.Composite2D;
import usf.saav.alma.data.processors.Extract2DFrom3D;
import usf.saav.alma.data.processors.LayeredVolume;
import usf.saav.alma.drawing.SelectBoxDrawing;
import usf.saav.alma.drawing.SelectPointDrawing;
import usf.saav.alma.util.ContourTreeThread;
import usf.saav.alma.util.CoordinateSystemController;
import usf.saav.common.monitor.MonitoredDouble;
import usf.saav.common.monitor.MonitoredInteger;
import usf.saav.common.monitor.MonitoredObject;
import usf.saav.common.range.IntRange1D;

public class DataViewManager {


	public MonitoredScalarField2D src_sf2d  = new MonitoredScalarField2D( );
	public MonitoredScalarField2D simp_sf2d  = new MonitoredScalarField2D( );

	public MonitoredLayeredVolume src_sf3d  = new MonitoredLayeredVolume( );
	public MonitoredScalarField3D simp_sf3d  = new MonitoredScalarField3D( );
	
	ViewMode viewmode = ViewMode.SCALARFIELD;
	
	boolean need2DSrcRefresh = true;
	boolean need3DSrcRefresh = true;
	boolean needSimpRefresh = true;
	boolean need2DSimpSFRefresh = true;
	boolean need3DSimpSFRefresh = true;
	public CoordinateSystemController csCont;
	public SelectBoxDrawing   sel_box;
	public SelectPointDrawing sel_pnt;

	public MonitoredInteger curZ;
	public MonitoredInteger x0;
	public MonitoredInteger y0;
	public MonitoredInteger z0;
	public MonitoredInteger z1; 
	public MonitoredDouble zoom;
	
	public DataSetManager dsm;
	
	
	public ContourTreeManager ctm;
	public PersistenceSimplificationManager psm;

	public DataViewManager( DataSetManager dsm ){

		this.dsm  = dsm;

		this.curZ = dsm.curZ;
		this.x0   = dsm.x0;
		this.y0   = dsm.y0;
		this.z0   = dsm.z0;
		this.z1   = dsm.z1;
		this.zoom = dsm.zoom;
		
		
		this.csCont  = new CoordinateSystemController( x0, y0, zoom );
		this.sel_box = new SelectBoxDrawing( );
		this.sel_pnt = new SelectPointDrawing( );

		
		x0.addMonitor(   this, "set2DSrcRefresh" );
		x0.addMonitor(   this, "set2DSimplificationRefresh");
		x0.addMonitor(   this, "set3DSrcRefresh" );
		x0.addMonitor(   this, "set3DSimplificationRefresh");
		
		y0.addMonitor(   this, "set2DSrcRefresh" );
		y0.addMonitor(   this, "set2DSimplificationRefresh");
		y0.addMonitor(   this, "set3DSrcRefresh" );
		y0.addMonitor(   this, "set3DSimplificationRefresh");
		
		curZ.addMonitor( this, "set2DSrcRefresh" );
		curZ.addMonitor( this, "set2DSimplificationRefresh" );
		
		z0.addMonitor( this, "set3DSrcRefresh" );
		z0.addMonitor( this, "set3DSimplificationRefresh");

		z1.addMonitor( this, "set3DSrcRefresh" );
		z1.addMonitor( this, "set3DSimplificationRefresh");

		zoom.addMonitor( this, "set2DSrcRefresh" );
		zoom.addMonitor( this, "set2DSimplificationRefresh");
		zoom.addMonitor( this, "set3DSrcRefresh" );
		zoom.addMonitor( this, "set3DSimplificationRefresh");
		
	}
	
	public void setGUI( AlmaGui gui ){
		this.ctm = new ContourTreeManager( this, gui );
		this.psm = new PersistenceSimplificationManager( this, ctm );
	}

	
	public void setData( ){
		
	}

	
	public void update() {
		
		if( need2DSrcRefresh  )   this.refresh2DSourceSF();
		if( need3DSrcRefresh  )   this.refresh3DSourceSF();
		if( needSimpRefresh )     psm.refreshSimplification();
		if( need2DSimpSFRefresh ) this.refreshSimplifiedSF2D();
		if( need3DSimpSFRefresh ) this.refreshSimplifiedSF3D();
		
		need2DSrcRefresh    = false;
		need3DSrcRefresh    = false;
		needSimpRefresh     = false;
		need2DSimpSFRefresh = false;
		need3DSimpSFRefresh = false;
		
	}
	
	
	public void set2DSrcRefresh( ){ System.out.println("set refresh"); need2DSrcRefresh = true; }
	public void set3DSrcRefresh( ){ need3DSrcRefresh = true; }

	public void set2DSimplificationRefresh( ){ need2DSimpSFRefresh = true; }
	public void set3DSimplificationRefresh( ){ need3DSimpSFRefresh = true; }
	
	public void setSimplifyScalarField( ){
		psm.psf_map.clear();
		needSimpRefresh = true;
	}


	public SingleScalarFieldView ssfv;
	
	private void refresh2DSourceSF( ){ 
		System.out.println("refresh2DSourceSF");

		csCont.setPosition( ssfv.getView().getPosition() );

		float [] xy0 = csCont.getCoordinateSystemPosition( 0, 0 );
		float [] xy1 = csCont.getCoordinateSystemPosition(  ssfv.getView().getWidth(),  ssfv.getView().getHeight() );
		IntRange1D xr = new IntRange1D( (int)xy0[0], (int)xy1[0] );
		IntRange1D yr = new IntRange1D( (int)xy0[1], (int)xy1[1] );
		
		// Update 2D scalar field
		src_sf2d.set( dsm.getSlice( xr, yr, curZ.get() ) );
	}


	private void refresh3DSourceSF( ){
		
		csCont.setPosition( ssfv.getView().getPosition() );

		float [] xy0 = csCont.getCoordinateSystemPosition( 0, 0 );
		float [] xy1 = csCont.getCoordinateSystemPosition(  ssfv.getView().getWidth(),  ssfv.getView().getHeight() );
		IntRange1D xr = new IntRange1D( (int)xy0[0], (int)xy1[0] );
		IntRange1D yr = new IntRange1D( (int)xy0[1], (int)xy1[1] );

		// Update 3D scalar field
		LayeredVolume stack = new LayeredVolume( );
		for(int z = z0.get(); z<= z1.get(); z++){
			stack.addLayers( dsm.getSlice( xr, yr, z ) );
		}
		src_sf3d.set(stack);
		
	}

	
	
	public void completedSimplification( PersistenceSimplifier2D _pst ){
		PS2D pst = (PS2D)_pst;
		psm.psf_map.put( pst.getZ(), pst );
		need2DSimpSFRefresh = true;
		need3DSimpSFRefresh = true;
	}
	
	public void completedSimplification( PersistenceSimplifier3D _pst ){
		PS3D pst = (PS3D)_pst;
		for( int i = pst.getZ().start(); i <= pst.getZ().end(); i++ ){
			psm.psf_map.put( i,  pst ); 
		}
		need2DSimpSFRefresh = true;
		need3DSimpSFRefresh = true;
	}
	
	

	private void refreshSimplifiedSF2D( ){ 
		
		ctm.cur_ctt.set( ctm.ctt_map.containsKey( curZ.get() ) ? ctm.ctt_map.get( curZ.get() ) : null );

		PersistenceSimplifierND psf = psm.psf_map.containsKey( curZ.get() ) ? psm.psf_map.get( curZ.get() ) : null;
		ContourTreeThread 		ctt = ctm.ctt_map.containsKey( curZ.get() ) ? ctm.ctt_map.get( curZ.get() ) : null;

		// Update 2D scalar field
		if( psf != null ){
			if( psf instanceof ScalarField2D ){
				float [] tmp = csCont.getWindowPosition( ctt.getX().start(), ctt.getY().start() );
				simp_sf2d.set( new Composite2D( src_sf2d.get(), (ScalarField2D)psf, (int)(tmp[0]/zoom.get()), (int)(tmp[1]/zoom.get()) ) );
			}
			if( psf instanceof ScalarField3D ){
				float [] tmp = csCont.getWindowPosition( ctt.getX().start(), ctt.getY().start() );
				int layer = ctt.getZ().clamptoRange(  curZ.get() ) - ctt.getZ().start();
				ScalarField2D tmp2d = new Extract2DFrom3D( (ScalarField3D)psf, layer );
				simp_sf2d.set( new Composite2D( src_sf2d.get(), tmp2d, (int)(tmp[0]/zoom.get()), (int)(tmp[1]/zoom.get()) ) );
			}
		}
		else{
			simp_sf2d.set( src_sf2d.get() );
		}
	}
	
	
	private void refreshSimplifiedSF3D( ){ 

		// Update 3D scalar field
		LayeredVolume stack = new LayeredVolume( );
		for(int z = z0.get(); z<= z1.get(); z++){
			PersistenceSimplifierND psf = psm.psf_map.containsKey( z ) ? psm.psf_map.get( z ) : null;
			ContourTreeThread 		ctt = ctm.ctt_map.containsKey( z ) ? ctm.ctt_map.get( z ) : null;

			ScalarField2D slice = src_sf3d.get().getLayer( z-z0.get() );
			if( psf == null || ctt == null ){// || !gui.monShowSimp.get() ){
				stack.addLayers( slice );
			}
			else if( psf instanceof ScalarField2D ){
				float [] tmp = csCont.getWindowPosition( ctt.getX().start(), ctt.getY().start() );
				stack.addLayers( new Composite2D( slice, (ScalarField2D)psf, (int)(tmp[0]/zoom.get()), (int)(tmp[1]/zoom.get()) ) );
			}
			else if( psf instanceof ScalarField3D ){
				float [] tmp = csCont.getWindowPosition( ctt.getX().start(), ctt.getY().start() );
				int layer = ctt.getZ().clamptoRange(  curZ.get() ) - ctt.getZ().start();
				ScalarField2D tmp2d = new Extract2DFrom3D( (ScalarField3D)psf, layer );
				stack.addLayers( new Composite2D( slice, tmp2d, (int)(tmp[0]/zoom.get()), (int)(tmp[1]/zoom.get()) ) );
			}
		}
		simp_sf3d.set(stack);

	}




	public class MonitoredScalarField2D extends MonitoredObject<ScalarField2D>{
		@Override protected Class<?> getClassType() { return ScalarField2D.class; } 
	}
	
	public class MonitoredLayeredVolume extends MonitoredObject<LayeredVolume>{
		@Override protected Class<?> getClassType() { return LayeredVolume.class; } 
	};

	public class MonitoredScalarField3D extends MonitoredObject<ScalarField3D>{
		@Override protected Class<?> getClassType() { return ScalarField3D.class; } 
	};	


	
	
	
	

	public static void main(String args[]) {
		AlmaTDADev.main(args);
	}


}
