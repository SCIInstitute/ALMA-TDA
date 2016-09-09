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
import java.util.Queue;
import java.util.Vector;

import usf.saav.alma.algorithm.mesh.Mesh;
import usf.saav.alma.algorithm.topology.AugmentedJoinTreeNode.Factory;
import usf.saav.alma.algorithm.topology.AugmentedJoinTreeNode.NodeType;
import usf.saav.common.BasicObject;

public abstract class AugmentedJoinTree extends BasicObject implements PersistenceSet, Runnable {
	
	protected Vector<AugmentedJoinTreeNode> nodes = new Vector<AugmentedJoinTreeNode>();
	protected AugmentedJoinTreeNode head;
	protected float persistence_max;
	protected AugmentedJoinTreeNode global_extreme;
	protected Mesh cl;
	protected float simplify = 0.0f;
	
	private Comparator<? super JoinTreeNode> comparator;
	private Factory factory;

	
	protected AugmentedJoinTree( Mesh cl, Comparator<? super JoinTreeNode> comparator, AugmentedJoinTreeNode.Factory factory ){
		this.cl = cl;
		this.comparator = comparator;
		this.factory = factory;
	}
	
	@Override
	public void run() {
		print_info_message( "Building tree..." );

		// Build a join tree.
		JoinTree jt = new JoinTree( cl, comparator );
		jt.run();

		head = findChild( jt.getRoot() );
		
		calculatePersistence();
		
		for(int i = 0; i < size(); i++){
			float per = getPersistence(i);
			if( Float.isNaN(per) )
				global_extreme = getNode(i);
			else
				persistence_max = Math.max( persistence_max, per );
		}
		
		print_info_message( "Building tree complete" );
	}
	

	protected void calculatePersistence(){
		print_info_message( "Finding Persistence");

		Queue<AugmentedJoinTreeNode> queue = new LinkedList<AugmentedJoinTreeNode>( );
		queue.addAll( nodes );

		while( queue.size() > 0 ){
			AugmentedJoinTreeNode node = queue.poll();
			if( node.getType() == NodeType.MERGE || node.getType() == NodeType.SPLIT ) {
				if( !findJoinPartner(node) ){
					queue.add(node);
				}
			}
		}
		
	}
	
	protected boolean findJoinPartner( AugmentedJoinTreeNode node ){ 
		Queue<AugmentedJoinTreeNode> wq = new LinkedList<AugmentedJoinTreeNode>();
		AugmentedJoinTreeNode partner = null;

		wq.addAll(node.getChildren());

		while( wq.size() > 0 ){
			AugmentedJoinTreeNode cnode = wq.poll();
			if( cnode.partner == null ){
				switch( cnode.getType() ){
				case LEAF_MAX:
					if( partner == null || cnode.value < partner.value )
						partner = cnode;
					break;
				case LEAF_MIN:
					if( partner == null || cnode.value > partner.value )
						partner = cnode;
					break;
				case MERGE:
					print_warning_message( "uncancelled merge " + cnode.getLocation() );
					return false; 
				case SPLIT:
					print_warning_message( "uncancelled merge " + cnode.getLocation() );
					return false; 
				default:
					break;
				
				}
			}
			
			wq.addAll( cnode.getChildren() );
		}

		if( partner == null ){
			print_warning_message( "no single partner found ");
			return false;
		}

		node.partner = partner;
		partner.partner = node;
		return true;
	}
	
	

	
	protected AugmentedJoinTreeNode findChild( JoinTreeNode current ){
		while( current.childCount() == 1 ){
			current = current.getChild(0);
		}
		if( current.childCount() == 0 ){
			nodes.add( factory.createNode( current.getPosition(), current.getValue() ) );
			return nodes.lastElement();
		}
		if( current.childCount() == 2 ){
			nodes.add( factory.createNode( current.getPosition(), current.getValue(),
							findChild( current.getChild(0) ),
							findChild( current.getChild(1) ) 
						) );
			return nodes.lastElement();
		}
		// Monkey saddle --- should probably do something a little smarter here
		if( current.childCount() == 3 ){
			AugmentedJoinTreeNode child = factory.createNode( current.getPosition(), current.getValue(),
														findChild( current.getChild(0) ),
														findChild( current.getChild(1) ) 
													);
			AugmentedJoinTreeNode parent = factory.createNode( current.getPosition(), current.getValue(),
														child,
														findChild( current.getChild(2) ) 
				);
			nodes.add(child);
			nodes.add(parent);
			
			return parent;
		}
		
		print_warning_message( "Error, split with " + current.childCount() + " children" );
		return null;
	}
	
	public AugmentedJoinTreeNode getGlobalExtreme(){ return global_extreme; }

	@Override public float getMaxPersistence(){ return persistence_max; }
	
	@Override 
	public void setPersistentSimplification( float threshold ){
		simplify = threshold;
	}

	@Override
	public float getPersistentSimplification( ){
		return simplify;
	}

	@Override
	public boolean isActive(int i){
		return getPersistence(i) >= simplify;
	}

	@Override
	public int size() {
		return nodes.size();
	}

	@Override
	public float getBirth(int i) {
		return nodes.get(i).getBirth();
	}

	@Override
	public float getDeath(int i) {
		return nodes.get(i).getDeath();
	}

	@Override
	public float getPersistence(int i) {
		return nodes.get(i).getPersistence();
	}

	public AugmentedJoinTreeNode getNode(int i) {
		return nodes.get(i);
	}

}
