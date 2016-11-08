
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
		return "[" + start() + ", " + end() + "]";
	}

	public boolean inRange(float value) {
		return ( min <= value && value <= max );
	}

	
}