package usf.saav.alma.drawing;

import java.util.Vector;

import usf.saav.alma.util.CoordinateSystem;
import usf.saav.common.Callback;
import usf.saav.common.mvc.ControllerComponent;
import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.mvc.swing.TGraphics;

public class SelectPointDrawing  extends ControllerComponent.Default implements ViewComponent, ControllerComponent {

	
	boolean validSel = false;

	private float selU0;
	private float selV0;

	private CoordinateSystem cs;

	private Vector<Callback> cb = new Vector<Callback>();
	

	public SelectPointDrawing( ){
		super( false );
	}
	
	public int [] getSelection( ){
		return new int[]{ (int)selU0, (int)selV0 };
	}
	
	public void setCoordinateSystem( CoordinateSystem csc ){
		this.cs = csc;
	}
	
	
	@Override
	public void draw(TGraphics g) {
		if( validSel ){
			
			float[] xy0 = cs.getWindowPosition( selU0, selV0);
			
			float x0 = xy0[0];
			float y0 = xy0[1];
			
			g.hint(TGraphics.DISABLE_DEPTH_TEST);
			g.strokeWeight(3);
			g.noFill();
			g.stroke( 255, 0, 0 );
			g.rect( x0-4, y0-4, 8, 8 );
			g.strokeWeight(1);
			g.hint(TGraphics.ENABLE_DEPTH_TEST);
			
		}
	}


	@Override
	public void drawLegend(TGraphics g) { }
	
	
	
	@Override
	public boolean mousePressed( int mouseX, int mouseY ) {
		if( !winX.inRange(mouseX) || !winY.inRange(mouseY) ) return false;
		
		if( cs == null ){
			print_error_message("Coordinate System Controller NOT SET!");
			return true;
		}
				
		validSel = true;
		
		float [] xy = cs.getCoordinateSystemPosition( mouseX, mouseY );
		selU0 = xy[0];
		selV0 = xy[1];
		
		for( Callback c : cb){
			c.call( getSelection() );
		}

		return true;
	}



	public void clearSelection() {
		validSel = false;
	}

	
	public void addMonitor( Object obj, String func_name ) {
		try {
			this.cb.add(new Callback( obj, func_name, int[].class ) );
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}


}