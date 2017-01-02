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
package usf.saav.alma.app;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import usf.saav.alma.util.ContourTreeThread;
import usf.saav.common.range.IntRange1D;
import usf.saav.mesh.Mesh;
import usf.saav.scalarfield.PersistenceSimplifier2D;
import usf.saav.scalarfield.PersistenceSimplifier3D;
import usf.saav.scalarfield.PersistenceSimplifierND;
import usf.saav.scalarfield.ScalarField2D;
import usf.saav.scalarfield.ScalarField3D;
import usf.saav.topology.TopoTree;

public class PersistenceSimplificationManager {
	
	public Map< Integer, PersistenceSimplifierND > psf_map = new HashMap< Integer, PersistenceSimplifierND >( );
	ContourTreeManager ctm;
	DataViewManager dvm;
	ExecutorService threadPool = Executors.newFixedThreadPool(4);

	public PersistenceSimplificationManager( DataViewManager dvm, ContourTreeManager ctm ){
		this.ctm = ctm;
		this.dvm = dvm;
	}
	
	
	class PS2D extends PersistenceSimplifier2D {
		int z;
		public PS2D(ScalarField2D sf, TopoTree ct, Mesh cl, int z, boolean runImmediately) {
			super(sf, ct, cl, runImmediately);
			this.z = z;
		}
		public int getZ(){ return z; }
	}

	class PS3D extends PersistenceSimplifier3D {
		IntRange1D z;
		public PS3D(ScalarField3D sf, TopoTree ct, Mesh cl, IntRange1D z, boolean runImmediately) {
			super(sf, ct, cl, runImmediately);
			this.z = z;
		}
		public IntRange1D getZ(){ return z; }
	}

	public void refreshSimplification( ) {
		System.out.println("rsimp1");
		if( !ctm.ctt_map.containsKey( dvm.curZ.get() ) ) return;
		System.out.println("rsimp2");
		if( psf_map.containsKey( dvm.curZ.get() ) ) return;
		System.out.println("rsimp3");
		
		ContourTreeThread ctt = ctm.ctt_map.get( dvm.curZ.get() );
		if( ctt.getScalarField() == null || ctt.getTree() == null ) return;
		
		if( ctt.getScalarField() instanceof ScalarField2D ){
			PersistenceSimplifier2D psf = new PS2D( (ScalarField2D)ctt.getScalarField(), ctt.getTree(), ctt.getComponentList(), dvm.curZ.get(), false );
			psf.setCallback( dvm, "completedSimplification" );
			threadPool.execute( psf );
		}
		
		if( ctt.getScalarField() instanceof ScalarField3D ){
			PersistenceSimplifier3D psf = new PS3D( (ScalarField3D)ctt.getScalarField(), ctt.getTree(), ctt.getComponentList(), ctt.getZ(), false );
			psf.setCallback( dvm, "completedSimplification" );
			threadPool.execute( psf );
		}
		
		for( int z = dvm.z0.get(); z <= dvm.z1.get(); z++ ){
			if(  psf_map.containsKey(z) ) continue;
			if( !ctm.ctt_map.containsKey(z) ) continue;
			if( dvm.curZ.get() == z ) continue;
			ctt = ctm.ctt_map.get( z );
			if( ctt.getScalarField() == null || ctt.getTree() == null ) continue;
			if( ctt.getScalarField() instanceof ScalarField2D ){
				PersistenceSimplifier2D psf = new PS2D( (ScalarField2D)ctt.getScalarField(), ctt.getTree(), ctt.getComponentList(), z, false );
				psf.setCallback( dvm, "completedSimplification" );
				threadPool.execute( psf );
			}
		}
		
	}
	

}
