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
				protected FloatRange1D rangeX = null;
				protected FloatRange1D rangeY = null;
				
				@Override public int size() { return data.getSize(); }
				@Override public Float2 getControlPoint(int i) { return new Float2((float)i/(float)(data.getSize()-1),data.getValue(i)); }

				@Override public FloatRange1D getRangeX() { 
					if( rangeX == null ){
						rangeX = new FloatRange1D(0,1);
					}
					return rangeX; 
				}
				
				@Override public FloatRange1D getRangeY() { 
					if( rangeY == null ){
						rangeY = new FloatRange1D();
						for( int i = 0; i < data.getSize(); i++){
							rangeY.expand( data.getValue(i) );
						}
					}
					return rangeY; 
				}
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

		int [] p = this.getPosition().clone();
		p[0]+=3; p[1]+=3; p[2]-=6; p[3]-=6; // add a little buffer space
		line.setPosition( p );
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

		g.noFill();
		
		g.stroke( 75 ); 
		float t = 1.0f - (float) ((0-yr.getMinimum())/yr.getRange());
		g.line( winX.start(), winY.interpolate(t), winX.end(), winY.interpolate(t) );
		
		g.stroke( 175 ); 
		g.fill( 0 ); 
		g.textSize( 14 );
		g.textAlign( TGraphics.LEFT, TGraphics.BOTTOM );
		for( int i = 1; i <= 6; i++ ){
			float curr = (float) ((float)i * yr.getRange() / 8);
			float t0 = 1.0f - (float) ((curr-yr.getMinimum())/yr.getRange());
			float t1 = 1.0f - (float) ((-curr-yr.getMinimum())/yr.getRange());
			if( t0 > 0 && t0 < 1 ){
				g.line( winX.start(), winY.interpolate(t0), winX.end(), winY.interpolate(t0) );
				g.text( String.format("%+1.2e", curr), winX.start()+2, (int) winY.interpolate(t0)-3 );
			}
			if( t1 > 0 && t1 < 1 ){
				g.line( winX.start(), winY.interpolate(t1), winX.end(), winY.interpolate(t1) );
				g.text( String.format("%+1.2e", -curr), winX.start()+2, (int) winY.interpolate(t1)-3 );
			}
		}
		
		line.setDrawingOffset( 0, (float) -yr.getMinimum()-(float)yr.getRange()  );
		line.setDrawingScale( 1, 1.0f-(1.0f/(float)yr.getRange()) );
		line.draw(g);
		
		g.hint( TGraphics.ENABLE_DEPTH_TEST );

		
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ViewComponent.Default#drawLegend(usf.saav.common.mvc.swing.TGraphics)
	 */
	@Override public void drawLegend(TGraphics g) { }
	
	


}
