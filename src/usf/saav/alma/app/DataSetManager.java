/*
 *     ALMA TDA - Contour tree based simplification and visualization for ALMA
 *     data cubes.
 *     Copyright (C) 2016 PAUL ROSEN
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     You may contact the Paul Rosen at <prosen@usf.edu>.
 */
package usf.saav.alma.app;

import java.io.IOException;
import java.util.Vector;

import org.json.JSONException;

import nom.tam.fits.common.FitsException;
import usf.saav.alma.app.Settings.SettingsDouble;
import usf.saav.alma.app.Settings.SettingsInt;
import usf.saav.alma.app.views.BuildCacheProgressView;
import usf.saav.alma.data.fits.CachedFitsReader;
import usf.saav.alma.data.fits.FitsAdder;
import usf.saav.alma.data.fits.FitsReader;
import usf.saav.alma.data.fits.RawFitsReader;
import usf.saav.alma.data.fits.SafeFitsReader;
import usf.saav.alma.data.processors.Extended3D;
import usf.saav.alma.data.processors.Extract2DFrom3D;
import usf.saav.alma.data.processors.Subset2D;
import usf.saav.common.range.IntRange1D;
import usf.saav.common.range.FloatRange1D;
import usf.saav.scalarfield.ScalarField2D;
import usf.saav.scalarfield.ScalarField3D;

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

	
	public DataSetManager( BuildCacheProgressView progress, String filename, String filename2 ) throws IOException, FitsException {

		FitsReader fr0 = new SafeFitsReader( new CachedFitsReader( new RawFitsReader(filename, true), progress, true ), true );
		FitsReader fr1 = new SafeFitsReader( new CachedFitsReader( new RawFitsReader(filename2, true), progress, true ), true );

		reader.add( new FitsAdder(fr0,fr1) );
		data = reader.firstElement().getVolume(0);
		initSettings(filename);
	}

	public DataSetManager( BuildCacheProgressView progress, String filename ) throws IOException, FitsException {

		reader.add( new SafeFitsReader( new CachedFitsReader( new RawFitsReader(filename, true), progress, true ), true ) );
		data = reader.firstElement().getVolume(0);
		initSettings(filename);
	}

	public void rebuildCache( BuildCacheProgressView progress ){
		
		for( int i = 0; i < reader.size(); i++ ){
			String filename = reader.get(i).getFile().getAbsolutePath();
			reader.get(i).close();
			try {
				reader.set( i, new SafeFitsReader( new CachedFitsReader( new RawFitsReader(filename, true), progress, true, true ), true ) );
			} catch (IOException e){
				e.printStackTrace();
			} catch ( FitsException e ) {
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

	public void appendFile(BuildCacheProgressView progress, String filename) throws IOException, FitsException {
		reader.add( new SafeFitsReader( new CachedFitsReader( new RawFitsReader(filename, true), progress, true ), true ) );
		data = new Extended3D( data, reader.lastElement().getVolume(0) );
	}
	
	public ScalarField2D getSlice( IntRange1D xr, IntRange1D yr, int z ){
		return new Subset2D( new Extract2DFrom3D( data, z), xr, yr );
	}

	private int getXCenter()
	{
		int min = reader.firstElement().getAxesSize()[0].start();
		int max = reader.firstElement().getAxesSize()[0].end();
		return (min+max)/2;
	}

	private int getYCenter()
	{
		int min = reader.firstElement().getAxesSize()[1].start();
		int max = reader.firstElement().getAxesSize()[1].end();
		return (min+max)/2;
	}

	private void initSettings(String filename)
	{
		try {
			settings = new Settings( filename + ".snapshot" );
			x0 = settings.initInteger( "x0", getXCenter() );
			y0 = settings.initInteger( "y0", getYCenter() );
			z0 = settings.initInteger( "z0", 0 );
			z1 = settings.initInteger( "z1", 20 );
			curZ = settings.initInteger( "curZ", 0 );
			zoom = settings.initDouble( "zoom", 1 );

			x0.setValidRange( reader.firstElement().getAxesSize()[0] );
			y0.setValidRange( reader.firstElement().getAxesSize()[1] );
			z0.setValidRange( reader.firstElement().getAxesSize()[2] );
			z1.setValidRange( reader.firstElement().getAxesSize()[2] );
			curZ.setValidRange( reader.firstElement().getAxesSize()[2] );

			int maxScaleFactor = 128;
			int zoomX = reader.firstElement().getAxesSize()[0].end()/maxScaleFactor;
			int zoomY = reader.firstElement().getAxesSize()[1].end()/maxScaleFactor;
			// approximately limit zoom based on scalar field sizes
			float maxZoom = (float)( zoomX > zoomY ? zoomX : zoomY );
			zoom.setValidRange(new FloatRange1D(1e-02f, maxZoom));

		} catch (JSONException e ) {
			e.printStackTrace();
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
	}
}
