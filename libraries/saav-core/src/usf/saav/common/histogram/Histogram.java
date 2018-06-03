package usf.saav.common.histogram;

import java.util.Arrays;

import usf.saav.common.MathX;

public class Histogram {

	int [] bins;
	float minV, maxV;

	public Histogram( int binN, float _minV, float _maxV ){
		bins = new int[binN];
		minV = _minV;
		maxV = _maxV;
		Arrays.fill(bins,0);
	}

	public void add( float val ){
		float b = MathX.map( val, minV, maxV, 0, bins.length );
		bins[(int) MathX.constrain( b, 0, bins.length-1 ) ]++;
	}

	public void addAll( float [] vals ){
		for( float val : vals ) {
			float b = MathX.map( val, minV, maxV, 0, bins.length );
			bins[(int) MathX.constrain( b, 0, bins.length-1 ) ]++;
		}
	}
	
	public int binMax( ){
		return MathX.max(bins);     
	}

	public int size(){ return bins.length; }
	public int get( int binID ){ return bins[binID]; }

	public double getApproximateMean( ){
		double mean = 0;
		long cnt = 0;
		for( int b : bins ) cnt+= b;
		for( int i = 0; i < bins.length; i++){
			double w = (double)bins[i]/(double)cnt;
			double loc = MathX.lerp( minV, maxV, ((double)i+0.5) / ((double)bins.length) );
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
			double loc = MathX.lerp( minV, maxV, ((double)i+0.5) / ((double)bins.length) );
			var += (loc-mean)*(loc-mean) * w;
		}
		return var;
	}
	
	public double getApproximateStdev( ){
		return Math.sqrt(getApproximateVariance());
	}	
}