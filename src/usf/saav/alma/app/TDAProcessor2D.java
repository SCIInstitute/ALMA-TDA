package usf.saav.alma.app;

import java.io.IOException;

import usf.saav.alma.data.fits.FitsReader;
import usf.saav.common.BasicObject;
import usf.saav.common.range.IntRange1D;
import usf.saav.mesh.ConnectedComponentMesh;
import usf.saav.mesh.ScalarFieldMesh;
import usf.saav.scalarfield.PersistenceSimplifier2D;
import usf.saav.scalarfield.ScalarField2D;
import usf.saav.topology.contour.PseudoContourTree;

public class TDAProcessor2D extends BasicObject {

	PersistenceSimplifier2D ps2d;
	ScalarField2D slice;

	public TDAProcessor2D( ){
		super(true);
	}

	public void process( FitsReader fits, IntRange1D xr, IntRange1D yr, int z, float simplification ){

		try {
			process( fits.getSlice( xr,  yr,  z, 0 ), simplification );
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	public void process( ScalarField2D _slice, float simplification ){
		
		slice = _slice;

		this.print_info_message("Constructing Mesh");
		ConnectedComponentMesh cl = new ConnectedComponentMesh( new ScalarFieldMesh( slice ) );
		this.print_info_message("Constructing Tree");
		PseudoContourTree ct = new PseudoContourTree( cl );

		System.out.println( "Regional Maximum Persistence: " + ct.getMaxPersistence() );

		this.print_info_message("Simplifying Tree");
		ct.setPersistentSimplification( simplification );
		this.print_info_message("Simplfying Field");
		ps2d = new PersistenceSimplifier2D( slice, ct, cl, true );

	}

}
