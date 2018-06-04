package usf.saav.topology;

import java.util.Comparator;

import usf.saav.mesh.Mesh;


public class AugmentedJoinTreeV2 extends JoinTreeV2 {

	protected JoinTreeV2 jt;
	
	public AugmentedJoinTreeV2( JoinTreeV2 _jt ) {
		super(_jt.sf,_jt.comparator);
		this.jt = _jt;
	}
	
	public AugmentedJoinTreeV2( Mesh sf ) {
		super(sf);
		jt = new JoinTreeV2(sf,this.comparator);
	}
	
	public AugmentedJoinTreeV2( Mesh sf, Comparator<? super JNode> comparator  ) {
		super(sf,comparator);
		jt = new JoinTreeV2(sf,comparator);
	}
	
	
	@Override
	public void run() {
		
		if( operationComplete ) return;

		jt.run();

		head = processTree( jt.head );
		setParents( );
		calculatePersistence();
		
		operationComplete = true;
		
	}
	
	protected AJNode processTree( JoinTreeNode current ){
		
		AJNode ret= null;
		while( current.childCount() == 1 ){
			current = current.getChild(0);
		}
		if( current.childCount() == 0 ){
			ret = new AJNode( current.getPosition(), current.getValue() );
		}
		if( current.childCount() == 2 ){
			ret = new AJNode( current.getPosition(), current.getValue(),
							processTree( current.getChild(0) ),
							processTree( current.getChild(1) )  );
		}

		if( ret != null )
			grid.add( ret );
		
		return ret;

	}
	
	

	protected class AJNode extends JoinTreeNode {
		
		private int   location;
		private float value;
		
		
		protected AJNode( int loc, float val ){
			this.location = loc;
			this.value = val;
		}
		
		protected AJNode( int loc, float val, AJNode c0, AJNode c1 ){
			this.location = loc;
			this.value = val;
			this.addChild(c0);
			this.addChild(c1);
		}
		
		
		@Override public int	getPosition() { return location; }
		@Override public float	getValue() { 	return value;	 }

		@Override public NodeType getType() {
			switch( this.childCount() ) {
				case 0  : return NodeType.LEAF;
				case 1  : return NodeType.NONCRITICAL;
				default : return NodeType.SADDLE;
			}
		}	



	}
	
	
	
	
}
