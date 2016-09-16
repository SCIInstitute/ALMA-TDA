package usf.saav.common.types;

import java.util.Comparator;

public class Triple<A, B, C> implements Comparator< Triple<A, B, C> > {

    private A first;
    private B second;
    private C third;

    public Triple(A first, B second, C third) {
    	super();
    	this.first = first;
    	this.second = second;
    	this.third = third;
    }

    public int hashCode() {
    	int hashFirst  = first  != null ? first.hashCode()  : 0;
    	int hashSecond = second != null ? second.hashCode() : 0;
    	int hashThird  = third  != null ? third.hashCode()  : 0;

    	return (hashFirst + hashSecond) * hashThird + (hashThird + hashFirst) * hashSecond + hashFirst;
    }

    @SuppressWarnings("unchecked")
	public boolean equals(Object other) {
    	if (other instanceof Triple) {
    		Triple<A, B, C> otherTriple = (Triple<A, B, C>) other;
    		return 
    		((  this.first == otherTriple.first ||
    			( this.first != null && otherTriple.first != null &&
    			  this.first.equals(otherTriple.first))) &&
    		 (	this.second == otherTriple.second ||
    			( this.second != null && otherTriple.second != null &&
    			  this.second.equals(otherTriple.second))) &&
    		 (	this.third == otherTriple.third ||
    			( this.third != null && otherTriple.third != null &&
    			  this.third.equals(otherTriple.third))) );
    	}

    	return false;
    }

    public String toString()
    { 
           return "(" + first + ", " + second + ", " + third + ")"; 
    }
    
    public A getFirst() {
    	return first;
    }

    public void setFirst(A first) {
    	this.first = first;
    }

    public B getSecond() {
    	return second;
    }

    public void setSecond(B second) {
    	this.second = second;
    }

    public C getThird() {
    	return third;
    }

    public void setThird(C third) {
    	this.third = third;
    }
    
	@SuppressWarnings("unchecked")
	@Override
	public int compare(Triple<A, B, C> arg0, Triple<A, B, C> arg1) {
		
		if( arg0.first instanceof Comparator<?> && arg1.first instanceof Comparator<?> ){
			int cmp0 = ((Comparator<A>)arg0.first).compare( arg0.first, arg1.first);
			if(cmp0 != 0) return cmp0;
		}
		
		if( arg0.second instanceof Comparator<?> && arg1.second instanceof Comparator<?> ){
			int cmp1 = ((Comparator<B>)arg0.second).compare( arg0.second, arg1.second);
			if(cmp1 != 0) return cmp1;
		}

		if( arg0.third instanceof Comparator<?> && arg1.third instanceof Comparator<?> ){
			int cmp2 = ((Comparator<C>)arg0.third).compare( arg0.third, arg1.third);
			if(cmp2 != 0) return cmp2;
		}

		return 0;
	}

	public void set(Triple<A,B,C> arg) {
		first  = arg.first;
		second = arg.second;
		third  = arg.third;
	}
}

