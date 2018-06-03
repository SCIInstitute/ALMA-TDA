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
