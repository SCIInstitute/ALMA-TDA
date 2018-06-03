/*
 *     saav-core - A (very boring) software development support library.
 *     Copyright (C) 2016 PAUL ROSEN
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     You may contact the Paul Rosen at <prosen@usf.edu>.
 */
package usf.saav.common.types;

import usf.saav.common.MathX;

public class FloatN {

    public FloatN( ) { data = new float[0]; }
    public FloatN( int siz ) { data = new float[siz]; }
    public FloatN( int siz, float default_val ) { 
    	data = new float[siz];
    	for(int i = 0; i < siz; i++){ data[i] = default_val; }
    }
    public FloatN( FloatN v ) { 
    	data = new float[v.data.length]; 
    	for(int i = 0; i < data.length; i++){ data[i] = v.data[i]; }
    }
    
    public FloatN( float [] data ){
    	this.data = data;
    }

    public int Size(){
    	return data.length;
    }
    
    public float Get(int idx){
    	if( idx < 0 || idx >= data.length ) return Float.NaN;
    	return data[idx];
    }

    public void Set(int idx, float val){
    	if( idx < 0 || idx >= data.length ) return;
    	data[idx] = val;
    }
    
    public double Min( ) {
        double val = Float.MAX_VALUE;
        for(int d = 0; d < data.length; d++){
            val = (val < data[d]) ? val : data[d];
        }
        return val;
    }
    
    public double Max( ) {
        double val = -Float.MAX_VALUE;
        for(int d = 0; d < data.length; d++){
            val = (val > data[d]) ? val : data[d];
        }
        return val;
    }
    
    public double Mean( ) {
    	return MathX.mean( data );
    }
    
    public double Stdev( ) {
    	return MathX.stdev( data );
    }
    
    public FloatN Add( FloatN right ) {
        FloatN ret = new FloatN( (int)Math.max( data.length, right.data.length ) );
        for(int i = 0; i < ret.data.length; i++){
        	if( i < data.length && i < right.data.length ){ ret.data[i] = data[i]+right.data[i]; }
        	else if ( i < data.length ){ ret.data[i] = data[i]; }
        	else if( i < right.data.length ){ ret.data[i] = right.data[i]; }
        }
        return ret;
    }
    
    public FloatN Subtract ( FloatN right ) {
        FloatN ret = new FloatN( (int)Math.max( data.length, right.data.length ) );
        for(int i = 0; i < ret.data.length; i++){
        	if( i < data.length && i < right.data.length ){ ret.data[i] = data[i]-right.data[i]; }
        	else if ( i < data.length ){ ret.data[i] = data[i]; }
        	else if( i < right.data.length ){ ret.data[i] = -right.data[i]; }
        }
        return ret;
    }

    public FloatN Divide ( float right ) {
        FloatN ret = new FloatN( data.length );
        for(int i = 0; i < data.length; i++){
            ret.data[i] = data[i] / right;
        }
        return ret;
    }    
    
	public FloatN fabsf(  FloatN vn ){
        FloatN ret = new FloatN( data.length );
        for(int i = 0; i < data.length; i++){
            ret.data[i] = Math.abs(data[i]);
        }
        return ret;
	}
    
    
	
	public static float L2Norm( FloatN v0,  FloatN v1){
	    float sum = 0;
	    for(int i = 0; i < (int)v0.data.length && i < (int)v1.data.length; i++){
	        sum += (v0.data[i]-v1.data[i])*(v0.data[i]-v1.data[i]);
	    }
	    return (float)Math.sqrt( sum );
	}
	
	
	public static double PearsonCorrelation( FloatN v0,  FloatN v1 ){
	    double mean_x = v0.Mean();
	    double mean_y = v1.Mean();
	
	    double num  = 0.0;
	    double denx = 0.0;
	    double deny = 0.0;
	
	    for(int i = 0; i < (int)v0.data.length && i < (int)v1.data.length; i++ ){
	        float x = ( i < (int)v0.data.length ) ? v0.data[i] : 0;
	        float y = ( i < (int)v1.data.length ) ? v1.data[i] : 0;
	        num  += (x-mean_x)*(y-mean_y);
	        denx += (x-mean_x)*(x-mean_x);
	        deny += (y-mean_y)*(y-mean_y);
	    }
	
	    if( Math.abs(num) < 1.0e-100 || denx < 1.0e-100 || deny < 1.0e-100 ) return 0;
	
	    return num / ( Math.sqrt(denx) * Math.sqrt(deny) );
	}
	
	
	
	public static FloatN lerp(  FloatN v0,  FloatN v1, float t ){
        FloatN ret = new FloatN( (int)Math.min( v0.data.length, v1.data.length ) );
	    for(int i = 0; i < (int)v0.data.length && i < (int)v1.data.length; i++){
	        ret.data[i] = ( (v0.data[i])*(1.0f-t)+(v1.data[i])*(t) );
	    }
	    return ret;
	}    
    
    private float [] data;
    
/*	
    VexN & operator = (  std::vector<float> & right ){
        clear();
        assign( right.begin(), right.end() );
        return (*this);
    }

    void Print( ){
        if(size()==0){
            std::cout << "< >";
        }
        else{
            std::cout << "< ";
            for(int i = 0; i < data.length-1; i++){
                std::cout << data[i] << ", ";
            }
            std::cout << back() << " >";
        }
        std::cout << std::endl;
    }
};
*/	
}
