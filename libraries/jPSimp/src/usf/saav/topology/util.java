/*
 *     jPSimp - Persistence calculation and simplification of scalar fields.
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
package usf.saav.topology;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import usf.saav.common.types.Pair;


public class util {

	public static boolean compareTree( JoinTreeNode head, JoinTreeNode pc2 ){
		Queue<Pair<JoinTreeNode,JoinTreeNode>> worklist = new LinkedList<Pair<JoinTreeNode,JoinTreeNode>>();
		worklist.add( new Pair<JoinTreeNode,JoinTreeNode>(head,pc2) );
		
		while( !worklist.isEmpty() ){
			if( !compareTree(worklist) ) return false;
		}
		return true;
	}
	
	private static boolean compareTree( Queue<Pair<JoinTreeNode,JoinTreeNode>> worklist ){
		if( worklist.size() == 0 ) return true;
		
		Pair<JoinTreeNode,JoinTreeNode> roots = worklist.poll();
		JoinTreeNode head = roots.getFirst();
		JoinTreeNode pc2 = roots.getSecond();
		
		
		//System.out.println(head.getLocation() + " " + " | " + proot.getIndex() + " " + proot.getValue( ) );
		if( head == null && pc2 == null ) return true;
		if( head == null || pc2 == null ){
			System.out.println( "empty/non-empty: " + (head==null) + " || " + (pc2 == null) );
			return false;
		}

		if( head.getPosition() != pc2.getPosition() ){
			System.out.println( "head compare: " + head.getPosition() + " != " + pc2.getPosition() );
			return false;
		}
		
		Collection<JoinTreeNode> children0 = ( buildChildList( head ) );
		Collection<JoinTreeNode> children1 = ( buildChildList( pc2 ) );

		//Collection<JoinTreeNode> children0 = fixEqualChildIssue( buildChildList( head ) );
		//Collection<JoinTreeNode> children1 = fixEqualChildIssue( buildChildList( pc2 ) );

		
		if( children0.size() != children1.size() ){
			System.out.println();
			System.out.println( "child size: " + children0.size() + " != " + children1.size() );

			System.out.println( "  head0: " + head.getPosition() + " (" + head.getValue() + ")" );
			System.out.print( "    child0: " );
			for( JoinTreeNode c0 : children0 ) System.out.print( c0.getPosition() + " (" + c0.getValue() + "), " ); System.out.println();
			for( JoinTreeNode c0 : children0 ){
				System.out.print( "      gchild: " );
				for( JoinTreeNode gc0 : c0.getChildren() ) System.out.print( gc0.getPosition() + " (" + gc0.getValue() + "), " ); System.out.println();
			}
			System.out.println( "  head1: " + pc2.getPosition() + " (" + pc2.getValue() + ")" );
			System.out.print( "    child1: " );
			for( JoinTreeNode c1 : children1 ) System.out.print( c1.getPosition() + " (" + c1.getValue() + "), " ); System.out.println();
			for( JoinTreeNode c1 : children1 ){
				System.out.print( "      gchild: " );
				for( JoinTreeNode gc1 : c1.getChildren() ) System.out.print( gc1.getPosition() + " (" + gc1.getValue() + "), " ); System.out.println();
			}			
			return false;
		}
		
		for( JoinTreeNode c0 : children0 ){
			boolean found = false;
			for( JoinTreeNode c1 : children1 ){
				if( c0.getPosition() == c1.getPosition() ){
					found = true;
					worklist.add( new Pair<JoinTreeNode,JoinTreeNode>(c0,c1) );
				}
			}
			if( !found ){
				System.out.println();
				System.out.println( "child not found: " + c0.getPosition() + "(" + c0.getValue() + ")" );
				System.out.println( "  head0: " + head.getPosition() + " (" + head.getValue() + ")" );
				System.out.print( "    child0: " );
				for( JoinTreeNode cc0 : children0 ) System.out.print( cc0.getPosition() + " (" + cc0.getValue() + "), " ); System.out.println();
				for( JoinTreeNode cc0 : children0 ){
					System.out.print( "      gchild: " );
					for( JoinTreeNode gc0 : cc0.getChildren() ) System.out.print( gc0.getPosition() + " (" + gc0.getValue() + "), " ); System.out.println();
				}
				System.out.println( "  head1: " + pc2.getPosition() + " (" + pc2.getValue() + ")" );
				System.out.print( "    child1: " );
				for( JoinTreeNode cc1 : children1 ) System.out.print( cc1.getPosition() + " (" + cc1.getValue() + "), " ); System.out.println();
				for( JoinTreeNode cc1 : children1 ){
					System.out.print( "      gchild: " );
					for( JoinTreeNode gc1 : cc1.getChildren() ) System.out.print( gc1.getPosition() + " (" + gc1.getValue() + "), " ); System.out.println();
				}

				return false;
			}
		}
		return true;

	}
	

	private static Vector<JoinTreeNode> buildChildList( JoinTreeNode head ){
		Vector<JoinTreeNode> ret = new Vector<JoinTreeNode>();
		for( JoinTreeNode n : head.getChildren() ){
			if( n.getPosition() == head.getPosition() ){
				ret.addAll(buildChildList(n));
			}
			else{
				ret.add(n);
			}
		}
		return ret;
	}

	/*
	public static boolean containsEqualValueNodes( JoinTreeNode proot ){
		if( proot == null ) return false;
		for( JoinTreeNode n : proot.getChildren() ){
			for( JoinTreeNode gc : n.getChildren() ){
				if( n.getPosition() != gc.getPosition() && n.getValue() == gc.getValue() ) return true;
			}
			if( containsEqualValueNodes(n) ) return true;
		}
		return false;
	}
	*/
	
	public static boolean containsMonkeySaddle( JoinTreeNode proot ){
		if( proot == null ) return false;
		if( proot.getChildCount() == 3 ) return true;
		for( JoinTreeNode pc : proot.getChildren() ){
			if(containsMonkeySaddle(pc)) return true;
		}
		return false;
	}

	public static boolean contains4waySaddle( JoinTreeNode proot ){
		if( proot == null ) return false;
		if( proot.getChildCount() == 4 ) return true;
		for( JoinTreeNode pc : proot.getChildren() ){
			if(containsMonkeySaddle(pc)) return true;
		}
		return false;
	}

	
}

