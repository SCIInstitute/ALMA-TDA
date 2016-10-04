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

import usf.saav.alma.util.CoordinateSystem;
import usf.saav.common.mvc.ControllerComponent;
import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.mvc.swing.TGraphics;
import usf.saav.common.range.IntRange1D;

// TODO: Auto-generated Javadoc
/**
 * The Class SelectBoxDrawing.
 */
public class SelectBoxDrawing extends ControllerComponent.Default implements ViewComponent, ControllerComponent {

	private boolean dragOn = false;

	private boolean validSel = false;

	private float selU0, selU1;
	private float selV0, selV1;

	private CoordinateSystem cs;

	/**
	 * Instantiates a new select box drawing.
	 */
	public SelectBoxDrawing( ){
		super( false );
	}

	/**
	 * Gets the selection.
	 *
	 * @return the selection
	 */
	public IntRange1D [] getSelection( ){
		return new IntRange1D[]{ new IntRange1D((int)selU0,(int)selU1), new IntRange1D((int)selV0,(int)selV1) };
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
			float[] xy1 = cs.getWindowPosition( selU1, selV1);

			float x0 = xy0[0];
			float x1 = xy1[0];
			float y0 = xy0[1];
			float y1 = xy1[1];

			g.strokeWeight(5);
			g.noFill();
			g.stroke( 255, 0, 0 );
			g.rect( x0, y0, x1-x0, y1-y0 );
			g.strokeWeight(1);
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

		dragOn = true;

		validSel = false;

		float [] xy = cs.getCoordinateSystemPosition( mouseX, mouseY );
		selU0 = selU1 = xy[0];
		selV0 = selV1 = xy[1];

		return true;
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ControllerComponent.Default#mouseDragged(int, int)
	 */
	@Override
	public boolean mouseDragged( int mouseX, int mouseY ) {
		if( !dragOn ) return false;

		validSel = true;

		float [] xy = cs.getCoordinateSystemPosition( mouseX, mouseY );
		selU1 = xy[0];
		selV1 = xy[1];

		return true;
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ControllerComponent.Default#mouseReleased()
	 */
	@Override
	public boolean mouseReleased( ) {
		dragOn = false;
		return false;
	}

	/**
	 * Clear selection.
	 */
	public void clearSelection() {
		validSel = false;
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.BasicComponent.Default#update()
	 */
	@Override
	public void update() {	}
}
