package usf.saav.alma.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;

import nom.tam.fits.common.FitsException;
import usf.saav.alma.algorithm.topology.PersistenceSet;
import usf.saav.alma.app.AlmaGui.ViewMode;
import usf.saav.alma.app.views.SingleScalarFieldView;
import usf.saav.alma.data.ScalarField2D;
import usf.saav.alma.data.ScalarField3D;
import usf.saav.alma.data.Settings;
import usf.saav.alma.data.Settings.SettingsDouble;
import usf.saav.alma.data.Settings.SettingsInt;
import usf.saav.alma.data.fits.CacheFactory;
import usf.saav.alma.data.fits.CachedFitsReader;
import usf.saav.alma.data.fits.FitsReader;
import usf.saav.alma.data.fits.RawFitsReader;
import usf.saav.alma.data.fits.SafeFitsReader;
import usf.saav.alma.data.processors.Composite2D;
import usf.saav.alma.data.processors.Extract2DFrom3D;
import usf.saav.alma.data.processors.LayeredVolume;
import usf.saav.alma.data.processors.PersistenceSimplifier2D;
import usf.saav.alma.data.processors.PersistenceSimplifier3D;
import usf.saav.alma.data.processors.PersistenceSimplifierND;
import usf.saav.alma.drawing.SelectBoxDrawing;
import usf.saav.alma.drawing.SelectPointDrawing;
import usf.saav.alma.util.ContourTreeThread;
import usf.saav.alma.util.CoordinateSystemController;
import usf.saav.common.monitor.MonitoredObject;
import usf.saav.common.range.IntRange1D;

// TODO: Auto-generated Javadoc
/**
 * The Class DataManager.
 */
public class DataManager {


	private Settings settings;
	public SettingsInt curZ;
	public SettingsInt x0;
	public SettingsInt y0;
	public SettingsInt z0;
	public SettingsInt z1; 
	public SettingsDouble zoom;
	
	public Map< Integer, PersistenceSimplifierND > psf_map = new HashMap< Integer, PersistenceSimplifierND >( );
	public Map< Integer, ContourTreeThread >       ctt_map = new HashMap< Integer, ContourTreeThread >( );

	
	public MonitoredScalarField2D src_sf2d  = new MonitoredScalarField2D( );
	public MonitoredScalarField2D simp_sf2d  = new MonitoredScalarField2D( );

	public MonitoredLayeredVolume src_sf3d  = new MonitoredLayeredVolume( );
	public MonitoredScalarField3D simp_sf3d  = new MonitoredScalarField3D( );
	
	public MonitoredContourTreeThread cur_ctt = new MonitoredContourTreeThread( );

	public MonitoredObject< Set<PersistenceSet> > pss = new MonitoredObject< Set<PersistenceSet> >( ){
		@Override protected Class<?> getClassType() { return Set.class; } 
	};
	
	
	


	
	FitsReader reader; 
	


	ExecutorService threadPool = Executors.newFixedThreadPool(4);
	
	ViewMode viewmode = ViewMode.SCALARFIELD;

	
	boolean need2DSrcRefresh = true;
	boolean need3DSrcRefresh = true;
	boolean needSimpRefresh = true;
	boolean need2DSimpSFRefresh = true;
	boolean need3DSimpSFRefresh = true;
	public CoordinateSystemController csCont;
	public SelectBoxDrawing   sel_box;
	public SelectPointDrawing sel_pnt;


	
	
	

	/**
	 * Instantiates a new data manager.
	 *
	 * @param filename the filename
	 */
	public DataManager( String filename ){

		try {
			reader = new SafeFitsReader( new CachedFitsReader( new RawFitsReader(filename, true), true,
					CacheFactory.Default.Instance()), true );
		} catch (FitsException | IOException e) {
			e.printStackTrace();
		}

		try {
			settings = new Settings( filename + ".snapshot" );
			x0 = settings.initInteger( "x0", 0 );
			y0 = settings.initInteger( "y0", 0 );
			z0 = settings.initInteger( "z0", 0 );
			z1 = settings.initInteger( "z1", 20 );
			curZ = settings.initInteger( "curZ", 0 );
			zoom = settings.initDouble( "zoom", 1 );
			
			x0.setValidRange( reader.getAxesSize()[0] );
			y0.setValidRange( reader.getAxesSize()[1] );
			z0.setValidRange( reader.getAxesSize()[2] );
			z1.setValidRange( reader.getAxesSize()[2] );
			curZ.setValidRange( reader.getAxesSize()[2] );
			
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

		this.csCont = new CoordinateSystemController( x0, y0, zoom );
		this.sel_box  = new SelectBoxDrawing( );
		this.sel_pnt  = new SelectPointDrawing( );

		
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
		
		//gui.monBuildTree.addMonitor( this, "buildContourTree" );

	}

	
	/**
	 * Update.
	 */
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
		
	}
	
	
	/**
	 * Sets the 2 D src refresh.
	 */
	public void set2DSrcRefresh( ){ System.out.println("set refresh"); need2DSrcRefresh = true; }
	
	/**
	 * Sets the 3 D src refresh.
	 */
	public void set3DSrcRefresh( ){ need3DSrcRefresh = true; }

	/**
	 * Sets the 2 D simplification refresh.
	 */
	public void set2DSimplificationRefresh( ){ need2DSimpSFRefresh = true; }
	
	/**
	 * Sets the 3 D simplification refresh.
	 */
	public void set3DSimplificationRefresh( ){ need3DSimpSFRefresh = true; }
	
	/**
	 * Sets the simplify scalar field.
	 */
	public void setSimplifyScalarField( ){
		psf_map.clear();
		needSimpRefresh = true;
	}

	/*
	public void buildContourTree( ){
		
		IntRange1D [] r = sel_box.getSelection();
		if( r == null ) return;
		
		sel_box.clearSelection();
		
		if( gui.monDim.get() == Dimension.DIM_2D ) {
			ContourTreeThread ctt = new ContourTreeThread( reader, r[0], r[1], curZ.get() );
			ctt.setCallback( this, "completedContourTree");
			ctt_map.put( curZ.get(), ctt );
			threadPool.submit( ctt );
		}
		if( gui.monDim.get() == Dimension.DIM_2D_STACK ) {
			for( int z = z0.get(); z <= z1.get(); z++ ){
				ContourTreeThread ctt = new ContourTreeThread( reader, r[0], r[1], z );
				ctt.setCallback( this, "completedContourTree");
				ctt_map.put( z, ctt );
				threadPool.submit( ctt );
			}
		}
		if( gui.monDim.get() == Dimension.DIM_3D ) {
			ContourTreeThread ctt = new ContourTreeThread( reader, r[0], r[1], new IntRange1D( z0.get(), z1.get() ) );
			ctt.setCallback( this, "completedContourTree");
			for( int z = z0.get(); z <= z1.get(); z++ ){
				ctt_map.put( z, ctt );
			}
			threadPool.submit( ctt );
		}

	}
	
	
	public void completedContourTree( ContourTreeThread ctt ){
		boolean stillProcessing = false;
		for( Entry<Integer,ContourTreeThread> e_ctt : ctt_map.entrySet() ){
			stillProcessing = stillProcessing || !e_ctt.getValue().isProcessingComplete();
		}
		
		if( !stillProcessing ){
			cur_ctt.set( ctt_map.containsKey( curZ.get() ) ? ctt_map.get( curZ.get() ) : null );;
			Set<PersistenceSet> pss = new HashSet<PersistenceSet>( );
			for( int z = z0.get(); z <= z1.get(); z++ ){
				if( ctt_map.containsKey(z) && ctt_map.get(z).getTree() != null ) 
					pss.add( ctt_map.get(z).getTree() );
			}
			pss.set( pss );
		}
	}
	 */

	public SingleScalarFieldView ssfv;
	
	private void refresh2DSourceSF( ){ 
		System.out.println("refresh2DSourceSF");

		csCont.setPosition( ssfv.getView().getPosition() );

		float [] xy0 = csCont.getCoordinateSystemPosition( 0, 0 );
		float [] xy1 = csCont.getCoordinateSystemPosition(  ssfv.getView().getWidth(),  ssfv.getView().getHeight() );
		IntRange1D xr = new IntRange1D( (int)xy0[0], (int)xy1[0] );
		IntRange1D yr = new IntRange1D( (int)xy0[1], (int)xy1[1] );
		
		// Update 2D scalar field
		try {
			src_sf2d.set( reader.getSlice( xr, yr, curZ.get(), 0) );
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			try{ 
				stack.addLayers( reader.getSlice( xr, yr, z, 0) );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		src_sf3d.set(stack);
		
	}

	
	private void refreshSimplification( ) {
		
		if( !ctt_map.containsKey( curZ.get() ) ) return;
		if( psf_map.containsKey( curZ.get() ) ) return;
		
		ContourTreeThread ctt = ctt_map.get( curZ.get() );
		if( ctt.getScalarField() == null || ctt.getTree() == null ) return;
		
		if( ctt.getScalarField() instanceof ScalarField2D ){
			PersistenceSimplifier2D psf = new PersistenceSimplifier2D( (ScalarField2D)ctt.getScalarField(), ctt.getTree(), ctt.getComponentList(), curZ.get(), false );
			psf.setCallback(this, "completedSimplification" );
			threadPool.execute( psf );
		}
		
		if( ctt.getScalarField() instanceof ScalarField3D ){
			PersistenceSimplifier3D psf = new PersistenceSimplifier3D( (ScalarField3D)ctt.getScalarField(), ctt.getTree(), ctt.getComponentList(), ctt.getZ(), false );
			psf.setCallback(this, "completedSimplification" );
			threadPool.execute( psf );
		}
		
		for( int z = z0.get(); z <= z1.get(); z++ ){
			if(  psf_map.containsKey(z) ) continue;
			if( !ctt_map.containsKey(z) ) continue;
			if( curZ.get() == z ) continue;
			ctt = ctt_map.get( z );
			if( ctt.getScalarField() == null || ctt.getTree() == null ) continue;
			if( ctt.getScalarField() instanceof ScalarField2D ){
				PersistenceSimplifier2D psf = new PersistenceSimplifier2D( (ScalarField2D)ctt.getScalarField(), ctt.getTree(), ctt.getComponentList(), z, false );
				psf.setCallback(this, "completedSimplification" );
				threadPool.execute( psf );
			}
		}
		
	}
	
	/**
	 * Completed simplification.
	 *
	 * @param pst the pst
	 */
	public void completedSimplification( PersistenceSimplifier2D pst ){
		psf_map.put( pst.getZ(), pst );
		need2DSimpSFRefresh = true;
		need3DSimpSFRefresh = true;
	}
	
	/**
	 * Completed simplification.
	 *
	 * @param pst the pst
	 */
	public void completedSimplification( PersistenceSimplifier3D pst ){
		for( int i = pst.getZ().start(); i <= pst.getZ().end(); i++ ){
			psf_map.put( i,  pst ); 
		}
		need2DSimpSFRefresh = true;
		need3DSimpSFRefresh = true;
	}
	
	

	private void refreshSimplifiedSF2D( ){ 
		
		cur_ctt.set( ctt_map.containsKey( curZ.get() ) ? ctt_map.get( curZ.get() ) : null );

		PersistenceSimplifierND psf = psf_map.containsKey( curZ.get() ) ? psf_map.get( curZ.get() ) : null;
		ContourTreeThread 		ctt = ctt_map.containsKey( curZ.get() ) ? ctt_map.get( curZ.get() ) : null;

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
			PersistenceSimplifierND psf = psf_map.containsKey( z ) ? psf_map.get( z ) : null;
			ContourTreeThread 		ctt = ctt_map.containsKey( z ) ? ctt_map.get( z ) : null;

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




	/**
	 * The Class MonitoredScalarField2D.
	 */
	public class MonitoredScalarField2D extends MonitoredObject<ScalarField2D>{
		@Override protected Class<?> getClassType() { return ScalarField2D.class; } 
	}
	
	/**
	 * The Class MonitoredLayeredVolume.
	 */
	public class MonitoredLayeredVolume extends MonitoredObject<LayeredVolume>{
		@Override protected Class<?> getClassType() { return LayeredVolume.class; } 
	};

	/**
	 * The Class MonitoredScalarField3D.
	 */
	public class MonitoredScalarField3D extends MonitoredObject<ScalarField3D>{
		@Override protected Class<?> getClassType() { return ScalarField3D.class; } 
	};	

	/**
	 * The Class MonitoredContourTreeThread.
	 */
	public class MonitoredContourTreeThread extends MonitoredObject< ContourTreeThread >{
		@Override protected Class<?> getClassType() { return ContourTreeThread.class; } 
	};


	
	
	
	

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String args[]) {
		AlmaTDADev.main(args);
	}


}
