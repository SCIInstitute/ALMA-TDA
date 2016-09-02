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
import usf.saav.common.mvc.ViewComponent;

public abstract class LabelDrawing extends ViewComponent.Default implements ViewComponent {

	protected int textSize = 30;
	protected String label = "";
	
	public void setLabelSize( int size ){
		textSize = size;
	}
	
	public int getLabelSize(){ return textSize; }
	
	@Override public void draw( PGraphics g ){
		if( !isEnabled() ) return;
		
		g.hint( PConstants.DISABLE_DEPTH_TEST );
		g.textSize(textSize);
		g.strokeWeight(1);
		g.stroke(0);
		g.fill(255,255,255,240);
		g.rect( winX.start(), winY.start(), Math.max(winX.length(),g.textWidth(label)+10), Math.max(winY.length(), textSize+5) );
		
		g.textAlign( PConstants.LEFT, PConstants.TOP );
		g.stroke(0);
		g.fill(0);
		g.text(label, winX.start()+5, winY.start() );

		g.hint( PConstants.ENABLE_DEPTH_TEST );

	}
	
	public static class BasicLabel extends LabelDrawing {
		public BasicLabel( ){ }
		public BasicLabel( String label ){
			this.label = label;
		}
	}
	
	public static class ComputingLabel extends LabelDrawing {
		int frameCount = 0;
		
		public void update( ){
			label = getComputeString();
		}
		
		protected String getComputeString( ){
			switch( (frameCount/5)%8 ){
				case 0: return "|";
				case 1: return "/"; 
				case 2: return "-";
				case 3: return "\\";
				case 4: return "|";
				case 5: return "/";
				case 6: return "-";
				case 7: return "\\";
			}
			return "";
		}
		
		@Override
		public void draw( PGraphics g ){
			frameCount++;
			g.textSize(textSize);
			setPosition( getU0(), getV0(), (int)Math.max( getWidth( ), g.textWidth(label)+10 ), getHeight() );
			super.draw(g);
		}
	}
	
}
