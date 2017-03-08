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
package usf.saav.alma.app.old;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
//import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import usf.saav.alma.app.views.AlmaGui;
import usf.saav.alma.app.views.BuildCacheProgressView;
import usf.saav.alma.app.views.HistoryView;
import usf.saav.alma.app.views.PropertiesView;
import usf.saav.alma.app.views.SingleScalarFieldView;
import usf.saav.alma.app.views.VolumeRenderingView;
import usf.saav.common.jocl.joclController;
import usf.saav.common.mvc.swing.TApp;
import usf.saav.common.mvc.swing.TGLFrame;
import usf.saav.scalarfield.ScalarField3D;
import nom.tam.fits.ImageHDU;
import nom.tam.fits.FitsFactory;
import nom.tam.fits.FitsUtil;
import nom.tam.fits.common.FitsException;
import nom.tam.util.BufferedFile;
//import nom.tam.fits.HeaderCard;
//import nom.tam.fits.HeaderCardException;


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
		menuStandard.monFileExport.addMonitor( this, "fileExport" );
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

	public void fileExport( ) throws FitsException {
		final JFileChooser fc = new JFileChooser();
		fc.setFileFilter( new FitsFileFilter() );

		switch( fc.showSaveDialog(this) ){
		case JFileChooser.APPROVE_OPTION:
			String filepath = fc.getSelectedFile().getAbsolutePath();
			if (! filepath.toLowerCase().endsWith(".fits"))
				filepath += ".fits";
			exportFile( filepath );
			break;
		case JFileChooser.CANCEL_OPTION:
			return;
		case JFileChooser.ERROR_OPTION:
			System.err.println( "JFileChooser error" );
			return;
		};
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

	public void exportFile( String filepath ) throws FitsException {
		ScalarField3D sf = dataVM.simp_sf3d.get();

		String originalFilename = dataSM.reader.get(0).getFile().getName();
		String comment = "Propagated from " + originalFilename;
		double [] coordOrigin = dataSM.reader.get(0).getCoordOrigin();
		double [] coordDelta = dataSM.reader.get(0).getCoordDelta();

		// slice dims
		final int SLICEWIDTH = sf.getWidth();
		final int SLICEHEIGHT = sf.getHeight();
		// channels
		final int DEPTH = sf.getDepth();
		final int WORDSIZE = 4;

		try {
			float [][][] data = new float[1][SLICEHEIGHT][SLICEWIDTH];
			BufferedFile bf = new BufferedFile(filepath, "rw", 16384);

			ImageHDU ihdu = (ImageHDU) FitsFactory.hduFactory(data);
			ihdu.getHeader().addValue("NAXIS3", DEPTH, "Actual number of channels");

			// propagate header info from RawFitsReader
			// Note: assuming first reader was the one that read the displayed data cube.
			// It's probably a reasonable assumption...
			for(int i = 0; i < coordOrigin.length; i++) {
				ihdu.getHeader().addValue("CRVAL"+(i+1), coordOrigin[i], comment);
			}

			for(int i = 0; i < coordDelta.length; i++) {
				ihdu.getHeader().addValue("CDELT"+(i+1), coordDelta[i], comment);
			}

			// TODO: setting cards from FitsProperties results in a bad header
//					FitsProperties properties = dataSM.reader.get(0).getProperties();
//					Iterator<FitsProperty> iter = properties.iterator();
//
//					while( iter.hasNext() ) {
//						try {
//							FitsProperty property = iter.next();
//							HeaderCard card = property.toHeaderCard();
//							ihdu.getHeader().addLine(card);
//						}
//						catch (HeaderCardException e) {
//							System.out.println("Creating HeaderCard failed: " + e.getMessage());
//						}
//					}

			ihdu.getHeader().write(bf);

			// write out data cube by channel
			for (int d = 0; d < DEPTH; ++d) {
				for (int w = 0; w < SLICEWIDTH; ++w) {
					for (int h = 0; h < SLICEHEIGHT; ++h) {
						data[0][h][w] = sf.getValue(w, h, d);
					}
				}
				bf.writeArray(data);
			}

			FitsUtil.pad(bf, SLICEWIDTH*SLICEHEIGHT*DEPTH*WORDSIZE);
			bf.close();
		} catch (FitsException e) {
			System.out.println("Fits export failed: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("BufferedFile created failed: " + e.getMessage());
		} catch (java.lang.OutOfMemoryError e) {
			JOptionPane.showMessageDialog(this, "System is out of memory. Try zooming out and try again.",
					                      "Out Of Memory Error", JOptionPane.ERROR_MESSAGE);
		}
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