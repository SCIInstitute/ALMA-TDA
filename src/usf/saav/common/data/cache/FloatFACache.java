package usf.saav.common.data.cache;

import java.io.IOException;

public class FloatFACache extends FullyAssociativeCache<FloatPage> {

	public FloatFACache( String data_file, boolean read_only ) throws IOException {
		super( data_file, FullyAssociativeCache.PAGE_8K, 2048, read_only, false );
	}
	
	public FloatFACache( String data_file, boolean read_only, boolean verbose ) throws IOException {
		super( data_file, FullyAssociativeCache.PAGE_8K, 2048, read_only, verbose );
	}

	public FloatFACache( String data_file, int pg_size, int pg_count, boolean read_only ) throws IOException {
		super( data_file, pg_size, pg_count, read_only, false );
	}	
	
	public FloatFACache( String data_file, int pg_size, int pg_count, boolean read_only, boolean verbose ) throws IOException {
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
	
	@Override
	public int sizeofElement()
	{
		return 4;
	}
}