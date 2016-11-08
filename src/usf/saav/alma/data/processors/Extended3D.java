package usf.saav.alma.data.processors;

import usf.saav.alma.data.ScalarField3D;

public class Extended3D extends ScalarField3D.Default implements ScalarField3D {

	private ScalarField3D base;
	private ScalarField3D extension;

	public Extended3D( ScalarField3D base, ScalarField3D extension ){
		this.base = base;
		this.extension = extension;
	}

	@Override public int getWidth() { return Math.max(base.getWidth(),extension.getWidth()); }
	@Override public int getHeight() { return Math.max(base.getHeight(),extension.getHeight()); }
	@Override public int getDepth() { return base.getDepth()+extension.getDepth(); }


	@Override
	public float getValue(int x, int y, int z) {
		if( z < base.getDepth() ){
			return base.getValue( x,y,z );
		}
		return extension.getValue(x, y, z-base.getDepth() );
	}
}
