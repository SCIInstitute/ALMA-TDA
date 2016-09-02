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
package usf.saav.alma.algorithm.mesh;

import java.util.Vector;

import usf.saav.alma.data.ScalarField2D;
import usf.saav.alma.data.ScalarField3D;
import usf.saav.alma.data.ScalarFieldND;
import usf.saav.common.algorithm.Surface1D;

public abstract class Mesh extends Vector<Mesh.Vertex> implements Surface1D {
	private static final long serialVersionUID = 2187532695992737840L;
	
	public Mesh( ){ }
	
	@Override public int getWidth() { return size(); }
	

	public static float [] getComponentMidpoint( Vertex c, ScalarFieldND sf ){
		if( sf instanceof ScalarField2D ){
			return getComponentMidpoint( c, ((ScalarField2D)sf).getWidth(), ((ScalarField2D)sf).getHeight() );
		}
		if( sf instanceof ScalarField3D ){
			return getComponentMidpoint( c, ((ScalarField3D)sf).getWidth(), ((ScalarField3D)sf).getHeight() );
		}
		return null;		
	}

	
	public static float [] getComponentMidpoint( Vertex c, int width, int height ){
		float retX = 0, retY = 0, retZ = 0;
		int cnt = 0;
		int slc = width*height;
		for( int p : c.positions() ){
			retX += (p%slc)%width;
			retY += (p%slc)/width;
			retZ += p/slc;
			cnt++;
		}		
		return new float[]{retX/cnt,retY/cnt,retZ/cnt};		
	}
	

	public interface Vertex {
		float value();
		int [] neighbors();
		int [] positions();
		int id();
	}

	
}
