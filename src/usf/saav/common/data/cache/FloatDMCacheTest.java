package usf.saav.common.data.cache;

import static org.junit.Assert.*;
import static java.nio.file.StandardCopyOption.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.BeforeClass;
import org.junit.Test;

public class FloatDMCacheTest {

	private static final int	 page_size_bytes = 8096;
	private static final int	 page_count      = 2048;
	
	private static File cache_file;
	private static FloatDMCache cache;
	
	@BeforeClass
	public static void setupBeforeClass() throws IOException
	{
		cache_file = new File("/Users/dwhite/Dropbox/alma/fits1/Continuum_33GHz.fits.cache");
		cache = new FloatDMCache( cache_file.getAbsolutePath(), page_size_bytes, page_count, false, false );
	}
	
	@Test
	public void testFloatDMCacheCtor() throws IOException {
		assertTrue(cache != null);
		
		assertEquals(16580608, cache.GetCurrentDataSize());
		assertEquals(16580608, cache.GetDataCacheSize());
		assertEquals(275264, cache.GetFileSize());
		assertEquals(2048, cache.GetPageCount());
		assertEquals(2048, cache.GetCurrentPageCount());
	}

	@Test
	public void testFullCacheGet() throws IOException {
		
		float total = 0;
		int numNans = 0;
		int numNegativeOnes = 0;
		
		for (long i = 0; i < cache.GetDataCacheSize() / 4; i++)
		{
			float f = cache.getValue(i);
			if (Float.isNaN(f))
				numNans++;
			else if (f == -1)
				numNegativeOnes++;
			else
				total += f;
		}
		assertEquals(16307104 / 4, numNans);
		assertEquals(8188 / 4, numNegativeOnes);
		assertEquals(293.510376, total, 1e-6);
	}
	
	private static FloatDMCache makeCache(String filename) throws IOException
	{
		File file = new File(filename);
		return new FloatDMCache( file.getAbsolutePath(), page_size_bytes, page_count, false, false );
	}
	
	private static boolean cacheFilesSame(String filename1, String filename2) throws IOException
	{
		FloatDMCache file1Cache = makeCache(filename1);
		FloatDMCache file2Cache = makeCache(filename2);
		
		if (file1Cache.GetDataCacheSize() != file2Cache.GetDataCacheSize())
		{
			System.out.println("Data cache sizes are different.");
			return false;
		}
		
		for (long i = 0; i < file1Cache.GetDataCacheSize() / 4; i++)
		{
			float f1 = file1Cache.getValue(i);
			float f2 = file2Cache.getValue(i);
			if (Float.isNaN(f1) != Float.isNaN(f2))
			{
				System.out.println("NaN values differ at i=" + i);
				return false;
			}

			else if (Math.abs(f1 - f2) > 1e-12)
			{
				System.out.println("Floats differ at i=" + i);
				return false;
			}
		}
		
		return true;
	}
	
	@Test
	public void testCompareCacheFiles() throws IOException
	{
		assertTrue(cacheFilesSame("/Users/dwhite/Dropbox/alma/fits1/Continuum_33GHz.fits.cache",
				"/Users/dwhite/Dropbox/alma/fits1/Continuum_33GHz.fits.cache"));
		assertTrue(cacheFilesSame("/Users/dwhite/Dropbox/alma/fits1/Continuum_33GHz.fits.cache",
				"/Users/dwhite/Dropbox/alma/fits1same/Continuum_33GHz.fits.cache"));
	}
	
	@Test
	public void testFileGet() throws IOException 
	{	
		long numFloats = cache.maxElementForFile();
		assertEquals(68816, numFloats);
		
		float total = 0;
		int numNans = 0;
		int numNegativeOnes = 0;
		
		for (long i = 0; i < numFloats; i++)
		{
			float f = cache.getValue(i);
			if (Float.isNaN(f))
				numNans++;
			else if (f == -1)
				numNegativeOnes++;
			else
				total += f;
		}
		assertEquals(1232, numNans);
		assertEquals(2047, numNegativeOnes);
		assertEquals(293.173645, total, 1e-6);
	}

	private static void copyOverCacheFile(String fits1, String fits2) throws IOException
	{		
		Files.copy(new File(fits1).toPath(), new File(fits2).toPath(), REPLACE_EXISTING);
	}
	
	@Test
	public void testSet() throws IOException 
	{
		String fits1 = "/Users/dwhite/Dropbox/alma/fits1/Continuum_33GHz.fits.cache";
		String fits2 = "/Users/dwhite/Dropbox/alma/fits2/Continuum_33GHz.fits.cache";
		
		copyOverCacheFile(fits1, fits2);
		
		assertTrue(cacheFilesSame(fits1, fits2));
		
		FloatDMCache file2Cache = makeCache(fits2);
		file2Cache.setValue(1000, -42);
		
		assertTrue(cacheFilesSame(fits1, fits2));
		
		file2Cache.writeBackAll();
		
		assertFalse(cacheFilesSame(fits1, fits2));
	}

	@Test
	public void testWriteBackAll() {
		//fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetCurrentPageCount() {
		assertEquals(2048, cache.GetCurrentPageCount());
	}

	@Test
	public void testClose() {
		//fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetCurrentDataSize() {
		assertEquals(16580608, cache.GetCurrentDataSize());
	}

	@Test
	public void testGetDataSize() {
		assertEquals(16580608, cache.GetDataCacheSize());
	}

	@Test
	public void testGetFileSize() throws IOException {
		assertEquals(275264, cache.GetFileSize());
	}

	@Test
	public void testGetPageCount() {
		assertEquals(2048, cache.GetPageCount());
	}

	@Test
	public void testInitializeFirstPage() throws IOException
	{
		String dummyFile = "/Users/dwhite/Dropbox/alma/test/dummy.fits.cache";
		File dummy = new File(dummyFile);
		dummy.delete();
		int dummyPageSize = 1000, dummyPageCount = 2;
		FloatDMCache dummyCache = new FloatDMCache( dummy.getAbsolutePath(), dummyPageSize, dummyPageCount, false, false );
		
		assertEquals(dummyPageSize * dummyPageCount, dummyCache.GetDataCacheSize());
		
		for (long i = 0; i < dummyCache.GetDataCacheSize() / 4; i++)
		{
			assertTrue(Float.isNaN(dummyCache.getValue(i)));
		}
		
		dummyCache.initializeFirstPage();
		
		for (long i = 0; i < dummyPageSize / 4; i++)
		{
			assertEquals(-1, dummyCache.getValue(i), 1e-6);
		}
		for (long i = dummyPageSize / 4; i < dummyCache.GetDataCacheSize() / 4; i++)
		{
			assertTrue(Float.isNaN(dummyCache.getValue(i)));
		}
	}
}
