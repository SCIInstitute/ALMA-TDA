package usf.saav.alma.app;

import java.io.IOException;

import usf.saav.alma.app.InteractiveViewer.MouseMode;
import usf.saav.alma.data.processors.Subsample2D;
import usf.saav.alma.data.processors.Subset2D;
import usf.saav.alma.drawing.ContourTreeDrawing;
import usf.saav.alma.drawing.HistogramDrawing;
import usf.saav.alma.drawing.LabelDrawing;
import usf.saav.alma.drawing.PersistenceDiagramDrawing;
import usf.saav.alma.drawing.ScalarFieldDrawing;
import usf.saav.alma.drawing.SelectBoxDrawing;
import usf.saav.alma.util.CoordinateSystemController;
import usf.saav.common.MathXv1;
import usf.saav.common.colormap.DivergentColormap;
import usf.saav.common.monitor.MonitoredObject;
import usf.saav.common.mvc.ControllerComponent;
import usf.saav.common.mvc.DefaultGLPanel;
import usf.saav.common.mvc.PositionedComponent;
import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.range.FloatRange1D;
import usf.saav.common.range.IntRange1D;
import usf.saav.scalarfield.ScalarField2D;
import usf.saav.scalarfield.ScalarFieldND;

public class AlmaGL extends DefaultGLPanel {
	private static final long serialVersionUID = 843187254338264292L;

	public SelectBoxDrawing   sel_box = new SelectBoxDrawing();

	public CoordinateSystemController csCont;

	private ScalarFieldDrawing sfv;
	private ContourTreeDrawing ctv;
	private PersistenceDiagramDrawing pdd;
	private LabelDrawing ctLabel;
	private HistogramDrawing hist2d;
	
	private FloatRange1D sf_range = new FloatRange1D();
	
	private DivergentColormap colormap = new DivergentColormap.OrangePurple();

	InteractiveViewer parent;
	
	public static boolean COLORMAP_GLOBAL = false;

	ScalarField2D currSlice;

	MonitoredObject<ScalarField2D> view_sf2d = new MonitoredObject<ScalarField2D>( ){
		@Override protected Class<?> getClassType() { return ScalarField2D.class; } 
	};
	


	
	AlmaGL( InteractiveViewer _parent, int width, int height ){
		super(width, height);
		
		parent = _parent;
		
		view       = new View();
		controller = new Controller(true);
		
		sfv     = new ScalarFieldDrawing( this.graphics );
		pdd     = new PersistenceDiagramDrawing();
		ctv     = new ContourTreeDrawing( parent.monZ );
		csCont  = new CoordinateSystemController( parent.monX, parent.monY, parent.monZoom );
		hist2d  = new HistogramDrawing( 32 );		
		ctLabel = new LabelDrawing.ComputingLabel() {
			@Override public void update( ){
				label = "Computing Contour Tree..." + getComputeString();
			}
		};
		
		parent.monZ.addMonitor(this, "updateSlice" );
		updateSlice();

	}

	@Override protected void update() { }

	
	public void updateSlice( ){
		System.out.println("update slice");
		try {
			currSlice = parent.fits.getSlice(parent.monZ.get(), 0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		view_sf2d.set( new Subsample2D(currSlice, 4) );
	}
	
	
	

	public class View extends ViewComponent.Subview implements ViewComponent, PositionedComponent {

		public View( ){  }

		public void setup( ){
			this.unregisterAll( );
			registerSubView( sfv,   	10 );
			registerSubView( ctv,   	20 );
			registerSubView( hist2d,	27 );
			registerSubView( sel_box,	30 );
			registerSubView( pdd,   	40 );
			registerSubView( ctLabel,	45 );
		}
		
		@Override
		public void setPosition( int u0, int v0, int w, int h ){
			super.setPosition(u0, v0, w, h);
			
			int panelSize = h/3;
			
			csCont.setPosition(	 winX.start(), winY.start(), winX.length(), winY.length() );
			sfv.setPosition(	 winX.start(), winY.start(), winX.length(), winY.length() );
			ctv.setPosition(	 winX.start(), winY.start(), winX.length(), winY.length() );
			sel_box.setPosition( winX.start(), winY.start(), winX.length(), winY.length() );
			pdd.setPosition(   	 winX.start()+10, winY.start()+10, panelSize, panelSize );
			ctLabel.setPosition( winX.start()+10, winY.start()+10, 20, 20 );
			hist2d.setPosition(	 winX.start()+10, winY.end()-panelSize/2-20, panelSize, panelSize/2 );
		}

		public void update() {
			sfv.setColormap( colormap );

			if( pdd != null && ctv != null )
				ctv.setSelected( pdd.getSelected() );
			
			super.update();
		}
	}


	public class Controller  extends ControllerComponent.Subcontroller implements ControllerComponent {
		public Controller( boolean verbose ) {
			super(verbose);
			
			System.out.println(this.isEnabled());
		}

		@Override
		public void setup( ){
			unregisterAll( );
			registerSubController( pdd,		5 );
			registerSubController( csCont, 15 );
			
			view_sf2d.addMonitor( hist2d, "setData" );
			view_sf2d.addMonitor( sfv,    "setScalarField" );

			csCont.addDragCallback( sfv, "setTranslation" );
			csCont.addZoomCallback( sfv, "adjustZoom" );

			parent.monX.addMonitor(    this, "setUpdateSF" );
			parent.monY.addMonitor(    this, "setUpdateSF" );
			parent.monZ.addMonitor(    this, "setUpdateSF" );
			parent.monZoom.addMonitor( this, "setUpdateSF" );
			parent.monMM.addMonitor(   this, "setMouseMode" );

			super.setup();
		}
		
		@Override
		public boolean keyPressed( char key ){
			if( super.keyPressed(key) ) return true;
			if( key == '-' ){ parent.monZ.decr(); return true; }
			if( key == '+' ){ parent.monZ.incr(); return true; }
			return false;
		}
		
		boolean sf_needs_update = true;
		public void setUpdateSF(){ sf_needs_update = true; }
			
		public void update() {
			if( !isEnabled() ) return;
			
			if( sf_needs_update ){
				updateSF();
			}

			super.update();
		}
		
		private void updateSF(){
			if( currSlice == null ) return;
			
			float [] xy0 = csCont.getCoordinateSystemPosition( 0, 0 );
			float [] xy1 = csCont.getCoordinateSystemPosition(  getView().getWidth(),  getView().getHeight() );
			IntRange1D xr = new IntRange1D( (int)xy0[0], (int)xy1[0] );
			IntRange1D yr = new IntRange1D( (int)xy0[1], (int)xy1[1] );

			ScalarField2D _sf2D = new Subset2D(currSlice,xr,yr);
 
			int stepX = (int)MathXv1.nextLargerPowerOf2( 2.0 * (double)_sf2D.getWidth()  / (double)winX.length() );
			int stepY = (int)MathXv1.nextLargerPowerOf2( 2.0 * (double)_sf2D.getHeight() / (double)winY.length() );
			
			view_sf2d.set( new Subsample2D( _sf2D, stepX, stepY ) );

			// update the color maps
			double [] r = ScalarFieldND.Default.getValueRange( view_sf2d.get() );
			if( COLORMAP_GLOBAL ){
				FloatRange1D selRange = sf_range;
				if( r[1] > r[0] ) selRange.expand( r );
				selRange.expand( 0 );
				colormap.setRange( selRange );
			} 
			else{
				if( r[1] > r[0] ) colormap.setRange( new FloatRange1D( r ) );
			}
			sf_needs_update = false;
		}
		
		public void setMouseMode( MouseMode mm ){
			unregisterSubController( csCont );
			unregisterSubController( sel_box );
			switch(mm){
				case NAVIGATE:		registerSubController( csCont,  10 ); break;
				case SELECT_REGION: registerSubController( sel_box, 10 ); break;
			}
		}
		
	}
	
	/**
	 * The Class Controller.
	 */
	public class ControllerOLD  extends ControllerComponent.Subcontroller implements ControllerComponent {


		/**
		 * Instantiates a new controller.
		 *
		 * @param verbose the verbose
		 */
		public ControllerOLD( boolean verbose ) {
			super(verbose);
		}

		boolean needUpdate = false;
		boolean needPDDUpdate = false;

		/* (non-Javadoc)
		 * @see usf.saav.common.mvc.ControllerComponent.Subcontroller#setup()
		 */
		public void setup( ){


			
			parent.monShowSimp.addMonitor( this, "setViewRefresh" );
			parent.monShowTree.addMonitor( ctv, "setEnabled" );
			
			

			//pdd.addPersistentSimplificationCallback( dataM.psm, "refreshSimplification" );
			pdd.addPersistentSimplificationCallback( this, "setSimplifyScalarField" );

			
			sel_box.setCoordinateSystem( csCont );
			ctv.setCoordinateSystem( csCont );
			
			


			//simp_sf2d.addMonitor( this, "simp_sf2d_update" );


			//dataM.ctm.pss.addMonitor( this,  "need_pdd_update" );
			
			//dataM.ctm.cur_ctt.addMonitor( this, "contourTreeUpdate" );
			super.setup();


		}
		
		void translate(){ System.out.println("translate"); }
		void zoom(){ System.out.println("zoom"); }
		
		/**
		 * Sets the view refresh.
		 */
		public void setViewRefresh( ){
			needUpdate = true;
		}
		
		/**
		 * Simp sf 2 d update.
		 */
		public void simp_sf2d_update( ){
			
		}

		
		/**
		 * Need pdd update.
		 */
		public void need_pdd_update() {
			needPDDUpdate = true;
		}
		
		/**
		 * Sets the mouse mode.
		 *
		 * @param mm the new mouse mode
		 */

		

		/* (non-Javadoc)
		 * @see usf.saav.common.mvc.ControllerComponent.Subcontroller#update()
		 */
		public void update() {
			if( !isEnabled() ) return;
			
		
			//view_sf2d.set( new Subsample2D(currSlice, 4) );
			refreshViewSF( );

			/*
			boolean showCTLabel = false;
			for( Entry<Integer,ContourTreeThread> ctt : dataM.ctm.ctt_map.entrySet() ){
				showCTLabel = showCTLabel || !ctt.getValue().isProcessingComplete();
			}
			ctLabel.setEnabled( showCTLabel );
			 */

			if( needUpdate ) refreshViewSF( );
			//if( needPDDUpdate){
			//	pdd.setParameterizations( dataM.ctm.cur_ctt.get().getTree(), dataM.ctm.pss.get().toArray( new TopoTree[dataM.ctm.pss.get().size()] ) );
			//}

			needUpdate = false;
			needPDDUpdate = false;


			super.update();
		}

		/**
		 * Contour tree update.
		 */
		public void contourTreeUpdate( ){
			/*
			ContourTreeThread ctt = dataM.ctm.cur_ctt.get();
			if( ctt != null ){
				ctv.setRegion( ctt.getX(), ctt.getY() );
				ctv.setField( ctt.getScalarField(), ctt.getTree(), ctt.getComponentList(), ctt.getZ() );
			}
			else{
				ctv.setRegion( null, null );
				ctv.setField( null, null, null, null );
			}
			*/
		}
		


		private void refreshViewSF( ){ 
			//System.out.println("refresh");
			//boolean showSimplified = monShowSimp.get();
			//boolean showSimplified = true;

			
		}


	}


}
