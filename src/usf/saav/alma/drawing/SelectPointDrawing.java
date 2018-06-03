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
package usf.saav.alma.drawing;

import java.util.Vector;

import usf.saav.alma.util.CoordinateSystem;
import usf.saav.common.monitoredvariables.Callback;
import usf.saav.common.mvc.ControllerComponent;
import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.mvc.swing.TGraphics;

// TODO: Auto-generated Javadoc
/**
 * The Class SelectPointDrawing.
 */
public class SelectPointDrawing  extends ControllerComponent.Default implements ViewComponent, ControllerComponent {

	
	boolean validSel = false;

	private float selU0;
	private float selV0;

	private CoordinateSystem cs;

	private Vector<Callback> cb = new Vector<Callback>();
	

	/**
	 * Instantiates a new select point drawing.
	 */
	public SelectPointDrawing( ){
		super( false );
	}
	
	/**
	 * Gets the selection.
	 *
	 * @return the selection
	 */
	public int [] getSelection( ){
		return new int[]{ (int)selU0, (int)selV0 };
	}
	
	/**
	 * Sets the coordinate system.
	 *
	 * @param csc the new coordinate system
	 */
	public void setCoordinateSystem( CoordinateSystem csc ){
		this.cs = csc;
	}
	
	
	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ViewComponent#draw(usf.saav.common.mvc.swing.TGraphics)
	 */
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


	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ViewComponent#drawLegend(usf.saav.common.mvc.swing.TGraphics)
	 */
	@Override
	public void drawLegend(TGraphics g) { }
	
	
	
	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ControllerComponent.Default#mousePressed(int, int)
	 */
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



	/**
	 * Clear selection.
	 */
	public void clearSelection() {
		validSel = false;
	}

	
	/**
	 * Adds the monitor.
	 *
	 * @param obj the obj
	 * @param func_name the func name
	 */
	public void addMonitor( Object obj, String func_name ) {
		try {
			this.cb.add(new Callback( obj, func_name, int[].class ) );
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}


}