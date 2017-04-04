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

import usf.saav.alma.app.TDAInteractive.MouseMode;
import usf.saav.alma.data.processors.Composite2D;
import usf.saav.alma.data.processors.LayeredVolume;
import usf.saav.alma.data.processors.Moment0;
import usf.saav.alma.data.processors.Subsample2D;
import usf.saav.alma.data.processors.Subset2D;
import usf.saav.alma.drawing.HistogramDrawing;
import usf.saav.alma.drawing.ScalarFieldDrawing;
import usf.saav.alma.drawing.SelectBoxDrawing;
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



	private SelectBoxDrawing		  sel_box;
	private ScalarFieldDrawing		  sfv;
	private HistogramDrawing		  hist2d;
	
	private FloatRange1D sf_range = new FloatRange1D();
	
	private DivergentColormap colormap = new DivergentColormap.OrangePurple();

	private AlmaModel model;
	
	public static boolean COLORMAP_GLOBAL = false;

	ScalarField2D currSlice;

	MonitoredObject<ScalarField2D> view_sf2d = new MonitoredObject<ScalarField2D>( ){
		@Override protected Class<?> getClassType() { return ScalarField2D.class; } 
	};
	
	
	AlmaCT currCT = null;

	
	public AlmaGL( AlmaModel _model, int width, int height ){
		super(width, height);
		
		model = _model;
		
		view       = new View();
		controller = new Controller(true);
		
		sel_box = new SelectBoxDrawing();
		sfv     = new ScalarFieldDrawing( this.graphics );
		hist2d  = new HistogramDrawing( 32 );		
		
		model.monZ.addMonitor(this, "updateSlice" );
		updateSlice();

	}

	@Override protected void update() { }

	private ScalarField2D getSlice(int z){
		try {
			return model.fits.getSlice(z, 0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return null;		
	}
	
	public void updateSlice( ){
		System.out.println("update slice");
		currSlice = getSlice(model.monZ.get());
		view_sf2d.set( new Subsample2D(currSlice, 4) );
	}
	
	
	

	public class View extends ViewComponent.Subview implements ViewComponent, PositionedComponent {

		public View( ){  }

		public void setup( ){
			this.unregisterAll( );
			registerSubView( sfv,   	10 );
			registerSubView( hist2d,	27 );
			registerSubView( sel_box,	30 );
			if( currCT != null ){
				currCT.registerViews(this);
			}
		}
		
		@Override
		public void setPosition( int u0, int v0, int w, int h ){
			super.setPosition(u0, v0, w, h);
			
			int panelSize = h/3;
			
			model.csCont.setPosition(	 winX.start(), winY.start(), winX.length(), winY.length() );
			sfv.setPosition(	 winX.start(), winY.start(), winX.length(), winY.length() );
			sel_box.setPosition( winX.start(), winY.start(), winX.length(), winY.length() );
			hist2d.setPosition(	 winX.start()+10, winY.end()-panelSize/2-20, panelSize, panelSize/2 );
			
			if( currCT != null ){
				currCT.setPosition(u0, v0, w, h);
			}
		}

		public void update() {
			sfv.setColormap( colormap );
			if( currCT != null ){
				currCT.update();
			}
			super.update();
		}
	}
	
	


	public class Controller extends ControllerComponent.Subcontroller implements ControllerComponent {
		public Controller( boolean verbose ) {
			super(verbose);
		}

		@Override
		public void setup( ){
			unregisterAll( );
			if (currCT != null ){
				currCT.registerControls( this );
			}
			registerSubController( model.csCont, 15 );
			
			view_sf2d.addMonitor( hist2d, "setData" );
			view_sf2d.addMonitor( sfv,    "setScalarField" );

			model.csCont.addDragCallback( sfv, "setTranslation" );
			model.csCont.addZoomCallback( sfv, "adjustZoom" );

			model.monX.addMonitor(    this, "setUpdateSF" );
			model.monY.addMonitor(    this, "setUpdateSF" );
			model.monZ.addMonitor(    this, "setUpdateSF" );
			model.monZoom.addMonitor( this, "setUpdateSF" );
			model.monShowM0.addMonitor( this, "setUpdateSF" );
			model.monMM.addMonitor(   this, "setMouseMode" );
			
			model.monShowSimp.addMonitor( this, "setUpdateSF" );

			sel_box.setCoordinateSystem( model.csCont );
			if (currCT != null ){
				currCT.setCoordinateSystem( model.csCont );
			}
			
			sel_box.addReleaseCallback( this, "buildNewCT" );

			super.setup();
		}
		
		public void buildNewCT(){
			model.monMM.set( MouseMode.NAVIGATE );
			if( currCT  != null ){
				currCT.unregisterControls( this );
				currCT.unregisterViews( (ViewComponent.Subview)getView() );
				currCT.stop();
			}
			currCT = new AlmaCT( model, getController(), sel_box.getSelection() );
			currCT.registerViews( (ViewComponent.Subview)getView() ); 
			currCT.registerControls( this );
			currCT.setCoordinateSystem( model.csCont );
			currCT.setPosition( this.getPosition() );
		}
		
		@Override
		public boolean keyPressed( char key ){
			if( super.keyPressed(key) ) return true;
			if( key == '-' ){ model.monZ.decr(); return true; }
			if( key == '+' ){ model.monZ.incr(); return true; }
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
			
			float [] xy0 = model.csCont.getCoordinateSystemPosition( 0, 0 );
			float [] xy1 = model.csCont.getCoordinateSystemPosition(  getView().getWidth(),  getView().getHeight() );
			IntRange1D xr = new IntRange1D( (int)xy0[0], (int)xy1[0] );
			IntRange1D yr = new IntRange1D( (int)xy0[1], (int)xy1[1] );
			
			if (model.monShowM0.get()){
				LayeredVolume vol = new LayeredVolume();
				
				//for( int cz = model.monZ0.get(); cz <= model.monZ1.get(); cz++ ){
				for( int cz = model.monZ0.get(); cz <= model.monZ1.get(); cz+=8 ){
					ScalarField2D tmpSlice = getSlice(cz);
					if( model.monShowSimp.get() ){
						if( currCT != null && currCT.getSlice(cz) != null  ){
							tmpSlice = new Composite2D( tmpSlice, currCT.getSlice(cz), currCT.selRegion[0].start(), currCT.selRegion[1].start() );
						}
					}
					
					ScalarField2D _sf2D = new Subset2D(tmpSlice,xr,yr);
		 
					int stepX = (int)MathXv1.nextLargerPowerOf2( 2.0 * (double)_sf2D.getWidth()  / (double)winX.length() );
					int stepY = (int)MathXv1.nextLargerPowerOf2( 2.0 * (double)_sf2D.getHeight() / (double)winY.length() );
					
					vol.addLayers( new Subsample2D( _sf2D, stepX, stepY ) );
				}
				view_sf2d.set( new Moment0(vol) );
				double [] r = ScalarFieldND.Default.getValueRange( view_sf2d.get() );
				colormap.setRange( new FloatRange1D(r) );
			}
			else{
	
				ScalarField2D tmpSlice = currSlice;
				if( model.monShowSimp.get() ){
					if( currCT != null && currCT.getSlice(model.monZ.get()) != null  ){
						tmpSlice = new Composite2D( tmpSlice, currCT.getSlice(model.monZ.get()), currCT.selRegion[0].start(), currCT.selRegion[1].start() );
					}
				}
				
				ScalarField2D _sf2D = new Subset2D(tmpSlice,xr,yr);
	 
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
			}
			sf_needs_update = false;
		}
		
		public void setMouseMode( MouseMode mm ){
			unregisterSubController( model.csCont );
			unregisterSubController( sel_box );
			switch(mm){
				case NAVIGATE:		registerSubController( model.csCont,  10 ); break;
				case SELECT_REGION: registerSubController( sel_box, 10 ); break;
			}
		}
		
	}


}
