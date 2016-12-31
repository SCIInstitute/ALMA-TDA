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

import usf.saav.scalarfield.ScalarField3D;


public class Extended3D extends ScalarField3D.Default implements ScalarField3D {

	private ScalarField3D base;
	private ScalarField3D extension;

	public Extended3D( ScalarField3D base, ScalarField3D extension ){
		this.base = base;
		this.extension = extension;
	}

	@Override public int getWidth() { return Math.max(base.getWidth(),extension.getWidth()); }
	@Override public int getHeight() { return Math.max(base.getHeight(),extension.getHeight()); }
	@Override public int getDepth() { return base.getDepth()+extension.getDepth(); }


	@Override
	public float getValue(int x, int y, int z) {
		if( z < base.getDepth() ){
			return base.getValue( x,y,z );
		}
		return extension.getValue(x, y, z-base.getDepth() );
	}
}
