package usf.saav.common.colormap;

import usf.saav.common.mvc.swing.TGraphics;

public interface ColormapV2 extends Colormap {
	    
	public void drawScale( TGraphics g, int loc_x, int loc_y, int w, int h );
    
    
    public static abstract class VectorColormap extends Colormap.VectorColormap implements ColormapV2 { }

    public static abstract class ArrayColormap extends Colormap.ArrayColormap implements ColormapV2 {
        public ArrayColormap( int cnt ){ super(cnt); }
    }
    
}

