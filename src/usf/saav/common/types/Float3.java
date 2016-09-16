package usf.saav.common.types;

public class Float3 {
	public static final Float3 INVALID = new Float3( Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE );


	public static Float3 WHITE = new Float3(1,1,1);


	public float x;
	public float y;
	public float z;

	public Float3(float _x, float _y, float _z){
		x = _x;
		y = _y;
		z = _z;
	}

	public Float3() {
		x = y = z = 0;
	}

	public Float3(double _x, double _y, double _z) {
		x = (float)_x;
		y = (float)_y;
		z = (float)_z;
	}
	
	public Float3(Float3 p) {
		x = p.x;
		y = p.y;
		z = p.z;
	}

	public Float3(String sx, String sy, String sz) {
			x = Float.valueOf(sx);
			y = Float.valueOf(sy);
			y = Float.valueOf(sy);
	}

	public Float3(double[] p) {
		if( p.length > 0 ) x = (float)p[0];
		if( p.length > 1 ) y = (float)p[1];
		if( p.length > 2 ) z = (float)p[2];
	}

	public Float3 clone(){
		return new Float3( x,y,z );
	}

	///////////////////////////////////////////////////////////////
	/// \brief Sets the value of the coordinates
	///
	/// \param x value of x (red) coordinate
	/// \param y value of y (green) coordinate
	/// \param z value of z (blue) coordinate
	///////////////////////////////////////////////////////////////
	public void Set(float _x, float _y, float _z){
		x = _x;
		y = _y;
		z = _z;
	}

	public int GetRed(){ return (int)(x*255); }
	public int GetGreen(){ return (int)(y*255); }
	public int GetBlue(){ return (int)(z*255); }
	public int GetAlpha(){ return 255; }

	public Float3 add(Float3 op2){ return new Float3(x+op2.x, y+op2.y, z+op2.z);    }
	public Float3 add(float op2) {        return new Float3(x+op2, y+op2, z+op2);    }
	public static Float3 add(Float3 op1, Float3 op2) {       return new Float3(op1.x+op2.x, op1.y+op2.y, op1.z+op2.z);    }
	public static Float3 add(Float3 op1, float op2) {        return new Float3(op1.x+op2, op1.y+op2, op1.z+op2);    }

	public  Float3 subtract(Float3 op2) {            return new Float3(x-op2.x, y-op2.y, z-op2.z);    }
	public  Float3 subtract(float op2) {            return new Float3(x-op2, y-op2, z-op2);    }
	public static  Float3 subtract(Float3 op1, Float3 op2) {            return new Float3(op1.x-op2.x, op1.y-op2.y, op1.z-op2.z);    }
	public static  Float3 subtract(Float3 op1, float op2) {            return new Float3(op1.x-op2, op1.y-op2, op1.z-op2);    }

	public Float3 multiply(float  op2) { return new Float3(x*op2, y*op2, z*op2);    }
	public Float3 multiply(Float3 op2) { return new Float3(x*op2.x, y*op2.y, z*op2.z);    }
	public static Float3 multiply(Float3 op1, float op2) {      return new Float3(op1.x*op2, op1.y*op2, op1.z*op2);    }
	public static Float3 multiply(Float3 op1, Float3 op2) {      return new Float3(op1.x*op2.x, op1.y*op2.y, op1.z*op2.z);    }

	public Float3 divide(float op2) {            return new Float3(x/op2, y/op2, z/op2);    }
	public Float3 divide(Float3 op2) {      return new Float3(x/op2.x, y/op2.y, z/op2.z);    }    
	public static Float3 divide(Float3 op1, float op2) {            return new Float3(op1.x/op2, op1.y/op2, op1.z/op2);    }
	public static Float3 divide(Float3 op1, Float3 op2) {      return new Float3(op1.x/op2.x, op1.y/op2.y, op1.z/op2.z);    }


	///////////////////////////////////////////////////////////////////
	/// \brief Cross Product of 3D Vectors
	///
	/// Inline function to calculate the cross product of 3D Vectors.
	///
	/// \param v1 First vector in cross product
	/// \param v2 Second vector in cross product
	///
	/// \return 3D vector cross product
	///////////////////////////////////////////////////////////////////
	public static Float3 cross(Float3 v1, Float3 v2){
		Float3 ret = new Float3();
		ret.x = v1.y*v2.z - v1.z*v2.y;
		ret.y = v1.z*v2.x - v1.x*v2.z;
		ret.z = v1.x*v2.y - v1.y*v2.x;
		return ret;
	}

	public static Float3 OuterProduct(Float3 v1, Float3 v2){
		return cross(v1,v2);
	}

	///////////////////////////////////////////////////////////////////
	/// \brief Dot Product of 3D Vectors
	///
	/// Inline function to calculate the dot product 3D Vectors.
	///
	/// \param v1 First vector in cross product
	/// \param v2 Second vector in cross product
	///
	/// \return Vector dot product
	///////////////////////////////////////////////////////////////////
	public static float dot(Float3 v1, Float3 v2){
		return ((v1.x*v2.x) + (v1.y*v2.y) + (v1.z*v2.z));
	}

	public static float InnerProduct(Float3 v1, Float3 v2){
		return dot( v1, v2 );
	}


	///////////////////////////////////////////////////////////////
	/// \brief Unit Vector
	///
	/// \return new unit vector of currect vector
	///////////////////////////////////////////////////////////////
	public Float3 UnitVector() {
		float length = Length();
		if (length != 0.0f) {
			return new Float3(x/length, y/length, z/length);
		}
		return new Float3(0,0,0);
	}

	///////////////////////////////////////////////////////////////
	/// \brief Normalize vector to magnitude of 1
	///////////////////////////////////////////////////////////////
	public void Normalize(){
		float length = Length();
		if (length != 0.0f) {
			x /= length;
			y /= length;
			z /= length;
		}
	}


	///////////////////////////////////////////////////////////////
	/// \brief Vector Magnitude
	///
	/// \return the magnitude of the vector
	///////////////////////////////////////////////////////////////
	public float Length()  {
		return (float) Math.sqrt(x*x+y*y+z*z);
	}    


    ///////////////////////////////////////////////////////////////
        /// \brief Vector Magnitude
        ///
        /// \return the magnitude of the vector
        ///////////////////////////////////////////////////////////////
        public float Magnitude() {
                return (float) Math.sqrt(x*x+y*y+z*z);
        }
        
        
		
        public static Float3 lerp( Float3 v0, Float3 v1, float t ){
		    return add( multiply(v0,1.0f-t), multiply(v1,t) );
		}
		
		
        public static Float3 blerp(Float3 a, Float3 b, Float3 c, Float3 d, float t0, float t1){
		        return lerp(lerp(a,b,t0),lerp(c,d,t0),t1);
		}

		public float get(int dim) {
			switch(dim){
			case 0: return x;
			case 1: return y;
			case 2: return z;
			}
			return 0;
		}
		
		

		public Float3 addEquals(Float3 op2) {
			x+=op2.x;
			y+=op2.y;
			z+=op2.z;
			return this;			
		}

		public Double3 toDouble3() {
			return new Double3( x,y,z );
		}
		
        public boolean isValid() {
            return  (x!=Float.MAX_VALUE) && (x!=-Float.MAX_VALUE) && (!Float.isNaN(x)) &&  
            		(y!=Float.MAX_VALUE) && (y!=-Float.MAX_VALUE) && (!Float.isNaN(y)) && 
            		(z!=Float.MAX_VALUE) && (z!=-Float.MAX_VALUE) && (!Float.isNaN(z));
        }

		public static Float3 average(Float3 ... v) {
			Float3 ret = new Float3(0,0,0);
			for( Float3 f : v ){
				ret.x += f.x/(float)v.length;
				ret.y += f.y/(float)v.length;
				ret.z += f.z/(float)v.length;
			}
			return ret;
		}


        ///////////////////////////////////////////////////////////////
        /// \brief Vector Output - Prints to Console
        ///////////////////////////////////////////////////////////////
        @Override public String toString() {
        	String ret = "";
        	if( x == Float.MAX_VALUE ) ret += "< FLT_MAX, ";
        	else if( Float.isNaN(x) ) ret += "< NaN, ";
        	else ret += "< " + Float.toString(x) + ", ";

        	if( y == Float.MAX_VALUE ) ret += "FLT_MAX, ";
        	else if( Float.isNaN(y) ) ret += "NaN, ";
        	else ret += Float.toString(y) + ", ";

        	if( z == Float.MAX_VALUE ) ret += "FLT_MAX >";
        	else if( Float.isNaN(z) ) ret += "NaN >";
        	else ret += Float.toString(z) + " >";
        	
        	return ret;
        }

        

    	public float x() { return this.x; }
    	public float y() { return this.y; }
    	public float z() { return this.z; }

    	public Float2 xx() { return new Float2(x,x); }
    	public Float2 xy() { return new Float2(x,y); }
    	public Float2 xz() { return new Float2(x,z); }

    	public Float2 yx() { return new Float2(y,x); }
    	public Float2 yy() { return new Float2(y,y); }
    	public Float2 yz() { return new Float2(y,z); }

    	public Float2 zx() { return new Float2(z,x); }
    	public Float2 zy() { return new Float2(z,y); }
    	public Float2 zz() { return new Float2(z,z); }
    	
    	public Float3 xxx() { return new Float3(x,x,x); }
    	public Float3 xxy() { return new Float3(x,x,y); }
    	public Float3 xxz() { return new Float3(x,x,z); }
    	public Float3 xyx() { return new Float3(x,y,x); }
    	public Float3 xyy() { return new Float3(x,y,y); }
    	public Float3 xyz() { return new Float3(x,y,z); }
    	public Float3 xzx() { return new Float3(x,z,x); }
    	public Float3 xzy() { return new Float3(x,z,y); }
    	public Float3 xzz() { return new Float3(x,z,z); }
    	
    	public Float3 yxx() { return new Float3(y,x,x); }
    	public Float3 yxy() { return new Float3(y,x,y); }
    	public Float3 yxz() { return new Float3(y,x,z); }
    	public Float3 yyx() { return new Float3(y,y,x); }
    	public Float3 yyy() { return new Float3(y,y,y); }
    	public Float3 yyz() { return new Float3(y,y,z); }
    	public Float3 yzx() { return new Float3(y,z,x); }
    	public Float3 yzy() { return new Float3(y,z,y); }
    	public Float3 yzz() { return new Float3(y,z,z); }

    	public Float3 zxx() { return new Float3(z,x,x); }
    	public Float3 zxy() { return new Float3(z,x,y); }
    	public Float3 zxz() { return new Float3(z,x,z); }
    	public Float3 zyx() { return new Float3(z,y,x); }
    	public Float3 zyy() { return new Float3(z,y,y); }
    	public Float3 zyz() { return new Float3(z,y,z); }
    	public Float3 zzx() { return new Float3(z,z,x); }
    	public Float3 zzy() { return new Float3(z,z,y); }
    	public Float3 zzz() { return new Float3(z,z,z); }

		public static float distance(Float3 curVert, Float3 refVert) {
			float x = curVert.x - refVert.x;
			float y = curVert.y - refVert.y;
			float z = curVert.z - refVert.z;
			return (float) Math.sqrt( x*x + y*y + z*z );
		}
    	

	/*
        inline Float3(Float2 & xy, float z){
                data[0] = xy.x;
                data[1] = xy.y;
                data[2] = z;
        }







    ///////////////////////////////////////////////////////////////
        /// \brief Vector Add
        ///
        /// \param op2 vector to add to current
        ///
        /// \return new vector with the resulting add
        ///////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
        /// \brief Vector Add/Set
        ///
        /// \param op2 vector to add to current
        ///
        /// \return current vector modified with the resulting add
        ///////////////////////////////////////////////////////////////
        inline Float3& operator+=(Float3& op2){
                x += op2.x;
                y += op2.y;
                z += op2.z;
                return (*this);
        }

        inline Float3& operator+=(float op2){
                x += op2;
                y += op2;
                z += op2;
                return (*this);
        }

        inline Float3& operator-=(Float3& op2){
                x -= op2.x;
                y -= op2.y;
                z -= op2.z;
                return (*this);
        }

        inline Float3& operator-=(float op2){
                x -= op2;
                y -= op2;
                z -= op2;
                return (*this);
        }

        inline Float3& operator/=(Float3& op2){
                x /= op2.x;
                y /= op2.y;
                z /= op2.z;
                return (*this);
        }

        inline Float3& operator/=(float op2){
                x /= op2;
                y /= op2;
                z /= op2;
                return (*this);
        }

        inline Float3& operator*=(Float3& op2){
                x *= op2.x;
                y *= op2.y;
                z *= op2.z;
                return (*this);
        }

        inline Float3& operator*=(float op2){
                x *= op2;
                y *= op2;
                z *= op2;
                return (*this);
        }



    ///////////////////////////////////////////////////////////////
        /// \brief Vector Component Access
        ///
        /// \param i which element to access
        ///
        /// \return reference to the element requested
        ///////////////////////////////////////////////////////////////
        inline float& operator[](int i){
                switch(i) {
                        case 0:    return x;
                        case 1: return y;
                        case 2:    return z;
                        default:
                                printf("ERROR (Float3::operator[]): index out of bounds\n");
                                return x;
                }
        }

        inline float& operator[](int i) const{
                switch(i) {
                        case 0:    return x;
                        case 1: return y;
                        case 2:    return z;
                        default:
                                printf("ERROR (Float3::operator[]): index out of bounds\n");
                                return x;
                }
        }

    ///////////////////////////////////////////////////////////////
        /// \brief Vector Negate
        ///
        /// \return new vector that is the negative of the current
        ///////////////////////////////////////////////////////////////
        inline Float3 operator-()  {
                return Float3(-x,-y,-z);
        }

    ///////////////////////////////////////////////////////////////
        /// \brief Get the 32-bit color value of the vector
        ///
        /// \return 32-bit RGBA color
        ///////////////////////////////////////////////////////////////
        inline unsigned int UIntColor() {
                int rgb[3];

                rgb[0] = Clamp((int)(x*255),0,255);
                rgb[1] = Clamp((int)(y*255),0,255);
                rgb[2] = Clamp((int)(z*255),0,255);

                return (0xFF000000 + rgb[0] + (rgb[1] << 8) + (rgb[2] << 16));
        }

    ///////////////////////////////////////////////////////////////
        /// \brief Set the vector based upon the 32-bit color value
        ///
        /// \param color 32-bit color value to set the buffer
        ///////////////////////////////////////////////////////////////
        inline void SetFromUInt(unsigned int color){
                x = ((float) ((color>>0) &0xFF)) / 255.0f;
                y = ((float) ((color>>8) &0xFF)) / 255.0f;
                z = ((float) ((color>>16)&0xFF)) / 255.0f;
        }

    ///////////////////////////////////////////////////////////////
        /// \brief Vector Set
        ///
        /// \param op2 vector to set the current to
        ///
        /// \return current vector modified
        ///////////////////////////////////////////////////////////////
        inline Float3& operator=(Float3& op2){
                x = op2.x;
                y = op2.y;
                z = op2.z;
                return (*this);
        }

    ///////////////////////////////////////////////////////////////
        /// \brief Checks if the vector has valid values
        ///
        /// \return true if valid
        ///////////////////////////////////////////////////////////////

        inline bool operator==(Float3& other) {
                return (x == other.x) && (y == other.y) && (z == other.z);
        }

        inline bool operator!=(Float3& other) {
                return (x != other.x) || (y != other.y) || (z != other.z);
        }


        inline bool operator < (Float3 & right) {
            if(x<right.x) return true;
            if(x>right.x) return false;
            if(y<right.y) return true;
            if(y>right.y) return false;
            if(z<right.z) return true;
            if(z>right.z) return false;
            return false;
        }

	*/
};

/*
inline Float3 operator*(float f, Float3 & v){
        return v*f;
}


inline float Distance(float x1, float x2,float y1, float y2, float z1, float z2){
        float dx = x1 - x2;
        float dy = y1 - y2;
        float dz = z1 - z2;

        return sqrtf(dx*dx + dy*dy + dz*dz);
}

#ifndef VEX3_INVALID
        #define VEX3_INVALID Float3(FLT_MAX,FLT_MAX,FLT_MAX)
#endif

#ifndef VEX3_MAX
        #define VEX3_MAX Float3(FLT_MAX,FLT_MAX,FLT_MAX)
#endif

#ifndef VEX3_MIN
        #define VEX3_MIN Float3(-FLT_MAX,-FLT_MAX,-FLT_MAX)
#endif



inline Float3 Min(Float3 v0, Float3 v1){ return Float3( SCI::Min(v0.x,v1.x), SCI::Min(v0.y,v1.y), SCI::Min(v0.z,v1.z) );  }
inline Float3 Max(Float3 v0, Float3 v1){ return Float3( SCI::Max(v0.x,v1.x), SCI::Max(v0.y,v1.y), SCI::Max(v0.z,v1.z) );  }



///////////////////////////////////////////////////////////////////
/// \brief Normalize a 3D Vector
///
/// Inline function to normalize a 3D Vector.
///
/// \param v Vector to normailze
///
/// \return Normalized 3D vector
///////////////////////////////////////////////////////////////////
inline Float3 normalize(Float3& v){
        return v.UnitVector();
}



inline float magnitude(Float3& v){
        return v.Magnitude();
}

inline Float3 CLAMP(Float3 v, float minv, float maxv){
        v.x = Clamp(v.x,minv,maxv);
        v.y = Clamp(v.y,minv,maxv);
        v.z = Clamp(v.z,minv,maxv);
        return v;
}

//inline float MIN(Float3 a){                            return MIN(a.x,a.y,a.z);                            }
////inline float MINABS(Float3 a){                            return MINABS(a.x,a.y,a.z);                            }
//inline Float3 MIN(Float3 a, Float3 b){                    return Float3(MIN(a.x,b.x),MIN(a.y,b.y),MIN(a.z,b.z));                            }
//inline Float3 MIN(Float3 a, Float3 b, Float3 c){            return Float3(MIN(a.x,b.x,c.x),MIN(a.y,b.y,c.y),MIN(a.z,b.z,c.z));                }
//inline Float3 MIN(Float3 a, Float3 b, Float3 c, Float3 d){    return Float3(MIN(a.x,b.x,c.x,d.x),MIN(a.y,b.y,c.y,d.y),MIN(a.z,b.z,c.z,d.z));    }
//
//inline float MAX(Float3 a){                    return MAX(a.x,a.y,a.z);                            }
//inline float MAXABS(Float3 a){                    return MAXABS(a.x,a.y,a.z);                            }
//inline Float3 MAX(Float3 a, Float3 b){                    return Float3(MAX(a.x,b.x),MAX(a.y,b.y),MAX(a.z,b.z));                            }
//inline Float3 MAX(Float3 a, Float3 b, Float3 c){            return Float3(MAX(a.x,b.x,c.x),MAX(a.y,b.y,c.y),MAX(a.z,b.z,c.z));                }
//inline Float3 MAX(Float3 a, Float3 b, Float3 c, Float3 d){    return Float3(MAX(a.x,b.x,c.x,d.x),MAX(a.y,b.y,c.y,d.y),MAX(a.z,b.z,c.z,d.z));    }

inline void Swap(Float3& v0, Float3& v1){
        Float3 tmp = v0;
        v0 = v1;
        v1 = tmp;
}

*/
