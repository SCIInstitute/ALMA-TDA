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

import java.io.File;

import processing.core.PApplet;

public class AlmaTDARelease extends AlmaTDADev {


	public AlmaTDARelease( ){ super(false); }


	public void setup( ){
		this.selectInput( "Select a file", "fileSelected",  null, this );
	}

	public void fileSelected(File selection) {
		if (selection == null) {
			println("Window was closed or the user hit cancel.");
			exit();
		} else {
			load( selection.getAbsolutePath() );
		}
	}



	public static void main(String args[]) {
		PApplet.main(new String[] { "usf.saav.alma.app.AlmaTDARelease" });
	}
}