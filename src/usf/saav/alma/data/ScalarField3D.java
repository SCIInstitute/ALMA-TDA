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

import usf.saav.common.algorithm.Surface3D;

// TODO: Auto-generated Javadoc
/**
 * The Interface ScalarField3D.
 */
public interface ScalarField3D extends ScalarFieldND, Surface3D {

	/* (non-Javadoc)
	 * @see usf.saav.common.algorithm.Surface3D#getWidth()
	 */
	public int getWidth( );
	
	/* (non-Javadoc)
	 * @see usf.saav.common.algorithm.Surface3D#getHeight()
	 */
	public int getHeight( );
	
	/* (non-Javadoc)
	 * @see usf.saav.common.algorithm.Surface3D#getDepth()
	 */
	public int getDepth( );
	
	/**
	 * Gets the value.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @return the value
	 */
	public float getValue( int x, int y, int z );

	double [] getCoordinate( int x, int y, int z );
	
	/**
	 * The Class Empty.
	 */
	public class Empty extends ScalarField3D.Default {
		int w,h,d;
		float default_val;
		
		/**
		 * Instantiates a new empty.
		 *
		 * @param w the w
		 * @param h the h
		 * @param d the d
		 * @param default_val the default val
		 */
		public Empty( int w, int h, int d, float default_val ) {
			this.w = w;
			this.h = h;
			this.d = d;
			this.default_val = default_val;
		}
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarField3D#getWidth()
		 */
		@Override public int getWidth()  {	return w; }
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarField3D#getHeight()
		 */
		@Override public int getHeight() {	return h; }
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarField3D#getDepth()
		 */
		@Override public int getDepth()  {	return d; }
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarField3D#getValue(int, int, int)
		 */
		@Override public float getValue(int x, int y, int z) { return default_val; }
	}
	
	/**
	 * The Class Default.
	 */
	public abstract class Default extends ScalarFieldND.Default implements ScalarField3D {

		protected Default( ){ }
		protected Default( boolean verbose ){ super(verbose); }
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarField3D#getCoordinate(int, int, int)
		 */
		@Override
		public double [] getCoordinate( int x, int y, int z ){
			return new double[]{x,y,z};
		}
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarFieldND#getSize()
		 */
		@Override
		public int getSize() {
			return getWidth()*getHeight()*getDepth();
		}

		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarFieldND#getValue(int)
		 */
		@Override
		public float getValue(int nodeID) {
			
			int width = getWidth();
			int height = getHeight();
			
			int x = nodeID%width;
			int y = (nodeID%(width*height))/width;
			int z = nodeID/(width*height);
			
			return getValue( x, y, z );
		}

		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarFieldND#getNeighbors(int)
		 */
		@Override
		public int[] getNeighbors(int nodeID) {
			int width = getWidth();
			int height = getHeight();
			int depth = getDepth();
			
			int slc    = nodeID/(width*height);
			int slcOff = nodeID%(width*height);
			
			int x = slcOff%width;
			int y = slcOff/width;
			int z = slc;
			
			int [] ret = new int[26];
			int cur = 0;
			for( int _z = -1; _z <= 1; _z++ ){
				if( (z+_z) < 0 || (z+_z)>= depth ) continue;				
				for( int _y = -1; _y <= 1; _y++ ){
					if( (y+_y) < 0 || (y+_y)>= height ) continue;				
					for( int _x = -1; _x <= 1; _x++ ){
						if( (x+_x) < 0 || (x+_x)>= width ) continue;
						if( _x == 0 && _y == 0 && _z==0 ) continue;
						ret[cur++] = (z+_z)*width*height+(y+_y)*width+(x+_x);
					}
				}
			}
			if( cur == ret.length ) return ret;
			return Arrays.copyOf( ret, cur );
		}
	}
}
