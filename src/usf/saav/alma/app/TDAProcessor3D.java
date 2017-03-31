package usf.saav.alma.app;

import java.io.IOException;

import usf.saav.alma.data.fits.FitsReader;
import usf.saav.common.BasicObject;
import usf.saav.common.range.IntRange1D;
import usf.saav.mesh.ConnectedComponentMesh;
import usf.saav.mesh.ScalarFieldMesh;
import usf.saav.scalarfield.PersistenceSimplifier3D;
import usf.saav.scalarfield.ScalarField3D;
import usf.saav.topology.contour.PseudoContourTree;

public class TDAProcessor3D extends BasicObject {

	PersistenceSimplifier3D ps3d;
	ScalarField3D vol;

	public TDAProcessor3D( ){
		super(true);
	}

	public void process( FitsReader fits, IntRange1D xr, IntRange1D yr, IntRange1D zr, float simplification ){

		try {
			process( fits.getVolume( xr,  yr,  zr, 0 ), simplification );
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	public void process( ScalarField3D _vol, float simplification ){
		
		vol = _vol;

		this.print_info_message("Constructing Mesh");
		ConnectedComponentMesh cl = new ConnectedComponentMesh( new ScalarFieldMesh( vol ) );
		this.print_info_message("Constructing Tree");
		PseudoContourTree ct = new PseudoContourTree( cl );

		System.out.println( "Regional Maximum Persistence: " + ct.getMaxPersistence() );

		this.print_info_message("Simplifying Tree");
		ct.setPersistentSimplification( simplification );
		this.print_info_message("Simplfying Field");
		ps3d = new PersistenceSimplifier3D( vol, ct, cl, true );

	}

}
