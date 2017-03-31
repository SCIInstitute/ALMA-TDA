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
package usf.saav.common.colormap;

import java.util.Vector;

import usf.saav.common.mvc.swing.TGraphics;
import usf.saav.common.types.Float3;
import usf.saav.common.types.Float4;

public interface Colormap {
	    
	    public Float4 getColor( float t );
	    public void   drawScale( TGraphics g, int loc_x, int loc_y, int w, int h, int textSize );
    
    
    public static abstract class VectorColormap implements Colormap {
        protected Vector< Float4 > cols = new Vector< Float4 >();
        
        public void clear(){ cols.clear(); }

        public void addColor( Float4 c ){ cols.add( c ); }
        public void addColor( Float3 c ){ cols.add( new Float4(c.x,c.y,c.z,1) ); }
        public void addColor( int r, int g, int b, int a ){ cols.add( new Float4( (float)r/255.0f,(float)g/255.0f,(float)b/255.0f,(float)a/255.0f) ); }
        public void addColor( float r, float g, float b, float a ){ cols.add( new Float4( r,g,b,a ) ); }
        
        public int  getColorCount(){ return (int)cols.size(); }
        
    }

    public static abstract class ArrayColormap implements Colormap {
        protected Float4 [] cols;
        
        public ArrayColormap( int cnt ){ cols = new Float4[cnt]; }
        
        protected void setColor( int idx, Float4 c ){ cols[idx] = c; }
        protected void setColor( int idx, Float3 c ){ cols[idx] = new Float4(c.x,c.y,c.z,1); }
        protected void setColor( int idx, int r, int g, int b, int a ){ cols[idx] = new Float4( (float)r/255.0f,(float)g/255.0f,(float)b/255.0f,(float)a/255.0f); }
        protected void setColor( int idx, float r, float g, float b, float a ){ cols[idx] = new Float4( r,g,b,a ); }
        
        public int  getColorCount(){ return (int)cols.length; }
        
    }
    
}

