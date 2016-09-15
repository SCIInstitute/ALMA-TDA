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

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import usf.saav.common.monitor.MonitoredBoolean;
import usf.saav.common.monitor.MonitoredEnum;
import usf.saav.common.monitor.MonitoredInteger;
import usf.saav.common.monitor.MonitoredTrigger;

public class AlmaGui extends JInternalFrame implements ActionListener {

	private static final long serialVersionUID = 6400845620173855634L;


	public enum ViewMode {
		SCALARFIELD, MOMENT0, MOMENT1, MOMENT2, VOLUME
	}
	
	public enum MouseMode {
		NAVIGATE, SELECT_REGION, SELECT_LINE
	}

	public enum TreeDimension {
		DIM_2D, DIM_2D_STACK, DIM_3D
	}
	
	public enum TreeType {
		MERGE, SPLIT, CONTOUR;
	}



	private MonitoredInteger 		 monZ,monZ0,monZ1;
	public  MonitoredEnum<MouseMode> monMM	  	  = new MonitoredEnum<MouseMode>( MouseMode.NAVIGATE );
	public  MonitoredEnum<ViewMode>  monView	  = new MonitoredEnum<ViewMode>( ViewMode.SCALARFIELD );
	public  MonitoredTrigger		 monBuildTree = new MonitoredTrigger("Contour Tree");
	public  MonitoredEnum<TreeDimension> monDim		  = new MonitoredEnum<TreeDimension>( TreeDimension.DIM_2D_STACK );
	public  MonitoredBoolean		 monShowTree  = new MonitoredBoolean( true );
	public  MonitoredBoolean		 monShowSimp  = new MonitoredBoolean( true );
	public  MonitoredEnum<TreeType>  monTree	  = new MonitoredEnum<TreeType>( TreeType.CONTOUR );

	
	private ButtonGroup rcView;
	private ButtonGroup rcMM;
	private ButtonGroup rcDim;
	private ButtonGroup rcTree;

	private JRadioButton 	guiMMNav, guiMMSelReg, guiMMSelLine;
	private JRadioButton 	guiViewSF, guiViewM0, guiViewM1, guiViewM2, guiViewVol;
	private JRadioButton	guiTreeSpl, guiTreeMer, guiTreeCon;
	private JRadioButton	guiDim2D, guiDim2DStack, guiDim3D;
	private JCheckBox		guiShowTree, guiShowSimp;

	private JTextField 	guiZ,guiZ0,guiZ1;

	private JButton		guiBuildTree;
	
	
	
	public AlmaGui( int x, int y, MonitoredInteger curZ, MonitoredInteger z0, MonitoredInteger z1 ){
	    super("Controls",
		          false, //resizable
		          false, //closable
		          false, //maximizable
		          true);//iconifiable


	    setLocation( x, y );
	    setSize( 150, 800 );

		this.monZ	 = curZ;
		this.monZ0 	 = z0;
		this.monZ1	 = z1;
		
		this.monZ.addMonitor(  this, "refreshVariables" );
		this.monZ0.addMonitor( this, "refreshVariables" );
		this.monZ1.addMonitor( this, "refreshVariables" );
		

		initControls( );

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
		
		
		
	    rcView = new ButtonGroup();
	    JPanel viewPanel = new JPanel(new GridLayout(0, 1));
	    viewPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder(Color.black), "View Mode" ) );

        viewPanel.add( guiViewSF  = createRadioButton("Slice",   true,  rcView)  );
        viewPanel.add( guiViewM0  = createRadioButton("Moment0", false, rcView) );
        viewPanel.add( guiViewM1  = createRadioButton("Moment1", false, rcView) );
        viewPanel.add( guiViewM2  = createRadioButton("Moment2", false, rcView) );
        viewPanel.add( guiViewVol = createRadioButton("Volume",  false, rcView) );

        viewPanel.add( guiShowTree = createCheckbox("Show Tree",  true ) );
		viewPanel.add( guiShowSimp = createCheckbox("Show Simp.", true ) );
        
		viewPanel.setMinimumSize( new java.awt.Dimension(100,400)  );
        
		

		rcMM = new ButtonGroup();
	    JPanel panelMM = new JPanel(new GridLayout(0, 1));
	    panelMM.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder(Color.black), "Mouse Mode" ) );
		panelMM.add( guiMMNav     = createRadioButton("Navigate",      true,  rcMM ) );
		panelMM.add( guiMMSelReg  = createRadioButton("Select Region", false, rcMM ) );
		panelMM.add( guiMMSelLine = createRadioButton("Select Line",   false, rcMM ) );
		

        
		rcTree = new ButtonGroup();
		JPanel panelTree = new JPanel(new GridLayout(0, 1));
		panelTree.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder(Color.black), "Tree Type" ) );
		panelTree.add( guiTreeMer    = createRadioButton("Merge Tree",   false, rcTree) );
		panelTree.add( guiTreeSpl    = createRadioButton("Split Tree",   false, rcTree) );
		panelTree.add( guiTreeCon    = createRadioButton("Contour Tree", true,  rcTree) );
	    

		

        
		rcDim = new ButtonGroup();
		JPanel panelDim = new JPanel(new GridLayout(0, 1));
		panelDim.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder(Color.black), "Tree Calculation" ) );
		panelDim.add( guiDim2D      = createRadioButton("2D",         false, rcDim) );
		panelDim.add( guiDim2DStack = createRadioButton("2D Stacked", true,  rcDim) );
		panelDim.add( guiDim3D      = createRadioButton("3D",         false, rcDim) );
		panelDim.add( guiBuildTree  = createButton( "Calculate" ) );

		slicePanel.setPreferredSize( new Dimension(125,65) );
		viewPanel.setPreferredSize(  new Dimension(125,150) );
		panelMM.setPreferredSize(    new Dimension(125,80) );
		panelTree.setPreferredSize(  new Dimension(125,80) );
		panelDim.setPreferredSize(   new Dimension(125,100) );

		
		JPanel mainPanel = new JPanel(new FlowLayout());
        //JPanel mainPanel = new JPanel(new GridLayout(0, 1));
		mainPanel.add( slicePanel );
        mainPanel.add( viewPanel );
		mainPanel.add( panelMM );
		mainPanel.add( panelTree );
		mainPanel.add( panelDim );
			

		this.add( mainPanel );
		this.setPreferredSize( new Dimension(125,700) );
		this.setSize( new Dimension(150,550) );
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {


		if( e.getSource() == guiViewSF  ){		monView.set( ViewMode.SCALARFIELD );	}
		if( e.getSource() == guiViewM0  ){		monView.set( ViewMode.MOMENT0 );		}
		if( e.getSource() == guiViewM1  ){		monView.set( ViewMode.MOMENT1 );		} 
		if( e.getSource() == guiViewM2  ){		monView.set( ViewMode.MOMENT2 ); 		}
		if( e.getSource() == guiViewVol ){		monView.set( ViewMode.VOLUME ); 		}
		
		if( e.getSource() == guiMMNav ){		monMM.set( MouseMode.NAVIGATE ); 		}
		if( e.getSource() == guiMMSelReg ){		monMM.set( MouseMode.SELECT_REGION ); 	}
		if( e.getSource() == guiMMSelLine ){	monMM.set( MouseMode.SELECT_LINE ); 	}
		
		if( e.getSource() == guiDim2D      ){	monDim.set( TreeDimension.DIM_2D ); 		}
		if( e.getSource() == guiDim2DStack ){	monDim.set( TreeDimension.DIM_2D_STACK );	}
		if( e.getSource() == guiDim3D      ){	monDim.set( TreeDimension.DIM_3D ); 		}
		
		if( e.getSource() == guiTreeSpl ){	monTree.set( TreeType.SPLIT   ); }
		if( e.getSource() == guiTreeMer ){	monTree.set( TreeType.MERGE   ); }
		if( e.getSource() == guiTreeCon ){	monTree.set( TreeType.CONTOUR ); }

		if( e.getSource() == guiShowTree ){		monShowTree.set( guiShowTree.isSelected() );	}
		if( e.getSource() == guiShowSimp ){		monShowSimp.set( guiShowSimp.isSelected() );	}
		
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
		
		if( e.getSource() == guiBuildTree  ){ 	monBuildTree.trigger(); 	}
		
	}
	
	
	
	
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
