package usf.saav.common.spline;

import java.util.Vector;

import usf.saav.common.MathXv1;
import usf.saav.common.types.Float2;

public abstract class LinearSpline extends Spline {

	//public LinearSpline( Float2 ... p){ super(p); }
	public LinearSpline( ){ super(); }
	
	@Override
	public Float2 interpolate(float t) {
		int idx0 = MathXv1.clamp( (int)(t * size() + 0), 0, size()-1 );
		int idx1 = MathXv1.clamp( (int)(t * size() + 1), 0, size()-1 );
		float off = (float) (t * size() - Math.floor( t * size() ));
		return Float2.lerp( getControlPoint(idx0),  getControlPoint(idx1), off );	
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
