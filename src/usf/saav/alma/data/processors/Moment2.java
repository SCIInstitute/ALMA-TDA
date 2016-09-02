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
package usf.saav.alma.data.processors;

import java.util.Arrays;

import usf.saav.alma.data.ScalarField2D;
import usf.saav.alma.data.ScalarField3D;

public class Moment2 extends ScalarField2D.Default {

	int w,h;
	double [] data;
	ScalarField3D src;
	Moment1 m1;
	
	public Moment2( ScalarField3D src ){
		init( src, new Moment1(src) );
	}

	public Moment2( ScalarField3D src, Moment1 mom1 ){
		init( src, mom1 );
	}

	private void init( ScalarField3D src, Moment1 mom1 ){

		this.src = src;
		this.m1  = mom1;
		w = src.getWidth();
		h = src.getHeight();
		data = new double[w*h];
		
		Arrays.fill(data, Double.NaN);
		/*
		double[] denom = new double[w*h];
		
		for( int z = 0; z < src.getDepth(); z++){
			for( int y = 0; y < src.getHeight(); y++ ){
				for(int x = 0; x < src.getWidth(); x++ ){
					double m1 = mom1.getValue(x, y);
					double av = Math.abs(src.getValue(x, y, z));
					double v  = z;//src.getCoordinate(x, y, z)[2];
					
					data[y*w+x]  += (v-m1)*(v-m1)*av;
					denom[y*w+x] += av;
				}
			}
		}
		
		for(int i = 0; i < data.length; i++){
			data[i] = Math.sqrt( data[i]/denom[i] );
		}
		*/
	}

	@Override public int getWidth() { return w; }
	@Override public int getHeight() { return h; }
	@Override public float getValue(int x, int y) {
		if( Double.isNaN(data[y*w+x]) ){
			double denom = 0;
			data[y*w+x] = 0; 
			
			for( int z = 0; z < src.getDepth(); z++){
						double m = m1.getValue(x, y);
						double av = Math.abs(src.getValue(x, y, z));
						data[y*w+x]  += (z-m)*(z-m)*av;
						denom += av;
			}
			data[y*w+x] = Math.sqrt( data[y*w+x]/denom );	
		}
		return (float) data[y*w+x]; 
		
	}
	
	@Override public double[] getCoordinate(int x, int y) { 
		return Arrays.copyOf(src.getCoordinate(x, y, 0),2); 
	}

	
}