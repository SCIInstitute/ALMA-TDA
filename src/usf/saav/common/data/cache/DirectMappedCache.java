package usf.saav.common.data.cache;

import java.io.IOException;

public abstract class DirectMappedCache< PageType extends BasePage > extends BaseCache<PageType> {

	
	protected BasePage [] 				 page_table;

	protected abstract BasePage createPage( ) throws IOException ;
	
	public DirectMappedCache( String file, int pg_size, int pg_count, boolean read_only, boolean verbose ) throws IOException {
		super(file, pg_size, pg_count, read_only, verbose );

		this.page_table  = new BasePage[pg_count];
		for(int i = 0; i < pg_count; i++){
			page_table[i] = createPage( );
		}
		
	}
	
	long loadedPages = 0;
	
	@SuppressWarnings("unchecked")
	protected PageType getPage( long pg ) throws IOException {

		int bank = (int) (pg%page_count);

		while( loadedPages < pg ){
			page_table[bank].loadPage(loadedPages++);
		}

		// if page isn't already in cache, load it
		if( page_table[bank].getPageID() != pg ) 
			page_table[bank].loadPage(pg);

		return (PageType)page_table[bank];
	}
	

	public void writeBackAll() throws IOException {
		for( BasePage p : page_table ){
			if( p.writeBack() && verbose) System.out.printf( "Wrote-back page\n", p.getPageID() );
		}
	}	
	
	 
	public void PrintPageInfo( ){
		System.out.printf("Cache Page Info\n");
		for( BasePage page : page_table )
			System.out.printf("  Page %d: %d / %d\n", page.getPageID(),page.getAccessTime(), page.getAccessCount());
	}
	
	
	public int GetCurrentPageCount( ){
		return GetPageCount();
	}
	
}
