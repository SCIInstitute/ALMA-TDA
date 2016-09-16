package usf.saav.common.types;

import java.util.Comparator;


public class PairComparator<A, B> implements Comparator< Pair<A, B> > {

	@SuppressWarnings("unchecked")
	@Override
	public int compare(Pair<A, B> arg0, Pair<A, B> arg1) {

		if( arg0.getFirst() instanceof Comparator<?> && arg1.getFirst() instanceof Comparator<?> ){
			int cmp0 = ((Comparator<A>)arg0.getFirst()).compare( arg0.getFirst(), arg1.getFirst());
			if(cmp0 != 0) return cmp0;
		}
		
		if( arg0.getSecond() instanceof Comparator<?> && arg1.getSecond() instanceof Comparator<?> ){
			int cmp1 = ((Comparator<B>)arg0.getSecond()).compare( arg0.getSecond(), arg1.getSecond());
			if(cmp1 != 0) return cmp1;
		}
		return 0;
	}
	
}
