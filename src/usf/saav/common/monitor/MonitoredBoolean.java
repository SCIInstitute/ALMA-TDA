package usf.saav.common.monitor;

import java.util.Vector;

import usf.saav.common.BasicObject;
import usf.saav.common.Callback;

public class MonitoredBoolean extends BasicObject {
	
	Vector<Callback> callbacksModified = new Vector<Callback>();
	Vector<Callback> callbacksValue    = new Vector<Callback>();
	
	boolean value = false;
	
	public MonitoredBoolean() {	}
	
	public MonitoredBoolean(boolean initial_value) {
		value = initial_value;
	}
	
	public boolean get(){ 
		return value; 
	}
	
	public void set(boolean newVal){
		if( value != newVal ){
			value = newVal;
			for( Callback cb : callbacksValue    ){ cb.call( value ); }
			for( Callback cb : callbacksModified ){ cb.call( ); }
		}
	}

	public void flip() {
		set( !value );
	}

	
	public String toString() {
		return Boolean.toString(value);
	}

	
	public void addMonitor( Object obj, String func_name ){
		try {
			callbacksValue.add( new Callback( obj, func_name, boolean.class ) );
			return;
		} catch (NoSuchMethodException e) {	/*e.printStackTrace();*/ }
		try {
			callbacksModified.add( new Callback( obj, func_name ) );
			return;
		} catch (NoSuchMethodException e) {	/*e.printStackTrace();*/ }
		print_error_message( func_name + " not found in " + obj.getClass().getSimpleName() );
	}

	public void removeMonitor( Object obj, String func_name ){
		for( Callback cb : callbacksValue ){ 
			if( cb.equals( obj, func_name ) ){
				callbacksValue.remove(cb);
				return;
			}
		}
		for( Callback cb : callbacksModified ){ 
			if( cb.equals( obj, func_name ) ){
				callbacksModified.remove(cb);
				return;
			}
		}

	}
	
}
