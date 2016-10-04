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


import java.util.Vector;

import usf.saav.alma.data.ScalarField2D;
import usf.saav.alma.data.ScalarField3D;

// TODO: Auto-generated Javadoc
/**
 * The Class LayeredVolume.
 */
public class LayeredVolume extends ScalarField3D.Default implements ScalarField3D {

	private Vector<ScalarField2D> layers = new Vector<ScalarField2D>( );
	private int w = Integer.MAX_VALUE, h = Integer.MAX_VALUE;

	/**
	 * Instantiates a new layered volume.
	 */
	public LayeredVolume( ){ }

	/**
	 * Instantiates a new layered volume.
	 *
	 * @param _layers the layers
	 */
	public LayeredVolume( ScalarField2D ... _layers ){
		addLayers(_layers);
	}

	/**
	 * Adds the layers.
	 *
	 * @param _layers the layers
	 */
	public void addLayers( ScalarField2D ... _layers ){
		for( ScalarField2D l : _layers ){
			w = Math.min(w, l.getWidth());
			h = Math.min(h, l.getHeight());
			this.layers.add(l);
		}
	}

	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField3D#getWidth()
	 */
	@Override public int getWidth()  { return w; }
	
	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField3D#getHeight()
	 */
	@Override public int getHeight() { return h; }
	
	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField3D#getDepth()
	 */
	@Override public int getDepth()  { return layers.size(); }

	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField3D#getValue(int, int, int)
	 */
	@Override
	public float getValue(int x, int y, int z) {
		return layers.get(z).getValue(x, y);
	}

	/**
	 * Gets the layer.
	 *
	 * @param i the i
	 * @return the layer
	 */
	public ScalarField2D getLayer(int i) {
		return layers.get(i);
	}
}
