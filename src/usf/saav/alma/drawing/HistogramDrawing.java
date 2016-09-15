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

import processing.core.PConstants;
import processing.core.PGraphics;
import usf.saav.alma.data.ScalarField1D;
import usf.saav.alma.data.ScalarField2D;
import usf.saav.alma.data.ScalarField3D;
import usf.saav.alma.data.ScalarFieldND;
import usf.saav.common.MathX;
import usf.saav.common.MathXv1.Gaussian;
import usf.saav.common.histogram.Histogram1D;
import usf.saav.common.mvc.ViewComponent;
import usf.saav.common.range.FloatRange1D;

public class HistogramDrawing extends ViewComponent.Default implements ViewComponent {

	protected FloatRange1D range;
	protected Histogram1D	 histogram;
	private Gaussian	 norm_dist;
	private int			 binCount;
	private long		 binMax = 0;
	
	protected boolean log_scale			= false;
	protected boolean show_3sigma		= true;
	protected boolean show_distribution = true;
	protected boolean show_mean			= true;
	protected boolean show_scales		= true;

	public HistogramDrawing( int bins ){ 
		binCount = bins;
	}

	public HistogramDrawing( int bins, boolean verbose ){
		super(verbose);
		binCount = bins;
	}
	
	public void setLogScale( ){
		log_scale = true;
	}
	
	public void setLinearScale( ){
		log_scale = false;
	}

	public void setData( ScalarField1D sf ){ _setData( sf ); }
	public void setData( ScalarField2D sf ){ _setData( sf ); }
	public void setData( ScalarField3D sf ){ _setData( sf ); }
	public void setData( ScalarFieldND sf ){ _setData( sf ); }

	protected void _setData( ScalarFieldND sf ){
		range = new FloatRange1D( );
		for(int i = 0; i < sf.getSize(); i++){
			range.expand( sf.getValue(i) );
		}
		histogram = new Histogram1D( range, binCount ); 
		for(int i = 0; i < sf.getSize(); i++){
			float v = sf.getValue(i);
			if( !Float.isNaN(v) )
				histogram.Add( v );
		}
		binMax = histogram.GetMaximumBin() + histogram.GetMaximumBin()/20;
		norm_dist = new Gaussian( histogram.getApproximateMean(), histogram.getApproximateStdev() );
		
	}
		
	@Override
	public void draw(PGraphics g) {
		if( !isEnabled() ) return;
		if( histogram == null || range == null ) return;
		
		g.hint( PConstants.DISABLE_DEPTH_TEST );
		
		g.strokeWeight(2);
		g.stroke(0);
		g.fill(255);
		g.rect( winX.start(), winY.start(),winX.length(), winY.length() );
		
		draw3Sigma( g );
		drawScales( g );
		drawHistogram( g );
		drawMean( g );
		drawGaussian( g );
		
		g.hint( PConstants.ENABLE_DEPTH_TEST );
		
	}
	
	
	private void draw3Sigma( PGraphics g ){
		if( !show_3sigma ) return;
		
		double mean  = norm_dist.getMean();
		double stdev = norm_dist.getStdev();
		
		float x0 = winX.interpolate( (float) range.getNormalized( mean + stdev*-3 ) );
		float x1 = winX.interpolate( (float) range.getNormalized( mean + stdev*-2 ) );
		float x2 = winX.interpolate( (float) range.getNormalized( mean + stdev*-1 ) );
		float x3 = winX.interpolate( (float) range.getNormalized( mean + stdev*+1 ) );
		float x4 = winX.interpolate( (float) range.getNormalized( mean + stdev*+2 ) );
		float x5 = winX.interpolate( (float) range.getNormalized( mean + stdev*+3 ) );
		
		g.noStroke();
		g.beginShape( PConstants.QUADS );
		g.fill( 250,0,0,25 ); g.vertex( x0, winY.start()+1 ); g.vertex( x0, winY.end()-1 ); g.vertex( x1, winY.end()-1 ); g.vertex( x1, winY.start()+1 ); 
		g.fill( 250,0,0,50 ); g.vertex( x1, winY.start()+1 ); g.vertex( x1, winY.end()-1 ); g.vertex( x2, winY.end()-1 ); g.vertex( x2, winY.start()+1 );
		g.fill( 250,0,0,75 ); g.vertex( x2, winY.start()+1 ); g.vertex( x2, winY.end()-1 ); g.vertex( x3, winY.end()-1 ); g.vertex( x3, winY.start()+1 );
		g.fill( 250,0,0,50 ); g.vertex( x3, winY.start()+1 ); g.vertex( x3, winY.end()-1 ); g.vertex( x4, winY.end()-1 ); g.vertex( x4, winY.start()+1 );
		g.fill( 250,0,0,25 ); g.vertex( x4, winY.start()+1 ); g.vertex( x4, winY.end()-1 ); g.vertex( x5, winY.end()-1 ); g.vertex( x5, winY.start()+1 );
		g.endShape();
	}
	
	private void drawScales( PGraphics g ){
		if( !show_scales ) return;
		
		g.strokeWeight(1);
		g.stroke(200);
		g.noFill();
		if( log_scale ){
			for( int p = 1; p < Math.ceil(Math.log10( binMax )); p++ ){
				int step = (int)Math.pow( 10, p ) / 10;
				for(int j = 1, i = step; j <= 10 && i < binMax; j++, i+= step ){
					float tickH = Math.max(4, (winY.length()-2) * (float)Math.log(i) / (float)Math.log(binMax) );
					if( j == 10 ) 
						g.line( winX.start()+1, winY.end()-tickH, winX.end()-1, winY.end()-tickH );
					else {
						g.line( winX.start()+1, winY.end()-tickH, winX.start()+6, winY.end()-tickH );
						g.line( winX.end()-6,   winY.end()-tickH, winX.end()-1,   winY.end()-tickH );
					}
				}
			}
		}
		else
		{
			int step = (int)Math.pow(10,Math.ceil(Math.log10( binMax ))) / 10;
			for(int j = 0, i = 0; j < 100 && i < binMax; j++, i+= step/10 ){
				float tickH = Math.max(4, (winY.length()-2) * (float)i / (float)binMax );
				if( (j%10) == 0 )
					g.line( winX.start()+1, winY.end()-tickH, winX.end()-1, winY.end()-tickH );
				else {
					g.line( winX.start()+1, winY.end()-tickH, winX.start()+6, winY.end()-tickH );
					g.line( winX.end()-6,   winY.end()-tickH, winX.end()-1,   winY.end()-tickH );
				}
			}       
		}
		g.flush();
	}
	

	
	private void drawHistogram( PGraphics g ){

		g.strokeWeight(1);
		g.stroke(50);
		g.fill(200);		
		float binW = (float)winX.length() / (float)histogram.GetBinCount() - 2;
		for(int i = 0; i < histogram.GetBinCount(); i++){
			float binX = winX.start() + (binW+2)*i + 1;
			float binH;
			if( log_scale )
				binH = Math.max(4, (winY.length()-2) * (float)Math.log(histogram.GetBinValue(i)) / (float)Math.log(binMax) );
			else
				binH = Math.max(4, (winY.length()-2) * (float)histogram.GetBinValue(i) / (float)binMax );
			g.rect( binX, winY.end()-binH, binW, binH );
		}
		g.flush();
		
	}
	
	private void drawMean( PGraphics g ){
		if( !show_mean ) return;
		
		g.strokeWeight(3);
		g.stroke(200,0,0);
		g.noFill();
		float meanx = winX.interpolate( (float) range.getNormalized( norm_dist.getMean() ) );
		g.line( meanx, winY.start()+1, meanx, winY.end()-1 );
		
	}
	
	
	private void drawGaussian( PGraphics g ){
		if( !show_distribution ) return;
		
		g.strokeWeight(1);
		g.stroke(100,0,0);
		g.beginShape( PConstants.LINE_STRIP );
		for( int i = 0; i <= 100; i++ ){
			double x = MathX.lerp(range.getMinimum(),range.getMaximum(),(float)i/(float)100);
			double y = norm_dist.get(x)/norm_dist.get( norm_dist.getMean() );
			double px = winX.interpolate( (float) range.getNormalized(x) );
			double py = winY.interpolate( (float) (1-y) );
			g.vertex( (float)px, (float)py );
		}
		g.endShape( );		
	}



}