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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import usf.saav.alma.data.ScalarFieldND;

public class ScalarFieldMesh extends Mesh {
	
	private static final long serialVersionUID = -4945822547977179117L;
	
	private Map<Integer,Integer> elemID = new HashMap<Integer,Integer>();
	private ScalarFieldND sf;
	
	public ScalarFieldMesh( ScalarFieldND sf ){
		this.sf = sf;
		for(int i = 0; i < sf.getSize(); i++ ){
			boolean isValid = !Float.isNaN( sf.getValue(i) );
			if( !isValid ) 
				elemID.put( i, -1 );
			else {
				elemID.put( i, getWidth() );
				add( new ScalarFieldVertex(i) );
			}
		}
	}


	class ScalarFieldVertex implements Vertex {
		private int nid;
		
		ScalarFieldVertex( int nid ){
			this.nid = nid;
		}
		
		@Override
		public float value() {
			return sf.getValue(nid);
		}

		@Override
		public int[] neighbors() {
			int [] n = sf.getNeighbors(nid);
			int j = 0;
			for(int i = 0; i < n.length; i++){
				if( elemID.get( n[i] ) != -1 ){
					n[j] = elemID.get( n[i] );
					j++;
				}
			}
			n = Arrays.copyOfRange(n, 0, j);
			return n;
		}

		@Override
		public int[] positions() {
			return new int[]{nid};
		}

		@Override
		public int id() {
			return nid;
		}
		
	}
	
}
