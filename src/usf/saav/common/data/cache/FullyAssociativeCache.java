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
package usf.saav.common.data.cache;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class FullyAssociativeCache<PageType extends BasePage> extends BaseCache<PageType> {

	
	protected HashMap<Long, BasePage>    page_table = new HashMap<Long,BasePage>();
	protected EvictionPolicy			 evict;


	public FullyAssociativeCache( String file, int pg_size, int pg_count, boolean read_only, boolean verbose ) throws IOException {
		super(file, pg_size, pg_count, read_only, verbose);
		evict = new EvictionPolicy.LRU(this,verbose);
	}


	public void writeBackAll() throws IOException {
		Iterator<Entry<Long, BasePage>> it = page_table.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Long, BasePage> page = it.next();
			if( page.getValue().writeBack() && verbose) System.out.printf( "Wrote-back page %d\n", page.getKey() );
		}
	}	
	
	public void evict( long ... pages ) throws IOException {
		for ( long page : pages ) {
			if (page == -1) continue;
			if (!page_table.containsKey(page)) continue;
		
			boolean wb = page_table.get(page).writeBack();
			page_table.remove(page);
			if (verbose) {
				System.out.printf("Evicting page %d %s\n", page,
						(wb ? "(wb)" : ""));
			}
		}		
	}	
	


	@Override
	public void PrintPageInfo( ){
		System.out.printf("Cache Page Info\n");
		Iterator<Entry<Long, BasePage>> it = page_table.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Long, BasePage> page = it.next();
			System.out.printf("  Page %d: %d / %d\n", page.getKey(),page.getValue().getAccessTime(), page.getValue().getAccessCount());
		}
	}


	@Override
	public int GetCurrentPageCount() {
		return page_table.size();
	}
	
	
	protected abstract BasePage createPage( ) throws IOException ;
	
	@SuppressWarnings("unchecked")
	protected PageType getPage( long page_id ) throws IOException {
		if( page_table.size() > page_count ){
			evict.Evict(4);
		}
		if( !page_table.containsKey( page_id ) ){
			page_table.put( page_id, createPage()  );
			if( verbose ){
				System.out.printf( "Load page %d\n", page_id );
			}
		}
		return (PageType) page_table.get(page_id);
	}

	
	/*
	protected IntPage GetIntPage( long page_id ) throws IOException {
		if( !page_table.containsKey( page_id ) ){
			page_table.put( page_id, new IntPage( page_id*page_size ) );
			if( verbose ){
				System.out.printf( "Load page %d\n", page_id );
			}
		}
		return (IntPage) page_table.get(page_id);
	}

	protected LongPage GetLongPage( long page_id ) throws IOException {
		if( !page_table.containsKey( page_id ) ){
			page_table.put( page_id, new LongPage( page_id*page_size ) );
			if( verbose ){
				System.out.printf( "Load page %d\n", page_id );
			}
		}
		return (LongPage) page_table.get(page_id);
	}
	
	
	protected FloatPage GetFloatPage( long page_id ) throws IOException {
		if( !page_table.containsKey( page_id ) ){
			page_table.put( page_id, new FloatPage( page_id*page_size ) );
			if( verbose ){
				System.out.printf( "Load page %d\n", page_id );
			}
		}
		return (FloatPage) page_table.get(page_id);
	}
	
	protected DoublePage GetDoublePage( long page_id ) throws IOException {
		if( !page_table.containsKey( page_id ) ){
			page_table.put( page_id, new DoublePage( page_id*page_size ) );
			if( verbose ){
				System.out.printf( "Load page %d\n", page_id );
			}
		}
		return (DoublePage) page_table.get(page_id);
	}		

	
	
	public class DoublePage extends BasePage {

		public DoublePage( long location ) throws IOException {
			super( location );
		}

		public double getValue( int elem ) {
			AccessEvent(false);
			if( elem >= 0 && elem < data.length )
				return data[elem];
			return Double.NaN;
		}
		
		public void setValue( int elem, double val ){
			AccessEvent(true);
			if( elem >= 0 && elem < data.length )
				data[elem] = val;
		}
		
		public int size( ){
			return data.length;
		}
		
		protected void Deserialize( ByteBuffer bb ){
			if( data == null ) 
				data = new double[page_size/8];
			
			if( bb != null ){
				for(int i = 0; i < data.length; i++){
					data[i] = bb.getDouble();
				}
			}
		}
		
		protected void Serialize( ByteBuffer bb ){
			for(int i = 0; i < data.length; i++){
				bb.putDouble( data[i] );
			}
		}	
		
		double [] data;
		
	}	
	
	
	public class IntPage extends BasePage {

		public IntPage( long location ) throws IOException {
			super( location );
		}

		public int getValue(int item) {
			AccessEvent( false );
			if( item >= 0 && item < data.length )
				return data[item];
			return Integer.MIN_VALUE; 
		}
		
		public void setValue( int item, int val ){
			AccessEvent( true );
			if( item >= 0 && item < data.length )
				data[item] = val;
		}
		
		public int size( ){
			return data.length;
		}
		
		protected void Deserialize( ByteBuffer bb ){
			if( data == null ) 
				data = new int[page_size/4];
			
			if( bb != null ){
				for(int i = 0; i < data.length; i++){
					data[i] = bb.getInt();
				}
			}
		}
		
		protected void Serialize( ByteBuffer bb ){
			for(int i = 0; i < data.length; i++){
				bb.putInt( data[i] );
			}
		}
		
		int [] data;
		
	}	
	
	

	
	public class LongPage extends BasePage {

		public LongPage( long location ) throws IOException {
			super( location );
		}

		public long getValue(int item) {
			AccessEvent( false );
			if( item >= 0 && item < data.length )
				return data[item];
			return Long.MIN_VALUE; 
		}
		
		public void setValue( int item, long val ){
			AccessEvent( true );
			if( item >= 0 && item < data.length )
				data[item] = val;
		}
		
		public int size( ){
			return data.length;
		}
		
		protected void Deserialize( ByteBuffer bb ){
			if( data == null ) 
				data = new long[page_size/8];
			
			if( bb != null ){
				for(int i = 0; i < data.length; i++){
					data[i] = bb.getLong();
				}
			}
		}
		
		protected void Serialize( ByteBuffer bb ){
			for(int i = 0; i < data.length; i++){
				bb.putLong( data[i] );
			}
		}
		
		long [] data;
		
	}	
*/
	
	
}
