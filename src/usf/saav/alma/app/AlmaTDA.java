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

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.filechooser.FileFilter;

import nom.tam.fits.common.FitsException;
import usf.saav.alma.app.views.AlmaGui;
import usf.saav.alma.app.views.BuildCacheProgressView;
import usf.saav.alma.app.views.HistoryView;
import usf.saav.alma.app.views.PropertiesView;
import usf.saav.alma.app.views.SingleScalarFieldView;
import usf.saav.alma.app.views.VolumeRenderingView;
import usf.saav.common.jocl.joclController;
import usf.saav.common.mvc.swing.TApp;
import usf.saav.common.mvc.swing.TGLFrame;

/**
 * The Class AlmaTDARelease.
 */
public class AlmaTDA extends TApp  {

	private static final long serialVersionUID = -226739546547617965L;


	public static void main(String args[]) {
		BuildCacheProgressView.createGUI();

		javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {
            	TApp frame = new AlmaTDA( 5, 5, 1200, 800 );
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
	}
	
	
	

	private joclController  jocl;

	private DataViewManager dataVM;
	private DataSetManager  dataSM;

	private AlmaStartupMenu  menuStartup;
	private AlmaStandardMenu menuStandard;

	private JInternalFrame guiFrame;
	private TGLFrame	   slcFrame;
	private TGLFrame	   volFrame;

	private JInternalFrame histFrame;
	private JInternalFrame propFrame;

	private RecentFileList recent;

	/**
	 * Instantiates a new alma TDA dev.
	 *
	 * @param title the title
	 * @param x the x
	 * @param y the y
	 * @param w the w
	 * @param h the h
	 */
	public AlmaTDA( int x, int y, int w, int h ){ 
		this( x, y, w, h, true );
	}


	/**
	 * Instantiates a new alma TDA dev.
	 *
	 * @param title the title
	 * @param x the x
	 * @param y the y
	 * @param w the w
	 * @param h the h
	 * @param verbose the verbose
	 */
	public AlmaTDA( int x, int y, int w, int h, boolean verbose ){ 
		super( x, y, w, h ); 
		
		recent = new RecentFileList();

		jocl = new joclController( false ); 

		menuStartup = new AlmaStartupMenu( recent );
		
		menuStartup.monFileOpen.addMonitor(  this, "fileOpen" );
		menuStartup.monFileLoad.addMonitor(  this, "fileLoad" );

		menuStandard = new AlmaStandardMenu( recent );
		menuStandard.monFileOpen.addMonitor(   this, "fileOpen" );
		menuStandard.monFileLoad.addMonitor(  this, "fileLoad" );
		menuStandard.monFileAppend.addMonitor( this, "fileAppend" );
		menuStandard.monFileClose.addMonitor(  this, "fileClose" );
		menuStandard.monFileRefreshCache.addMonitor( this, "rebuildCache" );
		menuStandard.monWindowProp.addMonitor( this, "showGeneralProperties" );
		menuStandard.monWindowHist.addMonitor( this, "showHistory" );
		menuStandard.monWindowVol.addMonitor(  this, "showVolume" );

		this.setJMenuBar( menuStartup );

		this.setTitle( "ALMA TDA" );

	}

	public void showGeneralProperties( ){
		if( propFrame == null ) addFrame( propFrame = new PropertiesView( dataSM.reader ) );
		propFrame.setVisible(true);
		try {
			propFrame.setIcon(false);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	public void showHistory( ){
		if( histFrame == null ) addFrame( histFrame = new HistoryView( dataSM.reader ) );
		histFrame.setVisible(true);
		try {
			histFrame.setIcon(false);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	public void showVolume( ){
		if( volFrame == null ){
			addFrame( volFrame = new VolumeRenderingView(   (AlmaGui) guiFrame, jocl, "Volume Rendering", 100, 100, 1000, 700 ) );
			(  (VolumeRenderingView)volFrame).setData( dataVM );
		}
		volFrame.setVisible(true);
		try {
			volFrame.setIcon(false);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	public void fileOpen( ){

		final JFileChooser fc = new JFileChooser();
		fc.setFileFilter( new FitsFileFilter() );

		switch( fc.showOpenDialog(this) ){
		case JFileChooser.APPROVE_OPTION: 
			fileClose(); 
			loadFile( fc.getSelectedFile().getAbsolutePath(), null ); 
			break;
		case JFileChooser.CANCEL_OPTION:  
			return;
		case JFileChooser.ERROR_OPTION:   
			System.err.println( "JFileChooser error" );
			return;
		};

	}
	
	public void fileLoad( String filename ){
		fileClose(); 
		loadFile( filename, null ); 
	}

	public void rebuildCache( ){
		new Thread() {
            public void run() {
        		setVisible(false);
        		BuildCacheProgressView.resetGUI();
        		BuildCacheProgressView.showGUI();
				dataSM.rebuildCache(BuildCacheProgressView.getGUI());
				dataVM.set2DSrcRefresh( );
				dataVM.set3DSrcRefresh( );
				dataVM.set2DSimplificationRefresh( );
				dataVM.set3DSimplificationRefresh( );
				BuildCacheProgressView.hideGUI();
        		setVisible(true);
            }
		}.start();
	}


	public void fileAppend( ){

		final JFileChooser fc = new JFileChooser();
		fc.setFileFilter( new FitsFileFilter() );

		switch( fc.showOpenDialog(this) ){
		case JFileChooser.APPROVE_OPTION: 

    		setVisible(false);
    		BuildCacheProgressView.resetGUI();
    		BuildCacheProgressView.showGUI();
			try {
				dataSM.appendFile( BuildCacheProgressView.getGUI(), fc.getSelectedFile().getAbsolutePath() );
			} catch ( IOException e ){
				System.err.println("Unable to read file");
				return;
			} catch( FitsException e ) {
				System.err.println("Unable to read file");
				return;
			}
			BuildCacheProgressView.hideGUI();
    		setVisible(true);
			
			break;
		case JFileChooser.CANCEL_OPTION:  
			return;
		case JFileChooser.ERROR_OPTION:   
			System.err.println( "JFileChooser error" );
			return;
		};

	}


	public void fileClose( ){
		if( guiFrame != null ) guiFrame.hide();
		if( slcFrame != null ) slcFrame.hide();
		if( volFrame != null ) volFrame.hide();

		dataSM = null;
		dataVM = null;

		this.setTitle( "ALMA TDA" );
		this.setJMenuBar( menuStartup );
	}

	public void loadFile( final String filename, final String filename2 ){

		recent.add(filename);
		menuStandard.updateRecent( recent );
		menuStartup.updateRecent( recent );
		
		new Thread() {
            public void run() {
        		setVisible(false);
        		
        		BuildCacheProgressView.resetGUI();
        		BuildCacheProgressView.showGUI();
				try {
					if( filename2 == null )
						dataSM = new DataSetManager( BuildCacheProgressView.getGUI(), filename );
					else
						dataSM = new DataSetManager( BuildCacheProgressView.getGUI(), filename, filename2 );
				} catch ( IOException e ) {
					System.err.println("Unable to read file");
					return;
				}
				catch ( FitsException e ) {
					System.err.println("Unable to read file");
					return;
				}

				dataVM = new DataViewManager( dataSM );
		
				if( guiFrame == null ) addFrame( guiFrame = new AlmaGui( 1000, 0, dataVM.curZ, dataVM.z0, dataVM.z1 ) );
				if( slcFrame == null ) addFrame( slcFrame = new SingleScalarFieldView( (AlmaGui) guiFrame, "Slice Viewer", 0, 0, 1000, 700 ) );
		
				dataVM.setGUI( (AlmaGui) guiFrame );
		
				((SingleScalarFieldView)slcFrame).setData( dataVM );
		
				guiFrame.setVisible(true);
				slcFrame.setVisible(true);
		
				setTitle( "ALMA TDA -- " + filename );
				setJMenuBar( menuStandard );
				
				BuildCacheProgressView.hideGUI();
				setVisible(true);
            }
		}.start();

	}



	private class FitsFileFilter extends FileFilter {

		@Override
		public boolean accept(File f){
			if(f.isDirectory()) return true;
			else if(f.getName().toLowerCase().endsWith(".fits")) return true;
			else return false;
		}

		@Override
		public String getDescription(){
			return "FITS files";
		}
	}


	
}