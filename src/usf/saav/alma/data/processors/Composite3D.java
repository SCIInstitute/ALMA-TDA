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
import usf.saav.scalarfield.ScalarField3D;
import usf.saav.scalarfield.ScalarFieldND;

public class Composite3D extends ScalarField3D.Default implements ScalarField3D {

	private ScalarField3D base;
	private ScalarFieldND composite;
	private int offX, offY, offZ;

	public Composite3D( ScalarField3D base, ScalarField3D composite, int offX, int offY, int offZ ){
		this.base = base;
		this.composite = composite;
		this.offX = offX;
		this.offY = offY;
		this.offZ = offZ;
	}

	@Override public int getWidth() { return base.getWidth(); }
	@Override public int getHeight() { return base.getHeight(); }
	@Override public int getDepth() { return base.getDepth(); }


	@Override
	public float getValue(int x, int y, int z) {
		if( composite instanceof ScalarField2D ){
			if( x >= offX && x < (offX+((ScalarField2D)composite).getWidth()) &&
				y >= offY && y < (offY+((ScalarField2D)composite).getHeight()) &&
				z == offZ ){
						return ((ScalarField2D)composite).getValue(x-offX, y-offY);
				}
				return base.getValue(x,y,z);
		}
		if( composite instanceof ScalarField3D ){
			if( x >= offX && x < (offX+((ScalarField3D)composite).getWidth()) &&
				y >= offY && y < (offY+((ScalarField3D)composite).getHeight()) &&
				z >= offZ && y < (offZ+((ScalarField3D)composite).getDepth()) ){
						return ((ScalarField3D)composite).getValue(x-offX, y-offY, z-offZ);
				}
				return base.getValue(x, y, z);
		}
		return base.getValue(x, y, z);
	}
}
