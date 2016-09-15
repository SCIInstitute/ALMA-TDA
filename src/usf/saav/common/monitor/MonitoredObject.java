package usf.saav.common.monitor;

import java.util.Vector;

import usf.saav.common.BasicObject;
import usf.saav.common.Callback;

public abstract class MonitoredObject< ObjectType > extends BasicObject {

	Vector<Callback> callbacksModified = new Vector<Callback>();
	Vector<Callback> callbacksValue    = new Vector<Callback>();
	
	ObjectType value = null;

	protected abstract Class<?> getClassType( );

	public MonitoredObject( ) {	}

	public MonitoredObject(ObjectType initial_value) {
		value = initial_value;
	}
	
	public ObjectType get(){ 
		return value; 
	}
	
	public void set(ObjectType newVal){
		if( value == null && newVal == null ) return;
		if( value == null || !value.equals(newVal) ){
			value = newVal;
			for( Callback cb : callbacksValue    ){ cb.call( value ); }
			for( Callback cb : callbacksModified ){ cb.call( ); }
		}
	}
	
	public String toString() {
		return value.toString();
	}

	
	public void addMonitor( Object obj, String func_name ){
		try {
			callbacksValue.add( new Callback( obj, func_name, getClassType() ) );
			return;
		} catch (NoSuchMethodException e) {	/*e.printStackTrace();*/ }
		try {
			callbacksModified.add( new Callback( obj, func_name ) );
			return;
		} catch (NoSuchMethodException e) {	/*e.printStackTrace();*/ }
		print_error_message( "Function not found " + obj.getClass().getSimpleName() + "." + func_name + "(" + getClassType().getSimpleName() + ")" );
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

