/*
 *     saav-core - A (very boring) software development support library.
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
package usf.saav.common.combinatorics;

public class DependentChooser extends Chooser {
	
	int curr = 0;
	IndependentChooser ic = null;
	
	public static long getCount( int n, int k ){
		return n*IndependentChooser.getCount( n-1, k-1 );
	}
	
	public DependentChooser( int n, int k ){
		super( n, k, getCount(n,k) );
		ic = new IndependentChooser( n-1,k-1 );
	}
	
	public void restart( ){
		if(ic != null) ic.restart();
		curr = 0;
	}
	
	public int [] next( ){
		
		int [] tmp = ic.next();
		if( tmp == null ){
			ic.restart();
			tmp = ic.next();
			curr++;
		}
		
		if( curr >= n )
			return null;
		
		choice[0] = curr;
		for(int i = 0; i < tmp.length; i++){
			choice[i+1] = ( tmp[i] >= curr ) ? tmp[i]+1 : tmp[i]; 
		}
		return choice.clone();
		
	}	

}
