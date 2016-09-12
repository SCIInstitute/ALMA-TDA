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

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import usf.saav.alma.data.ScalarField2D;
import usf.saav.alma.data.processors.Subsample2D;
import usf.saav.common.MathX;
import usf.saav.common.colormap.DivergentColormap;
import usf.saav.common.mvc.ViewComponent;

public class ScalarFieldDrawing extends ViewComponent.Default implements ViewComponent {

	public ScalarFieldDrawing( PApplet papplet ){
		this.papplet = papplet;
	}

	public void setColormap( DivergentColormap cm ){
		colormap = cm;
	}

	public void setTranslation( int tx, int ty ){
		this.tx = tx;
		this.ty = ty;
	}

	public void setScalarField( ScalarField2D _sf ){
		int stepX = MathX.nextLargerPowerOf2( 2*_sf.getWidth()/winX.length() );
		int stepY = MathX.nextLargerPowerOf2( 2*_sf.getHeight()/winY.length() );
		this.sf = new Subsample2D( _sf, stepX, stepY );

		img = null;
	}

	@Override
	public void update() {
		if( !isEnabled() ) return;
		if( img != null ) return;
		if( sf == null ) return;
		if( colormap == null ) return;

		img = sf.toPImage( papplet, colormap );
	}

	@Override
	public void draw(PGraphics g) {
		if( !isEnabled() ) return;
		if( img == null ) return;

		g.imageMode(PConstants.CENTER);
		g.image( img, winX.length()/2+tx, winY.length()/2+ty, (int)(winX.length()), (int)(winY.length()) );
	}

	public void drawLegend(PGraphics g) {
		if( !isEnabled() ) return;
		if( colormap == null ) return;

		g.hint( PConstants.DISABLE_DEPTH_TEST );

		g.stroke(0);
		g.fill(255,255,255,240);
		g.rect( winX.length()-105, 5, 100, 120);
		colormap.drawScale(g, winX.length()-100, 10, 20, 110);

		g.hint( PConstants.ENABLE_DEPTH_TEST );
	}

	// Local pointers to global data
	private PApplet papplet;

	// Image related variables
	private PImage img = null;
	private DivergentColormap colormap = null;

	// Input scalar field
	ScalarField2D sf;

	// Temporary translation
	private int tx = 0, ty = 0;
}
