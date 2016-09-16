package usf.saav.common;

import usf.saav.common.mvc.swing.TGraphics;
import usf.saav.common.types.Float3;

public class GraphicsX {

	public static void drawPoint( TGraphics g, Float3 p, float size ) {
		g.translate(p.x, p.y, p.z);
		g.sphere(size);
		g.translate(-p.x, -p.y, -p.z);
	}

	public static void drawTriad( TGraphics g, float view_rotX, float view_rotY ){
		for(int pass = 0; pass < 2; pass++){
			float cur_size = (pass==0)?1.0f:1.3f;
			g.pushMatrix();
				g.translate(g.width/13, g.height*12/13, (pass==0)?0:-100.0f );
				g.rotateY(view_rotY);  
				g.rotateX(view_rotX); 
	
				g.scale(80,-80,-80);
				g.strokeWeight( 0.25f*cur_size );
				g.noFill();
				g.stroke( 0,0,0, 255 );
				g.beginShape(TGraphics.LINES);
				if( pass == 0 ) g.stroke( 255,0,0, 255 );
				g.vertex( 0, 0, 0 );
				g.vertex( 1, 0, 0 );
				if( pass == 0 ) g.stroke( 0, 255,0, 255 );
				g.vertex( 0, 0, 0 );
				g.vertex( 0, 1, 0 );
				if( pass == 0 ) g.stroke( 0,0,255, 255 );
				g.vertex( 0, 0, 0 );
				g.vertex( 0, 0, 1 );
				g.endShape();
	
				g.sphereDetail(8);
				g.noStroke();
				g.fill(0, 255);
				if( pass == 0 ) g.fill(255, 0, 0, 255);
				drawPoint( g, new Float3(0.01,0,0), 0.125f*cur_size );
				drawPoint( g, new Float3(1,0,0), 0.125f*cur_size );
				if( pass == 0 ) g.fill(0, 255, 0, 255);
				drawPoint( g, new Float3(0,0.01,0), 0.125f*cur_size );
				drawPoint( g, new Float3(0,1,0), 0.125f*cur_size );
				if( pass == 0 ) g.fill(0, 0, 255, 255);
				drawPoint( g, new Float3(0,0,0.01), 0.125f*cur_size );
				drawPoint( g, new Float3(0,0,1), 0.125f*cur_size );

			g.popMatrix();
		}
	}

}
