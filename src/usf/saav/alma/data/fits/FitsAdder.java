package usf.saav.alma.data.fits;

import java.io.File;
import java.io.IOException;

import usf.saav.common.range.IntRange1D;
import usf.saav.scalarfield.ScalarField2D;
import usf.saav.scalarfield.ScalarField3D;

public class FitsAdder implements FitsReader {

	FitsReader r0,r1;
	
	public FitsAdder( FitsReader r0, FitsReader r1 ){
		this.r0 = r0;
		this.r1 = r1;
	}
	
	@Override
	public File getFile() {
		return r0.getFile();
	}

	@Override
	public void close() {
		r0.close();
		r1.close();
	}

	@Override
	public IntRange1D[] getAxesSize() {
		return r0.getAxesSize();
	}

	@Override
	public FitsHistory getHistory() {
		return r0.getHistory();
	}

	@Override
	public FitsProperties getProperties() {
		return r0.getProperties();
	}

	@Override
	public FitsTable getTable() {
		return r0.getTable();
	}

	@Override
	public ScalarField2D getSlice(int z, int w) throws IOException {
		return new Slice(z,w);
	}

	@Override
	public ScalarField2D getSlice(IntRange1D x_range, IntRange1D y_range, int z, int w) throws IOException {
		return new Slice(x_range,y_range,z,w);
	}

	@Override
	public ScalarField3D getVolume(int w) throws IOException {
		return new Volume(w);
	}

	@Override
	public ScalarField3D getVolume(IntRange1D x_range, IntRange1D y_range, IntRange1D z_range, int w) throws IOException {
		return new Volume(x_range,y_range,z_range,w);
	}
	
	class Slice extends ScalarField2D.Default {

		ScalarField2D b0, b1;

		Slice(int z, int w) throws IOException {
			b0 = r0.getSlice(z,w);
			b1 = r1.getSlice(z,w);
		}

		Slice(IntRange1D x_range, IntRange1D y_range, int z, int w) throws IOException {
			b0 = r0.getSlice(x_range, y_range,z,w);
			b1 = r1.getSlice(x_range, y_range,z,w);
		}
		
		@Override public int getWidth() {  return b0.getWidth(); }
		@Override public int getHeight() { return b0.getHeight(); }

		@Override
		public float getValue(int x, int y) {
			return b0.getValue(x,y) + b1.getValue(x,y);
		}
		
	}
	
	
	class Volume extends ScalarField3D.Default {
		
		ScalarField3D b0, b1;


		Volume( int w ) throws IOException{
			b0 = r0.getVolume(w);
			b1 = r1.getVolume(w);
		}
		
		Volume( IntRange1D x_range, IntRange1D y_range, IntRange1D z_range, int w ) throws IOException{
			b0 = r0.getVolume(x_range, y_range,z_range,w);
			b1 = r1.getVolume(x_range, y_range,z_range,w);
		}

		
		@Override public int getWidth() {  return b0.getWidth(); }
		@Override public int getHeight() { return b0.getHeight(); }
		@Override public int getDepth() { return b0.getDepth(); }

		@Override
		public float getValue(int x, int y, int z) {
			return b0.getValue(x,y,z) + b1.getValue(x,y,z);
		}
	}

}
