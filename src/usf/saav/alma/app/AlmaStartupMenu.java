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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import usf.saav.common.monitor.MonitoredObject;
import usf.saav.common.monitor.MonitoredTrigger;

public class AlmaStartupMenu  extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = -6905404187443383755L;

	private JMenu menuFile;
	private JMenuItem menuOpen;
	private JMenu menuRecent;
	private JMenuItem menuExit;
	
	public  MonitoredTrigger monFileOpen = new MonitoredTrigger("File Open");
	public  MonitoredObject<String> monFileLoad = new MonitoredObject<String>(){
		@Override protected Class<?> getClassType() { return String.class; }
	};
	
	private Vector<JMenuItem> recentItems = new Vector<JMenuItem>();

	
	public AlmaStartupMenu( RecentFileList recent ){

		menuOpen = new JMenuItem("Open", KeyEvent.VK_O );
		menuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuOpen.addActionListener( this );
		
		menuRecent = new JMenu( "Recent" );
		for(int i = 0; i < 10; i++){
			String next = recent.get(i);
			if( next != null ){
				JMenuItem menuNext = new JMenuItem(next, KeyEvent.VK_1+i );
				menuNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1+i, ActionEvent.CTRL_MASK));
				menuNext.addActionListener( this );
				recentItems.add(menuNext);
				menuRecent.add( menuNext );
			}
		}

		menuExit = new JMenuItem("Exit", KeyEvent.VK_Q );
		menuExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		menuExit.addActionListener( this );

		menuFile = new JMenu("File");
		menuFile.add(menuOpen);
		menuFile.add(menuRecent);
		menuFile.addSeparator();
		menuFile.add(menuExit);

		this.add(menuFile);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if( e.getSource() == menuExit ){
			System.exit(0);
		}
		if( e.getSource() == menuOpen ){
			monFileOpen.trigger();
		}
		for( int i = 0; i < recentItems.size(); i++){
			if( e.getSource() == recentItems.get(i) ){
				monFileLoad.set( recentItems.get(i).getText() );
			}
		}
		
	}
	
	
	public void updateRecent(RecentFileList recent) {
		for(int i = 0; i < 10; i++){
			String next = recent.get(i);
			if( next != null ){
				if( recentItems.size() > i ){
					recentItems.get(i).setText( next );
				}
				else {
					JMenuItem menuNext = new JMenuItem(next, KeyEvent.VK_1+i );
					menuNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1+i, ActionEvent.CTRL_MASK));
					menuNext.addActionListener( this );
					recentItems.add(menuNext);
					menuRecent.add( menuNext );
				}
			}
		}
	}

	
	
}
