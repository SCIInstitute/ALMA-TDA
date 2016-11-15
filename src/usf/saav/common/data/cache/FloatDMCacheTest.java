package usf.saav.common.data.cache;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class FloatDMCacheTest {

	private static final int	 page_size_bytes = 8096;
	private static final int	 page_size_elems = 2048;
	private static final int	 page_count      = 2048;
	private static final boolean use_zorder 	 = true; 
	
	private static File cache_file;
	private static FloatDMCache cache;
	
	@BeforeClass
	public static void setupBeforeClass() throws IOException
	{
		cache_file = new File("C:\\alma_data\\fits1\\Continuum_33GHz.fits.cache");
		cache = new FloatDMCache( cache_file.getAbsolutePath(), page_size_bytes, page_count, false, false );
	}
	
	@Test
	public void testFloatDMCacheStringIntIntBooleanBoolean() throws IOException {
		assertTrue(cache != null);
		
		assertEquals(16580608, cache.GetCurrentDataSize());
		assertEquals(16580608, cache.GetDataSize());
		assertEquals(275264, cache.GetFileSize());
		assertEquals(2048, cache.GetPageCount());
		assertEquals(2048, cache.GetCurrentPageCount());
	}

	@Test
	public void testGet() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSet() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testWriteBackAll() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetCurrentPageCount() {
		assertEquals(2048, cache.GetCurrentPageCount());
	}

	@Test
	public void testClose() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetCurrentDataSize() {
		assertEquals(16580608, cache.GetCurrentDataSize());
	}

	@Test
	public void testGetDataSize() {
		assertEquals(16580608, cache.GetDataSize());
	}

	@Test
	public void testGetFileSize() throws IOException {
		assertEquals(275264, cache.GetFileSize());
	}

	@Test
	public void testGetPageCount() {
		assertEquals(2048, cache.GetPageCount());
	}

}
