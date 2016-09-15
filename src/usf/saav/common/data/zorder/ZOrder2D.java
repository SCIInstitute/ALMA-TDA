package usf.saav.common.data.zorder;

public interface ZOrder2D {
	
	public long sizeX( );
	public long sizeY( );
	
	public long size();
	
	public boolean inRange( long x, long y );
	
	public long getOrdered2( long x, long y );
	
	public long [] getPosition2( long elem );
	
}
