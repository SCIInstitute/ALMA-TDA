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
	
	public String listCallbacks( ){
		StringBuffer sb = new StringBuffer();
		for( Callback cb : callbacksModified ){
			sb.append( cb.getTargetObject().getClass().getSimpleName() + "." + cb.getTargetFunc() + "( )\n" );
		}
		return sb.toString();
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
