package usf.saav.common.data.zorder;

public class ZOrder {

	public ZOrder( ){
		
	}

	

	public static long getOrdered3( long x, long y, long z ){
		return partition3(x) | (partition3(y)<<1) | (partition3(z)<<2); 
	}

	public static long getOrdered3( long [] xyz ){
		return partition3(xyz[0]) | (partition3(xyz[1])<<1) | (partition3(xyz[2])<<2); 
	}

	public static long [] getPosition3( long elem ){
		return new long[]{ unpartition3(elem), unpartition3(elem>>1), unpartition3(elem>>2) };
	}

	
	
	
	public static long getOrdered2( long x, long y ){
		return partition2(x) | (partition2(y)<<1);
	}

	public static long getOrdered2( long [] xy ){
		return partition2(xy[0]) | (partition2(xy[1])<<1);
	}

	public static long [] getPosition2( long elem ){
		return new long[]{ unpartition2(elem), unpartition2(elem>>1) };
	}

	
	private static final long B2[] = {0x5555555555555555L, 0x3333333333333333L, 0x0F0F0F0F0F0F0F0FL, 0x00FF00FF00FF00FFL, 0x0000FFFF0000FFFFL, 0x00000000FFFFFFFFL};
	private static final long S2[] = {1, 2, 4, 8, 16, 32 };

	
	
	private static long partition2( long x ){
		x = (x | (x << S2[4])) & B2[4];
		x = (x | (x << S2[3])) & B2[3];
		x = (x | (x << S2[2])) & B2[2];
		x = (x | (x << S2[1])) & B2[1];
		x = (x | (x << S2[0])) & B2[0];
		return x;
	}
	
	private static long unpartition2( long n ){
        n = n & B2[0];
        n = (n ^ (n >> S2[0])) & B2[1];
        n = (n ^ (n >> S2[1])) & B2[2];
        n = (n ^ (n >> S2[2])) & B2[3];
        n = (n ^ (n >> S2[3])) & B2[4];
        n = (n ^ (n >> S2[4])) & B2[5];
        return n;
	}
	
	
	private static final long B3[] = { 0x9249249249249249L, 0x30c30c30c30c30c3L, 0xf00f00f00f00f00fL, 0x00ff0000ff0000ffL, 0xffff00000000ffffL };
	private static final long S3[] = { 2, 4, 8, 16, 32 };
	

	private static long partition3( long x ){
		x = (x | (x << S3[3])) & B3[3];
		x = (x | (x << S3[2])) & B3[2];
		x = (x | (x << S3[1])) & B3[1];
		x = (x | (x << S3[0])) & B3[0];
        return x;
	}	
	

	
	private static long unpartition3( long n){
        n = n & B3[0];
        n = (n ^ (n >> S3[0])) & B3[1];
        n = (n ^ (n >> S3[1])) & B3[2];
        n = (n ^ (n >> S3[2])) & B3[3];
        n = (n ^ (n >> S3[3])) & B3[4];
        return n;
	}
	
	
	
	/*
	private static Random rand = new Random( ); 
	private static long count = 0;
	


	public static void main(String[] args) {
		System.out.println("Testing Z-Order 3D code!");
		recursiveTest3D( 0, 8192*8, 0,5,0 );
		new ZOrder.ZOrder3D( 10, 18, 19 );
		System.out.println("Done!");
	}
	
	private static void recursiveTest3D( long offset, long size, long x, long y, long z ){
		count++;
		if( count > 100000000 ) return;
		if( count%10000 == 0 ){
			System.out.println( count + " of " + 100000000 );
		}
		if( size == 1 ){
			ZOrder zo = new ZOrder();
			long [] unz = zo.getPosition3( offset );
			long zorder = zo.getOrder3(x,y,z);
			if( zorder != offset || unz[0] != x || unz[1] != y || unz[2] != z ){
				System.out.println( offset + " - " + x + " " + y + " " + z + " | " + zorder + " - " + unz[0] + " " + unz[1]+ " " + unz[2] );
			}
		}
		else {
			for(int i = 0; i < 4; i++){
				switch( rand.nextInt(8) ){
					case 0: recursiveTest3D( offset,                        size/2, x,        y,        z        ); break;
					case 1: recursiveTest3D( offset+1*size/2*size/2*size/2, size/2, x+size/2, y,        z        ); break;
					case 2: recursiveTest3D( offset+2*size/2*size/2*size/2, size/2, x,        y+size/2, z        ); break;
					case 3: recursiveTest3D( offset+3*size/2*size/2*size/2, size/2, x+size/2, y+size/2, z        ); break;
					case 4: recursiveTest3D( offset+4*size/2*size/2*size/2, size/2, x,        y,        z+size/2 ); break;
					case 5: recursiveTest3D( offset+5*size/2*size/2*size/2, size/2, x+size/2, y,        z+size/2 ); break;
					case 6: recursiveTest3D( offset+6*size/2*size/2*size/2, size/2, x,        y+size/2, z+size/2 ); break;
					case 7: recursiveTest3D( offset+7*size/2*size/2*size/2, size/2, x+size/2, y+size/2, z+size/2 ); break;
				}
			}
		}
	}
	*/
	

}
