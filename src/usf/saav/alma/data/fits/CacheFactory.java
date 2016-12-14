/**
 * 
 */
package usf.saav.alma.data.fits;

import java.io.File;
import java.io.IOException;

import usf.saav.common.BasicObject;
import usf.saav.common.data.cache.FloatDMCache;

/**
 * @author dwhite
 *
 */
public interface CacheFactory {
	CacheInterface create(String filename) throws IOException;
	
	
	public class Default extends BasicObject implements CacheFactory {

		public static final int	 page_size_elems = 2048;
		private static final int	 page_size_bytes = 4 * page_size_elems; // sizeof(float) = 4
		private static final int	 page_count      = 2048;

		private static CacheFactory instance_ = new Default();
		public static CacheFactory Instance() { return instance_; }
		
		@Override
		public CacheInterface create(String filename) throws IOException {
			
			File cache_file = new File(filename + ".cache");
			boolean cache_exists = cache_file.exists();
			FloatDMCache cache = new FloatDMCache( cache_file.getAbsolutePath(), page_size_bytes, page_count, false, false );
			
			if( !cache_exists ) {
				print_info_message("Initializing Cache");
				cache.initializeFirstPage();				
			}
			
			return cache;
		}
	
	}
}
