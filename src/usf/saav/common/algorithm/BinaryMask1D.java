package usf.saav.common.algorithm;

import java.util.Arrays;


/**
 * Used for creating a binary (on/off) mask in 1D
 */
public class BinaryMask1D implements Surface1D {
	
	/** the mask */
	private boolean [] mask;
	
	/** the width */
	private int width;
	
	/**
	 * Instantiates a new mask
	 *
	 * @param width the width of the mask
	 * @param default_val the default value to set the mask
	 */
	public BinaryMask1D( int width, boolean default_val ){
		mask = new boolean[width];
		Arrays.fill(mask, default_val);
		this.width = width;
	}

	/**
	 * Sets an element in the mask to true/on
	 *
	 * @param x the x
	 */
	public void set( int x ){ mask[ x ] = true; }
	
	/**
	 * Sets an element in the mask to false/off
	 *
	 * @param x the x
	 */
	public void clear( int x ){ mask[ x ] = false; }
	
	/**
	 * Checks if an element is sets
	 *
	 * @param x the x
	 * @return true, if is sets the
	 */
	public boolean isSet( int x ){ return mask[ x ]; }

	/* (non-Javadoc)
	 * @see usf.saav.alma.algorithm.Surface1D#size()
	 */
	@Override public int getWidth() { return mask.length; }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override 
	public String toString( ){
		String ret = "";
		for( int i = 0; i < mask.length; i++){
			ret += ( mask[i] ? "1 " : "0 " );
			if( (i%width)==(width-1) ) ret += "\n";
		}
		return ret;
	}


}
