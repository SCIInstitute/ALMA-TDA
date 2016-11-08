package usf.saav.common.mvc;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;

import usf.saav.common.mvc.swing.TGLFrame;
import usf.saav.common.mvc.swing.TGraphics;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;


public abstract class DefaultGLFrame extends TGLFrame {

	private static final long serialVersionUID = 8803343267843456114L;
	
	public ViewComponent 		view;
	public ControllerComponent	controller;
	public TGraphics 			graphics;
	
	protected boolean verbose;
	
	private boolean saveImage = false;
	private boolean saveVideo = false;
	
	
	protected DefaultGLFrame( String title, int x, int y, int width, int height ){ 
		super( title, x, y, width, height );
		graphics = new Graphics( profile, width, height );
		setGraphics(graphics);
	}
	
	protected DefaultGLFrame( String title, int x, int y, int width, int height, boolean verbose ){ 
		super( title, x, y, width, height );
		graphics = new Graphics( profile, width, height );
		setGraphics(graphics);
		this.verbose = verbose;
	}

	protected abstract void update( );
	
	public void saveImage( ){ saveImage = true; }
	public void saveVideoEnable( ){ saveVideo = true; }
	public void saveVideoDisable( ){ saveVideo = false; }
	public void saveVideoToggle( ){ saveVideo = !saveVideo; }

	public ViewComponent getView( ){
		return view;
	}

	public ControllerComponent getController( ){
		return controller;
	}
	
	
	private class Graphics extends TGraphics {

		public Graphics( GLProfile profile, int width, int height ){
			super(profile,width,height);
		}

		public void init( ){
			
			if( controller == null || view == null ){
				System.err.println("[Graphics] View and/or controller not set during initialization" );
				return;
			}			

			if( controller != null ) controller.setup();
			if( controller != null ) controller.setPosition(0, 0, width, height);
			
			if( view != null ) view.setup();		
			if( view != null ) view.setPosition(0, 0, width, height);
			
		}
		
		@Override
		public void draw(final GL2 gl) {
			background(255);
	
			if( controller == null || view == null ) return;
			
			controller.update();
			update();
			view.update();
			
			gl.glMatrixMode( GLMatrixFunc.GL_PROJECTION );
			gl.glLoadIdentity();
			gl.glOrtho( 0, width, height, 0, -1000, 1000 );
			
			gl.glMatrixMode( GLMatrixFunc.GL_MODELVIEW );
			gl.glLoadIdentity();
			
			gl.glEnable (GL.GL_BLEND); 
			gl.glBlendFunc (GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			
			view.draw( this );
			view.drawLegend( this );
			
			if( verbose ){
				if( frameRate < 10 && (frameCount%20) == 0 )
					System.out.println("[" + getClass().getSimpleName() + "] Frame rate: " + frameRate);
				else if( frameRate < 20 && (frameCount%40) == 0 )
					System.out.println("[" + getClass().getSimpleName() + "] Frame rate: " + frameRate);
				else if( (frameCount%100) == 0 )
					System.out.println("[" + getClass().getSimpleName() + "] Frame rate: " + frameRate);
			}

			if( saveImage || saveVideo ) saveFrame();
			saveImage = false;
		}
		

		@Override
		public void reshape(int u, int v, int width, int height) {
			if( view != null ) view.setPosition(0, 0, width, height);			
			if( controller != null ) controller.setPosition(0, 0, width, height);			
		}
	
	
		@Override public void mouseMoved(MouseEvent e) {   if( controller == null ) return; controller.mouseMoved(e.getX()*2,e.getY()*2); }
		@Override public void keyPressed(KeyEvent e) {     if( controller == null ) return; controller.keyPressed( e.getKeyChar() ); }
		@Override public void mouseDragged(MouseEvent e) { if( controller == null ) return; controller.mouseDragged(e.getX()*2,e.getY()*2); }
		@Override public void mousePressed(MouseEvent e) {
			if( controller == null ) return;  
			if(e.getClickCount() == 2) 
				controller.mouseDoubleClick(e.getX()*2,e.getY()*2);
			else 
				controller.mousePressed(e.getX()*2,e.getY()*2); 
		}
		@Override public void mouseReleased(MouseEvent e) {			if( controller == null ) return; controller.mouseReleased(); }
		@Override public void mouseWheelMoved(MouseWheelEvent e) {
			if( controller == null ) return; controller.mouseWheel( e.getX()*2, e.getY()*2, (float)e.getPreciseWheelRotation() ); 
		}

	}
	
	
}
