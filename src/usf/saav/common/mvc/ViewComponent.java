package usf.saav.common.mvc;

import java.util.Comparator;
import java.util.Vector;

import usf.saav.common.mvc.swing.TGraphics;
import usf.saav.common.types.Pair;

public interface ViewComponent extends PositionedComponent {

	
	public void draw( TGraphics g );	

	public void drawLegend( TGraphics g );	
	
	
	public abstract class Default extends PositionedComponent.Default implements ViewComponent {
		
		protected Default( ){ }
		protected Default(boolean verbose) { super(verbose); }
		
		@Override public void draw( TGraphics g ){ }

		@Override public void drawLegend( TGraphics g ){ }

	}
	
	

	class Subview extends Default implements ViewComponent {
		protected Vector< Pair<Integer,ViewComponent> > subviews = new Vector< Pair<Integer,ViewComponent> >();
		
		protected Subview() { }
		

		public void registerSubView( ViewComponent ctl, int priority ){
			subviews.add( new Pair<Integer,ViewComponent>(priority,ctl) );
			subviews.sort( new Comparator<Pair<Integer, ViewComponent>>() {
				@Override
				public int compare(Pair<Integer, ViewComponent> o1, Pair<Integer, ViewComponent> o2) {
					return o1.getFirst()-o2.getFirst();
				} 
			});
		}
		
		public void unregisterSubView( ViewComponent ctl ){
			for(int i = 0; i < subviews.size(); i++){
				if( subviews.get(i).getSecond().equals(ctl) ){
					subviews.remove(i);
					return;
				}
			}
		}
		
		public void unregisterAll( ){
			subviews.clear();
		}
						
		@Override 
		public void draw( TGraphics g ){ 
			if( !isEnabled() ) return;
			for(int i = 0; i < subviews.size(); i++){
				subviews.get(i).getSecond().draw(g);
				g.flush();
			}
		}

		@Override 
		public void drawLegend( TGraphics g ){ 
			if( !isEnabled() ) return;
			for(int i = 0; i < subviews.size(); i++){
				subviews.get(i).getSecond().drawLegend(g);
				g.flush();
			}
		}

		@Override 
		public void setup( ) {
			for(int i = 0; i < subviews.size(); i++){
				subviews.get(i).getSecond().setup();
			}
		}

		@Override 
		public void update() {
			if( !isEnabled() ) return;
			for(int i = 0; i < subviews.size(); i++){
				subviews.get(i).getSecond().update();
			}
		}
		

	}
}
