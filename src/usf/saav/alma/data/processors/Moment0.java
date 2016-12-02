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

// Let’s assume that the spectrum is given in terms of intensity A(v) (e.g. brightness
// temperature T_B) as a function of radial velocity v with a bin width of Δv. The
// zeroth moment of the spectrum is simply the integrated flux over the spectral line:
// M0=Δv∑A(v)

public class Moment0 extends ScalarField2D.Default {

	int w,h;
	double [] data;
	ScalarField3D src;
	//double dV;

	public Moment0( ScalarField3D src ){
		this.src = src;
		w = src.getWidth();
		h = src.getHeight();
		data = new double[w*h];
		//dV = src.getCoordinate(0, 0, 1)[2] - src.getCoordinate(0, 0, 0)[2];

		Arrays.fill(data, Double.NaN);
	}

	@Override public int getWidth() { return w; }
	@Override public int getHeight() { return h; }
	@Override public float getValue(int x, int y) {
		if( Double.isNaN(data[y*w+x]) ){
			data[y*w+x] = 0;
			for( int z = 0; z < src.getDepth(); z++){
				data[y*w+x] += Math.abs(src.getValue(x, y, z));
			}
		}
		return (float) data[y*w+x];
	}

	/*
	@Override public double[] getCoordinate(int x, int y) {
		return Arrays.copyOf(src.getCoordinate(x, y, 0),2);
	}
	*/
}
