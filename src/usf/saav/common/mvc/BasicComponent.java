package usf.saav.common.mvc;

import usf.saav.common.BasicObject;

public interface BasicComponent {
	
	public void setup( );
	public void update( );
		
	public void enable( );
	public void disable( );
	public void setEnabled( boolean enb );
	public boolean isEnabled( );

	public class Default extends BasicObject implements BasicComponent {

		private boolean enabled = true; 
		
		protected Default( ){ }
		protected Default(boolean verbose) { super(verbose); }
		
		@Override public void setup( ){ }
		@Override public void update( ){ }

		public void enable( ){ enabled = true; }
		public void disable( ){ enabled = false; }
		public void setEnabled( boolean enb ){ enabled = enb; }
		public boolean isEnabled( ){ return enabled; }

	}
}
