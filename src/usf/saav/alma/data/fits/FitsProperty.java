package usf.saav.alma.data.fits;



public class FitsProperty {
	String label;
	String value;
	String comment;
	public FitsProperty( String _label, String _value, String _comment ){
		label = _label;
		value = _value;
		comment = _comment;
	}
	@Override
	public String toString( ){
		String ret = label;
		if( value != null ) ret += ": " + value; 
		if( comment != null ) ret += "\t// " + comment;
		return ret;
	}
}

