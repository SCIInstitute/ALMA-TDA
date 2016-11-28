package usf.saav.alma.app;

import java.io.IOException;
import java.util.Vector;

import org.json.JSONException;

import nom.tam.fits.common.FitsException;
import usf.saav.alma.data.ScalarField2D;
import usf.saav.alma.data.ScalarField3D;
import usf.saav.alma.data.Settings;
import usf.saav.alma.data.Settings.SettingsDouble;
import usf.saav.alma.data.Settings.SettingsInt;
import usf.saav.alma.data.fits.CachedFitsReader;
import usf.saav.alma.data.fits.FitsReader;
import usf.saav.alma.data.fits.RawFitsReader;
import usf.saav.alma.data.fits.SafeFitsReader;
import usf.saav.alma.data.processors.Extended3D;
import usf.saav.alma.data.processors.Extract2DFrom3D;
import usf.saav.alma.data.processors.Subset2D;
import usf.saav.common.range.IntRange1D;

public class DataSetManager {

	private Settings settings;
	public SettingsInt curZ;
	public SettingsInt x0;
	public SettingsInt y0;
	public SettingsInt z0;
	public SettingsInt z1; 
	public SettingsDouble zoom;
	
	public Vector<FitsReader> reader = new Vector<FitsReader>( );
	public ScalarField3D data;

	
	public DataSetManager( String filename ) throws IOException, FitsException {

		reader.add( new SafeFitsReader( new CachedFitsReader( new RawFitsReader(filename, true), true ), true ) );
		data = reader.firstElement().getVolume(0);
	
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
	
	public void rebuildCache( ){
		
		for( int i = 0; i < reader.size(); i++ ){
			String filename = reader.get(i).getFile().getAbsolutePath();
			reader.get(i).close();
			try {
				reader.set( i, new SafeFitsReader( new CachedFitsReader( new RawFitsReader(filename, true), true, true ), true ) );
			} catch (IOException | FitsException e) {
				e.printStackTrace();
			}
		}
		try {
			data = reader.firstElement().getVolume(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public void appendFile(String filename) throws IOException, FitsException {
		reader.add( new SafeFitsReader( new CachedFitsReader( new RawFitsReader(filename, true), true ), true ) );
		data = new Extended3D( data, reader.lastElement().getVolume(0) );
	}
	
	public ScalarField2D getSlice( IntRange1D xr, IntRange1D yr, int z ){
		return new Subset2D( new Extract2DFrom3D( data, z), xr, yr );
	}

	
}
