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
package usf.saav.common.types;

import java.util.Comparator;


public class PairComparator<A, B> implements Comparator< Pair<A, B> > {

	@SuppressWarnings("unchecked")
	@Override
	public int compare(Pair<A, B> arg0, Pair<A, B> arg1) {

		if( arg0.getFirst() instanceof Comparator<?> && arg1.getFirst() instanceof Comparator<?> ){
			int cmp0 = ((Comparator<A>)arg0.getFirst()).compare( arg0.getFirst(), arg1.getFirst());
			if(cmp0 != 0) return cmp0;
		}
		
		if( arg0.getSecond() instanceof Comparator<?> && arg1.getSecond() instanceof Comparator<?> ){
			int cmp1 = ((Comparator<B>)arg0.getSecond()).compare( arg0.getSecond(), arg1.getSecond());
			if(cmp1 != 0) return cmp1;
		}
		return 0;
	}
	
}
