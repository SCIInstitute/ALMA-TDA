/*
 *     saav-core - A (very boring) software development support library.
 *     Copyright (C) 2016 PAUL ROSEN
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     You may contact the Paul Rosen at <prosen@usf.edu>.
 */
package usf.saav.common.monitoredvariables;

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
	
	public Object getTargetObject(){ return obj; }
	public String getTargetFunc(){ return func_name; }
	
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