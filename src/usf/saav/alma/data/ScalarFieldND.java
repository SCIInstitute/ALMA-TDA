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

import usf.saav.common.BasicObject;

public interface ScalarFieldND {

	public int getSize( );
	public float getValue( int nodeID );
	public int [] getNeighbors( int nodeID );
	public double [] getValueRange(); 
	
	public abstract class Default extends BasicObject implements ScalarFieldND {
		
		protected Default(){ }
		protected Default( boolean verbose ){ super(verbose); }

		@Override
		public double [] getValueRange(){
			double min =  Double.MAX_VALUE;
			double max = -Double.MAX_VALUE;
			@SuppressWarnings("unused")
			int nanCnt = 0;
			for(int i = 0; i < getSize(); i++){
				double v = getValue(i);
				if( Double.isNaN(v) ) nanCnt++;
				else{
					min = Math.min(min, v);
					max = Math.max(max, v);
				}
			}
			return new double[]{min,max};
		}
		
		
	}
}
