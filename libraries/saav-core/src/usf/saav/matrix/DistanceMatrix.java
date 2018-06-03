package usf.saav.matrix;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import usf.saav.common.SystemX;
import usf.saav.common.algorithm.ConnectedComponent;

public class DistanceMatrix extends BaseMatrix {

	String filename = null;
	Semaphore lock = new Semaphore(1);
	
	public DistanceMatrix( int dim, String _filename ) throws IOException {
		super( dim,dim );
		filename = _filename;
		if( SystemX.fileExists(filename) ){
			try {
				readData(filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			clear( Double.NaN );
		}
	}

	public DistanceMatrix( String _filename ) throws IOException {
		super(_filename);
		filename = _filename;
	}

	public DistanceMatrix( int wh ){
		super( wh, wh );
	}


	public void setOutputFile( String _filename ) {
		filename  = _filename;
	}
	
	public boolean isValid( int i, int j ){
		return !Double.isNaN( get(i,j) );
	}
	
	@Override
	public void set( int i, int j, double val ){
		try {
			lock.acquire();
			super.set(i, j, val);
			super.set(j, i, val);
			lock.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean saveData( ){
		if( filename == null ) return false;
		System.out.println( "Saving " + filename );
		try {
			lock.acquire();
			super.saveData(filename);
			lock.release();
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	

	public Vector<ConnectedComponent<Integer>> getConnectedComponents( double maxThrs ){
		Vector<ConnectedComponent<Integer>> cc = new Vector<ConnectedComponent<Integer>>( );
		boolean [] marks = new boolean[getWidth()];
		Arrays.fill(marks, false);
		for(int i = 0; i < marks.length; i++ ){
			if( marks[i] ) continue;
			ConnectedComponent<Integer> c = new ConnectedComponent<Integer>( );
			c.add(i);
			for( int j = i+1; j < marks.length; j++ ){
				if( get( i,j ) < maxThrs ){
					marks[j] = true;
					c.add(j);
				}
			}
			cc.add(c);
		}
		return cc;		
	}
	
	public DistanceMatrix getSubmatrix( ConnectedComponent<Integer> cc ){
		DistanceMatrix ret = new DistanceMatrix( cc.size() );
		int i = 0; 
		for( int idxI : cc ) {
			int j = 0;
			for( int idxJ : cc ) {
				ret.set( i, j, get(idxI,idxJ) );
				j++;
			}
			i++;
		}
		return ret;
	}
	
	
}
