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
package usf.saav.common.data.cache;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class FloatPage extends BasePage {

	public FloatPage( FileChannel file_chan, int page_size  ) throws IOException {
		super( file_chan, page_size  );
	}
	
	public float getValue(int item) {
		AccessEvent( false );
		if( item >= 0 && item < data.length )
			return data[item];
		return Float.NaN; 
	}
	
	public void setValue( int item, float val ){
		AccessEvent( true );
		if( item >= 0 && item < data.length )
			data[item] = val;
	}

	public int size( ){
		return data.length;
	}
	
	protected void Deserialize( ByteBuffer bb ){
		if( data == null ){ 
			data = new float[page_size/4];
			Arrays.fill(data, Float.NaN);
		}
		
		if( bb != null ){
			for(int i = 0; i < data.length; i++){
				data[i] = bb.getFloat();
			}
		}
	}
	
	protected void Serialize( ByteBuffer bb ){
		for(int i = 0; i < data.length; i++){
			bb.putFloat( data[i] );
		}
	}
	
	float [] data;
	
}