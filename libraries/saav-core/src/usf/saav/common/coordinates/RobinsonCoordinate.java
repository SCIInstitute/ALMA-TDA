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
package usf.saav.common.coordinates;

import usf.saav.common.MathX;
import usf.saav.common.types.Angle;
import usf.saav.common.types.Float2;

public class RobinsonCoordinate {


    @SuppressWarnings("unused")
	static private float [] ROB_LAT  = { 0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90 };

    static private float [] ROB_PLEN = { 1.0000f, 0.9986f, 0.9954f, 0.9900f, 0.9822f, 0.9730f, 0.9600f, 
                                 	 	 0.9427f, 0.9216f, 0.8962f, 0.8679f, 0.8350f, 0.7986f, 0.7597f, 
                                 	 	 0.7186f, 0.6732f, 0.6213f, 0.5722f, 0.5322f };
    static private float [] ROB_PDFE = { 0.0000f, 0.0620f, 0.1240f, 0.1860f, 0.2480f, 0.3100f, 0.3720f,
                                 		 0.4340f, 0.4958f, 0.5571f, 0.6176f, 0.6769f, 0.7346f, 0.7903f,
                                 		 0.8435f, 0.8936f, 0.9394f, 0.9761f, 1.0000f };
 
    float x = 0, y = 0;

    public RobinsonCoordinate( ){ }

    public RobinsonCoordinate( float x, float y ){
    	this.x = x;
    	this.y = y;
    }

    public float getX(){ return x; }
    public float getY(){ return y; }

    public Float2 toFloat2( ){ return new Float2(x,y); }

    public static RobinsonCoordinate fromSpherical( Angle latitude, Angle longitude ){
    	return ( new RobinsonCoordinate() ).setFromSpherical( latitude, longitude );
    }

    public static RobinsonCoordinate fromSpherical( SphericalCoordinate sc ){
    	return ( new RobinsonCoordinate() ).setFromSpherical( sc );
    }

    public RobinsonCoordinate setFromSpherical( Angle latitude, Angle longitude ){
    	float plen;
    	float pdfe;
    	if( Math.abs(latitude.getDegrees()) >= 90 ){
    		plen = ROB_PLEN[ROB_PLEN.length-1];
    		pdfe = ROB_PDFE[ROB_PDFE.length-1];
    	}
    	else{
    		float t = Math.abs(latitude.getDegrees()) / 5.0f;
    		int t0 = (int)Math.floor(t);
    		int t1 = t0+1;
    		plen = MathX.lerp( ROB_PLEN[t0], ROB_PLEN[t1], t-(float)Math.floor(t) );
    		pdfe = MathX.lerp( ROB_PDFE[t0], ROB_PDFE[t1], t-(float)Math.floor(t) );
    	}

    	x = 0.5f - (longitude.getDegrees() / 360.0f) * plen;    
    	y = 0.5f + pdfe / 2.0f * ((latitude.getDegrees()>=0)?1:-1);

    	return this;
    }

    public RobinsonCoordinate setFromSpherical( SphericalCoordinate sc ){
    	return setFromSpherical( sc.getLatitude( ), sc.getLongitude( ) );
    }



	  
}
