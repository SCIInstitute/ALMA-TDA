package usf.saav.common.histogram;

import usf.saav.common.range.FloatRange2D;
import usf.saav.common.types.Float2;

public class Histogram2D {

	public Histogram2D(){
		SetRange( new FloatRange2D() );
		SetBinCount( 1, 1 );
	}

	public Histogram2D( FloatRange2D _bb, int bin_x, int bin_y ){
		SetRange( _bb );
		SetBinCount( bin_x, bin_y );
	}
	
	public void SetRange( float xmin, float xmax, float ymin, float ymax ){
		bb = new FloatRange2D(xmin,xmax,ymin,ymax);
	}
	
	public void SetRange( FloatRange2D _bb ){
		bb = _bb;
	}
	
	public void SetBinCount( int cnt_x, int cnt_y ){
		bins = new int[cnt_x][cnt_y];
		Clear();
	}
	
	public void Clear( ){
		bin_max = 0;
		for(int x = 0; x < bins.length; x++){
			for(int y = 0; y < bins[x].length; y++){
				bins[x][y] = 0;
			}
		}
	}
	
	public void Add( float x, float y ){
		Float2 norm = bb.getNormalized( x, y );
		int bin_x = constrain( (int)(norm.x*(bins.length)), 0, bins.length-1 );
		int bin_y = constrain( (int)(norm.y*(bins[bin_x].length)), 0, bins[bin_x].length-1 );
		bins[bin_x][bin_y]++;
		bin_max = Math.max(bin_max, bins[bin_x][bin_y]);
	}
	
	public void Add( Float2 p ){
		Add(p.x,p.y);
	}
	
	public int GetBinValue( int idx, int idy ){
		return bins[idx][idy];
	}
	
	public int GetBinCountX( ){
		return bins.length;
	}
	
	public int GetBinCountY( ){
		return bins[0].length;
	}
	
	public int GetMaximumBin( ){
		return bin_max;
	}

	
	
	private int constrain( int v, int vmin, int vmax ){
		return Math.max( vmin,  Math.min(vmax,  v ));
	}
	
	FloatRange2D bb;
	int [][] bins;
	int bin_max;
	
}
