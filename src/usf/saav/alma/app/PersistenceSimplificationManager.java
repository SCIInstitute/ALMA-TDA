package usf.saav.alma.app;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import usf.saav.alma.data.processors.PersistenceSimplifier2D;
import usf.saav.alma.data.processors.PersistenceSimplifier3D;
import usf.saav.alma.data.processors.PersistenceSimplifierND;
import usf.saav.alma.util.ContourTreeThread;
import usf.saav.scalarfield.ScalarField2D;
import usf.saav.scalarfield.ScalarField3D;

public class PersistenceSimplificationManager {
	
	public Map< Integer, PersistenceSimplifierND > psf_map = new HashMap< Integer, PersistenceSimplifierND >( );
	ContourTreeManager ctm;
	DataViewManager dvm;
	ExecutorService threadPool = Executors.newFixedThreadPool(4);

	public PersistenceSimplificationManager( DataViewManager dvm, ContourTreeManager ctm ){
		this.ctm = ctm;
		this.dvm = dvm;
	}
	
	
	public void refreshSimplification( ) {
		
		if( !ctm.ctt_map.containsKey( dvm.curZ.get() ) ) return;
		if( psf_map.containsKey( dvm.curZ.get() ) ) return;
		
		ContourTreeThread ctt = ctm.ctt_map.get( dvm.curZ.get() );
		if( ctt.getScalarField() == null || ctt.getTree() == null ) return;
		
		if( ctt.getScalarField() instanceof ScalarField2D ){
			PersistenceSimplifier2D psf = new PersistenceSimplifier2D( (ScalarField2D)ctt.getScalarField(), ctt.getTree(), ctt.getComponentList(), dvm.curZ.get(), false );
			psf.setCallback( dvm, "completedSimplification" );
			threadPool.execute( psf );
		}
		
		if( ctt.getScalarField() instanceof ScalarField3D ){
			PersistenceSimplifier3D psf = new PersistenceSimplifier3D( (ScalarField3D)ctt.getScalarField(), ctt.getTree(), ctt.getComponentList(), ctt.getZ(), false );
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
				PersistenceSimplifier2D psf = new PersistenceSimplifier2D( (ScalarField2D)ctt.getScalarField(), ctt.getTree(), ctt.getComponentList(), z, false );
				psf.setCallback( dvm, "completedSimplification" );
				threadPool.execute( psf );
			}
		}
		
	}
	

}
