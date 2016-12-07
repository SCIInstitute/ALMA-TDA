package usf.saav.common.mvc;

import java.util.Comparator;
import java.util.Vector;

import usf.saav.common.types.Pair;

public interface ControllerComponent extends PositionedComponent {

	
	public boolean keyPressed( char key );
	public boolean mouseMoved( int mouseX, int mouseY );	
	public boolean mouseDragged( int mouseX, int mouseY );	
	public boolean mousePressed( int mouseX, int mouseY );	
	public boolean mouseDoubleClick( int mouseX, int mouseY );	
	public boolean mouseReleased();
	public boolean mouseWheel(int mouseX, int mouseY, float count);
	
	
	abstract class Default extends PositionedComponent.Default implements ControllerComponent {
		
		protected Default() { }
		protected Default(boolean verbose) { super(verbose); }

		@Override public boolean keyPressed(char key) { return false; }

		@Override public boolean mouseMoved(int mouseX, int mouseY) { return false; }

		@Override public boolean mouseDragged(int mouseX, int mouseY) { return false; }

		@Override public boolean mousePressed(int mouseX, int mouseY) {	return false; }

		@Override public boolean mouseDoubleClick(int mouseX, int mouseY) {	return false; }

		@Override public boolean mouseReleased() { return false; }

		@Override public boolean mouseWheel(int mouseX, int mouseY, float count) { return false; }
		
	}
	
	class Subcontroller extends Default implements ControllerComponent {
		protected Vector< Pair<Integer,ControllerComponent> > subcontrols = new Vector< Pair<Integer,ControllerComponent> >();
		
		protected Subcontroller() { }
		protected Subcontroller(boolean verbose) { super(verbose); }
		
		public void registerSubController( ControllerComponent ctl, int priority ){
			subcontrols.add( new Pair<Integer,ControllerComponent>(priority,ctl) );
			subcontrols.sort( new Comparator<Pair<Integer, ControllerComponent>>() {
				@Override
				public int compare(Pair<Integer, ControllerComponent> o1, Pair<Integer, ControllerComponent> o2) {
					return o1.getFirst()-o2.getFirst();
				} 
			});
		}

		public void unregisterSubController( ControllerComponent ctl ){
			for(int i = 0; i < subcontrols.size(); i++){
				if( subcontrols.get(i).getSecond().equals(ctl) ){
					subcontrols.remove(i);
					return;
				}
			}
		}
		

		public void unregisterAll( ){
			subcontrols.clear();
		}
				
		@Override public void setup( ){ 
			for(int i = 0; i < subcontrols.size(); i++){
				subcontrols.get(i).getSecond().setup();
			}
		}
		
		@Override public void update( ){
			if( !isEnabled() ) return;
			for(int i = 0; i < subcontrols.size(); i++){
				subcontrols.get(i).getSecond().update();
			}
		}

		@Override
		public boolean keyPressed( char key ){
			if( !isEnabled() ) return false;
			for(int i = 0; i < subcontrols.size(); i++){
				if( subcontrols.get(i).getSecond().keyPressed(key) ) return true;
			}
			return false;
		}
		
		@Override
		public boolean mouseMoved(int mouseX, int mouseY) {
			if( !isEnabled() ) return false;
			for(int i = 0; i < subcontrols.size(); i++){
				if( subcontrols.get(i).getSecond().mouseMoved(mouseX,mouseY) ) return true;
			}
			return false;
		}

		@Override
		public boolean mousePressed( int mouseX, int mouseY ) {
			if( !isEnabled() ) return false;
			for(int i = 0; i < subcontrols.size(); i++){
				if( subcontrols.get(i).getSecond().mousePressed(mouseX,mouseY) ) return true;
			}
			return false;
		}


		@Override
		public boolean mouseDoubleClick( int mouseX, int mouseY ) {
			if( !isEnabled() ) return false;
			for(int i = 0; i < subcontrols.size(); i++){
				if( subcontrols.get(i).getSecond().mouseDoubleClick(mouseX,mouseY) ) return true;
			}
			return false;
		}
		
		@Override
		public boolean mouseDragged( int mouseX, int mouseY ) {
			if( !isEnabled() ) return false;
			for(int i = 0; i < subcontrols.size(); i++){
				if( subcontrols.get(i).getSecond().mouseDragged(mouseX,mouseY) ) return true;
			}
			return false;
		}
		
		@Override
		public boolean mouseReleased() {
			if( !isEnabled() ) return false;
			for(int i = 0; i < subcontrols.size(); i++){
				if( subcontrols.get(i).getSecond().mouseReleased() ) return true;
			}
			return false;
		}
		
		@Override
		public boolean mouseWheel(int mouseX, int mouseY, float count) {
			
			if( !isEnabled() ) return false;
			for(int i = 0; i < subcontrols.size(); i++){
				if( subcontrols.get(i).getSecond().mouseWheel(mouseX,mouseY,count) ){
					return true;
				}
			}
			return false;
		}

	}
}
