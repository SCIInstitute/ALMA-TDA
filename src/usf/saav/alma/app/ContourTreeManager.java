/*
 *     ALMA TDA - Contour tree based simplification and visualization for ALMA
 *     data cubes.
 *     Copyright (C) 2016 PAUL ROSEN
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     You may contact the Paul Rosen at <prosen@usf.edu>.
 */
package usf.saav.alma.app;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import usf.saav.alma.app.views.AlmaGui;
import usf.saav.alma.app.views.AlmaGui.TreeDimension;
import usf.saav.alma.util.ContourTreeThread;
import usf.saav.common.monitor.MonitoredObject;
import usf.saav.common.range.IntRange1D;
import usf.saav.topology.TopoTree;

public class ContourTreeManager {

	DataViewManager dvm;
	AlmaGui gui;
	public Map< Integer, ContourTreeThread >       ctt_map = new HashMap< Integer, ContourTreeThread >( );
	ExecutorService threadPool = Executors.newFixedThreadPool(4);
	
	public MonitoredContourTreeThread cur_ctt = new MonitoredContourTreeThread( );
	public MonitoredObject< Set<TopoTree> > pss = new MonitoredObject< Set<TopoTree> >( ){
		@Override protected Class<?> getClassType() { return Set.class; } 
	};


	public ContourTreeManager( DataViewManager dvm, AlmaGui gui ){
		this.dvm = dvm;
		this.gui = gui;
		gui.monBuildTree.addMonitor( this, "buildContourTree" );
	}
	

	public void buildContourTree( ){
		
		IntRange1D [] r = dvm.sel_box.getSelection();
		if( r == null ) return;
		
		dvm.sel_box.clearSelection();
		
		if( gui.monDim.get() == TreeDimension.DIM_2D ) {
			ContourTreeThread ctt = new ContourTreeThread( dvm.dsm.data, r[0], r[1], dvm.curZ.get() );
			ctt.setCallback( this, "completedContourTree");
			ctt_map.put( dvm.curZ.get(), ctt );
			threadPool.submit( ctt );
		}
		if( gui.monDim.get() == TreeDimension.DIM_2D_STACK ) {
			for( int z = dvm.z0.get(); z <= dvm.z1.get(); z++ ){
				ContourTreeThread ctt = new ContourTreeThread( dvm.dsm.data, r[0], r[1], z );
				ctt.setCallback( this, "completedContourTree");
				ctt_map.put( z, ctt );
				threadPool.submit( ctt );
			}
		}
		if( gui.monDim.get() == TreeDimension.DIM_3D ) {
			ContourTreeThread ctt = new ContourTreeThread( dvm.dsm.data, r[0], r[1], new IntRange1D( dvm.z0.get(), dvm.z1.get() ) );
			ctt.setCallback( this, "completedContourTree");
			for( int z = dvm.z0.get(); z <= dvm.z1.get(); z++ ){
				ctt_map.put( z, ctt );
			}
			threadPool.submit( ctt );
		}

	}
	
	
	public void completedContourTree( ContourTreeThread ctt ){
		boolean stillProcessing = false;
		for( Entry<Integer,ContourTreeThread> e_ctt : ctt_map.entrySet() ){
			stillProcessing = stillProcessing || !e_ctt.getValue().isProcessingComplete();
		}
		
		if( !stillProcessing ){
			cur_ctt.set( ctt_map.containsKey( dvm.curZ.get() ) ? ctt_map.get( dvm.curZ.get() ) : null );
			Set<TopoTree> _pss = new HashSet<TopoTree>( );

			// Only simplify the current slice, if slice is outside of range of interest
			if( dvm.curZ.get() < dvm.z0.get() || dvm.curZ.get() > dvm.z1.get() ){
				int z = dvm.curZ.get();
				if( ctt_map.containsKey(z) && ctt_map.get(z).getTree() != null ) 
					_pss.add( ctt_map.get(z).getTree() );
			}
			else {
				for( int z = dvm.z0.get(); z <= dvm.z1.get(); z++ ){
					if( ctt_map.containsKey(z) && ctt_map.get(z).getTree() != null ) 
						_pss.add( ctt_map.get(z).getTree() );
				}
			}
			pss.set( _pss );
		}
	}
	
	

	public class MonitoredContourTreeThread extends MonitoredObject< ContourTreeThread >{
		@Override protected Class<?> getClassType() { return ContourTreeThread.class; } 
	};

	
}
