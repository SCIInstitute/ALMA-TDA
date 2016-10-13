package usf.saav.alma.data.processors;

import usf.saav.alma.data.ScalarField1D;
import usf.saav.alma.data.ScalarField3D;

// TODO: Auto-generated Javadoc
/**
 * The Class Extract1Dfrom3D.
 */
public class Extract1Dfrom3D extends ScalarField1D.Default implements ScalarField1D {

		private ScalarField3D sf;
		private int x, y;

		/**
		 * Instantiates a new extract 1 dfrom 3 D.
		 *
		 * @param sf the sf
		 * @param x the x
		 * @param y the y
		 */
		public Extract1Dfrom3D( ScalarField3D sf, int x, int y ){
			this.sf = sf;
			this.x = x;
			this.y = y;
		}
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarFieldND#getValue(int)
		 */
		@Override public float getValue(int z) { return sf.getValue(x, y, z); }
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarFieldND#getSize()
		 */
		@Override public int   getSize() { return sf.getDepth(); }
		
		/* (non-Javadoc)
		 * @see usf.saav.common.algorithm.Surface1D#getWidth()
		 */
		@Override public int   getWidth() { return sf.getDepth(); }

	}
