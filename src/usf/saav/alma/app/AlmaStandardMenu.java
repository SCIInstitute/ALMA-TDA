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

public class AlmaStandardMenu extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = -6905404187443383755L;

	private JMenu menuFile;
	private JMenuItem menuOpen;
	private JMenuItem menuAppend;
	private JMenuItem menuRefreshCache;
	private JMenuItem menuClose;
	private JMenuItem menuExit;
	private JMenuItem menuExport;

	private JMenu menuProperties;

	public  MonitoredTrigger monFileOpen		 = new MonitoredTrigger("File Open");
	public  MonitoredTrigger monFileAppend		 = new MonitoredTrigger("Append File");
	public  MonitoredTrigger monFileRefreshCache = new MonitoredTrigger("Refresh Cache");
	public  MonitoredTrigger monFileClose		 = new MonitoredTrigger("File Close");
	public  MonitoredTrigger monFileExport = new MonitoredTrigger("File Export");

	public  MonitoredTrigger monWindowProp = new MonitoredTrigger("Properties");
	public  MonitoredTrigger monWindowHist = new MonitoredTrigger("History");
	public  MonitoredTrigger monWindowVol  = new MonitoredTrigger("Volume");

	private JMenuItem menuProps;
	private JMenuItem menuHist;
	private JMenuItem menuVol;

	private JMenu menuRecent;
	
	public  MonitoredObject<String> monFileLoad = new MonitoredObject<String>(){
		@Override protected Class<?> getClassType() { return String.class; }
	};
	
	private Vector<JMenuItem> recentItems = new Vector<JMenuItem>();

	
	
	public AlmaStandardMenu( RecentFileList recent ){

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


		menuExport = new JMenuItem("FITS File Export", KeyEvent.VK_E);
		menuExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		menuExport.addActionListener( this );

		menuAppend = new JMenuItem("Append Volume", KeyEvent.VK_A );
		menuAppend.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		menuAppend.addActionListener( this );

		menuRefreshCache = new JMenuItem( "Refresh Cache" );
		menuRefreshCache.addActionListener( this );

		menuClose = new JMenuItem("Close", KeyEvent.VK_C );
		menuClose.addActionListener( this );
		
		menuExit = new JMenuItem("Exit", KeyEvent.VK_Q );
		menuExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		menuExit.addActionListener( this );

		menuFile = new JMenu("File");
		menuFile.add(menuOpen);
		menuFile.add(menuRecent);
		menuFile.addSeparator();
		menuFile.add(menuAppend);
		menuFile.add(menuRefreshCache);
		menuFile.addSeparator();
		menuFile.add(menuExport);
		menuFile.add(menuClose);
		menuFile.addSeparator();
		menuFile.add(menuExit);

		this.add(menuFile);

		
		menuProps = new JMenuItem("Show Properties", KeyEvent.VK_P );
		menuProps.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		menuProps.addActionListener( this );

		menuHist = new JMenuItem("Show History", KeyEvent.VK_H );
		menuHist.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		menuHist.addActionListener( this );

		menuVol = new JMenuItem("Show Volume Rendering", KeyEvent.VK_V );
		menuVol.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		menuVol.addActionListener( this );

		menuProperties = new JMenu("Windows");
		menuProperties.add(menuProps);
		menuProperties.add(menuHist);
		menuProperties.add(menuVol);

		this.add(menuProperties);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if( e.getSource() == menuExit		 ){ System.exit(0);				   }
		if( e.getSource() == menuOpen		 ){ monFileOpen.trigger();		   }
		if( e.getSource() == menuAppend		 ){ monFileAppend.trigger();	   }
		if( e.getSource() == menuRefreshCache){ monFileRefreshCache.trigger(); }
		if( e.getSource() == menuClose		 ){ monFileClose.trigger();		   }
		if( e.getSource() == menuProps		 ){ monWindowProp.trigger();	   }
		if( e.getSource() == menuHist		 ){ monWindowHist.trigger();	   }
		if( e.getSource() == menuVol		 ){ monWindowVol.trigger();		   }
		if( e.getSource() == menuExport      ){ monFileExport.trigger();       }
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
