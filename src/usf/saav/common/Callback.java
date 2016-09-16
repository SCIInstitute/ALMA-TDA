package usf.saav.common;

import java.lang.reflect.InvocationTargetException;

public class Callback {
	
	java.lang.reflect.Method method = null;
	Object obj;
	String func_name;
	
	public Callback( Object obj, String func_name, Class<?> ... parameterTypes ) throws NoSuchMethodException {
		this.obj = obj;
		this.func_name = func_name;
		try {
		  this.method = obj.getClass().getMethod( func_name, parameterTypes );
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public boolean call( Object ... args ){
		try {
			method.invoke( obj, args);
		} catch (IllegalAccessException | IllegalArgumentException  | InvocationTargetException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean equals(Object obj, String func_name) {
		return this.obj.equals(obj) && this.func_name.equals(func_name);
	}
	
	
}