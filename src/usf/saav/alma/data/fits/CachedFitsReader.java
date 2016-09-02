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
import usf.saav.common.data.cache.FloatDMCache;
import usf.saav.common.data.zorder.Partition2D;
import usf.saav.common.range.IntRange1D;

public class CachedFitsReader extends FitsReader.Default implements FitsReader {

	FitsReader reader = null;
	FloatDMCache cache = null;
	Partition2D partition =null;
	long sliceOffset;
	int sx,sy,sz;
	
	private static final int	 page_size_bytes = 8096;
	private static final int	 page_size_elems = 2048;
	private static final int	 page_count      = 2048;
	private static final boolean use_zorder 	 = true; 
	
	public CachedFitsReader( FitsReader reader, boolean verbose ){
		super(verbose);
		
		this.reader = reader;
		
		print_info_message( reader.getFile().getAbsolutePath() );
		
		File cache_file = new File(reader.getFile().getAbsolutePath() + ".cache");
		boolean cache_exists = cache_file.exists();
		
		try {
			cache = new FloatDMCache( cache_file.getAbsolutePath(), page_size_bytes, page_count, false, false );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if( !cache_exists ){
			print_info_message("Initializing Cache");
			for(int i = 0; i < page_size_elems; i++){
				try {
					cache.set(i, -1);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		sx = reader.getAxesSize()[0].length();
		sy = reader.getAxesSize()[1].length();
		sz = reader.getAxesSize()[2].length();
		
		sliceOffset = sx*sy;
		if( use_zorder ){
			partition = new Partition2D( sx, sy );
			sliceOffset = partition.size() + (page_size_elems-1);
			sliceOffset -= sliceOffset % page_size_elems;
		}
		print_info_message("slice offset=" + sliceOffset );
		
		for( int cz = 0; cz < sz; cz++ ){
			try {
				loadSlice(cz);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}

	}
	
	private long getOffset( int x, int y, int z ){
		if( use_zorder )
			return page_size_elems + sliceOffset*z + partition.getOrdered2( x,  y );
		return page_size_elems + sliceOffset*z + y*sy + x;
	}
	
	
	
	private void loadSlice( int z ) throws IOException {
		if( cache.get(z) == z ){
			return;
		}
		print_info_message("Loading slice " + (z) );
		for(int y = 0; y < sy; y+=1024){
			for(int x = 0; x < sx; x+=1024 ){

				ScalarField2D sf = reader.getSlice( new IntRange1D( x, Math.min( sx, x+1024 )-1 ), 
													new IntRange1D( y, Math.min( sy, y+1024 )-1 ),
													z, 0 );
				
				for(int iy = 0; iy < sf.getHeight(); iy++ ){
					for(int ix = 0; ix < sf.getWidth(); ix++ ){
						cache.set(  getOffset( x+ix,  y+iy, z ), 
									sf.getValue(ix, iy) );
					}
				}
				
			}
		}
		cache.set(z,z);
		cache.writeBackAll();
	}
	
	@Override public File getFile() { return reader.getFile(); }

	
	@Override
	public IntRange1D[] getAxesSize() {
		return reader.getAxesSize();
	}

	@Override
	public float getElement(int x, int y, int z, int w) {
		return reader.getElement(x, y, z, w);
	}

	@Override
	public ScalarField1D getRow(IntRange1D x_range, int y, int z, int w) throws IOException {
		return reader.getRow(x_range, y, z, w);
	}

	@Override
	public ScalarField1D getColumn(int x, IntRange1D y_range, int z, int w) throws IOException {
		return reader.getColumn(x, y_range, z, w);
	}

	@Override
	public ScalarField1D getLine(int x, int y, IntRange1D z_range, int w) throws IOException {
		return reader.getLine(x, y, z_range, w);
	}

	@Override
	public ScalarField2D getSlice(IntRange1D x_range, IntRange1D y_range, int z, int w) throws IOException {
		if( w != 0 ) return reader.getSlice(x_range, y_range, z, w);
		return new CachedSlice(x_range,y_range,z);
	}

	@Override
	public ScalarField3D getVolume(IntRange1D x_range, IntRange1D y_range, IntRange1D z_range, int w) throws IOException {
		if( w != 0 ) return reader.getVolume(x_range, y_range, z_range, w);
		return new CachedVolume( x_range, y_range, z_range );
	}
	
	class CachedSlice extends ScalarField2D.Default {
		IntRange1D x, y;
		int z;
		
		CachedSlice( IntRange1D x_range, IntRange1D y_range, int z ){
			print_info_message("getSlice( " + x_range.toString() + ", " + y_range.toString() + ", " + z + ", " + 0 + " )");
			this.x = x_range;
			this.y = y_range;
			this.z = z;
					
			try {
				loadSlice(z);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public float getValue(int ix, int iy) {
			long el = getOffset( x.start()+ix,  y.start()+iy, z );
			try {
				return cache.get( el );
			} catch (Exception e) {
				e.printStackTrace();
				print_warning_message( (x.start()+ix) + "," + (y.start()+iy) + "," +  z + " -> " + el );
			}
			return Float.NaN;
		}

		@Override public int getWidth() { return x.length(); }
		@Override public int getHeight() { return y.length(); }
	}
	

	class CachedVolume extends ScalarField3D.Default {
		IntRange1D x, y, z;
		
		CachedVolume( IntRange1D x_range, IntRange1D y_range, IntRange1D z_range ){
			print_info_message("getVolume( " + x_range.toString() + ", " + y_range.toString() + ", " + z_range.toString() + ", " + 0 + " )");
			this.x = x_range;
			this.y = y_range;
			this.z = z_range;
					
			try {
				for( int _z = z.start(); _z <= z.end(); _z++ ){
					loadSlice(_z);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public float getValue(int ix, int iy, int iz) {
			long el = getOffset( x.start()+ix,  y.start()+iy, z.start()+iz );
			try {
				return cache.get( el );
			} catch (Exception e) {
				e.printStackTrace();
				print_warning_message( (x.start()+ix) + "," + (y.start()+iy) + "," +  (z.start()+iz) + " -> " + el );
			}
			return Float.NaN;
		}

		@Override public int getWidth() { return x.length(); }
		@Override public int getHeight() { return y.length(); }
		@Override public int getDepth() { return z.length(); }
	}

}
