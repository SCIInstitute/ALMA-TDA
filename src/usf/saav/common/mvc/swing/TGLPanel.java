package usf.saav.common.mvc.swing;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

public class TGLPanel extends JPanel implements GLEventListener {
	private static final long serialVersionUID = -184570753250336350L;

	protected GLJPanel gljpanel;
	protected final GLProfile profile;
	
	public TGLPanel( int width, int height ){

		this.setSize(width, height);
		this.setPreferredSize( new Dimension(width, height) );

		//getting the capabilities object of GL2 profile        
		profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities  capabilities = new GLCapabilities(profile);

		// The canvas
		gljpanel = new GLJPanel( capabilities );
		gljpanel.setSize(width, height);
		gljpanel.setPreferredSize( new Dimension(width, height) );
		gljpanel.addGLEventListener( this );

		//adding canvas to frame
		this.add(gljpanel);
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
		gljpanel.repaint();
	}

	@Override public void dispose(GLAutoDrawable arg0) {  }
	@Override public void init(GLAutoDrawable arg0) {  }
	@Override public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) { }


}
