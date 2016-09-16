package usf.saav.common.data.cache;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.concurrent.Semaphore;

public abstract class BasePage {

	private static long currentAccessTime = 0;
	
		private FileChannel   file_chan;
		private long 		  access_count;
		private long 		  access_time;
		private boolean 	  dirty;
		private ByteBuffer 	  bb;
		private Semaphore	  load_sem = new Semaphore(1);
		private Semaphore	  save_sem = new Semaphore(1);

		protected int 		  page_size;
		protected long 		  page;
		
		protected BasePage( FileChannel file_chan, int page_size ) throws IOException {
			this.file_chan	  = file_chan;
			this.page_size	  = page_size;
			//this.access_time  = System.currentTimeMillis();
			this.access_time  = currentAccessTime;
			this.access_count = 0;
			this.page 	 	  = -1;
			this.dirty 		  = false;
			
			this.bb = ByteBuffer.allocate(page_size);
			this.bb.order(ByteOrder.LITTLE_ENDIAN);
			this.bb.rewind();
			
		}
		
		
		public boolean loadPage( long pg ) throws IOException {
			if( this.page == pg ) return false;
			
			try {
				load_sem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
			
			writeBack( );
			
			this.page   = pg;
			this.dirty  = false;
			
			if( (page*page_size) >= file_chan.size() ){
				Deserialize( null );
			}
			else {
				file_chan.position( page*page_size );
				file_chan.read(bb);
				bb.rewind();
				
				Deserialize( bb );
				bb.rewind();
			}

			load_sem.release();
			
			return true;
			
		}
		
		
		public boolean writeBack( ) throws IOException {
			if( page < 0 ) return false;
			try {
				save_sem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
			if( dirty ){
				Serialize( bb );
				bb.rewind();

				file_chan.position( page*page_size );
				file_chan.write( bb );
				bb.rewind();

				dirty = false;
				save_sem.release();
				return true; 
			}
			save_sem.release();
			return false;
		}
	
		public long getPageID( ){ return page; }
		public long getAccessTime( ){ return access_time; }
		public long getAccessCount( ){ return access_count; }
		
		abstract protected void Deserialize( ByteBuffer bb );
		abstract protected void Serialize( ByteBuffer bb );
		
		protected void AccessEvent( boolean write ){
			//access_time = System.currentTimeMillis();
			access_time = currentAccessTime++;
			access_count++;
			if( write ) dirty = true;
		}
		
		
	}
	
