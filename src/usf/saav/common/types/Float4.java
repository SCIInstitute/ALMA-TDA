package usf.saav.common.types;

import usf.saav.common.MathX;
import usf.saav.common.mvc.swing.TGraphics;


public class Float4 {

    public static final Float4 WHITE   = new Float4(1,1,1,1);
	public static final Float4 BLACK   = new Float4(0,0,0,1);
	public static final Float4 RED     = new Float4(1,0,0,1);
	public static final Float4 GREEN   = new Float4(0,1,0,1);
	public static final Float4 BLUE    = new Float4(0,0,1,1);
	public static final Float4 GRAY127 = new Float4(0.5,0.5,0.5,1);
    
	public float x;
    public float y;
    public float z;
    public float w;

    public Float4(float _x, float _y, float _z, float _w){
                x = _x;
                y = _y;
                z = _z;
                w = _w;
        }

	public Float4(double _x, double _y, double _z, double _w) {
        x = (float)_x;
        y = (float)_y;
        z = (float)_z;
        w = (float)_w;
	}
	
    public Float4(Float3 p, float _w) {
        x = (float)p.x;
        y = (float)p.y;
        z = (float)p.z;
        w = (float)_w;
	}

	public int GetRed(){   return MathX.clamp((int)(x*255),0,255); }
    public int GetGreen(){ return MathX.clamp((int)(y*255),0,255); }
    public int GetBlue(){  return MathX.clamp((int)(z*255),0,255); }
    public int GetAlpha(){ return MathX.clamp((int)(w*255),0,255); }

	public static Float4 lerp(Float4 v0, Float4 v1, double d) {
		return new Float4(
					v0.x*(1-d) + v1.x*d,
					v0.y*(1-d) + v1.y*d,
					v0.z*(1-d) + v1.z*d,
					v0.w*(1-d) + v1.w*d
				);
	}
	
	public int intColor() {
		return (GetAlpha()<<24) | (GetRed()<<16) | (GetGreen()<<8) | GetBlue();
	}	
	
	public static float dot(Float4 v1, Float4 v2){
        return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z + v1.w*v2.w;
	}


    /*
        inline Vex4(const Vex3 _xyz, const float _w){
                data[0] = _xyz.x;
                data[1] = _xyz.y;
                data[2] = _xyz.z;
                data[3] = _w;
        }

        inline Vex4( unsigned int uint_col ){
            SetFromUInt( uint_col );
        }

    ///////////////////////////////////////////////////////////////
        /// \brief Sets the value of the coordinates
        ///
        /// \param x value of x (red) coordinate
        /// \param y value of y (green) coordinate
        /// \param z value of z (blue) coordinate
        /// \param w value of w (alpha) coordinate
        ///////////////////////////////////////////////////////////////
        inline void Set(const float _x = 0, const float _y = 0, const float _z = 0, const float _w = 0){
                data[0] = _x;
                data[1] = _y;
                data[2] = _z;
                data[3] = _w;
        }

        ///////////////////////////////////////////////////////////////
        /// \brief Vector Output - Prints to Console
        ///////////////////////////////////////////////////////////////
        inline void Print() const {
                //printf("< %f %f %f %f >\n",x,y,z,w);

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
                        printf("FLT_MAX, ");
                }
                else if(z == -FLT_MAX){
                        printf("-FLT_MAX, ");
                }
                else{
                        printf("%f, ",z);
                }

                if(w == FLT_MAX){
                        printf("FLT_MAX >\n");
                }
                else if(w == -FLT_MAX){
                        printf("-FLT_MAX >\n");
                }
                else{
                        printf("%f >\n",w);
                }
        }

    ///////////////////////////////////////////////////////////////
        /// \brief Vector Scale
        ///
        /// \param op2 Scale amount
        ///
        /// \return result of the scale
        ///////////////////////////////////////////////////////////////
        inline Vex4 operator*(const float op2) const {
                return Vex4(x*op2, y*op2, z*op2, w*op2);
        }

        inline Vex4 operator*(const Vex4& op2) const {
                return Vex4(x*op2.x,y*op2.y,z*op2.z,w*op2.w);
        }

    ///////////////////////////////////////////////////////////////
        /// \brief Vector Divide
        ///
        /// \param op2 Divisor amount
        ///
        /// \return result of the division
        ///////////////////////////////////////////////////////////////
        inline Vex4 operator/(const float op2) const {
                return Vex4(x/op2, y/op2, z/op2, w/op2);
        }

        inline Vex4 & operator/=(const float op2) {
                x/=op2;
                y/=op2;
                z/=op2;
                w/=op2;
                return (*this);
        }

    ///////////////////////////////////////////////////////////////
        /// \brief Checks if the vector has valid values
        ///
        /// \return true if valid
        ///////////////////////////////////////////////////////////////
        inline bool isValid() const {
                return (x!=FLT_MAX) && (x!=-FLT_MAX) && (y!=FLT_MAX) && (y!=-FLT_MAX) && (z!=FLT_MAX) && (z!=-FLT_MAX) && (w!=FLT_MAX) && (w!=-FLT_MAX);
        }

        Vex4& operator+=(const Vex4& op2){
                x+=op2.x;
                y+=op2.y;
                z+=op2.z;
                w+=op2.w;
                return (*this);
        }

        Vex4& operator-=(const Vex4& op2){
                x-=op2.x;
                y-=op2.y;
                z-=op2.z;
                w-=op2.w;
                return (*this);
        }

        inline Vex4 operator+(const Vex4& op2) const {
                return Vex4(x+op2.x,y+op2.y,z+op2.z,w+op2.w);
        }

        inline Vex4 operator-(const Vex4& op2) const {
                return Vex4(x-op2.x,y-op2.y,z-op2.z,w-op2.w);
        }

        Vex4& operator+=(const float op2){
                x+=op2;
                y+=op2;
                z+=op2;
                w+=op2;
                return (*this);
        }

        Vex4& operator-=(const float op2){
                x-=op2;
                y-=op2;
                z-=op2;
                w-=op2;
                return (*this);
        }

        inline float & operator[](int i){
                if(i == 1){ return y; }
                if(i == 2){ return z; }
                if(i == 3){ return w; }
                return x;
        }

        inline Vex4 operator+(const float op2) const {
                return Vex4(x+op2,y+op2,z+op2,w+op2);
        }

        inline Vex4 operator-(const float op2) const {
                return Vex4(x-op2,y-op2,z-op2,w-op2);
        }


        inline Vex4 operator-() const {
                return Vex4(-x,-y,-z,-w);
        }

        double* CopyTo(double vex[4]) const {
                vex[0] = x;
                vex[1] = y;
                vex[2] = z;
                vex[3] = w;
                return vex;
        }

        inline unsigned int UIntColor() const {
                unsigned int rgb[4];

                rgb[0] = Clamp((int)(x*255),0,255);
                rgb[1] = Clamp((int)(y*255),0,255);
                rgb[2] = Clamp((int)(z*255),0,255);
                rgb[3] = Clamp((int)(w*255),0,255);

                return rgb[0] | (rgb[1] << 8) | (rgb[2] << 16) | (rgb[3] << 24);
        }

        inline void SetFromUInt(const unsigned int color){
                x = ((float) ((color>>0) &0xFF)) / 255.0f;
                y = ((float) ((color>>8) &0xFF)) / 255.0f;
                z = ((float) ((color>>16)&0xFF)) / 255.0f;
                w = ((float) ((color>>24)&0xFF)) / 255.0f;
        }


        */
	
    public Float3 xyz()  { return new Float3(x,y,z); }
    public Float3 yzx()  { return new Float3(y,z,x); }
    public Float3 zxy()  { return new Float3(z,x,y); }
    public Float3 zyx()  { return new Float3(z,y,x); }
    public Float3 xxx()  { return new Float3(x,x,x); }
    public Float3 yyy()  { return new Float3(y,y,y); }
    public Float3 zzz()  { return new Float3(z,z,z); }

    public Float2 zw()  { return new Float2(z,w); }
    public Float2 yz()  { return new Float2(y,z); }
    public Float2 xy()  { return new Float2(x,y); }
    public Float2 wy()  { return new Float2(w,y); }
    public Float2 xz()  { return new Float2(x,z); }
    public Float2 wx()  { return new Float2(w,x); }
    public Float2 yw()  { return new Float2(y,w); }

	public Float4 divide(float f) {
		return new Float4( x/f, y/f, z/f, w/f);
	}

	public void setFill(TGraphics g) {
		g.fill(x*255,y*255,z*255,w*255);
	}
	public void setStroke(TGraphics g) {
		g.stroke(x*255,y*255,z*255,w*255);
	}
	
    @Override public String toString() {
    	String ret = "";
    	if( x == Float.MAX_VALUE ) ret += "< FLT_MAX, ";
    	else if( Float.isNaN(x) ) ret += "< NaN, ";
    	else ret += "< " + Float.toString(x) + ", ";

    	if( y == Float.MAX_VALUE ) ret += "FLT_MAX, ";
    	else if( Float.isNaN(y) ) ret += "NaN, ";
    	else ret += Float.toString(y) + ", ";

    	if( z == Float.MAX_VALUE ) ret += "FLT_MAX, ";
    	else if( Float.isNaN(z) ) ret += "NaN, ";
    	else ret += Float.toString(z) + ", ";

    	if( w == Float.MAX_VALUE ) ret += "FLT_MAX >";
    	else if( Float.isNaN(w) ) ret += "NaN >";
    	else ret += Float.toString(w) + " >";
    	
    	return ret;
    }

	public void set(float _x, float _y, float _z, float _w) {
		x = _x;
		y = _y;
		z = _z;
		w = _w;
		// TODO Auto-generated method stub
		
	}



};

/*

#ifndef VEX4_INVALID
        #define VEX4_INVALID Vex4(FLT_MAX,FLT_MAX,FLT_MAX,FLT_MAX)
#endif


inline Vex4 abs(const Vex4& v){ return Vex4(::fabsf(v.x),::fabsf(v.y),::fabsf(v.z),::fabsf(v.w)); }
inline Vex4 tan(const Vex4& v){ return Vex4(::tanf(v.x), ::tanf(v.y), ::tanf(v.z), ::tanf(v.w) );  }
inline Vex4 cos(const Vex4& v){ return Vex4(::cosf(v.x), ::cosf(v.y), ::cosf(v.z), ::cosf(v.w) );  }
inline Vex4 sin(const Vex4& v){ return Vex4(::tanf(v.x), ::tanf(v.y), ::tanf(v.z), ::tanf(v.w) );  }

inline Vex4 fabs(const Vex4& v){ return Vex4(::fabsf(v.x),::fabsf(v.y),::fabsf(v.z),::fabsf(v.w)); }
inline Vex4 tanf(const Vex4& v){ return Vex4(::tanf(v.x), ::tanf(v.y), ::tanf(v.z), ::tanf(v.w) );  }
inline Vex4 cosf(const Vex4& v){ return Vex4(::cosf(v.x), ::cosf(v.y), ::cosf(v.z), ::cosf(v.w) );  }
inline Vex4 sinf(const Vex4& v){ return Vex4(::tanf(v.x), ::tanf(v.y), ::tanf(v.z), ::tanf(v.w));  }


inline float magnitude(const Vex4 & v){
        return sqrtf(dot(v,v));
}


inline void Swap(Vex4& v0, Vex4& v1){
        Vex4 tmp = v0;
        v0 = v1;
        v1 = tmp;
}

inline Vex4 lerp( const Vex4 & v0, const Vex4 & v1, float t ){
    return (v0)*(1.0f-t)+(v1)*(t);
}
}

*/

