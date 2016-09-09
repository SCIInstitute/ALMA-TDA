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

import usf.saav.alma.data.ScalarField2D;

public class Composite2D extends ScalarField2D.Default implements ScalarField2D {

	private ScalarField2D base;
	private ScalarField2D composite;
	private int offX, offY;

	public Composite2D( ScalarField2D base, ScalarField2D composite, int offX, int offY ){
		this.base = base;
		this.composite = composite;
		this.offX = offX;
		this.offY = offY;
	}

	@Override public int getWidth() { return base.getWidth(); }
	@Override public int getHeight() { return base.getHeight(); }

	@Override
	public float getValue(int x, int y) {
		if( x >= offX && x < (offX+composite.getWidth()) &&
			y >= offY && y < (offY+composite.getHeight()) ){
				return composite.getValue(x-offX, y-offY);
		}
		return base.getValue(x, y);
	}
}
