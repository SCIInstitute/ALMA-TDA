package usf.saav.common;

public class BasicObject {

	public static boolean verbose = false;
	
	private boolean use_glob  = false;
	private boolean p_verbose = false;
	
	/**
	 * Default constructor uses global verbose flag
	 */
	protected BasicObject( ){
		this.use_glob = true;
	}
	
	/**
	 * Constructor that enables overriding the global verbose flag
	 * @param verbose Yes or no to verbose output
	 */
	protected BasicObject( boolean verbose ){
		this.p_verbose = verbose;
	}
	
	/**
	 * Prints an informational message into the console
	 * @param message Message to be print only is verbose is true
	 */
	protected void print_info_message( String message ){
		if( (use_glob && verbose) || (!use_glob && p_verbose) ) System.out.println( "[" + getClass().getSimpleName() + "] " + message );
	}
	

	/**
	 * Prints an informational message into the console
	 * @param message Message to be print only is verbose is true
	 */
	protected void print_info_message(double message) {
		if( (use_glob && verbose) || (!use_glob && p_verbose) ) System.out.println( "[" + getClass().getSimpleName() + "] " + message );
	}

	/**
	 * Prints an informational message into the console
	 * @param message Message to be print only is verbose is true
	 */
	protected void print_info_message( int message ){
		if( (use_glob && verbose) || (!use_glob && p_verbose) ) System.out.println( "[" + getClass().getSimpleName() + "] " + message );
	}
	
	/**
	 * Prints a warning message into the console
	 * @param message Message to be print no matter verbose flag
	 */
	protected void print_warning_message( String message ){
		System.out.println( "[" + getClass().getSimpleName() + "] " + message );
	}
	
	/**
	 * Prints a warning message into the console
	 * @param message Message to be print no matter verbose flag
	 */
	protected void print_warning_message( int message ){
		System.out.println( "[" + getClass().getSimpleName() + "] " + message );
	}
	
	/**
	 * Prints an error message to stderr
	 * @param message Message to be print no matter verbose flag
	 */
	protected void print_error_message( String message ){
		System.err.println( "[" + getClass().getSimpleName() + "] " + message );
	}
	
	/**
	 * Prints an error message to stderr
	 * @param message Message to be print no matter verbose flag
	 */
	protected void print_error_message( int message ){
		System.err.println( "[" + getClass().getSimpleName() + "] " + message );
	}
	
}
