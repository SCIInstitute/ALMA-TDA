package usf.saav.alma.app;

import java.io.IOException;
import java.util.Arrays;

import nom.tam.fits.common.FitsException;
import usf.saav.alma.app.views.AlmaGui.TreeDimension;
import usf.saav.alma.data.fits.FitsReader;
import usf.saav.alma.data.fits.FitsWriter;
import usf.saav.alma.data.fits.RawFitsReader;
import usf.saav.alma.data.fits.SafeFitsReader;
import usf.saav.alma.data.processors.Composite2D;
import usf.saav.alma.data.processors.Extract2DFrom3D;
import usf.saav.common.range.IntRange1D;
import usf.saav.scalarfield.ScalarField2D;

public class Cmdline {

	String filename = null;
	FitsReader fits = null;
	TreeDimension treedim = TreeDimension.DIM_2D;
	IntRange1D xr,yr,zr;
	float simplification = 0;
	String  output  = null;
	SliceProcessor  sliceProc = new SliceProcessor();
	VolumeProcessor volProc   = new VolumeProcessor();
	boolean interactive = false;

	
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
		System.out.println("    input_file     ---  Input file in fits format.");
		System.out.println();
		System.out.println("  Options:");
		System.out.println("    interactive    ---  Places the application into interactive mode.");
		System.out.println("    dim=DIM_TYPE   ---  Type of contour tree to create. Options: 2D or 3D");
		System.out.println("    x=RANGE        ---  Range of pixels in x direction. Valid options included single values and inclusive/exclusive ranges.");
		System.out.println("    y=RANGE        ---  Range of pixels in y direction.");
		System.out.println("    z=RANGE        ---  Range of pixels in z direction.");
		System.out.println("    simplify=RANGE ---  Maximum persistence to simplify.");
		System.out.println("    output=FILE    ---  The place to save the results.");
		System.out.println( );
		System.out.println("  Example: ");
		System.out.println("    java -jar ALMA-TDA.jar x=[0, 512) y=[0, 512) z=0 output=output.fits input.fits");
		System.out.println();
	}
	
	
	public void loadFile( String _filename ){
		filename = _filename;
		try {
			fits = new SafeFitsReader( new RawFitsReader(filename, true), true );
		} catch (IOException | FitsException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		xr = fits.getAxesSize()[0].clone();
		yr = fits.getAxesSize()[1].clone();
		zr = fits.getAxesSize()[2].clone();
	}
	

	public void process3D(){
		if( treedim == TreeDimension.DIM_3D ){
			volProc.process( fits,  xr, yr, zr, simplification );
		}		
	}

	
	public void saveOutput( ){
		if( output == null ) return;
		try {
			FitsWriter fw = new FitsWriter( fits.getAxesSize()[0].length(), fits.getAxesSize()[1].length(), fits.getAxesSize()[2].length() );
			fw.open( fits, output );
			for(int d = 0; d < fits.getAxesSize()[2].length() && d < 3; d++ ){
				
				ScalarField2D writeslice = fits.getSlice(d, 0);
				if( treedim == TreeDimension.DIM_2D && zr.inRange(d) ){
					sliceProc.process(fits, xr, yr, d, simplification);
					writeslice = new Composite2D(writeslice, sliceProc.ps2d, xr.start(), yr.start() );
				}
				if( treedim == TreeDimension.DIM_3D && zr.inRange(d) ){
					ScalarField2D modslice = new Extract2DFrom3D( volProc.ps3d, d-zr.start() );
					writeslice = new Composite2D(writeslice, modslice, xr.start(), yr.start() );
				}
				fw.writeSlice( writeslice );
				
			}
			fw.close();
		} catch (IOException | FitsException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	


	private void printReproduce() {
		System.out.println();
		System.out.println("To reproduce this result, run: ");
		System.out.print("  java -jar ALMA-TDA.jar" );
		System.out.print(" dim=");
		switch(treedim){
			case DIM_2D: 		System.out.print("2D"); 	break;
			case DIM_3D:		System.out.print("3D"); 	break;
		}
		System.out.print(" x=" + xr.toString() + " y=" + yr.toString() + " z=" + zr.toString());
		System.out.print(" simplify=" + simplification );
		System.out.print(" output=" + output );
		System.out.print(" " + filename );
		System.out.println();
	}
	

	private void parseArgs(String [] args) {
		for( String arg : args ){
			if( arg.startsWith("dim=") ){
				if(      arg.substring(4).compareToIgnoreCase("2D") == 0    ) treedim = TreeDimension.DIM_2D;
				else if( arg.substring(4).compareToIgnoreCase("3D") == 0    ) treedim = TreeDimension.DIM_3D;
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
				xr = IntRange1D.parseRange( arg.substring(2) ); 
				continue;
			}
			if( arg.startsWith("y=") ){
				yr = IntRange1D.parseRange( arg.substring(2) );
				continue;
			}
			if( arg.startsWith("z=") ){
				zr = IntRange1D.parseRange( arg.substring(2) );
				continue;
			}
			if( arg.startsWith("simplify=") ){
				simplification = Float.parseFloat( arg.substring(9) );
				continue;
			}
			if( arg.startsWith("output=") ){
				output = arg.substring(7);
				continue;
			}
			System.err.println( "Unknown argument: " + arg );
		}	
	}
	
	
	public static void main( String [] args ){
		
		printLicense();
		
		if( args.length == 0 ){
			printUsage();
			return;
		}
		
		Cmdline almaTDA = new Cmdline();

		almaTDA.loadFile( args[args.length-1] );
		almaTDA.parseArgs( Arrays.copyOf(args, args.length-1) );
		if( almaTDA.interactive ){
			InteractiveViewer.create(almaTDA);
		}
		else{
			almaTDA.process3D();
			almaTDA.saveOutput();
			almaTDA.printReproduce();
		}
	}
	
	
}
