package usf.saav.common.algorithm;


/**
 * Two Dimensional Disjoint Set. Core functionality is provided by wrapping 1D version. Thus, 
 * performance is expected along the lines of the 1D version.
 *
 */
public class DisjointSet2D implements Surface2D {

	private DisjointSet1D djs;
	private int width, height;

	public DisjointSet2D( int width, int height ){
		djs = new DisjointSet1D( width*height );
		this.width = width;
		this.height = height;
	}

	public void union(int [] r1, int [] r2) {
		djs.union( r1[1]*width+r1[0], r2[1]*width+r2[0]);
	}

	public void union(int r1x, int r1y, int r2x, int r2y ) {
		djs.union( r1y*width+r1x, r2y*width+r2x );
	}

	public int find(int [] p) {
		return djs.find( p[1]*width+p[0] );
	}

	public int find(int x, int y) {
		return djs.find( y*width+x );
	}

	public void print( ){
		for( int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				System.out.print( find(x,y) + "\t" );
			}
			System.out.println();
		}
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

}
