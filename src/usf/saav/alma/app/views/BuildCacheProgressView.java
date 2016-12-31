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
package usf.saav.alma.app.views;

import javax.swing.JFrame;

public class BuildCacheProgressView extends ProgressView {

	private static final long serialVersionUID = -6779001405431020762L;

	private BuildCacheProgressView( ){ }
	
	
    private static JFrame frame = null;
    private static BuildCacheProgressView gui = null;

    public static void createGUI() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

		        gui = new BuildCacheProgressView( );
		        gui.setOpaque(true); //content panes must be opaque
		        
		        frame = new JFrame("Building Data File Cache");
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        frame.setContentPane(gui);
		        frame.pack();
		        
            }
        });
    }

    public static BuildCacheProgressView getGUI() {
		return gui;
    }
    
    public static void hideGUI( ){
    	frame.setVisible(false);
    }

    public static void showGUI( ){
    	frame.setVisible(true);
    }

    public static void resetGUI( ){
    	gui.reset();
    }

}

