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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * The Class JoinTreeNode.
 */
public class JoinTreeNode {

		private int position;
		private float value;
		private JoinTreeNode parent;
		private Vector< JoinTreeNode > children = new Vector< JoinTreeNode >( );

		/**
		 * Instantiates a new join tree node.
		 *
		 * @param value the value
		 * @param position the position
		 */
		public JoinTreeNode( float value, int position ) {
			this.position = position;
			this.value 	  = value;
		}
		
		/**
		 * Sets the parent.
		 *
		 * @param p the new parent
		 */
		public void setParent( JoinTreeNode p ){
			parent = p;
		}
		
		/**
		 * Adds the child.
		 *
		 * @param c the c
		 */
		public void addChild( JoinTreeNode c ){
			children.add(c);
		}
		
		/**
		 * Gets the parent.
		 *
		 * @return the parent
		 */
		public JoinTreeNode getParent( ){
			return parent;
		}
		
		/**
		 * Child count.
		 *
		 * @return the int
		 */
		public int childCount() {
			return children.size();
		}
		

		/**
		 * Gets the child.
		 *
		 * @param idx the idx
		 * @return the child
		 */
		public JoinTreeNode getChild( int idx ){
			return children.get(idx);
		}
		
		/**
		 * Gets the children.
		 *
		 * @return the children
		 */
		public List<JoinTreeNode> getChildren( ){
			return children;
		}
		
		/**
		 * Checks if is child.
		 *
		 * @param node the node
		 * @return true, if is child
		 */
		public boolean isChild(JoinTreeNode node) {
			return children.contains(node);
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public float getValue( ){ return value; }
		
		/**
		 * Gets the position.
		 *
		 * @return the position
		 */
		public int   getPosition( ){ return position; }
		
		/**
		 * Find leaves.
		 *
		 * @param leaf_list the leaf list
		 */
		public void findLeaves( Queue<JoinTreeNode> leaf_list ) {
			Queue<JoinTreeNode> work_queue = new LinkedList<JoinTreeNode>();
			work_queue.add(this);
			
			while( !work_queue.isEmpty() ){
				JoinTreeNode curr = work_queue.poll();
				if( !curr.hasChildren() ) leaf_list.add(curr);
				work_queue.addAll( curr.children );
			}
		}
		
		/**
		 * Prints the.
		 *
		 * @param recursive the recursive
		 */
		public void print( boolean recursive ){
			print( "", false );
		}

		/**
		 * Prints the.
		 *
		 * @param splitTree the split tree
		 * @param recursive the recursive
		 */
		public void print( boolean splitTree, boolean recursive ){
			print( "", splitTree );
		}

		private void print( String spaces, boolean split ){
			if( split ){
				for( JoinTreeNode c : children )
					c.print(spaces + "  ", split );
			}
			
			System.out.println( spaces + value + " : " + position );
			if( !split ){
				for( JoinTreeNode c : children )
					c.print(spaces + "  ", split );
			}
		}

		/**
		 * Checks for parent.
		 *
		 * @return true, if successful
		 */
		public boolean hasParent() {
			return parent != null;
		}
		
		/**
		 * Checks for children.
		 *
		 * @return true, if successful
		 */
		public boolean hasChildren( ){
			return children.size() != 0;
		}

		/**
		 * Removes the child.
		 *
		 * @param node the node
		 */
		public void removeChild(JoinTreeNode node) {
			children.remove(node);
		}
		
		static class ComparatorValueAscending implements Comparator<Object> {
			@Override
			public int compare(Object o1, Object o2) {
				if( o1 instanceof JoinTreeNode && o2 instanceof JoinTreeNode ){
					if( ((JoinTreeNode)o1).value > ((JoinTreeNode)o2).value ) return  1;
					if( ((JoinTreeNode)o1).value < ((JoinTreeNode)o2).value ) return -1;
					if( ((JoinTreeNode)o1).position < ((JoinTreeNode)o2).position ) return  1;
					if( ((JoinTreeNode)o1).position > ((JoinTreeNode)o2).position ) return -1;
				}
				return 0;
			}	
		}

		/**
		 * The Class ComparatorValueDescending.
		 */
		public static class ComparatorValueDescending implements Comparator<Object> {
			
			/* (non-Javadoc)
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			@Override
			public int compare(Object o1, Object o2) {
				if( o1 instanceof JoinTreeNode && o2 instanceof JoinTreeNode ){
					if( ((JoinTreeNode)o1).value > ((JoinTreeNode)o2).value ) return -1;
					if( ((JoinTreeNode)o1).value < ((JoinTreeNode)o2).value ) return  1;
					if( ((JoinTreeNode)o1).position < ((JoinTreeNode)o2).position ) return -1;
					if( ((JoinTreeNode)o1).position > ((JoinTreeNode)o2).position ) return  1;
				}
				return 0;
			}	
		}
		
		static class ComparatorChildCountAscending implements Comparator<Object> {
			@Override
			public int compare(Object o1, Object o2) {
				if( o1 instanceof JoinTreeNode && o2 instanceof JoinTreeNode ){
					return ((JoinTreeNode)o1).childCount() - ((JoinTreeNode)o2).childCount();
				}
				return 0;
			}	
		}

		/**
		 * First common ancestor.
		 *
		 * @param node0 the node 0
		 * @param node1 the node 1
		 * @return the join tree node
		 */
		public static JoinTreeNode firstCommonAncestor(JoinTreeNode node0, JoinTreeNode node1) {
			Vector<JoinTreeNode> list0 = ancestorList(node0);
			Vector<JoinTreeNode> list1 = ancestorList(node1);
			int i = list0.size()-1;
			int j = list1.size()-1;
			while( i >= 0 && j >= 0){
				JoinTreeNode a = list0.get(i);
				JoinTreeNode b = list1.get(j);
				if( a.equals(b) ){
					i--;
					j--;
				}
				else{
					break;
				}
			}
			if( i >= 0 ) return list0.get(i);
			if( j >= 0 ) return list1.get(j);
			return null;
		}
		
		/**
		 * Ancestor list.
		 *
		 * @param node the node
		 * @return the vector
		 */
		public static Vector<JoinTreeNode> ancestorList( JoinTreeNode node ){
			Vector<JoinTreeNode> ret = new Vector<JoinTreeNode>();
			while(node.hasParent()){
				ret.add(node);
				node = node.parent;
			}
			return ret;
		}
		
		/**
		 * Gets the sublevelset.
		 *
		 * @param node the node
		 * @return the sublevelset
		 */
		public static Set<JoinTreeNode> getSublevelset( JoinTreeNode node ){
			HashSet<JoinTreeNode> ret = new HashSet<JoinTreeNode>( );
			Queue<JoinTreeNode> proc = new LinkedList<JoinTreeNode>( );
			proc.add(node);
			while( proc.size() > 0 ){
				JoinTreeNode curr = proc.poll();
				proc.addAll( curr.getChildren() );
				ret.add(curr);
			}
			return ret;
		}
}
