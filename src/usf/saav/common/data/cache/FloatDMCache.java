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

public class FloatDMCache extends DirectMappedCache<FloatPage> {

	public FloatDMCache( String data_file, boolean read_only ) throws IOException {
		super( data_file, FullyAssociativeCache.PAGE_8K, 2048, read_only, false );
	}
	
	public FloatDMCache( String data_file, boolean read_only, boolean verbose ) throws IOException {
		super( data_file, FullyAssociativeCache.PAGE_8K, 2048, read_only, verbose );
	}

	public FloatDMCache( String data_file, int pg_size, int pg_count, boolean read_only ) throws IOException {
		super( data_file, pg_size, pg_count, read_only, false );
	}	
	
	public FloatDMCache( String data_file, int pg_size, int pg_count, boolean read_only, boolean verbose ) throws IOException {
		super( data_file, pg_size, pg_count, read_only, verbose );
	}
	
	public float get( long element ) throws IOException {
		long pg = element*4/page_size;
		long el = element%(page_size/4);
		return getPage( pg ).getValue( (int)el );
	}

	public void set( long element, float val ) throws IOException {
		if( read_only ) return;

		long pg = element*4/page_size;
		long el = element%(page_size/4);
		getPage( pg ).setValue( (int)el, val );
	}
	
	public void setBlock( long startElement, float [] val ) throws IOException {
		if( read_only ) return;

		for( int i = 0; i < val.length; i++ ){
			
			long  element = startElement+i;
			float v = val[i];
			
			long pg = element*4/page_size;
			long el = element%(page_size/4);
			getPage( pg ).setValue( (int)el, v );
		}
	} 
	
	public void setScattered( long startElement, int strideElement, float [] val ) throws IOException{
		if( read_only ) return;
		
		for( int i = 0; i < val.length; i++ ){
			
			long element = startElement + strideElement*i;
			long pg = element*4/page_size;
			long el = element%(page_size/4);
			
			getPage( pg ).setValue( (int)el, val[i] );
		}
	}

	@Override
	protected BasePage createPage() throws IOException {
		return new FloatPage(file_chan, page_size );
	}
	
}

