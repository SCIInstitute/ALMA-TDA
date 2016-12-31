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
package usf.saav.alma.app.views;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import usf.saav.alma.data.fits.FitsProperties;
import usf.saav.alma.data.fits.FitsProperty;
import usf.saav.alma.data.fits.FitsReader;

public class PropertiesView extends JInternalFrame {
	private static final long serialVersionUID = 6188756035699890222L;

	private JTabbedPane tabbedPane = new JTabbedPane();

	public PropertiesView( Vector<FitsReader> readers ) {
		
		super("Properties",
	          true,  //resizable
	          false, //closable
	          false, //maximizable
	          true); //iconifiable

		for( FitsReader r: readers ){
			addReader( r );
		}
		this.add( tabbedPane );
		this.setPreferredSize( new Dimension(500,700) );
		this.setSize( new Dimension(400,550) );
		
		
	}
	
	public PropertiesView(FitsReader reader) {
		
		super("Properties",
	          true,  //resizable
	          false, //closable
	          false, //maximizable
	          true); //iconifiable

		this.addReader( reader );
		this.add( tabbedPane );
		this.setPreferredSize( new Dimension(500,700) );
		this.setSize( new Dimension(400,550) );
		
	}
	
	
	public void addReader( FitsReader reader ) {

		FitsProperties props = reader.getProperties();
		
		String [] data = new String[props.size()];
		int curr = 0;
		for( FitsProperty p : props ){
			data[curr++] = p.toString();
		}
		
		JList<String> list = new JList<String>(data);
		list.setLayoutOrientation(JList.VERTICAL);
		
		JScrollPane listScroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(listScroller);
        
		tabbedPane.addTab( reader.getFile().getName(), panel );
		
	}
	
}
