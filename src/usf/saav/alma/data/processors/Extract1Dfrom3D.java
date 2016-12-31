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

import usf.saav.scalarfield.ScalarField1D;
import usf.saav.scalarfield.ScalarField3D;


// TODO: Auto-generated Javadoc
/**
 * The Class Extract1Dfrom3D.
 */
public class Extract1Dfrom3D extends ScalarField1D.Default implements ScalarField1D {

		private ScalarField3D sf;
		private int x, y;

		/**
		 * Instantiates a new extract 1 dfrom 3 D.
		 *
		 * @param sf the sf
		 * @param x the x
		 * @param y the y
		 */
		public Extract1Dfrom3D( ScalarField3D sf, int x, int y ){
			this.sf = sf;
			this.x = x;
			this.y = y;
		}
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarFieldND#getValue(int)
		 */
		@Override public float getValue(int z) { return sf.getValue(x, y, z); }
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarFieldND#getSize()
		 */
		@Override public int   getSize() { return sf.getDepth(); }
		
		/* (non-Javadoc)
		 * @see usf.saav.common.algorithm.Surface1D#getWidth()
		 */
		@Override public int   getWidth() { return sf.getDepth(); }

	}
