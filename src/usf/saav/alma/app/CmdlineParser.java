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
package usf.saav.alma.app;

import usf.saav.alma.app.TDAInteractive.TreeDimension;
import usf.saav.common.range.IntRange1D;

public class CmdlineParser {

	boolean	interactive = false;
	
	public CmdlineParser( TDAExec exec, String [] args) {
		
		if( !args[args.length-1].equalsIgnoreCase("interactive") ) {
			exec.filename = args[args.length-1];
		}
				
		for( String arg : args ){
			if( arg.startsWith("dim=") ){
				if(      arg.substring(4).compareToIgnoreCase("2D") == 0    ) exec.treedim = TreeDimension.DIM_2D;
				else if( arg.substring(4).compareToIgnoreCase("3D") == 0    ) exec.treedim = TreeDimension.DIM_3D;
				else{
					System.err.println( "Unknown dimension " + arg.substring(4) );
				}
				continue;
			}
			if( arg.equalsIgnoreCase("interactive") ){
				interactive = true;
				continue;
			}
			if( arg.startsWith("x=") ){
				exec.xr = IntRange1D.parseRange( arg.substring(2) ); 
				continue;
			}
			if( arg.startsWith("y=") ){
				exec.yr = IntRange1D.parseRange( arg.substring(2) );
				continue;
			}
			if( arg.startsWith("z=") ){
				exec.zr = IntRange1D.parseRange( arg.substring(2) );
				continue;
			}
			if( arg.startsWith("simplify=") ){
				exec.simplification = Float.parseFloat( arg.substring(9) );
				continue;
			}
			if( arg.startsWith("output=") ){
				exec.output = arg.substring(7);
				continue;
			}
			// if this isn't the last arg, throw an error
			if( arg != args[args.length-1] ){
				System.err.println( "Unknown argument: " + arg );
			}
		}	
	}
	

	public static void printLicense(){
		System.out.println("   ALMA TDA - Contour tree based simplification and visualization for ALMA" ); 
		System.out.println("   data cubes." ); 
		System.out.println("   Copyright (C) 2016 PAUL ROSEN" ); 
		System.out.println(); 
		System.out.println("   This program is free software: you can redistribute it and/or modify" ); 
		System.out.println("   it under the terms of the GNU General Public License as published by" ); 
		System.out.println("   the Free Software Foundation, either version 3 of the License, or" ); 
		System.out.println("   (at your option) any later version." ); 
		System.out.println(); 
		System.out.println("   This program is distributed in the hope that it will be useful," ); 
		System.out.println("   but WITHOUT ANY WARRANTY; without even the implied warranty of" ); 
		System.out.println("   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the" ); 
		System.out.println("   GNU General Public License for more details." ); 
		System.out.println(); 
		System.out.println("   You should have received a copy of the GNU General Public License" ); 
		System.out.println("   along with this program.  If not, see <http://www.gnu.org/licenses/>." ); 
		System.out.println(); 
		System.out.println("   You may contact the Paul Rosen at <prosen@usf.edu>." );  
		System.out.println();
	}
	
	public static void printUsage(){
		System.out.println();
		System.out.println();
		System.out.println("Usage: java -jar ALMA-TDA.jar [options] input_file" );
		System.out.println();
		System.out.println("  Required:");
		System.out.println("    input_file      ---  Input file in fits format.");
		System.out.println();
		System.out.println("  Options:");
		System.out.println("    dim=DIM_TYPE    ---  Type of contour tree to create. Options: 2D or 3D. (default: 2D)");
		System.out.println("    x=RANGE         ---  Range of pixels in x direction. Valid options included single values and inclusive/exclusive ranges. (default: width of image)");
		System.out.println("    y=RANGE         ---  Range of pixels in y direction. (default: height of image)");
		System.out.println("    z=RANGE         ---  Range of pixels in z direction. (default: depth of image)");
		System.out.println("    simplify=AMOUNT ---  Maximum persistence to simplify. (default: 0, no simplification)");
		System.out.println("    output=FILE     ---  The place to save the results. (default: not saved)");
		System.out.println("    interactive     ---  Places the application into interactive mode.");
		System.out.println( );
		System.out.println("  Example: ");
		System.out.println("    java -jar ALMA-TDA.jar x=[0,512) y=[0,512) z=0 output=output.fits input.fits");
		System.out.println();
	}
	
	
	public static void printReproduce( TDAExec exec ) {
		System.out.println();
		System.out.println("To reproduce this result, run: ");
		System.out.print("  java -jar ALMA-TDA.jar" );
		System.out.print(" dim=");
		switch(exec.treedim){
			case DIM_2D: 		System.out.print("2D"); 	break;
			case DIM_2D_STACK:	System.out.print("2D"); 	break;
			case DIM_3D:		System.out.print("3D"); 	break;
		}
		System.out.print(" x=" + exec.xr.toString() + " y=" + exec.yr.toString() + " z=" + exec.zr.toString());
		System.out.print(" simplify=" + exec.simplification );
		System.out.print(" output=" + exec.output );
		System.out.print(" " + exec.filename );
		System.out.println();
	}
	
	
	
}
