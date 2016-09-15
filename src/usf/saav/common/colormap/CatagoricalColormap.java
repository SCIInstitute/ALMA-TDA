package usf.saav.common.colormap;

import usf.saav.common.mvc.swing.TGraphics;
import usf.saav.common.types.Float4;

public class CatagoricalColormap extends Colormap.VectorColormap {

	public CatagoricalColormap( ){ }

	public Float4 getColor( float t ) { 
		while(t<0){t+=cols.size();} 
		return cols.get( ((int)t)%cols.size() ); 
	}

	public Float4 getColor( float t, float alpha_255_override ) {
		Float4 ret = getColor(t);
		ret.w = alpha_255_override/255.0f;
		return ret;
	}	


	@Override
	public void drawScale(TGraphics g, int loc_x, int loc_y, int w, int h) {
		/*
	    glDisable(GL_DEPTH_TEST);
	    glBegin(GL_QUAD_STRIP);
	        for(int i = 0; i < (int)cols.size(); i++){
	            glColor4fv( GetColor( i ).data );
	            glVertex3f( u_min, lerp( v_min, v_max, (float)i/(float)cols.size() ), 0.0f );
	            glVertex3f( u_max, lerp( v_min, v_max, (float)i/(float)cols.size() ), 0.0f );
	        }
	    glEnd();

	    glColor3f(0,0,0);
	    glBegin(GL_LINE_LOOP);
	        glVertex3f( u_min, v_min, 0.0f );
	        glVertex3f( u_max, v_min, 0.0f );
	        glVertex3f( u_max, v_max, 0.0f );
	        glVertex3f( u_min, v_max, 0.0f );
	    glEnd();
		 */
		/*
	    if( font ){
	        for(int i = 0; i <= 10; i+=2){
	            glPushMatrix();
	                float t = lerp( GetRangeMinimum( ), GetRangeMaximum(), (float)i/10.0f );
	                glTranslatef(u_max+0.005f, lerp( v_min, v_max, (float)i/10.0f ), 0.0f);
	                glScalef(0.03f,0.03f*window_aspect,1.0f);
	                glTranslatef(0, lerp( 0.1f, -0.8f, (float)i/10.0f), 0);
	                font->Printf( "%f", t );
	            glPopMatrix();
	        }
	    }
		 */
	}



	public static class DefaultCatagorical extends CatagoricalColormap {

		public DefaultCatagorical( float alpha ){
			initializeMap( (int)(alpha * 255.0f) );
		}

		public DefaultCatagorical( int alpha ){
			initializeMap( alpha );
		}
		public DefaultCatagorical( ){
			initializeMap( 255 );
		}

		private void initializeMap( int alpha ){
			clear();
			addColor( new Float4( 141, 211, 199, alpha ).divide(255.0f) );
			addColor( new Float4( 255, 255, 179, alpha ).divide(255.0f) );
			addColor( new Float4( 190, 186, 218, alpha ).divide(255.0f) );
			addColor( new Float4( 251, 128, 114, alpha ).divide(255.0f) );
			addColor( new Float4( 128, 177, 211, alpha ).divide(255.0f) );
			addColor( new Float4( 253, 180,  98, alpha ).divide(255.0f) );
			addColor( new Float4( 179, 222, 105, alpha ).divide(255.0f) );
			addColor( new Float4( 252, 205, 229, alpha ).divide(255.0f) );
			addColor( new Float4( 217, 217, 217, alpha ).divide(255.0f) );
			addColor( new Float4( 188, 128, 189, alpha ).divide(255.0f) );
			addColor( new Float4( 204, 235, 197, alpha ).divide(255.0f) );
			addColor( new Float4( 255, 237, 111, alpha ).divide(255.0f) );		
		}
	}
	
	public static class DefaultCatagoricalNoGray extends CatagoricalColormap {

		public DefaultCatagoricalNoGray( float alpha ){
			initializeMap( (int)(alpha * 255.0f) );
		}

		public DefaultCatagoricalNoGray( int alpha ){
			initializeMap( alpha );
		}
		public DefaultCatagoricalNoGray( ){
			initializeMap( 255 );
		}

		private void initializeMap( int alpha ){
			clear();
			addColor( new Float4( 141, 211, 199, alpha ).divide(255.0f) );
			addColor( new Float4( 255, 255, 179, alpha ).divide(255.0f) );
			addColor( new Float4( 190, 186, 218, alpha ).divide(255.0f) );
			addColor( new Float4( 251, 128, 114, alpha ).divide(255.0f) );
			addColor( new Float4( 128, 177, 211, alpha ).divide(255.0f) );
			addColor( new Float4( 253, 180,  98, alpha ).divide(255.0f) );
			addColor( new Float4( 179, 222, 105, alpha ).divide(255.0f) );
			addColor( new Float4( 252, 205, 229, alpha ).divide(255.0f) );
			addColor( new Float4( 188, 128, 189, alpha ).divide(255.0f) );
			addColor( new Float4( 204, 235, 197, alpha ).divide(255.0f) );
			addColor( new Float4( 255, 237, 111, alpha ).divide(255.0f) );		
		}
	}	


	public static class DefaultCohomology extends CatagoricalColormap {
		public DefaultCohomology( ){
			addColor( new Float4(141,211,199,255).divide(255.0f) );
			addColor( new Float4(255,255,179,255).divide(255.0f) );
			addColor( new Float4(190,186,218,255).divide(255.0f) );
			addColor( new Float4(251,128,114,255).divide(255.0f) );
			addColor( new Float4(128,177,211,255).divide(255.0f) );
			addColor( new Float4(253,180, 98,255).divide(255.0f) );
			addColor( new Float4(179,222,105,255).divide(255.0f) );
			addColor( new Float4(252,205,229,255).divide(255.0f) );
			addColor( new Float4(188,128,189,255).divide(255.0f) );
			addColor( new Float4(102,194,165,255).divide(255.0f) );
			addColor( new Float4(141,160,203,255).divide(255.0f) );
			addColor( new Float4(255,217, 47,255).divide(255.0f) );
			addColor( new Float4(166,216, 84,255).divide(255.0f) );
			addColor( new Float4(231,138,195,255).divide(255.0f) );
		}
	}	
	
	

	public class OrangePurpleColormap extends CatagoricalColormap {
		public OrangePurpleColormap( ){
			clear();
			addColor( new Float4(  84,  39, 136, 255 ).divide(255.0f) );
			addColor( new Float4( 128, 115, 172, 255 ).divide(255.0f) );
			addColor( new Float4( 178, 171, 210, 255 ).divide(255.0f) );
			addColor( new Float4( 216, 218, 235, 255 ).divide(255.0f) );
			addColor( new Float4( 254, 224, 182, 255 ).divide(255.0f) );
			addColor( new Float4( 253, 184,  99, 255 ).divide(255.0f) );
			addColor( new Float4( 224, 130,  20, 255 ).divide(255.0f) );
			addColor( new Float4( 179,  88,   6, 255 ).divide(255.0f) );
		}
	
		public Float4 getColor( float t ) {
			return super.getColor( t * 8.0f ); 
		}
	}
	
	
	public static class PathlineColormap extends CatagoricalColormap {
		public PathlineColormap( ){
		    addColor( new Float4(251, 180, 174, 255 ).divide(255.0f) );   
		    addColor( new Float4(179, 205, 227, 255 ).divide(255.0f) );
		    addColor( new Float4(204, 235, 197, 255 ).divide(255.0f) );
		    addColor( new Float4(222, 203, 228, 255 ).divide(255.0f) );
		    addColor( new Float4(254, 217, 166, 255 ).divide(255.0f) );
		    addColor( new Float4(255, 255, 204, 255 ).divide(255.0f) );
		    addColor( new Float4(229, 216, 189, 255 ).divide(255.0f) );
		    addColor( new Float4(253, 218, 236, 255 ).divide(255.0f) );
			
		}
	}


	


}
