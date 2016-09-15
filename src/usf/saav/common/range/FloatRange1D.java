package usf.saav.common.range;

import usf.saav.common.MathX;

/**
 * Class for building 1D value ranges.
 * 
 * Last code review: 2005/01/20
 * 
 * @author Paul Rosen
 *
 */
public class FloatRange1D {

	private double r_min = Double.MAX_VALUE;
	private double r_max = -Double.MAX_VALUE;
	private double mean_value = 0;
	private long element_count = 0;

	/**
	 * Default constructor creates an invalid range
	 */
	public FloatRange1D() {
	}

	/**
	 * Constructor that starts with a prefixed range
	 * 
	 * @param _min
	 * @param _max
	 */
	public FloatRange1D(double _min, double _max) {
		r_min = _min;
		r_max = _max;
	}
	
	public FloatRange1D( double ... v ){
		r_min = r_max = v[0];
		expand(v);
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public FloatRange1D clone() {
		FloatRange1D ret = new FloatRange1D();
		ret.r_min = r_min;
		ret.r_max = r_max;
		ret.mean_value = mean_value;
		ret.element_count = element_count;
		return ret;
	}

	/**
	 * Get the minimum range value
	 * 
	 * @return
	 */
	public double getMinimum() {
		return r_min;
	}

	/**
	 * Get the maximum range value
	 * 
	 * @return
	 */
	public double getMaximum() {
		return r_max;
	}

	/**
	 * Get the size of the range
	 * 
	 * @return
	 */
	public double getRange() {
		return r_max - r_min;
	}

	/**
	 * Get the mean value of the range
	 * 
	 * @return
	 */
	public double getMean() {
		return mean_value;
	}

	/**
	 * Get the exact center of the range
	 * 
	 * @return
	 */
	public double getCenter() {
		return (r_max + r_min) / 2.0;
	}

	/**
	 * Normalizes a value from [range_min,range_max] to [0,1].
	 * 
	 * @param v
	 *            The value to normalize
	 * @return
	 */
	public double getNormalized(double v) {
		if ((r_max - r_min) == 0)
			return 0.5;
		return (v - r_min) / (r_max - r_min);
	}

	/**
	 * Normalize a value from [range_min,range_max] to [min_v,max_v].
	 * 
	 * @param v
	 * @param min_v
	 * @param max_v
	 * @return
	 */
	public double getNormalizedToRange(double v, double min_v, double max_v) {
		return getNormalized(v) * (max_v - min_v) + min_v;
	}

	/**
	 * Calculate a mean centered value. This function simply offsets the value
	 * by the mean.
	 * 
	 * @param v
	 * @return
	 */
	public double getMeanCentered(double v) {
		return v - mean_value;
	}

	/**
	 * Check to see if a value is outside of the range.
	 * 
	 * @param p
	 * @return
	 */
	public boolean isOutside(float p) {
		return (p < r_min || p > r_max);
	}
	
	public boolean isOutside(double p) {
		return (p < r_min || p > r_max);
	}

	/**
	 * Check to see if a value is inside of the range.
	 * 
	 * @param p
	 * @return
	 */
	public boolean isInside(float p) {
		return (p <= r_min && p <= r_max);
	}

	public boolean isInside(double p) {
		return (p <= r_min && p <= r_max);
	}
	
	/**
	 * Expand the range based upon values of another range.
	 * 
	 * @param br
	 */
	public void expand(FloatRange1D br) {
		r_min = Math.min(br.r_min, r_min);
		r_max = Math.max(br.r_max, r_max);
		double m1 = getMean() * element_count;
		double m2 = br.getMean() * br.element_count;
		element_count += br.element_count;
		mean_value = (m1 + m2) / element_count;
	}

	/**
	 * Expand the range based upon a single value.
	 * 
	 * @param v
	 */
	public void expand(double ... _v) {
		for( double v : _v ){
			if (Double.isNaN(v))
				return;
			r_min = Math.min(v, r_min);
			r_max = Math.max(v, r_max);
			double m1 = getMean() * element_count + v;
			element_count++;
			mean_value = m1 / element_count;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + r_min + ", " + r_max + "]";
	}


	public float clamp( float val ) {
		return MathX.clamp( val, (float)r_min,  (float)r_max );
	}

}
