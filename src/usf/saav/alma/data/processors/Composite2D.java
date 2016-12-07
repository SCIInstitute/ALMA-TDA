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

import usf.saav.scalarfield.ScalarField2D;


// TODO: Auto-generated Javadoc
/**
 * The Class Composite2D.
 */
public class Composite2D extends ScalarField2D.Default implements ScalarField2D {

	private ScalarField2D base;
	private ScalarField2D composite;
	private int offX, offY;

	/**
	 * Instantiates a new composite 2 D.
	 *
	 * @param base the base
	 * @param composite the composite
	 * @param offX the off X
	 * @param offY the off Y
	 */
	public Composite2D( ScalarField2D base, ScalarField2D composite, int offX, int offY ){
		this.base = base;
		this.composite = composite;
		this.offX = offX;
		this.offY = offY;
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.algorithm.Surface2D#getWidth()
	 */
	@Override public int getWidth() { return base.getWidth(); }
	
	/* (non-Javadoc)
	 * @see usf.saav.common.algorithm.Surface2D#getHeight()
	 */
	@Override public int getHeight() { return base.getHeight(); }

	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField2D#getValue(int, int)
	 */
	@Override
	public float getValue(int x, int y) {
		if( x >= offX && x < (offX+composite.getWidth()) &&
			y >= offY && y < (offY+composite.getHeight()) ){
				return composite.getValue(x-offX, y-offY);
		}
		return base.getValue(x, y);
	}
}
