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

import usf.saav.alma.algorithm.mesh.Mesh;
import usf.saav.alma.algorithm.topology.AugmentedJoinTreeNode;
import usf.saav.alma.algorithm.topology.PersistenceSet;
import usf.saav.alma.data.ScalarFieldND;
import usf.saav.alma.util.CoordinateSystem;
import usf.saav.common.MathX;
import usf.saav.common.monitor.MonitoredInteger;
import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.mvc.swing.TGraphics;
import usf.saav.common.range.IntRange1D;

public class ContourTreeDrawing extends ViewComponent.Default implements ViewComponent {

	private ScalarFieldND  sf;
	private PersistenceSet ct;
	private Mesh  cl;
	private Set<Integer>   selected;
	//private int tx = 0, ty = 0;
	//private MonitoredInteger x,y;
	//private MonitoredDouble zoom;
	private IntRange1D rx, ry;
	private CoordinateSystem cs;
	private MonitoredInteger z;
	private IntRange1D zr;

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
	

	public void setRegion( IntRange1D rx, IntRange1D ry ){
		this.rx = rx;
		this.ry = ry;
	}

	public void setRegion(IntRange1D[] sel ) {
		setRegion(sel[0], sel[1]);
	}

	public void setField( ScalarFieldND sf, PersistenceSet ct, Mesh cl, IntRange1D zr ){
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
	
	public void setSelected( Set<Integer> sel ){
		selected = sel;
	}

	public Set<Integer> getSelected( ){
		return selected;
	}

	public void setCoordinateSystem( CoordinateSystem csc ){
		this.cs = csc;
	}
	
	
	
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
				AugmentedJoinTreeNode n = ct.getNode(i);
				float per = n.getPersistence();
				if( Float.isNaN(per) ){
					per = -1;
				}

				float [] pos = Mesh.getComponentMidpoint( cl.get(n.getLocation()), sf );

				if( selected.contains(i) ) g.strokeWeight(3);
				float size = MathX.lerp(4,10,per/maxPersistence);
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

				if( winX.inRange( (float)px ) && winY.inRange( (float)py ) )
					g.ellipse( (float)px, (float)py, size, size, (int)(size*1.5) );

				if( selected.contains(i) ) g.strokeWeight(1);

			}
			
			g.hint( TGraphics.ENABLE_DEPTH_TEST );

	}
	
	public void drawLegend( TGraphics g  ){
		if( !isEnabled() ) return;
		if( sf == null || cl == null || ct == null ) return;

		g.hint( TGraphics.DISABLE_DEPTH_TEST );
		
		g.stroke(0);
		g.fill(255,255,255,240);
		g.rect( winX.end()-105, winY.end()-85, 100, 80);

		g.fill(100,100,255); g.rect( winX.end()-100, winY.end()-80, 10, 10 );
		g.fill(255,255,  0); g.rect( winX.end()-100, winY.end()-65, 10, 10 );
		g.fill(255,  0,255); g.rect( winX.end()-100, winY.end()-50, 10, 10 );
		g.fill(200,200,200); g.rect( winX.end()-100, winY.end()-35, 10, 10 );
		g.fill(255,  0,  0); g.rect( winX.end()-100, winY.end()-20, 10, 10 );

		g.fill(0);
		g.textSize(10);
		g.textAlign( TGraphics.LEFT, TGraphics.TOP );
		g.text("Leaf",			winX.end()-85, winY.end()-90+10);
		g.text("Merge",			winX.end()-85, winY.end()-75+10);
		g.text("Split",			winX.end()-85, winY.end()-60+10);
		g.text("Out of Layer",  winX.end()-85, winY.end()-45+10);
		g.text("Unknown/Error", winX.end()-85, winY.end()-30+10);

		g.hint( TGraphics.ENABLE_DEPTH_TEST );

	}
}
