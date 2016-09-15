package usf.saav.common.range;

import usf.saav.common.types.Float3;

/**
 * Class for building 3D value ranges.
 * 
 * Last code review: 2005/01/20
 * 
 * @author Paul Rosen
 *
 */
public class FloatRange3D {

	private FloatRange1D br_x, br_y, br_z;

	/**
	 * Default Constructor produces an empty 3D range
	 */
	public FloatRange3D() {
		br_x = new FloatRange1D();
		br_y = new FloatRange1D();
		br_z = new FloatRange1D();
	}

	/**
	 * Constructor that starts with a prefixed range
	 * 
	 * @param x_min
	 * @param x_max
	 * @param y_min
	 * @param y_max
	 * @param z_min
	 * @param z_max
	 */
	public FloatRange3D(double x_min, double x_max, double y_min,
			double y_max, double z_min, double z_max) {
		br_x = new FloatRange1D(x_min, x_max);
		br_y = new FloatRange1D(y_min, y_max);
		br_z = new FloatRange1D(z_min, z_max);
	}

	/**
	 * Get the minimum range value
	 * 
	 * @return
	 */
	public Float3 getMinimum() {
		return new Float3(br_x.getMinimum(), br_y.getMinimum(),
				br_z.getMinimum());
	}

	/**
	 * Get the maximum range value
	 * 
	 * @return
	 */
	public Float3 getMaximum() {
		return new Float3(br_x.getMaximum(), br_y.getMaximum(),
				br_z.getMaximum());
	}

	/**
	 * Get the mean value of the range
	 * 
	 * @return
	 */
	public Float3 getMean() {
		return new Float3(br_x.getMean(), br_y.getMean(), br_z.getMean());
	}

	/**
	 * Get the size of the range
	 * 
	 * @return
	 */
	public Float3 getRange() {
		return new Float3(br_x.getRange(), br_y.getRange(), br_z.getRange());
	}

	/**
	 * Get the exact center of the range
	 * 
	 * @return
	 */
	public Float3 getCenter() {
		return new Float3(br_x.getCenter(), br_y.getCenter(), br_z.getCenter());
	}

	/**
	 * Calculates the radius of the sphere guaranteed to enclose all values.
	 * 
	 * @return
	 */
	public float getRadius() {
		return getRange().Magnitude() / 2.0f;
	}

	/**
	 * Expand the range based upon values of another range.
	 * 
	 * @param br
	 */
	public void expand(FloatRange3D bb) {
		br_x.expand(bb.br_x);
		br_y.expand(bb.br_y);
		br_z.expand(bb.br_z);
	}

	/**
	 * Expand the range based upon a single 3D value.
	 * 
	 * @param v
	 */
	public void expand(double x, double y, double z) {
		br_x.expand(x);
		br_y.expand(y);
		br_z.expand(z);
	}

	/**
	 * Expand the range based upon a single 3D value.
	 * 
	 * @param v
	 */
	public void expand(Float3 xyz) {
		br_x.expand(xyz.x);
		br_y.expand(xyz.y);
		br_z.expand(xyz.z);
	}

	/**
	 * Normalizes a 3D value from [range_min,range_max] to [0,1].
	 * 
	 * @param v
	 *            The value to normalize
	 * @return
	 */
	public Float3 getNormalized(double x, double y, double z) {
		return new Float3(br_x.getNormalized(x), br_y.getNormalized(y),
				br_z.getNormalized(z));
	}

	/**
	 * Normalizes a 3D value from [range_min,range_max] to [0,1].
	 * 
	 * @param v
	 *            The value to normalize
	 * @return
	 */
	public Float3 getNormalized(Float3 xyz) {
		return new Float3(br_x.getNormalized(xyz.x), br_y.getNormalized(xyz.y),
				br_z.getNormalized(xyz.z));
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
	 * Normalizes a value from [range_min,range_max] to [0,1] based upon the z
	 * direction.
	 * 
	 * @param v
	 *            The value to normalize
	 * @return
	 */
	public double getNormalizedZ(double z) {
		return br_z.getNormalized(z);
	}

	/**
	 * Calculate a 3D mean centered value. This function simply offsets the
	 * value by the mean.
	 * 
	 * @param v
	 * @return
	 */
	public Float3 getMeanCentered(double x, double y, double z) {
		return new Float3(br_x.getMeanCentered(x), br_y.getMeanCentered(y),
				br_z.getMeanCentered(z));
	}

	/**
	 * Calculate a 3D mean centered value. This function simply offsets the
	 * value by the mean.
	 * 
	 * @param v
	 * @return
	 */
	public Float3 getMeanCentered(Float3 xyz) {
		return new Float3(br_x.getMeanCentered(xyz.x),
				br_y.getMeanCentered(xyz.y), br_z.getMeanCentered(xyz.z));
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
	 * Calculate a mean centered value based upon the z direction. This function
	 * simply offsets the value by the mean.
	 * 
	 * @param v
	 * @return
	 */
	public double getMeanCenteredZ(double z) {
		return br_z.getMeanCentered(z);
	}

	/**
	 * Check to see if a value is outside of the range.
	 * 
	 * @param p
	 * @return
	 */
	public boolean isOutside(Float3 p) {
		return br_x.isOutside(p.x) || br_y.isOutside(p.y)
				|| br_z.isOutside(p.z);
	}

	/**
	 * Check to see if a value is inside of the range.
	 * 
	 * @param p
	 * @return
	 */
	public boolean isInside(Float3 p) {
		return !isOutside(p);
	}
	

	public boolean isInside(double x, double y, double z) {
		return !isOutside(x,y,z);
	}

	private boolean isOutside(double x, double y, double z) {
		return br_x.isOutside(x) || br_y.isOutside(y)
				|| br_z.isOutside(z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[ (" + br_x.getMinimum() + "," + br_y.getMinimum() + ","
				+ br_z.getMinimum() + ")->(" + br_x.getMaximum() + ","
				+ br_y.getMaximum() + "," + br_z.getMaximum() + ") ]";
	}


}
