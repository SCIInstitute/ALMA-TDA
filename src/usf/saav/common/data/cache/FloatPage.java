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