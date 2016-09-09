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

public class StraightThru3D implements ScalarField3D {

	ScalarField3D src;

	public StraightThru3D( ScalarField3D src ){
		this.src = src;
	}

	@Override public int getSize() { return src.getSize(); }
	@Override public float getValue(int nodeID) { return src.getValue(nodeID); }
	@Override public int[] getNeighbors(int nodeID) { return src.getNeighbors(nodeID); }
	@Override public int getWidth() { return src.getWidth(); }
	@Override public int getHeight() { return src.getHeight(); }
	@Override public int getDepth() { return src.getDepth(); }
	@Override public float getValue(int x, int y, int z) { return src.getValue(x, y, z); }
	@Override public double[] getCoordinate(int x, int y, int z) { return src.getCoordinate(x, y, z); }
	@Override public double[] getValueRange() { return src.getValueRange(); }
}
