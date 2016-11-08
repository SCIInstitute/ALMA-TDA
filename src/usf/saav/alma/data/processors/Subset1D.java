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

import usf.saav.alma.data.ScalarField1D;
import usf.saav.common.range.IntRange1D;

public class Subset1D extends ScalarField1D.Default {

	ScalarField1D src;
	int x0, xN;

	public Subset1D( ScalarField1D src, IntRange1D x_range ){
		this.src = src;
		this.x0 = x_range.start();
		this.xN = x_range.length();
	}
	
	@Deprecated
	public Subset1D( ScalarField1D src, int [] range ){
		this.src = src;
		if( range.length == 0 ){
			this.x0 = 0;
			this.xN = src.getSize();
		}

		if( range.length == 1 ){
			this.x0 = 0;
			this.xN = range[0];
		}
		if( range.length >= 2 ){
			this.x0 = range[0];
			this.xN = range[1]-range[0]+1;
		}
	}

	@Override public int getWidth() { return xN; }
	@Override public int getSize() { return xN; }
	@Override public float getValue(int nodeID) { return src.getValue(nodeID+x0); }
	@Override public double getCoordinate(int nodeID) { return src.getCoordinate(nodeID+x0); }
}
