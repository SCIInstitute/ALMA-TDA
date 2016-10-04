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

import usf.saav.common.algorithm.Surface1D;

// TODO: Auto-generated Javadoc
/**
 * The Interface ScalarField1D.
 */
public interface ScalarField1D extends ScalarFieldND, Surface1D {
	
	double getCoordinate( int nodeID );
	
	/**
	 * The Class Empty.
	 */
	public class Empty extends ScalarField1D.Default {
		int w;
		float default_val;
		
		/**
		 * Instantiates a new empty.
		 *
		 * @param w the w
		 * @param default_val the default val
		 */
		public Empty( int w, float default_val ) {
			this.w = w;
			this.default_val = default_val;
		}
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarFieldND#getSize()
		 */
		@Override public int getSize() { return w; }
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarFieldND#getValue(int)
		 */
		@Override public float getValue(int x) { return default_val; }
		
		/* (non-Javadoc)
		 * @see usf.saav.common.algorithm.Surface1D#getWidth()
		 */
		@Override public int getWidth() { return w; }
	}
	
	/**
	 * The Class Default.
	 */
	public abstract class Default extends ScalarFieldND.Default implements ScalarField1D {

		protected Default( ){ }
		protected Default( boolean verbose ){ super(verbose); }
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarField1D#getCoordinate(int)
		 */
		@Override
		public double getCoordinate( int x ){
			return x;
		}
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarFieldND#getNeighbors(int)
		 */
		@Override
		public int[] getNeighbors(int nodeID) {
			int w = getSize();
			if( nodeID > 0 && nodeID < (w-1) ) return new int[]{nodeID-1,nodeID+1};
			if( nodeID == 0 && nodeID < (w-1) ) return new int[]{nodeID+1};
			if( nodeID > 0 && nodeID == (w-1) ) return new int[]{nodeID-1};
			return new int[]{};
		}
	}
}
