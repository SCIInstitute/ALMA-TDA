package usf.saav.common.algorithm;

import java.util.Arrays;

public class BinaryMask2D implements Surface2D {
	
	private boolean [] mask;
	
	private int height;
	private int width;
	
	public BinaryMask2D( int width, int height, boolean default_val){
		this.width = width;
		this.height = height;
		this.mask = new boolean[width*height];
		Arrays.fill(mask, default_val);
	}
	
	public void set( int x, int y ){ mask[ width*y + x ] = true; }
	
	public void set( int [] p ){ mask[ width*p[1] + p[0] ] = true; }
	
	
	public void clear( int x, int y ){ mask[ width*y + x ] = false;	}
	
	public void clear( int [] p ){ mask[ width*p[1] + p[0] ] = false; }
	

	public boolean isSet( int x, int y ){ return mask[ width*y + x ]; }

	public boolean isSet( int [] p ){ return mask[ width*p[1] + p[0] ]; }

	
	@Override public int getWidth(){ return width; }
	@Override public int getHeight(){ return height; }
	
	@Override 
	public String toString( ){
		String ret = "";
		for( int i = 0; i < mask.length; i++){
			ret += ( mask[i] ? "1 " : "0 " );
			if( (i%width)==(width-1) ) ret += "\n";
		}
		return ret;
	}
	

}
