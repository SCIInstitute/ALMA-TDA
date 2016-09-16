package usf.saav.common.graph;

import java.util.Vector;


public class GraphNode<T extends GraphNode<?>> {

		private Vector<GraphNode<T>> nodeList = new Vector<GraphNode<T>>();
		private int flag;
		private int id;

		public GraphNode( int id ){
			this.id = id;
		}

		public int getID(){
			return id;
		}
		
		
		
		
		public void setFlag(int f) {
			flag = f;
		}
		
		public int getFlag( ){
			return flag;
		}

		public boolean isFlagSet() {
			return flag != 0;
		}

		public void clearFlag() {
			flag = 0;
		}
		
		
		
		public void removeEdge(GraphNode<T> to) {
			nodeList.remove(to);
		}

		public void removeAllEdges() {
			nodeList.clear();
		}
		

		public boolean containsEdge( GraphNode<T> to ){
			return nodeList.contains(to);
		}
		
		
		public void addEdge( GraphNode<T> to, boolean allowDuplicates, boolean allowSelfEdges ){
			if( !allowDuplicates && nodeList.contains(to) ) return;
			if( !allowSelfEdges  && this == to ) return;
			nodeList.add(to);
		}

		
		
		public Vector< GraphNode<T> > getNeighbors() {
			return nodeList;
		}

		public int getNeighborCount() {
			return nodeList.size();
		}

		public static class Default extends GraphNode<Default> {

			public Default(int id) {
				super(id);
			}
			
		}

}
