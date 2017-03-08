package usf.saav.alma.app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import nom.tam.fits.common.FitsException;
import usf.saav.alma.data.fits.RawFitsReader;
import usf.saav.alma.data.fits.SafeFitsReader;
import usf.saav.alma.data.processors.Subsample2D;
import usf.saav.common.monitor.MonitoredBoolean;
import usf.saav.common.monitor.MonitoredDouble;
import usf.saav.common.monitor.MonitoredEnum;
import usf.saav.common.monitor.MonitoredInteger;
import usf.saav.common.monitor.MonitoredObject;
import usf.saav.common.monitor.MonitoredTrigger;
import usf.saav.scalarfield.ScalarField2D;

public class InteractiveViewer extends JFrame {

	
    public static void create( final Cmdline config ) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new InteractiveViewer( config, "Interactive", 1000, 200, 700 );
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
	
    MonitoredInteger			 monX		  = new MonitoredInteger(0);
    MonitoredInteger			 monY		  = new MonitoredInteger(0);
    MonitoredInteger			 monZ		  = new MonitoredInteger(0);
    MonitoredDouble				 monZoom	  = new MonitoredDouble(1.0);
    MonitoredInteger			 monZ0		  = new MonitoredInteger(0);
    MonitoredInteger			 monZ1		  = new MonitoredInteger(0);
	MonitoredEnum<MouseMode>	 monMM	  	  = new MonitoredEnum<MouseMode>( MouseMode.NAVIGATE );
	MonitoredEnum<TreeDimension> monDim		  = new MonitoredEnum<TreeDimension>( TreeDimension.DIM_2D_STACK );
	MonitoredBoolean			 monShowTree  = new MonitoredBoolean( true );
	MonitoredBoolean			 monShowSimp  = new MonitoredBoolean( true );
	MonitoredTrigger			 monButton    = new MonitoredTrigger( );

	AlmaGL  glPanel  = null;
    AlmaGui guiPanel = null;
	SafeFitsReader fits;
    
	private static final long serialVersionUID = 2558216175591225433L;

	
	
    private InteractiveViewer(Cmdline config, String title, int glWidth, int guiWidth, int height) {
        super(title);
        

        
        if( config.filename == null ){
			final JFileChooser fc = new JFileChooser();
			fc.setFileFilter( new FitsFileFilter() );
	
			switch( fc.showOpenDialog(this) ){
			case JFileChooser.APPROVE_OPTION: 
				config.filename = fc.getSelectedFile().getAbsolutePath();
				break;
			case JFileChooser.CANCEL_OPTION: 
				System.err.println( "[InteractiveViewer] No file selected" );
				System.exit(0);
			default:   
				System.err.println( "[InteractiveViewer] JFileChooser error" );
				System.exit(-1);
				return;
			};
        }
        
		try {
			fits = new SafeFitsReader( new RawFitsReader(config.filename, true), true );
		} catch (IOException | FitsException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		monX.set(fits.getAxesSize()[0].middle());
		monY.set(fits.getAxesSize()[1].middle());
		monZ.set(0);
		monZ0.set(0);
		monZ1.set(fits.getAxesSize()[2].end());
		
        glPanel  = new AlmaGL( this, glWidth, height );
	    guiPanel = new AlmaGui( guiWidth, height );

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


	

    
    public class AlmaGui extends JPanel implements ActionListener {

    	private static final long serialVersionUID = 6400845620173855634L;

    	private JRadioButton 	guiMMNav, guiMMSelReg;
    	private JRadioButton	guiDim2D, guiDim2DStack, guiDim3D;
    	private JCheckBox		guiShowTree, guiShowSimp;
    	private JTextField 		guiZ,guiZ0,guiZ1;
    	private JButton			guiBuildTree;
    	
    	
    	/**
    	 * Instantiates a new alma gui.
    	 *
    	 * @param x the x
    	 * @param y the y
    	 */
    	public AlmaGui( int w, int h ){

    	    setSize( w, h );
    	    setPreferredSize( new Dimension( w, h ) );

    		monZ.addMonitor(  this, "refreshVariables" );
    		monZ0.addMonitor( this, "refreshVariables" );
    		monZ1.addMonitor( this, "refreshVariables" );
    		
    		initControls( );
    	    setSize( w, h );
    	    setPreferredSize( new Dimension( w, h ) );

    	}
    	
    	
    	private JRadioButton createRadioButton( String label, boolean selected, ButtonGroup group ){
    		JRadioButton btn = new JRadioButton(label);
    		btn.setActionCommand(label);
    		btn.setSelected(selected);
    		btn.addActionListener(this);
    		group.add(btn);
    		return btn;
    	}
    	
    	private JTextField createTextfield( String value ){
    		JTextField ret = new JTextField(value);
    		ret.addActionListener(this);
    		return ret;
    	}
    	
    	private JCheckBox createCheckbox( String label, boolean selected ){
    		JCheckBox ret = new JCheckBox( label );
    		ret.setSelected( selected );
    		ret.addActionListener(this);
    		return ret;
    	}
    	

    	private JButton createButton( String label  ){
    		JButton ret = new JButton( label );
    		ret.addActionListener(this);
    		return ret;
    	}
    	
    	
    	private void initControls( ){

    	    JPanel slicesubPanel = new JPanel(new GridLayout(0, 2));
    	    slicesubPanel.add( new  JLabel("Active Slice") );
    	    slicesubPanel.add( guiZ = createTextfield( Integer.toString(monZ.get()) ) );
    	    slicesubPanel.setPreferredSize( new Dimension(125,20) );
    	    
    	    JPanel volPanel = new JPanel(new GridLayout(0, 2));
    	    volPanel.add(guiZ0 = createTextfield( Integer.toString(monZ0.get()) ) );
    	    volPanel.add(guiZ1 = createTextfield( Integer.toString(monZ1.get()) ) );

    	    JPanel slicePanel = new JPanel(new GridLayout(0, 1));
    	    slicePanel.add(slicesubPanel);
    	    slicePanel.add( new JLabel("Active Volume") );
    	    slicePanel.add(volPanel);
    		
    		
    	    JPanel viewPanel = new JPanel(new GridLayout(0, 1));
    	    viewPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder(Color.black), "View Mode" ) );
            viewPanel.add( guiShowTree = createCheckbox("Show Tree",  true ) );
    		viewPanel.add( guiShowSimp = createCheckbox("Show Simp.", true ) );
            
        	ButtonGroup rcMM;
    		rcMM = new ButtonGroup();
    	    JPanel panelMM = new JPanel(new GridLayout(0, 1));
    	    panelMM.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder(Color.black), "Mouse Mode" ) );
    		panelMM.add( guiMMNav     = createRadioButton("Navigate",      true,  rcMM ) );
    		panelMM.add( guiMMSelReg  = createRadioButton("Select Region", false, rcMM ) );
    		

        	ButtonGroup rcDim;
    		rcDim = new ButtonGroup();
    		JPanel panelDim = new JPanel(new GridLayout(0, 1));
    		panelDim.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder(Color.black), "Tree Calculation" ) );
    		panelDim.add( guiDim2D      = createRadioButton("2D",         false, rcDim) );
    		panelDim.add( guiDim2DStack = createRadioButton("2D Stacked", true,  rcDim) );
    		panelDim.add( guiDim3D      = createRadioButton("3D",         false, rcDim) );

    	    JPanel panelBtn = new JPanel(new GridLayout(0, 1));
    	    panelBtn.add( guiBuildTree  = createButton( "Calculate & Exit" ) );

    		slicePanel.setPreferredSize( new Dimension(150,70) );
    		viewPanel.setPreferredSize(  new Dimension(150,70) );
    		panelMM.setPreferredSize(    new Dimension(150,70) );
    		panelDim.setPreferredSize(   new Dimension(150,85) );
    		panelBtn.setPreferredSize(   new Dimension(150,50) );

    		
    		JPanel mainPanel = new JPanel(new FlowLayout());
    		mainPanel.setPreferredSize( this.getPreferredSize() );
    		mainPanel.add( slicePanel );
            mainPanel.add( viewPanel );
    		mainPanel.add( panelMM );
    		mainPanel.add( panelDim );
    		mainPanel.add( panelBtn );
    			
    		this.add( mainPanel );

    	}



    	/* (non-Javadoc)
    	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
    	 */
    	@Override
    	public void actionPerformed(ActionEvent e) {

    		if( e.getSource() == guiMMNav ){		monMM.set( MouseMode.NAVIGATE ); 		}
    		if( e.getSource() == guiMMSelReg ){		monMM.set( MouseMode.SELECT_REGION ); 	}
    		
    		if( e.getSource() == guiDim2D      ){	monDim.set( TreeDimension.DIM_2D ); 		}
    		if( e.getSource() == guiDim2DStack ){	monDim.set( TreeDimension.DIM_2D_STACK );	}
    		if( e.getSource() == guiDim3D      ){	monDim.set( TreeDimension.DIM_3D ); 		}

    		if( e.getSource() == guiShowTree ){		monShowTree.set( guiShowTree.isSelected() );	}
    		if( e.getSource() == guiShowSimp ){		monShowSimp.set( guiShowSimp.isSelected() );	}
    		
    		if( e.getSource() == guiBuildTree ){	monShowTree.set( guiShowTree.isSelected() );	}

    		try{
    			if( e.getSource() == guiZ  ){ monZ.set(  Integer.parseInt( guiZ.getText()  ) ); }
    			if( e.getSource() == guiZ0 ){ monZ0.set( Integer.parseInt( guiZ0.getText() ) ); }
    			if( e.getSource() == guiZ1 ){ monZ1.set( Integer.parseInt( guiZ1.getText() ) ); }
    		}
    		catch( NumberFormatException nfe ){
    			guiZ.setText(  Integer.toString(monZ.get()) );
    			guiZ0.setText( Integer.toString(monZ0.get()) );
    			guiZ1.setText( Integer.toString(monZ1.get()) );
    		}
    		
    	}
    	
    	
    	
    	/**
    	 * Refresh variables.
    	 */
    	public void refreshVariables( ){
    		try {
    			if( Integer.parseInt( guiZ.getText() ) != monZ.get() ){
    				guiZ.setText( Integer.toString(monZ.get()) );
    			}
    			if( Integer.parseInt( guiZ0.getText() ) != monZ0.get() ){
    				guiZ0.setText( Integer.toString(monZ0.get()) );
    			}
    			if( Integer.parseInt( guiZ1.getText() ) != monZ1.get() ){
    				guiZ1.setText( Integer.toString(monZ1.get()) );
    			}
    		} catch( NumberFormatException nfe ){
    			guiZ.setText(  Integer.toString(monZ.get()) );
    			guiZ0.setText( Integer.toString(monZ0.get()) );
    			guiZ1.setText( Integer.toString(monZ1.get()) );
    		}
    	}

    }

    
}
