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

public class SplitTreeNode extends AugmentedJoinTreeNode {

	public SplitTreeNode( int loc, float val ){
		super(loc,val);
	}
	
	public SplitTreeNode( int loc, float val, AugmentedJoinTreeNode c0, AugmentedJoinTreeNode c1 ){
		super(loc,val,c0,c1);
	}
	
	public NodeType getType() {
		if( child0 == null && child1 == null ) return NodeType.LEAF_MAX;
		if( child0 != null && child1 != null ) return NodeType.SPLIT;
		return NodeType.UNKNOWN;
	}
	
	public static class Factory implements AugmentedJoinTreeNode.Factory {
		public AugmentedJoinTreeNode createNode( int loc, float val ){
			return new SplitTreeNode(loc,val);
		}
		public AugmentedJoinTreeNode createNode( int loc, float val, AugmentedJoinTreeNode c0, AugmentedJoinTreeNode c1  ){
			return new SplitTreeNode(loc,val,c0,c1);
		}
	}
}
