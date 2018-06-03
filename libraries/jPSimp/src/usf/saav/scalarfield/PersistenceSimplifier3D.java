/*
 *     jPSimp - Persistence calculation and simplification of scalar fields.
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
package usf.saav.scalarfield;

import usf.saav.common.monitoredvariables.Callback;
import usf.saav.mesh.Mesh;
import usf.saav.topology.TopoTree;

public class PersistenceSimplifier3D extends PersistenceSimplifierND implements ScalarField3D {

	private ScalarField3D sf;
	

	public PersistenceSimplifier3D(ScalarField3D sf, TopoTree ct, Mesh cl, boolean runImmediately ) {
		this( sf, ct, cl, runImmediately, true );
	}
	
	public PersistenceSimplifier3D(ScalarField3D sf, TopoTree ct, Mesh cl, boolean runImmediately, boolean verbose ) {
		super(sf, ct, cl, runImmediately);
		this.sf = sf;
	}

	@Override public int getWidth() { return sf.getWidth(); }
	@Override public int getHeight() { return sf.getHeight(); }
	@Override public int getDepth() { return sf.getDepth(); }
	@Override public float getValue(int x, int y, int z) { return super.getValue( z*getWidth()*getHeight() + y*getWidth() + x ); }

	@Override
	public void setCallback( Object obj, String func_name ) {
		try {
			this.cb = new Callback( obj, func_name, PersistenceSimplifier3D.class );
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	
}
