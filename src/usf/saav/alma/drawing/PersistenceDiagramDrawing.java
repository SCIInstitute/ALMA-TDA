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

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import usf.saav.alma.algorithm.topology.PersistenceSet;
import usf.saav.common.MathX;
import usf.saav.common.monitor.MonitoredFloat;
import usf.saav.common.mvc.ControllerComponent;
import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.range.FloatRange1D;
import usf.saav.common.types.Float2;

public class PersistenceDiagramDrawing extends ViewComponent.Default implements ControllerComponent, ViewComponent {

	Vector<Float2> splinePoints = new Vector<Float2>( );

	public PersistenceDiagramDrawing() { }

	PersistenceSet curr = null;

	public void setParameterizations( PersistenceSet curr, PersistenceSet ... pd ){

		if( curr == null || pd == null || pd.length == 0 ){
			this.pd = null;
			return;
		}

		this.curr = curr;
		this.pd = pd;

		bd_range = new FloatRange1D( curr.getBirth(0) );

		for( PersistenceSet _pd : pd ){
			for(int i = 0; i < _pd.size(); i++){
				bd_range.expand( _pd.getBirth(i) );
				bd_range.expand( _pd.getDeath(i) );
			}
		}
		simp = curr.getPersistentSimplification();
		splinePoints.add( new Float2(0,0) );
		splinePoints.add( new Float2(getWidth(), getHeight()) );
	}

	public void setSelected( Set<Integer> sel ){
		selected = sel;
	}

	public Set<Integer> getSelected( ){
		return selected;
	}

	public void setPosition( int x0, int y0, int size_x, int size_y ){
		super.setPosition( x0, y0, Math.min(size_x, size_y), Math.min(size_x, size_y) );
	}

	public boolean isActive(){
		return ( pd != null );
	}

	public void draw( PGraphics g ){
		if( !isEnabled() ) return;
		if( !isActive() ) return;

		g.hint( PConstants.DISABLE_DEPTH_TEST );

		g.strokeWeight(2);
		g.stroke(0);
		g.fill(255);
		g.rect( winX.start(), winY.start(),winX.length(), winY.length() );

		float px0 = PApplet.lerp( winX.end()-5, winX.start()+5, 1 );
		float py0 = PApplet.lerp( winY.end()-5, winY.start()+5, (float)(simp/bd_range.getRange()) );
		float px1 = PApplet.lerp( winX.end()-5, winX.start()+5, (float)(simp/bd_range.getRange()) );
		float py1 = PApplet.lerp( winY.end()-5, winY.start()+5, 1 );

		g.strokeWeight(5);
		g.stroke(150,0,0);
		g.line( px0,py0,px1,py1 );

		g.strokeWeight(1);
		g.stroke(0);
		g.fill(100);

		for( PersistenceSet _pd : pd ){
			for(int i = 0; i < _pd.size(); i++){

				float x = PApplet.lerp( winX.start()+5, winX.end()-5, (float)bd_range.getNormalized( _pd.getBirth(i) ) );
				float y = PApplet.lerp( winY.end()-5, winY.start()+5, (float)bd_range.getNormalized( _pd.getDeath(i) ) );

				if( selected.contains(i) ) g.strokeWeight( 3 );

				if( _pd.isActive(i) )
					g.fill(100);
				else
					g.fill(200);
				g.ellipse( x, y, 7, 7 );

				if( selected.contains(i) ) g.strokeWeight( 1 );
			}
		}

		g.hint( PConstants.ENABLE_DEPTH_TEST );
	}


	private Set<Integer> selectClosestToMouse( int mouseX, int mouseY ){
		Set<Integer> ret = new HashSet<Integer>();
		if( !winX.inRange(mouseX) || !winY.inRange(mouseY) )
			return ret;

		float curD = 3*3;
		for(int i = 0; i < curr.size(); i++){

			float x = PApplet.lerp( winX.start()+5, winX.end()-5, (float)bd_range.getNormalized( curr.getBirth(i) ) );
			float y = PApplet.lerp( winY.end()-5, winY.start()+5, (float)bd_range.getNormalized( curr.getDeath(i) ) );

			float dx = x - mouseX;
			float dy = y - mouseY;

			if( (dx*dx+dy*dy) < curD ){
				ret.add(i);
			}
		}
		return ret;
	}

	@Override public boolean keyPressed(char key) { return false; }
	@Override public boolean mouseWheel(int mouseX, int mouseY, int count) { return false; }

	@Override public boolean mouseMoved(int mouseX, int mouseY) {
		if( !isEnabled() ) return false;
		if( !isActive() ) return false;
		selected = selectClosestToMouse( mouseX, mouseY );
		return false;
	}

	@Override
	public boolean mouseDragged(int mouseX, int mouseY) {
		if( !isEnabled() ) return false;

		if( modifySimpl ){
			simp = ( mouseY + mouseX-winX.start() - winY.end() - 5 )*(float)bd_range.getRange()/( winY.start() - winY.end() + 10 );
			simp = MathX.clamp( simp, 0, (float)bd_range.getMaximum() );
			for( PersistenceSet _pd : pd ){
				_pd.setPersistentSimplification( simp );
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean mousePressed(int mouseX, int mouseY) {
		if( !isEnabled() ) return false;
		if( !isActive() ) return false;
		if( !winX.inRange(mouseX) || !winY.inRange(mouseY) ) return false;

		modifySimpl = true;
		return true;
	}

	@Override
	public boolean mouseDoubleClick(int mouseX, int mouseY) {
		if( !isEnabled() ) return false;
		if( !isActive() ) return false;
		if( !winX.inRange(mouseX) || !winY.inRange(mouseY) ) return false;

		return false;
	}

	@Override
	public boolean mouseReleased() {
		if( !isEnabled() ) return false;
		if( modifySimpl ){
			modifySimpl = false;
			simplification.set(simp);
			return true;
		}
		return false;
	}

	public void addPersistentSimplificationCallback( Object obj, String func_name ){
		simplification.addMonitor(obj, func_name);
	}

	private MonitoredFloat simplification = new MonitoredFloat( 0 );
	private PersistenceSet [] pd = null;
	private Set<Integer> selected = new HashSet<Integer>();
	private float simp;
	private boolean modifySimpl = false;
	private FloatRange1D bd_range;
}
