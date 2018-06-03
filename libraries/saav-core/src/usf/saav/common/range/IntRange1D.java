/*
 *     saav-core - A (very boring) software development support library.
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


package usf.saav.common.range;

/**
 *
 * @author dhaval
 */
public class IntRange1D {
	
	int max, min;

	/**
	 * TODO: Short description.
	 * @param _min
	 * @param _max
	 */
	public IntRange1D(int _min, int _max) {
		min = Math.min(_min, _max);
		max = Math.max(_min, _max);
	}

	/**
	 * TODO: Short description.
	 * @param vals
	 */
	public IntRange1D(int ... vals) {
		min = max = vals[0];
		for(int v : vals){
			min = Math.min(min, v);
			max = Math.max(max, v);
		}
	}
	
	public void set(int _min, int _max) {
		min = Math.min(_min, _max);
		max = Math.max(_min, _max);
	}
	
	public IntRange1D clone(){
		return new IntRange1D( min, max );
	}
	
	
	/**
	 * TODO: Short description.
	 * @param x
	 * @return
	 */
	public int clamptoRange(int x) {
		return Math.max(min, (Math.min(max, x)));
	}
	
	/**
	 * TODO: Short description.
	 * @param old_range
	 * @return
	 */
	public IntRange1D clamptoRange( IntRange1D old_range ) {
		return new IntRange1D( clamptoRange(old_range.start()), clamptoRange(old_range.end()) );
	}

	
	public static IntRange1D parseRange( String range ) {
		
		if( range.charAt(0) == '[' || range.charAt(0) == '(' ){
			String[] parts = range.split(",");
			if( parts.length != 2 ) throw new NumberFormatException();
			
			int v0 = Integer.parseInt(parts[0].substring(1));
			int v1 = Integer.parseInt(parts[1].substring(0,parts[1].length()-1));
			
			if( parts[0].charAt(0) == '(' ) v0++;
			if( parts[1].charAt(parts[1].length()-1) == ')') v1--;
			
			return new IntRange1D(v0,v1);
		}
		try {
			int v = Integer.parseInt( range );
			return new IntRange1D( v );
		} catch( Exception e ){
			//e.printStackTrace();
		}
		throw new NumberFormatException();
		
	}

	
	/**
	 * TODO: Short description.
	 * @return
	 */
	public int length(){ return max-min+1; }
	@Deprecated public int range(){ return max-min+1; }
	@Deprecated public int size(){ return max-min+1; }
	
	/**
	 * TODO: Short description.
	 * @return
	 */
	public int start(){ return min; }
	@Deprecated public int getStart(){ return min; }
	@Deprecated public int min(){ return min; }
	@Deprecated public int getMin(){ return min; }
	@Deprecated public int getMinimum(){ return min; }

	/**
	 * TODO: Short description.
	 * @return
	 */
	public int end(){ return max; }
	@Deprecated public int getEnd(){ return max; }
	@Deprecated public int max(){ return max; }
	@Deprecated public int getMax(){ return max; }
	@Deprecated public int getMaximum(){ return max; }
	
	public int middle() {
		return (min+max)/2;
	}

	
	/**
	 * TODO: Short description.
	 * @param value
	 * @param _min
	 * @param _max
	 * @return
	 */
	public static int clamptoRange(int value, int _min, int _max) {
		return Math.max(_min, (Math.min(value, _max)));

	}

	/**
	 * TODO: Short description.
	 * @param values
	 * @param _min
	 * @param _max
	 * @return
	 */
	public static int[] clamptoRange(int[] values, int _min, int _max) {
		int [] ret = new int[values.length];
		for( int i = 0; i < values.length; i++){
			ret[i] = clamptoRange(values[i],_min,_max);
		}
		return ret;
	}
	

	public boolean inRange(int value) {
		return ( min <= value && value <= max );
	}


	public float interpolate( float i ) {
		return ((float)min*(1-i) + (float)max*i);
	}

	public float location( float i ) {
		return (float)(i-min)/(float)(max-min);
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override public String toString(){
		if( start() == end() ) return Integer.toString(start());
		return "[" + start() + "," + end() + "]";
	}

	public boolean inRange(float value) {
		return ( min <= value && value <= max );
	}

	
}