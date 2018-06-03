package usf.saav.matrix;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Vector;

public class BaseMatrix {

	public double [][] data;
	int w,h;
	
	protected BaseMatrix( String filename ) throws IOException {
		readData( filename );
	}
	
	public BaseMatrix( double [][] _data ){
		data = _data;
		w = 0;
		 h = data.length;
		 for(int i = 0; i < h; i++ ){
			 w = Math.max(w, data[i].length);
		 }
	}
	
	public BaseMatrix( int _w, int _h ){
		w = _w;
		h = _h;
		data = new double[w][h];
	}
	
	public void readData( String filename ) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader( filename ));

		Vector<double[]> rows = new Vector<double[]>();
		
		 String line;
		 while( ( line = reader.readLine() ) != null ) {
	            String [] elems = line.split("\\s+");
	            double [] newrow = new double[elems.length];
	            for( int i = 0; i < elems.length; i++ ){
	            	if( elems[i].equalsIgnoreCase("inf") ){
	            		newrow[i] = Double.POSITIVE_INFINITY;
	            	}
	            	else if( elems[i].equalsIgnoreCase("NaN") ){
	            		newrow[i] = Double.NaN;
	            	}
	            	else{
		            	newrow[i] = Double.parseDouble(elems[i]);
		            	if( newrow[i] > 1.0e128 ) 
		            		newrow[i] = Double.POSITIVE_INFINITY;
	            	}
	            }
	            rows.add(newrow);
	     }
		 reader.close();

		 w = 0;
		 h = rows.size();
		 data = new double[h][];
		 for(int i = 0; i < h; i++ ){
			 data[i] = rows.get(i);
			 w = Math.max(w, data[i].length);
		 }		
	}
	
	public void saveData( String filename ) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter( filename );
		pw.append(toString());
		pw.close();

	}
	
	
	public int getWidth(){
		return w;
	}
	
	public int getHeight(){
		return h;
	}
	
	public double get( int u, int v ){
		if( v >= h ) return Double.NaN;
		return (u<data[v].length)?data[v][u]:Double.NaN;
	}
	
	public void set( int u, int v, double val ){
		if( u < 0 || u >= w ) return;
		if( v < 0 || v >= h ) return;
		data[v][u] = val;		
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer( );
		for( int v = 0; v < h; v++ ){
			for( int u = 0; u < w; u++ ){
				if( Double.isInfinite(get(u,v)) ){
					sb.append( "Inf " );
				}
				else if( Double.isNaN(get(u,v)) ){
					sb.append( "NaN " );
				}
				else{
					sb.append( get(u,v) + " " );
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public void clear( double val ) {
		for(int i = 0; i < data.length; i++){
			Arrays.fill( data[i], val);
		}
	}
	
	
}
