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

public class Float2 {
	
	public final static Float2 INVALID = new Float2(Float.MAX_VALUE, Float.MAX_VALUE);

    public float x;
    public float y;

    public Float2(){
    	x = y = 0;
    }

    public Float2(float _x, float _y){
    	x = _x;
    	y = _y;
    }

    public Float2(double _x, double _y){
    	x = (float)_x;
    	y = (float)_y;
    }

	public Float2(String sx, String sy) {
		x = Float.valueOf(sx);
		y = Float.valueOf(sy);
	}
	

	public void Set(float _x, float _y){ 
		x = _x; y = _y; 
	}
	
    public Float2 add(Float2 op2){ return new Float2(x+op2.x, y+op2.y);    }
    public Float2 add(float op2) {        return new Float2(x+op2, y+op2);    }
    public static Float2 add(Float2 op1, Float2 op2) {       return new Float2(op1.x+op2.x, op1.y+op2.y);    }
    public static Float2 add(Float2 op1, float op2) {        return new Float2(op1.x+op2, op1.y+op2);    }

    public static  Float2 subtract(Float2 op1, Float2 op2) {            return new Float2(op1.x-op2.x, op1.y-op2.y);    }
    public static  Float2 subtract(Float2 op1, float op2) {            return new Float2(op1.x-op2, op1.y-op2);    }
    
    public static Float2 multiply(Float2 op1, float op2) {      return new Float2(op1.x*op2, op1.y*op2);    }
    public static Float2 multiply(Float2 op1, Float2 op2) {      return new Float2(op1.x*op2.x, op1.y*op2.y);    }

    public Float2 divide(float op2) {            return new Float2(x/op2, y/op2);    }
    public Float2 divide(Float2 op2) {      return new Float2(x/op2.x, y/op2.y);    }    
    public static Float2 divide(Float2 op1, float op2) {            return new Float2(op1.x/op2, op1.y/op2);    }
    public static Float2 divide(Float2 op1, Float2 op2) {      return new Float2(op1.x/op2.x, op1.y/op2.y);    }
	
	public Float2 addEquals(Float2 op2) {
		x+=op2.x;
		y+=op2.y;
		return this;			
	}
    
    ///////////////////////////////////////////////////////////////
    /// \brief Dot Product
    ///
    /// \param op2 Other vector to apply dot product
    ///
    /// \return result of dot product (x0*x1 + y0*y1)
    ///////////////////////////////////////////////////////////////
    public static float dot(Float2 v1, Float2 v2){
            return v1.x*v2.x + v1.y*v2.y;
    }
	public static float cross(Float2 v1, Float2 v2) {
        return v1.x*v2.y - v1.y*v2.x;
	}
    
    
    ///////////////////////////////////////////////////////////////
    /// \brief Checks if the vector has valid values
    ///
    /// \return true if valid
    ///////////////////////////////////////////////////////////////
    public boolean isValid() {
            return (x!=Float.MAX_VALUE) && (x!=-Float.MAX_VALUE) && (x!=Float.NaN) && (y!=Float.MAX_VALUE) && (y!=-Float.MAX_VALUE) && (y!=Float.NaN);
    }
    

    ///////////////////////////////////////////////////////////////
    /// \brief Vector Output - Prints to Console
    ///////////////////////////////////////////////////////////////
    public void Print() {
        if(x == Float.MAX_VALUE){
        	System.out.print("< FLT_MAX, ");
        }
        else if(x == -Float.MAX_VALUE){
        	System.out.print("< -FLT_MAX, ");
        }
        else if(x == Float.NaN){
        	System.out.print("< NaN, ");
        }
        else{
        	System.out.printf("< %f, ",x);
        }

            if(y == Float.MAX_VALUE){
            	System.out.print("FLT_MAX >\n");
            }
            else if(y == -Float.MAX_VALUE){
            	System.out.print("-FLT_MAX >\n");
            }
            else if(y == Float.NaN){
            	System.out.print("NaN >\n");
            }
            else{
            	System.out.printf("%f >\n",y);
            }
    }    
    
	///////////////////////////////////////////////////////////////
	/// \brief Vector Magnitude
	///
	/// \return the magnitude of the vector
	///////////////////////////////////////////////////////////////
	public float Length()  {
		return (float) Math.sqrt(x*x+y*y);
	}    


    ///////////////////////////////////////////////////////////////
        /// \brief Vector Magnitude
        ///
        /// \return the magnitude of the vector
        ///////////////////////////////////////////////////////////////
        public float Magnitude() {
                return (float) Math.sqrt(x*x+y*y);
        }
        

        public static Float2 lerp( Float2 v0, Float2 v1, float t ){
   		    return add( multiply(v0,1.0f-t), multiply(v1,t) );
        }

		public static float distance(Float2 p, Float2 mp) {
			return Float2.subtract(p, mp).Length();
		}


        
    /*
        ///////////////////////////////////////////////////////////////
        /// \brief Scalar Multiply
        ///
        /// \param scalar scale to multiply to vector
        ///
        /// \return new vector with the resulting scale
        ///////////////////////////////////////////////////////////////
        inline Float2 operator*(const float scalar) const  { return Float2(x*scalar, y*scalar); }

        ///////////////////////////////////////////////////////////////
        /// \brief Component-wise Multiply
        ///
        /// \param scalar scale to multiply each component
        ///
        /// \return new vector with the resulting scaling
        ///////////////////////////////////////////////////////////////
        inline Float2 operator*(const Float2 & scalar) const  { return Float2(x*scalar.x, y*scalar.y); }

        ///////////////////////////////////////////////////////////////
        /// \brief Scalar Divide
        ///
        /// \param scalar scale by which to divide the vector
        ///
        /// \return new vector with the resulting divide
        ///////////////////////////////////////////////////////////////
        inline Float2 operator/(const float scalar) const { return Float2(x/scalar, y/scalar); }

        ///////////////////////////////////////////////////////////////
        /// \brief Component-wise Divide
        ///
        /// \param scalar scale by which to divide each component
        ///
        /// \return new vector with the resulting divide
        ///////////////////////////////////////////////////////////////
        inline Float2 operator/(const Float2 & scalar) const { return Float2(x/scalar.x, y/scalar.y); }

        ///////////////////////////////////////////////////////////////
        /// \brief Vector Add
        ///
        /// \param op2 vector to add to current
        ///
        /// \return new vector with the resulting add
        ///////////////////////////////////////////////////////////////
        inline Float2 operator+(const Float2& op2) const { return Float2(x+op2.x, y+op2.y); }

        ///////////////////////////////////////////////////////////////
        /// \brief Vector Add
        ///
        /// \param op2 value to add to each component
        ///
        /// \return new vector with the resulting add
        ///////////////////////////////////////////////////////////////
        inline Float2 operator+(const float op2) const { return Float2(x+op2, y+op2); }

        ///////////////////////////////////////////////////////////////
        /// \brief Vector Subtract
        ///
        /// \param op2 vector to subtract from current
        ///
        /// \return new vector with the resulting subtract
        ///////////////////////////////////////////////////////////////
        inline Float2 operator-(const Float2 op2) const { return Float2(x-op2.x, y-op2.y); }

        ///////////////////////////////////////////////////////////////
        /// \brief Vector Subtract
        ///
        /// \param op2 value to subtract from each component
        ///
        /// \return new vector with the resulting subtract
        ///////////////////////////////////////////////////////////////
        inline Float2 operator-(const float op2) const { return Float2(x-op2, y-op2); }

        ///////////////////////////////////////////////////////////////
        /// \brief Vector Set
        ///
        /// \param op2 vector to set the current to
        ///
        /// \return current vector modified
        ///////////////////////////////////////////////////////////////
        inline Float2& operator=(const Float2 op2){
            x = op2.x;
            y = op2.y;
            return (*this);
        }

        ///////////////////////////////////////////////////////////////
        /// \brief Vector Add/Set
        ///
        /// \param op2 vector to add to current
        ///
        /// \return current vector modified with the resulting add
        ///////////////////////////////////////////////////////////////
        inline Float2& operator+=(const Float2 op2) {
            x += op2.x;
            y += op2.y;
            return (*this);
        }

        ///////////////////////////////////////////////////////////////
        /// \brief Vector Add/Set
        ///
        /// \param op2 value to add to each component
        ///
        /// \return current vector modified with the resulting add
        ///////////////////////////////////////////////////////////////
        inline Float2& operator+=(const float op2) {
            x += op2;
            y += op2;
            return (*this);
        }

        inline Float2& operator-=(const Float2& op2) {
            x -= op2.x;
            y -= op2.y;
            return (*this);
        }

        inline Float2& operator-=(const float op2) {
            x -= op2;
            y -= op2;
            return (*this);
        }

        inline Float2& operator*=(const float op2) {
            x *= op2;
            y *= op2;
            return (*this);
        }





            inline Float2 operator-(){
                    return Float2(-x,-y);
            }




        ///////////////////////////////////////////////////////////////
            /// \brief Unit Vector
            ///
            /// \return new unit vector of currect vector
            ///////////////////////////////////////////////////////////////
            inline Float2 UnitVector() const {
                    float length = Length();
                    if (length != 0.0f) {
                            return Float2(x,y) / length;
                    }
                    return Float2();
            }


        ///////////////////////////////////////////////////////////////
            /// \brief Normalize vector to magnitude of 1
            ///////////////////////////////////////////////////////////////
            inline void Normalize(){
                    float length = Length();
                    if (length != 0.0f) {
                            x /= length;
                            y /= length;
                    }
            }

        ///////////////////////////////////////////////////////////////
            /// \brief Vector Magnitude
            ///
            /// \return the magnitude of the vector
            ///////////////////////////////////////////////////////////////
            inline float Length() const  {
              return sqrtf(x*x+y*y);
            }

            inline float& operator[](const int i){
                    switch(i) {
                            case 0:    return x;
                            case 1: return y;
                            default:
                                    printf("ERROR (Float2::operator[]): index out of bounds\n");
                                    return x;
                    }
            }

            inline const float& operator[](const int i) const{
                    switch(i) {
                            case 0:    return x;
                            case 1: return y;
                            default:
                                    printf("ERROR (Float2::operator[]): index out of bounds\n");
                                    return x;
                    }
            }

            inline Float2 Perpendicular() const {
                    return Float2(-y,x);
            }

    };


    #ifndef VEX2_INVALID
            #define VEX2_INVALID Float2(FLT_MAX,FLT_MAX)
    #endif

    #ifndef VEX2_MAX
            #define VEX2_MAX Float2(FLT_MAX,FLT_MAX)
    #endif

    #ifndef VEX2_MIN
            #define VEX2_MIN Float2(-FLT_MAX,-FLT_MAX)
    #endif

    inline Float2 abs(const Float2& v){ return Float2(::fabsf(v.x),::fabsf(v.y)); }
    inline Float2 tan(const Float2& v){ return Float2(::tanf(v.x), ::tanf(v.y));  }
    inline Float2 cos(const Float2& v){ return Float2(::cosf(v.x), ::cosf(v.y));  }
    inline Float2 sin(const Float2& v){ return Float2(::tanf(v.x), ::tanf(v.y));  }

    inline Float2 fabs(const Float2& v){ return Float2(::fabsf(v.x),::fabsf(v.y)); }
    inline Float2 tanf(const Float2& v){ return Float2(::tanf(v.x), ::tanf(v.y));  }
    inline Float2 cosf(const Float2& v){ return Float2(::cosf(v.x), ::cosf(v.y));  }
    inline Float2 sinf(const Float2& v){ return Float2(::tanf(v.x), ::tanf(v.y));  }

    ///////////////////////////////////////////////////////////////////
    /// \brief Normalize a 2D Vector
    ///
    /// Inline function to normalize a 2D Vector.  Included for CG
    /// compatibility.
    ///
    /// \param v Vector to normailze
    ///
    /// \return Normalized 2D vector
    ///////////////////////////////////////////////////////////////////
    inline Float2 normalize(Float2 v){
            return v.UnitVector();
    }


    inline float magnitude(Float2 v){
            return sqrtf(dot(v,v));
    }




    inline Float2 Min(Float2 v0, Float2 v1){ return Float2( SCI::Min(v0.x,v1.x), SCI::Min(v0.y,v1.y) );  }
    inline Float2 Max(Float2 v0, Float2 v1){ return Float2( SCI::Max(v0.x,v1.x), SCI::Max(v0.y,v1.y) );  }

    //inline float MIN(Float2 a){                            return MIN(a.x,a.y);                            }
    ////inline float MINABS(Float2 a){                            return MINABS(a.x,a.y);                            }
    //inline Float2 MIN(Float2 a, Float2 b){                    return Float2(MIN(a.x,b.x),MIN(a.y,b.y));                            }
    //inline Float2 MIN(Float2 a, Float2 b, Float2 c){            return Float2(MIN(a.x,b.x,c.x),MIN(a.y,b.y,c.y));                }
    //inline Float2 MIN(Float2 a, Float2 b, Float2 c, Float2 d){    return Float2(MIN(a.x,b.x,c.x,d.x),MIN(a.y,b.y,c.y,d.y));    }
    //
    //inline float MAX(Float2 a){                    return MAX(a.x,a.y);                            }
    //inline float MAXABS(Float2 a){                    return MAXABS(a.x,a.y);                            }
    //inline Float2 MAX(Float2 a, Float2 b){                    return Float2(MAX(a.x,b.x),MAX(a.y,b.y));                            }
    //inline Float2 MAX(Float2 a, Float2 b, Float2 c){            return Float2(MAX(a.x,b.x,c.x),MAX(a.y,b.y,c.y));                }
    //inline Float2 MAX(Float2 a, Float2 b, Float2 c, Float2 d){    return Float2(MAX(a.x,b.x,c.x,d.x),MAX(a.y,b.y,c.y,d.y));    }
    //
    //inline void Swap(Float2& v0, Float2& v1){
    //    Float2 tmp = v0;
    //    v0 = v1;
    //    v1 = tmp;
    //}

    //#include "global.h"




    inline Float2 plerp(Float2 cen, Float2 p0, Float2 p1, float t ){
        float a0 = atan2f( p0.y-cen.y, p0.x-cen.x );
        float a1 = atan2f( p1.y-cen.y, p1.x-cen.x );

        float r0 = (p0-cen).Length();
        float r1 = (p1-cen).Length();

        if(a0 <= 0){ a0 += 2.0f * PI; }
        if(a1 <= 0){ a1 += 2.0f * PI; }

    //    if( fabsf(r0-r1) > 10 && fabsf(a0-a1) < 0.5f ){
    //        a1 += 2.0f * PI;
    //    }
        if( fabsf(a0-a1) < 0.5f ){
               a1 += 2.0f * PI;
        }

        float angle  = lerp(a0,a1,t);
        float radius = lerp(r0,r1,t);

        return cen + Float2( ::cosf( angle ), ::sinf( angle ) ) * radius;
    }
*/
}
