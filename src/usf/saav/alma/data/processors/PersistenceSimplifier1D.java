package usf.saav.alma.data.processors;

import usf.saav.alma.algorithm.mesh.Mesh;
import usf.saav.alma.algorithm.topology.PersistenceSet;
import usf.saav.alma.data.ScalarField1D;
import usf.saav.common.Callback;
import usf.saav.common.range.IntRange1D;

// TODO: Auto-generated Javadoc
/**
 * The Class PersistenceSimplifier1D.
 */
public class PersistenceSimplifier1D extends PersistenceSimplifierND implements ScalarField1D {

	private ScalarField1D sf;
	
	/**
	 * Instantiates a new persistence simplifier 1 D.
	 *
	 * @param sf the sf
	 * @param ct the ct
	 * @param cl the cl
	 * @param z the z
	 * @param runImmediately the run immediately
	 */
	public PersistenceSimplifier1D(ScalarField1D sf, PersistenceSet ct, Mesh cl, int z, boolean runImmediately ) {
		this( sf, ct, cl, z, runImmediately, true );
	}
	
	/**
	 * Instantiates a new persistence simplifier 1 D.
	 *
	 * @param sf the sf
	 * @param ct the ct
	 * @param cl the cl
	 * @param z the z
	 * @param runImmediately the run immediately
	 * @param verbose the verbose
	 */
	public PersistenceSimplifier1D(ScalarField1D sf, PersistenceSet ct, Mesh cl, int z, boolean runImmediately, boolean verbose ) {
		super(sf, ct, cl, new IntRange1D(z), runImmediately);
		this.sf = sf;
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.algorithm.Surface1D#getWidth()
	 */
	@Override public int 	getWidth() { return sf.getWidth(); }
	
	/* (non-Javadoc)
	 * @see usf.saav.alma.data.ScalarField1D#getCoordinate(int)
	 */
	@Override public double getCoordinate(int nodeID) { return sf.getCoordinate(nodeID); }
			  
  			/**
  			 * Gets the z.
  			 *
  			 * @return the z
  			 */
  			public int	getZ( ){ return z.start(); }

	/* (non-Javadoc)
	 * @see usf.saav.alma.data.processors.PersistenceSimplifierND#setCallback(java.lang.Object, java.lang.String)
	 */
	@Override
	public void setCallback( Object obj, String func_name ) {
		try {
			this.cb = new Callback( obj, func_name, PersistenceSimplifier1D.class );
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

}
