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
package usf.saav.alma.data.processors;

import usf.saav.common.range.IntRange1D;
import usf.saav.scalarfield.ScalarField3D;

// TODO: Auto-generated Javadoc
/**
 * The Class Subset3D.
 */
public class Subset3D extends ScalarField3D.Default {
	ScalarField3D src;
	int x0, xN;
	int y0, yN;
	int z0, zN;

	/**
	 * Instantiates a new subset 3 D.
	 *
	 * @param src the src
	 * @param x_range the x range
	 * @param y_range the y range
	 * @param z_range the z range
	 */
	public Subset3D( ScalarField3D src, IntRange1D x_range, IntRange1D y_range, IntRange1D z_range ){
		this.src = src;
		this.x0 = x_range.start();
		this.xN = x_range.length();
		
		this.y0 = y_range.start();
		this.yN = y_range.length();

		this.z0 = z_range.start();
		this.zN = z_range.length();

	}


	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField3D#getWidth()
	 */
	@Override public int getWidth() { return xN; }
	
	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField3D#getHeight()
	 */
	@Override public int getHeight() { return yN; }
	
	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField3D#getDepth()
	 */
	@Override public int getDepth() { return zN; }
	
	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField3D#getValue(int, int, int)
	 */
	@Override public float getValue(int x, int y, int z) { return src.getValue(x+x0, y+y0, z+zN); }

}
