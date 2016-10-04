/*
 *     ALMA TDA - Contour tree based simplification and visualization for ALMA 
 *     data cubes.
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
package usf.saav.alma.util;

// TODO: Auto-generated Javadoc
/**
 * The Interface CoordinateSystem.
 */
public interface CoordinateSystem {

	/**
	 * Gets the coordinate system position.
	 *
	 * @param wx the wx
	 * @param wy the wy
	 * @return the coordinate system position
	 */
	public float [] getCoordinateSystemPosition( float wx, float wy );
	
	/**
	 * Gets the window position.
	 *
	 * @param csx the csx
	 * @param csy the csy
	 * @return the window position
	 */
	public float [] getWindowPosition( float csx, float csy );

}
