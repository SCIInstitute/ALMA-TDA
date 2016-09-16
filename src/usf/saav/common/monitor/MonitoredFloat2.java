package usf.saav.common.monitor;

import java.util.Arrays;
import usf.saav.common.BasicObject;
import usf.saav.common.CallbackSet;

public class MonitoredFloat2 extends BasicObject {
	
	CallbackSet callbacksModified = new CallbackSet();
	CallbackSet callbacksArray    = new CallbackSet( float[].class );
	CallbackSet callbacksValues   = new CallbackSet(float.class, float.class);
	
	float [] value = {0,0};
	
	public MonitoredFloat2() {	}

	public MonitoredFloat2( float initial_value0, float initial_value1 ) {
		value[0] = initial_value0;
		value[1] = initial_value1;
	}

	public MonitoredFloat2( float [] initial_value ) {
		value[0] = initial_value[0];
		value[1] = initial_value[1];
	}
	
	public float [] get(){ 
		return value.clone(); 
	}

	public void set(float newVal0, float newVal1){
		if( value[0] != newVal0 || value[1] != newVal1 ){
			value[0] = newVal0;
			value[1] = newVal1;
			callbacksValues.call( value[0], value[1] );
			callbacksArray.call( value.clone() );
			callbacksModified.call( );
		}
	}

	public void set(float [] newVal){
		set(newVal[0],newVal[1]);
	}

	public String toString() {
		return Arrays.toString(value);
	}

	public void addEqual(float i0, float i1) {
		set( value[0] + i0, value[1] + i1 );			
	}
	
	public void addMonitor( Object obj, String func_name ){
		if( callbacksValues.add( obj, func_name ) ) return;
		if( callbacksArray.add( obj, func_name ) ) return;
		if( callbacksModified.add( obj, func_name ) ) return;
		print_error_message( "Function not found" );
	}
	
	public void removeMonitor( Object obj, String func_name ){
		if( callbacksValues.remove( obj, func_name ) ) return;
		if( callbacksArray.remove( obj, func_name ) ) return;
		if( callbacksModified.remove( obj, func_name ) ) return;
		print_error_message( "Function not found" );
	}
}

