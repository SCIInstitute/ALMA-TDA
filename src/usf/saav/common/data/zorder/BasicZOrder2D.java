package usf.saav.common.data.zorder;

public class BasicZOrder2D implements ZOrder2D {

	@Override
	public long sizeX( ){ return Long.MAX_VALUE; }
	
	@Override
	public long sizeY( ){ return Long.MAX_VALUE; }
	
	@Override
	public long size(){ return Long.MAX_VALUE; }
	
	@Override
	public boolean inRange( long x, long y ){ return true; }
	
	@Override
	public long getOrdered2( long x, long y ){ return ZOrder.getOrdered2(x, y); }
	
	@Override
	public long [] getPosition2( long elem ){ return ZOrder.getPosition2(elem); }
	
}
