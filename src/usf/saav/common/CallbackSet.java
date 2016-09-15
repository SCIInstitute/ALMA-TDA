package usf.saav.common;

import java.util.Vector;

public class CallbackSet {

	Vector<Callback> callbacks = new Vector<Callback>();
	Class<?> [] params;

	public CallbackSet( Class<?> ... params ){
		this.params = params;
	}
	
	public void call( Object ... args ){
		for( Callback cb : callbacks ){ 
			cb.call( args ); 
		}
	}
	
	public boolean add( Object obj, String func_name ){
		try {
			callbacks.add( new Callback( obj, func_name, params ) );
			return true;
		} catch (NoSuchMethodException e) {	/*e.printStackTrace();*/ }
		return false;
	}
	
	public boolean remove( Object obj, String func_name ){
		for( Callback cb : callbacks ){ 
			if( cb.equals( obj, func_name ) ){
				callbacks.remove(cb);
				return true;
			}
		}
		return false;
	}

}
