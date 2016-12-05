package usf.saav.common.colormap;

import java.text.DecimalFormat;

import usf.saav.common.MathXv1;
import usf.saav.common.mvc.swing.TGraphics;
import usf.saav.common.range.FloatRange1D;
import usf.saav.common.types.Float3;
import usf.saav.common.types.Float4;

public class SequentialColormap extends Colormap.VectorColormap {

	protected FloatRange1D br = new FloatRange1D( 0, 1 );
	private Float4 nanColor = new Float4(0,0,0,1);
	private Float4 infColor = new Float4(0,0,0,1);

	public SequentialColormap(){ }

	public void setRange(FloatRange1D b){
		br = b.clone();
	}

	public Float4 getColor( float v ) {
		if( cols.size() == 0 ) return new Float4(1.0f,1.0f,1.0f,1.0f); 
		if( Float.isNaN(v) ) return nanColor;
		if( Float.isInfinite(v) ) return infColor;

		float t = (float) br.getNormalized(v);
		
		if( t <= 0.0f ) return cols.get(0);
		if( t >= 1.0f ) return cols.lastElement();

		float ti = t*(float)(cols.size()-1);

		int   i0 = (int)Math.floor(ti);
		int   i1 = Math.min( i0+1, (int)cols.size()-1 );

		return Float4.lerp( cols.get(i0), cols.get(i1), ti-Math.floor(ti) );

	}

	@Override
	public void drawScale(TGraphics g, int loc_x, int loc_y, int w, int h) {

		g.hint( TGraphics.DISABLE_DEPTH_TEST );
		g.textSize(16);
		g.noStroke( );
		g.textAlign(TGraphics.LEFT, TGraphics.BOTTOM);
		float step = (float)h/40.0f;
		for ( int i = 0; i <= 40; i++ ) {
			float v = MathXv1.lerp( (float)br.getMinimum(), (float)br.getMaximum(), 1.0f - (float)i/40.0f );
			Float4 col = getColor( v );
			g.fill( col.GetRed(), col.GetGreen(), col.GetBlue(), 255 );

			g.rect( (float)loc_x, (float)loc_y+(float)i*step, (float)w, step );
		}

		for( int j = 0; j <= 8; j++){
			float i = MathXv1.lerp( 1.0f, 39.0f, (float)j/8.0f );
			float v = MathXv1.lerp( (float)br.getMinimum(), (float)br.getMaximum(), 1.0f - (float)i/40.0f );
			g.fill(0);
			String txt = " " + new DecimalFormat("#.###").format(v); 
			g.text( txt, (int)(loc_x+w), (int) (loc_y+10+(float)i*step*1.01));
		}

		g.hint( TGraphics.ENABLE_DEPTH_TEST );
	}
	

	public void setNaNColor(Float3 col) {
		this.nanColor  = new Float4(col.x,col.y,col.z,1);
	}
	
	public void setNaNColor(Float4 col) {
		this.nanColor  = col;
	}
	
	public void setInfColor(Float3 col) {
		this.infColor  = new Float4(col.x,col.y,col.z,1);
	}
	
	public void setInfColor(Float4 col) {
		this.infColor  = col;
	}
	
	



	public static class ColorblindSafeRainbowSequential extends SequentialColormap {
		public ColorblindSafeRainbowSequential( ){
			addColor( new Float4( 235.0f/255.0f,   0.0f/255.0f,   0.0f/255.0f, 255/255.0f ) );
			addColor( new Float4( 251.0f/255.0f, 167.0f/255.0f,  91.0f/255.0f, 255/255.0f ) );
			addColor( new Float4( 240.0f/255.0f, 235.0f/255.0f,  82.0f/255.0f, 255/255.0f ) );
			addColor( new Float4(  24.0f/255.0f, 250.0f/255.0f, 232.0f/255.0f, 255/255.0f ) );
			addColor( new Float4(   0.0f/255.0f, 135.0f/255.0f, 195.0f/255.0f, 255/255.0f ) );
			addColor( new Float4( 255.0f/255.0f, 255.0f/255.0f, 255.0f/255.0f, 255/255.0f ) );
		}
	}

	public static class RainbowSequential extends SequentialColormap {
		public RainbowSequential( ){
			addColor( new Float4(   0/255.0f,   0/255.0f, 255/255.0f, 255/255.0f ) );
			addColor( new Float4(   0/255.0f, 255/255.0f, 255/255.0f, 255/255.0f ) );
			addColor( new Float4(   0/255.0f, 255/255.0f,   0/255.0f, 255/255.0f ) );
			addColor( new Float4( 255/255.0f, 255/255.0f,   0/255.0f, 255/255.0f ) );
			addColor( new Float4( 255/255.0f,   0/255.0f,   0/255.0f, 255/255.0f ) );
		}
	}


	public static class RedBlueWhiteSequential extends SequentialColormap {
		public RedBlueWhiteSequential( ){
			addColor( new Float4( 255/255.0f,   0/255.0f,   0/255.0f, 255/255.0f ) );
			addColor( new Float4(   0/255.0f,   0/255.0f, 255/255.0f, 255/255.0f ) );
			addColor( new Float4( 255/255.0f, 255/255.0f, 255/255.0f, 255/255.0f ) );
		}
	}
	
	public static class RedBlueSequential extends SequentialColormap {

		public RedBlueSequential( ){
			clear();
			addColor( new Float4(  20,  38,  95, 255 ).divide(255.0f)  );
			addColor( new Float4(  13, 100, 137, 255 ).divide(255.0f)  );
			addColor( new Float4( 255, 255, 255, 255 ).divide(255.0f)  );
			addColor( new Float4( 177,  65,  57, 255 ).divide(255.0f)  );
			addColor( new Float4(  70,   0,   0, 255 ).divide(255.0f)  );
		}
	}

	public static class RedSequential extends SequentialColormap {
		public RedSequential( ){
			addColor( new Float4( 255/255.0f, 255/255.0f, 255/255.0f, 255.0f/255.0f )  );
			addColor( new Float4( 254/255.0f, 229/255.0f, 217/255.0f, 255.0f/255.0f )  );
			addColor( new Float4( 252/255.0f, 187/255.0f, 161/255.0f, 255.0f/255.0f ) );
			addColor( new Float4( 252/255.0f, 146/255.0f, 114/255.0f, 255.0f/255.0f ) );
			addColor( new Float4( 251/255.0f, 106/255.0f,  74/255.0f, 255.0f/255.0f ) );
			addColor( new Float4( 222/255.0f,  45/255.0f,  38/255.0f, 255.0f/255.0f ) );
			addColor( new Float4( 165/255.0f,  15/255.0f,  21/255.0f, 255.0f/255.0f ) );
		}
	}
	
	public static class GreenSequential extends SequentialColormap {
		public GreenSequential( ){
			addColor( new Float4( 255/255.0f, 255/255.0f, 255/255.0f, 255.0f/255.0f )  );
			addColor( new Float4( 237/255.0f,248/255.0f,251/255.0f, 255.0f/255.0f )  );
			addColor( new Float4( 204/255.0f,236/255.0f,230/255.0f, 255.0f/255.0f ) );
			addColor( new Float4( 153/255.0f,216/255.0f,201/255.0f, 255.0f/255.0f ) );
			addColor( new Float4( 102/255.0f,194/255.0f,164/255.0f, 255.0f/255.0f ) );
			addColor( new Float4(  44/255.0f,162/255.0f, 95/255.0f, 255.0f/255.0f ) );
			addColor( new Float4(   0/255.0f,109/255.0f, 44/255.0f, 255.0f/255.0f ) );
		}
	}

	public static class BlueSequential extends SequentialColormap {
		public BlueSequential( ){
			addColor( new Float4( 255/255.0f, 255/255.0f, 255/255.0f, 255.0f/255.0f )  );
			addColor( new Float4( 242/255.0f, 240/255.0f, 247/255.0f, 255.0f/255.0f ) );
			addColor( new Float4( 218/255.0f, 218/255.0f, 235/255.0f, 255.0f/255.0f ) );
			addColor( new Float4( 188/255.0f, 189/255.0f, 220/255.0f, 255.0f/255.0f ) );
			addColor( new Float4( 158/255.0f, 154/255.0f, 200/255.0f, 255.0f/255.0f ) );
			addColor( new Float4( 117/255.0f, 107/255.0f, 177/255.0f, 255.0f/255.0f ) );
			addColor( new Float4(  84/255.0f,  39/255.0f, 143/255.0f, 255.0f/255.0f ) );
		}
	}	

	public static class GreySequential extends SequentialColormap {
		public GreySequential( ){
			addColor( new Float4( 247/255.0f,247/255.0f,247/255.0f, 255.0f/255.0f )  );
			addColor( new Float4( 217/255.0f,217/255.0f,217/255.0f, 255.0f/255.0f ) );
			addColor( new Float4( 189/255.0f,189/255.0f,189/255.0f, 255.0f/255.0f ) );
			addColor( new Float4( 150/255.0f,150/255.0f,150/255.0f, 255.0f/255.0f ) );
			addColor( new Float4(  99/255.0f, 99/255.0f, 99/255.0f, 255.0f/255.0f ) );
			addColor( new Float4(  37/255.0f, 37/255.0f, 37/255.0f, 255.0f/255.0f ) );
		}
	}


	

}
