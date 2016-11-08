package usf.saav.common.monitor;

import java.util.Vector;

import usf.saav.common.BasicObject;
import usf.saav.common.Callback;

public class MonitoredFloat extends BasicObject {
	
	Vector<Callback> callbacksModified = new Vector<Callback>();
	Vector<Callback> callbacksValue    = new Vector<Callback>();
	
	float value = 0;
	
	public MonitoredFloat() {	}
	
	public MonitoredFloat( float initial_value) {
		value = initial_value;
	}
	
	public float get(){ 
		return value; 
	}
	
	public void set(float newVal){
		if( value != newVal ){
			value = newVal;
			for( Callback cb : callbacksValue    ){ cb.call( value ); }
			for( Callback cb : callbacksModified ){ cb.call( ); }
		}
	}

	public void incr() {
		set( value+1 );
	}

	public void decr() {
		set(value-1);
	}
	
	public String toString() {
		return Float.toString(value);
	}

	public void addEqual(float i) {
		set( value + i );			
	}
	
	public void addMonitor( Object obj, String func_name ){
		try {
			callbacksValue.add( new Callback( obj, func_name, float.class ) );
			return;
		} catch (NoSuchMethodException e) {	/*e.printStackTrace();*/ }
		try {
			callbacksModified.add( new Callback( obj, func_name ) );
			return;
		} catch (NoSuchMethodException e) {	/*e.printStackTrace();*/ }
		print_error_message( "Function ``" + func_name + "'' not found" );
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

