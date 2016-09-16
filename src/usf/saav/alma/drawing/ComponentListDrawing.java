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
package usf.saav.alma.drawing;

import usf.saav.alma.algorithm.mesh.Mesh;
import usf.saav.alma.algorithm.mesh.Mesh.Vertex;
import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.mvc.swing.TGraphics;

public class ComponentListDrawing extends ViewComponent.Default implements ViewComponent  {

	Mesh cL;
	int field_width;
	int pass = 0;

	public ComponentListDrawing( Mesh cl, int fieldwidth ){
		this.cL = cl;
		this.field_width = fieldwidth;
	}

	public void draw( TGraphics p ){
		
		if( cL == null )
			return;

		int color = 0;

		if( pass/4 == 0 ){

			for( Vertex c : cL ){
				switch( color%6 ){
				case 0: p.stroke(255,0,0); break;
				case 1: p.stroke(255,255,0); break;
				case 2: p.stroke(0,255,0); break;
				case 3: p.stroke(0,255,255); break;
				case 4: p.stroke(0,0,255); break;
				case 5: p.stroke(255,0,255); break;
				}
				for( int pos : c.positions() ){
					p.point( pos%field_width, (int)(pos/field_width) );
				}

				color++;
			}
		}
		else{

			for( Vertex c : cL ){
				if( c.id() == (pass/4-1) ){
					p.stroke(0,0,0);
				}
				else{
					switch( color%6 ){
					case 0: p.stroke(255,0,0); break;
					case 1: p.stroke(255,255,0); break;
					case 2: p.stroke(0,255,0); break;
					case 3: p.stroke(0,255,255); break;
					case 4: p.stroke(0,0,255); break;
					case 5: p.stroke(255,0,255); break;
					}
					boolean contains = false;
					for( int n : c.neighbors() ){
						contains = contains || (n==(pass/4-1));
					}
					if( !contains ) p.stroke(255,255,255);
				}
				for( int pos : c.positions() ){
					p.point( pos%field_width, (int)(pos/field_width) );
				}

				color++;
			}
		}
		pass++;
	}
}
