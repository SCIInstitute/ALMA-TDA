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
package usf.saav.common.data.zorder;

import usf.saav.common.MathX;
import usf.saav.common.algorithm.ZOrder;

public class Partition2D {
	
	//private static int MIN_PARTITION_SIZE = 64;
	
	long dim_size;
	//Partition2D z0, z1, z2;
	long x_rep, y_rep;
	
	public Partition2D( long sizeX, long sizeY ){
		//dim_size = MathX.min( sizeX, sizeY );
		//System.out.println( sizeX + ", " + sizeY );
		//long main_tile = MathX.nextSmallerPowerOf2( dim_size );
		//long x_rem = MathX.nextLargerPowerOf2( sizeX - main_tile );
		//long y_rem = MathX.nextLargerPowerOf2( sizeY - main_tile );
		dim_size = Math.min( 1024,  MathX.nextLargerPowerOf2( Math.max(sizeX,sizeY) ) );
		x_rep = (sizeX+dim_size-1)/dim_size;
		y_rep = (sizeY+dim_size-1)/dim_size;
		//System.out.println( dim_size );
		//System.out.println( dim_size + " x " + x_rep );
		//System.out.println( dim_size + " x " + y_rep );
		
		//System.out.println( MathX.nextLargerPowerOf2( dim_size ) );
		
		//dim_size = MathX.nextSmallerPowerOf2( dim_size );
		//dim_size = MathX.nextLargerPowerOf2( dim_size );
		//dim_size = MathX.max( dim_size, MIN_PARTITION_SIZE );
		//System.out.println(dim_size);
		
		/*
		if( sizeX > dim_size ){
			z0 = new Partition2D( sizeX-dim_size, dim_size );
		}
		if( sizeY > dim_size ){
			z1 = new Partition2D( dim_size, sizeY-dim_size );
		}
		if( sizeX > dim_size && sizeY > dim_size ){
			z2 = new Partition2D( sizeX-dim_size, sizeY-dim_size );
		}
		*/
		
		
	}

	public long size() { 
		long tsize = (dim_size*dim_size)*(x_rep*y_rep);
		//if( z0 != null ) tsize += z0.size();
		//if( z1 != null ) tsize += z1.size();
		//if( z2 != null ) tsize += z2.size();
		return tsize;
	}
	
	public long getOrdered2( long x, long y ){
		
		int tileX = (int) (x/dim_size);
		int tileY = (int) (y/dim_size);
		
		int tileOffX = (int) (x%dim_size);
		int tileOffY = (int) (y%dim_size);
		
		long tile_size = dim_size*dim_size;
		long offset = tile_size*(tileY*x_rep+tileX);
		
		return offset + ZOrder.getOrdered2(tileOffX,tileOffY);
		
		/*
		if( x < dim_size && y < dim_size ) return off + ZOrder.getOrdered2( x,y );
		off+=dim_size*dim_size;
		if( z0 != null ){
			if( x>=dim_size && y<dim_size ) return off + z0.getOrdered2( x-dim_size, y );
			off += z0.size();
		}
		if( z1 != null ){
			if( x<dim_size && y>=dim_size ) return off + z1.getOrdered2( x,  y-dim_size );
			off += z1.size();
		}
		if( z2 != null ) return off + z2.getOrdered2( x-dim_size, y-dim_size );
		return -1;
		*/
	}

	public long [] getPosition2( long elem ){
		
		long tile_size = dim_size*dim_size;
		
		long tile_off = elem%tile_size;
		long tile_id  = elem/tile_size;
		
		long tileX = tile_id%x_rep;
		long tileY = tile_id/x_rep;

		long[] ret = ZOrder.getPosition2(tile_off);

		ret[0] += tileX*tile_size;
		ret[1] += tileY*tile_size;
		
		return ret;
		
		/*
		if( elem < dim_size*dim_size ) return ZOrder.getPosition2(elem);
		elem -= dim_size*dim_size;
		if( z0 != null && elem < z0.size() ) return z0.getPosition2( elem );
		elem -= z0.size();
		if( z1 != null && elem < z1.size() ) return z1.getPosition2( elem );
		elem -= z1.size();
		if( z2 != null && elem < z2.size() ) return z2.getPosition2( elem );
		//if( zy != null ) return zy.getPosition2( elem - size*size );
		return null;
		*/			
	}

}