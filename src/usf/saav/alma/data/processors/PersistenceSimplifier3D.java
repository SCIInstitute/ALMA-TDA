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
import usf.saav.alma.data.ScalarField3D;
import usf.saav.common.Callback;
import usf.saav.common.range.IntRange1D;

// TODO: Auto-generated Javadoc
/**
 * The Class PersistenceSimplifier3D.
 */
public class PersistenceSimplifier3D extends PersistenceSimplifierND implements ScalarField3D {

	private ScalarField3D sf;
	

	/**
	 * Instantiates a new persistence simplifier 3 D.
	 *
	 * @param sf the sf
	 * @param ct the ct
	 * @param cl the cl
	 * @param z the z
	 * @param runImmediately the run immediately
	 */
	public PersistenceSimplifier3D(ScalarField3D sf, PersistenceSet ct, Mesh cl, IntRange1D z, boolean runImmediately ) {
		this( sf, ct, cl, z, runImmediately, true );
	}
	
	/**
	 * Instantiates a new persistence simplifier 3 D.
	 *
	 * @param sf the sf
	 * @param ct the ct
	 * @param cl the cl
	 * @param z the z
	 * @param runImmediately the run immediately
	 * @param verbose the verbose
	 */
	public PersistenceSimplifier3D(ScalarField3D sf, PersistenceSet ct, Mesh cl, IntRange1D z, boolean runImmediately, boolean verbose ) {
		super(sf, ct, cl, z, runImmediately);
		this.sf = sf;
	}

	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField3D#getWidth()
	 */
	@Override public int getWidth() { return sf.getWidth(); }
	
	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField3D#getHeight()
	 */
	@Override public int getHeight() { return sf.getHeight(); }
	
	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField3D#getDepth()
	 */
	@Override public int getDepth() { return sf.getDepth(); }
	
	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField3D#getValue(int, int, int)
	 */
	@Override public float getValue(int x, int y, int z) { return super.getValue( z*getWidth()*getHeight() + y*getWidth() + x ); }
	
	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField3D#getCoordinate(int, int, int)
	 */
	@Override public double[] getCoordinate(int x, int y, int z) { return sf.getCoordinate(x, y, z); }
	
	/**
	 * Gets the z.
	 *
	 * @return the z
	 */
	public IntRange1D getZ( ){ return z; }

	/* (non-Javadoc)
	 * @see usf.saav.alma.data.processors.PersistenceSimplifierND#setCallback(java.lang.Object, java.lang.String)
	 */
	@Override
	public void setCallback( Object obj, String func_name ) {
		try {
			this.cb = new Callback( obj, func_name, PersistenceSimplifier3D.class );
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	
}
