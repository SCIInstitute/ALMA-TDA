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

// TODO: Auto-generated Javadoc
/**
 * The Class AugmentedJoinTreeNode.
 */
public abstract class AugmentedJoinTreeNode {
	
	/**
	 * The Class ComparePersistenceAscending.
	 */
	public static class ComparePersistenceAscending implements Comparator<AugmentedJoinTreeNode> {
		
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override public int compare(AugmentedJoinTreeNode o1, AugmentedJoinTreeNode o2) {
			if( o1.getPersistence() > o2.getPersistence() ) return  1;
			if( o1.getPersistence() < o2.getPersistence() ) return -1;
			return 0;
		}
	}

	/**
	 * The Class ComparePersistenceDescending.
	 */
	public static class ComparePersistenceDescending implements Comparator<AugmentedJoinTreeNode> {
		
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override public int compare(AugmentedJoinTreeNode o1, AugmentedJoinTreeNode o2) {
			if( o1.getPersistence() < o2.getPersistence() ) return  1;
			if( o1.getPersistence() > o2.getPersistence() ) return -1;
			return 0;
		}
	}

	
	/**
	 * The Enum NodeType.
	 */
	public enum NodeType {
		LEAF_MIN, LEAF_MAX, MERGE, SPLIT, UNKNOWN
	}
	
	private int location;
	float value;
	AugmentedJoinTreeNode child0;
	AugmentedJoinTreeNode child1;
	AugmentedJoinTreeNode partner;
	
	/**
	 * Instantiates a new augmented join tree node.
	 *
	 * @param loc the loc
	 * @param val the val
	 */
	public AugmentedJoinTreeNode( int loc, float val ){
		this.location = loc;
		this.value = val;
	}
	
	/**
	 * Instantiates a new augmented join tree node.
	 *
	 * @param loc the loc
	 * @param val the val
	 * @param c0 the c 0
	 * @param c1 the c 1
	 */
	public AugmentedJoinTreeNode( int loc, float val, AugmentedJoinTreeNode c0, AugmentedJoinTreeNode c1 ){
		this.location = loc;
		this.value = val;
		this.child0 = c0;
		this.child1 = c1;
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public abstract NodeType getType();
	
	/**
	 * Gets the birth.
	 *
	 * @return the birth
	 */
	public float getBirth(){
		if( partner == null ) return Float.NaN;
		return Math.min( value, partner.value );
	}
	
	/**
	 * Gets the death.
	 *
	 * @return the death
	 */
	public float getDeath(){
		if( partner == null ) return Float.NaN;
		return Math.max( value, partner.value );
	}
	
	/**
	 * Gets the persistence.
	 *
	 * @return the persistence
	 */
	public float getPersistence() {
		if( partner == null ) return Float.NaN;
		return getDeath()-getBirth();
	}
	
	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public int getLocation() {
		return location;
	}
	
	/**
	 * Gets the children.
	 *
	 * @return the children
	 */
	public Vector<AugmentedJoinTreeNode> getChildren(){
		Vector<AugmentedJoinTreeNode> ret = new Vector<AugmentedJoinTreeNode>();
		if( child0 != null ) ret.add(child0);
		if( child1 != null ) ret.add(child1);
		return ret;
	}
	
	/**
	 * Gets the partner.
	 *
	 * @return the partner
	 */
	public AugmentedJoinTreeNode getPartner(){
		return partner;
	}
	
	/**
	 * The Interface Factory.
	 */
	public interface Factory {
		
		/**
		 * Creates the node.
		 *
		 * @param loc the loc
		 * @param val the val
		 * @return the augmented join tree node
		 */
		public AugmentedJoinTreeNode createNode( int loc, float val );
		
		/**
		 * Creates the node.
		 *
		 * @param loc the loc
		 * @param val the val
		 * @param c0 the c 0
		 * @param c1 the c 1
		 * @return the augmented join tree node
		 */
		public AugmentedJoinTreeNode createNode( int loc, float val, AugmentedJoinTreeNode c0, AugmentedJoinTreeNode c1  );

	}
	
}
