package usf.saav.alma.app;

import java.io.IOException;

import org.json.JSONException;

import nom.tam.fits.common.FitsException;
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
	
	FitsReader reader; 
	
	public DataSetManager( String filename ) throws FitsException, IOException {

		reader = new SafeFitsReader( new CachedFitsReader( new RawFitsReader(filename, true), true ), true );
	
		try {
			settings = new Settings( filename + ".snapshot" );
			x0 = settings.initInteger( "x0", 0 );
			y0 = settings.initInteger( "y0", 0 );
			z0 = settings.initInteger( "z0", 0 );
			z1 = settings.initInteger( "z1", 20 );
			curZ = settings.initInteger( "curZ", 0 );
			zoom = settings.initDouble( "zoom", 1 );
			
			x0.setValidRange( reader.getAxesSize()[0] );
			y0.setValidRange( reader.getAxesSize()[1] );
			z0.setValidRange( reader.getAxesSize()[2] );
			z1.setValidRange( reader.getAxesSize()[2] );
			curZ.setValidRange( reader.getAxesSize()[2] );
			
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	public ScalarField2D getSlice(IntRange1D xr, IntRange1D yr, int z, int w) {
		try {
			return reader.getSlice( xr, yr, z, w);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
