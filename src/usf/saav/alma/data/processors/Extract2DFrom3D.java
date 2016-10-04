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
import usf.saav.alma.data.ScalarField3D;

// TODO: Auto-generated Javadoc
/**
 * The Class Extract2DFrom3D.
 */
public class Extract2DFrom3D extends ScalarField2D.Default implements ScalarField2D {

	private ScalarField3D sf;
	private int layer;

	/**
	 * Instantiates a new extract 2 D from 3 D.
	 *
	 * @param sf the sf
	 * @param layer the layer
	 */
	public Extract2DFrom3D( ScalarField3D sf, int layer ){
		this.sf = sf;
		this.layer = layer;
	}
	
	/* (non-Javadoc)
	 * @see usf.saav.common.algorithm.Surface2D#getWidth()
	 */
	@Override public int getWidth() { return sf.getWidth(); }
	
	/* (non-Javadoc)
	 * @see usf.saav.common.algorithm.Surface2D#getHeight()
	 */
	@Override public int getHeight() { return sf.getHeight(); }
	
	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField2D#getValue(int, int)
	 */
	@Override public float getValue(int x, int y) { return sf.getValue(x, y, layer); }

}
