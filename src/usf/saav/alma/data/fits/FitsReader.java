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

// TODO: Auto-generated Javadoc
/**
 * The Interface FitsReader.
 */
public interface FitsReader {

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile();
	
	/**
	 * Gets the axes size.
	 *
	 * @return the axes size
	 */
	public IntRange1D[] getAxesSize();
	
	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A SINGLE ELEMENT FROM THE DATA            //
	/**
	 * Gets the element.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @param w the w
	 * @return the element
	 */
	/////////////////////////////////////////////////////////////////////
	public float getElement( int x, int y, int z, int w );

	
	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A ROW FROM THE DATA                       //
	/**
	 * Gets the row.
	 *
	 * @param y the y
	 * @param z the z
	 * @param w the w
	 * @return the row
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/////////////////////////////////////////////////////////////////////
	public ScalarField1D getRow( int y, int z, int w ) throws IOException;
	
	/**
	 * Gets the row.
	 *
	 * @param x_range the x range
	 * @param y the y
	 * @param z the z
	 * @param w the w
	 * @return the row
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ScalarField1D getRow( IntRange1D x_range, int y, int z, int w ) throws IOException;
	
	
	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A COLUMN FROM THE DATA                    //
	/**
	 * Gets the column.
	 *
	 * @param x the x
	 * @param z the z
	 * @param w the w
	 * @return the column
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/////////////////////////////////////////////////////////////////////
	public ScalarField1D getColumn( int x, int z, int w ) throws IOException;
	
	/**
	 * Gets the column.
	 *
	 * @param x the x
	 * @param y_range the y range
	 * @param z the z
	 * @param w the w
	 * @return the column
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ScalarField1D getColumn( int x, IntRange1D y_range, int z, int w ) throws IOException;
	
	
	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A LINE FROM THE DATA                      //
	/**
	 * Gets the line.
	 *
	 * @param x the x
	 * @param y the y
	 * @param w the w
	 * @return the line
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/////////////////////////////////////////////////////////////////////
	public ScalarField1D getLine( int x, int y, int w ) throws IOException;
	
	/**
	 * Gets the line.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z_range the z range
	 * @param w the w
	 * @return the line
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ScalarField1D getLine( int x, int y, IntRange1D z_range, int w ) throws IOException;
	
	
	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A SLICE FROM THE DATA                     //
	/**
	 * Gets the slice.
	 *
	 * @param z the z
	 * @param w the w
	 * @return the slice
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/////////////////////////////////////////////////////////////////////
	public ScalarField2D getSlice( int z, int w ) throws IOException;
	
	/**
	 * Gets the slice.
	 *
	 * @param x_range the x range
	 * @param y_range the y range
	 * @param z the z
	 * @param w the w
	 * @return the slice
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ScalarField2D getSlice( IntRange1D x_range, IntRange1D y_range, int z, int w ) throws IOException;
	

	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A CUBE/VOLUME FROM THE DATA               //
	/**
	 * Gets the volume.
	 *
	 * @param w the w
	 * @return the volume
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/////////////////////////////////////////////////////////////////////
	public ScalarField3D getVolume( int w ) throws IOException;
	
	/**
	 * Gets the volume.
	 *
	 * @param x_range the x range
	 * @param y_range the y range
	 * @param z_range the z range
	 * @param w the w
	 * @return the volume
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ScalarField3D getVolume( IntRange1D x_range, IntRange1D y_range, IntRange1D z_range, int w ) throws IOException;
	
	
	
	/**
	 * The Class Default.
	 */
	public abstract class Default extends BasicObject implements FitsReader {

		/**
		 * Instantiates a new default.
		 *
		 * @param verbose the verbose
		 */
		public Default(boolean verbose) {
			super(verbose);
		}
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.fits.FitsReader#getRow(int, int, int)
		 */
		public ScalarField1D getRow( int y, int z, int w ) throws IOException {
			return getRow( getAxesSize()[0], y, z, w );
		}
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.fits.FitsReader#getColumn(int, int, int)
		 */
		public ScalarField1D getColumn( int x, int z, int w ) throws IOException {
			return getColumn( x, getAxesSize()[1], z, w );
		}
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.fits.FitsReader#getLine(int, int, int)
		 */
		public ScalarField1D getLine( int x, int y, int w ) throws IOException {
			return getLine( x, y, getAxesSize()[2], w );
		}
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.fits.FitsReader#getSlice(int, int)
		 */
		public ScalarField2D getSlice( int z, int w ) throws IOException {
			return getSlice( getAxesSize()[0], getAxesSize()[1], z, w );
		}
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.data.fits.FitsReader#getVolume(int)
		 */
		public ScalarField3D getVolume( int w ) throws IOException {
			return getVolume( getAxesSize()[0], getAxesSize()[1], getAxesSize()[2], w );
		}
		
	}
}
