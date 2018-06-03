package usf.saav.common.histogram.deprecated;

import java.util.Arrays;

import usf.saav.common.MathX;
import usf.saav.common.range.FloatRange1D;

@Deprecated
public class Histogram1D {

	public Histogram1D(){
		SetRange( new FloatRange1D() );
		SetBinCount( 1 );
	}

	public Histogram1D( FloatRange1D _bb, int bin_x ){
		SetRange( _bb );
		SetBinCount( bin_x );
	}
	
	public void SetRange( float xmin, float xmax  ){
		bb = new FloatRange1D(xmin,xmax);
	}
	
	public void SetRange( FloatRange1D _bb ){
		bb = _bb;
	}
	
	public void SetBinCount( int cnt_x ){
		bins = new int[cnt_x];
		Clear();
	}
	
	public void Clear( ){
		Arrays.fill(bins, 0);
	}
	
	public void Add( float x ){
		double norm = bb.getNormalized( x );
		int bin_x = MathX.constrain( (int)(norm*(bins.length)), 0, bins.length-1 );
		bins[bin_x]++;
		bin_max = Math.max(bin_max, bins[bin_x]);
	}
	
	
	public int GetBinValue( int idx ){
		return bins[idx];
	}
	
	public int GetBinCount( ){
		return bins.length;
	}
		
	public int GetMaximumBin( ){
		return bin_max;
	}
	
	public double getApproximateMean( ){
		double mean = 0;
		long cnt = 0;
		for( int b : bins ) cnt+= b;
		for( int i = 0; i < bins.length; i++){
			double w = (double)bins[i]/(double)cnt;
			double loc = MathX.lerp( bb.getMinimum(), bb.getMaximum(), ((double)i+0.5) / ((double)bins.length) );
			mean += loc * w;
		}
		return mean;
	}

	public double getApproximateVariance( ){
		double mean = getApproximateMean( );
		double var = 0;
		long cnt = 0;
		for( int b : bins ) cnt+= b;
		for( int i = 0; i < bins.length; i++){
			double w = (double)bins[i]/(double)cnt;
			double loc = MathX.lerp( bb.getMinimum(), bb.getMaximum(), ((double)i+0.5) / ((double)bins.length) );
			var += (loc-mean)*(loc-mean) * w;
		}
		return var;
	}
	
	public double getApproximateStdev( ){
		return Math.sqrt(getApproximateVariance());
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		float prev = (float) bb.getMinimum();
		for( int i = 0; i < bins.length; i++ ) {
			float cur = (float) MathX.lerp(bb.getMinimum(),bb.getMaximum(), (float)(i+1)/(float)bins.length );
			sb.append( "[" + prev + "," + cur + ") : " + bins[i] + "\n");
			prev = cur;
		}
		return sb.toString();		
	}

	
	FloatRange1D bb;
	int [] bins;
	int bin_max;


}
