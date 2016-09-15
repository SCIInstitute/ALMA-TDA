package usf.saav.common.data.zorder;

import usf.saav.common.MathX;

public class Partition2D {
	
	private static int MIN_PARTITION_SIZE = 64;
	
	long dim_size;
	Partition2D z0, z1, z2;
	
	public Partition2D( long sizeX, long sizeY ){
		dim_size = MathX.min( sizeX, sizeY );
		//System.out.println( sizeX + ", " + sizeY );
		//System.out.println( MathX.nextSmallerPowerOf2( dim_size ) );
		//System.out.println( MathX.nextLargerPowerOf2( dim_size ) );
		
		//dim_size = MathX.nextSmallerPowerOf2( dim_size );
		dim_size = MathX.nextLargerPowerOf2( dim_size );
		dim_size = MathX.max( dim_size, MIN_PARTITION_SIZE );
		//System.out.println(dim_size);
		
		if( sizeX > dim_size ){
			z0 = new Partition2D( sizeX-dim_size, dim_size );
		}
		if( sizeY > dim_size ){
			z1 = new Partition2D( dim_size, sizeY-dim_size );
		}
		if( sizeX > dim_size && sizeY > dim_size ){
			z2 = new Partition2D( sizeX-dim_size, sizeY-dim_size );
		}
		
		
	}

	public long size() { 
		long tsize = dim_size*dim_size;
		if( z0 != null ) tsize += z0.size();
		if( z1 != null ) tsize += z1.size();
		if( z2 != null ) tsize += z2.size();
		return tsize;
	}
	
	public long getOrdered2( long x, long y ){
		long off = 0;
		if( x < dim_size && y < dim_size ) return off + ZOrder.getOrdered2( x,y );
		off+=dim_size*dim_size;
		if( z0 != null ){
			if( x>=dim_size && y<dim_size ) return off + z0.getOrdered2( x-dim_size, y );
			off += z0.size();
		}
		if( z1 != null ){
			if( x<dim_size && y>=dim_size ) return off + z1.getOrdered2( x,  y-dim_size );
			off += z1.size();
		}
		if( z2 != null ) return off + z2.getOrdered2( x-dim_size, y-dim_size );
		return -1;
	}

	public long [] getPosition2( long elem ){
		if( elem < dim_size*dim_size ) return ZOrder.getPosition2(elem);
		elem -= dim_size*dim_size;
		if( z0 != null && elem < z0.size() ) return z0.getPosition2( elem );
		elem -= z0.size();
		if( z1 != null && elem < z1.size() ) return z1.getPosition2( elem );
		elem -= z1.size();
		if( z2 != null && elem < z2.size() ) return z2.getPosition2( elem );
		//if( zy != null ) return zy.getPosition2( elem - size*size );
		return null;			
	}

}
