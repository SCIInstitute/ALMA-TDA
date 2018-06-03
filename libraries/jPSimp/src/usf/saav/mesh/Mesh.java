/*
 *     jPSimp - Persistence calculation and simplification of scalar fields.
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
package usf.saav.mesh;

import java.util.Vector;

public abstract class Mesh extends Vector<Mesh.Vertex> {
	private static final long serialVersionUID = 2187532695992737840L;

	public Mesh( ){ }

	public int getWidth() { return size(); }

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
