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
package usf.saav.alma.drawing.VolumeRendering;

import usf.saav.alma.drawing.HistogramDrawing;
import usf.saav.common.MathX;
import usf.saav.common.monitoredvariables.MonitoredTrigger;
import usf.saav.common.mvc.ControllerComponent;
import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.mvc.swing.TGraphics;
import usf.saav.common.spline.LinearXCatmullYSpline;
import usf.saav.common.spline.Spline;
import usf.saav.common.spline.Spline.Constraint;
import usf.saav.common.types.Float2;
import usf.saav.common.types.Float4;
import usf.saav.common.types.Pair;

// TODO: Auto-generated Javadoc
/**
 * The Class InteractiveTF1D.
 */
public class InteractiveTF1D extends HistogramDrawing implements ControllerComponent, ViewComponent, TransferFunction1D { // extends ControllerComponent.Default implements ControllerComponent, ViewComponent, TransferFunction1D {

	Float4 [] tf = new Float4[64];

	MonitoredTrigger modifiedCB = new MonitoredTrigger( );
	
	//Histogram1D histogram;
	//Gaussian distro;
	
	Spline red = new LinearXCatmullYSpline.Default( new Float2(0.0,0.0),  new Float2(0.60,0.1), new Float2(0.75,0.5),  new Float2(0.90,0.8), new Float2(1.0,0.95) );
	Spline grn = new LinearXCatmullYSpline.Default( new Float2(0.0,0.0),  new Float2(0.35,0.1), new Float2(0.50,0.95), new Float2(0.65,0.1), new Float2(1.0,0.0) );
	Spline blu = new LinearXCatmullYSpline.Default( new Float2(0.0,0.95), new Float2(0.10,0.8), new Float2(0.25,0.5),  new Float2(0.40,0.1), new Float2(1.0,0.0) );
	Spline alp = new LinearXCatmullYSpline.Default( new Float2(0.0,0.4),  new Float2(0.05,0.4), new Float2(0.15,0.2),  new Float2(0.2,0.05), new Float2(0.8,0.05), new Float2(0.85,0.2), new Float2(0.95,0.4), new Float2(1.0,0.4) );
	Constraint spline_constraint = new Constraint() {
		public void constrainFirstPoint( Float2 p ){
			p.x = 0;
			p.y = MathX.clamp( p.y, 0, 1 );
		}
		public void constrainLastPoint( Float2 p ){
			p.x = 1;
			p.y = MathX.clamp( p.y, 0, 1 );
		}
		public void constrainIntermediatePoint( Float2 p ){
			p.x = MathX.clamp( p.x, 0, 1 );
			p.y = MathX.clamp( p.y, 0, 1 );
		}
		public void constrainInterpolatedPoint( Float2 p ){
			p.x = MathX.clamp( p.x, 0, 1 );
			p.y = MathX.clamp( p.y, 0, 1 );
		}
	};

	/**
	 * Instantiates a new interactive TF 1 D.
	 */
	public InteractiveTF1D( ){
		super( 32 );
		show_distribution = false;
		show_mean		  = false;
		show_scales		  = false;
		for( int i = 0; i < tf.length; i++){
			tf[i] = new Float4(0,0,0,0);
		}
		red.setConstraint( spline_constraint );
		grn.setConstraint( spline_constraint );
		blu.setConstraint( spline_constraint );
		alp.setConstraint( spline_constraint );
		buildTF( );
	}

	/* (non-Javadoc)
	 * @see usf.saav.alma.drawing.VolumeRendering.TransferFunction1D#size()
	 */
	@Override
	public int size() {
		return tf.length;
	}

	/* (non-Javadoc)
	 * @see usf.saav.alma.drawing.VolumeRendering.TransferFunction1D#get(int)
	 */
	@Override
	public Float4 get(int idx) {
		return tf[idx];
	}

	private void buildTF( ){
		for( int i = 0; i < tf.length; i++){
			float t = (float)i/(float)tf.length;
			float r = MathX.clamp( red.interpolate( t ).y, 0, 1 );
			float g = MathX.clamp( grn.interpolate( t ).y, 0, 1 );
			float b = MathX.clamp( blu.interpolate( t ).y, 0, 1 );
			float a = (float)(Math.pow(10,alp.interpolate(t).y)-1)/9;
			tf[i].set( r,g,b,a );
		}
		modifiedCB.trigger();
	}

	/* (non-Javadoc)
	 * @see usf.saav.alma.drawing.VolumeRendering.TransferFunction1D#getOffset()
	 */
	@Override
	public float getOffset() {
		return (float) -range.getMinimum();
	}

	/* (non-Javadoc)
	 * @see usf.saav.alma.drawing.VolumeRendering.TransferFunction1D#getScale()
	 */
	@Override
	public float getScale() {
		return (float)(1.0/range.getRange());
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.BasicComponent.Default#update()
	 */
	@Override
	public void update( ) {
		red.setPosition( this.getPosition() );
		red.update();
		grn.setPosition( this.getPosition() );
		grn.update();
		blu.setPosition( this.getPosition() );
		blu.update();
		alp.setPosition( this.getPosition() );
		alp.update();
	}

	/* (non-Javadoc)
	 * @see usf.saav.alma.drawing.HistogramDrawing#draw(usf.saav.common.mvc.swing.TGraphics)
	 */
	@Override
	public void draw(TGraphics g) {
		if( !isEnabled() ) return;
		if( range == null || histogram == null ) return;

		super.draw(g);

		red.setColor(255,  0,  0); red.draw(g);
		grn.setColor(  0,255,  0); grn.draw(g);
		blu.setColor(  0,  0,255); blu.draw(g);
		alp.setColor(  0,  0,  0); alp.draw(g);
	}

	boolean haveControl = false;
	Pair<Float2,Float> sel = new Pair<Float2,Float>(null,3.0f);

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ViewComponent.Default#drawLegend(usf.saav.common.mvc.swing.TGraphics)
	 */
	@Override public void drawLegend(TGraphics g) { }
	
	
	

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ControllerComponent#mouseDragged(int, int)
	 */
	@Override public boolean mouseDragged(int mouseX, int mouseY) {
		if( !isEnabled() ) return false;
		if( haveControl ){
			if( sel.getFirst() != null ){
				sel.getFirst().x =   winX.location( mouseX );
				sel.getFirst().y = 1-winY.location( mouseY );
			}
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ControllerComponent#mousePressed(int, int)
	 */
	@Override public boolean mousePressed(int mouseX, int mouseY) {
		if( !isEnabled() ) return false;
		haveControl = false;
		if( inRange(mouseX,mouseY) ){
			haveControl = true;
			sel = new Pair<Float2,Float>(null,10.0f);
			Pair<Float2,Float> s;
			s = red.selectPoint( mouseX, mouseY );
			if( s.getSecond() < sel.getSecond() ) sel = s;
			s = grn.selectPoint( mouseX, mouseY );
			if( s.getSecond() < sel.getSecond() ) sel = s;
			s = blu.selectPoint( mouseX, mouseY );
			if( s.getSecond() < sel.getSecond() ) sel = s;
			s = alp.selectPoint( mouseX, mouseY );
			if( s.getSecond() < sel.getSecond() ) sel = s;
		}
		return haveControl;
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ControllerComponent#mouseDoubleClick(int, int)
	 */
	@Override public boolean mouseDoubleClick(int mouseX, int mouseY) {
		if( !isEnabled() ) return false;
		return inRange(mouseX,mouseY);
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ControllerComponent#mouseReleased()
	 */
	@Override public boolean mouseReleased() {
		if( !isEnabled() ) return false;
		if( haveControl ){
			buildTF( );
			haveControl = false;
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ControllerComponent#keyPressed(char)
	 */
	@Override
	public boolean keyPressed(char key) {
		return false;
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ControllerComponent#mouseMoved(int, int)
	 */
	@Override
	public boolean mouseMoved(int mouseX, int mouseY) {
		return false;
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ControllerComponent#mouseWheel(int, int, float)
	 */
	@Override
	public boolean mouseWheel(int mouseX, int mouseY, float count) {
		return false;
	}
	
	

	public void addModifiedCallback( Object obj, String func ){
		modifiedCB.addMonitor( obj,  func );
	}
	
	
}
