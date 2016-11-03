package usf.saav.alma.app.views;

import usf.saav.alma.app.DataViewManager;
import usf.saav.alma.data.ScalarField3D;
import usf.saav.alma.drawing.LabelDrawing;
import usf.saav.alma.drawing.LabelDrawing.BasicLabel;
import usf.saav.alma.drawing.VolumeRendering.InteractiveTF1D;
import usf.saav.alma.drawing.VolumeRendering.VolumeRenderer;
import usf.saav.common.jocl.joclController;
import usf.saav.common.monitor.MonitoredObject;
import usf.saav.common.mvc.ControllerComponent;
import usf.saav.common.mvc.DefaultGLFrame;
import usf.saav.common.mvc.PositionedComponent;
import usf.saav.common.mvc.ViewComponent;

public class VolumeRenderingView extends DefaultGLFrame {

	private static final long serialVersionUID = -4977266239265456457L;

	private DataViewManager dataM;
	private BasicLabel rangeLabel;

	VolumeRenderer vr;
	InteractiveTF1D tf1d;
	AlmaGui gui;

	private MonitoredObject<ScalarField3D> view_sf3d = new MonitoredObject<ScalarField3D>( ){
		@Override protected Class<?> getClassType() { return ScalarField3D.class; } 
	};

	public VolumeRenderingView( AlmaGui gui, joclController jocl, String title, int x, int y, int width, int height ){
		super(title,x,y,width,height);

		this.gui = gui;
		//this.model = _model;

		vr   = new VolumeRenderer( graphics, jocl, 512 );
		tf1d = new InteractiveTF1D( );
		vr.setTransferFunction( tf1d );

		view = new View();
		controller = new Controller(true);
	}
	
	public void setData( DataViewManager _dataM ){

		dataM = _dataM;

		rangeLabel = new LabelDrawing.BasicLabel() {
			@Override
			public void update( ){
				label = "Range: " + dataM.z0.get() + "-" + dataM.z1.get();
			}
		};
		
		((View)getView()).setData(dataM);
		((Controller)getController()).setData(dataM);
	}



	@Override protected void update() { }
	
	public void disable( ){
		getView().disable();
		getController().disable();
		rangeLabel.disable();
	}

	public void enable( ){
		getView().enable();
		getController().enable();
		rangeLabel.enable();
	}


	public class View extends ViewComponent.Subview implements ViewComponent, PositionedComponent {

		public void setup() {
			registerSubView( vr,     25 );
			registerSubView( tf1d,   26 );
			registerSubView( rangeLabel, 60 );

			super.setup();
		}

		public void setData(DataViewManager dataM) { }

		@Override
		public void setPosition( int u0, int v0, int w, int h ){
			super.setPosition(u0, v0, w, h);

			int tf_h    = 150;
			int vr_size = winY.length()-tf_h;
			
			vr.setPosition(   winX.start()+winX.length()/2-vr_size/2, winY.start(),    vr_size, vr_size );
			tf1d.setPosition( winX.start()+winX.length()/2-vr_size/2, winY.end()-tf_h, vr_size, tf_h );
			rangeLabel.setPosition( winX.start()+10, winY.end()-40, 20, 20 );

		}
	}


	public class Controller extends ControllerComponent.Subcontroller implements ControllerComponent {
		
		private boolean needUpdate = true;
		
		public Controller( boolean verbose ) {
			super(verbose);
		}

		public void setup( ){
			
			registerSubController( tf1d, 15 );

			gui.monShowSimp.addMonitor( this, "simp_sf3d_update" );

			view_sf3d.addMonitor( vr, "setVolume" );
			view_sf3d.addMonitor( tf1d, "setData" );
			
			super.setup();
		}
		
		public void setData(DataViewManager dataM) {
			dataM.simp_sf3d.addMonitor( this, "simp_sf3d_update" );
		}

				
		public void simp_sf3d_update( ){
			needUpdate = true;
		}
		
		public void update() {
			if( !isEnabled() ) return;
			if( needUpdate ) refreshViewSF( );
			needUpdate = false;
			super.update();
		}

		private void refreshViewSF( ){ 
			boolean showSimplified = gui.monShowSimp.get();
			ScalarField3D sf3D = ((showSimplified)?(dataM.simp_sf3d):(dataM.src_sf3d)).get();
			view_sf3d.set( sf3D );
		}
	}

}
