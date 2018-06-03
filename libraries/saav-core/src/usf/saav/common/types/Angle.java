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

public class Angle {

	public static final Angle ANGLE_NEG_270_DEG = new Angle(-(float)Math.PI*3.0f/2.0f);
	public static final Angle ANGLE_NEG_180_DEG = new Angle(-(float)Math.PI);
	public static final Angle ANGLE_NEG_90_DEG  = new Angle(-(float)Math.PI/2.0f);
	public static final Angle ANGLE_POS_0_DEG 	= new Angle(0);
	public static final Angle ANGLE_POS_90_DEG	= new Angle((float)Math.PI/2.0f);
	public static final Angle ANGLE_POS_180_DEG	= new Angle((float)Math.PI);
	public static final Angle ANGLE_POS_270_DEG	= new Angle((float)Math.PI*3.0f/2.0f);

	float ang = 0;
	
	protected Angle( float rad ){
		ang = rad; 
	}
	
	public Angle(){ };
	
	public Angle clone(){
		return new Angle( ang );
	}
	
	public Angle setDegrees( float deg ){
		ang = MathX.radians( deg );
		return this;
	}
	
	public Angle setRadians( float rad ){
		ang = rad;
		return this;
	}
	
	public float getDegrees( ){
		return MathX.degrees( ang );
	}
	
	public float getRadians( ){
		return ang;
	}
	
	public static Angle initDegrees( float deg ){
		return (new Angle()).setDegrees( deg );
	}

	public static Angle initDegrees( double deg ){
		return (new Angle()).setDegrees( (float)deg );
	}

	public static Angle initRadians( float rad ){
		return (new Angle()).setRadians( rad );
	}

	public static Angle initRadians(double rad) {
		return (new Angle()).setRadians( (float) rad );
	}
	
	public static Angle initVector(double x, double y) {
		return (new Angle()).setRadians( (float) Math.atan2( y, x) );
	}
	
	public boolean isClockwise( Angle o ){
		double a0 = getDegrees();
		double a1 = o.getDegrees();
		if( a1 > a0 && a1 > (a0+180) ) return true;
		if( a0 > a1 && (a0-a1) < (a1+360-a0) ) return true;
		return false;
	}
	
	public boolean isCounterclockwise( Angle o ){
		double a0 = getDegrees();
		double a1 = o.getDegrees();
		if( a1 > a0 && a1 < (a0+180) ) return true;
		if( a0 > a1 && (a0-a1) > (a1+360-a0) ) return true;
		return false;
	}

	
	public float cos( ){
		return (float) Math.cos( ang );
	}
	
	public float sin( ){
		return (float) Math.sin( ang );
	}
	
	public static Angle acos( float a ){
		return Angle.initRadians( (float) Math.acos( a ) );
	}

	public static Angle asin( float a ){
		return Angle.initRadians( (float) Math.asin( a ) );
	}
	
	public static boolean between( Angle a, Angle a0, Angle a1 ){
		return MathX.between( a.ang, a0.ang, a1.ang );
	}
	
	public static Angle lerp( Angle a, Angle b, float t ){
		return new Angle( MathX.lerp( a.ang, b.ang, t ) );
	}
	
	public String toString(){
		return "Deg: " + getDegrees() + ", " + "Rad: " + getRadians() ;
	}
	

}
