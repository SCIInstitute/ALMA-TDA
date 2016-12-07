package usf.saav.common.colormap;

import java.text.DecimalFormat;

import usf.saav.common.MathXv1;
import usf.saav.common.mvc.swing.TGraphics;
import usf.saav.common.range.FloatRange1D;
import usf.saav.common.types.Float4;

public class DivergentColormap implements Colormap {

    protected FloatRange1D br = new FloatRange1D( -1, 1 );
    protected double absMax = 1;
    protected SequentialColormap positive = new SequentialColormap();
    protected SequentialColormap negative = new SequentialColormap();
    double min,zero,max;
    
	DivergentColormap( ){ }

	public void setRange(FloatRange1D b){
		if( b.getRange() < 1.0e-15 ){ 
			double c = b.getCenter();
			b.expand( c + 0.0001f );
			b.expand( c - 0.0001f );
		}
		//positive.setRange( new FloatRange1D( 0, Math.max(0, b.getMaximum()) ) );
		//negative.setRange( new FloatRange1D( 0, Math.max(0,-b.getMinimum()) ) );
		positive.setRange( new FloatRange1D( 0, 1 ) );
		negative.setRange( new FloatRange1D( 0, 1 ) );
		min = b.getMinimum(); zero = 0; max = b.getMaximum();
		br = new FloatRange1D(b.getMinimum(),b.getMaximum());
		absMax = Math.max( Math.abs(b.getMinimum()), Math.abs(b.getMaximum()) );
	}
	

	public void setRange(double _min, double _zero, double _max){

		//positive.setRange( new FloatRange1D( zero, max ) );
		//negative.setRange( new FloatRange1D( zero, min ) );
		positive.setRange( new FloatRange1D( 0, 1 ) );
		negative.setRange( new FloatRange1D( 0, 1 ) );
		min = _min; zero = _zero; max = _max;
		br = new FloatRange1D(min,max);
		absMax = Math.max( Math.abs(min), Math.abs(max) );
	}
	
	public Float4 getColor( float t ) {
		if( t >= zero )
			return positive.getColor( (float)((t-zero)/(max-zero)) );
		return negative.getColor( (float)((t-zero)/(min-zero)) );
		/*
		if( t >= positive.br.getMinimum() ) 
			return positive.getColor(t);
		return negative.getColor(t);
		*/
	}
	

	@Override
	public void drawScale( TGraphics g, int loc_x, int loc_y, int w, int h ){

		g.hint( TGraphics.DISABLE_DEPTH_TEST );
		g.textSize(12);
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
			String txt = " " + v; 
			DecimalFormat d;
			if( absMax > 1000 ) d = new DecimalFormat("###.#");
			else if( absMax < 0.01 ) d = new DecimalFormat("0.###E0");
			else d = new DecimalFormat("#.###");
			txt = " " + d.format(v);
			g.text( txt, (int)(loc_x+w), (int) (loc_y+5+(float)i*step*1.01));
		}

		g.hint( TGraphics.ENABLE_DEPTH_TEST );
	}
	

	public static class OrangePurple extends DivergentColormap {
		public OrangePurple( ){
			positive.addColor( 247, 247, 247, 255 );
			positive.addColor( 254, 224, 182, 255 );
			positive.addColor( 253, 184,  99, 255 );
			positive.addColor( 224, 130,  20, 255 );
			positive.addColor( 179,  88,   6, 255 );
			positive.addColor( 127,  59,   8, 255 );

			negative.addColor( 247, 247, 247, 255 );
			negative.addColor( 216, 218, 235, 255 );
			negative.addColor( 178, 171, 210, 255 );
			negative.addColor( 128, 115, 172, 255 );
			negative.addColor(  84,  39, 136, 255 );
			negative.addColor(  45,   0,  75, 255 );
		}
	}

    
}
