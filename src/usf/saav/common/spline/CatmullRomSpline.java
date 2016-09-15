package usf.saav.common.spline;

import java.util.Vector;

import usf.saav.common.MathX;
import usf.saav.common.types.Float2;

public abstract class CatmullRomSpline extends Spline {

	//public CatmullRomSpline( Float2 ... p){ super(p); }
	public CatmullRomSpline( ){ super(); }
	
	
	@Override
	public Float2 interpolate(float t) {
		
		int idx0 = MathX.clamp( (int)(t * size() - 1), 0, size()-1 );
		int idx1 = MathX.clamp( (int)(t * size() + 0), 0, size()-1 );
		int idx2 = MathX.clamp( (int)(t * size() + 1), 0, size()-1 );
		int idx3 = MathX.clamp( (int)(t * size() + 2), 0, size()-1 );
		float off = (float) (t * size() - Math.floor( t * size() ));
	
		float x = MathX.Interpolate.CatmullRom( getControlPoint(idx0).x, getControlPoint(idx1).x, getControlPoint(idx2).x, getControlPoint(idx3).x, off );
		float y = MathX.Interpolate.CatmullRom( getControlPoint(idx0).y, getControlPoint(idx1).y, getControlPoint(idx2).y, getControlPoint(idx3).y, off );
		
		return new Float2(x,y);
	}

	public static class Default extends LinearSpline {
		protected Vector<Float2> pnts = new Vector<Float2>( );

		public Default( Float2 ... p){ 
			for( Float2 _p : p)
				pnts.add(_p);
		}
		
		@Override public int size() { return pnts.size(); }

		@Override public Float2 getControlPoint(int i) { return pnts.get(i); }
		
	}
}
