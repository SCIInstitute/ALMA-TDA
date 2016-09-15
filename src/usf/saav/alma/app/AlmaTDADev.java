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

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import usf.saav.alma.app.views.SingleScalarFieldView;
import usf.saav.alma.app.views.VolumeRenderingView;
import usf.saav.common.jocl.joclController;
import usf.saav.common.mvc.swing.TApp;
import usf.saav.common.mvc.swing.TGLFrame;

public class AlmaTDADev extends TApp {

	private static final long serialVersionUID = 8573376435979745456L;

	private joclController jocl;
	private DataManager    dataM;

	
	public AlmaTDADev( String title, int x, int y, int w, int h ){ 
		this(title, x,y,w,h, true);
	}
	
	public AlmaTDADev( String title, int x, int y, int w, int h, boolean verbose ){ 
		super(x,y,w,h); 
		jocl = new joclController( );
		init();
	}
	


	
	//@Override
	public void init( ){
		//String filename = "/Users/prosen/Code/alma/data/anil_seth/NGC404_CO21_briggs.pbcor.fits";
		//String filename = "/Users/prosen/Code/alma/data/anil_seth/NGC404_CO21_uniform.pbcor.fits";
		//String filename = "\\\\saav.cspaul.com\\projects\\ALMA\\data\\anil_seth\\NGC404_CO21_briggs.pbcor.fits";
		//String filename = "\\\\saav.cspaul.com\\projects\\ALMA\\data\\anil_seth\\NGC404_CO21_uniform.pbcor.fits";
		//String filename = "/Users/prosen/Code/alma/data/Continuum_33GHz.fits";
		//String filename = "/Users/prosen/Code/alma/data/betsy/CH3OH_7m+12m_natural.feather.fits";
		//String filename = "/Users/prosen/Code/alma/data/betsy/HC3N_7m+12m_natural.feather.fits";
		String filename = "/Users/prosen/Code/alma/data/betsy/HCN_7m+12m_natural.feather.fits";
		//String filename = "/Users/prosen/Code/alma/data/betsy/HCOp_7m+12m_natural.feather.fits";
		//String filename = "/Users/prosen/Code/alma/data/betsy/SO_7m+12m_natural.feather.fits";
		
		load( filename );
		
	}
	
	
	public void load( String filename ){
		
		dataM = new DataManager( filename );
		//view = new AlmaView(this);
		
		AlmaGui gui = createGUIFrame( );
		JInternalFrame ssfv = createGLFrame( gui );
		JInternalFrame vol = createVolFrame( );
		addFrame( gui );
    	addFrame( ssfv );
    	addFrame( vol );
    	

	}

	
	
	 
	public JInternalFrame createGLFrame(AlmaGui gui) {
		TGLFrame frame = new SingleScalarFieldView( dataM, gui, "GL Window", 0, 0, 1000, 700 );
		//frame.putClientProperty("dragMode", "fixed");
	    frame.setVisible(true);
	    return frame;
	}
	
	public JInternalFrame createVolFrame( ) {
		TGLFrame frame = new VolumeRenderingView( dataM, jocl, "GL Window", 0, 0, 1000, 700 );
		//frame.putClientProperty("dragMode", "fixed");
	    frame.setVisible(true);
	    return frame;
	}
	
	public AlmaGui createGUIFrame() {
		AlmaGui frame = new AlmaGui( 1000, 0, dataM.curZ, dataM.z0, dataM.z1 );
		//frame.putClientProperty("dragMode", "fixed");
	    frame.setVisible(true);
	    return frame;
	}
	
	
	public static void main(String args[]) {
		//TestFrame frame = new TestFrame();
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {
            	TApp frame = new AlmaTDADev( "ALMA TDA", 5, 5, 1200, 800, true );

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
	}
	
	
}
