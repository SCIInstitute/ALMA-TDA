/*
 *     saav-core - A (very boring) software development support library.
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
package usf.saav.common.types;

public class Integer3 {
		public static final Integer3 INVALID = new Integer3( Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE );

		public int x;
		public int y;
		public int z;

		public Integer3() {
			x = y = z = 0;
		}
		
		public Integer3(Integer3 p) {
			x = p.x;
			y = p.y;
			z = p.z;
		}

		public Integer3(int _x, int _y, int _z){
			x = _x;
			y = _y;
			z = _z;
		}

		public Integer3 clone(){
			return new Integer3( x,y,z );
		}

		public void Set(int _x, int _y, int _z){
			x = _x;
			y = _y;
			z = _z;
		}

		public Integer3 add(Integer3 op2){ return new Integer3(x+op2.x, y+op2.y, z+op2.z);    }
		public Integer3 add(int op2) {        return new Integer3(x+op2, y+op2, z+op2);    }
		public static Integer3 add(Integer3 op1, Integer3 op2) {       return new Integer3(op1.x+op2.x, op1.y+op2.y, op1.z+op2.z);    }
		public static Integer3 add(Integer3 op1, int op2) {        return new Integer3(op1.x+op2, op1.y+op2, op1.z+op2);    }

		public  Integer3 subtract(Integer3 op2) {            return new Integer3(x-op2.x, y-op2.y, z-op2.z);    }
		public  Integer3 subtract(int op2) {            return new Integer3(x-op2, y-op2, z-op2);    }
		public static  Integer3 subtract(Integer3 op1, Integer3 op2) {            return new Integer3(op1.x-op2.x, op1.y-op2.y, op1.z-op2.z);    }
		public static  Integer3 subtract(Integer3 op1, int op2) {            return new Integer3(op1.x-op2, op1.y-op2, op1.z-op2);    }

		public Integer3 multiply(int  op2) { return new Integer3(x*op2, y*op2, z*op2);    }
		public Integer3 multiply(Integer3 op2) { return new Integer3(x*op2.x, y*op2.y, z*op2.z);    }
		public static Integer3 multiply(Integer3 op1, int op2) {      return new Integer3(op1.x*op2, op1.y*op2, op1.z*op2);    }
		public static Integer3 multiply(Integer3 op1, Integer3 op2) {      return new Integer3(op1.x*op2.x, op1.y*op2.y, op1.z*op2.z);    }

		public Integer3 divide(int op2) {            return new Integer3(x/op2, y/op2, z/op2);    }
		public Integer3 divide(Integer3 op2) {      return new Integer3(x/op2.x, y/op2.y, z/op2.z);    }    
		public static Integer3 divide(Integer3 op1, int op2) {            return new Integer3(op1.x/op2, op1.y/op2, op1.z/op2);    }
		public static Integer3 divide(Integer3 op1, Integer3 op2) {      return new Integer3(op1.x/op2.x, op1.y/op2.y, op1.z/op2.z);    }

		public void Print() {
			if( x == Integer.MAX_VALUE){
				System.out.print("< INT_MAX, ");
			}
			else{
				System.out.printf("< %d, ",x);
			}

			if(y == Integer.MAX_VALUE){
				System.out.print("INT_MAX, ");
			}
			else{
				System.out.printf("%d, ",y);
			}

			if(z == Integer.MAX_VALUE){
				System.out.print("INT_MAX >\n");
			}
			else{
				System.out.printf("%d >\n",z);
			}
		}    


}
