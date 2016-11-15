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
		
//		for (int i = 0; i < numFloats; i++)
//		{
//			System.out.print(cache.get(i) + " ");
//		}
		
		long indexOfFirstNanInFile = -1;
		for (long i = 0; i < cache.GetDataSize(); i++)
		{
			indexOfFirstNanInFile++;
			//System.out.print(i + " ");
			if (Float.isNaN(cache.get(i)))
				break;
			//System.out.print(cache.get(i) + " ");
//			if (i % 100 == 0)
//				System.out.println();
		}
		assertEquals(67584, indexOfFirstNanInFile);
		
		long indexOfLastNumberInFile;
		for (indexOfLastNumberInFile = cache.GetDataSize() - 1; indexOfLastNumberInFile > 0; indexOfLastNumberInFile--)
		{
			
			
			if (!Float.isNaN(cache.get(indexOfLastNumberInFile)))
				break;
		}
		assertEquals(16579375, indexOfLastNumberInFile);
		
//		for (int i = 0; i < 10; ++i)
//		{
//			System.out.println("Page " + i);
//			for (int j = 0; j < page_size_bytes; ++j)
//			{
//				//float x = cache.get(j);
//				System.out.print(cache.get(i * page_size_elems + j) + " ");
//			}
//			System.out.println();
//		}
		
		assertTrue(Float.isNaN(cache.get(indexOfFirstNanInFile + 1)));

		for (long i = indexOfFirstNanInFile + 1; i < 68816+100 ; i++)
		{
			float x = cache.get(i);
			System.out.println(i + ": " + x);
			//assertTrue(Float.isNaN(x) || x == -1);
			//if (!(Float.isNaN(x) || x == -1))
				
		}
		
		
		for (long i = 0; i < cache.GetPageCount(); i++)
		{
			System.out.print("Page: " + i);
			int numNans = 0;
			for (long j = 0; j < page_size_elems; ++j)
			{
				if (Float.isNaN(cache.get(j + i * page_size_elems)))
					numNans++;
				
			}
			System.out.println(" has " + numNans + " nans");
		}
		
		//fail("Not yet implemented"); // TODO
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
