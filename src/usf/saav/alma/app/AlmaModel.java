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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;

import nom.tam.fits.common.FitsException;
import processing.core.PApplet;
import usf.saav.alma.algorithm.topology.PersistenceSet;
import usf.saav.alma.app.views.SingleScalarFieldView;
import usf.saav.alma.app.views.VolumeRenderingView;
import usf.saav.alma.data.ScalarField2D;
import usf.saav.alma.data.ScalarField3D;
import usf.saav.alma.data.Settings;
import usf.saav.alma.data.Settings.SettingsDouble;
import usf.saav.alma.data.Settings.SettingsInt;
import usf.saav.alma.data.fits.CachedFitsReader;
import usf.saav.alma.data.fits.FitsReader;
import usf.saav.alma.data.fits.RawFitsReader;
import usf.saav.alma.data.fits.SafeFitsReader;
import usf.saav.alma.data.processors.LayeredVolume;
import usf.saav.alma.data.processors.PersistenceSimplifierND;
import usf.saav.alma.drawing.SelectBoxDrawing;
import usf.saav.alma.util.ContourTreeThread;
import usf.saav.alma.util.CoordinateSystemController;
import usf.saav.common.monitor.MonitoredObject;
import usf.saav.common.mvc.ModelComponent;

public class AlmaModel extends ModelComponent.Default implements ModelComponent {

	private Settings settings;
	public SettingsInt curZ;
	public SettingsInt x0;
	public SettingsInt y0;
	public SettingsInt z0;
	public SettingsInt z1; 
	public SettingsDouble zoom;
	
	
	public MonitoredObject<ScalarField2D> src_sf2d  = new MonitoredObject<ScalarField2D>( ){
		@Override protected Class<?> getClassType() { return ScalarField2D.class; } 
	};

	public MonitoredObject<LayeredVolume> src_sf3d  = new MonitoredObject<LayeredVolume>( ){
		@Override protected Class<?> getClassType() { return LayeredVolume.class; } 
	};

	public MonitoredObject<ScalarField2D> simp_sf2d  = new MonitoredObject<ScalarField2D>( ){
		@Override protected Class<?> getClassType() { return ScalarField2D.class; } 
	};

	public MonitoredObject<ScalarField3D> simp_sf3d  = new MonitoredObject<ScalarField3D>( ){
		@Override protected Class<?> getClassType() { return ScalarField3D.class; } 
	};
	
	public Map< Integer, PersistenceSimplifierND > psf_map = new HashMap< Integer, PersistenceSimplifierND >( );
	public Map< Integer, ContourTreeThread >       ctt_map = new HashMap< Integer, ContourTreeThread >( );

	public MonitoredObject< ContourTreeThread > cur_ctt = new MonitoredObject< ContourTreeThread >( ){
		@Override protected Class<?> getClassType() { return ContourTreeThread.class; } 
	};

	public MonitoredObject< Set<PersistenceSet> > pss = new MonitoredObject< Set<PersistenceSet> >( ){
		@Override protected Class<?> getClassType() { return Set.class; } 
	};
	
	FitsReader reader; 

	public AlmaGui gui;
	public SingleScalarFieldView ssfv;
	public VolumeRenderingView   vrv;
	
	public CoordinateSystemController csCont;
	public SelectBoxDrawing sel;
	

	public AlmaModel( AlmaTDADev mvc, String filename ){

		try {
			reader = new SafeFitsReader( new CachedFitsReader( new RawFitsReader(filename, true), true ), true );
		} catch (FitsException | IOException e) {
			e.printStackTrace();
		}

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
		
		this.csCont = new CoordinateSystemController( x0, y0, zoom );
		
		this.gui  = new AlmaGui(mvc, curZ, z0, z1 );
		this.ssfv = new SingleScalarFieldView( mvc, mvc, this );
		this.vrv  = new VolumeRenderingView( mvc, this, mvc.jocl );
		this.vrv.disable( );
		this.sel  = new SelectBoxDrawing( );

	}
	
	public static void main(String args[]) {
		PApplet.main(new String[] { "usf.saav.alma.app.AlmaTDADev" });
	}
}
