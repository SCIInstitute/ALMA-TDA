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
package usf.saav.common.mvc;

import usf.saav.common.range.IntRange1D;

public interface PositionedComponent extends BasicComponent {
	
	public void setPosition( int u0, int v0, int w, int h );
	public void setPosition( int [] xywh );
	public int[] getPosition( );
	
	public int getU0();
	public int getV0();
	public int getWidth();
	public int getHeight( );
	
	public boolean inRange( int x, int y );
	

	public class Default extends BasicComponent.Default implements PositionedComponent {
		
		protected IntRange1D winX = new IntRange1D( 0,0 );
		protected IntRange1D winY = new IntRange1D( 0,0 );
		
		protected Default( ){ }
		protected Default(boolean verbose) { super(verbose); }
		
		@Override
		public boolean inRange( int x, int y ){
			return winX.inRange(x) && winY.inRange(y);
		}
		
		@Override 
		public void setPosition( int u0, int v0, int w, int h ){
			winX.set( u0, u0+w-1 );
			winY.set( v0, v0+h-1 );
		}
		
		@Override 
		public void setPosition( int [] xywh ){
			setPosition(xywh[0],xywh[1],xywh[2],xywh[3]);
		}
		
		@Override 
		public int[] getPosition( ){
			return new int[]{winX.start(),winY.start(),winX.length(),winY.length()};
		}
		
		@Override public int getU0(){ return winX.start(); }
		@Override public int getV0(){ return winY.start(); }
		@Override public int getWidth(){ return winX.length(); }
		@Override public int getHeight( ){ return winY.length(); }
		
	}
	
}
