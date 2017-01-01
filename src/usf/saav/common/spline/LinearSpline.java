/*
 *     ALMA TDA - Contour tree based simplification and visualization for ALMA
 *     data cubes.
 *     Copyright (C) 2016 PAUL ROSEN
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     You may contact the Paul Rosen at <prosen@usf.edu>.
 */
package usf.saav.common.spline;

import java.util.Vector;

import usf.saav.common.MathXv1;
import usf.saav.common.range.FloatRange1D;
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
		protected FloatRange1D rangeX = new FloatRange1D();
		protected FloatRange1D rangeY = new FloatRange1D();

		public Default( Float2 ... p){ 
			for( Float2 _p : p){
				rangeX.expand(_p.x);
				rangeY.expand(_p.y);
				pnts.add(_p);
			}
		}
		
		@Override public int size() { return pnts.size(); }

		@Override public Float2 getControlPoint(int i) { return pnts.get(i); }
		@Override public FloatRange1D getRangeX() { return rangeX; }
		@Override public FloatRange1D getRangeY() { return rangeY; }
	}
	
}
