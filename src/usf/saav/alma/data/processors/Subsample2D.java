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

import usf.saav.alma.data.ScalarField2D;

public class Subsample2D extends ScalarField2D.Default {
	int sx,sy;
	ScalarField2D field;
	public Subsample2D( ScalarField2D field, int step ){
		this(field,step,step);
	}		
	public Subsample2D( ScalarField2D field, int step_x, int step_y ){
		super(false);
		this.field = field;
		this.sx = Math.max( 1, step_x );
		this.sy = Math.max( 1, step_y );
		print_info_message( step_x + " " + step_y + " " + field.getWidth() + " " + field.getHeight() );
	}
	@Override public int getWidth()  {	return field.getWidth()/sx; }
	@Override public int getHeight() {	return field.getHeight()/sy; }
	@Override public float getValue(int x, int y) { 
		return field.getValue(x*sx, y*sy); 
	}		
}
