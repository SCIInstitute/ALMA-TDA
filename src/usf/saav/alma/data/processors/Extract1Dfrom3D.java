package usf.saav.alma.data.processors;

import usf.saav.scalarfield.ScalarField1D;
import usf.saav.scalarfield.ScalarField3D;


public class Extract1Dfrom3D extends ScalarField1D.Default implements ScalarField1D {

		private ScalarField3D sf;
		private int x, y;

		public Extract1Dfrom3D( ScalarField3D sf, int x, int y ){
			this.sf = sf;
			this.x = x;
			this.y = y;
		}
		
		@Override public float getValue(int z) { return sf.getValue(x, y, z); }
		@Override public int   getSize() { return sf.getDepth(); }
		@Override public int   getWidth() { return sf.getDepth(); }

	}
