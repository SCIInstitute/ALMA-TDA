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