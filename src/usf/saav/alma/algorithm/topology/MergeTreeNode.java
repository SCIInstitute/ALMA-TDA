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


// TODO: Auto-generated Javadoc
/**
 * The Class MergeTreeNode.
 */
public class MergeTreeNode extends AugmentedJoinTreeNode {
	
	/**
	 * Instantiates a new merge tree node.
	 *
	 * @param loc the loc
	 * @param val the val
	 */
	public MergeTreeNode( int loc, float val ){
		super(loc,val);
	}
	
	/**
	 * Instantiates a new merge tree node.
	 *
	 * @param loc the loc
	 * @param val the val
	 * @param c0 the c 0
	 * @param c1 the c 1
	 */
	public MergeTreeNode( int loc, float val, AugmentedJoinTreeNode c0, AugmentedJoinTreeNode c1 ){
		super(loc,val,c0,c1);
	}
	
	/* (non-Javadoc)
	 * @see usf.saav.alma.algorithm.topology.AugmentedJoinTreeNode#getType()
	 */
	public NodeType getType() {
		if( child0 == null && child1 == null ) return NodeType.LEAF_MIN;
		if( child0 != null && child1 != null ) return NodeType.MERGE;
		return NodeType.UNKNOWN;
	}
	
	/**
	 * The Class Factory.
	 */
	public static class Factory implements AugmentedJoinTreeNode.Factory {
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.algorithm.topology.AugmentedJoinTreeNode.Factory#createNode(int, float)
		 */
		public AugmentedJoinTreeNode createNode( int loc, float val ){
			return new MergeTreeNode(loc,val);
		}
		
		/* (non-Javadoc)
		 * @see usf.saav.alma.algorithm.topology.AugmentedJoinTreeNode.Factory#createNode(int, float, usf.saav.alma.algorithm.topology.AugmentedJoinTreeNode, usf.saav.alma.algorithm.topology.AugmentedJoinTreeNode)
		 */
		public AugmentedJoinTreeNode createNode( int loc, float val, AugmentedJoinTreeNode c0, AugmentedJoinTreeNode c1  ){
			return new MergeTreeNode(loc,val,c0,c1);
		}
	}
	
	
}
