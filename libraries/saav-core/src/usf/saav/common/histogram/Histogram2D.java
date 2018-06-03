package usf.saav.common.histogram;

import java.util.Arrays;

import usf.saav.common.MathX;

public class Histogram2D {

	int [][] bins;
	float minX, maxX;
	float minY, maxY;

	public Histogram2D( int binX, int binY, float _minX, float _maxX, float _minY, float _maxY ){
		bins = new int[binX][binY];
		minX = _minX;
		maxX = _maxX;
		minY = _minY;
		maxY = _maxY;
		Arrays.fill(bins,0);
	}

	public void add( float valX, float valY ){
		float bX = MathX.map( valX, minX, maxX, 0, bins.length );
		float bY = MathX.map( valY, minY, maxY, 0, bins.length );
		bins[(int) MathX.constrain( bX, 0, bins.length-1 ) ][(int) MathX.constrain( bY, 0, bins[0].length-1 ) ]++;
	}

	
	public int binMax( ){
		int mB = 0;
		for( int [] b : bins )
			mB = Math.max( mB, MathX.max(b) );
		return mB;
	}

	public int size(){ return bins.length; }
	public int get( int binX, int binY ){ return bins[binX][binY]; }

	
}
