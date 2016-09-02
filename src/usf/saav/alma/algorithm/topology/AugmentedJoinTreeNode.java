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
import java.util.Vector;

public abstract class AugmentedJoinTreeNode {
	
	public static class ComparePersistenceAscending implements Comparator<AugmentedJoinTreeNode> {
		@Override public int compare(AugmentedJoinTreeNode o1, AugmentedJoinTreeNode o2) {
			if( o1.getPersistence() > o2.getPersistence() ) return  1;
			if( o1.getPersistence() < o2.getPersistence() ) return -1;
			return 0;
		}
	}

	public static class ComparePersistenceDescending implements Comparator<AugmentedJoinTreeNode> {
		@Override public int compare(AugmentedJoinTreeNode o1, AugmentedJoinTreeNode o2) {
			if( o1.getPersistence() < o2.getPersistence() ) return  1;
			if( o1.getPersistence() > o2.getPersistence() ) return -1;
			return 0;
		}
	}

	
	public enum NodeType {
		LEAF_MIN, LEAF_MAX, MERGE, SPLIT, UNKNOWN
	}
	
	private int location;
	float value;
	AugmentedJoinTreeNode child0;
	AugmentedJoinTreeNode child1;
	AugmentedJoinTreeNode partner;
	
	public AugmentedJoinTreeNode( int loc, float val ){
		this.location = loc;
		this.value = val;
	}
	public AugmentedJoinTreeNode( int loc, float val, AugmentedJoinTreeNode c0, AugmentedJoinTreeNode c1 ){
		this.location = loc;
		this.value = val;
		this.child0 = c0;
		this.child1 = c1;
	}
	public abstract NodeType getType();
	
	public float getBirth(){
		if( partner == null ) return Float.NaN;
		return Math.min( value, partner.value );
	}
	public float getDeath(){
		if( partner == null ) return Float.NaN;
		return Math.max( value, partner.value );
	}
	public float getPersistence() {
		if( partner == null ) return Float.NaN;
		return getDeath()-getBirth();
	}
	public int getLocation() {
		return location;
	}
	public Vector<AugmentedJoinTreeNode> getChildren(){
		Vector<AugmentedJoinTreeNode> ret = new Vector<AugmentedJoinTreeNode>();
		if( child0 != null ) ret.add(child0);
		if( child1 != null ) ret.add(child1);
		return ret;
	}
	public AugmentedJoinTreeNode getPartner(){
		return partner;
	}
	
	public interface Factory {
		public AugmentedJoinTreeNode createNode( int loc, float val );
		public AugmentedJoinTreeNode createNode( int loc, float val, AugmentedJoinTreeNode c0, AugmentedJoinTreeNode c1  );

	}
	
}
