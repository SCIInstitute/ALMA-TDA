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
package usf.saav.alma.drawing.VolumeRendering;

import usf.saav.common.monitor.MonitoredTrigger;
import usf.saav.common.types.Float4;

public interface TransferFunction1D  {

	public int size();
	public Float4 get( int idx );
	public float getOffset( );
	public float getScale( );
	
	public void addModifiedCallback( Object obj, String func );

	public class Default implements TransferFunction1D {

		private static final Float4 transferFunc[] = {
									new Float4 ( 0.0f, 0.0f, 0.0f, 0.0f ),
									new Float4 ( 1.0f, 0.0f, 0.0f, 1.0f ),
									new Float4 ( 1.0f, 0.5f, 0.0f, 1.0f ),
									new Float4 ( 1.0f, 1.0f, 0.0f, 1.0f ),
									new Float4 ( 0.0f, 1.0f, 0.0f, 1.0f ),
									new Float4 ( 0.0f, 1.0f, 1.0f, 1.0f ),
									new Float4 ( 0.0f, 0.0f, 1.0f, 1.0f ),
									new Float4 ( 1.0f, 0.0f, 1.0f, 1.0f ),
									new Float4 ( 0.0f, 0.0f, 0.0f, 0.0f )
							};

		@Override
		public int size() {
			return transferFunc.length;
		}

		@Override
		public Float4 get(int idx) {
			return transferFunc[idx];
		}

		@Override
		public float getOffset() {
			return 0;
		}

		@Override
		public float getScale() {
			return 1;
		}
		
		MonitoredTrigger modifiedCB = new MonitoredTrigger( );

		public void addModifiedCallback( Object obj, String func ){
			modifiedCB.addMonitor( obj,  func );
		}

	}
}
