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
package usf.saav.alma.algorithm.topology;

import usf.saav.alma.algorithm.mesh.Mesh;

public class PseudoContourTree implements PersistenceSet {

	private MergeTree mt;
	private SplitTree st;
	private AugmentedJoinTreeNode global_min;
	private AugmentedJoinTreeNode global_max;
	private float persistence_max = 0;
	float simplify = 0.0f;
	

	public PseudoContourTree( Mesh sf ){
		System.out.println("[ContourTree] Building Contour Tree");
		
		
		this.mt = new MergeTree(sf);
		this.st = new SplitTree(sf);
		
		Thread t1 = new Thread( mt );
		Thread t2 = new Thread( st );
		
		t1.run();
		t2.run();
		
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		persistence_max = Math.max( mt.getMaxPersistence(), st.getMaxPersistence() );
		global_min = mt.getGlobalExtreme();
		global_max = st.getGlobalExtreme();
		
		if( global_min != null && global_max != null ){
			global_min.partner = global_max;
			global_max.partner = global_min;
		}
		else{
			System.out.println("[ContourTree] Error finding global min and/or max");
		}
	}

	
	public void setPersistentSimplification( float threshold ){ simplify = threshold; }
	public float getPersistentSimplification( ){ return simplify; }
	
	public float getMaxPersistence(){ return persistence_max; }
	public AugmentedJoinTreeNode getGlobalMin(){ return global_min; }
	public AugmentedJoinTreeNode getGlobalMax(){ return global_max; }
	
	@Override
	public int size() {
		return mt.size() + st.size();
	}

	@Override
	public float getBirth(int i) {
		if( i < mt.size() )
			return mt.getBirth(i);
		return st.getBirth(i-mt.size());
	}

	@Override
	public float getDeath(int i) {
		if( i < mt.size() )
			return mt.getDeath(i);
		return st.getDeath(i-mt.size());
	}

	@Override
	public float getPersistence(int i) {
		if( i < mt.size() )
			return mt.getPersistence(i);
		return st.getPersistence(i-mt.size());
	}

	public AugmentedJoinTreeNode getNode(int i) {
		if( i < mt.size() )
			return mt.getNode(i);
		return st.getNode(i-mt.size());
	}
	
	public boolean isActive(int i){
		return getPersistence(i) >= simplify;
	}

}
