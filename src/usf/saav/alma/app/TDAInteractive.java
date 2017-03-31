package usf.saav.alma.app;

import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import nom.tam.fits.common.FitsException;
import usf.saav.alma.app.interactive.AlmaGL;
import usf.saav.alma.app.interactive.AlmaGui;
import usf.saav.alma.app.interactive.AlmaModel;


// anil: interactive dim=3D x=[0,256) y=[0,256) z=[0,2] simplify=0.1 output=/Users/prosen/blah.fits /Volumes/Samsung_T3/alma/anil_seth/NGC404_CO21_briggs.pbcor.fits

public class TDAInteractive extends JFrame {

	private static final long serialVersionUID = 2558216175591225433L;


    public static void create( final TDAExec config ) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new TDAInteractive( config, "ALMA-TDA --- Interactive Mode", 1000, 200, 700 );
            }
        });
    }


	/**
	 * The Enum MouseMode.
	 */
	public enum MouseMode {
		NAVIGATE, SELECT_REGION
	}

	/**
	 * The Enum TreeDimension.
	 */
	public enum TreeDimension {
		DIM_2D, DIM_2D_STACK, DIM_3D
	}
	

	AlmaModel model    = null;
	AlmaGL    glPanel  = null;
    AlmaGui   guiPanel = null;
    
	
	
    private TDAInteractive(TDAExec config, String title, int glWidth, int guiWidth, int height) {
        super(title);
        

        
        if( config.filename == null ){
			final JFileChooser fc = new JFileChooser();
			fc.setFileFilter( new FitsFileFilter() );
	
			switch( fc.showOpenDialog(this) ){
			case JFileChooser.APPROVE_OPTION: 
				config.filename = fc.getSelectedFile().getAbsolutePath();
				break;
			case JFileChooser.CANCEL_OPTION: 
				System.err.println( "[TDAInteractive] No file selected" );
				System.exit(0);
			default:   
				System.err.println( "[TDAInteractive] JFileChooser error" );
				System.exit(-1);
				return;
			};
        }
        

		try {
			model = new AlmaModel( this, config );
		} catch (IOException | FitsException e) {
			e.printStackTrace();
			System.exit(-1);
		}
        glPanel  = new AlmaGL( model, glWidth, height );
	    guiPanel = new AlmaGui( model, guiWidth, height );

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout( new FlowLayout( FlowLayout.RIGHT ) );
        setResizable(false);
        add( glPanel );
        add( guiPanel );
        pack();
        setVisible(true);
		
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
