package usf.saav.common.graph;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

public class Graph<T extends GraphNode<?>> {

	Vector< GraphNode<T> > verts = new Vector< GraphNode<T> > ();
	
	public Graph( ){ }
	
	private Graph( Vector<GraphNode<T>> subtree ) {
		for(GraphNode<T> n : subtree){
			verts.add( n );
		}
	}

	public GraphNode<T> add( ){
		GraphNode<T> ret = new GraphNode<T>( verts.size() );
		verts.add( ret );
		return ret;
	}

	public GraphNode<T> get( int idx ){
		return verts.get(idx);
	}
	

	public boolean addUndirectedEdge(GraphNode<T> obj0, GraphNode<T> obj1) {
		obj0.addEdge( obj1, false, false );
		obj1.addEdge( obj0, false, false );
		return true;
	}

	public boolean addDirectedEdge(GraphNode<T> from, GraphNode<T> to) {
		from.addEdge( to, false, false );
		return true;
	}
	
	public void removeEdge( GraphNode<T> n0, GraphNode<T> n1 ){
		n0.removeEdge(n1);
		n1.removeEdge(n0);
	}


	
	public int size() {
		return verts.size();
	}
	
	private void clearFlags( ){
		for( GraphNode<T> n : verts ){
			n.clearFlag( );
		}
	}
	
	public Vector<Graph<T>> findConnectComponents( ){
		clearFlags();
		Vector<Graph<T>> ret = new Vector<Graph<T>>();
		for( GraphNode<T> n : verts ){
			if( !n.isFlagSet() ){
				ret.add( new Graph<T>( breadthFirstSearch( n ) ) );
			}
		}
		return ret;		
	}

	public Vector<GraphNode<T>> breadthFirstSearch(GraphNode<T> startNode ) {
		Vector< GraphNode<T> > ret = new Vector< GraphNode<T> >( );
		Queue< GraphNode<T> > wq = new LinkedList< GraphNode<T> >( );
		
		wq.add(startNode);
		startNode.setFlag(1);
		
		while( !wq.isEmpty() ){
			GraphNode<T> n = wq.poll();
			ret.add(n);
			for( GraphNode<T> x : n.getNeighbors() ){
				if( !x.isFlagSet() ){
					x.setFlag(1);
					wq.add(x);
				}
			}
		}
		return ret;
	}
	

	public Vector<GraphNode<T>> depthFirstSearch(GraphNode<T> startNode) {
		Vector< GraphNode<T> > ret = new Vector< GraphNode<T> >( );
		Stack< GraphNode<T> > wq = new Stack< GraphNode<T> >( );
		
		wq.push(startNode);
		startNode.setFlag(1);
		
		while( !wq.isEmpty() ){
			GraphNode<T> n = wq.pop();
			ret.add(n);
			Vector< GraphNode<T> > children = n.getNeighbors();
			for(int i = children.size()-1; i >= 0; i-- ){
				GraphNode<T> x = children.get(i);
				if( !x.isFlagSet() ){
					x.setFlag(1);
					wq.push(x);
				}
			}
		}
		return ret;

	}

	public int getID(GraphNode<T> n) {
		return verts.indexOf(n);		
	}
	
	public static class Default extends Graph< GraphNode.Default > {
		
	}

}
