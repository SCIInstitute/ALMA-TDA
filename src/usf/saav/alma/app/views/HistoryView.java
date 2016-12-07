package usf.saav.alma.app.views;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import usf.saav.alma.data.fits.FitsReader;

public class HistoryView extends JInternalFrame {
	private static final long serialVersionUID = 6188756035699890222L;

	private JTabbedPane tabbedPane = new JTabbedPane();

	public HistoryView( Vector<FitsReader> readers ) {
		
		super("History",
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
	
	public HistoryView(FitsReader reader) {
		
		super("History",
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

		String [] data = reader.getHistory().getHistoryArray();
		
		JList<String> list = new JList<String>(data);
		list.setLayoutOrientation(JList.VERTICAL);
		
		JScrollPane listScroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(listScroller);
        
		tabbedPane.addTab( reader.getFile().getName(), panel );
		
	}
	
}

