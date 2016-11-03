package usf.saav.alma.app;

import java.io.IOException;
import java.util.Vector;

import org.json.JSONException;

import nom.tam.fits.FitsException;
import usf.saav.alma.data.ScalarField2D;
import usf.saav.alma.data.Settings;
import usf.saav.alma.data.Settings.SettingsDouble;
import usf.saav.alma.data.Settings.SettingsInt;
import usf.saav.alma.data.fits.CachedFitsReader;
import usf.saav.alma.data.fits.FitsReader;
import usf.saav.alma.data.fits.RawFitsReader;
import usf.saav.alma.data.fits.SafeFitsReader;
import usf.saav.common.range.IntRange1D;

public class DataSetManager {

	private Settings settings;
	public SettingsInt curZ;
	public SettingsInt x0;
	public SettingsInt y0;
	public SettingsInt z0;
	public SettingsInt z1; 
	public SettingsDouble zoom;
	
	Vector<FitsReader> reader = new Vector<FitsReader>( ); 
	
	public DataSetManager( String filename ) throws IOException, FitsException {

		reader.add( new SafeFitsReader( new CachedFitsReader( new RawFitsReader(filename, true), true ), true ) );
	
		try {
			settings = new Settings( filename + ".snapshot" );
			x0 = settings.initInteger( "x0", 0 );
			y0 = settings.initInteger( "y0", 0 );
			z0 = settings.initInteger( "z0", 0 );
			z1 = settings.initInteger( "z1", 20 );
			curZ = settings.initInteger( "curZ", 0 );
			zoom = settings.initDouble( "zoom", 1 );
			
			x0.setValidRange( reader.firstElement().getAxesSize()[0] );
			y0.setValidRange( reader.firstElement().getAxesSize()[1] );
			z0.setValidRange( reader.firstElement().getAxesSize()[2] );
			z1.setValidRange( reader.firstElement().getAxesSize()[2] );
			curZ.setValidRange( reader.firstElement().getAxesSize()[2] );
			
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	public ScalarField2D getSlice(IntRange1D xr, IntRange1D yr, int z, int w) {
		try {
			for( int vol = 0; vol < reader.size(); vol++){
				System.out.println(reader.get(vol).getAxesSize()[2].toString());
				if( z < reader.get(vol).getAxesSize()[2].end() ){
					return reader.get(vol).getSlice( xr, yr, z, w);
				}
				z -= reader.get(vol).getAxesSize()[2].length();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void appendFile(String filename) throws IOException, FitsException {
		reader.add( new SafeFitsReader( new CachedFitsReader( new RawFitsReader(filename, true), true ), true ) );
	}

	
}
