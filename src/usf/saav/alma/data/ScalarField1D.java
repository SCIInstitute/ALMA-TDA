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

public interface ScalarField1D extends ScalarFieldND, Surface1D {
	
	double getCoordinate( int nodeID );
	
	public class Empty extends ScalarField1D.Default {
		int w;
		float default_val;
		public Empty( int w, float default_val ) {
			this.w = w;
			this.default_val = default_val;
		}
		@Override public int getSize() { return w; }
		@Override public float getValue(int x) { return default_val; }
		@Override public int getWidth() { return w; }
	}
	
	public abstract class Default extends ScalarFieldND.Default implements ScalarField1D {

		protected Default( ){ }
		protected Default( boolean verbose ){ super(verbose); }
		
		@Override
		public double getCoordinate( int x ){
			return x;
		}
		
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
