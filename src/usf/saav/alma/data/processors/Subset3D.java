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
import usf.saav.common.range.IntRange1D;

public class Subset3D extends ScalarField3D.Default {
	ScalarField3D src;
	int x0, xN;
	int y0, yN;
	int z0, zN;

	public Subset3D( ScalarField3D src, IntRange1D x_range, IntRange1D y_range, IntRange1D z_range ){
		this.src = src;
		this.x0 = x_range.start();
		this.xN = x_range.length();
		
		this.y0 = y_range.start();
		this.yN = y_range.length();

		this.z0 = z_range.start();
		this.zN = z_range.length();

	}

	@Deprecated
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

	@Override public int getWidth() { return xN; }
	@Override public int getHeight() { return yN; }
	@Override public int getDepth() { return zN; }
	@Override public float getValue(int x, int y, int z) { return src.getValue(x+x0, y+y0, z+zN); }
	@Override public double[] getCoordinate(int x, int y, int z) { return src.getCoordinate(x+x0, y+y0, z+z0); }
}
