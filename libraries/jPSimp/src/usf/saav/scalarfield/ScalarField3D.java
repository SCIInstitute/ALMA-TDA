/*
 *     jPSimp - Persistence calculation and simplification of scalar fields.
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
package usf.saav.scalarfield;

import java.io.IOException;
import java.io.PrintWriter;



public interface ScalarField3D extends ScalarFieldND {

	public float getValue( int x, int y, int z );
	public int getWidth() ;
	public int getHeight();
	public int getDepth() ;

	
	public class Empty extends ScalarField3D.Default {
		int w,h,d;
		float default_val;
		public Empty( int w, int h, int d, float default_val ) {
			this.w = w;
			this.h = h;
			this.d = d;
			this.default_val = default_val;
		}
		@Override public int getWidth()  {	return w; }
		@Override public int getHeight() {	return h; }
		@Override public int getDepth()  {	return d; }
		@Override public float getValue(int x, int y, int z) { return default_val; }
	}
	
	public abstract class Default extends ScalarFieldND.Default implements ScalarField3D {

		protected Default( ){ }
		protected Default( boolean verbose ){ super(verbose); }
		
		@Override
		public int getSize() {
			return getWidth()*getHeight()*getDepth();
		}

		@Override
		public float getValue(int nodeID) {
			
			int width = getWidth();
			int height = getHeight();
			
			int x = nodeID%width;
			int y = (nodeID%(width*height))/width;
			int z = nodeID/(width*height);
			
			return getValue( x, y, z );
		}

		public static void saveField(ScalarField3D sf,String filename) throws IOException {
			PrintWriter pw = new PrintWriter( filename );
			pw.println( "3D " + sf.getWidth() + " " + sf.getHeight() + " " + sf.getDepth() );
			for(int z = 0; z < sf.getDepth(); z++ ){
				for(int y = 0; y < sf.getHeight(); y++ ){
					for(int x = 0; x < sf.getWidth(); x++){
						pw.print( sf.getValue(x,y,z) + " " );
					}
					pw.println();
				}
				pw.println();
			}
			pw.close();
		}

	}
}
