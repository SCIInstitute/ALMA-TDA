package usf.saav.alma.data.processors;

import usf.saav.common.Callback;
import usf.saav.common.range.IntRange1D;
import usf.saav.mesh.Mesh;
import usf.saav.scalarfield.ScalarField1D;
import usf.saav.topology.TopoTree;

public class PersistenceSimplifier1D extends PersistenceSimplifierND implements ScalarField1D {

	private ScalarField1D sf;
	
	public PersistenceSimplifier1D(ScalarField1D sf, TopoTree ct, Mesh cl, int z, boolean runImmediately ) {
		this( sf, ct, cl, z, runImmediately, true );
	}
	public PersistenceSimplifier1D(ScalarField1D sf, TopoTree ct, Mesh cl, int z, boolean runImmediately, boolean verbose ) {
		super(sf, ct, cl, new IntRange1D(z), runImmediately);
		this.sf = sf;
	}

	@Override public int 	getWidth() { return sf.getWidth(); }
	//@Override public double getCoordinate(int nodeID) { return sf.getCoordinate(nodeID); }
			  public int	getZ( ){ return z.start(); }

	@Override
	public void setCallback( Object obj, String func_name ) {
		try {
			this.cb = new Callback( obj, func_name, PersistenceSimplifier1D.class );
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

}
