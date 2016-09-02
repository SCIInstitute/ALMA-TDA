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
package usf.saav.alma.data.processors;

import usf.saav.alma.data.ScalarField3D;

public class Subsample3D extends ScalarField3D.Default {
	int sx,sy,sz;
	ScalarField3D field;
	public Subsample3D( ScalarField3D field, int step ){
		this(field,step,step,step);
	}		
	public Subsample3D( ScalarField3D field, int step_x, int step_y, int step_z ){
		this.field = field;
		this.sx = Math.max( 1, step_x );
		this.sy = Math.max( 1, step_y );
		this.sz = Math.max( 1, step_z );
	}
	@Override public int getWidth()  {	return field.getWidth()/sx; }
	@Override public int getHeight() {	return field.getHeight()/sy; }
	@Override public int getDepth()  {	return field.getDepth()/sz; }
	@Override public float getValue(int x, int y, int z) { 
		return field.getValue(x*sx, y*sy, z*sz); 
	}		
}