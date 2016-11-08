package usf.saav.common.mvc.swing;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.awt.TextRenderer;

import usf.saav.common.MathX;
import usf.saav.common.types.Float4;

public abstract class TGraphics implements GLEventListener, MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {

	public static final int ENABLE_DEPTH_TEST	= 329900;
	public static final int DISABLE_DEPTH_TEST	= 329901;
	
	public static final int LEFT 				= 329910;
	public static final int TOP					= 329912;
	public static final int CENTER				= 329913;
	public static final int RIGHT				= 329914;
	public static final int BOTTOM				= 329915;
	public static final int CORNER				= 329920;
	public static final int CORNERS				= 329921;

	public static final int RGB					= 349900;

	public static final int LINE_STRIP 			= GL.GL_LINE_STRIP;
	public static final int LINES 				= GL.GL_LINES;
	public static final int QUADS 				= GL2.GL_QUADS;
	public static final int TRIANGLES			= GL.GL_TRIANGLES;
	public static final int TRIANGLE_STRIP		= GL.GL_TRIANGLE_STRIP;
	public static final int TRIANGLE_FAN		= GL.GL_TRIANGLE_FAN;
	


	
	public static final float EPSILON			= (float) 1.0e-4;
	
	
	private GL2 gl;
	private long lastFrame = System.nanoTime();
	

	public int   width, height;
	public int   frameCount = 0;
	public float frameRate  = 0;
	
	private Map<Integer,TextRenderer> textRendererList = new HashMap<Integer,TextRenderer>( );
	private TextRenderer text_renderer;
	private int textSize = 0;
	private int textAlignH = LEFT;
	private int textAlignV = TOP;

	private int sphereDetail = 3;
	GeodesicSphere sphere = new GeodesicSphere( );
	
	
	private GLProfile profile;

	
	
	public TGraphics( GLProfile profile, int width, int height ){
		this.profile = profile;
		this.width  = width;
		this.height = height;
		lastFrame = System.nanoTime();
		textSize(20);
	}

	public abstract void init( );
	public abstract void draw( final GL2 gl );
	public abstract void reshape( int u, int v, int width, int height );

	@Override public void keyTyped(KeyEvent e) { }
	@Override public void keyPressed(KeyEvent e) { }
	@Override public void keyReleased(KeyEvent e) { }
	@Override public void mouseClicked(MouseEvent e) { }
	@Override public void mousePressed(MouseEvent e) { }
	@Override public void mouseReleased(MouseEvent e) { }
	@Override public void mouseEntered(MouseEvent e) { }
	@Override public void mouseExited(MouseEvent e) { }
	@Override public void mouseDragged(MouseEvent e) { }
	@Override public void mouseMoved(MouseEvent e) { }
	@Override public void mouseWheelMoved(MouseWheelEvent e) { }

	
	
	private static ByteBuffer createByteBuffer(int size) {
		return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
	}
	
	private BufferedImage screenshot() {
	    BufferedImage screenshot = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    Graphics graphics = screenshot.getGraphics();
	    
	    ByteBuffer buffer = createByteBuffer(width * height * 3);

	    gl.glReadPixels(0, 0, width, height, GL.GL_RGB, GL.GL_FLOAT, buffer);

	    FloatBuffer fb = buffer.asFloatBuffer();

	    for (int h = 0; h < height; h++) {
	        for (int w = 0; w < width; w++) {
	            graphics.setColor(new Color( fb.get(), fb.get(), fb.get() ));
	            graphics.drawRect(w,height - h, 1, 1); // height - h is for flipping the image
	        }
	    }
	    return screenshot;
	}
	
	

	public void saveFrame(){
		try {
            ImageIO.write( screenshot(), "png", new File("screen_" + frameCount + ".png") );
        } catch (IOException ex) {
        	System.err.println("[TGraphics] Failed to save image");
        }
		
	}

	protected void background( int color ){
		gl.glClearColor( (float)color/255.0f, (float)color/255.0f, (float)color/255.0f, 1 );
		gl.glClear( GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_COLOR_BUFFER_BIT );			
	}

	
	@Override
	public final void display(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2();
		draw( gl );
		frameCount++;
		long curFrame = System.nanoTime();
		double diff = (double)(curFrame-lastFrame)*1.0e-9;
		double fps = 1.0/diff;
		frameRate = (float) (frameRate*0.8 + fps*0.2);
		lastFrame = curFrame;
		
	}

	@Override public final void dispose(GLAutoDrawable arg0) { 
		text_renderer = null;
		textRendererList.clear();
	}

	@Override public final void init(GLAutoDrawable arg0) {
		init();
	}
	
	@Override public final void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		width = arg3;
		height = arg4;
		reshape(arg1, arg2, arg3, arg4);
	}
	
	
	
	
	

	public void hint(int hint ) {
		switch(hint){
		case ENABLE_DEPTH_TEST: gl.glEnable( GL.GL_DEPTH_TEST ); break;
		case DISABLE_DEPTH_TEST: gl.glDisable( GL.GL_DEPTH_TEST ); break;
		default:
			System.err.println("[TGraphics] Unknown hint flag");
		}		
	}

	

	private boolean fillEnable = true;
	private Float4  fillColor = new Float4(0,0,0,1);
	
	private boolean strokeEnable = true;
	private Float4  strokeColor = new Float4(0,0,0,1);
	private float   strokeWeight = 1;
	


	public static int color(float x, float y, float z, float w) {
		return color( (int)(x*255), (int)(y*255), (int)(z*255), (int)(w*255) );
	}

	public static int color( int r, int g, int b, int a ){
		
		r = MathX.clamp(r, 0, 255);
		g = MathX.clamp(g, 0, 255);
		b = MathX.clamp(b, 0, 255);
		a = MathX.clamp(a, 0, 255);
		
		int col = ((r&0xFF) << 16)
				| ((g&0xFF) <<  8)
				| ((b&0xFF) <<  0)
				| ((a&0xFF) << 24);
		
		return col;
	}

	
	public void noStroke( ){ strokeEnable = false; }
	public void stroke( float col ){
		strokeEnable = true;
		strokeColor.set( col, col, col, strokeColor.w );
	}
	public void stroke( float r, float g, float b, float a ){
		strokeEnable = true;
		strokeColor.set(r, g, b, a);
	}
	public void stroke( int col ){
		strokeEnable = true;
		strokeColor.set( (float)col/255.0f, (float)col/255.0f, (float)col/255.0f, strokeColor.w );
	}
	public void stroke(int r, int g, int b) {
		strokeEnable = true;
		strokeColor.set((float)r/255.0f, (float)g/255.0f, (float)b/255.0f, 1 );
	}
	public void stroke( int r, int g, int b, int a ){
		strokeEnable = true;
		strokeColor.set((float)r/255.0f, (float)g/255.0f, (float)b/255.0f, (float)a/255.0f);
	}
	
	public void strokeWeight( float w ){
		strokeWeight = w;
	}

	public void noFill( ){ fillEnable = false; }
	public void fill( float col ){
		fillEnable = true;
		fillColor.set( col, col, col, strokeColor.w );
	}
	public void fill( float r, float g, float b, float a ){
		fillEnable = true;
		fillColor.set(r, g, b, a);
	}

	public void fill( int col ){
		fillEnable = true;
		fillColor.set( (float)col/255.0f, (float)col/255.0f, (float)col/255.0f, 1.0f );
	}
	public void fill(int col, int alpha) {
		fillEnable = true;
		fillColor.set( (float)col/255.0f, (float)col/255.0f, (float)col/255.0f, (float)alpha/255.0f );
	}
	public void fill(int r, int g, int b) {
		fillEnable = true;
		fillColor.set((float)r/255.0f, (float)g/255.0f, (float)b/255.0f, 1 );
	}
	public void fill( int r, int g, int b, int a ){
		fillEnable = true;
		fillColor.set((float)r/255.0f, (float)g/255.0f, (float)b/255.0f, (float)a/255.0f);
	}




	
	
	
	
	
	public void rect( float u0, float v0, float w, float h ){
		if( fillEnable ){
			gl.glColor4f( fillColor.x, fillColor.y, fillColor.z, fillColor.w );
			gl.glBegin( GL2ES3.GL_QUADS );
				gl.glVertex3f( u0,  v0, 0 );
				gl.glVertex3f( u0,  v0+h, 0 );
				gl.glVertex3f( u0+w,  v0+h, 0 );
				gl.glVertex3f( u0+w,  v0, 0 );
			gl.glEnd();
		}
		if( strokeEnable ){
			gl.glColor4f( strokeColor.x, strokeColor.y, strokeColor.z, strokeColor.w );
			gl.glLineWidth( strokeWeight );
			gl.glBegin( GL2ES3.GL_LINE_LOOP );
				gl.glVertex3f( u0,  v0, 0 );
				gl.glVertex3f( u0,  v0+h, 0 );
				gl.glVertex3f( u0+w,  v0+h, 0 );
				gl.glVertex3f( u0+w,  v0, 0 );
			gl.glEnd();
		}	
	}	
	
	public void line(float x0, float y0, float x1, float y1) {
		gl.glColor4f( strokeColor.x, strokeColor.y, strokeColor.z, strokeColor.w );
		gl.glLineWidth( strokeWeight );
		gl.glBegin( GL2ES3.GL_LINES );
			gl.glVertex3f( x0,  y0, 0 );
			gl.glVertex3f( x1,  y1, 0 );
		gl.glEnd();
	}



	private int shapeType = 0;
	private void setShapeColor( ){
		switch( shapeType ){
		case GL.GL_LINE_LOOP:
		case GL.GL_LINE_STRIP:
		case GL.GL_LINES: 		gl.glColor4f( strokeColor.x, strokeColor.y, strokeColor.z, strokeColor.w ); break;
		default:				gl.glColor4f(   fillColor.x,   fillColor.y,   fillColor.z,    fillColor.w ); break;	
		}
	}
	public void beginShape(int type) {
		shapeType = type;
		gl.glBegin( type ); 	
	}
	public void vertex(float x, float y) {	setShapeColor(); gl.glVertex3f(x, y, 0); 	}
	public void vertex(float x, float y, float z) { setShapeColor(); gl.glVertex3f(x, y, z); }
	public void endShape() {	shapeType = 0;	gl.glEnd(); 	}
	
	
	public void pushMatrix() { gl.glPushMatrix(); }
	public void popMatrix() { gl.glPopMatrix(); }

	public void translate(float x, float y) { gl.glTranslatef(x, y, 0); }
	public void translate(float x, float y, float z) { gl.glTranslatef(x, y, z); }

	public void rotateX(float rotX) { gl.glRotatef( rotX, 1, 0, 0 ); }
	public void rotateY(float rotY) { gl.glRotatef( rotY, 0, 1, 0 ); }
	public void rotateZ(float rotZ) { gl.glRotatef( rotZ, 0, 0, 1 ); }

	public void scale(float x, float y, float z) { gl.glScalef(x, y, z); }

	
	
	public void flush() { 	gl.glFlush(); 	}

	
	
	

	

	public void text( String str, int xPosition, int yPosition ){
		if( text_renderer == null )
			textSize( textSize );
		
		int xPos = xPosition;
		int yPos = height-yPosition;
		int ySize = textSize;
		int xSize = textWidth(str);
		
		switch( textAlignH ){
			case LEFT:	  /*do nothing*/   break;
			case CENTER:  xPos -= xSize/2; break;
			case RIGHT:	  xPos -= xSize;   break;
			default: System.err.println( "[TGraphics] Unknown horizontal alignment " + textAlignH );
		}
		
		switch( textAlignV ){
			case TOP:	 yPos -= ySize;   break;
			case CENTER: yPos -= ySize/2; break;
			case BOTTOM: /*do nothing*/   break;
			default: System.err.println( "[TGraphics] Unknown vertical alignment " + textAlignV );
		}

		text_renderer.beginRendering( width, height );
		text_renderer.setColor( fillColor.x, fillColor.y, fillColor.z, fillColor.w );
		text_renderer.draw( str, xPos, yPos);
		text_renderer.endRendering();
	}
	
	
	public void textSize(int size){
		if( size < 8 ) size = 8;
		if( !textRendererList.containsKey(size) ){
			Font font = new Font("SansSerif", Font.BOLD, size);
			textRendererList.put(size, new TextRenderer( font ) );
		}
		textSize = size;
		text_renderer = textRendererList.get(textSize);
	}
	
	public void textAlign( int horizontal, int vertical ){
		textAlignH = horizontal;
		textAlignV = vertical;
	}

	public int textWidth(String label) {
		int w = 0;
		for( int i = 0; i < label.length(); i++ )
			w += text_renderer.getCharWidth(label.charAt(i));
		return w;
	}

	

	
	
	
	
	public void sphere(float size) {
		if( fillEnable ){
			gl.glColor4f( fillColor.x, fillColor.y, fillColor.z, fillColor.w );
			sphere.drawSolidSphere( this, sphereDetail );
		}
		if( strokeEnable ){
			gl.glColor4f( strokeColor.x, strokeColor.y, strokeColor.z, strokeColor.w );
			sphere.drawWireSphere( this, sphereDetail );
		}	
	}
	public void sphereDetail(int i) {
		sphereDetail = i;
	}

	
	
	
	
	

	public void point(int i, int j) {
		// TODO Auto-generated method stub
		
	}

	public void ellipse(float px, float py, float sizeX, float sizeY, int detail ) {
		int steps = detail;
		
		if( fillEnable ){
			gl.glColor4f( fillColor.x, fillColor.y, fillColor.z, fillColor.w );
			gl.glBegin( GL2ES3.GL_TRIANGLE_FAN );
			gl.glVertex3d( px,  py, 0 );
			for(int i = 0; i < steps; i++){
				float a = 2.0f*(float)Math.PI*(float)i/(float)(steps-1);
				gl.glVertex3d( px+sizeX*Math.cos(a),  py+sizeY*Math.sin(a), 0 );
			}
			gl.glEnd();
		}
		
		if( strokeEnable ){
			gl.glColor4f( strokeColor.x, strokeColor.y, strokeColor.z, strokeColor.w );
			gl.glLineWidth( strokeWeight );
			gl.glBegin( GL2ES3.GL_LINE_STRIP );
			for(int i = 0; i < steps; i++){
				float a = 2.0f*(float)Math.PI*(float)i/(float)(steps-1);
				gl.glVertex3d( px+sizeX*Math.cos(a),  py+sizeY*Math.sin(a), 0 );
			}
			gl.glEnd();
		}	
	}
	
	public void ellipse(float px, float py, float sizeX, float sizeY) {
		ellipse( px,py,sizeX,sizeY, (int) Math.pow(2,this.sphereDetail));
	}

	public TImage createImage(int img_w, int img_h, int img_type ) {
		return new TImage( profile, img_w, img_h );
	}

	
	private int imageMode = CORNER;
	
	public void image(TImage img, int x0, int y0, int w_x1, int h_y1) {
		
		img.enable(gl);
		img.bind(gl);
		
		gl.glColor4f(1,1,1,1);
		gl.glBegin( GL2ES3.GL_QUADS );
		if( imageMode == CORNER ){
			gl.glTexCoord2d(0.0, 0.0); gl.glVertex2d( x0, 	   y0 );
			gl.glTexCoord2d(1.0, 0.0); gl.glVertex2d( x0+w_x1, y0 );
			gl.glTexCoord2d(1.0, 1.0); gl.glVertex2d( x0+w_x1, y0+h_y1 );
			gl.glTexCoord2d(0.0, 1.0); gl.glVertex2d( x0,	   y0+h_y1 );
		}
		if( imageMode == CORNERS ){
			gl.glTexCoord2d(0.0, 0.0); gl.glVertex2d( x0, 	y0 );
			gl.glTexCoord2d(1.0, 0.0); gl.glVertex2d( w_x1, y0 );
			gl.glTexCoord2d(1.0, 1.0); gl.glVertex2d( w_x1, h_y1 );
			gl.glTexCoord2d(0.0, 1.0); gl.glVertex2d( x0,	h_y1 );
		}
		if( imageMode == CENTER ){
			gl.glTexCoord2d(0.0, 0.0); gl.glVertex2d( x0-w_x1/2, y0-h_y1/2 );
			gl.glTexCoord2d(1.0, 0.0); gl.glVertex2d( x0+w_x1/2, y0-h_y1/2 );
			gl.glTexCoord2d(1.0, 1.0); gl.glVertex2d( x0+w_x1/2, y0+h_y1/2 );
			gl.glTexCoord2d(0.0, 1.0); gl.glVertex2d( x0-w_x1/2, y0+h_y1/2 );
		}
		
		gl.glEnd();
		
		img.disable(gl);
		
	}

	public void imageMode(int mode) {
		if( mode != CORNER && mode != CORNERS && mode != CENTER ){
			System.err.println("[TGraphics] Invalid Image Mode");
			return;
		}
		imageMode = mode;
	}
	
	/*
	 draw image

	*/

}

