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
package usf.saav.common.algorithm.deprecated;

import java.util.Arrays;

import usf.saav.common.data.Field2D;

@Deprecated
public class BinaryMask2D implements Field2D {
	
	private boolean [] mask;
	
	private int height;
	private int width;
	
	public BinaryMask2D( int width, int height, boolean default_val){
		this.width = width;
		this.height = height;
		this.mask = new boolean[width*height];
		Arrays.fill(mask, default_val);
	}
	
	public void set( int x, int y ){ mask[ width*y + x ] = true; }
	
	public void set( int [] p ){ mask[ width*p[1] + p[0] ] = true; }
	
	
	public void clear( int x, int y ){ mask[ width*y + x ] = false;	}
	
	public void clear( int [] p ){ mask[ width*p[1] + p[0] ] = false; }
	

	public boolean isSet( int x, int y ){ return mask[ width*y + x ]; }

	public boolean isSet( int [] p ){ return mask[ width*p[1] + p[0] ]; }

	
	@Override public int getWidth(){ return width; }
	@Override public int getHeight(){ return height; }
	
	@Override 
	public String toString( ){
		String ret = "";
		for( int i = 0; i < mask.length; i++){
			ret += ( mask[i] ? "1 " : "0 " );
			if( (i%width)==(width-1) ) ret += "\n";
		}
		return ret;
	}
	

}
