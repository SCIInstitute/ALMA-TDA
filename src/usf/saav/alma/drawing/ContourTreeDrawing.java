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
package usf.saav.alma.drawing;

import java.util.Set;

import usf.saav.alma.util.CoordinateSystem;
import usf.saav.common.MathXv1;
import usf.saav.common.monitor.MonitoredInteger;
import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.mvc.swing.TGraphics;
import usf.saav.common.range.IntRange1D;
import usf.saav.mesh.Mesh;
import usf.saav.mesh.Mesh.Vertex;
import usf.saav.scalarfield.ScalarField2D;
import usf.saav.scalarfield.ScalarField3D;
import usf.saav.scalarfield.ScalarFieldND;
import usf.saav.topology.TopoTree;
import usf.saav.topology.TopoTreeNode;

// TODO: Auto-generated Javadoc
/**
 * The Class ContourTreeDrawing.
 */
public class ContourTreeDrawing extends ViewComponent.Default implements ViewComponent {

	private ScalarFieldND  sf;
	private TopoTree ct;
	private usf.saav.mesh.Mesh  cl;
	private Set<Integer>   selected;
	//private int tx = 0, ty = 0;
	//private MonitoredInteger x,y;
	//private MonitoredDouble zoom;
	private IntRange1D rx, ry;
	private CoordinateSystem cs;
	private MonitoredInteger z;
	private IntRange1D zr;

	/**
	 * Instantiates a new contour tree drawing.
	 *
	 * @param z the z
	 */
	public ContourTreeDrawing( MonitoredInteger z ){
		this.z = z;
	}

	/*
	public ContourTreeDrawing( MonitoredInteger x, MonitoredInteger y, MonitoredDouble zoom ){ 
		this.x = x;
		this.y = y;
		this.zoom = zoom;
	}
	*/
	

	/**
	 * Sets the region.
	 *
	 * @param rx the rx
	 * @param ry the ry
	 */
	public void setRegion( IntRange1D rx, IntRange1D ry ){
		this.rx = rx;
		this.ry = ry;
	}

	/**
	 * Sets the region.
	 *
	 * @param sel the new region
	 */
	public void setRegion(IntRange1D[] sel ) {
		setRegion(sel[0], sel[1]);
	}

	/**
	 * Sets the field.
	 *
	 * @param sf the sf
	 * @param ct the ct
	 * @param cl the cl
	 * @param zr the zr
	 */
	public void setField( ScalarFieldND sf, TopoTree ct, Mesh cl, IntRange1D zr ){
		this.ct = ct;
		this.sf = sf;
		this.cl = cl;
		this.zr = zr;
	}

	/*
	public void setTranslation( int x, int y ){
		this.tx = x;
		this.ty = y;
	}
	*/
	
	/**
	 * Sets the selected.
	 *
	 * @param sel the new selected
	 */
	public void setSelected( Set<Integer> sel ){
		selected = sel;
	}

	/**
	 * Gets the selected.
	 *
	 * @return the selected
	 */
	public Set<Integer> getSelected( ){
		return selected;
	}

	/**
	 * Sets the coordinate system.
	 *
	 * @param csc the new coordinate system
	 */
	public void setCoordinateSystem( CoordinateSystem csc ){
		this.cs = csc;
	}
	
	
	
	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ViewComponent.Default#draw(usf.saav.common.mvc.swing.TGraphics)
	 */
	public void draw( TGraphics g ) {
		if( !isEnabled() ) return;
		if( rx == null && ry == null ) return;

		
		float [] p0 = cs.getWindowPosition(rx.start(), ry.start() );
		float [] p1 = cs.getWindowPosition(rx.end(), ry.end() );

		float x0 = p0[0];
		float x1 = p1[0];
		float y0 = p0[1];
		float y1 = p1[1];

		g.hint( TGraphics.DISABLE_DEPTH_TEST );
		g.strokeWeight(3);
		g.stroke(0);
		g.noFill();
		g.rect( x0, y0, (x1-x0), (y1-y0) );
		g.hint( TGraphics.ENABLE_DEPTH_TEST );


		if( sf == null || cl == null || ct == null ) return;

		g.hint( TGraphics.DISABLE_DEPTH_TEST );

		g.strokeWeight(1);
		g.stroke(0);

			float maxPersistence = ct.getMaxPersistence();

			for(int i = 0; i < ct.size(); i++){
				if( !ct.isActive(i) ) continue;
				TopoTreeNode n = ct.getNode(i);
				float per = n.getPersistence();
				if( Float.isNaN(per) ){
					per = -1;
				}

				float [] pos = getComponentMidpoint( cl.get(n.getPosition()), sf );

				if( selected.contains(i) ) g.strokeWeight(3);
				float size = MathXv1.lerp(4,10,per/maxPersistence);
				if( Float.isNaN(size) ) size = 4;
				if( size > 10 ) size = 10;


				switch( n.getType() ){
				case LEAF_MIN:   g.fill(100,100,255); break;
				case LEAF_MAX:   g.fill(100,100,255); break;
				case MERGE:  g.fill(255,255,  0); break;
				case SPLIT:  g.fill(255,  0,255); break;
				default:     g.fill(255,  0,  0);
							 size = 10;
							 break;
				}

				if( z.get() != (pos[2]+zr.start()) ) g.fill(200);

				float [] pp = cs.getWindowPosition( rx.start()+pos[0] , ry.start()+pos[1] );

				float px = pp[0];
				float py = pp[1];
				

				if( winX.inRange( (float)px ) && winY.inRange( (float)py ) ){
					g.ellipse( (float)px, (float)py, size, size, (int)(size*1.5) );
				}

				if( selected.contains(i) ) g.strokeWeight(1);

			}
			
			g.hint( TGraphics.ENABLE_DEPTH_TEST );

	}
	
	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ViewComponent.Default#drawLegend(usf.saav.common.mvc.swing.TGraphics)
	 */
	public void drawLegend( TGraphics g  ){
		if( !isEnabled() ) return;
		if( sf == null || cl == null || ct == null ) return;

		float size = 25;
		g.hint( TGraphics.DISABLE_DEPTH_TEST );
		
		g.stroke(0);
		g.fill(255,255,255,240);
		g.rect( winX.end()-10.5f*size, winY.end()-8.5f*size, 10.0f*size, 8.0f*size );

		g.fill(100,100,255); g.rect( winX.end()-10*size, winY.end()-8.0f*size, size, size );
		g.fill(255,255,  0); g.rect( winX.end()-10*size, winY.end()-6.5f*size, size, size );
		g.fill(255,  0,255); g.rect( winX.end()-10*size, winY.end()-5.0f*size, size, size );
		g.fill(200,200,200); g.rect( winX.end()-10*size, winY.end()-3.5f*size, size, size );
		g.fill(255,  0,  0); g.rect( winX.end()-10*size, winY.end()-2.0f*size, size, size );

		g.fill(0);
		g.textSize((int)size);
		g.textAlign( TGraphics.LEFT, TGraphics.TOP );
		g.text("Leaf",			(int)(winX.end()-8.5f*size), (int)(winY.end()-9.0f*size+1.0f*size));
		g.text("Merge",			(int)(winX.end()-8.5f*size), (int)(winY.end()-7.5f*size+1.0f*size));
		g.text("Split",			(int)(winX.end()-8.5f*size), (int)(winY.end()-6.0f*size+1.0f*size));
		g.text("Out of Layer",  (int)(winX.end()-8.5f*size), (int)(winY.end()-4.5f*size+1.0f*size));
		g.text("Unknown/Error", (int)(winX.end()-8.5f*size), (int)(winY.end()-3.0f*size+1.0f*size));

		g.hint( TGraphics.ENABLE_DEPTH_TEST );

	}
	
	
	/**
	 * Gets the component midpoint.
	 *
	 * @param c the c
	 * @param sf the sf
	 * @return the component midpoint
	 */
	private static float [] getComponentMidpoint( Vertex c, ScalarFieldND sf ){
		if( sf instanceof ScalarField2D ){
			return getComponentMidpoint( c, ((ScalarField2D)sf).getWidth(), ((ScalarField2D)sf).getHeight() );
		}
		if( sf instanceof ScalarField3D ){
			return getComponentMidpoint( c, ((ScalarField3D)sf).getWidth(), ((ScalarField3D)sf).getHeight() );
		}
		return null;
	}

	/**
	 * Gets the component midpoint.
	 *
	 * @param c the c
	 * @param width the width
	 * @param height the height
	 * @return the component midpoint
	 */
	private static float [] getComponentMidpoint( Vertex c, int width, int height ){
		float retX = 0, retY = 0, retZ = 0;
		int cnt = 0;
		int slc = width*height;
		for( int p : c.positions() ){
			retX += (p%slc)%width;
			retY += (p%slc)/width;
			retZ += p/slc;
			cnt++;
		}
		return new float[]{retX/cnt,retY/cnt,retZ/cnt};
	}
	
	
}
