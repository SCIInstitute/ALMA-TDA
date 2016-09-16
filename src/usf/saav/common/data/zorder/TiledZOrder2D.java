package usf.saav.common.data.zorder;

import java.util.Arrays;

import usf.saav.common.MathX;

public class TiledZOrder2D {

	long dim_size;

	private long tileX, tileY;
	
	public TiledZOrder2D( long sizeX, long sizeY ){
		dim_size = MathX.min( sizeX, sizeY );

		System.out.println( dim_size );

		dim_size = MathX.nextSmallerPowerOf2( dim_size );
		//dim_size = MathX.max( dim_size, MIN_PARTITION_SIZE );
		//System.out.println(dim_size);
		System.out.println( dim_size );
		tileX = sizeX/dim_size;
		tileY = sizeY/dim_size;
		System.out.println( tileX + " " + tileY );
		
	}

	public long sizeX( ){ 
		return tileX*dim_size;
	}

	public long sizeY( ){ 
		return tileY*dim_size;
	}

	public long size() { 
		return (tileX*dim_size)*(tileY*dim_size);
	}
	
	public boolean inRange( long x, long y ){
		return ( x >= 0 && x < sizeX() && y >= 0 && y < sizeY() );
	}
	
	public long getOrdered2( long x, long y ){
		if( !inRange(x,y) ) return -1;
		
		long tx = x/dim_size;
		long ox = x%dim_size;
		long ty = y/dim_size;
		long oy = y%dim_size;
		
		long toff = (ty*tileX+tx)*(dim_size*dim_size);
		long zo = ZOrder.getOrdered2( ox,oy );
				
		return toff + zo;
	}

	public long [] getPosition2( long elem ){
		if( elem < 0 || elem >= size() ) return null;
		
		long tileIdx = elem/(dim_size*dim_size);
		long tileOff = elem%(dim_size*dim_size);
		
		long tx = tileIdx%tileX;
		long ty = tileIdx/tileX;
		
		long[] p = ZOrder.getPosition2(tileOff);
		p[0] += tx*dim_size;
		p[1] += ty*dim_size;
		return p;			
	}
	
	
	public static void main(String[] args) {
		System.out.println("Testing Z-Order 2D code!");
		TiledZOrder2D tile = new TiledZOrder2D( 5, 9 );
		//recursiveTest3D( 0, 8192*8, 0,5,0 );
		//new ZOrder.ZOrder3D( 10, 18, 19 );
		for(int y = 0; y < 9; y++ ){
			for(int x = 0; x < 5; x++){
				long el = tile.getOrdered2(x, y);
				long [] p = tile.getPosition2(el);
				System.out.println( x + ","+ y + " -- " + el + " -- " + Arrays.toString(p) );
			}
		}
		System.out.println("Done!");
	}
	
	
	
}
