package usf.saav.alma.app.interactive;

import java.io.IOException;

import nom.tam.fits.common.FitsException;
import usf.saav.alma.app.CmdlineParser;
import usf.saav.alma.app.TDAExec;
import usf.saav.alma.app.TDAInteractive;
import usf.saav.alma.app.TDAInteractive.MouseMode;
import usf.saav.alma.app.TDAInteractive.TreeDimension;
import usf.saav.alma.data.fits.RawFitsReader;
import usf.saav.alma.data.fits.SafeFitsReader;
import usf.saav.alma.util.CoordinateSystemController;
import usf.saav.common.BasicObject;
import usf.saav.common.monitor.MonitoredBoolean;
import usf.saav.common.monitor.MonitoredDouble;
import usf.saav.common.monitor.MonitoredEnum;
import usf.saav.common.monitor.MonitoredInteger;
import usf.saav.common.monitor.MonitoredTrigger;
import usf.saav.common.range.IntRange1D;

public class AlmaModel extends BasicObject {

    public MonitoredInteger				monX		  = new MonitoredInteger(0);
    public MonitoredInteger				monY		  = new MonitoredInteger(0);
    public MonitoredInteger				monZ		  = new MonitoredInteger(0);
    public MonitoredDouble				monZoom		  = new MonitoredDouble(1.0);
    public MonitoredInteger				monZ0		  = new MonitoredInteger(0);
    public MonitoredInteger				monZ1		  = new MonitoredInteger(0);
    public MonitoredEnum<MouseMode>		monMM	  	  = new MonitoredEnum<MouseMode>( MouseMode.NAVIGATE );
    public MonitoredEnum<TreeDimension> monDim		  = new MonitoredEnum<TreeDimension>( TreeDimension.DIM_2D );
    public MonitoredBoolean				monShowTree	  = new MonitoredBoolean( true );
    public MonitoredBoolean				monShowSimp	  = new MonitoredBoolean( true );
    public MonitoredBoolean				monShowM0	  = new MonitoredBoolean( false );
    public MonitoredTrigger				monButton	  = new MonitoredTrigger( );
    
	SafeFitsReader fits;
	

	public CoordinateSystemController csCont;
	
	public TDAExec config;
	public TDAInteractive window;

	public AlmaModel( TDAInteractive _window, TDAExec _config ) throws IOException, FitsException {
		super(true);
		config = _config;
		window = _window;
		
		fits = new SafeFitsReader( new RawFitsReader(config.filename, true), true );
		
		monX.set(fits.getAxesSize()[0].middle());
		monY.set(fits.getAxesSize()[1].middle());
		monZ.set(0);
		monZ0.set(0);
		monZ1.set(fits.getAxesSize()[2].end());
		
		csCont  = new CoordinateSystemController( monX, monY, monZoom );
		
		monButton.addMonitor( this, "finishUp" );

	}
	
	public void updateConfigSimplification( float val ){
		this.print_info_message( "New simplification: " + val );
		config.simplification = val;
	}
	
	public void updateConfigRanges( IntRange1D x, IntRange1D y, IntRange1D z ){
		config.treedim = monDim.get();
		config.xr = x;
		config.yr = y;
		config.zr = z;
	}
	
    public void finishUp(){
        window.setVisible(false);
    	config.loadFile( );
    	config.saveOutput( );
		CmdlineParser.printReproduce( config );
		System.exit(0);
    }

}
