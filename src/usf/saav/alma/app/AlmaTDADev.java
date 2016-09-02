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

	/*

	AlmaController controller;
	AlmaModel model;
	AlmaView view;
	*/
	
	joclController jocl;
	
	//boolean devMode = false;

	public AlmaTDADev( ){ this(true); }
	public AlmaTDADev( boolean verbose ){ 
		super(verbose); 
		jocl = new joclController( );
	}
	

	/*

	public void setup() {
		
		devMode = true;

		String filename = "/Users/prosen/Code/alma/data/anil_seth/NGC404_CO21_briggs.pbcor.fits";
		//String filename = "/Users/prosen/Code/alma/data/Continuum_33GHz.fits";
		//String filename = "/Users/prosen/Code/alma/data/betsy/CH3OH_7m+12m_natural.feather.fits";
		//String filename = "/Users/prosen/Code/alma/data/betsy/HC3N_7m+12m_natural.feather.fits";
		//String filename = "/Users/prosen/Code/alma/data/betsy/HCN_7m+12m_natural.feather.fits";
		//String filename = "/Users/prosen/Code/alma/data/betsy/HCOp_7m+12m_natural.feather.fits";
		//String filename = "/Users/prosen/Code/alma/data/betsy/SO_7m+12m_natural.feather.fits";
		
		load(filename);
		
		textFont(createFont("Arial", 32,true));

	}
	*/
	
	@Override
	public void init( ){
		String filename = "/Users/prosen/Code/alma/data/anil_seth/NGC404_CO21_briggs.pbcor.fits";
		//String filename = "/Users/prosen/Code/alma/data/anil_seth/NGC404_CO21_uniform.pbcor.fits";
		//String filename = "\\\\saav.cspaul.com\\projects\\ALMA\\data\\anil_seth\\NGC404_CO21_briggs.pbcor.fits";
		//String filename = "\\\\saav.cspaul.com\\projects\\ALMA\\data\\anil_seth\\NGC404_CO21_uniform.pbcor.fits";
		//String filename = "/Users/prosen/Code/alma/data/Continuum_33GHz.fits";
		//String filename = "/Users/prosen/Code/alma/data/betsy/CH3OH_7m+12m_natural.feather.fits";
		//String filename = "/Users/prosen/Code/alma/data/betsy/HC3N_7m+12m_natural.feather.fits";
		//String filename = "/Users/prosen/Code/alma/data/betsy/HCN_7m+12m_natural.feather.fits";
		//String filename = "/Users/prosen/Code/alma/data/betsy/HCOp_7m+12m_natural.feather.fits";
		//String filename = "/Users/prosen/Code/alma/data/betsy/SO_7m+12m_natural.feather.fits";
		
		load( filename );
		
	}
	
	public void load( String filename ){
		
		model = new AlmaModel(this,filename);		
		controller = new AlmaController(this, model, verbose);
		view = new AlmaView(this);
		
		textFont(createFont("Arial", 32,true));
		
	}


	/*
	public void load( String filename ){
		
		model = new AlmaModel(this,filename);		
		controller = new AlmaController(this,devMode);
		view = new AlmaView(this);
		
		model.setup();
		controller.setup();
		view.setup();
		
		view.setPosition(0, 0, width, height);
		
	}
	*/
	
	@Override
	public void settings() {
		size(1200, 800, P3D);
	}

	/*
	public void draw() {
		background(255);

		if( model == null || controller == null || view == null ) return;
		
		controller.update();
		model.update();
		view.update();
		
		view.draw( g );
		view.drawScale( g );
		
		if( devMode ){
			if( frameRate < 10 && (frameCount%20) == 0 )
				PApplet.println("[AlmaTDA] Frame rate: " + frameRate);
			else if( frameRate < 20 && (frameCount%40) == 0 )
				PApplet.println("[AlmaTDA] Frame rate: " + frameRate);
			else if( (frameCount%100) == 0 )
				PApplet.println("[AlmaTDA] Frame rate: " + frameRate);
		}
	}

	@Override public void mouseMoved() {    if( model == null ) return; controller.mouseMoved(mouseX,mouseY); }
	@Override public void keyPressed() {    if( model == null ) return; controller.keyPressed( key ); }
	@Override public void mouseDragged() {  if( model == null ) return; controller.mouseDragged(mouseX,mouseY); }
	@Override public void mousePressed() {  if( model == null ) return; controller.mousePressed(mouseX,mouseY); }
	@Override public void mouseReleased() { if( model == null ) return; controller.mouseReleased(); }
	@Override public void mouseWheel(MouseEvent event) { if( model == null ) return; controller.mouseWheel( mouseX, mouseY, event.getCount() ); }

	*/
	
	
	 
	
	public static void main(String args[]) {
		PApplet.main(new String[] { "usf.saav.alma.app.AlmaTDADev" });
	}
	
}
