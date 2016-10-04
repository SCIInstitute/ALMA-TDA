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
package usf.saav.alma.data;

import java.util.Arrays;
import java.util.Random;

import usf.saav.common.algorithm.Surface2D;
import usf.saav.common.colormap.Colormap;
import usf.saav.common.mvc.swing.TGraphics;
import usf.saav.common.mvc.swing.TImage;
import usf.saav.common.types.Float4;

// TODO: Auto-generated Javadoc
/**
 * The Interface ScalarField2D.
 */
public interface ScalarField2D extends ScalarFieldND, Surface2D {

	/**
	 * Gets the value.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the value
	 */
	public float getValue( int x, int y );
	
	double [] getCoordinate( int x, int y );
	
	TImage toPImage( TGraphics g, Colormap colormap );
	
	/**
	 * The Class Default.
	 */
	public abstract class Default extends ScalarFieldND.Default implements ScalarField2D {

		protected Default( ){ }
		protected Default( boolean verbose ){ super(verbose); }
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarField2D#getCoordinate(int, int)
		 */
		@Override
		public double [] getCoordinate( int x, int y ){
			return new double[]{x,y};
		}
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarFieldND#getSize()
		 */
		@Override
		public int getSize() {
			return getWidth()*getHeight();
		}

		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarFieldND#getValue(int)
		 */
		@Override
		public float getValue(int nodeID) {
			return getValue( nodeID%getWidth(), nodeID/getWidth() );
		}
		
		/**
		 * Checks if is valid.
		 *
		 * @param nodeID the node ID
		 * @return true, if is valid
		 */
		public boolean isValid( int nodeID ){
			return !Float.isNaN( getValue( nodeID ) );
		}

		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarFieldND#getNeighbors(int)
		 */
		@Override
		public int[] getNeighbors(int nodeID) { 
			int width = getWidth();
			int height = getHeight();
			
			int y = nodeID/width;
			int x = nodeID%width;
			
			int [] ret = new int[8];
			int cur = 0;
			for( int _y = -1; _y <= 1; _y++ ){
				if( (y+_y) < 0 || (y+_y)>= height ) continue;				
				for( int _x = -1; _x <= 1; _x++ ){
					if( (x+_x) < 0 || (x+_x)>= width ) continue;
					if( _x == 0 && _y == 0 ) continue;
					ret[cur++] = (y+_y)*width+(x+_x);
				}
			}
			if( cur == ret.length ) return ret;
			return Arrays.copyOf( ret, cur );
		}
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarField2D#toPImage(usf.saav.common.mvc.swing.TGraphics, usf.saav.common.colormap.Colormap)
		 */
		@Override
		public TImage toPImage( TGraphics g, Colormap colormap ){
			TImage img = g.createImage( getWidth(), getHeight(), TGraphics.RGB);
			int i = 0;
			for(int h = 0; h < getHeight(); h++){
				for(int w = 0; w < getWidth(); w++){
					Float4 c = colormap.getColor( getValue(i) );
					img.set( w, h, c.x,c.y,c.z,c.w );
					//img.pixels[i] = TGraphics.color(c.x,c.y,c.z,c.w);
					i++;
				}
			}
			return img;
		}
	}	
	
	/**
	 * The Class Empty.
	 */
	public class Empty extends ScalarField2D.Default {
		int w,h;
		float default_val;
		
		/**
		 * Instantiates a new empty.
		 *
		 * @param w the w
		 * @param h the h
		 * @param default_val the default val
		 */
		public Empty( int w, int h, float default_val ) {
			this.w = w;
			this.h = h;
			this.default_val = default_val;
		}
		
		/**
		 * Instantiates a new empty.
		 *
		 * @param w the w
		 * @param h the h
		 * @param default_val the default val
		 * @param verbose the verbose
		 */
		public Empty( int w, int h, float default_val, boolean verbose ) {
			super(verbose);
			this.w = w;
			this.h = h;
			this.default_val = default_val;
		}
		
		/* (non-Javadoc)
		 * @see usf.saav.common.algorithm.Surface2D#getWidth()
		 */
		@Override public int getWidth()  {	return w; }
		
		/* (non-Javadoc)
		 * @see usf.saav.common.algorithm.Surface2D#getHeight()
		 */
		@Override public int getHeight() {	return h; }
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarField2D#getValue(int, int)
		 */
		@Override public float getValue(int x, int y) { return default_val; }
	}
	

	/**
	 * The Class Test.
	 */
	public class Test extends Default {
		
		float [] data;
		int width, height;
		
		/**
		 * Instantiates a new test.
		 *
		 * @param w the w
		 * @param h the h
		 */
		public Test( int w, int h ){
			super(true);
			Random random = new Random();
			data = new float[w*h];
			width = w;
			height = h;
			for(int i = 0; i < data.length; i++){
				data[i] = random.nextFloat()*100;
			}
		}

		/* (non-Javadoc)
		 * @see usf.saav.common.algorithm.Surface2D#getWidth()
		 */
		@Override
		public int getWidth() {
			return width;
		}

		/* (non-Javadoc)
		 * @see usf.saav.common.algorithm.Surface2D#getHeight()
		 */
		@Override
		public int getHeight() {
			return height;
		}

		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarField2D#getValue(int, int)
		 */
		@Override
		public float getValue(int x, int y) {
			return data[y*width+x];
		}
	}
}