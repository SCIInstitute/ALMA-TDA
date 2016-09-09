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

import processing.core.PApplet;
import processing.core.PImage;
import usf.saav.common.algorithm.Surface2D;
import usf.saav.common.colormap.Colormap;
import usf.saav.common.types.Float4;

public interface ScalarField2D extends ScalarFieldND, Surface2D {

	public float getValue( int x, int y );
	
	double [] getCoordinate( int x, int y );
	
	PImage toPImage( PApplet papplet, Colormap colormap );
	
	public abstract class Default extends ScalarFieldND.Default implements ScalarField2D {

		protected Default( ){ }
		protected Default( boolean verbose ){ super(verbose); }
		
		@Override
		public double [] getCoordinate( int x, int y ){
			return new double[]{x,y};
		}
		
		@Override
		public int getSize() {
			return getWidth()*getHeight();
		}

		@Override
		public float getValue(int nodeID) {
			return getValue( nodeID%getWidth(), nodeID/getWidth() );
		}
		
		public boolean isValid( int nodeID ){
			return !Float.isNaN( getValue( nodeID ) );
		}

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
		
		@Override
		public PImage toPImage( PApplet papplet, Colormap colormap ){
			PImage img = papplet.createImage( getWidth(), getHeight(), PApplet.RGB);
			int i = 0;
			for(int h = 0; h < getHeight(); h++){
				for(int w = 0; w < getWidth(); w++){
					Float4 c = colormap.getColor( getValue(i) );
					img.pixels[i] = papplet.color(c.x*255,c.y*255,c.z*255,c.w*255);
					i++;
				}
			}
			return img;
		}
	}	
	
	public class Empty extends ScalarField2D.Default {
		int w,h;
		float default_val;
		public Empty( int w, int h, float default_val ) {
			this.w = w;
			this.h = h;
			this.default_val = default_val;
		}
		public Empty( int w, int h, float default_val, boolean verbose ) {
			super(verbose);
			this.w = w;
			this.h = h;
			this.default_val = default_val;
		}
		@Override public int getWidth()  {	return w; }
		@Override public int getHeight() {	return h; }
		@Override public float getValue(int x, int y) { return default_val; }
	}
	

	public class Test extends Default {
		
		float [] data;
		int width, height;
		
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

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}

		@Override
		public float getValue(int x, int y) {
			return data[y*width+x];
		}
	}
}