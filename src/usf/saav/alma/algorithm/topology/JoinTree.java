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
package usf.saav.alma.algorithm.topology;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import usf.saav.alma.algorithm.mesh.Mesh;
import usf.saav.alma.algorithm.mesh.ScalarFieldMesh;
import usf.saav.common.algorithm.BinaryMask1D;
import usf.saav.common.algorithm.DisjointSet1D;

public class JoinTree implements Runnable {
 
	/***
	 ***
	 *** This section is for public functions and variables	
	 ***
	 ***/

	private Comparator<? super JoinTreeNode> comparator;
	private Mesh sf;
	private int size;
	private JoinTreeNode head;
	private JoinTreeNode [] grid;
	protected boolean operationComplete = false;
	

	public JoinTree( Mesh sf ) {
		this.sf = sf;
		this.comparator = new JoinTreeNode.ComparatorValueAscending();
	}
	
	protected JoinTree( Mesh sf, Comparator<? super JoinTreeNode> comparator  ) {
		this.sf = sf;
		this.comparator = comparator;
	}

		
	public JoinTreeNode getNode( int x ){
		if( !operationComplete ) return null;
		return grid[x];
	}
	
	public JoinTreeNode getRoot( ){
		if( !operationComplete ) return null;
		return head;
	}
	
	public void print( ){
		if( !operationComplete ) return;
		System.out.println("join tree");
		if( head != null ) head.print(true);
	}


	@Override
	public void run() {
		
		if( operationComplete ) return;

		this.size = sf.getWidth();
		this.grid = new JoinTreeNode[size];

		// We first order the points for adding to the tree.
		Queue< JoinTreeNode > tq = new PriorityQueue< JoinTreeNode >( size, comparator );
		for(int i = 0; i < sf.getWidth(); i++ ){
			tq.add( new JoinTreeNode( sf.get(i).value(), i ) );
		}
		
		// Disjoint Set used to mark which set a points belongs to
		DisjointSet1D dj = new DisjointSet1D( sf.getWidth() );
		
		// Mask for marking who has been processed
		BinaryMask1D bm = new BinaryMask1D( sf.getWidth(), false );
		
		// start popping elements off the of the list
		while( tq.size() > 0 ){
			head = tq.poll();
			init_mergeWithNeighbors( head, sf, bm, dj );
			bm.set(head.getPosition());
		}
		
		// place all of the nodes within list ordered by position
		init_grid( head );
		
		operationComplete = true;
		
	}
	

	
	/***
	 ***
	 *** This section is for private functions and variables
	 ***	
	 ***/

	
	private void init_grid( JoinTreeNode node ) {
		// set node to grid an recurse
		Queue< JoinTreeNode > queue = new LinkedList< JoinTreeNode >( );
		queue.add(node);
		
		while( !queue.isEmpty() ){
			node = queue.poll();
			grid[ node.getPosition() ] = node;
			queue.addAll( node.getChildren() );
		}
	}

	private void init_mergeWithNeighbors( JoinTreeNode me, Mesh sf, BinaryMask1D bm, DisjointSet1D dj ) {
		
		int [] neighbors = sf.get( me.getPosition() ).neighbors();
		
		// set any neighbor sets as children
		for( int n : neighbors ){
			if( bm.isSet( n ) ){
				int setIdx = dj.find( n );
				if( !me.isChild( grid[setIdx] ) ){
					me.addChild( grid[setIdx] );
					grid[setIdx].setParent(me);
				}
			}
		}

		// update the disjoint set with new connection
		for( int n : neighbors ){
			if( bm.isSet( n ) ){
				dj.union( me.getPosition(), n );
			}
		}
		
		// update the root for the set
		grid[ dj.find( me.getPosition() ) ] = me;

	}
	
	

	/***
	 ***
	 *** Main function for testing algorithm
	 ***	
	 ***/
	public static void main( String args[] ){
		usf.saav.alma.data.ScalarField2D sf = new usf.saav.alma.data.ScalarField2D.Test( 10, 10 );
		JoinTree jt = new JoinTree( new ScalarFieldMesh( sf ) );
		System.out.println(sf.toString());
		jt.print();
	}

	
}


