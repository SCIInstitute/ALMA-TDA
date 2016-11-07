package usf.saav.alma.data.fits;

import java.util.Vector;

public class FitsHistory extends Vector<String> {

	private static final long serialVersionUID = 3224620726539901800L;

	
	public String [] getHistoryArray( ){
		
		int lines = 0;
		for(String h : this ){
			if( !h.startsWith(">") ) lines++;
		}
		
		String [] ret = new String[lines];
		int curline = 0;
		for( String h: this ){
			if( !h.startsWith(">") ){
				ret[curline++] = h;
			}
			else{
				ret[curline-1] += h.substring(1);
			}
		}
		
		return ret;		
	}
}
