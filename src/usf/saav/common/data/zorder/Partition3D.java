package usf.saav.common.data.zorder;

import usf.saav.common.MathXv1;

public class Partition3D {
	
	private static int MIN_PARTITION_SIZE = 16;
	

	long size;
	Partition3D zx, zy;
	int configDir;
	
	public Partition3D( long sizeX, long sizeY, long sizeZ ){
		size = MathXv1.min( sizeX, sizeY, sizeZ );
		size = MathXv1.nextLargerPowerOf2( size );
		size = Math.max(size, MIN_PARTITION_SIZE);
		
		if( sizeX <= size && sizeY <= size && sizeZ <= size ){
			configDir = -1;
			return;
		}		
		if( sizeX >= sizeY && sizeX >= sizeZ ){
			configDir = 0;
			zx = new Partition3D(       size, sizeY, sizeZ );
			zy = new Partition3D( sizeX-size, sizeY, sizeZ );
			return;
		}
		if( sizeY >= sizeX && sizeY >= sizeZ ){
			configDir = 1;
			zx = new Partition3D( sizeX,       size, sizeZ );
			zy = new Partition3D( sizeX, sizeY-size, sizeZ );
			return;
		}
		if( sizeZ >= sizeX && sizeZ >= sizeY ){
			configDir = 2;
			zx = new Partition3D( sizeX, sizeY,       size );
			zy = new Partition3D( sizeX, sizeY, sizeZ-size );
			return;
		}
		
		System.out.println("Unknown configuration: " + sizeX + " " + sizeY + " " + sizeZ );
		
	}
	
	public long size() { 
		if( configDir == -1 ) return size*size*size;
		return zx.size() + zy.size();
	}


	public long getOrder( long x, long y, long z ){
		if( configDir == -1 )
			return ZOrder.getOrdered3(x, y, z);
		if( configDir == 0 ){
			if( x < size ) return zx.getOrder(x, y, z);
			return zx.size() + zy.getOrder(x-size, y, z);
		}
		if( configDir == 0 ){
			if( y < size ) return zx.getOrder(x, y, z);
			return zx.size() + zy.getOrder(x, y-size, z);
		}
		if( configDir == 0 ){
			if( z < size ) return zx.getOrder(x, y, z);
			return zx.size() + zy.getOrder(x, y, z-size);
		}
		return -1;
	}


}
