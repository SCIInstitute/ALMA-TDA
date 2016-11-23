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
	public void testGet() throws IOException {
		
		long numFloats = cache.GetFileSize() / 4;
		assertEquals(68816, numFloats);
		
		long indexOfFirstNanInFile = -1;
		for (long i = 0; i < cache.GetDataSize(); i++)
		{
			indexOfFirstNanInFile++;
			if (Float.isNaN(cache.get(i)))
				break;
		}
		assertEquals(67584, indexOfFirstNanInFile);
		
		long indexOfLastNumberInFile;
		for (indexOfLastNumberInFile = cache.GetDataSize() - 1; indexOfLastNumberInFile > 0; indexOfLastNumberInFile--)
		{
			if (!Float.isNaN(cache.get(indexOfLastNumberInFile)))
				break;
		}
		assertEquals(16579375, indexOfLastNumberInFile);
		
		assertTrue(Float.isNaN(cache.get(indexOfFirstNanInFile + 1)));

		float total = 0;
		int numNans = 0;
		int numNegativeOnes = 0;
		
		for (long i = 0; i < cache.GetDataSize(); i++)
		{
			float f = cache.get(i);
			if (Float.isNaN(f))
				numNans++;
			else if (f == -1)
				numNegativeOnes++;
			else
				total += f;
		}
		System.out.println("num nans = " + numNans + " out of " + cache.GetDataSize());
		System.out.println("num -1 = " + numNegativeOnes);
		System.out.println("total = " + total);
		assertEquals(16307104, numNans);
		assertEquals(8188, numNegativeOnes);
		assertEquals(1174.0561523, total, 1e-6);
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
