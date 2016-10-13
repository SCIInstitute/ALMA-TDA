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
package usf.saav.alma.data;

import usf.saav.alma.data.ScalarField2D.Default;

// TODO: Auto-generated Javadoc
/**
 * The Class Synthetic2D.
 */
public abstract class Synthetic2D extends Default {

	int width, height;
	
	/**
	 * Instantiates a new synthetic 2 D.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public Synthetic2D( int width, int height ){
		this.width = width;
		this.height = height;
	}
	
	/* (non-Javadoc)
	 * @see usf.saav.common.algorithm.Surface2D#getWidth()
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.algorithm.Surface2D#getHeight()
	 */
	@Override
	public int getHeight() {
		return height;
	}
	
	/**
	 * The Class MonkeySaddle.
	 */
	public static class MonkeySaddle extends Synthetic2D {

		/**
		 * Instantiates a new monkey saddle.
		 *
		 * @param width the width
		 * @param height the height
		 */
		public MonkeySaddle(int width, int height) {
			super(width, height);
		}

		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarField2D#getValue(int, int)
		 */
		@Override
		public float getValue(int x, int y) {
			double dx = ((double)x - width/2)/100;
			double dy = ((double)y - height/2)/100;
			return (float) ((dx*dx*dx) - 3 * dx * (dy*dy)) + (float)(dx/100 + dy/100);
		}
	}
	
	
	/**
	 * The Class GaussianMixing.
	 */
	public static class GaussianMixing extends Synthetic2D {
		
		int [] px;
		int [] py;
		double [] A;
	
		/**
		 * Instantiates a new gaussian mixing.
		 *
		 * @param width the width
		 * @param height the height
		 */
		public GaussianMixing(int width, int height) {
			super(width, height);
			px = new int[]{width/3+10, width*2/3-10, width/3-10, width*2/3+10};
			py = new int[]{height/3-10, height*2/3+10, height*2/3+10, height/3-10};
			A  = new double[]{ 70, 50, -40, -20 };
		}

		/* (non-Javadoc)
		 * @see usf.saav.alma.data.ScalarField2D#getValue(int, int)
		 */
		@Override
		public float getValue(int x, int y) {
			double val = 0;
			double sx = 60, sy = 60;
			for(int i = 0; i < px.length; i++){
				double dx = x-px[i];
				double dy = y-py[i];
				val += A[i]*Math.exp(- (dx*dx)/(2*sx*sx) - (dy*dy)/(2*sy*sy) );
			}
			return (float) val;
		}
	}
}

