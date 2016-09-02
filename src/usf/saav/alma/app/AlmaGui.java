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

import interfascia.GUIController;
import interfascia.GUIEvent;
import interfascia.IFAutoFrameGroup;
import interfascia.IFAutoFrameGroup.Align;
import interfascia.IFButton;
import interfascia.IFCheckBox;
import interfascia.IFHAlign;
import interfascia.IFLabel;
import interfascia.IFRadioButton;
import interfascia.IFRadioController;
import interfascia.IFTextField;
import processing.core.PApplet;
import usf.saav.common.monitor.MonitoredBoolean;
import usf.saav.common.monitor.MonitoredEnum;
import usf.saav.common.monitor.MonitoredInteger;
import usf.saav.common.monitor.MonitoredTrigger;
import usf.saav.common.mvc.ViewComponent;

public class AlmaGui extends ViewComponent.Default implements ViewComponent {

	public enum ViewMode {
		SCALARFIELD, MOMENT0, MOMENT1, MOMENT2, VOLUME
	}
	
	public enum MouseMode {
		NAVIGATE, SELECT
	}

	public enum Dimension {
		DIM_2D, DIM_2D_STACK, DIM_3D
	}
	
	public enum TreeType {
		MERGE, SPLIT, CONTOUR;
	}

	private GUIController c;
	
	private IFRadioController rcMM;
	private IFRadioController rcView;
	private IFRadioController rcDim;
	private IFRadioController rcTree;


	private IFTextField 	guiZ,guiZ0,guiZ1;
	private IFRadioButton 	guiMMNav, guiMMSel;
	private IFRadioButton 	guiViewSF, guiViewM0, guiViewM1, guiViewM2, guiViewVol;
	private IFButton		guiBuildTree;
	private IFRadioButton	guiDim2D, guiDim2DStack, guiDim3D;
	private IFCheckBox		guiShowTree;
	private IFRadioButton	guiTreeSpl, guiTreeMer, guiTreeCon;
	private IFCheckBox		guiShowSimp;


	private MonitoredInteger 		 monZ,monZ0,monZ1;
	public  MonitoredEnum<MouseMode> monMM	  	  = new MonitoredEnum<MouseMode>( MouseMode.NAVIGATE );
	public  MonitoredEnum<ViewMode>  monView	  = new MonitoredEnum<ViewMode>( ViewMode.SCALARFIELD );
	public  MonitoredTrigger		 monBuildTree = new MonitoredTrigger("Contour Tree");
	public  MonitoredEnum<Dimension> monDim		  = new MonitoredEnum<Dimension>( Dimension.DIM_2D_STACK );
	public  MonitoredBoolean		 monShowTree  = new MonitoredBoolean( true );
	public  MonitoredBoolean		 monShowSimp  = new MonitoredBoolean( true );
	public  MonitoredEnum<TreeType>  monTree	  = new MonitoredEnum<TreeType>( TreeType.CONTOUR );


	private IFAutoFrameGroup guiSliceGrp;
	private IFAutoFrameGroup guiViewGrp;
	private IFAutoFrameGroup guiSimpGrp;
	private IFAutoFrameGroup guiDimGrp;
	private IFAutoFrameGroup guiTreeGrp;
	private IFAutoFrameGroup guiMouseGrp;
	
	private IFLabel guiLblSlice, guiLblVol;
	

	//IFHAlign alnZ0Z1;
	

	public AlmaGui( PApplet papplet, MonitoredInteger curZ, MonitoredInteger z0, MonitoredInteger z1 ){

		super.setPosition( papplet.width-130, 0, 130, papplet.height );

		this.monZ	 = curZ;
		this.monZ0 	 = z0;
		this.monZ1	 = z1;
		
		this.monZ.addMonitor(  this, "refreshVariables" );
		this.monZ0.addMonitor( this, "refreshVariables" );
		this.monZ1.addMonitor( this, "refreshVariables" );

		initControls( papplet );
		setControlPositions( );
		
	}
	
	private void initControls( PApplet papplet ){

		int u0 = 0;
		int v0 = 0;
		 
		c = new GUIController( papplet ); 

		rcView = new IFRadioController("Mode Selector");
		rcView.addActionListener( this );

		c.add(guiViewSF  = new IFRadioButton("Slice",   u0, v0, rcView));
		c.add(guiViewM0  = new IFRadioButton("Moment0", u0, v0, rcView));
		c.add(guiViewM1  = new IFRadioButton("Moment1", u0, v0, rcView));
		c.add(guiViewM2  = new IFRadioButton("Moment2", u0, v0, rcView));
		c.add(guiViewVol = new IFRadioButton("Volume",  u0, v0, rcView));
		guiViewSF.setSelected();

		c.add( guiZ = new IFTextField( "Slice #",  u0, v0, 40, Integer.toString(monZ.get()) ) );
		guiZ.addActionListener(this);

		c.add(guiZ0 = new IFTextField( "Slice #",  u0, v0, 40, Integer.toString(monZ0.get()) ) );
		guiZ0.addActionListener(this);

		c.add(guiZ1 = new IFTextField( "Slice #",  u0, v0, 40, Integer.toString(monZ1.get()) ));
		guiZ1.addActionListener(this);

		
		
		c.add( guiShowTree = new IFCheckBox("Show Tree", u0, v0 ) );
		guiShowTree.setSelected();
		guiShowTree.addActionListener(this);

		c.add( guiShowSimp = new IFCheckBox("Show Simp.", u0, v0 ) );
		guiShowSimp.setSelected();
		guiShowSimp.addActionListener(this);

		
		rcMM = new IFRadioController("Mouse Mode");
		rcMM.addActionListener( this );

		c.add( guiMMNav    = new IFRadioButton("Navigate",  u0, v0, rcMM) );
		c.add( guiMMSel    = new IFRadioButton("Selection", u0, v0, rcMM) );
		guiMMNav.setSelected();

		
		
		rcTree = new IFRadioController("Tree Type");
		rcTree.addActionListener( this );
		
		c.add( guiTreeMer    = new IFRadioButton("Merge Tree",   u0, v0, rcTree) );
		c.add( guiTreeSpl    = new IFRadioButton("Split Tree",   u0, v0, rcTree) );
		c.add( guiTreeCon    = new IFRadioButton("Contour Tree", u0, v0, rcTree) );
		guiTreeCon.setSelected();

		rcDim = new IFRadioController("Tree Dimension");
		rcDim.addActionListener( this );
		
		c.add( guiDim2D      = new IFRadioButton("2D",         u0, v0, rcDim) );
		c.add( guiDim2DStack = new IFRadioButton("2D Stacked", u0, v0, rcDim) );
		c.add( guiDim3D      = new IFRadioButton("3D",         u0, v0, rcDim) );
		guiDim2DStack.setSelected();
		
		c.add( guiBuildTree = new IFButton("Calculate", u0, v0, 40, 40 ) );
		guiBuildTree.addActionListener( this );
		
		c.add( guiLblSlice   = new IFLabel( "Active Slice",  u0, v0 ) );
		c.add( guiLblVol     = new IFLabel( "Active Volume", u0, v0 ) );
		
		
		c.add( guiSliceGrp = new IFAutoFrameGroup( null, u0, v0, 40, 40 ) );
		c.add( guiViewGrp  = new IFAutoFrameGroup( "View Mode", u0, v0, 40, 40 ) );
		c.add( guiMouseGrp = new IFAutoFrameGroup( "Mouse Mode", u0, v0, 40, 40 ) );
		c.add( guiTreeGrp  = new IFAutoFrameGroup( "Tree Type", u0, v0, 40, 40 ) );
		c.add( guiDimGrp   = new IFAutoFrameGroup( "Tree Dimension", u0, v0, 40, 40 ) );
		c.add( guiSimpGrp  = new IFAutoFrameGroup( "Simplification", u0, v0, 40, 40 ) );
		
	}
	
	
	
	public void refreshVariables( ){
		try {
		if( Integer.parseInt( guiZ.getValue() ) != monZ.get() ){
			guiZ.setValue( Integer.toString(monZ.get()) );
		}
		if( Integer.parseInt( guiZ0.getValue() ) != monZ0.get() ){
			guiZ0.setValue( Integer.toString(monZ0.get()) );
		}
		if( Integer.parseInt( guiZ1.getValue() ) != monZ1.get() ){
			guiZ1.setValue( Integer.toString(monZ1.get()) );
		}
		} catch( NumberFormatException nfe ){
			guiZ.setValue( Integer.toString(monZ.get()) );
			guiZ0.setValue( Integer.toString(monZ0.get()) );
			guiZ1.setValue( Integer.toString(monZ1.get()) );
		}
	}
	
	
	public void actionPerformed( GUIEvent e ){
		
		setControlPositions();

		if( e.getSource() == guiViewSF  ){		monView.set( ViewMode.SCALARFIELD );	}
		if( e.getSource() == guiViewM0  ){		monView.set( ViewMode.MOMENT0 );		}
		if( e.getSource() == guiViewM1  ){		monView.set( ViewMode.MOMENT1 );		} 
		if( e.getSource() == guiViewM2  ){		monView.set( ViewMode.MOMENT2 ); 		}
		if( e.getSource() == guiViewVol ){		monView.set( ViewMode.VOLUME ); 		}
		
		if( e.getSource() == guiMMNav ){		monMM.set( MouseMode.NAVIGATE ); 		}
		if( e.getSource() == guiMMSel ){		monMM.set( MouseMode.SELECT ); 			}
		
		if( e.getSource() == guiDim2D      ){		monDim.set( Dimension.DIM_2D ); 		}
		if( e.getSource() == guiDim2DStack ){		monDim.set( Dimension.DIM_2D_STACK ); 		}
		if( e.getSource() == guiDim3D      ){		monDim.set( Dimension.DIM_3D ); 		}

		if( e.getSource() == guiShowTree ){		monShowTree.set( guiShowTree.isSelected() );	}

		if( e.getSource() == guiShowSimp ){		monShowSimp.set( guiShowSimp.isSelected() );	}

		if( e.getSource() == guiZ && e.getMessage().equals("Completed") ){
			monZ.set( Integer.parseInt( guiZ.getValue() ) );
		}
		if( e.getSource() == guiZ0 && e.getMessage().equals("Completed") ){
			monZ0.set( Integer.parseInt( guiZ0.getValue() ) );
		}
		if( e.getSource() == guiZ1 && e.getMessage().equals("Completed") ){
			monZ1.set( Integer.parseInt( guiZ1.getValue() ) );
		}
		
		if( e.getSource() == guiBuildTree && e.getMessage().equals("Clicked")  ){
			monBuildTree.trigger();
		}
		
	}
	



	private void setControlPositions(){
		
		int u0 = winX.start();
		int v0 = winY.start();
		int width = winX.length();
		int height = winY.length();

		guiBuildTree.setSize( width-50, 20 );		

		
		c.setPosition( u0, v0 );
		c.setSize( width, height );
		
		guiSliceGrp.clear();
		guiSliceGrp.addComponent( guiLblSlice, Align.LEFT);
		guiSliceGrp.addComponent( new IFHAlign(  u0, v0, 10, 10, guiZ ), 	     Align.LEFT);
		guiSliceGrp.addComponent( guiLblVol,   Align.LEFT);
		guiSliceGrp.addComponent( new IFHAlign(  u0, v0, 10, 10, guiZ0, guiZ1 ), Align.LEFT);
		guiSliceGrp.setPosition( u0+10, 10 );
		guiSliceGrp.setSize( width-20, 40 );

		guiViewGrp.clear();
		guiViewGrp.addComponent( guiViewSF,   Align.LEFT );
		guiViewGrp.addComponent( guiViewM0,   Align.LEFT );
		guiViewGrp.addComponent( guiViewM1,   Align.LEFT );
		guiViewGrp.addComponent( guiViewM2,   Align.LEFT );
		guiViewGrp.addComponent( guiViewVol,  Align.LEFT );
		guiViewGrp.addComponent( guiShowTree, Align.LEFT );
		guiViewGrp.addComponent( guiShowSimp, Align.LEFT );
		guiViewGrp.setPosition( u0+10, guiSliceGrp.getY() + guiSliceGrp.getHeight() + 15 );
		guiViewGrp.setSize( width-20, 40 );

		guiMouseGrp.clear();
		guiMouseGrp.addComponent( guiMMNav, Align.LEFT );
		guiMouseGrp.addComponent( guiMMSel, Align.LEFT );
		guiMouseGrp.setPosition( u0+10, guiViewGrp.getY() + guiViewGrp.getHeight() + 15 );
		guiMouseGrp.setSize( width-20, 40 );

		guiTreeGrp.clear();
		guiTreeGrp.addComponent( guiTreeMer, Align.LEFT );
		guiTreeGrp.addComponent( guiTreeSpl, Align.LEFT );
		guiTreeGrp.addComponent( guiTreeCon, Align.LEFT );
		guiTreeGrp.setSize( width-40, 40 );

		guiDimGrp.clear();
		guiDimGrp.addComponent( guiDim2D,      Align.LEFT );
		guiDimGrp.addComponent( guiDim2DStack, Align.LEFT );
		guiDimGrp.addComponent( guiDim3D,      Align.LEFT );
		guiDimGrp.setSize( width-40, 40 );

		guiSimpGrp.clear();
		guiSimpGrp.addComponent( guiTreeGrp,   Align.CENTER );
		guiSimpGrp.addComponent( guiDimGrp,    Align.CENTER );
		guiSimpGrp.addComponent( guiBuildTree, Align.CENTER );
		guiSimpGrp.setPosition( u0+10, guiMouseGrp.getY() + guiMouseGrp.getHeight() + 15 );
		guiSimpGrp.setSize( width-20, 40 );

	}
	
	
	@Override
	public void setPosition(int u0, int v0, int w, int h) {
		super.setPosition(u0, v0, w, h);
		setControlPositions();
	}

}
