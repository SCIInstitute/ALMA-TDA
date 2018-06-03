package usf.saav.alma.app;

import java.io.IOException;
import java.io.PrintWriter;

import nom.tam.fits.common.FitsException;
import usf.saav.alma.data.fits.RawFitsReader;
import usf.saav.alma.data.fits.SafeFitsReader;
import usf.saav.scalarfield.ScalarField2D;

public class Extract1D {

	
	private SafeFitsReader fits;

	public Extract1D( String filename ) throws IOException, FitsException {
		String dir = "/Users/prosen/Code/chris_s/topoViz/data/radioAstronomy/";
		fits = new SafeFitsReader( new RawFitsReader(filename, true), true );
		
		for( int y = 115; y < 140; y++ ) {
			for( int x = 115; x < 140; x++ ) {
				PrintWriter pw = new PrintWriter( dir + "output_"+y+"_"+x+".json");
				pw.println("{");
				pw.println("\"results\": [" );
				for( int z = 0; z < 1947; z++ ) {
					ScalarField2D slice = fits.getSlice(z, 0);
					float v = slice.getValue( x, y);
					//System.out.print( );
					//System.out.print( " " );
					
					pw.println("{ \"id\": \""+z+"\", \"value\": " + v + "},");
				}
				pw.println("]}");
				pw.close();
			}
		}
		
	}
	
	public static void main( String []args ) {
		
		try {
			new Extract1D("/Volumes/data/NRAO/LineData_36GHz.fits");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FitsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}


