package usf.saav.common.types;


public class Double3 {

	public static final Double3 NaND = new Double3(  Double.NaN, 	   Double.NaN, 		   Double.NaN 	   );
	public static final Double3 MAX 	= new Double3(  Double.MAX_VALUE,  Double.MAX_VALUE,  Double.MAX_VALUE );
	public static final Double3 MIN 	= new Double3( -Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE );


	public double x;
	public double y;
	public double z;


	public Double3() { set(0,0,0);	}
	public Double3(Float3 p) { set(p.x,p.y,p.z); }
	public Double3(Double3 p) { set(p.x,p.y,p.z); }
	public Double3(float  _x, float  _y, float  _z) { set(_x,_y,_z); }
	public Double3(double _x, double _y, double _z) { set(_x,_y,_z); }

	public Double3 clone(){
		return new Double3( x,y,z );
	}

	///////////////////////////////////////////////////////////////
	/// \brief Sets the value of the coordinates
	///
	/// \param x value of x (red) coordinate
	/// \param y value of y (green) coordinate
	/// \param z value of z (blue) coordinate
	///////////////////////////////////////////////////////////////
	public void set(double _x, double _y, double _z){
		x = _x;
		y = _y;
		z = _z;
	}

	public void set(Double3 v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}


	public int GetRed(){ return (int)(x*255); }
	public int GetGreen(){ return (int)(y*255); }
	public int GetBlue(){ return (int)(z*255); }
	public int GetAlpha(){ return 255; }

	public 		  Double3 add(Double3 op2) { return new Double3(x+op2.x, y+op2.y, z+op2.z);    }
	public 		  Double3 add(double  op2) { return new Double3(x+op2, y+op2, z+op2);    }
	public static Double3 add(Double3 op1, Double3 op2) { return new Double3(op1.x+op2.x, op1.y+op2.y, op1.z+op2.z);    }
	public static Double3 add(Double3 op1, double  op2) { return new Double3(op1.x+op2, op1.y+op2, op1.z+op2);    }

	public 		  Double3 subtract(Double3 op2) {            return new Double3(x-op2.x, y-op2.y, z-op2.z);    }
	public 		  Double3 subtract(double  op2) {            return new Double3(x-op2, y-op2, z-op2);    }
	public static Double3 subtract(Double3 op1, Double3 op2) {            return new Double3(op1.x-op2.x, op1.y-op2.y, op1.z-op2.z);    }
	public static Double3 subtract(Double3 op1, double  op2) {            return new Double3(op1.x-op2, op1.y-op2, op1.z-op2);    }

	public 		  Double3 multiply(Double3 op2) {      return new Double3(x*op2.x, y*op2.y, z*op2.z);    }
	public 		  Double3 multiply(double f) { return new Double3( x*f, y*f, z*f );	}
	public static Double3 multiply(Double3 op1, double  op2) {      return new Double3(op1.x*op2, op1.y*op2, op1.z*op2);    }
	public static Double3 multiply(Double3 op1, Double3 op2) {      return new Double3(op1.x*op2.x, op1.y*op2.y, op1.z*op2.z);    }

	public 		  Double3 divide(double  op2) { return new Double3(x/op2, y/op2, z/op2);    }
	public 		  Double3 divide(Double3 op2) { return new Double3(x/op2.x, y/op2.y, z/op2.z);    }    
	public static Double3 divide(Double3 op1, double  op2) { return new Double3(op1.x/op2, op1.y/op2, op1.z/op2);    }
	public static Double3 divide(Double3 op1, Double3 op2) { return new Double3(op1.x/op2.x, op1.y/op2.y, op1.z/op2.z);    }

	public void addTo(Double3 op2) {
		x += op2.x;
		y += op2.y;
		z += op2.z;		
	}


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
	public static Double3 cross(Double3 v1, Double3 v2){
		Double3 ret = new Double3();
		ret.x = v1.y*v2.z - v1.z*v2.y;
		ret.y = v1.z*v2.x - v1.x*v2.z;
		ret.z = v1.x*v2.y - v1.y*v2.x;
		return ret;
	}

	public static Double3 OuterProduct(Double3 v1, Double3 v2){
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
	public static double dot(Double3 v1, Double3 v2){
		return ((v1.x*v2.x) + (v1.y*v2.y) + (v1.z*v2.z));
	}

	public static double InnerProduct(Double3 v1, Double3 v2){
		return dot( v1, v2 );
	}


	///////////////////////////////////////////////////////////////
	/// \brief Unit Vector
	///
	/// \return new unit vector of currect vector
	///////////////////////////////////////////////////////////////
	public Double3 UnitVector() {
		double length = Length();
		if (length != 0.0f) {
			return new Double3(x/length, y/length, z/length);
		}
		return new Double3(0,0,0);
	}

	///////////////////////////////////////////////////////////////
	/// \brief Normalize vector to magnitude of 1
	///////////////////////////////////////////////////////////////
	public void Normalize(){
		double length = Length();
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
	public double Length()  {
		return (double) Math.sqrt(x*x+y*y+z*z);
	}    


	///////////////////////////////////////////////////////////////
	/// \brief Vector Magnitude
	///
	/// \return the magnitude of the vector
	///////////////////////////////////////////////////////////////
	public double Magnitude() {
		return (double) Math.sqrt(x*x+y*y+z*z);
	}



	public static Double3 lerp( Double3 v0, Double3 v1, double t ){
		return add( multiply(v0,1.0f-t), multiply(v1,t) );
	}


	public static Double3 blerp(Double3 a, Double3 b, Double3 c, Double3 d, double t0, double t1){
		return lerp(lerp(a,b,t0),lerp(c,d,t0),t1);
	}

	public static Double3 trilerp(Double3 a, Double3 b, Double3 c, Double3 d, Double3 e, Double3 f, Double3 g, Double3 h, double t0, double t1, double t2){
		return lerp( blerp(a,b,c,d,t0,t1), blerp(e,f,g,h,t0,t1), t2 );
	}
	
	public double get(int dim) {
		switch(dim){
		case 0: return x;
		case 1: return y;
		case 2: return z;
		}
		return 0;
	}
	

	public static Double3 abs(Double3 v){ return new Double3( Math.abs(v.x), Math.abs(v.y), Math.abs(v.z) ); }
	public static Double3 tan(Double3 v){ return new Double3( Math.tan(v.x), Math.tan(v.y), Math.tan(v.z) ); }
	public static Double3 cos(Double3 v){ return new Double3( Math.cos(v.x), Math.cos(v.y), Math.cos(v.z) ); }
	public static Double3 sin(Double3 v){ return new Double3( Math.tan(v.x), Math.tan(v.y), Math.tan(v.z) ); }
	
	public static Double3 min(Double3 v0, Double3 v1){ return new Double3( Math.min(v0.x,v1.x), Math.min(v0.y,v1.y), Math.min(v0.z,v1.z) );  }
	public static Double3 max(Double3 v0, Double3 v1){ return new Double3( Math.max(v0.x,v1.x), Math.max(v0.y,v1.y), Math.max(v0.z,v1.z) );  }
	
	public Float3 toFloat3() {	return new Float3(x,y,z);	}
	
	public static double distance(Double3 p0, Double3 p1) {
		double dx = p0.x-p1.x;
		double dy = p0.y-p1.y;
		double dz = p0.z-p1.z;
		return Math.sqrt( dx*dx + dy*dy + dz*dz );
	}	

	
	

	public double x() { return this.x; }
	public double y() { return this.y; }
	public double z() { return this.z; }

	public Double2 xx() { return new Double2(x,x); }
	public Double2 xy() { return new Double2(x,y); }
	public Double2 xz() { return new Double2(x,z); }

	public Double2 yx() { return new Double2(y,x); }
	public Double2 yy() { return new Double2(y,y); }
	public Double2 yz() { return new Double2(y,z); }

	public Double2 zx() { return new Double2(z,x); }
	public Double2 zy() { return new Double2(z,y); }
	public Double2 zz() { return new Double2(z,z); }
	
	public Double3 xxx() { return new Double3(x,x,x); }
	public Double3 xxy() { return new Double3(x,x,y); }
	public Double3 xxz() { return new Double3(x,x,z); }
	public Double3 xyx() { return new Double3(x,y,x); }
	public Double3 xyy() { return new Double3(x,y,y); }
	public Double3 xyz() { return new Double3(x,y,z); }
	public Double3 xzx() { return new Double3(x,z,x); }
	public Double3 xzy() { return new Double3(x,z,y); }
	public Double3 xzz() { return new Double3(x,z,z); }
	
	public Double3 yxx() { return new Double3(y,x,x); }
	public Double3 yxy() { return new Double3(y,x,y); }
	public Double3 yxz() { return new Double3(y,x,z); }
	public Double3 yyx() { return new Double3(y,y,x); }
	public Double3 yyy() { return new Double3(y,y,y); }
	public Double3 yyz() { return new Double3(y,y,z); }
	public Double3 yzx() { return new Double3(y,z,x); }
	public Double3 yzy() { return new Double3(y,z,y); }
	public Double3 yzz() { return new Double3(y,z,z); }

	public Double3 zxx() { return new Double3(z,x,x); }
	public Double3 zxy() { return new Double3(z,x,y); }
	public Double3 zxz() { return new Double3(z,x,z); }
	public Double3 zyx() { return new Double3(z,y,x); }
	public Double3 zyy() { return new Double3(z,y,y); }
	public Double3 zyz() { return new Double3(z,y,z); }
	public Double3 zzx() { return new Double3(z,z,x); }
	public Double3 zzy() { return new Double3(z,z,y); }
	public Double3 zzz() { return new Double3(z,z,z); }
	


	/*
	        inline Double3(Vex2 & xy, double z){
	                data[0] = xy.x;
	                data[1] = xy.y;
	                data[2] = z;
	        }



	        ///////////////////////////////////////////////////////////////
	        /// \brief Vector Output - Prints to Console
	        ///////////////////////////////////////////////////////////////
	        inline void Print() {
	                if(x == FLT_MAX){
	                        printf("< FLT_MAX, ");
	                }
	                else if(x == -FLT_MAX){
	                        printf("< -FLT_MAX, ");
	                }
	                else{
	                        printf("< %f, ",x);
	                }

	                if(y == FLT_MAX){
	                        printf("FLT_MAX, ");
	                }
	                else if(y == -FLT_MAX){
	                        printf("-FLT_MAX, ");
	                }
	                else{
	                        printf("%f, ",y);
	                }

	                if(z == FLT_MAX){
	                        printf("FLT_MAX >\n");
	                }
	                else if(z == -FLT_MAX){
	                        printf("-FLT_MAX >\n");
	                }
	                else{
	                        printf("%f >\n",z);
	                }
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
	        inline Double3& operator+=(Double3& op2){
	                x += op2.x;
	                y += op2.y;
	                z += op2.z;
	                return (*this);
	        }

	        inline Double3& operator+=(double op2){
	                x += op2;
	                y += op2;
	                z += op2;
	                return (*this);
	        }

	        inline Double3& operator-=(Double3& op2){
	                x -= op2.x;
	                y -= op2.y;
	                z -= op2.z;
	                return (*this);
	        }

	        inline Double3& operator-=(double op2){
	                x -= op2;
	                y -= op2;
	                z -= op2;
	                return (*this);
	        }

	        inline Double3& operator/=(Double3& op2){
	                x /= op2.x;
	                y /= op2.y;
	                z /= op2.z;
	                return (*this);
	        }

	        inline Double3& operator/=(double op2){
	                x /= op2;
	                y /= op2;
	                z /= op2;
	                return (*this);
	        }

	        inline Double3& operator*=(Double3& op2){
	                x *= op2.x;
	                y *= op2.y;
	                z *= op2.z;
	                return (*this);
	        }

	        inline Double3& operator*=(double op2){
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
	        inline double& operator[](int i){
	                switch(i) {
	                        case 0:    return x;
	                        case 1: return y;
	                        case 2:    return z;
	                        default:
	                                printf("ERROR (Double3::operator[]): index out of bounds\n");
	                                return x;
	                }
	        }

	        inline double& operator[](int i) const{
	                switch(i) {
	                        case 0:    return x;
	                        case 1: return y;
	                        case 2:    return z;
	                        default:
	                                printf("ERROR (Double3::operator[]): index out of bounds\n");
	                                return x;
	                }
	        }

	    ///////////////////////////////////////////////////////////////
	        /// \brief Vector Negate
	        ///
	        /// \return new vector that is the negative of the current
	        ///////////////////////////////////////////////////////////////
	        inline Double3 operator-()  {
	                return Double3(-x,-y,-z);
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
	                x = ((double) ((color>>0) &0xFF)) / 255.0f;
	                y = ((double) ((color>>8) &0xFF)) / 255.0f;
	                z = ((double) ((color>>16)&0xFF)) / 255.0f;
	        }

	    ///////////////////////////////////////////////////////////////
	        /// \brief Vector Set
	        ///
	        /// \param op2 vector to set the current to
	        ///
	        /// \return current vector modified
	        ///////////////////////////////////////////////////////////////
	        inline Double3& operator=(Double3& op2){
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
	        inline bool isValid() {
	                return (x!=FLT_MAX) && (x!=-FLT_MAX) && (y!=FLT_MAX) && (y!=-FLT_MAX) && (z!=FLT_MAX) && (z!=-FLT_MAX);
	        }


	        inline bool operator==(Double3& other) {
	                return (x == other.x) && (y == other.y) && (z == other.z);
	        }

	        inline bool operator!=(Double3& other) {
	                return (x != other.x) || (y != other.y) || (z != other.z);
	        }

	        inline Vex2 xx() { return Vex2(x,x); }
	        inline Vex2 xy() { return Vex2(x,y); }
	        inline Vex2 xz() { return Vex2(x,z); }
	        inline Vex2 yx() { return Vex2(y,x); }
	        inline Vex2 yy() { return Vex2(y,y); }
	        inline Vex2 yz() { return Vex2(y,z); }
	        inline Vex2 zx() { return Vex2(z,x); }
	        inline Vex2 zy() { return Vex2(z,y); }
	        inline Vex2 zz() { return Vex2(z,z); }


	        inline bool operator < (Double3 & right) {
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
	inline Double3 operator*(double f, Double3 & v){
	        return v*f;
	}


	inline double Distance(double x1, double x2,double y1, double y2, double z1, double z2){
	        double dx = x1 - x2;
	        double dy = y1 - y2;
	        double dz = z1 - z2;

	        return sqrtf(dx*dx + dy*dy + dz*dz);
	}




	///////////////////////////////////////////////////////////////////
	/// \brief Normalize a 3D Vector
	///
	/// Inline function to normalize a 3D Vector.
	///
	/// \param v Vector to normailze
	///
	/// \return Normalized 3D vector
	///////////////////////////////////////////////////////////////////
	inline Double3 normalize(Double3& v){
	        return v.UnitVector();
	}



	inline double magnitude(Double3& v){
	        return v.Magnitude();
	}

	inline Double3 CLAMP(Double3 v, double minv, double maxv){
	        v.x = Clamp(v.x,minv,maxv);
	        v.y = Clamp(v.y,minv,maxv);
	        v.z = Clamp(v.z,minv,maxv);
	        return v;
	}

	//inline double MIN(Double3 a){                            return MIN(a.x,a.y,a.z);                            }
	////inline double MINABS(Double3 a){                            return MINABS(a.x,a.y,a.z);                            }
	//inline Double3 MIN(Double3 a, Double3 b){                    return Double3(MIN(a.x,b.x),MIN(a.y,b.y),MIN(a.z,b.z));                            }
	//inline Double3 MIN(Double3 a, Double3 b, Double3 c){            return Double3(MIN(a.x,b.x,c.x),MIN(a.y,b.y,c.y),MIN(a.z,b.z,c.z));                }
	//inline Double3 MIN(Double3 a, Double3 b, Double3 c, Double3 d){    return Double3(MIN(a.x,b.x,c.x,d.x),MIN(a.y,b.y,c.y,d.y),MIN(a.z,b.z,c.z,d.z));    }
	//
	//inline double MAX(Double3 a){                    return MAX(a.x,a.y,a.z);                            }
	//inline double MAXABS(Double3 a){                    return MAXABS(a.x,a.y,a.z);                            }
	//inline Double3 MAX(Double3 a, Double3 b){                    return Double3(MAX(a.x,b.x),MAX(a.y,b.y),MAX(a.z,b.z));                            }
	//inline Double3 MAX(Double3 a, Double3 b, Double3 c){            return Double3(MAX(a.x,b.x,c.x),MAX(a.y,b.y,c.y),MAX(a.z,b.z,c.z));                }
	//inline Double3 MAX(Double3 a, Double3 b, Double3 c, Double3 d){    return Double3(MAX(a.x,b.x,c.x,d.x),MAX(a.y,b.y,c.y,d.y),MAX(a.z,b.z,c.z,d.z));    }

	inline void Swap(Double3& v0, Double3& v1){
	        Double3 tmp = v0;
	        v0 = v1;
	        v1 = tmp;
	}

 */
