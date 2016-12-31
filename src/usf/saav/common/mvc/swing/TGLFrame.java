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
package usf.saav.common.mvc.swing;
import javax.swing.JInternalFrame;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

public class TGLFrame extends JInternalFrame implements GLEventListener {
	private static final long serialVersionUID = -184570753250336350L;

	protected GLJPanel gljpanel;
	protected final GLProfile profile;
	
	public TGLFrame( String title, int x0, int y0, int width, int height ){
		super( title,
				true,  //resizable
				false, //closable
				false, //maximizable
				true); //iconifiable

		this.setLocation(x0, y0);
		this.setSize(width, height);

		//getting the capabilities object of GL2 profile        
		profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities  capabilities = new GLCapabilities(profile);

		// The canvas
		gljpanel = new GLJPanel( capabilities );
		gljpanel.setSize(width, height);
		gljpanel.addGLEventListener( this );

		//adding canvas to frame
		this.getContentPane().add(gljpanel);
		this.setVisible(true);
	}


	public void setGraphics( TGraphics g ){

		gljpanel.addGLEventListener( g );
		gljpanel.addMouseListener( g );
		gljpanel.addMouseMotionListener( g );
		gljpanel.addMouseWheelListener( g );
		gljpanel.addKeyListener( g );
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		if( this.isIcon() ){ return; }
		gljpanel.repaint();
	}

	@Override public void dispose(GLAutoDrawable arg0) {  }
	@Override public void init(GLAutoDrawable arg0) {  }
	@Override public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) { }

}