package usf.saav.alma.app.views;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import usf.saav.alma.data.fits.FitsHistory;

public class HistoryView extends JInternalFrame {
	private static final long serialVersionUID = 6188756035699890222L;


	public HistoryView(FitsHistory history) {
		super("History",
	          true, //resizable
	          true,  //closable
	          false, //maximizable
	          true); //iconifiable

		//setLocation( x, y );
		//setSize( 150, 800 );

		


		Object [] data = history.toArray( new String[history.size()] );
		
		for(String h : history){
			System.out.println(h);
		}
		
		JList list = new JList(data); //data has type Object[]
		//list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		//list.setVisibleRowCount(-1);

		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(list);
		mainPanel.add(listScroller);

		this.add( mainPanel );
		this.setPreferredSize( new Dimension(125,700) );
		this.setSize( new Dimension(150,550) );
		
	}

}
