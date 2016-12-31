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
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Set;

public interface EvictionPolicy {
	public void Evict(int page_count) throws IOException;

	public abstract class BasePolicy implements EvictionPolicy {

		FullyAssociativeCache<?> cache;
		boolean   verbose = false;

		public BasePolicy(FullyAssociativeCache<?> cache, boolean verbose) {
			this.cache   = cache;
			this.verbose = verbose;
		}

	}

	
	public class LRU extends BasePolicy {

		public LRU(FullyAssociativeCache<?> cache, boolean verbose) {
			super(cache, verbose);
		}

		@Override
		public void Evict(int page_count) throws IOException {
			if (cache.page_table.size() <= cache.page_count) {
				return;
			}

			long[] lru_page = new long[page_count];
			long[] lru_time = new long[page_count];
			Arrays.fill(lru_page, -1);

			Set<Entry<Long,BasePage>> x = cache.page_table.entrySet();
			for (Entry<Long, ? extends BasePage> page : x ) {
				long cur_page = page.getKey();
				long cur_time = page.getValue().getAccessTime();
				for (int i = 0; i < lru_page.length; i++) {
					if (lru_page[i] == -1 || cur_time < lru_time[i]) {
						long ltmp = cur_page;
						long ftmp = cur_time;
						cur_page = lru_page[i];
						cur_time = lru_time[i];
						lru_page[i] = ltmp;
						lru_time[i] = ftmp;
					}
				}
			}
			cache.evict(lru_page);

		}

	}
	

}
