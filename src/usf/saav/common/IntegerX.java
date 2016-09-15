package usf.saav.common;

import java.util.Collection;

public class IntegerX {


	public static int [] IntegerCollectionToArray( Collection<Integer> col ){
		int j = 0;
		int [] ret = new int[col.size()];
		for( int v : col ){
			ret[j++] = v;
		}

		return ret;
	}
	
}
