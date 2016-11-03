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

import nom.tam.fits.BasicHDU;
import nom.tam.fits.BinaryTableHDU;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.HeaderCard;
import nom.tam.fits.ImageHDU;
import nom.tam.image.ImageTiler;
import nom.tam.util.Cursor;
import usf.saav.alma.data.ScalarField1D;
import usf.saav.alma.data.ScalarField2D;
import usf.saav.alma.data.ScalarField3D;
import usf.saav.common.range.IntRange1D;


public class RawFitsReader extends FitsReader.Default implements FitsReader {

	File file;
	Fits fits;

	ImageTiler tiler;

	IntRange1D [] axesRange;
	
	double [] coordOrigin = new double[4];
	double [] coordDelta  = new double[4];

	FitsHistory    history    = new FitsHistory( );
	FitsProperties properties = new FitsProperties( );
	FitsTable      table      = null;


	public RawFitsReader( String filename, boolean verbose ) throws IOException, FitsException {
		super(verbose);

		file = new File(filename);
		fits = new Fits( file, true );

		for(BasicHDU<?> header : fits.read() ){

			if( header instanceof ImageHDU ){
				
				ImageHDU img = (ImageHDU)header;
				int [] axes;
				axes = img.getAxes();

				axesRange = new IntRange1D[axes.length];
				for(int i = 0; i < axes.length; i++){
					axesRange[i] = new IntRange1D(0,axes[axes.length-i-1]-1);
					print_info_message("Axis " + i + " " +axesRange[i].toString());
				}

				coordOrigin[0] = img.getHeader().getDoubleValue("CRVAL1");
				coordOrigin[1] = img.getHeader().getDoubleValue("CRVAL2");
				coordOrigin[2] = img.getHeader().getDoubleValue("CRVAL3");
				coordOrigin[3] = img.getHeader().getDoubleValue("CRVAL4");

				coordDelta[0] = img.getHeader().getDoubleValue("CDELT1");
				coordDelta[1] = img.getHeader().getDoubleValue("CDELT2");
				coordDelta[2] = img.getHeader().getDoubleValue("CDELT3");
				coordDelta[3] = img.getHeader().getDoubleValue("CDELT4");

				
      		   Cursor<String, HeaderCard> iter = img.getHeader().iterator();
      		   HeaderCard card;
      		   while( (card=iter.next()) != null ){
      			   if( card.getKey().compareTo("HISTORY")==0 ){
      				   history.add( card.getComment() );
      			   }
      			   else if( card.getKey().compareTo("END")==0 ){
      				   break;
      			   }
      			   else if( card.getKey().length()==0 ){
      				   continue;
      			   }
      			   else{
      				 properties.add( new FitsProperty( card.getKey(), card.getValue(), card.getComment() ) );
      			   }
      		   }
      		   
				tiler = img.getTiler();

			}
			else if ( header instanceof BinaryTableHDU ){

				BinaryTableHDU bt = (BinaryTableHDU)header;

				table = new FitsTable( bt.getNRows(), bt.getNCols() );
				for( int i = 0; i < bt.getNCols(); i++){
					table.setColumnLabel(i, bt.getColumnName(i));
				}
				
				for( int row = 0; row < bt.getNRows(); row++){
					for(int col = 0; col < bt.getNCols(); col++){
						table.setData( row, col, bt.getElement( row, col) );
					}
				}

				for( int s : bt.getAxes() ){
					print_info_message( Integer.toString(s) );
				}
				
				print_info_message( table.toString() );
				
			}
			else{
				print_warning_message("Unknown Header Type: " + header.getClass().getSimpleName() );
			}

		}


	}

	public void finalize(){
		try {
			fits.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public FitsHistory getHistory( ){
		return history;
	}
	
	@Override
	public FitsProperties getProperties( ){
		return properties;
	}

	@Override
	public FitsTable getTable( ){
		return table;
	}

	

	public IntRange1D[] getAxesSize(){ return this.axesRange; } 

	public File getFile( ){ return file; }

	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A SINGLE ELEMENT FROM THE DATA            //
	/////////////////////////////////////////////////////////////////////


	public float getElement( int x, int y, int z, int w ){
		try {
			return ((float[])tiler.getTile(new int[]{w,z,y,x},new int[]{1,1,1,1}))[0];
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Float.NaN;
	}

	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A ROW FROM THE DATA                       //
	/////////////////////////////////////////////////////////////////////
	public ScalarField1D getRow( IntRange1D x_range, int y, int z, int w ) throws IOException{
		return new FitsRow( x_range, y, z, w );
	}


	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A COLUMN FROM THE DATA                    //
	/////////////////////////////////////////////////////////////////////
	public ScalarField1D getColumn( int x, IntRange1D y_range, int z, int w ) throws IOException{
		return new FitsColumn( x, y_range, z, w );
	}



	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A LINE FROM THE DATA                      //
	/////////////////////////////////////////////////////////////////////
	public ScalarField1D getLine( int x, int y, IntRange1D z_range, int w ) throws IOException{
		return new FitsLine( x,y,z_range, w );
	}


	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A SLICE FROM THE DATA                     //
	/////////////////////////////////////////////////////////////////////
	public ScalarField2D getSlice( IntRange1D x_range, IntRange1D y_range, int z, int w ) throws IOException{
		print_info_message("getSlice( [" + x_range.start() + ", " + x_range.end() + "], [" + y_range.start() + ", " + y_range.end() + "], " + z + ", " + w + " )");
		return new FitsSlice( x_range, y_range, z,w );
	}


	/////////////////////////////////////////////////////////////////////
	// FUNCTIONS FOR GETTING A CUBE/VOLUME FROM THE DATA               //
	/////////////////////////////////////////////////////////////////////
	public ScalarField3D getVolume( IntRange1D x_range, IntRange1D y_range, IntRange1D z_range, int w ) throws IOException{
		print_info_message("getVolume( [" + x_range.start() + ", " + x_range.end() + "], [" + y_range.start() + ", " + y_range.end() + "], [" + z_range.start() + ", " + z_range.end() + "], " + w + " )");
		return new FitsVolume(x_range, y_range, z_range, w);
	}	

	/////////////////////////////////////////////////////////////////////
	// INTERNAL FUNCTIONS TO FORM DATA QUERY                           //
	/////////////////////////////////////////////////////////////////////

	private int [] tilePosition( int x, int y, int z, int w ){
		return new int[]{w,z,y,x};
	}
	private int [] tileSize( int sx, int sy, int sz, int sw ){
		return new int[]{sw,sz,sy,sx};
	}

	class FitsRow extends ScalarField1D.Default {

		float [] data;
		int x0 = 0;

		public FitsRow(IntRange1D x, int y, int z, int w) throws IOException {
			data = (float[]) tiler.getTile( 
					tilePosition( x.start(), y, z, w ), 
					tileSize( x.length(), 1, 1, 1 ) 
					);
		}

		@Override 
		public double getCoordinate( int x ){ 
			return coordOrigin[0] + (x0+x)*coordDelta[0];
		}

		@Override public int getWidth() { return data.length; }
		@Override public int getSize() { return data.length; }
		@Override public float getValue(int nodeID) { return data[nodeID]; }
	}

	class FitsColumn extends ScalarField1D.Default {

		float [] data;
		int y0 = 0;

		public FitsColumn(int x, IntRange1D y, int z, int w) throws IOException {
			data = (float[]) tiler.getTile( 
					tilePosition( x, y.start(), z, w ), 
					tileSize( 1, y.length(), 1, 1 ) 
					);

		}

		@Override
		public double getCoordinate( int y ){
			return coordOrigin[1] + (y0+y)*coordDelta[1];
		}

		@Override public int getWidth() { return data.length; }
		@Override public int getSize() { return data.length; }
		@Override public float getValue(int nodeID) { return data[nodeID]; }
	}

	class FitsLine extends ScalarField1D.Default {

		float [] data;
		int z0 = 0;

		public FitsLine(int x, int y, IntRange1D z, int w) throws IOException {
			data = (float[]) tiler.getTile( 
					tilePosition( x, y, z.start(), w ), 
					tileSize( 1, 1, z.length(), 1 ) 
					);
		}

		@Override
		public double getCoordinate( int z ){
			return coordOrigin[2] + (z0+z)*coordDelta[2];
		}

		@Override public int getWidth() { return data.length; }
		@Override public int getSize() { return data.length; }
		@Override public float getValue(int nodeID) { return data[nodeID]; }
	}


	public class FitsSlice extends ScalarField2D.Default {

		float [] data;
		int x0=0, y0=0;
		int width,height;

		public FitsSlice( IntRange1D x, IntRange1D y, int z, int w) throws IOException {
			width  = x.length();
			height = y.length();
			data = (float[]) tiler.getTile( 
					tilePosition( x.start(), y.start(), z, w ), 
					tileSize( x.length(), y.length(), 1, 1 ) 
					);
		}

		@Override
		public double [] getCoordinate( int x, int y ){
			return new double[]{
					coordOrigin[0] + (x0+x)*coordDelta[0],
					coordOrigin[1] + (y0+y)*coordDelta[1]
			};
		}

		@Override public int getWidth()  { return width; }
		@Override public int getHeight() { return height; }
		@Override public float getValue(int x, int y) { return data[y*width+x]; }

	}

	public class FitsVolume extends ScalarField3D.Default {

		float [] data;
		int width,height,depth;
		int x0=0, y0=0, z0=0;

		public FitsVolume( IntRange1D x, IntRange1D y, IntRange1D z, int w) throws IOException {
			width  = x.length();
			height = y.length();
			depth  = z.length();
			data = (float[]) tiler.getTile( 
					tilePosition( x.start(), y.start(), z.start(), w ), 
					tileSize( x.length(), y.length(), z.length(), 1 ) 
					);
		}

		@Override
		public double [] getCoordinate( int x, int y, int z ){
			return new double[]{
					coordOrigin[0] + (x0+x)*coordDelta[0],
					coordOrigin[1] + (y0+y)*coordDelta[1],
					coordOrigin[2] + (z0+z)*coordDelta[2]
			};
		}

		@Override public int getWidth()  { return width; }
		@Override public int getHeight() { return height; }
		@Override public int getDepth()  { return depth; }
		@Override public float getValue(int x, int y, int z) { return data[z*width*height + y*width + x]; }

	}



}

