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

import usf.saav.common.MathX;

public class IndependentChooser extends Chooser {

	public static long getCount( int n, int k ){
		return MathX.factorial(n).divide( MathX.factorial(k).multiply( MathX.factorial(n-k) ) ).longValue();
	}
	
	int curr;
	
	public IndependentChooser( int n, int k ){
		super( n, k, getCount(n,k) );
		restart();
	}
	
	public void restart( ){
		curr = 0;
	}
	
	public int [] next( ){

		if( curr >= getCount() ) return null;

		if( curr == 0 ){
			for(int i = 0; i < k; i++){
				choice[i] = i;
			}
		}
		else {
			for(int dim = k-1; dim > 0; dim--){
				choice[dim]++;
				if( choice[dim] >= n ){
					choice[dim-1]++;
					choice[dim] = choice[dim-1]+1;
				}
				else{
					break;
				}
			}
		}
		curr++;
		return choice.clone();
		
	}	

}
