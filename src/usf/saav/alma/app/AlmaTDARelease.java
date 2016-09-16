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

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import usf.saav.common.mvc.swing.TApp;

public class AlmaTDARelease extends AlmaTDADev {

	private static final long serialVersionUID = -226739546547617965L;


	public AlmaTDARelease( String title, int x, int y, int w, int h ){ super(title,x,y,w,h,false); }

	public void setup( ){
		this.selectInput( "Select a file", "fileSelected",  null, this );
	}

	private void selectInput(String string, String string2, Object object, AlmaTDARelease almaTDARelease) {
		// TODO Auto-generated method stub
	}


	public void fileSelected(File selection) {
		if (selection == null) {
			System.err.println("Window was closed or the user hit cancel.");
			System.exit(0);
		} else {
			load( selection.getAbsolutePath() );
		}
	}


	public JInternalFrame createGLFrame() {
		/*
		TGLFrame frame = new AlmaTDADev( "GL Window", 0, 0, 600, 600 );
		frame.putClientProperty("dragMode", "fixed");
	    frame.setVisible(true);
	    return frame;
	    */
		return null;
	}


	public static void main(String args[]) {
		//TestFrame frame = new TestFrame();
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {
            	TApp frame = new TApp( 5, 5, 800, 800 );

            	//frame.addFrame( createGLFrame() );
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
	}
	
}