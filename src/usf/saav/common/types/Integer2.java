package usf.saav.common.types;

public class Integer2 {

	public final static Integer2 INVALID = new Integer2(Integer.MAX_VALUE,
			Integer.MAX_VALUE);

	public int x;
	public int y;

	public Integer2() {
		x = y = 0;
	}

	public Integer2(int _x, int _y) {
		x = _x;
		y = _y;
	}

	public void Set(int _x, int _y) {
		x = _x;
		y = _y;
	}

	public Integer2 add(Integer2 op2) {
		return new Integer2(x + op2.x, y + op2.y);
	}

	public Integer2 add(int op2) {
		return new Integer2(x + op2, y + op2);
	}

	public static Integer2 add(Integer2 op1, Integer2 op2) {
		return new Integer2(op1.x + op2.x, op1.y + op2.y);
	}

	public static Integer2 add(Integer2 op1, int op2) {
		return new Integer2(op1.x + op2, op1.y + op2);
	}

	public static Integer2 subtract(Integer2 op1, Integer2 op2) {
		return new Integer2(op1.x - op2.x, op1.y - op2.y);
	}

	public static Integer2 subtract(Integer2 op1, int op2) {
		return new Integer2(op1.x - op2, op1.y - op2);
	}

	public static Integer2 multiply(Integer2 op1, int op2) {
		return new Integer2(op1.x * op2, op1.y * op2);
	}

	public static Integer2 multiply(Integer2 op1, Integer2 op2) {
		return new Integer2(op1.x * op2.x, op1.y * op2.y);
	}

	public Integer2 divide(int op2) {
		return new Integer2(x / op2, y / op2);
	}

	public Integer2 divide(Integer2 op2) {
		return new Integer2(x / op2.x, y / op2.y);
	}

	public static Integer2 divide(Integer2 op1, int op2) {
		return new Integer2(op1.x / op2, op1.y / op2);
	}

	public static Integer2 divide(Integer2 op1, Integer2 op2) {
		return new Integer2(op1.x / op2.x, op1.y / op2.y);
	}

	// /////////////////////////////////////////////////////////////
	// / \brief Checks if the vector has valid values
	// /
	// / \return true if valid
	// /////////////////////////////////////////////////////////////
	public boolean isValid() {
		return (x != Integer.MAX_VALUE) && (y != Integer.MAX_VALUE);
	}

	// /////////////////////////////////////////////////////////////
	// / \brief Vector Output - Prints to Console
	// /////////////////////////////////////////////////////////////
	public void Print() {
		if (x == Integer.MAX_VALUE) {
			System.out.print("< INT_MAX, ");
		} else {
			System.out.printf("< %d, ", x);
		}
		if (y == Integer.MAX_VALUE) {
			System.out.print("INT_MAX >\n");
		} else {
			System.out.printf("%d >\n", y);
		}
	}

	/*
	 * /////////////////////////////////////////////////////////////// ///
	 * \brief Scalar Multiply /// /// \param scalar scale to multiply to vector
	 * /// /// \return new vector with the resulting scale
	 * /////////////////////////////////////////////////////////////// inline
	 * Vex2 operator*(const int scalar) const { return Vex2(x*scalar, y*scalar);
	 * }
	 * 
	 * /////////////////////////////////////////////////////////////// ///
	 * \brief Component-wise Multiply /// /// \param scalar scale to multiply
	 * each component /// /// \return new vector with the resulting scaling
	 * /////////////////////////////////////////////////////////////// inline
	 * Vex2 operator*(const Vex2 & scalar) const { return Vex2(x*scalar.x,
	 * y*scalar.y); }
	 * 
	 * /////////////////////////////////////////////////////////////// ///
	 * \brief Scalar Divide /// /// \param scalar scale by which to divide the
	 * vector /// /// \return new vector with the resulting divide
	 * /////////////////////////////////////////////////////////////// inline
	 * Vex2 operator/(const int scalar) const { return Vex2(x/scalar, y/scalar);
	 * }
	 * 
	 * /////////////////////////////////////////////////////////////// ///
	 * \brief Component-wise Divide /// /// \param scalar scale by which to
	 * divide each component /// /// \return new vector with the resulting
	 * divide ///////////////////////////////////////////////////////////////
	 * inline Vex2 operator/(const Vex2 & scalar) const { return
	 * Vex2(x/scalar.x, y/scalar.y); }
	 * 
	 * /////////////////////////////////////////////////////////////// ///
	 * \brief Vector Add /// /// \param op2 vector to add to current /// ///
	 * \return new vector with the resulting add
	 * /////////////////////////////////////////////////////////////// inline
	 * Vex2 operator+(const Vex2& op2) const { return Vex2(x+op2.x, y+op2.y); }
	 * 
	 * /////////////////////////////////////////////////////////////// ///
	 * \brief Vector Add /// /// \param op2 value to add to each component ///
	 * /// \return new vector with the resulting add
	 * /////////////////////////////////////////////////////////////// inline
	 * Vex2 operator+(const int op2) const { return Vex2(x+op2, y+op2); }
	 * 
	 * /////////////////////////////////////////////////////////////// ///
	 * \brief Vector Subtract /// /// \param op2 vector to subtract from current
	 * /// /// \return new vector with the resulting subtract
	 * /////////////////////////////////////////////////////////////// inline
	 * Vex2 operator-(const Vex2 op2) const { return Vex2(x-op2.x, y-op2.y); }
	 * 
	 * /////////////////////////////////////////////////////////////// ///
	 * \brief Vector Subtract /// /// \param op2 value to subtract from each
	 * component /// /// \return new vector with the resulting subtract
	 * /////////////////////////////////////////////////////////////// inline
	 * Vex2 operator-(const int op2) const { return Vex2(x-op2, y-op2); }
	 * 
	 * /////////////////////////////////////////////////////////////// ///
	 * \brief Vector Set /// /// \param op2 vector to set the current to /// ///
	 * \return current vector modified
	 * /////////////////////////////////////////////////////////////// inline
	 * Vex2& operator=(const Vex2 op2){ x = op2.x; y = op2.y; return (*this); }
	 * 
	 * /////////////////////////////////////////////////////////////// ///
	 * \brief Vector Add/Set /// /// \param op2 vector to add to current /// ///
	 * \return current vector modified with the resulting add
	 * /////////////////////////////////////////////////////////////// inline
	 * Vex2& operator+=(const Vex2 op2) { x += op2.x; y += op2.y; return
	 * (*this); }
	 * 
	 * /////////////////////////////////////////////////////////////// ///
	 * \brief Vector Add/Set /// /// \param op2 value to add to each component
	 * /// /// \return current vector modified with the resulting add
	 * /////////////////////////////////////////////////////////////// inline
	 * Vex2& operator+=(const int op2) { x += op2; y += op2; return (*this); }
	 * 
	 * inline Vex2& operator-=(const Vex2& op2) { x -= op2.x; y -= op2.y; return
	 * (*this); }
	 * 
	 * inline Vex2& operator-=(const int op2) { x -= op2; y -= op2; return
	 * (*this); }
	 * 
	 * inline Vex2& operator*=(const int op2) { x *= op2; y *= op2; return
	 * (*this); }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * inline Vex2 operator-(){ return Vex2(-x,-y); }
	 * 
	 * 
	 * 
	 * 
	 * /////////////////////////////////////////////////////////////// ///
	 * \brief Unit Vector /// /// \return new unit vector of currect vector
	 * /////////////////////////////////////////////////////////////// inline
	 * Vex2 UnitVector() const { int length = Length(); if (length != 0.0f) {
	 * return Vex2(x,y) / length; } return Vex2(); }
	 * 
	 * 
	 * /////////////////////////////////////////////////////////////// ///
	 * \brief Normalize vector to magnitude of 1
	 * /////////////////////////////////////////////////////////////// inline
	 * void Normalize(){ int length = Length(); if (length != 0.0f) { x /=
	 * length; y /= length; } }
	 * 
	 * /////////////////////////////////////////////////////////////// ///
	 * \brief Vector Magnitude /// /// \return the magnitude of the vector
	 * /////////////////////////////////////////////////////////////// inline
	 * int Length() const { return sqrtf(x*x+y*y); }
	 * 
	 * inline int& operator[](const int i){ switch(i) { case 0: return x; case
	 * 1: return y; default:
	 * printf("ERROR (Vex2::operator[]): index out of bounds\n"); return x; } }
	 * 
	 * inline const int& operator[](const int i) const{ switch(i) { case 0:
	 * return x; case 1: return y; default:
	 * printf("ERROR (Vex2::operator[]): index out of bounds\n"); return x; } }
	 * 
	 * inline Vex2 Perpendicular() const { return Vex2(-y,x); }
	 * 
	 * };
	 * 
	 * 
	 * #ifndef VEX2_INVALID #define VEX2_INVALID Vex2(FLT_MAX,FLT_MAX) #endif
	 * 
	 * #ifndef VEX2_MAX #define VEX2_MAX Vex2(FLT_MAX,FLT_MAX) #endif
	 * 
	 * #ifndef VEX2_MIN #define VEX2_MIN Vex2(-FLT_MAX,-FLT_MAX) #endif
	 * 
	 * inline Vex2 abs(const Vex2& v){ return Vex2(::fabsf(v.x),::fabsf(v.y)); }
	 * inline Vex2 tan(const Vex2& v){ return Vex2(::tanf(v.x), ::tanf(v.y)); }
	 * inline Vex2 cos(const Vex2& v){ return Vex2(::cosf(v.x), ::cosf(v.y)); }
	 * inline Vex2 sin(const Vex2& v){ return Vex2(::tanf(v.x), ::tanf(v.y)); }
	 * 
	 * inline Vex2 fabs(const Vex2& v){ return Vex2(::fabsf(v.x),::fabsf(v.y));
	 * } inline Vex2 tanf(const Vex2& v){ return Vex2(::tanf(v.x), ::tanf(v.y));
	 * } inline Vex2 cosf(const Vex2& v){ return Vex2(::cosf(v.x), ::cosf(v.y));
	 * } inline Vex2 sinf(const Vex2& v){ return Vex2(::tanf(v.x), ::tanf(v.y));
	 * }
	 * 
	 * /////////////////////////////////////////////////////////////////// ///
	 * \brief Normalize a 2D Vector /// /// Inline function to normalize a 2D
	 * Vector. Included for CG /// compatibility. /// /// \param v Vector to
	 * normailze /// /// \return Normalized 2D vector
	 * ///////////////////////////////////////////////////////////////////
	 * inline Vex2 normalize(Vex2 v){ return v.UnitVector(); }
	 * 
	 * inline int cross(Vex2 v1, Vex2 v2){ return v1.x*v2.y - v1.y*v2.x; }
	 * 
	 * /////////////////////////////////////////////////////////////// ///
	 * \brief Dot Product /// /// \param op2 Other vector to apply dot product
	 * /// /// \return result of dot product (x0*x1 + y0*y1)
	 * /////////////////////////////////////////////////////////////// inline
	 * int dot(Vex2 v1, Vex2 v2){ return v1.x*v2.x + v1.y*v2.y; }
	 * 
	 * inline int magnitude(Vex2 v){ return sqrtf(dot(v,v)); }
	 * 
	 * 
	 * 
	 * 
	 * inline Vex2 Min(Vex2 v0, Vex2 v1){ return Vex2( SCI::Min(v0.x,v1.x),
	 * SCI::Min(v0.y,v1.y) ); } inline Vex2 Max(Vex2 v0, Vex2 v1){ return Vex2(
	 * SCI::Max(v0.x,v1.x), SCI::Max(v0.y,v1.y) ); }
	 * 
	 * //inline int MIN(Vex2 a){ return MIN(a.x,a.y); } ////inline int
	 * MINABS(Vex2 a){ return MINABS(a.x,a.y); } //inline Vex2 MIN(Vex2 a, Vex2
	 * b){ return Vex2(MIN(a.x,b.x),MIN(a.y,b.y)); } //inline Vex2 MIN(Vex2 a,
	 * Vex2 b, Vex2 c){ return Vex2(MIN(a.x,b.x,c.x),MIN(a.y,b.y,c.y)); }
	 * //inline Vex2 MIN(Vex2 a, Vex2 b, Vex2 c, Vex2 d){ return
	 * Vex2(MIN(a.x,b.x,c.x,d.x),MIN(a.y,b.y,c.y,d.y)); } // //inline int
	 * MAX(Vex2 a){ return MAX(a.x,a.y); } //inline int MAXABS(Vex2 a){ return
	 * MAXABS(a.x,a.y); } //inline Vex2 MAX(Vex2 a, Vex2 b){ return
	 * Vex2(MAX(a.x,b.x),MAX(a.y,b.y)); } //inline Vex2 MAX(Vex2 a, Vex2 b, Vex2
	 * c){ return Vex2(MAX(a.x,b.x,c.x),MAX(a.y,b.y,c.y)); } //inline Vex2
	 * MAX(Vex2 a, Vex2 b, Vex2 c, Vex2 d){ return
	 * Vex2(MAX(a.x,b.x,c.x,d.x),MAX(a.y,b.y,c.y,d.y)); } // //inline void
	 * Swap(Vex2& v0, Vex2& v1){ // Vex2 tmp = v0; // v0 = v1; // v1 = tmp; //}
	 * 
	 * //#include "global.h"
	 * 
	 * 
	 * inline Vex2 lerp( Vex2 v0, Vex2 v1, int t ){ return
	 * (v0)*(1.0f-t)+(v1)*(t); }
	 * 
	 * 
	 * inline Vex2 plerp(Vex2 cen, Vex2 p0, Vex2 p1, int t ){ int a0 = atan2f(
	 * p0.y-cen.y, p0.x-cen.x ); int a1 = atan2f( p1.y-cen.y, p1.x-cen.x );
	 * 
	 * int r0 = (p0-cen).Length(); int r1 = (p1-cen).Length();
	 * 
	 * if(a0 <= 0){ a0 += 2.0f * PI; } if(a1 <= 0){ a1 += 2.0f * PI; }
	 * 
	 * // if( fabsf(r0-r1) > 10 && fabsf(a0-a1) < 0.5f ){ // a1 += 2.0f * PI; //
	 * } if( fabsf(a0-a1) < 0.5f ){ a1 += 2.0f * PI; }
	 * 
	 * int angle = lerp(a0,a1,t); int radius = lerp(r0,r1,t);
	 * 
	 * return cen + Vex2( ::cosf( angle ), ::sinf( angle ) ) * radius; }
	 */

}
