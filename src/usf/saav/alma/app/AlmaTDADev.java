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

import processing.core.PApplet;
import usf.saav.common.jocl.joclController;
import usf.saav.common.mvc.DefaultApp;

public class AlmaTDADev extends DefaultApp<AlmaModel, AlmaView, AlmaController> {
	
	joclController jocl;

	public AlmaTDADev( ){ this(true); }
	public AlmaTDADev( boolean verbose ){ 
		super(verbose); 
		jocl = new joclController( );
	}

	
	@Override
	public void init( ){
		String filename = "/Users/prosen/Code/alma/data/anil_seth/NGC404_CO21_briggs.pbcor.fits";
		load( filename );
	}
	
	public void load( String filename ){
		
		model = new AlmaModel(this,filename);		
		controller = new AlmaController(this, model, verbose);
		view = new AlmaView(this);
		
		textFont(createFont("Arial", 32,true));
		
	}
	
	@Override
	public void settings() {
		size(1200, 800, P3D);
	}
	
	public static void main(String args[]) {
		PApplet.main(new String[] { "usf.saav.alma.app.AlmaTDADev" });
	}
	
}
