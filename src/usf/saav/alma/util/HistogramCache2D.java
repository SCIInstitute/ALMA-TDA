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
package usf.saav.alma.util;

import usf.saav.common.histogram.Histogram1D;
import usf.saav.common.range.FloatRange1D;
import usf.saav.scalarfield.ScalarField2D;

public class HistogramCache2D {

	FloatRange1D range;
	int cacheBins;
	
	public HistogramCache2D( FloatRange1D range, int cacheBins ){
		this.range = range;
		this.cacheBins = cacheBins;
	}
	
	public Histogram1D getHistogram( ScalarField2D sf, int ox, int oy, int sx, int sy ){
		
		Histogram1D histogram = new Histogram1D( range, cacheBins );
		for(int i = 0; i < sf.getSize(); i++){
			float v = sf.getValue(i);
			if( !Float.isNaN(v) )
				histogram.Add( v );
		}
		return histogram;
	}
}
