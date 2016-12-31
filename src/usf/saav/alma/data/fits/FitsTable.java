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
package usf.saav.alma.data.fits;

import java.util.Arrays;

public class FitsTable {
	
	String [] names;
	Object [][] data;
	
	public FitsTable( int rows, int cols ){
		names = new String[cols];
		data  = new Object[rows][cols];
	}
	
	public void setData( int row, int col, Object _data ){
		data[row][col] = _data;
	}
	
	public void setColumnLabel( int col, String label ){
		names[col] = label;
	}
	
	@Override
	public String toString( ){
		StringBuilder ret = new StringBuilder( );
		for(int i = 0; i < names.length; i++){
			ret.append( names[i] );
			if( i < names.length-1 ) ret.append(" | ");
		}
		ret.append("\n");
		
		for( int row = 0; row < data.length; row++){
			for(int col = 0; col < data[row].length; col++){
				Object o = data[row][col];
				if( o instanceof int[] ){
					ret.append( Arrays.toString( (int[])o ) );
				}
				else if( o instanceof float[] ){
					ret.append( Arrays.toString( (float[])o ) );
				}
				else {
					ret.append( o.toString() );
				}
				if( col < data[row].length-1 ) ret.append(" | ");
			}
			if( row < data.length-1 ) ret.append("\n");
		}
		
		return ret.toString();
	}
}