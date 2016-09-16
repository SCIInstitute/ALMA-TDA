package usf.saav.common.types;

public class Double2 {
	
	public final static Double2 INVALID = new Double2(Double.NaN, Double.NaN);
	public final static Double2 MAX_VALUE = new Double2(Double.MAX_VALUE, Double.MAX_VALUE);
	public final static Double2 MNI_VALUE = new Double2(Double.MIN_VALUE, Double.MIN_VALUE);


    public double x;
    public double y;

    public Double2(){
    	x = y = 0;
    }

    public Double2(float _x, float _y){
    	x = _x;
    	y = _y;
    }

    public Double2(double _x, double _y){
    	x = _x;
    	y = _y;
    }

	public Double2(String sx, String sy) {
		x = Double.valueOf(sx);
		y = Double.valueOf(sy);
	}

	public Double2(Float2 flt2) {
		x = flt2.x;
		y = flt2.y;
	}

	public Double2(double[] xy) {
		x = (xy.length>=1) ? xy[0] : 0;
		y = (xy.length>=2) ? xy[1] : 0;
	}

	public Double2(Double2 xy) {
		x = xy.x;
		y = xy.y;
	}

	public void Set(double _x, double _y){ 
		x = _x; y = _y; 
	}
	
    public Double2 add(Double2 op2){ return new Double2(x+op2.x, y+op2.y);    }
    public Double2 add(double op2) {        return new Double2(x+op2, y+op2);    }
    public static Double2 add(Double2 op1, Double2 op2) {       return new Double2(op1.x+op2.x, op1.y+op2.y);    }
    public static Double2 add(Double2 op1, double op2) {        return new Double2(op1.x+op2, op1.y+op2);    }

    public static  Double2 subtract(Double2 op1, Double2 op2) {            return new Double2(op1.x-op2.x, op1.y-op2.y);    }
    public static  Double2 subtract(Double2 op1, double op2) {            return new Double2(op1.x-op2, op1.y-op2);    }
    
    public static Double2 multiply(Double2 op1, double op2) {      return new Double2(op1.x*op2, op1.y*op2);    }
    public static Double2 multiply(Double2 op1, Double2 op2) {      return new Double2(op1.x*op2.x, op1.y*op2.y);    }

    public Double2 divide(double op2) {            return new Double2(x/op2, y/op2);    }
    public Double2 divide(Double2 op2) {      return new Double2(x/op2.x, y/op2.y);    }    
    public static Double2 divide(Double2 op1, double op2) {            return new Double2(op1.x/op2, op1.y/op2);    }
    public static Double2 divide(Double2 op1, Double2 op2) {      return new Double2(op1.x/op2.x, op1.y/op2.y);    }
	
    
    ///////////////////////////////////////////////////////////////
    /// \brief Dot Product
    ///
    /// \param op2 Other vector to apply dot product
    ///
    /// \return result of dot product (x0*x1 + y0*y1)
    ///////////////////////////////////////////////////////////////
    public static double dot(Double2 v1, Double2 v2){
            return v1.x*v2.x + v1.y*v2.y;
    }

    
    ///////////////////////////////////////////////////////////////
    /// \brief Checks if the vector has valid values
    ///
    /// \return true if valid
    ///////////////////////////////////////////////////////////////
    public boolean isValid() {
            return !Double.isNaN(x) && !Double.isNaN(y);
    }
    

    ///////////////////////////////////////////////////////////////
    /// \brief Vector Output - Prints to Console
    ///////////////////////////////////////////////////////////////
    public void Print() {
    	if(Double.isNaN(x)){
        	System.out.print("< NaN, ");
        }
        else{
        	System.out.printf("< %f, ",x);
        }

        if(Double.isNaN(y)){
            	System.out.print("NaN >\n");
            }
            else{
            	System.out.printf("%f >\n",y);
            }
    }
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	if(Double.isNaN(x)){
        	sb.append("< NaN, ");
        }
        else{
        	sb.append("< " + x + ", ");
        }

        if(Double.isNaN(y)){
        	sb.append("NaN >");
            }
            else{
            	sb.append(y + " >");
            }
        return sb.toString();    
    }
    

	public double Length() {
		return Math.sqrt(x*x+y*y);
	}    
	
    public static double  min(Double2 a){                            		return Math.min(a.x,a.y); }
    public static Double2 min(Double2 a, Double2 b){ 						return new Double2( Math.min(a.x,b.x), Math.min(a.y,b.y) ); }
    //public static Double2 min(Double2 a, Double2 b, Double2 c){				return new Double2(MathX.min(a.x,b.x,c.x), MathX.min(a.y,b.y,c.y)); }
    //public static Double2 min(Double2 a, Double2 b, Double2 c, Double2 d){	return new Double2(MathX.min(a.x,b.x,c.x,d.x), MathX.min(a.y,b.y,c.y,d.y)); }
    
    public static double  max(Double2 a){									return Math.max(a.x,a.y); }
    public static Double2 max(Double2 a, Double2 b){						return new Double2(Math.max(a.x,b.x),Math.max(a.y,b.y)); }
    //public static Double2 max(Double2 a, Double2 b, Double2 c){				return new Double2(MathX.max(a.x,b.x,c.x),MathX.max(a.y,b.y,c.y)); }
    //public static Double2 max(Double2 a, Double2 b, Double2 c, Double2 d){	return new Double2(MathX.max(a.x,b.x,c.x,d.x),MathX.max(a.y,b.y,c.y,d.y)); }

    //inline double MINABS(Double2 a){                            return MINABS(a.x,a.y);                            }
    //inline double MAXABS(Double2 a){                    return MAXABS(a.x,a.y);                            }

    

    public static Double2 abs(final Double2 v){ return new Double2(Math.abs(v.x), Math.abs(v.y)); }
    public static Double2 tan(final Double2 v){ return new Double2(Math.tan(v.x), Math.tan(v.y)); }
    public static Double2 cos(final Double2 v){ return new Double2(Math.cos(v.x), Math.cos(v.y)); }
    public static Double2 sin(final Double2 v){ return new Double2(Math.tan(v.x), Math.tan(v.y)); }

	public Angle getAngle() {
		return Angle.initRadians( Math.atan2( y, x) );
	}

	public static Double2 offsetAndScale(Double2 offset, Double2 value, double scale) {
		return new Double2(
				offset.x + value.x * scale,
				offset.y + value.y * scale
				);
	}

    /*
    public static Double2 fabs(const Double2& v){ return new Double2(::fabsf(v.x),::fabsf(v.y)); }
    public static Double2 tanf(const Double2& v){ return new Double2(::tanf(v.x), ::tanf(v.y));  }
    public static Double2 cosf(const Double2& v){ return new Double2(::cosf(v.x), ::cosf(v.y));  }
    public static Double2 sinf(const Double2& v){ return new Double2(::tanf(v.x), ::tanf(v.y));  }
    */    
    
    /*
        ///////////////////////////////////////////////////////////////
        /// \brief Scalar Multiply
        ///
        /// \param scalar scale to multiply to vector
        ///
        /// \return new vector with the resulting scale
        ///////////////////////////////////////////////////////////////
        inline Double2 operator*(const double scalar) const  { return Double2(x*scalar, y*scalar); }

        ///////////////////////////////////////////////////////////////
        /// \brief Component-wise Multiply
        ///
        /// \param scalar scale to multiply each component
        ///
        /// \return new vector with the resulting scaling
        ///////////////////////////////////////////////////////////////
        inline Double2 operator*(const Double2 & scalar) const  { return Double2(x*scalar.x, y*scalar.y); }

        ///////////////////////////////////////////////////////////////
        /// \brief Scalar Divide
        ///
        /// \param scalar scale by which to divide the vector
        ///
        /// \return new vector with the resulting divide
        ///////////////////////////////////////////////////////////////
        inline Double2 operator/(const double scalar) const { return Double2(x/scalar, y/scalar); }

        ///////////////////////////////////////////////////////////////
        /// \brief Component-wise Divide
        ///
        /// \param scalar scale by which to divide each component
        ///
        /// \return new vector with the resulting divide
        ///////////////////////////////////////////////////////////////
        inline Double2 operator/(const Double2 & scalar) const { return Double2(x/scalar.x, y/scalar.y); }

        ///////////////////////////////////////////////////////////////
        /// \brief Vector Add
        ///
        /// \param op2 vector to add to current
        ///
        /// \return new vector with the resulting add
        ///////////////////////////////////////////////////////////////
        inline Double2 operator+(const Double2& op2) const { return Double2(x+op2.x, y+op2.y); }

        ///////////////////////////////////////////////////////////////
        /// \brief Vector Add
        ///
        /// \param op2 value to add to each component
        ///
        /// \return new vector with the resulting add
        ///////////////////////////////////////////////////////////////
        inline Double2 operator+(const double op2) const { return Double2(x+op2, y+op2); }

        ///////////////////////////////////////////////////////////////
        /// \brief Vector Subtract
        ///
        /// \param op2 vector to subtract from current
        ///
        /// \return new vector with the resulting subtract
        ///////////////////////////////////////////////////////////////
        inline Double2 operator-(const Double2 op2) const { return Double2(x-op2.x, y-op2.y); }

        ///////////////////////////////////////////////////////////////
        /// \brief Vector Subtract
        ///
        /// \param op2 value to subtract from each component
        ///
        /// \return new vector with the resulting subtract
        ///////////////////////////////////////////////////////////////
        inline Double2 operator-(const double op2) const { return Double2(x-op2, y-op2); }

        ///////////////////////////////////////////////////////////////
        /// \brief Vector Set
        ///
        /// \param op2 vector to set the current to
        ///
        /// \return current vector modified
        ///////////////////////////////////////////////////////////////
        inline Double2& operator=(const Double2 op2){
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
        inline Double2& operator+=(const Double2 op2) {
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
        inline Double2& operator+=(const double op2) {
            x += op2;
            y += op2;
            return (*this);
        }

        inline Double2& operator-=(const Double2& op2) {
            x -= op2.x;
            y -= op2.y;
            return (*this);
        }

        inline Double2& operator-=(const double op2) {
            x -= op2;
            y -= op2;
            return (*this);
        }

        inline Double2& operator*=(const double op2) {
            x *= op2;
            y *= op2;
            return (*this);
        }





            inline Double2 operator-(){
                    return Double2(-x,-y);
            }




        ///////////////////////////////////////////////////////////////
            /// \brief Unit Vector
            ///
            /// \return new unit vector of currect vector
            ///////////////////////////////////////////////////////////////
            inline Double2 UnitVector() const {
                    double length = Length();
                    if (length != 0.0f) {
                            return Double2(x,y) / length;
                    }
                    return Double2();
            }


        ///////////////////////////////////////////////////////////////
            /// \brief Normalize vector to magnitude of 1
            ///////////////////////////////////////////////////////////////
            inline void Normalize(){
                    double length = Length();
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


            inline double& operator[](const int i){
                    switch(i) {
                            case 0:    return x;
                            case 1: return y;
                            default:
                                    printf("ERROR (Double2::operator[]): index out of bounds\n");
                                    return x;
                    }
            }

            inline const double& operator[](const int i) const{
                    switch(i) {
                            case 0:    return x;
                            case 1: return y;
                            default:
                                    printf("ERROR (Double2::operator[]): index out of bounds\n");
                                    return x;
                    }
            }

            inline Double2 Perpendicular() const {
                    return Double2(-y,x);
            }

    };





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
    inline Double2 normalize(Double2 v){
            return v.UnitVector();
    }

    inline double cross(Double2 v1, Double2 v2){
            return v1.x*v2.y - v1.y*v2.x;
    }

    inline double magnitude(Double2 v){
            return sqrtf(dot(v,v));
    }



    //inline void Swap(Double2& v0, Double2& v1){
    //    Double2 tmp = v0;
    //    v0 = v1;
    //    v1 = tmp;
    //}

    //#include "global.h"


    inline Double2 lerp( Double2 v0, Double2 v1, double t ){
        return (v0)*(1.0f-t)+(v1)*(t);
    }


    inline Double2 plerp(Double2 cen, Double2 p0, Double2 p1, double t ){
        double a0 = atan2f( p0.y-cen.y, p0.x-cen.x );
        double a1 = atan2f( p1.y-cen.y, p1.x-cen.x );

        double r0 = (p0-cen).Length();
        double r1 = (p1-cen).Length();

        if(a0 <= 0){ a0 += 2.0f * PI; }
        if(a1 <= 0){ a1 += 2.0f * PI; }

    //    if( fabsf(r0-r1) > 10 && fabsf(a0-a1) < 0.5f ){
    //        a1 += 2.0f * PI;
    //    }
        if( fabsf(a0-a1) < 0.5f ){
               a1 += 2.0f * PI;
        }

        double angle  = lerp(a0,a1,t);
        double radius = lerp(r0,r1,t);

        return cen + Double2( ::cosf( angle ), ::sinf( angle ) ) * radius;
    }
*/
}
