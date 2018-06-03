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

import usf.saav.common.types.Angle;
import usf.saav.common.types.Float3;

public class SphericalCoordinate {


	protected Angle longitude = Angle.initRadians(0);
	protected Angle latitude = Angle.initRadians(0);
	protected float radius = 0;

	public SphericalCoordinate() { }

	public SphericalCoordinate(Angle latitude, Angle longitude, float radius) {
		this.longitude = longitude.clone();
		this.latitude = latitude.clone();
		this.radius = radius;
	}

	public Float3 toEuclidean() {
		float xf = (float) (radius * latitude.cos() * longitude.cos());
		float yf = (float) (radius * latitude.sin());
		float zf = (float) (radius * latitude.cos() * longitude.sin());
		return new Float3(xf, yf, zf);
	}

	public RobinsonCoordinate toRobinson() {
		return RobinsonCoordinate.fromSpherical(this);
	}

	public SphericalCoordinate setFromEuclidean(Float3 xyz) {
		radius = xyz.Length();
		latitude = Angle.asin( xyz.y / radius );
		longitude = Angle.acos( xyz.x / radius / latitude.cos() );
		return this;
	}

	public static SphericalCoordinate fromEuclidean(Float3 xyz) {
		return (new SphericalCoordinate()).setFromEuclidean(xyz);
	}

	public Angle getLatitude( ) {
		return latitude;
	}

	public Angle getLongitude( ) {
		return longitude;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public void setLongitude(Angle longitude) {
		this.longitude = longitude.clone();
	}

	public void setLatitude(Angle latitude) {
		this.latitude = latitude.clone();
	}

}
