package usf.saav.alma.app.views;

import java.util.Map.Entry;

import processing.core.PApplet;
import usf.saav.alma.algorithm.topology.PersistenceSet;
import usf.saav.alma.app.AlmaGui.MouseMode;
import usf.saav.alma.app.AlmaGui.ViewMode;
import usf.saav.alma.app.AlmaModel;
import usf.saav.alma.app.AlmaTDADev;
import usf.saav.alma.data.ScalarField2D;
import usf.saav.alma.data.ScalarField3D;
import usf.saav.alma.data.processors.Moment0;
import usf.saav.alma.data.processors.Moment1;
import usf.saav.alma.data.processors.Moment2;
import usf.saav.alma.drawing.ContourTreeDrawing;
import usf.saav.alma.drawing.HistogramDrawing;
import usf.saav.alma.drawing.LabelDrawing;
import usf.saav.alma.drawing.PersistenceDiagramDrawing;
import usf.saav.alma.drawing.ScalarFieldDrawing;
import usf.saav.alma.util.ContourTreeThread;
import usf.saav.common.colormap.DivergentColormap;
import usf.saav.common.monitor.MonitoredObject;
import usf.saav.common.mvc.ControllerComponent;
import usf.saav.common.mvc.PositionedComponent;
import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.range.FloatRange1D;

public class SingleScalarFieldView {

	public static boolean COLORMAP_GLOBAL = true;

	private ScalarFieldDrawing sfv;
	private ContourTreeDrawing ctv;
	private PersistenceDiagramDrawing pdd;
	private LabelDrawing sliceLabel;
	private LabelDrawing rangeLabel;
	private LabelDrawing ctLabel;
	private HistogramDrawing hist2d;
	private HistogramDrawing hist3d;

	private FloatRange1D sf_range = new FloatRange1D();
	private FloatRange1D m0_range = new FloatRange1D();
	private FloatRange1D m1_range = new FloatRange1D();
	private FloatRange1D m2_range = new FloatRange1D();

	private DivergentColormap colormap = new DivergentColormap.OrangePurple();

	private AlmaModel model;
	private AlmaTDADev mvc;

	private ViewMode viewmode = ViewMode.SCALARFIELD;

	private MonitoredObject<ScalarField2D> view_sf2d = new MonitoredObject<ScalarField2D>( ){
		@Override protected Class<?> getClassType() { return ScalarField2D.class; } 
	};
	
	private MonitoredObject<ScalarField3D> view_sf3d = new MonitoredObject<ScalarField3D>( ){
		@Override protected Class<?> getClassType() { return ScalarField3D.class; } 
	};
	


	public SingleScalarFieldView( AlmaTDADev _mvc, PApplet papplet, AlmaModel _model ){

		this.mvc   = _mvc;
		this.model = _model;

		sfv  = new ScalarFieldDrawing( papplet );
		pdd  = new PersistenceDiagramDrawing();
		ctv  = new ContourTreeDrawing( model.curZ );
		hist2d = new HistogramDrawing( 32 );		
		hist3d = new HistogramDrawing( 32 );

		////////////////////////////////////////////////
		//
		// Initialize Labels
		//
		ctLabel = new LabelDrawing.ComputingLabel() {
			@Override public void update( ){
				label = "Computing Contour Tree..." + getComputeString();
			}
		};

		sliceLabel = new LabelDrawing.BasicLabel() {
			@Override public void update( ){
				label = "Slice: " + model.curZ.get( );
			}
		};

		rangeLabel = new LabelDrawing.BasicLabel() {
			@Override public void update( ){
				label = "Range: " + model.z0.get() + "-" + model.z1.get();
			}
		};

	}

	private View view = null;
	public View getView( ){
		if( view == null ) view = new View();
		return view;
	}


	private Controller controller = null;
	public Controller getController( ){
		if( controller == null ) controller = new Controller(true);
		return controller;
	}

	public void disable( ){
		getView().disable();
		getController().disable();
	}

	public void enable( ){
		getView().enable();
		getController().enable();
	}


	public class View extends ViewComponent.Subview implements ViewComponent, PositionedComponent {

		public View( ){  }

		public void setup() {
			registerSubView( sfv,   	 10 );
			registerSubView( ctv,   	 20 );
			registerSubView( hist2d,	 27 );
			registerSubView( hist3d,	 28 );
			registerSubView( model.sel,  30 );
			registerSubView( pdd,   	 40 );
			registerSubView( ctLabel, 	 45 );
			registerSubView( sliceLabel, 55 );
			registerSubView( rangeLabel, 60 );

			super.setup();
		}

		@Override
		public void setPosition( int u0, int v0, int w, int h ){
			super.setPosition(u0, v0, w, h);

			sfv.setPosition( winX.start(),    winY.start(),    winX.length(), winY.length()   );
			ctv.setPosition( winX.start(),    winY.start(),    winX.length(), winY.length()   );
			model.sel.setPosition( winX.start(),    winY.start(),    winX.length(), winY.length()   );

			pdd.setPosition( winX.start()+10, winY.start()+10, 200,   200 );

			hist2d.setPosition( winX.start()+10, winY.start()+220, 200,   100 );
			hist3d.setPosition( winX.start()+10, winY.start()+340, 200,   100 );

			sliceLabel.setPosition( winX.start()+10, winY.end()-40, 20, 20 );
			rangeLabel.setPosition( winX.start()+10, winY.end()-40, 20, 20 );

			ctLabel.setPosition( winX.start()+10, winY.start()+10, 20, 20 );

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
		}

		boolean needUpdate = false;
		boolean needPDDUpdate = false;

		public void setup( ){
			
			registerSubController( pdd,   5 );
			registerSubController( model.csCont,		   15 );

			model.sel.setCoordinateSystem( model.csCont );
			ctv.setCoordinateSystem( model.csCont );

			model.csCont.addTranslationCallback( sfv, "setTranslation" );
			//model.csCont.addTranslationCallback( ctv, "setTranslation" );

			model.simp_sf2d.addMonitor( this, "simp_sf2d_update" );
			model.simp_sf3d.addMonitor( this, "simp_sf3d_update" );

			model.gui.monMM.addMonitor( this, "setMouseMode" );
			model.gui.monView.addMonitor( this, "setViewMode" );
			model.gui.monShowSimp.addMonitor( this, "setViewRefresh" );
			model.gui.monShowTree.addMonitor( ctv, "setEnabled" );
			
			view_sf2d.addMonitor( hist2d, "setData" );
			view_sf2d.addMonitor( sfv, "setScalarField" );
			
			view_sf3d.addMonitor( hist3d, "setData" );
			
			model.pss.addMonitor( this,  "need_pdd_update" );
			
			model.cur_ctt.addMonitor( this, "contourTreeUpdate" );

			pdd.addPersistentSimplificationCallback( mvc.controller, "setSimplifyScalarField" );
			
			super.setup();

		}
		
		public void setViewRefresh( ){
			needUpdate = true;
		}
		
		public void simp_sf2d_update( ){
			needUpdate = needUpdate || (viewmode == ViewMode.SCALARFIELD);
		}
		
		public void simp_sf3d_update( ){
			needUpdate = needUpdate || (viewmode == ViewMode.MOMENT0);
			needUpdate = needUpdate || (viewmode == ViewMode.MOMENT1);
			needUpdate = needUpdate || (viewmode == ViewMode.MOMENT2);
		}

		public void setViewMode( ViewMode vm ){
			viewmode = vm;
			needUpdate = true;
		}
		
		public void need_pdd_update() {
			needPDDUpdate = true;
		}
		
		public void setMouseMode( MouseMode mm ){
			unregisterSubController( model.csCont );
			unregisterSubController( model.sel );
			switch(mm){
			case NAVIGATE: registerSubController(model.csCont, 10 ); break;
			case SELECT: registerSubController( model.sel, 10 ); break;
			}
		}
		
		


		public void update() {
			if( !isEnabled() ) return;

			if( viewmode == ViewMode.SCALARFIELD ){
				rangeLabel.disable();
				sliceLabel.enable();
			}
			else {
				rangeLabel.enable();
				sliceLabel.disable();
			}
			
			
			boolean showCTLabel = false;
			for( Entry<Integer,ContourTreeThread> ctt : model.ctt_map.entrySet() ){
				showCTLabel = showCTLabel || !ctt.getValue().isProcessingComplete();
			}
			ctLabel.setEnabled( showCTLabel );


			ctv.setEnabled( model.gui.monShowTree.get() );

			setMouseMode( model.gui.monMM.get() );
			
			if( needUpdate ) refreshViewSF( );
			if( needPDDUpdate) pdd.setParameterizations( model.cur_ctt.get().getTree(), model.pss.get().toArray( new PersistenceSet[model.pss.get().size()] ) );

			needUpdate = false;
			needPDDUpdate = false;


			super.update();
		}


		public void contourTreeUpdate( ){
			ContourTreeThread ctt = model.cur_ctt.get();
			if( ctt != null ){
				ctv.setRegion( ctt.getX(), ctt.getY() );
				ctv.setField( ctt.getScalarField(), ctt.getTree(), ctt.getComponentList(), ctt.getZ() );
			}
			else{
				ctv.setRegion( null, null );
				ctv.setField( null, null, null, null );
			}
		}


		@SuppressWarnings("incomplete-switch")
		private void refreshViewSF( ){ 
			boolean showSimplified = model.gui.monShowSimp.get();

			ScalarField2D sf2D = ((showSimplified)?(model.simp_sf2d):(model.src_sf2d)).get();
			ScalarField3D sf3D = ((showSimplified)?(model.simp_sf3d):(model.src_sf3d)).get();

			switch(viewmode){
			case SCALARFIELD: view_sf2d.set( sf2D );			    break;
			case MOMENT0: 	  view_sf2d.set( new Moment0( sf3D ) ); break;
			case MOMENT1: 	  view_sf2d.set( new Moment1( sf3D ) ); break;
			case MOMENT2: 	  view_sf2d.set( new Moment2( sf3D ) ); break;
			default: break;
			}
			view_sf3d.set( sf3D );



			// update the color maps
			if( COLORMAP_GLOBAL ){
				double [] r = view_sf2d.get().getValueRange();
				FloatRange1D selRange = null;
				
				switch(viewmode){
					case MOMENT0:	  selRange = m0_range; break;
					case MOMENT1:	  selRange = m1_range; break;
					case MOMENT2:	  selRange = m2_range; break;
					case SCALARFIELD: selRange = sf_range; break;
				}
				selRange.expand( r );
				colormap.setRange( selRange );
			}
			else{
				colormap.setRange( new FloatRange1D( view_sf2d.get().getValueRange() ) );
			}

		}


		@Override
		public boolean keyPressed( char key ){
			if( super.keyPressed(key) ) return true;
			return false;
		}



	}
}
