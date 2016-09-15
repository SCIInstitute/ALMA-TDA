package usf.saav.common.monitor;

import java.util.Vector;

import usf.saav.common.BasicObject;
import usf.saav.common.Callback;

public class MonitoredTrigger extends BasicObject {

	Vector<Callback> callbacksModified = new Vector<Callback>();
	
	String name;
		
	public MonitoredTrigger() {	}
	public MonitoredTrigger(String name) { this.name = name;	}
		
	public void trigger(){
		for( Callback cb : callbacksModified ){ cb.call( ); }
	}

	public String toString() {
		return "MonitoredTrigger (" + name + ")";
	}
	
	public void addMonitor( Object obj, String func_name ){
		try {
			callbacksModified.add( new Callback( obj, func_name ) );
			return;
		} catch (NoSuchMethodException e) {	/*e.printStackTrace();*/ }
		print_error_message( "Function not found" );
	}

	public void removeMonitor( Object obj, String func_name ){
		for( Callback cb : callbacksModified ){ 
			if( cb.equals( obj, func_name ) ){
				callbacksModified.remove(cb);
				return;
			}
		}

	}
}
