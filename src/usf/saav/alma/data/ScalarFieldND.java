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

// TODO: Auto-generated Javadoc
/**
 * The Interface ScalarFieldND.
 */
public interface ScalarFieldND {

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize( );
	
	/**
	 * Gets the value.
	 *
	 * @param nodeID the node ID
	 * @return the value
	 */
	public float getValue( int nodeID );
	
	/**
	 * Gets the neighbors.
	 *
	 * @param nodeID the node ID
	 * @return the neighbors
	 */
	public int [] getNeighbors( int nodeID );
	
	/**
	 * Gets the value range.
	 *
	 * @return the value range
	 */
	public double [] getValueRange(); 
	
	/**
	 * The Class Default.
	 */
	public abstract class Default extends BasicObject implements ScalarFieldND {
		
		protected Default(){ }
		protected Default( boolean verbose ){ super(verbose); }

		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarFieldND#getValueRange()
		 */
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
