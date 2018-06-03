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

import java.util.Vector;

import usf.saav.common.BasicObject;

public class MonitoredInteger extends BasicObject {
	
	Vector<Callback> callbacksModified = new Vector<Callback>();
	Vector<Callback> callbacksValue    = new Vector<Callback>();
	
	int value = 0;
	
	public MonitoredInteger() {	}
	
	public MonitoredInteger(int initial_value) {
		value = initial_value;
	}
	
	public int get(){ 
		return value; 
	}
	
	public void set(int newVal){
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
		return Integer.toString(value);
	}

	public void addEqual(int i) {
		set( value + i );			
	}
	
	public String listCallbacks( ){
		StringBuffer sb = new StringBuffer();
		for( Callback cb : callbacksValue ){
			sb.append( cb.getTargetObject().getClass().getSimpleName() + "." + cb.getTargetFunc() + "( " + int.class.getSimpleName() + " )\n" );
		}
		for( Callback cb : callbacksModified ){
			sb.append( cb.getTargetObject().getClass().getSimpleName() + "." + cb.getTargetFunc() + "( )\n" );
		}
		return sb.toString();
	}
	
	public void addMonitor( Object obj, String func_name ){
		try {
			callbacksValue.add( new Callback( obj, func_name, int.class ) );
			return;
		} catch (NoSuchMethodException e) {	/*e.printStackTrace();*/ }
		try {
			callbacksModified.add( new Callback( obj, func_name ) );
			return;
		} catch (NoSuchMethodException e) {	/*e.printStackTrace();*/ }
		print_error_message( "Function not found" );
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
