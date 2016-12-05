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
		
		if( !ctm.ctt_map.containsKey( dvm.curZ.get() ) ) return;
		if( psf_map.containsKey( dvm.curZ.get() ) ) return;
		
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
