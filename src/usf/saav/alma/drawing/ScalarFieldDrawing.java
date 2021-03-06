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

import usf.saav.common.colormap.Colormap;
import usf.saav.common.colormap.DivergentColormap;
import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.mvc.swing.TGraphics;
import usf.saav.common.mvc.swing.TImage;
import usf.saav.common.types.Float4;
import usf.saav.scalarfield.ScalarField2D;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarFieldDrawing.
 */
public class ScalarFieldDrawing extends ViewComponent.Default implements ViewComponent {

	/**
	 * Instantiates a new scalar field drawing.
	 *
	 * @param papplet the papplet
	 */
	public ScalarFieldDrawing( TGraphics papplet ){
		this.papplet = papplet;
	}

	/**
	 * Sets the colormap.
	 *
	 * @param cm the new colormap
	 */
	public void setColormap( DivergentColormap cm ){
		colormap = cm;
	}

	/**
	 * Sets the translation.
	 *
	 * @param tx the tx
	 * @param ty the ty
	 */
	public void setTranslation( int tx, int ty ){
		this.tx = tx;
		this.ty = ty;
	}
	
	public void adjustZoom( float zoom_adj ){
		this.zoom *= zoom_adj;
	}

	/**
	 * Sets the scalar field.
	 *
	 * @param _sf the new scalar field
	 */
	public void setScalarField( ScalarField2D _sf ){

		this.sf  = _sf;
		this.img = null;
		
		this.tx = 0;
		this.ty = 0;
		this.zoom = 1;
		
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.BasicComponent.Default#update()
	 */
	@Override
	public void update() {
		if( !isEnabled() ) return;
		if( img != null ) return;
		if( sf == null ) return;
		if( colormap == null ) return;

		img = toPImage( papplet, sf, colormap );
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ViewComponent.Default#draw(usf.saav.common.mvc.swing.TGraphics)
	 */
	@Override
	public void draw(TGraphics g) {
		if( !isEnabled() ) return;
		if( img == null ) return;

		g.fill(255);
		g.imageMode(TGraphics.CENTER);
		g.image( img, (int)(winX.middle()+tx), (int)(winY.middle()+ty), (int)(zoom*winX.length()), (int)(zoom*winY.length()) );

	}


	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ViewComponent.Default#drawLegend(usf.saav.common.mvc.swing.TGraphics)
	 */
	public void drawLegend(TGraphics g) {
		if( !isEnabled() ) return;
		if( colormap == null ) return;
		
		g.hint( TGraphics.DISABLE_DEPTH_TEST );

		float size = 25.0f;
		g.stroke(0);
		g.fill(255,255,255,240);
		g.rect( winX.length()-10.5f*size, 5, 10.0f*size, 12.0f*size);
		colormap.drawScale(g, (int)(winX.length()-10.0f*size), 10, 20, (int)(11.0f*size), (int)size );

		g.hint( TGraphics.ENABLE_DEPTH_TEST );
		
	}

	// Local pointers to global data
	private TGraphics papplet;

	// Image related variables
	private TImage img = null;
	private DivergentColormap colormap = null;

	// Input scalar field
	private ScalarField2D sf;

	// Temporary translation
	private int tx = 0, ty = 0;
	
	// Temporary zoom
	private float zoom = 1;
	
	
	
	private static TImage toPImage( TGraphics g, ScalarField2D sf, Colormap colormap ){
		TImage img = g.createImage( sf.getWidth(), sf.getHeight(), TGraphics.RGB);
		int i = 0;
		for(int h = 0; h < sf.getHeight(); h++){
			for(int w = 0; w < sf.getWidth(); w++){
				float v = sf.getValue(i);
				
				Float4 c = colormap.getColor( v );
				if( Float.isNaN(v) ) c = new Float4(1,0,1,1);
				else if( Float.isInfinite(v) ) c = new Float4(1,1,0,1);
				else System.out.print(v + " " );
				img.set( w, h, c.x,c.y,c.z,c.w );
				//img.set( w, h, c.x*255,c.y*255,c.z*255,c.w*255 );
				//System.out.print(c + " ");
				//img.set( w, h, 255,0,0,255 );
				//img.pixels[i] = TGraphics.color(c.x,c.y,c.z,c.w);
				i++;
			}
			System.out.println();
		}
		System.out.println();
		return img;
	}
	
}
