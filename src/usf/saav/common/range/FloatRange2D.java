package usf.saav.common.range;

import usf.saav.common.types.Float2;

/**
 * Class for building 2D value ranges.
 * 
 * Last code review: 2005/01/20
 * 
 * @author Paul Rosen
 *
 */
public class FloatRange2D {

	private FloatRange1D br_x, br_y;

	/**
	 * Default Constructor produces an empty 2D range
	 */
	public FloatRange2D() {
		br_x = new FloatRange1D();
		br_y = new FloatRange1D();
	}

	/**
	 * Constructor that starts with a prefixed range
	 * 
	 * @param x_min
	 * @param x_max
	 * @param y_min
	 * @param y_max
	 */
	public FloatRange2D(double x_min, double x_max, double y_min,
			double y_max) {
		br_x = new FloatRange1D(x_min, x_max);
		br_y = new FloatRange1D(y_min, y_max);
	}

	/**
	 * Constructor that starts with a prefixed range
	 * 
	 * @param range_x
	 * @param range_y
	 */
	public FloatRange2D(FloatRange1D range_x, FloatRange1D range_y) {
		br_x = range_x.clone();
		br_y = range_y.clone();
	}

	/**
	 * Get the minimum range value
	 * 
	 * @return
	 */
	public Float2 getMinimum() {
		return new Float2(br_x.getMinimum(), br_y.getMinimum());
	}

	/**
	 * Get the maximum range value
	 * 
	 * @return
	 */
	public Float2 getMaximum() {
		return new Float2(br_x.getMaximum(), br_y.getMaximum());
	}

	/**
	 * Get the mean value of the range
	 * 
	 * @return
	 */
	public Float2 getMean() {
		return new Float2(br_x.getMean(), br_y.getMean());
	}

	/**
	 * Get the size of the range
	 * 
	 * @return
	 */
	public Float2 getRange() {
		return new Float2(br_x.getRange(), br_y.getRange());
	}

	/**
	 * Get the exact center of the range
	 * 
	 * @return
	 */
	public Float2 getCenter() {
		return new Float2(br_x.getCenter(), br_y.getCenter());
	}

	/**
	 * Normalizes a 2D value from [range_min,range_max] to [0,1].
	 * 
	 * @param v
	 *            The value to normalize
	 * @return
	 */
	public Float2 getNormalized(double x, double y) {
		return new Float2(br_x.getNormalized(x), br_y.getNormalized(y));
	}

	/**
	 * Normalizes a 2D value from [range_min,range_max] to [0,1].
	 * 
	 * @param v
	 *            The value to normalize
	 * @return
	 */
	public Float2 getNormalized(Float2 xy) {
		return new Float2(br_x.getNormalized(xy.x), br_y.getNormalized(xy.y));
	}

	/**
	 * Normalizes a value from [range_min,range_max] to [0,1] based upon the x
	 * direction.
	 * 
	 * @param v
	 *            The value to normalize
	 * @return
	 */
	public double getNormalizedX(double x) {
		return br_x.getNormalized(x);
	}

	/**
	 * Normalizes a value from [range_min,range_max] to [0,1] based upon the y
	 * direction.
	 * 
	 * @param v
	 *            The value to normalize
	 * @return
	 */
	public double getNormalizedY(double y) {
		return br_y.getNormalized(y);
	}

	/**
	 * Calculate a 2D mean centered value. This function simply offsets the
	 * value by the mean.
	 * 
	 * @param v
	 * @return
	 */
	public Float2 getMeanCentered(double x, double y) {
		return new Float2(br_x.getMeanCentered(x), br_y.getMeanCentered(y));
	}

	/**
	 * Calculate a 2D mean centered value. This function simply offsets the
	 * value by the mean.
	 * 
	 * @param v
	 * @return
	 */
	public Float2 getMeanCentered(Float2 xy) {
		return new Float2(br_x.getMeanCentered(xy.x),
				br_y.getMeanCentered(xy.y));
	}

	/**
	 * Calculate a mean centered value based upon the x direction. This function
	 * simply offsets the value by the mean.
	 * 
	 * @param v
	 * @return
	 */
	public double getMeanCenteredX(double x) {
		return br_x.getMeanCentered(x);
	}

	/**
	 * Calculate a mean centered value based upon the y direction. This function
	 * simply offsets the value by the mean.
	 * 
	 * @param v
	 * @return
	 */
	public double getMeanCenteredY(double y) {
		return br_y.getMeanCentered(y);
	}

	/**
	 * Expand the range based upon values of another range.
	 * 
	 * @param br
	 */
	public void expand(FloatRange2D bb) {
		br_x.expand(bb.br_x);
		br_y.expand(bb.br_y);
	}

	/**
	 * Expand the range based upon a single 2D value.
	 * 
	 * @param v
	 */
	public void expand(double x, double y) {
		br_x.expand(x);
		br_y.expand(y);
	}

	/**
	 * Expand the range based upon a single 2D value.
	 * 
	 * @param v
	 */
	public void expand(Float2 xy) {
		br_x.expand(xy.x);
		br_y.expand(xy.y);
	}

	/**
	 * Check to see if a value is outside of the range.
	 * 
	 * @param p
	 * @return
	 */
	public boolean isOutside(Float2 p) {
		return br_x.isOutside(p.x) || br_y.isOutside(p.y);
	}

	/**
	 * Check to see if a value is inside of the range.
	 * 
	 * @param p
	 * @return
	 */
	public boolean isInside(Float2 p) {
		return !isOutside(p);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[ (" + br_x.getMinimum() + "," + br_y.getMinimum() + ")->("
				+ br_x.getMaximum() + "," + br_y.getMaximum() + ") ]";
	}

}
