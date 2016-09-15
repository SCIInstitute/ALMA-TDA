package usf.saav.common;

import java.math.BigInteger;

import usf.saav.common.types.Float3;
import usf.saav.common.types.FloatN;


public class MathXv1 {

	public static class Interpolate {
		public static float UniformCatmullRom( float P0, float P1, float P2, float P3, float t ){
			
			float t0 = -1, t1 = 0, t2 = 1, t3 = 2;
			float A1 = (t1-t)/(t1-t0)*P0 + (t-t0)/(t1-t0)*P1;
			float A2 = (t2-t)/(t2-t1)*P1 + (t-t1)/(t2-t1)*P2;
			float A3 = (t3-t)/(t3-t2)*P2 + (t-t2)/(t3-t2)*P3;

			float B1 = (t2-t)/(t2-t0)*A1 + (t-t0)/(t2-t0)*A2;
			float B2 = (t3-t)/(t3-t1)*A2 + (t-t1)/(t3-t1)*A3;

			float C  = (t2-t)/(t2-t1)*B1 + (t-t1)/(t2-t1)*B2;
			
			return C;
		}
		
		public static float CatmullRom( float a, float b, float c, float d, float t ){
			float q0 = t * ( (2.0f-t)*t - 1.0f);
			float q1 = t * t * (3.0f*t-5.0f) + 2.0f;
			float q2 = t * ( (4.0f-3.0f*t)*t + 1.0f);
			float q3 = t * t * (t-1.0f);
			return (a*q0+b*q1+c*q2+d*q3)*0.5f;
		}
		

		public static float Linear( float a, float b, float t ){
			return a*(1-t)+b*t;
		}

		public static double Linear( double a, double b, double t ) {
			if( Double.isNaN(a) ) return b;
			if( Double.isNaN(b) ) return a;
			return a*(1-t)+b*t;
		}


	}
	
	
	public static class Gaussian {
		

	    public static String clProgramSource =
				"float " +  
				"gaussian( float mu, " + "\n" +  
				"			float a_const, " + "\n" +
				"			float b_const, " + "\n" + 
				"           float x ){ " + "\n" +
				"    float a = a_const; " + "\n" +
				"    float b = b_const * ( x - mu ) * ( x - mu ); " + "\n" +
				"    return (float) (a * exp( b )); " + "\n" +
				"}" + "\n";

	    double stdev;
		double mean;
		double a_const, b_const;

		public Gaussian( double mean, double stdev ){
			this.stdev = stdev;
			this.mean = mean;

			a_const = (1.0 / ( stdev * Math.sqrt(2*Math.PI) ));
			b_const = (-0.5 / ( stdev * stdev ));
		}
		
		public double getStdev( ){ return stdev; }
		public double getMean( ){ return mean; }

		public double get( double x ) {
			double a = a_const;
			double b = b_const * ( x - mean ) * ( x - mean );
			return (a * Math.exp( b ));
		}
		
		public double getInfluence( double min_val ){
			return (Math.sqrt( Math.log( min_val / a_const ) / b_const ) + mean );
		}

	}



	public static float lerp( float a, float b, float t ){
		return a*(1-t)+b*t;
	}

	public static double lerp( double a, double b, double t ) {
		if( Double.isNaN(a) ) return b;
		if( Double.isNaN(b) ) return a;
		return a*(1-t)+b*t;
	}

	public static float [] lerp( float [] a, float [] b, float t ){
		float [] ret = new float[Math.max(a.length, b.length)];
		int i = 0;
		while( i < a.length && i < b.length ){
			ret[i] = a[i]*(1-t)+b[i]*t;
			i++;
		}
		while( i < a.length ){
			ret[i] = a[i];
			i++;
		}
		while( i < b.length ){
			ret[i] = b[i];
			i++;
		}
		return ret;
	}
	
	public static long nextSmallerPowerOf2( long v ){
		return nextLargerPowerOf2(v/2+1);
	}

	public static int nextSmallerPowerOf2( int v ){
		return nextLargerPowerOf2(v/2+1);
	}

	public static long nextLargerPowerOf2( long v ){
		v--;
		v |= v >> 1;
		v |= v >> 2;
		v |= v >> 4;
		v |= v >> 8;
		v |= v >> 16;
		v |= v >> 32;
		return v+1;
	}

	public static int nextLargerPowerOf2( int v ){
		v--;
		v |= v >> 1;
		v |= v >> 2;
		v |= v >> 4;
		v |= v >> 8;
		v |= v >> 16;
		return v+1;
	}
	
	public static Float [] lerp( Float [] a, Float [] b, float t ){
		Float [] ret = new Float[Math.max(a.length, b.length)];
		int i = 0;
		while( i < a.length && i < b.length ){
			ret[i] = a[i]*(1-t)+b[i]*t;
			i++;
		}
		while( i < a.length ){
			ret[i] = a[i];
			i++;
		}
		while( i < b.length ){
			ret[i] = b[i];
			i++;
		}
		return ret;
	}
	
	public static float [] pow( float [] a, float pow ){
		float [] ret = new float[a.length];
		for( int i = 0; i < ret.length; i++ ){
			ret[i] = (float) Math.pow( a[i], pow );
		}
		return ret;
	}

	public static float [] square( float [] a ){
		float [] ret = new float[a.length];
		for( int i = 0; i < ret.length; i++ ){
			ret[i] = a[i]*a[i];
		}
		return ret;
	}

	public static float blerp(float a, float b, float c, float d, float t0, float t1){
		return lerp(lerp(a,b,t0),lerp(c,d,t0),t1);
	}

	public static double blerp(double a, double b, double c, double d, double t0, double t1){
		return lerp(lerp(a,b,t0),lerp(c,d,t0),t1);
	}

	public static float trilerp(float a, float b, float c, float d, float e, float f, float g, float h, float t0, float t1, float t2){
		return lerp( blerp(a,b,c,d,t0,t1), blerp(e,f,g,h,t0,t1), t2 );
	}

	public static double trilerp(double a, double b, double c, double d, double e, double f, double g, double h, double t0, double t1, double t2){
		return lerp( blerp(a,b,c,d,t0,t1), blerp(e,f,g,h,t0,t1), t2 );
	}


	public static float clamp( float v, float minv, float maxv ){
		return Math.max( minv,  Math.min(v,maxv) );
	}
	
	public static float constrain( float v, float minv, float maxv ){
		return Math.max( minv,  Math.min(v,maxv) );
	}

	public static int clamp( int v, int minv, int maxv ){
		return Math.max( minv,  Math.min(v,maxv) );
	}
	
	public static int constrain( int v, int vmin, int vmax ){
		return Math.max( vmin,  Math.min(vmax,  v ));
	}


	public static float normalize( float v, float minv, float maxv ){
		return (v-minv)/(maxv-minv);
	}

	public static float normalize( int v, int minv, int maxv ){
		return (float)(v-minv)/(float)(maxv-minv);
	}

	public static float normalize( long v, long minv, long maxv ){
		return (float)(v-minv)/(float)(maxv-minv);
	}

	public static float min( float v0, float v1 ){ return (v0<v1)?v0:v1;  }
	public static float max( float v0, float v1 ){ return (v0>v1)?v0:v1;  }
	public static int   min(   int v0,   int v1 ){ return (v0<v1)?v0:v1; }
	public static int   max(   int v0,   int v1 ){ return (v0>v1)?v0:v1; }
	public static long  min(  long v0,  long v1 ){ return (v0<v1)?v0:v1; }
	public static long  max(  long v0,  long v1 ){ return (v0>v1)?v0:v1; }

	public static float min( float v0, float v1, float v2 ){ return min( v0, min(v1,v2) ); }
	public static float max( float v0, float v1, float v2 ){ return max( v0, max(v1,v2) ); }
	public static float mid( float v0, float v1, float v2 ){ return ( (v0 <= v1 && v0 >= v2) || (v0 >= v1 && v0 <= v2) ) ? v0 : mid(v1,v2,v0); }

	public static int   min(   int v0,   int v1,   int v2 ){ return min( v0, min(v1,v2) ); }
	public static int   max(   int v0,   int v1,   int v2 ){ return max( v0, max(v1,v2) ); }
	public static int   mid(   int v0,   int v1,   int v2 ){ return ( (v0 <= v1 && v0 >= v2) || (v0 >= v1 && v0 <= v2) ) ? v0 : mid(v1,v2,v0); }

	public static long  min(  long v0,  long v1,  long v2 ){ return min( v0, min(v1,v2) ); }
	public static long  max(  long v0,  long v1,  long v2 ){ return max( v0, max(v1,v2) ); }
	public static long  mid(  long v0,  long v1,  long v2 ){ return ( (v0 <= v1 && v0 >= v2) || (v0 >= v1 && v0 <= v2) ) ? v0 : mid(v1,v2,v0); }
	

	public static boolean between( int v, int r0, int r1 ) {
		return (v>=min(r0, r1)) && (v<=max(r0, r1));
	}

	public static boolean between( float v, float r0, float r1 ) {
		return (v>=min(r0, r1)) && (v<=max(r0, r1));
	}

	public static boolean ConcordantPair( float x1, float y1, float x2, float y2 ){
		return (x1!=x2) && ( sign( x2 - x1 ) == sign( y2 - y1 ) );
	}

	public static boolean DiscordantPair( float x1, float y1, float x2, float y2 ){
		return (x1!=x2) && ( sign( x2 - x1 ) == -sign( y2 - y1 ) );
	}

	public static float sign(float f) {
		return (f < 0 ) ? -1.0f : ( (f > 0) ? 1.0f : 0.0f );
	}

	public static double MultipleCorrelation( FloatN dependent, FloatN independent_0 ){
		return Math.abs( FloatN.PearsonCorrelation(dependent, independent_0) );
	}

	public static double MultipleCorrelation( double corr_dep_ind0 ){
		return Math.abs( corr_dep_ind0 );
	}




	public static BigInteger factorial( long n ){
		BigInteger ret = BigInteger.ONE;
		for(long i = 2; i <= n; i++){
			ret = ret.multiply( BigInteger.valueOf(i) );
		}
		return ret;
	}


	public static float mean( float ... data ) {
		float val = 0;
		for(int d = 0; d < data.length; d++){
			val += data[d];
		}
		return val / (float)data.length;
	}

	public static float stdev( float ... data ) {
		float mean = mean( data );
		float sum_dif = 0;
		for(int d = 0; d < data.length; d++){
			sum_dif += (float)Math.pow( (float)( data[d] - mean), 2.0f );
		}
		return (float)Math.sqrt((float)sum_dif/(float)data.length);
	}


	public static double mean( double ... data ) {
		double val = 0;
		for(int d = 0; d < data.length; d++){
			val += data[d];
		}
		return val / (double)data.length;
	}

	public static double stdev( double ... data ) {
		double mean = mean( data );
		double sum_dif = 0;
		for(int d = 0; d < data.length; d++){
			sum_dif += (double)Math.pow( (double)( data[d] - mean), 2.0 );
		}
		return (double)Math.sqrt((double)sum_dif/(double)data.length);
	}

	public static double radians( double deg ){
		return deg * Math.PI / 180.0;
	}

	public static double degrees( double rad ){
		return rad * 180.0 / Math.PI;
	}

	public static float radians( float deg ){
		return (float) (deg * Math.PI / 180.0);
	}

	public static float degrees( float rad ){
		return (float) (rad * 180.0 / Math.PI);
	}



	/*
	private float Clamp(float val, float vmin, float vmax) {
		return Math.max( vmin,  Math.min(val,vmax));
	}
	 */
	
	
	public static Float3 abs(Float3 v){ return new Float3(Math.abs(v.x), Math.abs(v.y), Math.abs(v.z)); }
	public static Float3 tan(Float3 v){ return new Float3(Math.tan(v.x), Math.tan(v.y), Math.tan(v.z));  }
	public static Float3 cos(Float3 v){ return new Float3(Math.cos(v.x), Math.cos(v.y), Math.cos(v.z));  }
	public static Float3 sin(Float3 v){ return new Float3(Math.tan(v.x), Math.tan(v.y), Math.tan(v.z));  }

	public static Float3 absf(Float3 v){ return new Float3(Math.abs(v.x), Math.abs(v.y), Math.abs(v.z)); }
	public static Float3 tanf(Float3 v){ return new Float3(Math.tan(v.x), Math.tan(v.y), Math.tan(v.z));  }
	public static Float3 cosf(Float3 v){ return new Float3(Math.cos(v.x), Math.cos(v.y), Math.cos(v.z));  }
	public static Float3 sinf(Float3 v){ return new Float3(Math.tan(v.x), Math.tan(v.y), Math.tan(v.z));  }

	public static Float3 fabs(Float3 v){  return new Float3(Math.abs(v.x),Math.abs(v.y),Math.abs(v.z)); }
	public static Float3 fabsf(Float3 v){ return new Float3(Math.abs(v.x),Math.abs(v.y),Math.abs(v.z)); }

	public static boolean inRange(float x, float minimum, float maximum) {
		return x>=minimum && x <= maximum;
	}


}
