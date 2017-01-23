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

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ByteOrder;

import usf.saav.common.histogram.Histogram1D;
import usf.saav.common.range.FloatRange1D;
import usf.saav.scalarfield.ScalarField2D;
import usf.saav.scalarfield.ScalarFieldND;

public class HistogramCache2D {

	private ByteBuffer bb;
	private FloatRange1D range;
	private int cacheBins;
	
	public HistogramCache2D( FloatRange1D range, int cacheBins, int buffer_size ) {
		this.range = range;
		this.cacheBins = cacheBins;

		this.bb = ByteBuffer.allocate(buffer_size);
		this.bb.order(ByteOrder.LITTLE_ENDIAN);
		this.bb.rewind();
	}

	private void Serialize(float[] data) {
		// basic... - length should match buffer size?
		//fb.put(data);
		//this.fb.capacity()
		//this.bb.put(data, 0, data.length);
		FloatBuffer fb = this.bb.asFloatBuffer();
		//fb.put(data, 0, data.length);
		fb.put(data);
		//fb.rewind();
		this.bb.rewind();
	}

	private void Deserialize(float[] data) {
		// basic... - length should match buffer size?
		//this.bb.get(data, 0, data.length);
		FloatBuffer fb = this.bb.asFloatBuffer();
		fb.get(data);
		//fb.rewind();
		this.bb.rewind();
	}

	public void setData(ScalarFieldND sf) {
System.out.println("[HistogramCache2D setData]");
	}

	public Histogram1D getHistogram( /*ScalarField2D sf, int ox, int oy, int sx, int sy*/ ) {
		
		Histogram1D histogram = new Histogram1D( range, cacheBins );
//		for(int i = 0; i < sf.getSize(); i++){
//			float v = sf.getValue(i);
//			if( !Float.isNaN(v) )
//				histogram.Add( v );
//		}
		return histogram;
	}
}
