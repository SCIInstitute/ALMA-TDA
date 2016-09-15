package usf.saav.common;

public class DistanceX {

	
	// int EditDistance(char s[1..m], char t[1..n])
	public static int EditDistance( String s, String t ){
		
		// For all i and j, d[i,j] will hold the Levenshtein distance between
		// the first i characters of s and the first j characters of t.
		// Note that d has (m+1)  x(n+1) values.
		
		//let d be a 2-d array of int with dimensions [0..m, 0..n]
		int [][] d = new int[s.length()][t.length()];

		for( int i = 0; i < s.length(); i++){
			d[i][0] = i; // the distance of any first string to an empty second string
		}
		
		for( int j = 0; j < t.length(); j++ ){
			d[0][j] = j; // the distance of any second string to an empty first string
		}
		 
		for( int j = 1; j < t.length(); j++ ){
			for( int i = 1; i < s.length(); i++){
				if( s.charAt(i) == t.charAt(j) ){
					d[i][j] = d[i-1][j-1];
				}
		       else{
		    	   d[i][j] = d[i-1][j-1] + 1; // a substitution
		    	   d[i][j] = Math.min( d[i][j],	d[i-1][j] + 1 );   // a deletion
		    	   d[i][j] = Math.min( d[i][j],	d[i][j-1] + 1 );  // an insertion
		       }
			}
		}
		  
		return d[s.length()-1][t.length()-1];		
	}
	
	
}
