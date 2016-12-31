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
package usf.saav.common.mvc.swing;

import java.util.Vector;

import usf.saav.common.MathXv1;
import usf.saav.common.types.Float3;
import usf.saav.common.types.Integer3;

public class GeodesicSphere {
	Vector<Float3> vdata = new Vector<Float3>( );
	Vector<Vector<Integer3>> idata = new Vector< Vector<Integer3> > ( );

	public GeodesicSphere( ){

		double X = 0.525731112119133606;
		double Y = 0.000000000000000000;
		double Z = 0.850650808352039932;

		vdata.add( new Float3( -X,  Y,  Z ) );
		vdata.add( new Float3(  X,  Y,  Z ) ); 
		vdata.add( new Float3( -X,  Y, -Z ) ); 
		vdata.add( new Float3(  X,  Y, -Z ) );
		vdata.add( new Float3(  Y,  Z,  X ) );
		vdata.add( new Float3(  Y,  Z, -X ) ); 
		vdata.add( new Float3(  Y, -Z,  X ) ); 
		vdata.add( new Float3(  Y, -Z, -X ) );
		vdata.add( new Float3(  Z,  X,  Y ) ); 
		vdata.add( new Float3( -Z,  X,  Y ) ); 
		vdata.add( new Float3(  Z, -X,  Y ) ); 
		vdata.add( new Float3( -Z, -X,  Y ) );

		Vector<Integer3> idata0 = new Vector<Integer3>( );
		idata0.add( new Integer3( 0, 4, 1 ) );
		idata0.add( new Integer3( 0, 9, 4 ) );
		idata0.add( new Integer3( 9, 5, 4 ) );
		idata0.add( new Integer3( 4, 5, 8 ) );
		idata0.add( new Integer3( 4, 8, 1 ) );
		idata0.add( new Integer3( 8, 10, 1 ) );
		idata0.add( new Integer3( 8, 3, 10 ) );
		idata0.add( new Integer3( 5, 3, 8 ) );
		idata0.add( new Integer3( 5, 2, 3 ) );
		idata0.add( new Integer3( 2, 7, 3 ) );
		idata0.add( new Integer3( 7, 10, 3 ) );
		idata0.add( new Integer3( 7, 6, 10 ) );
		idata0.add( new Integer3( 7, 11, 6 ) );
		idata0.add( new Integer3( 11, 0, 6  ) ); 
		idata0.add( new Integer3( 0, 1, 6 ) );
		idata0.add( new Integer3( 6, 1, 10 ) );
		idata0.add( new Integer3( 9, 0, 11 ) );
		idata0.add( new Integer3( 9, 11, 2 ) );
		idata0.add( new Integer3( 9, 2, 5 ) );
		idata0.add( new Integer3( 7, 2, 11 ) );
		idata.add( idata0 );

	}

	void subdivide( Integer3 idx,  Vector<Integer3> new_idata ) {

		int i1 = idx.x;
		int i2 = idx.y;
		int i3 = idx.z;

		Float3 v1 = vdata.get(idx.x);
		Float3 v2 = vdata.get(idx.y);
		Float3 v3 = vdata.get(idx.z);

		Float3 v12 = Float3.add(v1, v2).UnitVector();
		Float3 v23 = Float3.add(v2, v3).UnitVector();
		Float3 v31 = Float3.add(v3, v1).UnitVector();

		int i12 = vdata.size(); vdata.add(v12);
		int i23 = vdata.size(); vdata.add(v23);
		int i31 = vdata.size(); vdata.add(v31);

		new_idata.add( new Integer3(  i1, i12, i31 ) );
		new_idata.add( new Integer3(  i2, i23, i12 ) );
		new_idata.add( new Integer3(  i3, i31, i23 ) );
		new_idata.add( new Integer3( i12, i23, i31 ) );

	}

	private void subdivide( int tess_level ){
		int cur_level = idata.size()-1;

		while(cur_level < tess_level ){
			Vector<Integer3> prev_idata = idata.get(cur_level);
			Vector<Integer3> new_idata = new Vector<Integer3>( );
			for( int i = 0; i < prev_idata.size(); i++ ){
				subdivide( prev_idata.get(i), new_idata );
			}
			idata.add(new_idata);
			cur_level++;
		}
	}

	public void drawSolidSphere(TGraphics g, int sphereDetail) {
		int tess_level = MathXv1.clamp(sphereDetail-3, 0, 10);
		if( tess_level >= idata.size() ){
			subdivide( tess_level );
		}
		
		Float3 v;
		Vector<Integer3> cur_idata = idata.get(tess_level);
		g.beginShape( TGraphics.TRIANGLES );
		for( Integer3 i : cur_idata ){
			v = vdata.get(i.x); g.vertex(v.x,v.y,v.z);
			v = vdata.get(i.y); g.vertex(v.x,v.y,v.z);
			v = vdata.get(i.z); g.vertex(v.x,v.y,v.z);
		}
		g.endShape();
	}

	public void drawWireSphere(TGraphics g, int sphereDetail) {
		int tess_level = MathXv1.clamp(sphereDetail-3, 0, 10);
		if( tess_level >= idata.size() ){
			subdivide( tess_level );
		}
		
		Float3 v;
		Vector<Integer3> cur_idata = idata.get(tess_level);
		g.beginShape( TGraphics.LINES );
		for( Integer3 i : cur_idata ){
			v = vdata.get(i.x); g.vertex(v.x,v.y,v.z);
			v = vdata.get(i.y); g.vertex(v.x,v.y,v.z);
			v = vdata.get(i.z); g.vertex(v.x,v.y,v.z);
		}
		g.endShape();
	}

}
