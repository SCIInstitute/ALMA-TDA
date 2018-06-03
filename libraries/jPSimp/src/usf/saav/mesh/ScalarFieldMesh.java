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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import usf.saav.scalarfield.ScalarField1D;
import usf.saav.scalarfield.ScalarField2D;
import usf.saav.scalarfield.ScalarField3D;
import usf.saav.scalarfield.ScalarFieldND;


public class ScalarFieldMesh extends Mesh {

	private static final long serialVersionUID = -4945822547977179117L;

	private Map<Integer,Integer> elemID = new HashMap<Integer,Integer>();
	private ScalarFieldND sf;

	public ScalarFieldMesh( ScalarField1D sf ){
		this( (ScalarFieldND)sf );
	}

	public ScalarFieldMesh( ScalarField2D sf ){
		this( (ScalarFieldND)sf );
	}

	public ScalarFieldMesh( ScalarField3D sf ){
		this( (ScalarFieldND)sf );
	}

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
	
	
	
	private int[] getNeighbors( ScalarField3D sf, int nodeID) {
			int width = sf.getWidth();
			int height = sf.getHeight();
			int depth = sf.getDepth();
			
			int slc    = nodeID/(width*height);
			int slcOff = nodeID%(width*height);
			
			int x = slcOff%width;
			int y = slcOff/width;
			int z = slc;
			
			int [] ret = new int[26];
			int cur = 0;
			for( int _z = -1; _z <= 1; _z++ ){
				if( (z+_z) < 0 || (z+_z)>= depth ) continue;				
				for( int _y = -1; _y <= 1; _y++ ){
					if( (y+_y) < 0 || (y+_y)>= height ) continue;				
					for( int _x = -1; _x <= 1; _x++ ){
						if( (x+_x) < 0 || (x+_x)>= width ) continue;
						if( _x == 0 && _y == 0 && _z==0 ) continue;
						ret[cur++] = (z+_z)*width*height+(y+_y)*width+(x+_x);
					}
				}
			}
			if( cur == ret.length ) return ret;
			return Arrays.copyOf( ret, cur );
	}
	
	private int[] getNeighbors( ScalarField2D sf, int nodeID ){
		int width = sf.getWidth();
		int height = sf.getHeight();
		
		int y = nodeID/width;
		int x = nodeID%width;
		
		int [] ret = new int[8];
		int cur = 0;
		for( int _y = -1; _y <= 1; _y++ ){
			if( (y+_y) < 0 || (y+_y)>= height ) continue;				
			for( int _x = -1; _x <= 1; _x++ ){
				if( (x+_x) < 0 || (x+_x)>= width ) continue;
				if( _x == 0 && _y == 0 ) continue;
				ret[cur++] = (y+_y)*width+(x+_x);
			}
		}
		if( cur == ret.length ) return ret;
		return Arrays.copyOf( ret, cur );
	}
	
	private int[] getNeighbors( ScalarField1D sf, int nodeID ){
		int w = sf.getSize();
		if( nodeID > 0 && nodeID < (w-1) ) return new int[]{nodeID-1,nodeID+1};
		if( nodeID == 0 && nodeID < (w-1) ) return new int[]{nodeID+1};
		if( nodeID > 0 && nodeID == (w-1) ) return new int[]{nodeID-1};
		return new int[]{};
	}
	
	private int[] getNeighbors(int nid){
		if( sf instanceof ScalarField1D ) return getNeighbors((ScalarField1D)sf,nid);
		if( sf instanceof ScalarField2D ) return getNeighbors((ScalarField2D)sf,nid);
		if( sf instanceof ScalarField3D ) return getNeighbors((ScalarField3D)sf,nid);
		return new int[]{};
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
			int [] n = getNeighbors(nid);
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
