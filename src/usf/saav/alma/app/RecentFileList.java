/*
 *     ALMA TDA - Contour tree based simplification and visualization for ALMA
 *     data cubes.
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
package usf.saav.alma.app;

import java.util.Arrays;
import java.util.prefs.Preferences;

public class RecentFileList {
	
	String [] filelist = new String[10];
	Preferences prefs;
	
	public RecentFileList(){
		prefs = Preferences.userNodeForPackage(RecentFileList.class);

		for( int i = 0; i < filelist.length; i++){
			filelist[i] = prefs.get( "recent"+i, null );
		}
	}
	
	public void add( String filename ){
		
		String [] newFilelist = new String[filelist.length];
		Arrays.fill( newFilelist, null);
		newFilelist[0] = filename;
		for(int i = 0, j = 1; i < filelist.length && j < filelist.length; i++ ){
			if( filelist[i] != null && !filelist[i].equals(filename) ){
				newFilelist[j++] = filelist[i];
			}
		}
		filelist = newFilelist;
		
		for(int i = 0; i < filelist.length; i++ ){
			if( filelist[i] != null ){
				prefs.put("recent"+i, filelist[i]);
			}
		}
		
	}
	
	public String get( int idx ){
		if( idx < 0 || idx >=  filelist.length ) return null;
		return filelist[idx];
	}
}
