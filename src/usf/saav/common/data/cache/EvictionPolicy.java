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
