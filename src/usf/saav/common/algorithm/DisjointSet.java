package usf.saav.common.algorithm;

public class DisjointSet {

    protected int [] sets;
    
    /*
    public DisjointSet(){
        clear(0);
    }
    */
    
    public DisjointSet(int elemN){
        clear(elemN);
    }

    public void clear( int elemN ){
    	sets = new int[elemN];
        for(int i = 0; i < elemN; i++){
            sets[i] = i;
        }
    }

    public void invalidateSet( int elem ){
        int set = get( elem );
        if( set >= 0 ){
            sets[set] = -1;
        }
    }

    public int get( int elem ){
        while(true){
            if( elem       == -1   ) return -1;
            if( sets[elem] == -1   ) return -1;
            if( sets[elem] == elem ) return elem;
            sets[elem] = sets[ sets[elem] ];
            elem = sets[elem];
        }
    }

    public void join( int set0, int set1 ){

        set0 = get( set0 );
        set1 = get( set1 );

        if( set0 == -1 || set1 == -1 ){
            if( set0 != -1 ) sets[set0] = -1;
            if( set1 != -1 ) sets[set1] = -1;
        }
        else{
            sets[set1] = set0;
        }
    }
    

}
