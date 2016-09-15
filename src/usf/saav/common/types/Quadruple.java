package usf.saav.common.types;

import java.util.Comparator;

public class Quadruple<A, B, C, D> implements Comparator< Quadruple<A, B, C, D> > {

    private A first;
    private B second;
    private C third;
    private D fourth;

    public Quadruple(A first, B second, C third, D fourth) {
    	super();
    	this.first = first;
    	this.second = second;
    	this.third = third;
    	this.fourth = fourth;
    }

    public int hashCode() {
    	int hashFirst  = first  != null ? first.hashCode()  : 0;
    	int hashSecond = second != null ? second.hashCode() : 0;
    	int hashThird  = third  != null ? third.hashCode()  : 0;
    	int hashFourth = fourth != null ? fourth.hashCode() : 0;

    	return (hashFirst + hashSecond) * hashThird + (hashThird + hashFourth) * hashFirst + hashSecond + hashFourth;
    }

    @SuppressWarnings("unchecked")
	public boolean equals(Object other) {
    	if (other instanceof Pair) {
    		Quadruple<A, B, C, D> otherPair = (Quadruple<A, B, C, D>) other;
    		return 
    		((  this.first == otherPair.first ||
    			( this.first != null && otherPair.first != null &&
    			  this.first.equals(otherPair.first))) &&
    		 (	this.second == otherPair.second ||
    			( this.second != null && otherPair.second != null &&
    			  this.second.equals(otherPair.second))) &&
    		 (	this.third == otherPair.third ||
    			( this.third != null && otherPair.third != null &&
    			  this.third.equals(otherPair.third))) &&
    		 (	this.fourth == otherPair.fourth ||
    			( this.fourth != null && otherPair.fourth != null &&
    			  this.fourth.equals(otherPair.fourth))) );
    	}

    	return false;
    }

    public String toString()
    { 
           return "(" + first + ", " + second + ", " + third + ", " + fourth + ")"; 
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
    
    public D getFourth() {
    	return fourth;
    }

    public void setFourth(D fourth) {
    	this.fourth = fourth;
    }    

	@SuppressWarnings("unchecked")
	@Override
	public int compare(Quadruple<A, B, C, D> arg0, Quadruple<A, B, C, D> arg1) {
		
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

		if( arg0.fourth instanceof Comparator<?> && arg1.fourth instanceof Comparator<?> ){
			int cmp3 = ((Comparator<D>)arg0.fourth).compare( arg0.fourth, arg1.fourth);
			if(cmp3 != 0) return cmp3;
		}
		return 0;
	}
}
