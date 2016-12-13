package usf.saav.alma.data.fits;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import nom.tam.fits.common.FitsException;
import usf.saav.alma.data.ScalarField1D;
import usf.saav.alma.data.ScalarField2D;
import usf.saav.alma.data.ScalarField3D;
import usf.saav.common.range.IntRange1D;

public class CachedFitsReaderTest {

	private static CachedFitsReader reader;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		reader = new CachedFitsReader( new RawFitsReader("/Users/dwhite/Dropbox/alma/fits1/Continuum_33GHz.fits", true), true );
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testCachedFitsReader() throws Exception {
		new CachedFitsReader( new RawFitsReader("/Users/dwhite/Dropbox/alma/fits1/Continuum_33GHz.fits", true), true );
		new CachedFitsReader( new RawFitsReader("/Users/dwhite/Dropbox/alma/fits2/Continuum_33GHz.fits", true), true );
		try
		{ 
			new CachedFitsReader( new RawFitsReader("/Users/dwhite/Dropbox/alma/test/dummy.fits", true), true );
			fail("Ctor should throw for non-existent file");
		}
		catch(FitsException e)
		{
		}
	}

	@Test
	public void testGetFile() {
		assertEquals("Continuum_33GHz.fits", reader.getFile().getName());
	}

	@Test
	public void testGetAxesSize() {
		
		IntRange1D[] axesSize = reader.getAxesSize();
		assertEquals(4, axesSize.length);
		
		IntRange1D axis0 = axesSize[0];
		assertEquals(0, axis0.start());
		assertEquals(255, axis0.end());
		
		IntRange1D axis1 = axesSize[1];
		assertEquals(0, axis1.start());
		assertEquals(255, axis1.end());
		
		IntRange1D axis2 = axesSize[2];
		assertEquals(0, axis2.start());
		assertEquals(0, axis2.end());
		
		IntRange1D axis3 = axesSize[3];
		assertEquals(0, axis3.start());
		assertEquals(0, axis3.end());
	}

	@Test
	public void testGetElement() {
		assertEquals(reader.getElement(250, 30, 0, 0), 0.005653, 1e-6);
		assertEquals(reader.getElement(3, 159, 0, 0), -0.001908, 1e-6);
	}

	@Test
	public void testGetRow() throws IOException {
		
		ScalarField1D row2 = reader.getRow(2, 0, 0);
		assertEquals(256, row2.getWidth());
		assertEquals(256, row2.getSize());
		
		assertEquals(reader.getRow(30, 0, 0).getValue(250), 0.005653, 1e-6);
		assertEquals(reader.getRow(159, 0, 0).getValue(3), -0.001908, 1e-6);
	}

	@Test
	public void testGetColumn() throws IOException {
		
		ScalarField1D col2 = reader.getColumn(2, 0, 0);
		assertEquals(256, col2.getWidth());
		assertEquals(256, col2.getSize());
		
		assertEquals(reader.getColumn(250, 0, 0).getValue(30), 0.005653, 1e-6);
		assertEquals(reader.getColumn(3, 0, 0).getValue(159), -0.001908, 1e-6);
	}

	@Test
	public void testGetLine() throws IOException {
		ScalarField1D line = reader.getLine(250, 30, 0);
		assertEquals(1, line.getWidth());
		assertEquals(1, line.getSize());
		
		assertEquals(line.getValue(0), 0.005653, 1e-6);
	}

	@Test
	public void testGetSlice() throws IOException {
		
		ScalarField2D fullSlice = reader.getSlice(0, 0);
		assertTrue(fullSlice instanceof CachedFitsReader.CachedSlice);
		assertEquals(256, fullSlice.getWidth());
		assertEquals(256, fullSlice.getHeight());
		assertEquals(256*256, fullSlice.getSize());
		assertEquals(fullSlice.getValue(250, 30), 0.005653, 1e-6);
		assertEquals(fullSlice.getValue(3, 159), -0.001908, 1e-6);
		
		reader.getSlice(1, 0);
	}

	@Test
	public void testGetVolume() throws IOException {
		
		ScalarField3D fullVolume = reader.getVolume(0);
		assertTrue(fullVolume instanceof CachedFitsReader.CachedVolume);
		assertEquals(256, fullVolume.getWidth());
		assertEquals(256, fullVolume.getHeight());
		assertEquals(1, fullVolume.getDepth());
		assertEquals(256*256*1, fullVolume.getSize());
	}

}
