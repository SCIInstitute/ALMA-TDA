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
package usf.saav.alma.data.fits;

import java.io.File;
import java.io.IOException;

import usf.saav.alma.data.ScalarField1D;
import usf.saav.alma.data.ScalarField2D;
import usf.saav.alma.data.ScalarField3D;
import usf.saav.common.BasicObject;
import usf.saav.common.range.IntRange1D;

public interface FitsReader {

	public File getFile();

	public IntRange1D[] getAxesSize();
	
	public FitsHistory getHistory( );
	public FitsProperties getProperties( );
	public FitsTable getTable();


	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A SINGLE ELEMENT FROM THE DATA            //
	/////////////////////////////////////////////////////////////////////
	public float getElement( int x, int y, int z, int w );


	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A ROW FROM THE DATA                       //
	/////////////////////////////////////////////////////////////////////
	public ScalarField1D getRow( int y, int z, int w ) throws IOException;
	public ScalarField1D getRow( IntRange1D x_range, int y, int z, int w ) throws IOException;


	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A COLUMN FROM THE DATA                    //
	/////////////////////////////////////////////////////////////////////
	public ScalarField1D getColumn( int x, int z, int w ) throws IOException;
	public ScalarField1D getColumn( int x, IntRange1D y_range, int z, int w ) throws IOException;


	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A LINE FROM THE DATA                      //
	/////////////////////////////////////////////////////////////////////
	public ScalarField1D getLine( int x, int y, int w ) throws IOException;
	public ScalarField1D getLine( int x, int y, IntRange1D z_range, int w ) throws IOException;


	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A SLICE FROM THE DATA                     //
	/////////////////////////////////////////////////////////////////////
	public ScalarField2D getSlice( int z, int w ) throws IOException;
	public ScalarField2D getSlice( IntRange1D x_range, IntRange1D y_range, int z, int w ) throws IOException;


	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A CUBE/VOLUME FROM THE DATA               //
	/////////////////////////////////////////////////////////////////////
	public ScalarField3D getVolume( int w ) throws IOException;
	public ScalarField3D getVolume( IntRange1D x_range, IntRange1D y_range, IntRange1D z_range, int w ) throws IOException;



	public abstract class Default extends BasicObject implements FitsReader {

		public Default(boolean verbose) {
			super(verbose);
		}

		public ScalarField1D getRow( int y, int z, int w ) throws IOException {
			return getRow( getAxesSize()[0], y, z, w );
		}

		public ScalarField1D getColumn( int x, int z, int w ) throws IOException {
			return getColumn( x, getAxesSize()[1], z, w );
		}

		public ScalarField1D getLine( int x, int y, int w ) throws IOException {
			return getLine( x, y, getAxesSize()[2], w );
		}

		public ScalarField2D getSlice( int z, int w ) throws IOException {
			return getSlice( getAxesSize()[0], getAxesSize()[1], z, w );
		}

		public ScalarField3D getVolume( int w ) throws IOException {
			return getVolume( getAxesSize()[0], getAxesSize()[1], getAxesSize()[2], w );
		}

	}
	
	
}
