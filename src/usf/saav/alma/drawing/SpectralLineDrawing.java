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

import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.mvc.swing.TGraphics;
import usf.saav.common.range.FloatRange1D;
import usf.saav.common.spline.LinearSpline;
import usf.saav.common.spline.Spline;
import usf.saav.common.types.Float2;
import usf.saav.scalarfield.ScalarField1D;

// TODO: Auto-generated Javadoc
/**
 * The Class SpectralLineDrawing.
 */
public class SpectralLineDrawing  extends ViewComponent.Default implements ViewComponent { 

	Spline line = null;
	
	/**
	 * Instantiates a new spectral line drawing.
	 */
	public SpectralLineDrawing( ){
	}
	
	/**
	 * Sets the data.
	 *
	 * @param sf the new data
	 */
	public void setData( final ScalarField1D sf ){
		if( sf == null ){
			line = null;
		}
		else{
			line = new LinearSpline() {
				ScalarField1D data = sf;
				@Override public int size() { return data.getSize(); }
				@Override public Float2 getControlPoint(int i) { return new Float2((float)i/(float)data.getSize(),data.getValue(i)); }
			};
		}
	}
		

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.BasicComponent.Default#update()
	 */
	@Override
	public void update( ) {
		if( !isEnabled() ) return;
		if( line == null ) return;

		line.setPosition( this.getPosition() );
		line.update();
	}

	
	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ViewComponent.Default#draw(usf.saav.common.mvc.swing.TGraphics)
	 */
	@Override
	public void draw(TGraphics g) {
		if( !isEnabled() ) return;
		if( line == null ) return;

		g.hint( TGraphics.DISABLE_DEPTH_TEST );
		
		g.strokeWeight(2);
		g.stroke(0);
		g.fill(255);
		g.rect( winX.start(), winY.start(), winX.length(), winY.length() );
		
		FloatRange1D yr = line.getRangeY( );
		line.setDrawingOffset( 0, (float) -yr.getMinimum() );
		line.setDrawingScale( 1, (1.0f/(float)yr.getRange()) );
		line.draw(g);
		
		g.hint( TGraphics.ENABLE_DEPTH_TEST );

		
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ViewComponent.Default#drawLegend(usf.saav.common.mvc.swing.TGraphics)
	 */
	@Override public void drawLegend(TGraphics g) { }
	
	


}
