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

import processing.core.PApplet;
import processing.core.PImage;
import usf.saav.alma.algorithm.mesh.Mesh;
import usf.saav.alma.algorithm.topology.PersistenceSet;
import usf.saav.alma.data.ScalarField2D;
import usf.saav.common.Callback;
import usf.saav.common.colormap.Colormap;
import usf.saav.common.range.IntRange1D;

public class PersistenceSimplifier2D extends PersistenceSimplifierND implements ScalarField2D {

	private ScalarField2D sf;
	
	public PersistenceSimplifier2D(ScalarField2D sf, PersistenceSet ct, Mesh cl, int z, boolean runImmediately ) {
		this( sf, ct, cl, z, runImmediately, true );
	}
	public PersistenceSimplifier2D(ScalarField2D sf, PersistenceSet ct, Mesh cl, int z, boolean runImmediately, boolean verbose ) {
		super(sf, ct, cl, new IntRange1D(z), runImmediately);
		this.sf = sf;
	}

	@Override public int getWidth() { return sf.getWidth(); }
	@Override public int getHeight() { return sf.getHeight(); }
	@Override public float getValue(int x, int y) { return super.getValue( y*getWidth()+x ); }
	@Override public double[] getCoordinate(int x, int y) { return sf.getCoordinate(x, y); }
	@Override public PImage toPImage(PApplet papplet, Colormap colormap) { return sf.toPImage(papplet, colormap); }
	public int				getZ( ){			  	return z.start(); }

	@Override
	public void setCallback( Object obj, String func_name ) {
		try {
			this.cb = new Callback( obj, func_name, PersistenceSimplifier2D.class );
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

}
