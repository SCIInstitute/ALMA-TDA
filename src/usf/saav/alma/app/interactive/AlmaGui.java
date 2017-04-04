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
package usf.saav.alma.app.interactive;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import usf.saav.alma.app.TDAInteractive.MouseMode;
import usf.saav.alma.app.TDAInteractive.TreeDimension;

public class AlmaGui extends JPanel implements ActionListener {

	private static final long serialVersionUID = 6400845620173855634L;

	private JRadioButton 	guiMMNav, guiMMSelReg;
	private JRadioButton	guiDim2D, guiDim2DStack, guiDim3D;
	private JCheckBox		guiShowTree, guiShowSimp, guiMoment0;
	private JTextField 		guiZ,guiZ0,guiZ1;
	private JButton			guiBuildTree;
	
	private AlmaModel model;
	
	/**
	 * Instantiates a new alma gui.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public AlmaGui( AlmaModel _model, int w, int h ){
		model = _model;
		
	    setSize( w, h );
	    setPreferredSize( new Dimension( w, h ) );

	    model.monZ.addMonitor(  this, "refreshVariables" );
	    model.monZ0.addMonitor( this, "refreshVariables" );
	    model.monZ1.addMonitor( this, "refreshVariables" );
	    model.monMM.addMonitor( this, "refreshVariables" );
		
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
	    slicesubPanel.add( new  JLabel("View Slice") );
	    slicesubPanel.add( guiZ = createTextfield( Integer.toString(model.monZ.get()) ) );
	    slicesubPanel.setPreferredSize( new Dimension(125,20) );
	    
	    JPanel volPanel = new JPanel(new GridLayout(0, 2));
	    volPanel.add(guiZ0 = createTextfield( Integer.toString(model.monZ0.get()) ) );
	    volPanel.add(guiZ1 = createTextfield( Integer.toString(model.monZ1.get()) ) );

	    JPanel slicePanel = new JPanel(new GridLayout(0, 1));
	    slicePanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder(Color.black), "1. Navigate" ) );
	    slicePanel.add(slicesubPanel);
	    //slicePanel.add( new JLabel("Compute Volume") );
	    //slicePanel.add(volPanel);

	    
    	ButtonGroup rcDim;
		rcDim = new ButtonGroup();
		JPanel panelDim = new JPanel(new GridLayout(0, 1));
		panelDim.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder(Color.black), "2. Tree Configuration" ) );
		//panelDim.add( guiDim2D      = createRadioButton("2D",         true, rcDim) );
		//panelDim.add( guiDim2DStack = createRadioButton("2D Stacked", false, rcDim) );
		panelDim.add( new JLabel("Compute Volume") );
	    panelDim.add(volPanel);
		panelDim.add( guiDim2DStack = createRadioButton("2D", true, rcDim) );
		panelDim.add( guiDim3D      = createRadioButton("3D",         false, rcDim) );

    	ButtonGroup rcMM;
		rcMM = new ButtonGroup();
	    JPanel panelMM = new JPanel(new GridLayout(0, 1));
	    panelMM.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder(Color.black), "3. Select a Region" ) );
		panelMM.add( guiMMNav     = createRadioButton("Navigate",      true,  rcMM ) );
		panelMM.add( guiMMSelReg  = createRadioButton("Select Region", false, rcMM ) );
		

		
	    JPanel viewPanel = new JPanel(new GridLayout(0, 1));
	    viewPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder(Color.black), "4. Explore Results" ) );
        viewPanel.add( guiShowTree = createCheckbox("Show Tree",  true ) );
		viewPanel.add( guiShowSimp = createCheckbox("Show Simp.", true ) );
		//viewPanel.add( guiMoment0 = createCheckbox("Show Moment 0", false ) );
        


	    JPanel panelBtn = new JPanel(new GridLayout(0, 1));
	    panelBtn.add( guiBuildTree  = createButton( "5. Calculate & Exit" ) );

		slicePanel.setPreferredSize( new Dimension(160,50) );
		panelDim.setPreferredSize(   new Dimension(160,110) );
		panelMM.setPreferredSize(    new Dimension(160,75) );
		viewPanel.setPreferredSize(  new Dimension(160,75) );
		panelBtn.setPreferredSize(   new Dimension(160,55) );

		
		JPanel mainPanel = new JPanel(new FlowLayout());
		mainPanel.setPreferredSize( this.getPreferredSize() );
		mainPanel.add( slicePanel );
		mainPanel.add( panelDim );
		mainPanel.add( panelMM );
        mainPanel.add( viewPanel );
		mainPanel.add( panelBtn );
			
		this.add( mainPanel );

	}



	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if( e.getSource() == guiMMNav ){		model.monMM.set( MouseMode.NAVIGATE ); 		}
		if( e.getSource() == guiMMSelReg ){		model.monMM.set( MouseMode.SELECT_REGION ); 	}
		
		if( e.getSource() == guiDim2D      ){	model.monDim.set( TreeDimension.DIM_2D ); 		}
		if( e.getSource() == guiDim2DStack ){	model.monDim.set( TreeDimension.DIM_2D_STACK );	}
		if( e.getSource() == guiDim3D      ){	model.monDim.set( TreeDimension.DIM_3D ); 		}

		if( e.getSource() == guiShowTree ){		model.monShowTree.set( guiShowTree.isSelected() );	}
		if( e.getSource() == guiShowSimp ){		model.monShowSimp.set( guiShowSimp.isSelected() );	}
		if( e.getSource() == guiMoment0 ){		model.monShowM0.set( guiMoment0.isSelected() );	}
		
		if( e.getSource() == guiBuildTree ){	model.monButton.trigger();	}

		try{
			if( e.getSource() == guiZ  ){ model.monZ.set(  Integer.parseInt( guiZ.getText()  ) ); }
			if( e.getSource() == guiZ0 ){ model.monZ0.set( Integer.parseInt( guiZ0.getText() ) ); }
			if( e.getSource() == guiZ1 ){ model.monZ1.set( Integer.parseInt( guiZ1.getText() ) ); }
		}
		catch( NumberFormatException nfe ){
			guiZ.setText(  Integer.toString(model.monZ.get()) );
			guiZ0.setText( Integer.toString(model.monZ0.get()) );
			guiZ1.setText( Integer.toString(model.monZ1.get()) );
		}
		
	}
	
	
	
	/**
	 * Refresh variables.
	 */
	public void refreshVariables( ){
		try {
			if( Integer.parseInt( guiZ.getText() ) != model.monZ.get() ){
				guiZ.setText( Integer.toString(model.monZ.get()) );
			}
			if( Integer.parseInt( guiZ0.getText() ) != model.monZ0.get() ){
				guiZ0.setText( Integer.toString(model.monZ0.get()) );
			}
			if( Integer.parseInt( guiZ1.getText() ) != model.monZ1.get() ){
				guiZ1.setText( Integer.toString(model.monZ1.get()) );
			}
		} catch( NumberFormatException nfe ){
			guiZ.setText(  Integer.toString(model.monZ.get()) );
			guiZ0.setText( Integer.toString(model.monZ0.get()) );
			guiZ1.setText( Integer.toString(model.monZ1.get()) );
		}
		
		switch( model.monMM.get() ){
			case NAVIGATE: guiMMNav.setSelected(true ); break;
			case SELECT_REGION: guiMMSelReg.setSelected(true); break;
		}
	}

}