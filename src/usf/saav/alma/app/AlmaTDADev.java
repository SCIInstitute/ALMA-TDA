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

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import nom.tam.fits.common.FitsException;
import usf.saav.alma.app.views.SingleScalarFieldView;
import usf.saav.alma.app.views.VolumeRenderingView;
import usf.saav.common.jocl.joclController;
import usf.saav.common.mvc.swing.TApp;
import usf.saav.common.mvc.swing.TGLFrame;

public class AlmaTDADev extends TApp {

	private static final long serialVersionUID = 8573376435979745456L;

	private joclController  jocl;
	
	private DataViewManager dataVM;
	private DataSetManager  dataSM;

	private AlmaStartupMenu  menuStartup;
	private AlmaStandardMenu menuStandard;

	private JInternalFrame guiFrame;
	private TGLFrame	   slcFrame;
	private TGLFrame	   volFrame;

	
	public AlmaTDADev( int x, int y, int w, int h ){ 
		this( x, y, w, h, true );
	}
	
	public AlmaTDADev( int x, int y, int w, int h, boolean verbose ){ 
		super( x, y, w, h ); 
		
		jocl = new joclController( ); 
		
		menuStartup = new AlmaStartupMenu( );
		menuStartup.monFileOpen.addMonitor(  this, "fileOpen" );
		
		menuStandard = new AlmaStandardMenu( );
		menuStandard.monFileOpen.addMonitor(  this, "fileOpen" );
		menuStandard.monFileClose.addMonitor( this, "fileClose" );
		
		this.setJMenuBar( menuStartup );
		
		this.setTitle( "ALMA TDA" );

	}

	
	public void fileOpen( ){
		
		final JFileChooser fc = new JFileChooser();

		switch( fc.showOpenDialog(this) ){
			case JFileChooser.APPROVE_OPTION: 
				closeFile(); 
				openFile( fc.getSelectedFile().getAbsolutePath() ); 
				break;
			case JFileChooser.CANCEL_OPTION:  
				return;
			case JFileChooser.ERROR_OPTION:   
				System.err.println( "JFileChooser error" );
				return;
		};
		
	}

	public void fileClose( ){
		closeFile( );
	}
	
	public void closeFile( ){
		if( guiFrame != null ) guiFrame.hide();
		if( slcFrame != null ) slcFrame.hide();
		if( volFrame != null ) volFrame.hide();
		
		dataSM = null;
		dataVM = null;
		
		this.setTitle( "ALMA TDA" );
		this.setJMenuBar( menuStartup );
	}

	public void openFile( String filename ){
		
		try {
			dataSM = new DataSetManager( filename );
		} catch (FitsException | IOException e) {
			System.err.println("Unable to read file");
			return;
		}
		dataVM = new DataViewManager(dataSM );
		
		if( guiFrame == null ) addFrame( guiFrame = new AlmaGui( 1000, 0, dataVM.curZ, dataVM.z0, dataVM.z1 ) );
    	if( slcFrame == null ) addFrame( slcFrame = new SingleScalarFieldView( (AlmaGui) guiFrame, "Slice Viewer", 0, 0, 1000, 700 ) );
    	if( volFrame == null ) addFrame( volFrame = new VolumeRenderingView(   (AlmaGui) guiFrame, jocl, "Volume Rendering", 100, 100, 1000, 700 ) );

    	((SingleScalarFieldView)slcFrame).setData( dataVM );
    	(  (VolumeRenderingView)volFrame).setData( dataVM );
    			
    	guiFrame.setVisible(true);
    	slcFrame.setVisible(true);
		volFrame.setVisible(true);
		
		this.setTitle( "ALMA TDA -- " + filename );
		this.setJMenuBar( menuStandard );
		
	}

	
	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {
            	AlmaTDADev frame = new AlmaTDADev( 5, 5, 1200, 800, true );
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                
           		//String filename = "/Users/prosen/Code/alma/data/anil_seth/NGC404_CO21_briggs.pbcor.fits";
           		//String filename = "/Users/prosen/Code/alma/data/anil_seth/NGC404_CO21_briggs.pbcor.fits";
           		//String filename = "/Users/prosen/Code/alma/data/anil_seth/NGC404_CO21_uniform.pbcor.fits";
           		//String filename = "\\\\saav.cspaul.com\\projects\\ALMA\\data\\anil_seth\\NGC404_CO21_briggs.pbcor.fits";
           		//String filename = "\\\\saav.cspaul.com\\projects\\ALMA\\data\\anil_seth\\NGC404_CO21_uniform.pbcor.fits";
           		//String filename = "/Users/prosen/Code/alma/data/Continuum_33GHz.fits";
           		String filename = "/Users/prosen/Code/alma/data/betsy/CH3OH_7m+12m_natural.feather.fits";
           		//String filename = "/Users/prosen/Code/alma/data/betsy/HC3N_7m+12m_natural.feather.fits";
           		//String filename = "/Users/prosen/Code/alma/data/betsy/HCN_7m+12m_natural.feather.fits";
           		//String filename = "/Users/prosen/Code/alma/data/betsy/HCOp_7m+12m_natural.feather.fits";
           		//String filename = "/Users/prosen/Code/alma/data/betsy/SO_7m+12m_natural.feather.fits";
            		
           		frame.openFile( filename );

            }
        });
	}
	
	
}
