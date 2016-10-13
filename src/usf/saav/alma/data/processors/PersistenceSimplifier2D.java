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

import usf.saav.alma.algorithm.mesh.Mesh;
import usf.saav.alma.algorithm.topology.PersistenceSet;
import usf.saav.alma.data.ScalarField2D;
import usf.saav.common.Callback;
import usf.saav.common.colormap.Colormap;
import usf.saav.common.mvc.swing.TGraphics;
import usf.saav.common.mvc.swing.TImage;
import usf.saav.common.range.IntRange1D;

// TODO: Auto-generated Javadoc
/**
 * The Class PersistenceSimplifier2D.
 */
public class PersistenceSimplifier2D extends PersistenceSimplifierND implements ScalarField2D {

	private ScalarField2D sf;
	
	/**
	 * Instantiates a new persistence simplifier 2 D.
	 *
	 * @param sf the sf
	 * @param ct the ct
	 * @param cl the cl
	 * @param z the z
	 * @param runImmediately the run immediately
	 */
	public PersistenceSimplifier2D(ScalarField2D sf, PersistenceSet ct, Mesh cl, int z, boolean runImmediately ) {
		this( sf, ct, cl, z, runImmediately, true );
	}
	
	/**
	 * Instantiates a new persistence simplifier 2 D.
	 *
	 * @param sf the sf
	 * @param ct the ct
	 * @param cl the cl
	 * @param z the z
	 * @param runImmediately the run immediately
	 * @param verbose the verbose
	 */
	public PersistenceSimplifier2D(ScalarField2D sf, PersistenceSet ct, Mesh cl, int z, boolean runImmediately, boolean verbose ) {
		super(sf, ct, cl, new IntRange1D(z), runImmediately);
		this.sf = sf;
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
	@Override public float getValue(int x, int y) { return super.getValue( y*getWidth()+x ); }
	
	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField2D#getCoordinate(int, int)
	 */
	@Override public double[] getCoordinate(int x, int y) { return sf.getCoordinate(x, y); }
	
	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField2D#toPImage(usf.saav.common.mvc.swing.TGraphics, usf.saav.common.colormap.Colormap)
	 */
	@Override public TImage toPImage(TGraphics g, Colormap colormap) { return sf.toPImage(g, colormap); }
	
	/**
	 * Gets the z.
	 *
	 * @return the z
	 */
	public int				getZ( ){			  	return z.start(); }

	/* (non-Javadoc)
	 * @see usf.saav.alma.data.processors.PersistenceSimplifierND#setCallback(java.lang.Object, java.lang.String)
	 */
	@Override
	public void setCallback( Object obj, String func_name ) {
		try {
			this.cb = new Callback( obj, func_name, PersistenceSimplifier2D.class );
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

}
