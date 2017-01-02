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
package usf.saav.alma.app.views;

import java.util.Map.Entry;

import usf.saav.alma.app.DataViewManager;
import usf.saav.alma.app.views.AlmaGui.MouseMode;
import usf.saav.alma.app.views.AlmaGui.ViewMode;
import usf.saav.alma.data.processors.Extract1Dfrom3D;
import usf.saav.alma.data.processors.Moment0;
import usf.saav.alma.data.processors.Moment1;
import usf.saav.alma.data.processors.Moment2;
import usf.saav.alma.data.processors.Subsample2D;
import usf.saav.alma.data.processors.Subsample3D;
import usf.saav.alma.drawing.ContourTreeDrawing;
import usf.saav.alma.drawing.HistogramDrawing;
import usf.saav.alma.drawing.LabelDrawing;
import usf.saav.alma.drawing.PersistenceDiagramDrawing;
import usf.saav.alma.drawing.ScalarFieldDrawing;
import usf.saav.alma.drawing.SpectralLineDrawing;
import usf.saav.alma.util.ContourTreeThread;
import usf.saav.common.MathXv1;
import usf.saav.common.colormap.DivergentColormap;
import usf.saav.common.monitor.MonitoredObject;
import usf.saav.common.mvc.ControllerComponent;
import usf.saav.common.mvc.DefaultGLFrame;
import usf.saav.common.mvc.PositionedComponent;
import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.range.FloatRange1D;
import usf.saav.scalarfield.ScalarField2D;
import usf.saav.scalarfield.ScalarField3D;
import usf.saav.scalarfield.ScalarFieldND;
import usf.saav.topology.TopoTree;

// TODO: Auto-generated Javadoc
/**
 * The Class SingleScalarFieldView.
 */
public class SingleScalarFieldView extends DefaultGLFrame {
	private static final long serialVersionUID = 7078442093905364332L;

	public static boolean COLORMAP_GLOBAL = true;

	private ScalarFieldDrawing sfv;
	private ContourTreeDrawing ctv;
	private PersistenceDiagramDrawing pdd;
	private LabelDrawing sliceLabel;
	private LabelDrawing rangeLabel;
	private LabelDrawing ctLabel;
	private HistogramDrawing hist2d;
	private HistogramDrawing hist3d;
	private SpectralLineDrawing lineD;

	private FloatRange1D sf_range = new FloatRange1D();
	private FloatRange1D m0_range = new FloatRange1D();
	private FloatRange1D m1_range = new FloatRange1D();
	private FloatRange1D m2_range = new FloatRange1D();

	private DivergentColormap colormap = new DivergentColormap.OrangePurple();

	
	private DataViewManager dataM;

	private ViewMode viewmode = ViewMode.SCALARFIELD;

	private MonitoredObject<ScalarField2D> view_sf2d = new MonitoredObject<ScalarField2D>( ){
		@Override protected Class<?> getClassType() { return ScalarField2D.class; } 
	};
	
	private MonitoredObject<ScalarField3D> view_sf3d = new MonitoredObject<ScalarField3D>( ){
		@Override protected Class<?> getClassType() { return ScalarField3D.class; } 
	};
	
	
	AlmaGui gui;


	/**
	 * Instantiates a new single scalar field view.
	 *
	 * @param gui the gui
	 * @param title the title
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 */
	public SingleScalarFieldView( AlmaGui gui, String title, int x, int y, int width, int height ){
		super(title,x,y,width,height);
		
		this.gui = gui;
		
		view = new View();
		controller = new Controller(true);
		
		sfv  = new ScalarFieldDrawing( this.graphics );
		pdd  = new PersistenceDiagramDrawing();

		hist2d = new HistogramDrawing( 32 );		
		hist3d = new HistogramDrawing( 32 );
		lineD  = new SpectralLineDrawing( );

		////////////////////////////////////////////////
		//
		// Initialize Labels
		//
		ctLabel = new LabelDrawing.ComputingLabel() {
			@Override public void update( ){
				label = "Computing Contour Tree..." + getComputeString();
			}
		};

	}

	


	public void setData( DataViewManager _dataM ) {

		dataM = _dataM;
		dataM.ssfv = this;

		ctv  = new ContourTreeDrawing( dataM.curZ );

		sliceLabel = new LabelDrawing.BasicLabel() {
			@Override public void update( ){
				label = "Slice: " + dataM.curZ.get( );
			}
		};

		rangeLabel = new LabelDrawing.BasicLabel() {
			@Override public void update( ){
				label = "Range: " + dataM.z0.get() + "-" + dataM.z1.get();
			}
		};
		
		((View)getView()).setData(dataM);
		((Controller)getController()).setData(dataM);
		
	}

	
	

	@Override
	protected void update() {
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#disable()
	 */
	public void disable( ){
		getView().disable();
		getController().disable();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#enable()
	 */
	public void enable( ){
		getView().enable();
		getController().enable();
	}


	/**
	 * The Class View.
	 */
	public class View extends ViewComponent.Subview implements ViewComponent, PositionedComponent {

		/**
		 * Instantiates a new view.
		 */
		public View( ){  }

		public void setData( DataViewManager dataM ){
			this.unregisterAll( );
			registerSubView( sfv,   		 10 );
			registerSubView( ctv,   		 20 );
			registerSubView( hist2d,		 27 );
			registerSubView( hist3d,		 28 );
			registerSubView( lineD,			 29 );
			registerSubView( dataM.sel_box,  30 );
			registerSubView( dataM.sel_pnt,  31 );
			registerSubView( pdd,   		 40 );
			registerSubView( ctLabel,	 	 45 );
			registerSubView( sliceLabel,	 55 );
			registerSubView( rangeLabel,	 60 );
			
		}
		

		/* (non-Javadoc)
		 * @see usf.saav.common.mvc.PositionedComponent.Default#setPosition(int, int, int, int)
		 */
		@Override
		public void setPosition( int u0, int v0, int w, int h ){
			super.setPosition(u0, v0, w, h);
			
			//int panelSize = Math.min( 400,  h/4 );
			int panelSize = h/3;
			
			sfv.setPosition( winX.start(),    winY.start(),    winX.length(), winY.length()   );
			ctv.setPosition( winX.start(),    winY.start(),    winX.length(), winY.length()   );
			
			dataM.sel_box.setPosition( winX.start(),    winY.start(),    winX.length(), winY.length()   );
			dataM.sel_pnt.setPosition( winX.start(),    winY.start(),    winX.length(), winY.length()   );

			pdd.setPosition(    winX.start()+10, winY.start()+10, 			  panelSize, panelSize );
			hist2d.setPosition( winX.start()+10, winY.start()+panelSize+20,   panelSize, panelSize/2 );
			hist3d.setPosition( winX.start()+10, winY.start()+panelSize*3/2+40, panelSize, panelSize/2 );
			
			lineD.setPosition( winX.end()-panelSize*3/2-20,   winY.start()+panelSize+20, panelSize*3/2, panelSize );

			sliceLabel.setPosition( winX.start()+10, winY.end()-40, 20, 20 );
			rangeLabel.setPosition( winX.start()+10, winY.end()-40, 20, 20 );

			ctLabel.setPosition( winX.start()+10, winY.start()+10, 20, 20 );

		}


		/* (non-Javadoc)
		 * @see usf.saav.common.mvc.ViewComponent.Subview#update()
		 */
		public void update() {
			dataM.update();
			
			sfv.setColormap( colormap );

			if( pdd != null && ctv != null )
				ctv.setSelected( pdd.getSelected() );
			
			super.update();
		}
	}


	/**
	 * The Class Controller.
	 */
	public class Controller  extends ControllerComponent.Subcontroller implements ControllerComponent {


		/**
		 * Instantiates a new controller.
		 *
		 * @param verbose the verbose
		 */
		public Controller( boolean verbose ) {
			super(verbose);
		}

		boolean needUpdate = false;
		boolean needPDDUpdate = false;

		/* (non-Javadoc)
		 * @see usf.saav.common.mvc.ControllerComponent.Subcontroller#setup()
		 */
		public void setup( ){
			
			gui.monMM.addMonitor( this, "setMouseMode" );
			gui.monView.addMonitor( this, "setViewMode" );
			gui.monShowSimp.addMonitor( this, "setViewRefresh" );
			gui.monShowTree.addMonitor( ctv, "setEnabled" );
			
			view_sf2d.addMonitor( hist2d, "setData" );
			view_sf2d.addMonitor( sfv, "setScalarField" );
			
			view_sf3d.addMonitor( hist3d, "setData" );

			//pdd.addPersistentSimplificationCallback( dataM.psm, "refreshSimplification" );
			pdd.addPersistentSimplificationCallback( dataM, "setSimplifyScalarField" );
			

			super.setup();

		}
		
		public void setData( DataViewManager dataM ){
			
			unregisterAll( );
			registerSubController( pdd, 		  5 );
			registerSubController( dataM.csCont, 15 );

			dataM.sel_box.setCoordinateSystem( dataM.csCont );
			dataM.sel_pnt.setCoordinateSystem( dataM.csCont );
			ctv.setCoordinateSystem( dataM.csCont );

			dataM.csCont.addDragCallback( sfv, "setTranslation" );
			dataM.csCont.addZoomCallback( sfv, "adjustZoom" );
			//dataM.csCont.addTranslationCallback( ctv, "setTranslation" );

			dataM.simp_sf2d.addMonitor( this, "simp_sf2d_update" );
			dataM.simp_sf3d.addMonitor( this, "simp_sf3d_update" );


			dataM.ctm.pss.addMonitor( this,  "need_pdd_update" );
			
			dataM.ctm.cur_ctt.addMonitor( this, "contourTreeUpdate" );


			dataM.sel_pnt.addMonitor( this, "single_line_update" );

		}
		
		/**
		 * Single line update.
		 *
		 * @param _p the p
		 */
		public void single_line_update( int [] _p ){
			float [] p = dataM.csCont.getWindowPosition( _p[0], _p[1] );
			lineD.setData( new Extract1Dfrom3D( view_sf3d.get(), (int)p[0], (int)p[1] ) );
		}

		
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
			needUpdate = needUpdate || (viewmode == ViewMode.SCALARFIELD);
		}
		
		/**
		 * Simp sf 3 d update.
		 */
		public void simp_sf3d_update( ){
			needUpdate = needUpdate || (viewmode == ViewMode.MOMENT0);
			needUpdate = needUpdate || (viewmode == ViewMode.MOMENT1);
			needUpdate = needUpdate || (viewmode == ViewMode.MOMENT2);
		}

		/**
		 * Sets the view mode.
		 *
		 * @param vm the new view mode
		 */
		public void setViewMode( ViewMode vm ){
			viewmode = vm;
			needUpdate = true;
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
		public void setMouseMode( MouseMode mm ){
			unregisterSubController( dataM.csCont );
			unregisterSubController( dataM.sel_box );
			unregisterSubController( dataM.sel_pnt );
			switch(mm){
				case NAVIGATE:		registerSubController( dataM.csCont,  10 ); break;
				case SELECT_REGION: registerSubController( dataM.sel_box, 10 ); break;
				case SELECT_LINE:	registerSubController( dataM.sel_pnt, 10 ); break;
			}
		}
		

		/* (non-Javadoc)
		 * @see usf.saav.common.mvc.ControllerComponent.Subcontroller#update()
		 */
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
			for( Entry<Integer,ContourTreeThread> ctt : dataM.ctm.ctt_map.entrySet() ){
				showCTLabel = showCTLabel || !ctt.getValue().isProcessingComplete();
			}
			ctLabel.setEnabled( showCTLabel );


			//ctv.setEnabled( model.gui.monShowTree.get() );

			//setMouseMode( model.gui.monMM.get() );
			
			if( needUpdate ) refreshViewSF( );
			if( needPDDUpdate){
				pdd.setParameterizations( dataM.ctm.cur_ctt.get().getTree(), dataM.ctm.pss.get().toArray( new TopoTree[dataM.ctm.pss.get().size()] ) );
			}

			needUpdate = false;
			needPDDUpdate = false;


			super.update();
		}

		/**
		 * Contour tree update.
		 */
		public void contourTreeUpdate( ){
			ContourTreeThread ctt = dataM.ctm.cur_ctt.get();
			if( ctt != null ){
				ctv.setRegion( ctt.getX(), ctt.getY() );
				ctv.setField( ctt.getScalarField(), ctt.getTree(), ctt.getComponentList(), ctt.getZ() );
			}
			else{
				ctv.setRegion( null, null );
				ctv.setField( null, null, null, null );
			}
		}
		


		private void refreshViewSF( ){ 
			System.out.println("refresh");
			boolean showSimplified = gui.monShowSimp.get();
			//boolean showSimplified = true;

			ScalarField2D _sf2D = ((showSimplified)?(dataM.simp_sf2d):(dataM.src_sf2d)).get();
			ScalarField3D _sf3D = ((showSimplified)?(dataM.simp_sf3d):(dataM.src_sf3d)).get();
 
			int stepX = (int)MathXv1.nextLargerPowerOf2( 2.0 * (double)_sf2D.getWidth()  / (double)winX.length() );
			int stepY = (int)MathXv1.nextLargerPowerOf2( 2.0 * (double)_sf2D.getHeight() / (double)winY.length() );
			int stepZ = (int)Math.ceil( (float)_sf3D.getDepth()/128.0 );

			ScalarField2D red2D = new Subsample2D( _sf2D, stepX, stepY );
			ScalarField3D red3D = new Subsample3D( _sf3D, stepX, stepY, stepZ );

			switch(viewmode){
			case SCALARFIELD: view_sf2d.set( red2D );			    break;
			case MOMENT0: 	  view_sf2d.set( new Moment0( red3D ) ); break;
			case MOMENT1: 	  view_sf2d.set( new Moment1( red3D ) ); break;
			case MOMENT2: 	  view_sf2d.set( new Moment2( red3D ) ); break;
			default: break;
			}
			view_sf3d.set( red3D );

			// update the color maps
			double [] r = ScalarFieldND.Default.getValueRange( view_sf2d.get() );
			//System.out.println( r[0] + " " + r[1] );
			if( COLORMAP_GLOBAL ){
				FloatRange1D selRange = null;
				
				switch(viewmode){
					case MOMENT0:	  selRange = m0_range; break;
					case MOMENT1:	  selRange = m1_range; break;
					case MOMENT2:	  selRange = m2_range; break;
					case SCALARFIELD: selRange = sf_range; break;
				}
				if( r[1] > r[0] ) selRange.expand( r );
				selRange.expand( 0 );
				colormap.setRange( selRange );
			} 
			else{
				if( r[1] > r[0] ) colormap.setRange( new FloatRange1D( r ) );
			}
		}


		/* (non-Javadoc)
		 * @see usf.saav.common.mvc.ControllerComponent.Subcontroller#keyPressed(char)
		 */
		@Override
		public boolean keyPressed( char key ){
			if( super.keyPressed(key) ) return true;
			if( key == '-' ){ dataM.curZ.decr(); return true; }
			if( key == '+' ){ dataM.curZ.incr(); return true; }
			return false;
		}
	}



}
