package usf.saav.alma.drawing;

import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.mvc.swing.TGraphics;
import usf.saav.common.range.FloatRange1D;
import usf.saav.common.spline.LinearSpline;
import usf.saav.common.spline.Spline;
import usf.saav.common.types.Float2;
import usf.saav.scalarfield.ScalarField1D;

public class SpectralLineDrawing  extends ViewComponent.Default implements ViewComponent { 

	Spline line = null;
	
	public SpectralLineDrawing( ){
	}
	
	public void setData( final ScalarField1D sf ){
		if( sf == null ){
			line = null;
		}
		else{
			line = new LinearSpline() {
				ScalarField1D data = sf;
				@Override public int size() { return data.getSize(); }
				@Override public Float2 getControlPoint(int i) { return new Float2((float)i/(float)data.getSize(),data.getValue(i)); }
			};
		}
	}
		

	@Override
	public void update( ) {
		if( !isEnabled() ) return;
		if( line == null ) return;

		line.setPosition( this.getPosition() );
		line.update();
	}

	
	@Override
	public void draw(TGraphics g) {
		if( !isEnabled() ) return;
		if( line == null ) return;

		g.hint( TGraphics.DISABLE_DEPTH_TEST );
		
		g.strokeWeight(2);
		g.stroke(0);
		g.fill(255);
		g.rect( winX.start(), winY.start(), winX.length(), winY.length() );
		
		FloatRange1D yr = line.getRangeY( );
		line.setDrawingOffset( 0, (float) -yr.getMinimum() );
		line.setDrawingScale( 1, (1.0f/(float)yr.getRange()) );
		line.draw(g);
		
		g.hint( TGraphics.ENABLE_DEPTH_TEST );

		
	}

	@Override public void drawLegend(TGraphics g) { }
	
	


}
