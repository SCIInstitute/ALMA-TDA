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
package usf.saav.common.algorithm.deprecated;

import java.util.HashMap;

@Deprecated
public class HashDisjointSet1D {

	private int size = 0;
	private HashMap<Integer,Integer> elements = new HashMap<Integer,Integer>( );

	/**
	 *  Construct a disjoint sets object.
	 *
	 *  @param numElements the initial number of elements--also the initial
	 *  number of disjoint sets, since every element is initially in its own set.
	 **/
	public HashDisjointSet1D(int numElements) {
		size = numElements;
	}

	/**
	 *  union() unites two disjoint sets into a single set.  A 
	 *  union-by-lower_id heuristic is used to choose the new root.
	 *
	 *  @param set1 a member of the first set.
	 *  @param set2 a member of the other set.
	 **/
	public void union( int newRoot, int oldRoot ){
		int r0 = find( newRoot );
		int r1 = find( oldRoot );
		if( r0 != r1 ) elements.put( r1, r0 );
	}


	/**
	 *  find() finds the name of the set containing a given element.
	 *  Performs path compression along the way.
	 *
	 *  @param x the element sought.
	 *  @return the set containing x.
	 **/
	public int find(int x) {
		if( !elements.containsKey(x) ) return x;
		if( elements.get(x) == x ) return x;
		int ret = find( elements.get(x) );
		elements.put(x, ret);
		return ret;
	}


	public int getWidth() {
		return size;
	}
	

	public int size() {
		return size;
	}
	
	public void set(int loc, int value) {
		elements.put(loc, value);
	}




	/**
	 *  main() is test code.  All the find()s on the same output line should be
	 *  identical.
	 **/
	public static void main(String[] args) {
		int NumElements = 128;
		int NumInSameSet = 16;

		HashDisjointSet1D s = new HashDisjointSet1D(NumElements);

		for (int k = 1; k < NumInSameSet; k *= 2) {
			for (int j = 0; j + k < NumElements; j += 2 * k) {
				s.union( j , j+k );
			}
		}

		for (int i = 0; i < NumElements; i++) {
			System.out.print(s.find(i) + "*");
			if (i % NumInSameSet == NumInSameSet - 1) {
				System.out.println();
			}
		}
		System.out.println();

	}


}

