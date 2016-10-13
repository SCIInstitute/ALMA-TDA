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

import usf.saav.alma.data.ScalarField3D;

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
	public Subset3D( ScalarField3D src, int [] x_range, int [] y_range, int [] z_range ){
		this.src = src;

		if( x_range.length == 0 ){
			this.x0 = 0;
			this.xN = src.getWidth();
		}
		if( x_range.length == 1 ){
			this.x0 = 0;
			this.xN = x_range[0];
		}
		if( x_range.length >= 2 ){
			this.x0 = x_range[0];
			this.xN = x_range[1]-x_range[0]+1;
		}

		if( y_range.length == 0 ){
			this.y0 = 0;
			this.yN = src.getHeight();
		}
		if( y_range.length == 1 ){
			this.y0 = 0;
			this.yN = y_range[0];
		}
		if( y_range.length >= 2 ){
			this.y0 = y_range[0];
			this.yN = y_range[1]-y_range[0]+1;
		}

		if( z_range.length == 0 ){
			this.z0 = 0;
			this.zN = src.getDepth();
		}
		if( z_range.length == 1 ){
			this.z0 = 0;
			this.zN = z_range[0];
		}
		if( z_range.length >= 2 ){
			this.z0 = z_range[0];
			this.zN = z_range[1]-z_range[0]+1;
		}
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
	
	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField3D.Default#getCoordinate(int, int, int)
	 */
	@Override public double[] getCoordinate(int x, int y, int z) { return src.getCoordinate(x+x0, y+y0, z+z0); }
}
