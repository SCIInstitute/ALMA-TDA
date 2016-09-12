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
import usf.saav.alma.app.AlmaGui.ViewMode;
import usf.saav.common.mvc.PositionedComponent;
import usf.saav.common.mvc.ViewComponent;

public class AlmaView extends ViewComponent.Subview implements ViewComponent, PositionedComponent {
	
	private AlmaTDADev mvc;

	public AlmaView( AlmaTDADev mvc ){ 	
		this.mvc = mvc;
	}
	
	public void setup() {
		
		registerSubView( mvc.model.ssfv.getView(), 10 );
		registerSubView( mvc.model.vrv.getView(),  10 );
		registerSubView( mvc.model.gui,    		   50 );
		
		mvc.model.gui.monView.addMonitor( this, "setViewMode" );

		super.setup();
	}
	
	@Override
	public void setPosition( int u0, int v0, int w, int h ){
		super.setPosition(u0, v0, w, h);
		
		mvc.model.ssfv.getView().setPosition( winX.start(), winY.start(), winX.length()-150, winY.length()   );
		mvc.model.vrv.getView().setPosition(  winX.start(), winY.start(), winX.length()-150, winY.length()   );
		
		mvc.model.gui.setPosition( winX.end()-150,  winY.start(),    150,   winY.length()   );

	}
	
	public void setViewMode( ViewMode vm ){
		mvc.model.vrv.disable();
		mvc.model.ssfv.disable();
		
		switch(vm){
		case MOMENT0: 
		case MOMENT1:
		case MOMENT2:
		case SCALARFIELD:
			mvc.model.ssfv.enable();
			break;
		case VOLUME:
			mvc.model.vrv.enable();
			break;
		default:
			break;
		}
	}
	
	public static void main(String args[]) {
		PApplet.main(new String[] { "usf.saav.alma.app.AlmaTDADev" });
	}

}
