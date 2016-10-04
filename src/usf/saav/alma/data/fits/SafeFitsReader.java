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
import usf.saav.common.range.IntRange1D;

// TODO: Auto-generated Javadoc
/**
 * The Class SafeFitsReader.
 */
public class SafeFitsReader extends FitsReader.Default implements FitsReader {
	
	private FitsReader reader;

	/**
	 * Instantiates a new safe fits reader.
	 *
	 * @param reader the reader
	 * @param verbose the verbose
	 */
	public SafeFitsReader(FitsReader reader, boolean verbose){
		super(verbose);
		this.reader = reader;
	}
	
	/* (non-Javadoc)
	 * @see usf.saav.alma.data.fits.FitsReader#getFile()
	 */
	@Override public File getFile() { return reader.getFile(); }

	/* (non-Javadoc)
	 * @see usf.saav.alma.data.fits.FitsReader#getAxesSize()
	 */
	@Override
	public IntRange1D[] getAxesSize() {
		return reader.getAxesSize();
	}

	/* (non-Javadoc)
	 * @see usf.saav.alma.data.fits.FitsReader#getElement(int, int, int, int)
	 */
	@Override
	public float getElement(int x, int y, int z, int w) {
		if( !reader.getAxesSize()[0].inRange(x) ) return Float.NaN;
		if( !reader.getAxesSize()[1].inRange(y) ) return Float.NaN;
		if( !reader.getAxesSize()[2].inRange(z) ) return Float.NaN;
		if( !reader.getAxesSize()[3].inRange(w) ) return Float.NaN;
		return reader.getElement(x, y, z, w);
	}

	/* (non-Javadoc)
	 * @see usf.saav.alma.data.fits.FitsReader#getRow(usf.saav.common.range.IntRange1D, int, int, int)
	 */
	@Override
	public ScalarField1D getRow(IntRange1D x_range, int y, int z, int w) throws IOException {
		if( !reader.getAxesSize()[1].inRange(y) ) return new ScalarField1D.Empty( x_range.length(), Float.NaN );
		if( !reader.getAxesSize()[2].inRange(z) ) return new ScalarField1D.Empty( x_range.length(), Float.NaN );
		if( !reader.getAxesSize()[3].inRange(w) ) return new ScalarField1D.Empty( x_range.length(), Float.NaN );

		// TODO: Make this safe
		return reader.getRow(x_range, y, z, w);
	}

	/* (non-Javadoc)
	 * @see usf.saav.alma.data.fits.FitsReader#getColumn(int, usf.saav.common.range.IntRange1D, int, int)
	 */
	@Override
	public ScalarField1D getColumn(int x, IntRange1D y_range, int z, int w) throws IOException {
		if( !reader.getAxesSize()[0].inRange(x) ) return new ScalarField1D.Empty( y_range.length(), Float.NaN );
		if( !reader.getAxesSize()[2].inRange(z) ) return new ScalarField1D.Empty( y_range.length(), Float.NaN );
		if( !reader.getAxesSize()[3].inRange(w) ) return new ScalarField1D.Empty( y_range.length(), Float.NaN );

		// TODO: Make this safe
		return reader.getColumn(x, y_range, z, w);
	}

	/* (non-Javadoc)
	 * @see usf.saav.alma.data.fits.FitsReader#getLine(int, int, usf.saav.common.range.IntRange1D, int)
	 */
	@Override
	public ScalarField1D getLine(int x, int y, IntRange1D z_range, int w) throws IOException {
		if( !reader.getAxesSize()[0].inRange(x) ) return new ScalarField1D.Empty( z_range.length(), Float.NaN );
		if( !reader.getAxesSize()[1].inRange(y) ) return new ScalarField1D.Empty( z_range.length(), Float.NaN );
		if( !reader.getAxesSize()[3].inRange(w) ) return new ScalarField1D.Empty( z_range.length(), Float.NaN );

		// TODO: Make this safe
		return reader.getLine(x, y, z_range, w);
	}

	/* (non-Javadoc)
	 * @see usf.saav.alma.data.fits.FitsReader#getSlice(usf.saav.common.range.IntRange1D, usf.saav.common.range.IntRange1D, int, int)
	 */
	@Override
	public ScalarField2D getSlice(IntRange1D x_range, IntRange1D y_range, int z, int w) throws IOException {
		if( !reader.getAxesSize()[2].inRange(z) ) return new ScalarField2D.Empty( x_range.length(), y_range.length(), Float.NaN );
		if( !reader.getAxesSize()[3].inRange(w) ) return new ScalarField2D.Empty( x_range.length(), y_range.length(), Float.NaN );
		return new SafeSlice( x_range, y_range, z, w);
	}

	/* (non-Javadoc)
	 * @see usf.saav.alma.data.fits.FitsReader#getVolume(usf.saav.common.range.IntRange1D, usf.saav.common.range.IntRange1D, usf.saav.common.range.IntRange1D, int)
	 */
	@Override
	public ScalarField3D getVolume(IntRange1D x_range, IntRange1D y_range, IntRange1D z_range, int w) throws IOException {
		if( !reader.getAxesSize()[3].inRange(w) ) return new ScalarField3D.Empty( x_range.length(), y_range.length(), z_range.length(), Float.NaN );
		return new SafeVolume( x_range, y_range, z_range, w );
	}
	
	class SafeVolume extends ScalarField3D.Default {

		ScalarField3D baseVolume;
		
		IntRange1D rx,ry,rz;
		int ox, oy, oz;
		int ex, ey, ez;
		
		SafeVolume(IntRange1D x_range, IntRange1D y_range, IntRange1D z_range, int w) throws IOException {
			IntRange1D [] volSize = reader.getAxesSize();
			
			rx = x_range;
			ry = y_range;
			rz = z_range;
			
			IntRange1D subVolRX;
			IntRange1D subVolRY;
			IntRange1D subVolRZ;
			subVolRX = new IntRange1D( Math.max(volSize[0].start(), x_range.start()), Math.min( volSize[0].end(), x_range.end() ) );
			subVolRY = new IntRange1D( Math.max(volSize[1].start(), y_range.start()), Math.min( volSize[1].end(), y_range.end() ) );
			subVolRZ = new IntRange1D( Math.max(volSize[2].start(), z_range.start()), Math.min( volSize[2].end(), z_range.end() ) );
			
			ox = subVolRX.start()-rx.start();
			oy = subVolRY.start()-ry.start();
			oz = subVolRZ.start()-rz.start();
			
			ex = ox+subVolRX.length();
			ey = oy+subVolRY.length();
			ez = oz+subVolRZ.length();
			
			baseVolume = reader.getVolume(subVolRX, subVolRY, subVolRZ, w);
		}
		
		@Override public double [] getCoordinate( int x, int y, int z ){ return baseVolume.getCoordinate(x, y, z); }
		@Override public int getWidth() {  return rx.length(); }
		@Override public int getHeight() { return ry.length(); }
		@Override public int getDepth() {  return rz.length(); }

		@Override
		public float getValue(int x, int y, int z) {
			if( x < ox || x >= ex ) return Float.NaN;
			if( y < oy || y >= ey ) return Float.NaN;
			if( z < oz || z >= ez ) return Float.NaN;
			return baseVolume.getValue(x-ox, y-oy, z-oz);
		}
	}

	class SafeSlice extends ScalarField2D.Default {

		ScalarField2D baseSlice;
		
		IntRange1D rx,ry;
		int z;
		int ox, oy;
		int ex, ey;
		
		SafeSlice(IntRange1D x_range, IntRange1D y_range, int z, int w) throws IOException {
			IntRange1D [] volSize = reader.getAxesSize();
			
			this.rx = x_range;
			this.ry = y_range;
			this.z  = z;
			
			IntRange1D subVolRX;
			IntRange1D subVolRY;
			subVolRX = new IntRange1D( Math.max(volSize[0].start(), x_range.start()), Math.min( volSize[0].end(), x_range.end() ) );
			subVolRY = new IntRange1D( Math.max(volSize[1].start(), y_range.start()), Math.min( volSize[1].end(), y_range.end() ) );
			
			ox = subVolRX.start()-rx.start();
			oy = subVolRY.start()-ry.start();
			
			ex = ox+subVolRX.length();
			ey = oy+subVolRY.length();
			
			baseSlice = reader.getSlice(subVolRX, subVolRY, z, w);
		}
		
		@Override public double [] getCoordinate( int x, int y ){ return baseSlice.getCoordinate(x, y); }
		@Override public int getWidth() {  return rx.length(); }
		@Override public int getHeight() { return ry.length(); }

		@Override
		public float getValue(int x, int y) {
			if( x < ox || x >= ex ) return Float.NaN;
			if( y < oy || y >= ey ) return Float.NaN;
			return baseSlice.getValue(x-ox, y-oy);
		}
	}
}
