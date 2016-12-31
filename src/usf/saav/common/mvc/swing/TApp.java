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
package usf.saav.common.mvc.swing;
import javax.swing.DesktopManager;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

public class TApp extends JFrame {

	private static final long serialVersionUID = -5213783446944186344L;
	
	private JDesktopPane desktop;

	public TApp( int x, int y, int width, int height ){
		
		this.setBounds(x, y, width, height);
		
		 desktop = new JDesktopPane();
		 desktop.setSize(width, height);
		 desktop.setDesktopManager( new NoDragDesktopManager( desktop.getDesktopManager() ) );

		 setContentPane(desktop);
		 
	}
	
	public void addFrame( JInternalFrame frame ){
		desktop.add(frame);
	    try {
	        frame.setSelected(true);
	    } catch (java.beans.PropertyVetoException e) {}
	}
	
	
	
	private class NoDragDesktopManager implements DesktopManager {
		
		DesktopManager base;
		public NoDragDesktopManager( DesktopManager base ){
			this.base = base;
		}
		
		public void beginDraggingFrame(JComponent f) {
	        if (!"fixed".equals(f.getClientProperty("dragMode")))
	            base.beginDraggingFrame(f);
	    }

	    public void dragFrame(JComponent f, int newX, int newY) {
	        if (!"fixed".equals(f.getClientProperty("dragMode")))
	        	base.dragFrame(f, newX, newY);
	    }

	    public void endDraggingFrame(JComponent f) {
	        if (!"fixed".equals(f.getClientProperty("dragMode")))
	        	base.endDraggingFrame(f);
	    }


		@Override public void openFrame(JInternalFrame f) { base.openFrame(f); }
		@Override public void closeFrame(JInternalFrame f) { base.closeFrame(f); }
		@Override public void maximizeFrame(JInternalFrame f) { base.maximizeFrame(f); }
		@Override public void minimizeFrame(JInternalFrame f) { base.minimizeFrame(f); }
		@Override public void iconifyFrame(JInternalFrame f) { base.iconifyFrame(f); }
		@Override public void deiconifyFrame(JInternalFrame f) { base.deiconifyFrame(f); }
		@Override public void activateFrame(JInternalFrame f) { base.activateFrame(f); }
		@Override public void deactivateFrame(JInternalFrame f) { base.deactivateFrame(f); }
		@Override public void beginResizingFrame(JComponent f, int direction) { base.beginResizingFrame(f, direction); }
		@Override public void resizeFrame(JComponent f, int newX, int newY, int newWidth, int newHeight) {
			base.resizeFrame(f, newX, newY, newWidth, newHeight);
		}
		@Override public void endResizingFrame(JComponent f) { base.endResizingFrame(f); }
		@Override public void setBoundsForFrame(JComponent f, int newX, int newY, int newWidth, int newHeight) {
			base.setBoundsForFrame(f, newX, newY, newWidth, newHeight);
		}
	}
	
	
}
