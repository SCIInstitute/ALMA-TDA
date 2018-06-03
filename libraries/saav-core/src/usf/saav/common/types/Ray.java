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


public class Ray {

	Float3 pos;
	Float3 dir;
	
	public Ray( Float3 pos, Float3 dir ){
		this.pos = pos.clone();
		this.dir = dir.clone();
	}

	public Ray( Float3 pos, Double3 dir ){
		this.pos = pos.clone();
		this.dir = dir.toFloat3();
	}

	public Ray( float x, float y, float z, float dx, float dy, float dz ){
		this.pos = new Float3(x,y,z);
		this.dir = new Float3(dx,dy,dz);
	}

	public Float3 getPosition(float t) {
		return pos.add( dir.multiply( t ) );
	}

	public Float3 getDirection() {
		return dir;
	}
	
	public float getMagnitude() {
		return dir.Magnitude();
	}
	
	public Float3 getOrigin(){
		return pos;
	}
	
}

