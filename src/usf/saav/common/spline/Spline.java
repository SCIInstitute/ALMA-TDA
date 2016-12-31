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

import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.mvc.swing.TGraphics;
import usf.saav.common.range.FloatRange1D;
import usf.saav.common.types.Float2;
import usf.saav.common.types.Pair;

public abstract class Spline extends ViewComponent.Default implements ViewComponent {
	
		//protected Vector<Float2> pnts = new Vector<Float2>( );
		protected float colr=0,colg=0,colb=0,cola=255;
		protected Constraint constraint;
		float xo = 0, yo = 0;
		float xs = 1, ys = 1;

		Spline( ){
			
		}
		/*
		Spline( Float2 ... p){ 
			for( Float2 _p : p)
				pnts.add(_p);
		}
		*/
		
		public abstract int size( );
		public abstract Float2 getControlPoint( int i );
		public abstract Float2 interpolate( float t );			
	

		public void setColor( float grey ){
			this.colr = grey;
			this.colg = grey;
			this.colb = grey;
			this.cola = 255;
		}

		public void setColor( float r, float g, float b ){
			this.colr = r;
			this.colg = g;
			this.colb = b;
			this.cola = 255;
		}
		
		public void setColor( float r, float g, float b, float a ){
			this.colr = r;
			this.colg = g;
			this.colb = b;
			this.cola = a;
		}
		
		public void setConstraint( Constraint c ){
			this.constraint = c;
		}

		@Override
		public void update( ){
			if( !isEnabled() ) return;
			if( this.constraint == null ) return;
			if( size() == 0 ) return;
			this.constraint.constrainFirstPoint( getControlPoint(0) );
			for(int i = 1; i < size()-1; i++ ){
				this.constraint.constrainIntermediatePoint( getControlPoint(i) );
			}
			this.constraint.constrainLastPoint( getControlPoint(size()-1) );
		}
		
		@Override
		public void draw( TGraphics g ){
			if( !isEnabled() ) return;
			
			g.hint( TGraphics.DISABLE_DEPTH_TEST );
			g.strokeWeight(8);
			g.stroke(colr,colg,colb,cola);
			g.noFill();
			
			g.beginShape( TGraphics.LINE_STRIP );
			for( float ti = 0; ti <= 100; ti++){
				float t = (float)ti/(float)100;
				Float2 xy = interpolate( t );
				if( this.constraint != null ) this.constraint.constrainInterpolatedPoint(xy);
				g.vertex( winX.interpolate( (xy.x+xo)*xs ), winY.interpolate( 1-((xy.y+yo)*ys) ) );
			}
			g.endShape();

			g.strokeWeight(1);
			g.stroke(0);
			g.fill(colr,colg,colb,cola);

			for(int i = 0; i < size(); i++ ){
				g.ellipse(winX.interpolate( (getControlPoint(i).x+xo)*xs ),  winY.interpolate( 1 - ((getControlPoint(i).y+yo)*ys) ), 8, 8, 8 );
			}
			g.hint( TGraphics.DISABLE_DEPTH_TEST );
		}
		
		
		public Pair<Float2, Float> selectPoint( int mx, int my ){
			Float2 sel = null;
			float selD = Float.MAX_VALUE;
				
			for(int i = 0; i < size(); i++){
				Float2 p = getControlPoint(i);
				float dx = mx-winX.interpolate( p.x );
				float dy = my-winY.interpolate( 1-p.y );
				float d = (float)Math.sqrt(dx*dx+dy*dy);
				if( d < selD ){
					sel = p;
					selD = d;
				}
			}
			return new Pair<Float2,Float>(sel,selD);
		}
		
		public static abstract class Constraint {
			public void constrainFirstPoint( Float2 p ){ }
			public void constrainLastPoint( Float2 p ){ }
			public void constrainIntermediatePoint( Float2 p ){ }
			public void constrainInterpolatedPoint( Float2 p ){ }
		}

		public FloatRange1D getRangeY() {
			FloatRange1D ret = new FloatRange1D();
			for( float ti = 0; ti <= 100; ti++){
				float t = (float)ti/(float)100;
				Float2 xy = interpolate( t );
				ret.expand(xy.y);
			}
			return ret;
		}
		
		public FloatRange1D getRangeX() {
			FloatRange1D ret = new FloatRange1D();
			for( float ti = 0; ti <= 100; ti++){
				float t = (float)ti/(float)100;
				Float2 xy = interpolate( t );
				ret.expand(xy.x);
			}
			return ret;
		}

		public void setDrawingOffset( float xo, float yo ) {
			this.xo = xo;
			this.yo = yo;
		}

		public void setDrawingScale( float xs, float ys ) {
			this.xs = xs;
			this.ys = ys;
		}
	
}
