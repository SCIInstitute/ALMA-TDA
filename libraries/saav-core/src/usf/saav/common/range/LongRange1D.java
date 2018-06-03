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
 * Class for building 1D value ranges.
 * 
 * Last code review: 2005/01/20
 * 
 * @author Paul Rosen
 *
 */
public class LongRange1D {

	private long r_min = Long.MAX_VALUE;
	private long r_max = Long.MIN_VALUE;
	private long element_count = 0;

	/**
	 * Default constructor creates an invalid range
	 */
	public LongRange1D() {
	}

	/**
	 * Constructor that starts with a prefixed range
	 * 
	 * @param _min
	 * @param _max
	 */
	public LongRange1D(long _min, long _max) {
		r_min = _min;
		r_max = _max;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public LongRange1D clone() {
		LongRange1D ret = new LongRange1D();
		ret.r_min = r_min;
		ret.r_max = r_max;
		ret.element_count = element_count;
		return ret;
	}

	/**
	 * Get the minimum range value
	 * 
	 * @return
	 */
	public long getMinimum() {
		return r_min;
	}

	/**
	 * Get the maximum range value
	 * 
	 * @return
	 */
	public long getMaximum() {
		return r_max;
	}

	/**
	 * Get the size of the range
	 * 
	 * @return
	 */
	public long getRange() {
		return r_max - r_min;
	}

	/**
	 * Get the exact center of the range
	 * 
	 * @return
	 */
	public long getCenter() {
		return (long) ((r_max + r_min) / 2.0);
	}

	/**
	 * Normalizes a value from [range_min,range_max] to [0,1].
	 * 
	 * @param v
	 * @return
	 */
	public double getNormalized(long v) {
		return (double) (v - r_min) / (double) (r_max - r_min);
	}

	/**
	 * Expand the range based upon values of another range.
	 * 
	 * @param br
	 */
	public void expand(LongRange1D br) {
		r_min = Math.min(br.r_min, r_min);
		r_max = Math.max(br.r_max, r_max);
		element_count += br.element_count;
	}

	/**
	 * Expand the range based upon a single value.
	 * 
	 * @param v
	 */
	public void expand(long v) {
		r_min = Math.min(v, r_min);
		r_max = Math.max(v, r_max);
		element_count++;
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

}
