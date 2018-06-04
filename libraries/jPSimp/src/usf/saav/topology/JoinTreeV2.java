package usf.saav.topology;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import usf.saav.common.algorithm.HashDisjointSet;
import usf.saav.mesh.Mesh;
import usf.saav.mesh.ScalarFieldMesh;
import usf.saav.scalarfield.ScalarField2D;

public class JoinTreeV2 implements Runnable {
 
	protected Comparator<? super JNode> comparator;
	protected Mesh sf;
	protected JoinTreeNode head;
	protected boolean operationComplete = false;
	protected ArrayList<JoinTreeNode> grid = new ArrayList<JoinTreeNode>();

	public JoinTreeV2( Mesh sf ) {
		this( sf, new JNode.ComparatorValueAscending() );
	}
	
	public JoinTreeV2( Mesh sf, Comparator<? super JNode> comparator  ) {
		this.sf = sf;
		this.comparator = comparator;
	}

	
	public JoinTreeNode getRoot( ){
		if( !operationComplete ) return null;
		return head;
	}
		
	public String toString( ){
		if( head == null ){ return "<empty>"; }
		return head.toString();
	}
	
	public int size() {
		return grid.size();
	}

	public float getBirth(int i) {
		return grid.get(i).getBirth();
	}

	public float getDeath(int i) {
		return grid.get(i).getDeath();
	}

	public float getPersistence(int i) {
		return grid.get(i).getPersistence();
	}

	public TopoTreeNode getNode(int i) {
		return grid.get(i);
	}	
	

	public String toDot( ){
		if( head == null ){ return "Digraph{\n}"; }
		else {
			StringBuffer dot_node = new StringBuffer( );
			StringBuffer dot_edge = new StringBuffer( );
			head.toDot( dot_node, dot_edge );
			return "Digraph{\n" + dot_node + dot_edge + "}"; 
		}
	}

	public String toDot( int maxdepth ){
		if( head == null ){ return "Digraph{\n}"; }
		else {
			StringBuffer dot_node = new StringBuffer( );
			StringBuffer dot_edge = new StringBuffer( );
			head.toDot( dot_node, dot_edge, maxdepth );
			return "Digraph{\n" + dot_node + dot_edge + "}"; 
		}
	}

	



	@Override
	public void run() {
		
		if( operationComplete ) return;

		int size = sf.getWidth();
		//this.size = sf.getWidth();
		//grid = new Node[size];

		// Order the points for adding to the tree.
		Queue< JNode > tq = new PriorityQueue< JNode >( size, comparator );
		for(int i = 0; i < size; i++ ){
			grid.add( new JNode( sf.get(i).value(), i ) );
			tq.add( (JNode)grid.get(i) );
		}

		// Disjoint Set used to mark which set a points belongs to
		HashDisjointSet<JNode> djs = new HashDisjointSet<JNode>( );
		
		// start popping elements off the of the list
		while( tq.size() > 0 ){
			JNode me = tq.poll();

			// set any neighbor sets as children
			for( int _n : sf.get( me.getPosition() ).neighbors() ){
				JNode n = (JNode)grid.get(_n);
				if( comparator.compare( n, me ) < 0 ) {
					//Node r0 = djs.find( me );
					JNode r1 = djs.find( n );
					if( me != r1 ) {
						djs.union( me, r1 );
						me.addChild( r1 );
					}
					
				}
			}
			head = me;
			
		}
		
		correctMonkeySaddles();
		setParents( );
		calculatePersistence();
		
		operationComplete = true;
		
	}
	
	
	protected void correctMonkeySaddles( ) {
		Queue<JoinTreeNode> proc = new LinkedList<JoinTreeNode>();
		
		proc.add(head);
		while( !proc.isEmpty() ) {
			JoinTreeNode curr = proc.poll();
			if( curr.childCount() > 2 ) {
				//System.out.println("Monkey Saddle " + curr.getPosition());
				//System.out.println( curr );
				JNode newNode = new JNode( curr.getValue(), curr.getPosition() );
				while( curr.childCount() > 1 ) {
					newNode.addChild( curr.children.remove(1) );
				}
				curr.addChild(newNode);
				grid.add(newNode);
				//System.out.println( curr );
			}
			proc.addAll(curr.children);
		}		
	}


	protected void setParents( ) {
		for( JoinTreeNode curr : grid ) {
			for( JoinTreeNode child : curr.getChildren() ) {
				child.setParent( curr );
			}
		}		
	}

	
	private JoinTreeNode getNextCritical( JoinTreeNode curr ) {
		while( curr.childCount() == 1 ) { 
			curr = curr.getChild(0); 
		}
		return curr;
	}

	protected void calculatePersistence(){
		//print_info_message( "Finding Persistence");
		
		Stack<JoinTreeNode> pstack = new Stack<JoinTreeNode>( );
		pstack.push( getNextCritical(this.head) );
		
		while( !pstack.isEmpty() ){
			JoinTreeNode curr = pstack.pop();
			//System.out.println(curr.getPosition() + " " + curr.getChildCount());
			
			// leaf is only thing in the stack, done
			if( pstack.isEmpty() && curr.childCount() == 0 ) break;			
			
			// saddle point, push children onto stack
			if( curr.childCount() == 2 ){
				pstack.push(curr);
				pstack.push( getNextCritical( curr.getChild(0) ) );
				pstack.push( getNextCritical( curr.getChild(1) ) );
			}

			// leaf node, 2 options
			if( curr.childCount() == 0 && pstack.size() >= 2 ) {
				JoinTreeNode sibling = pstack.pop();
				JoinTreeNode parent  = pstack.pop();
				
				// sibling is a saddle, restack.
				if( sibling.childCount() == 2 ){
					pstack.push( parent );
					pstack.push( curr );
					pstack.push( sibling );
				}
				
				// sibling is a leaf, we can match a partner.
				if( sibling.childCount() == 0 ){
					// curr value is closer to parent than sibling
					if( Math.abs(curr.getValue()-parent.getValue()) < Math.abs(sibling.getValue()-parent.getValue()) ){
						curr.setPartner(parent);
						parent.setPartner(curr);
						pstack.push( sibling );
					}
					// sibling value is closer to parent than curr
					else {
						sibling.setPartner(parent);
						parent.setPartner(sibling);
						pstack.push( curr );
					}
					//max_persistence = Math.max(max_persistence,parent.getPersistence());
				}
			}
		}
	
		
	}
		
	
	public class JNode extends JoinTreeNode {

		private int   position;
		private float value;

		public JNode( float value, int position ) {
			this.position = position;
			this.value 	  = value;
		}

		@Override public float getValue( ){ return value; }
		@Override public int   getPosition( ){ return position; }

		@Override public NodeType getType() {
			switch( this.childCount() ) {
				case 0  : return NodeType.LEAF;
				case 1  : return NodeType.NONCRITICAL;
				default : return NodeType.SADDLE;
			}
		}	

	}

	
	
	public static void main( String args[] ){
		ScalarField2D sf = new ScalarField2D.RandomField( 10, 10 );
		JoinTreeV2 jt = new JoinTreeV2( new ScalarFieldMesh( sf ) );
		jt.run();
		System.out.println(sf.toString());
		System.out.println(jt);
	}
	
	
}
