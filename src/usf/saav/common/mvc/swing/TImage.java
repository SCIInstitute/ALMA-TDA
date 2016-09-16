package usf.saav.common.mvc.swing;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import usf.saav.common.MathX;

public class TImage {

	private BufferedImage img;
	private Texture tex;
	private boolean needUpdate = true;
	private GLProfile profile;
	private GL prevGL;

	public TImage(GLProfile profile, int img_w, int img_h) {
		this.profile = profile;
		this.img     = new BufferedImage( img_w, img_h, BufferedImage.TYPE_INT_ARGB  );
	}
	
	public void set( int x, int y, int col ){ 
		needUpdate = true; 
		img.setRGB(x, y, col); 
	}
	
	public void set( int x, int y, float r, float g, float b ){
		set(x,y,(int)(r*255),(int)(g*255),(int)(b*255),255);
	}
	public void set( int x, int y, float r, float g, float b, float a ){ 
		set(x,y,(int)(r*255),(int)(g*255),(int)(b*255),(int)(a*255));
	}
	public void set( int x, int y, int r, int g, int b ){ 
		set(x,y,r,g,b,255);
	}
	public void set( int x, int y, int r, int g, int b, int a ){
		
		r = MathX.clamp(r, 0, 255);
		g = MathX.clamp(g, 0, 255);
		b = MathX.clamp(b, 0, 255);
		a = MathX.clamp(a, 0, 255);
		
		int col = ((r&0xFF) << 16)
				| ((g&0xFF) <<  8)
				| ((b&0xFF) <<  0)
				| ((a&0xFF) << 24);
		
		set(x,y,col);
	}

	public int get( int x, int y ){ return img.getRGB(x, y); }

	public void enable( GL gl ){ 
		if( gl != prevGL ){
			prevGL     = gl;
			tex        = AWTTextureIO.newTexture( profile, img, false );
			needUpdate = false;
		}
		if(needUpdate){ 
			TextureData texData = AWTTextureIO.newTextureData( profile, img,  false );
			tex.updateImage( gl, texData );
			//tex        = AWTTextureIO.newTexture( profile, img, false );
			needUpdate = false;
		}
		tex.enable(gl); 
	}
	public void bind( GL gl ){   tex.bind(gl); }
	public void disable( GL gl ){ tex.disable(gl); }

	public int[] getPixels() {
		return ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
	}
	
	public void update() {
		needUpdate = true;
	}
	
	
	
}
