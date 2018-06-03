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
package usf.saav.common;

import usf.saav.common.types.Float2;
import usf.saav.common.types.Float3;
import usf.saav.common.types.Float4;
import usf.saav.common.types.Ray;

public class GeometryX {
	public static final float ZERO_TOLERANCE = (float) 1.0e-10;
	
	

	public static float PointPlaneDistance(Float3 p, Float3 p0, Float3 norm){
		return Float3.dot(norm, Float3.subtract(p,p0) );
	}

	public static float PointPlaneDistance(Float3 p, Float4 pln){
		return Float4.dot(pln, new Float4(p,1.0f) );
	}

	
	
	public static Float3 BarycentricInterpolation( float[] b, Float3 [] vals ){
		Float3 ret = new Float3(0,0,0);
		for(int i = 0; i < b.length && i < vals.length; i++){
			ret = ret.add( vals[i].multiply(b[i]) );
		}
		return ret;
	}

	public static Float3 BarycentricInterpolationVector( float[] b, Float3 [] vals ){
		Float3 ret = new Float3(0,0,0);
		float  len = 0;
		for(int i = 0; i < b.length && i < vals.length; i++){
			float clen = vals[i].Length();
			ret = ret.add( vals[i].multiply(b[i]/clen) );
			len = len + clen;
		}
		return ret.multiply(len);
	}



	
	///////////////////////////////////////////////////////////////////
	/// \brief Calculate the plane formed by a 3D point and a normal
	///
	/// Calculates the constants (ABCD) of the plane equation 
	/// Ax + By + Cz + D = 0
	///
	/// \param p0 Point on the plane
	/// \param norm Normal of the plane
	///
	/// \return 4D vector with the values of ABCD
	///////////////////////////////////////////////////////////////////
	public static Float4 CalculatePlane(Float3 p0, Float3 norm){
		return new Float4(norm,-Float3.dot(norm,p0));
	}

	///////////////////////////////////////////////////////////////////
	/// \brief Calculate the plane formed by 3-3D points
	///
	/// Calculates the constants (ABCD) of the plane equation 
	/// Ax + By + Cz + D = 0
	/// Vertices are expected in counter-clockwise order.
	///
	/// \param p0 First point in plane
	/// \param p1 Second point in plane
	/// \param p2 Third point in plane
	///
	/// \return 4D vector with the values of ABCD
	///////////////////////////////////////////////////////////////////
	public static Float4 CalculatePlane(Float3 p0, Float3 p1, Float3 p2){
		Float3 p1p0 = Float3.subtract(p1,p0);
		Float3 p2p0 = Float3.subtract(p2,p0);
		Float3 norm = Float3.cross(p1p0,p2p0).UnitVector();
		return CalculatePlane(p0,norm);
	}
	
	///////////////////////////////////////////////////////////////////
	/// \brief Calculate the intersection of a ray and plane in 3D
	///
	/// \param r0 Start point of the ray
	/// \param rd Direction (and magnitude) of the ray (e.g. r1 - r0)
	/// \param plane Plane formed by the plane equation 
	///        Ax + By + Cz + D = 0
	///
	/// \return The interpolation value t of the intersection.  The 
	///         intersection equals r0 + t * rd.  With values of t < 0 
	///         or t > 1, the plane and ray do not intersect.  When the 
	///         ray and plane are near parallel, -1 is returned.
	///////////////////////////////////////////////////////////////////
	public static float RayPlaneIntersection(Float3 r0, Float3 rd, Float4 plane){
		float ndotr = Float3.dot(plane.xyz(),rd);
		if(Math.abs(ndotr) < ZERO_TOLERANCE){ return Float.NaN; }
		return -Float4.dot(plane,new Float4(r0,1)) / ndotr;
	}	
	public static float RayPlaneIntersection(Ray r, Float4 plane){
		float ndotr = Float3.dot(plane.xyz(),r.getDirection() );
		if(Math.abs(ndotr) < ZERO_TOLERANCE){ return Float.NaN; }
		return -Float4.dot(plane,new Float4( r.getOrigin(),1 )) / ndotr;
	}
	
	
	///////////////////////////////////////////////////////////////////
	/// \brief Calculate the distance between a segment and point in 2D
	///
	/// Calculates the distance between a segment and a point in 2D
	///
	/// \param v0 Start point of the segment
	/// \param v1 End point of the segment
	/// \param p  A point anywhere in 2D space
	///
	/// \return The distance of the point to the segment
	///////////////////////////////////////////////////////////////////
	public static float PointSegmentDistance(Float2 v0, Float2 v1, Float2 p){
		Float2 v1v0 = Float2.subtract(v1,v0);
		Float2 v0p  = Float2.subtract(v0,p);
		Float2 v1p  = Float2.subtract(v1,p);

		float u = -Float2.dot(v0p,v1v0) / Float2.dot(v1v0,v1v0);

		if(u >= 1){ return v1p.Length(); }
		if(u <= 0){ return v0p.Length(); }

		Float2 p_close = Float2.lerp(v0,v1,u);

		return Float2.subtract(p_close,p).Length();
	}

	public static float PointSegmentDistance(Float3 v0, Float3 v1, Float3 p){
		Float3 v1v0 = Float3.subtract(v1,v0);
		Float3 v0p  = Float3.subtract(v0,p);
		Float3 v1p  = Float3.subtract(v1,p);

		float u = -Float3.dot(v0p,v1v0) / Float3.dot(v1v0,v1v0);

		if(u >= 1){ return v1p.Length(); }
		if(u <= 0){ return v0p.Length(); }

		Float3 p_close = Float3.lerp(v0,v1,u);

		return Float3.subtract(p_close,p).Length();
	}

	public static boolean pointInsideRectangle(float x, float y, float x0, float y0, float w, float h ) {
		if( x < x0 || x > (x0+w) ) return false;
		if( y < y0 || y > (y0+h) ) return false;
		return true;
	}

	
	public static float [] LineLineIntersection(Float2 p0, Float2 pd, Float2 r0, Float2 rd){
		float de = Float2.cross(pd,rd);
		if(Math.abs(de) < ZERO_TOLERANCE){ return null; }
		return new float[] {
				Float2.cross( rd, Float2.subtract(p0,r0) ) / de, 
				Float2.cross( pd, Float2.subtract(r0,p0) ) / de, 
		};
	}
	
	
	/*
	public static Float3 BarycentricCoordinates(Float3 p, Float4 param0, Float4 param1){
		float x = Float4.dot(param0,new Float4(p,1));
		float y = Float4.dot(param1,new Float4(p,1));
		float z = 1 - y - x;
		return new Float3(x,y,z);
	}

	public static Float3 BarycentricCoordinates(Float3 p, Float3 v1, Float3 v2, Float3 v3){
		Float4 [] params = BarycentricParameters(v1,v2,v3);
		return BarycentricCoordinates(p,params[0],params[1]);
	}
	

	public static Float4 [] BarycentricParameters(Float3 v1, Float3 v2, Float3 v3){

		Float3 ADG = Float3.subtract(v3,v1);
		Float3 BEH = Float3.subtract(v2,v3);

		float d0 = Float2.dot( ADG.yz(), BEH.xx() ) - Float2.dot( ADG.xx(), BEH.yz() );
		float d1 = Float2.dot( ADG.xz(), BEH.yy() ) - Float2.dot( ADG.yy(), BEH.xz() );

		Float3 _r0,_r1;

		if(Math.abs(d0) > Math.abs(d1)){
			_r0 = new Float3( BEH.y+BEH.z,-BEH.x,-BEH.x).divide(d0);
			_r1 = new Float3( ADG.y+ADG.z,-ADG.x,-ADG.x).divide(d0);
		}
		else{
			_r0 = new Float3(-BEH.y, BEH.x+BEH.z,-BEH.y).divide(d1);
			_r1 = new Float3(-ADG.y, ADG.x+ADG.z,-ADG.y).divide(d1);
		}

		Float4 [] ret = new Float4[2];
		ret[0] = new Float4(_r0,-Float3.dot(_r0,v3));
		ret[1] = new Float4(_r1,-Float3.dot(_r1,v3));
		return ret;
	}
	*/
	
	
	
	/*
	
	
	///////////////////////////////////////////////////////////////////
	/// \brief Calculate the distance between a line and point in 2D
	///
	/// Calculates the distance between a line (not segment) and a 
	/// point in 2D
	///
	/// \param v0 Any point on the line
	/// \param v1 A different point on the line
	/// \param p  A point anywhere in 2D space
	///
	/// \return The distance of the point to the line
	///////////////////////////////////////////////////////////////////
	public static float PointLineDistance(Float2 v0, Float2 v1, Float2 p){
		Float2 v1v0 = v1-v0;
		Float2 pv0  = p-v0;

		float u = dot(pv0,v1v0) / dot(v1v0,v1v0);
		Float2 p_close = lerp(v0,v1,u);

		return (p_close - p).Length();
	}

	public static float PointLineDistance(Float3 v0, Float3 v1, Float3 p){
		Float3 v1v0 = v1-v0;
		Float3 pv0  = p-v0;

		float u = dot(pv0,v1v0) / dot(v1v0,v1v0);
		Float3 p_close = lerp(v0,v1,u);

		return (p_close - p).Length();
	}


	public static float[] QuadraticEquation(float a, float b, float c){
		float tmp = (b*b) - 4.0f*a*c;
		float [] ret;

		if(fabsf(a) < ZERO_TOLERANCE){ return new float[0]; }

		if(fabsf(tmp) < ZERO_TOLERANCE){
			ret = new float[1];
			ret[0] = -b / (2.0f * a); 
			return ret;
		}

		if(tmp < 0){ return new float[0]; }

		ret = new float[2];
		ret[0] = (-b + sqrtf(tmp)) / (2.0f * a);
		ret[1] = (-b - sqrtf(tmp)) / (2.0f * a);
		
		return 2;
	}




	///////////////////////////////////////////////////////////////////
	/// \brief Calculate the distance between 2 2D points
	///
	/// \param v0 First point in space
	/// \param v1 Second point in space
	///
	/// \return Distance between the 2 points
	///////////////////////////////////////////////////////////////////
	public static float PointPointDistance(Float2 v0, Float2 v1){
		return (v1-v0).Length();
	}

	///////////////////////////////////////////////////////////////////
	/// \brief Calculate the distance between 2 3D points
	///
	/// \param v0 First point in space
	/// \param v1 Second point in space
	///
	/// \return Distance between the 2 points
	///////////////////////////////////////////////////////////////////
	public static float PointPointDistance(Float3 v0, Float3 v1){
		return (v1-v0).Length();
	}

	///////////////////////////////////////////////////////////////////
	/// \brief Calculate the area of the 3D triangle
	///
	/// \param v0 Vertex 0
	/// \param v1 Vertex 1
	/// \param v2 Vertex 2
	///
	/// \return area of the triangle
	///////////////////////////////////////////////////////////////////
	public static float TriangleArea(Float3 v0, Float3 v1, Float3 v2){
		float a = (v1-v0).Length();
		float b = (v2-v1).Length();
		float c = (v0-v2).Length();

		float s = 0.5f * (a + b + c);

		return sqrtf(s*(s-a)*(s-b)*(s-c));
	}

	public static float TriangleArea(Float2 v0, Float2 v1, Float2 v2){
		float a = (v1-v0).Length();
		float b = (v2-v1).Length();
		float c = (v0-v2).Length();

		float s = 0.5f * (a + b + c);

		return sqrtf(s*(s-a)*(s-b)*(s-c));
	}







	public static Float3 MirrorPointAcrossPlane(Float3 p, Float4 pln){
		float d = RayPlaneIntersection(p, -pln.xyz(), pln );

		return p - pln.xyz() * (d * 2.0f);
	}



	public static Float2 LineLineIntersectionPoint(Float2 p0, Float2 pd, Float2 r0, Float2 rd){
		float de = cross(pd,rd);

		if(abs(de) < ZERO_TOLERANCE){ return VEX2_INVALID; }

		return p0 + pd * cross(rd,p0-r0) / de;
	}

	public static float SegmentLineIntersection(Float2 s0, Float2 s1, Float2 l0, Float2 ld){
		float de = cross(s1-s0,ld);

		if(abs(de) < ZERO_TOLERANCE){ return FLT_MAX; }

		float ua = cross(ld,s0-l0) / de;

		if(ua < 0 || ua > 1){ return FLT_MAX; }

		return ua;
	}

	public static float SegmentSegmentIntersection(Float2 p0, Float2 p1, Float2 r0, Float2 r1){
		Float2 r1r0 = r1 - r0;
		Float2 p1p0 = p1 - p0;
		Float2 p0r0 = p0 - r0;

		float de = cross(p1p0,r1r0);

		if(abs(de) < ZERO_TOLERANCE){ return FLT_MAX; }

		float ua = cross(r1r0,p0r0) / de;
		if((ua+ZERO_TOLERANCE) < 0 || (ua-ZERO_TOLERANCE) > 1){ return FLT_MAX; }

		float ub = cross(p1p0,p0r0) / de;
		if((ub+ZERO_TOLERANCE) < 0 || (ub-ZERO_TOLERANCE) > 1){ return FLT_MAX; }

		return Clamp(ua,0.0f,1.0f);
	}

	public static Float2 SegmentSegmentIntersectionPoint(Float2 p0, Float2 p1, Float2 r0, Float2 r1){
		float t = SegmentSegmentIntersection(p0,p1,r0,r1);
		if(t >= 0 && t <= 1.0f){
			return lerp(p0,p1,t);
		}
		return Float2(FLT_MAX,FLT_MAX);
	}

	public static float SegmentSegmentIntersection(Float2 p[], Float2 r[]){
		return SegmentSegmentIntersection(p[0],p[1],r[0],r[1]);
	}

	public static Float2 SegmentSegmentIntersectionPoint(Float2 p[], Float2 r[]){
		float t = SegmentSegmentIntersection(p[0],p[1],r[0],r[1]);
		if(t >= 0 && t <= 1.0f){
			return lerp(p[0],p[1],t);
		}
		return Float2(FLT_MAX,FLT_MAX);
	}

	public static void PlanePlaneIntersection(Float4 p1, Float4 p2, Float3 p, Float3 u){
		u = cross(p1.xyz(),p2.xyz());

		Float3 p1w = p1.xyz() * p2.w;
		Float3 p2w = p2.xyz() * p1.w;
		Float3 p12w = p1w - p2w;

		if(fabsf(u.x) > fabsf(u.y) && fabsf(u.x) > fabsf(u.z)){
			p.x =  0;
			p.y =  p12w.z / u.x;
			p.z = -p12w.y / u.x;
		}
		else if(fabsf(u.y) > fabsf(u.z)){
			p.x = -p12w.z / u.y;
			p.y =  0;
			p.z =  p12w.x / u.y;
		}
		else{
			p.x =  p12w.y / u.z;
			p.y = -p12w.x / u.z;
			p.z =  0;
		}

		u.Normalize();
	}

	public static Float3 PlanePlanePlaneIntersection(Float4 p0, Float4 p1, Float4 p2){
		Float3 p,u;
		PlanePlaneIntersection(p0,p1,p,u);
		return p + u * RayPlaneIntersection(p,u,p2);
	}


	//http://local.wasp.uwa.edu.au/~pbourke/geometry/2circle/
	public static int CircleCircleIntersection(Float2 c0, float r0, Float2 c1, float r1, Float2 p0, Float2 p1){
		float d = (c0-c1).Length();

		// too far away
		if(d > (r0+r1)){ return 0; }
		// one circle contains the other
		if(d < fabsf(r0-r1)){ return 0; }

		// circles touch from outside
		if(d == (r0+r1)){
			p0 = p1 = c0 + (c1 - c0) * r0 / d;
			return 1;
		}
		// circles touch from inside
		if(d == (r0-r1)){
			p0 = p1 = c0 + (c1 - c0) * r0 / d;
			return 1;
		}

		float a = (r0*r0 - r1*r1 + d*d) / (2*d);
		float h = sqrtf(r0*r0 - a*a);

		Float2 c2 = c0 + (c1 - c0) * a / d;
		Float2 t = ( c1 - c0 ) * (h / d);

		p0.x = c2.x + t.y;
		p0.y = c2.y - t.x;

		p1.x = c2.x - t.y;
		p1.y = c2.y + t.x;

		return 2;
	}

	//http://mathforum.org/library/drmath/view/55103.html
	public static int CircleTangentPoints(Float2 c, float r, Float2 p, Float2 p0, Float2 p1){
		float r1 = (p-c).Length()/2.0f;
		Float2 c1 = (p+c)/2.0f;

		return CircleCircleIntersection(c,r,c1,r1,p0,p1);
	}



	//http://local.wasp.uwa.edu.au/~pbourke/geometry/sphereline/
	public static float[] CircleLineIntersection(Float2 cen, float r, Float2 r0, Float2 rd){
		float a = dot( rd, rd );
		float b = 2.0f * dot( rd, r0-cen );
		float c = dot( cen, cen ) + dot( r0, r0 ) - 2.0f * dot( cen, r0 ) - r*r;
		return  QuadraticEquation(a,b,c);
	}

	public static float [] CircleLineIntersection(float r, Float2 r0, Float2 rd){
		float a = dot( rd, rd );
		float b = 2.0f * dot( rd, r0 );
		float c = dot( r0, r0 ) - r*r;
		return  QuadraticEquation(a,b,c);
	}

	public static int CircleLineIntersection(float r, Float2 r0, Float2 rd, Float2 ret0, Float2 ret1){
		float t0 = 0, t1 = 0;
		int intersections = CircleLineIntersection(r,r0,rd,t0,t1);

		if(intersections >= 1){ ret0 = r0 + rd * t0; }
		if(intersections >= 2){ ret1 = r0 + rd * t1; }

		return intersections;
	}

	public static float [] CircleSegmentIntersection(Float2 cen, float r, Float2 r0, Float2 r1){
		float tt[2] = {FLT_MAX, FLT_MAX};
		int intersections = CircleLineIntersection(cen,r,r0,r1-r0,t0,t1);
		int ret = 0;

		if(intersections >= 1 && t0 >= 0 && t0 <= 1.0f){ tt[ret++] = t0; }
		if(intersections >= 2 && t1 >= 0 && t1 <= 1.0f){ tt[ret++] = t1; }

		return tt;
	}

	public static int CircleSegmentIntersection(Float2 cen, float r, Float2 r0, Float2 r1, Float2 ret0, Float2 ret1){
		float t0,t1;
		int intersections = CircleSegmentIntersection(cen,r,r0,r1,t0,t1);

		if(intersections >= 1){ ret0 = lerp(r0,r1,t0); }
		if(intersections >= 2){ ret1 = lerp(r0,r1,t1); }

		return intersections;
	}


	public static float [] CircleSegmentIntersection(float r, Float2 r0, Float2 r1){
		float tt[2] = {FLT_MAX, FLT_MAX};
		int intersections = CircleLineIntersection(r,r0,r1-r0,t0,t1);
		int ret = 0;

		if(intersections >= 1 && t0 >= 0 && t0 <= 1.0f){ tt[ret++] = t0; }
		if(intersections >= 2 && t1 >= 0 && t1 <= 1.0f){ tt[ret++] = t1; }

		return tt;
	}

	public static int CircleSegmentIntersection(float r, Float2 r0, Float2 r1, Float2 ret0, Float2 ret1){
		float t0,t1;
		int intersections = CircleSegmentIntersection(r,r0,r1,t0,t1);

		if(intersections >= 1){ ret0 = lerp(r0,r1,t0); }
		if(intersections >= 2){ ret1 = lerp(r0,r1,t1); }

		return intersections;
	}






	public static Float3 TriangleSegmentIntersection(Float3 r0, Float3 r1, Float4 plane, Float3 p0, Float3 p1, Float3 p2){
		float t = RayPlaneIntersection(r0,r1-r0,plane);
		if(t < 0 || t > 1){
			return Float3(-1,-1,-1);
		}
		Float3 p = r0 + (r1-r0)*t;
		return BarycentricCoordinates(p,p0,p1,p2);
	}

	public static boolean SegmentSphereIntersection(Float3 r0, Float3 r1, Float3 cen, float rad){
		Float3  rd  = r1 - r0;
		Float3  r0c = r0 - cen;
		Float3  r1c = r1 - cen;

		if(r0c.Length() < rad) return true;
		if(r1c.Length() < rad) return true;

		float A = dot(rd,rd);
		float B = dot(rd,r0c) * 2;
		float C = dot(r0c,r0c) - (rad * rad);

		float discriminant = B*B - 4*A*C;

		if(discriminant >= 0){
			float t0 = (-B + sqrtf(discriminant)) / (2*A);
			float t1 = (-B - sqrtf(discriminant)) / (2*A);
			if((t0 >= 0 && t0 <= 1.0f) || (t1 >= 0 && t1 <= 1.0f)) return true;
		}

		return false;
	}




	//#include <mylib/vector.h>

	public static int PolygonCollapse(Vector<Float3> pIn, Vector<Float3> pOut){

		int size = (int)pIn.size();
		pOut.clear();
		int ret = 0;

		for(int i = 0; i < size; i++){
			int pa = (i-1+size)%size;
			int pb = (i+1)%size;

			Float3 d0 = (pIn[i] -pIn[pa]).UnitVector();
			Float3 d1 = (pIn[pb]-pIn[pa]).UnitVector();

			if(dot(d0,d1)<=0.9999f){
				pOut.push_back( pIn[i] );
			}
			else{
				ret++;
			}
		}
		return ret;
	}

	public static void PolygonCollapse(Vector<Float3> pInOut){
		int loop = 0;
		Vector<Float3> ptmp;
		do {
			loop = PolygonCollapse(pInOut,ptmp);
			pInOut = ptmp;
		} while(loop > 0);
	}

	public static void PolygonClip(Vector<Float3> pIn, Vector<Float3> pOut, Float4 plane){
		Vector<float> side;

		for(int i = 0; i < pIn.size(); i++){
			side.push_back( dot(Float4(pIn[i],1),plane) );
		}

		pOut.clear();
		for(int i = 0; i < pIn.size(); i++){
			int j = (i+1)%pIn.size();
			if(side[i] >= 0){ pOut.push_back( pIn[i] ); }
			if(side[i] >= 0 && side[j] <  0){ pOut.push_back( lerp(pIn[i],pIn[j],(0-side[i])/(side[j]-side[i])) ); }
			if(side[i] <  0 && side[j] >= 0){ pOut.push_back( lerp(pIn[i],pIn[j],(0-side[i])/(side[j]-side[i])) ); }
		}
	}

	public static void PolygonClip(Vector<Float3> pInOut, Float4 plane){
		Vector<Float3> ptmp;
		PolygonClip(pInOut,ptmp,plane);
		pInOut = ptmp;
	}


	public static void ClipFrustumWithPlane(Vector<Float3> pIn, Vector<Float3> pOut, Float3 cop, Float4 plane){
		float side[64];
		for(int i = 0; i < (int)pIn.size(); i++){
			side[i] = dot(Float4(pIn[i],1),plane);
		}

		pOut.clear();
		for(int i = 0; i < pIn.size(); i++){
			int j = (i+1)%pIn.size();
			if(side[i] >= 0){ pOut.push_back( pIn[i] ); }
			if(side[i] <  0){
				float t = RayPlaneIntersection(cop,(pIn[i]-cop),plane);
				t = (t<0||t>10)?10:t;
				pOut.push_back( cop + (pIn[i]-cop) * t );
			}
			if(side[i] >= 0 && side[j] <  0){ pOut.push_back( lerp(pIn[i],pIn[j],(0-side[i])/(side[j]-side[i])) ); }
			if(side[i] <  0 && side[j] >= 0){ pOut.push_back( lerp(pIn[i],pIn[j],(0-side[i])/(side[j]-side[i])) ); }
		}
	}


	public static void ClipFrustumWithPlane(Vector<Float3> pInOut, Float3 cop, Float4 plane){
		Vector<Float3> ptmp;

		ClipFrustumWithPlane(pInOut,ptmp,cop,plane);

		pInOut = ptmp;
	}

	public static int PointsOnPlaneCount(Vector<Float3> pIn, Float4 plane, float eps){
		int ret = 0;
		for(int i = 0; i < pIn.size(); i++){
			float side = dot(Float4(pIn[i],1),plane);
			if(fabsf(side) <= eps){
				ret++;
			}
		}
		return ret;
	}

	public static void PointsOnPlane(Vector<Float3> pIn, Vector<Float3> pOut, Float4 plane, float eps){
		pOut.clear();
		for(int i = 0; i < pIn.size(); i++){
			float side = dot(Float4(pIn[i],1),plane);
			if(fabsf(side) <= eps){
				pOut.push_back(pIn[i]);
			}
		}
	}

	public static void PointsOnPlane(Vector<Float3> pInOut, Float4 plane, float eps){
		Vector<Float3> ptmp;
		PointsOnPlane(pInOut,ptmp,plane,eps);
		pInOut = ptmp;
	}


	//
	//public static void PolygonClip(Float3* pIn, int pInN, Float3* pOut, int & pOutN, Float4 plane){
	//float side[64];
	//for(int i = 0; i < pInN; i++){
	//side[i] = dot(Float4(pIn[i],1),plane);
	//}
	//
	//pOutN = 0;
	//for(int i = 0; i < pInN; i++){
	//int j = (i+1)%pInN;
	//if(side[i] >= 0){ pOut[pOutN++] = pIn[i]; }
	//if(side[i] >= 0 && side[j] <  0){ pOut[pOutN++] = lerp(pIn[i],pIn[j],(0-side[i])/(side[j]-side[i])); }
	//if(side[i] <  0 && side[j] >= 0){ pOut[pOutN++] = lerp(pIn[i],pIn[j],(0-side[i])/(side[j]-side[i])); }
	//}
	//}
	//
	//public static void PolygonClip(Float3* p, int & pN, Float4 plane){
	//Float3 ptmp[64];
	//int ptmpN = 0;
	//PolygonClip(p,pN,ptmp,ptmpN,plane);
	//
	//COPY_ARRAY(ptmp,ptmpN,p);
	//pN = ptmpN;
	//}


	//public static void ClipFrustumWithPlane(Float3 * pIn, int pInN, Float3 * pOut, int & pOutN, Float3 cop, Float4 plane){
	//float side[64];
	//for(int i = 0; i < pInN; i++){
	//side[i] = dot(Float4(pIn[i],1),plane);
	//}
	//
	//pOutN = 0;
	//for(int i = 0; i < pInN; i++){
	//int j = (i+1)%pInN;
	//if(side[i] >= 0){ pOut[pOutN++] = pIn[i]; }
	//if(side[i] <  0){
	//float t = RayPlaneIntersection(cop,(pIn[i]-cop),plane);
	//t = (t<0||t>10)?10:t;
	//pOut[pOutN++] = cop + (pIn[i]-cop) * t;
	//}
	//if(side[i] >= 0 && side[j] <  0){ pOut[pOutN++] = lerp(pIn[i],pIn[j],(0-side[i])/(side[j]-side[i])); }
	//if(side[i] <  0 && side[j] >= 0){ pOut[pOutN++] = lerp(pIn[i],pIn[j],(0-side[i])/(side[j]-side[i])); }
	//}
	//}

	//public static void ClipFrustumWithPlane(Float3 * pInOut, int & pInOutN, Float3 cop, Float4 plane){
	//Float3 ptmp[64];
	//int ptmpN;
	//
	//ClipFrustumWithPlane(pInOut,pInOutN,ptmp,ptmpN,cop,plane);
	//
	//pInOutN = ptmpN;
	//COPY_ARRAY(ptmp,ptmpN,pInOut);
	//}

	//public static void PointsOnPlane(Float3 * pIn, int pInN, Float3 * pOut, int & pOutN, Float4 plane, float eps){
	//pOutN = 0;
	//for(int i = 0; i < pInN; i++){
	//float side = dot(Float4(pIn[i],1),plane);
	//if(fabsf(side) <= eps){
	//pOut[pOutN++] = pIn[i];
	//}
	//}
	//}
	//
	//public static void PointsOnPlane(Float3 * pInOut, int & pInOutN, Float4 plane, float eps){
	//Float3 ptmp[64];
	//int ptmpN;
	//
	//PointsOnPlane(pInOut,pInOutN,ptmp,ptmpN,plane,eps);
	//
	//pInOutN = ptmpN;
	//COPY_ARRAY(ptmp,ptmpN,pInOut);
	//}

	public static Float3 ClosestPointBetweenLines(Float3 P0, Float3 u, Float3 Q0, Float3 v){
		Float3 w0 = P0 - Q0;

		float a = dot(u,u);
		float b = dot(u,v);
		float c = dot(v,v);
		float d = dot(u,w0);
		float e = dot(v,w0);

		float s = (b*e - c*d) / (a*c - b*b);
		float t = (a*e - b*d) / (a*c - b*b);

		Float3 p0 = P0 + u*s;
		Float3 p1 = Q0 + v*t;

		return (p0+p1)/2.0f;
	}



	public static void ClipLine(Float3 p0, Float3 p1, Float4 pln){
		float d0 = dot(Float4(p0,1),pln);
		float d1 = dot(Float4(p1,1),pln);
		if(d0 >= 0 && d1 >= 0){
			return;
		}
		if(d0 <= 0 && d1 <= 0){
			p0 = p1 = VEX3_INVALID;
			return;
		}
		if(d0 <= 0 && d1 >= 0){
			p0 = p0 + (p1-p0) * RayPlaneIntersection(p0,p1-p0,pln);
			return;
		}
		if(d0 >= 0 && d1 <= 0){
			p1 = p0 + (p1-p0) * RayPlaneIntersection(p0,p1-p0,pln);
			return;
		}
	}





	public static int __RayConeIntersection(Float3 r0, Float3 rd, Float3 c_origin, Float3 c_axis, float cosAngle, float t0, float t1){
		float fCosSqr = cosAngle*cosAngle;
		Float3 kE = r0 - c_origin;

		float fAdD = dot(c_axis,rd.UnitVector());
		float fEdA = dot(kE,c_axis);
		float fEdD = dot(kE,rd.UnitVector());
		float fEdE = dot(kE,kE);

		float A = 0.5f*(fAdD*fAdD - fCosSqr) * rd.Length();
		float B = 1.0f*(fAdD*fEdA - fCosSqr*fEdD);
		float C = 0.5f*(fEdA*fEdA - fCosSqr*fEdE) / rd.Length();

		int intersections = 0;

		if (fabsf(A) >= ZERO_TOLERANCE)
		{
			intersections = QuadraticEquation(A,B,C,t0,t1);
		}
		else if (fabsf(B) >= ZERO_TOLERANCE)
		{
			t0 = (C/B);
			intersections = 1;
		}

		float ret[2] = { FLT_MAX, FLT_MAX };
		int ret_cnt = 0;

		//if(intersections >= 1){
		//    Float3 pnt = r0 + rd * t0;
		//    float diff = dot((pnt-c_origin).UnitVector(),c_axis)-cosAngle;
		//    if(fabsf(diff) < ZERO_TOLERANCE){ ret[ret_cnt++] = t0; }
		//}
		//if(intersections >= 2){
		//    Float3 pnt = r0 + rd * t1;
		//    float diff = dot((pnt-c_origin).UnitVector(),c_axis)-cosAngle;
		//    if(fabsf(diff) < ZERO_TOLERANCE){ ret[ret_cnt++] = t1; }
		//}

		if(intersections >= 1){
			Float3 pnt = r0 + rd * t0;
			float dir = dot(pnt-c_origin,c_axis)*cosAngle;
			if(dir >= 0){ ret[ret_cnt++] = t0; }
		}
		if(intersections >= 2){
			Float3 pnt = r0 + rd * t1;
			float dir = dot(pnt-c_origin,c_axis)*cosAngle;
			if(dir >= 0){ ret[ret_cnt++] = t1; }
		}


		t0 = ret[0];
		t1 = ret[1];

		return ret_cnt;
	}

	public static int RayConeIntersection(Float3 r0, Float3 rd, Float3 c_origin, Float3 c_axis, float c_angle, float t0, float t1){
		float cosAngle = cosf(c_angle);

		return __RayConeIntersection(r0,rd,c_origin,c_axis,cosAngle,t0,t1);
	}

	public static int RayConeIntersection(Float3 r0, Float3 rd, Float3 c_origin, Float3 c_axis, Float3 c_pnt, float t0, float t1){

		float cosAngle = dot((c_pnt-c_origin).UnitVector(),c_axis.UnitVector());

		return __RayConeIntersection(r0,rd,c_origin,c_axis,cosAngle,t0,t1);
	}





	//
	//
	//public static int __DoubleConeLineIntersection(Float3 c_origin, Float3 c_axis, float cosAngle, Float3 r0, Float3 rd, float t0, float t1){
	//float fCosSqr = cosAngle*cosAngle;
	//Float3 kE = r0 - c_origin;
	//
	//float fAdD = dot(c_axis,rd.UnitVector());
	//float fEdA = dot(kE,c_axis);
	//float fEdD = dot(kE,rd.UnitVector());
	//float fEdE = dot(kE,kE);
	//
	//float A = 0.5f*(fAdD*fAdD - fCosSqr) * rd.Length();
	//float B = 1.0f*(fAdD*fEdA - fCosSqr*fEdD);
	//float C = 0.5f*(fEdA*fEdA - fCosSqr*fEdE) / rd.Length();
	//
	//if (fabsf(A) >= ZERO_TOLERANCE)
	//{
	//return QuadraticEquation(A,B,C,t0,t1);
	//}
	//else if (fabsf(B) >= ZERO_TOLERANCE)
	//{
	//t0 = (C/B);
	//return 1;
	//}
	//return 0;
	//}
	//
	//public static int __ConeLineIntersection(Float3 c_origin, Float3 c_axis, float cosAngle, Float3 r0, Float3 rd, float t0, float t1){
	//
	//int intersections = __DoubleConeLineIntersection(c_origin, c_axis, cosAngle, r0, rd, t0, t1);
	//
	//float ret[2] = { FLT_MAX, FLT_MAX };
	//int ret_cnt = 0;
	//
	//if(intersections >= 1){
	//Float3 pnt = r0 + rd * t0;
	//float diff = dot((pnt-c_origin).UnitVector(),c_axis)-cosAngle;
	//if(fabsf(diff) < ZERO_TOLERANCE){ ret[ret_cnt++] = t0; }
	//}
	//if(intersections >= 2){
	//Float3 pnt = r0 + rd * t1;
	//float diff = dot((pnt-c_origin).UnitVector(),c_axis)-cosAngle;
	//if(fabsf(diff) < ZERO_TOLERANCE){ ret[ret_cnt++] = t1; }
	//}
	//
	//t0 = ret[0];
	//t1 = ret[1];
	//
	//return ret_cnt;
	//}
	//
	//public static int __ConeSegmentIntersection(Float3 c_origin, Float3 c_axis, float cosAngle, Float3 r0, Float3 r1, float t0, float t1){
	//
	//int intersections = __ConeLineIntersection(c_origin, c_axis, cosAngle, r0, r1-r0, t0, t1);
	//
	//float ret[2] = { FLT_MAX, FLT_MAX };
	//int ret_cnt = 0;
	//
	//if(intersections >= 1 && t0 >= 0 && t0 <= 1.0f){ ret[ret_cnt++] = t0; }
	//if(intersections >= 2 && t1 >= 0 && t1 <= 1.0f){ ret[ret_cnt++] = t1; }
	//
	//t0 = ret[0];
	//t1 = ret[1];
	//
	//return ret_cnt;
	//}
	//
	//public static int RayConeIntersection(Float3 r0, Float3 rd, Float3 c_origin, Float3 c_axis, float c_angle, float t0, float t1){
	//float cosAngle = cosf(c_angle);
	//
	////return __RayConeIntersection(r0,rd,c_origin,c_axis,cosAngle,t0,t1);
	//return __ConeSegmentIntersection(c_origin,c_axis,cosAngle,r0,r0+rd,t0,t1);
	//}
	//
	//public static int RayConeIntersection(Float3 r0, Float3 rd, Float3 c_origin, Float3 c_axis, Float3 c_pnt, float t0, float t1){
	//
	//float cosAngle = dot((c_pnt-c_origin).UnitVector(),c_axis.UnitVector());
	//
	////return __RayConeIntersection(r0,rd,c_origin,c_axis,cosAngle,t0,t1);
	//return __ConeSegmentIntersection(c_origin,c_axis,cosAngle,r0,r0+rd,t0,t1);
	//}


	public static Float2 CalculateConic(Float2 p0, Float2 p1){
		Float2 p0_2 = p0 * p0;
		Float2 p1_2 = p1 * p1;
		float A, B;

		if(fabsf(p0.x-p1.x) > fabsf(p0.y-p1.y)){
			B = cross(p1_2,p0_2) / ( p1_2.x - p0_2.x );
			A = ( B * p0_2.x ) / ( B - p0_2.y );
		}
		else{
			A = cross(p0_2,p1_2) / ( p1_2.y - p0_2.y );
			B = ( A * p0_2.y ) / ( A - p0_2.x );
		}

		if(fabsf(p0.y-p1.y) < 0.00001f){    A = FLT_MAX;     }
		if(fabsf(p0.x-p1.x) < 0.00001f){    B = FLT_MAX;    }

		return Float2(A,B);
	}



	//public static int ConicLineIntersection(Float2 AB, Float2 r0, Float2 rd, float t0, float t1){
	//
	//Float2 R0 = r0*r0;
	//Float2 RD = rd*rd;
	//Float2 R0RD = r0*rd;
	//
	//float _a = (AB.y*RD.x+AB.x*RD.y) / (AB.x*AB.y);
	//float _b = ((2.0f * AB.y * r0.x * rd.x) + (2.0f * AB.x * r0.y * rd.y)) / (AB.x*AB.y);
	//float _c = (AB.y * R0.x + AB.x * R0.y - AB.x*AB.y) / (AB.x*AB.y);
	//
	//return QuadraticEquation(_a,_b,_c,t0,t1);
	//}


	public static boolean PointInCone(Float3 center, Float3 _up, Float3 _edge, Float3 _pnt){

		Float3 up = _up.UnitVector();
		Float3 edge = _edge - center;
		Float3 pnt = _pnt - center;

		float A = dot(up,edge)*dot(up,edge)/dot(edge,edge);


		return A <= dot(up,pnt)*dot(up,pnt)/dot(pnt,pnt);
	}

	template <class C> C LinearBezier( float t, C & p0, C & p1 ){
		return p0 * (1-t) + p1 * (t);
	}

	template <class C> C QuadraticBezier(float t, C & p0, C & p1, C & p2){
		return p0 * (1-t) * (1-t) + p1 * (1-t) * (t) * 2 + p2 * (t) * (t);
	}

	template <class C> C CubicBezier(float t, C & p0, C & p1, C & p2, C & p3){
		float t0 = t;
		float t1 = 1-t;
		C ret;
		ret += p0 * t1 * t1 * t1;
		ret += p1 * t1 * t1 * t0 * 3.0f;
		ret += p2 * t1 * t0 * t0 * 3.0f;
		ret += p3 * t0 * t0 * t0;
		return ret;
	}


	public static float LinearBezier(float t, float p0, float p1){    return LinearBezier<float>(t,p0,p1); }
	public static Float2  LinearBezier(float t, Float2  p0, Float2  p1){    return LinearBezier<Float2>(t,p0,p1);  }
	public static Float3  LinearBezier(float t, Float3  p0, Float3  p1){    return LinearBezier<Float3>(t,p0,p1);  }
	public static Float4  LinearBezier(float t, Float4  p0, Float4  p1){    return LinearBezier<Float4>(t,p0,p1);  }


	public static float QuadraticBezier(float t, float p0, float p1, float p2){ return QuadraticBezier<float>(t,p0,p1,p2); }
	public static Float2  QuadraticBezier(float t, Float2  p0, Float2  p1, Float2  p2){ return QuadraticBezier<Float2>(t,p0,p1,p2);  }
	public static Float3  QuadraticBezier(float t, Float3  p0, Float3  p1, Float3  p2){ return QuadraticBezier<Float3>(t,p0,p1,p2);  }
	public static Float4  QuadraticBezier(float t, Float4  p0, Float4  p1, Float4  p2){ return QuadraticBezier<Float4>(t,p0,p1,p2);  }

	public static float CubicBezier(float t, float p0, float p1, float p2, float p3){ return CubicBezier<float>(t,p0,p1,p2,p3); }
	public static Float2  CubicBezier(float t, Float2  p0, Float2  p1, Float2  p2, Float2  p3){ return CubicBezier<Float2>(t,p0,p1,p2,p3);  }
	public static Float3  CubicBezier(float t, Float3  p0, Float3  p1, Float3  p2, Float3  p3){ return CubicBezier<Float3>(t,p0,p1,p2,p3);  }
	public static Float4  CubicBezier(float t, Float4  p0, Float4  p1, Float4  p2, Float4  p3){ return CubicBezier<Float4>(t,p0,p1,p2,p3);  }

	//public static Float3 CubicBezier(float t, Float3 p0, Float3 p1, Float3 p2, Float3 p3){
	//float t0 = t;
	//float t1 = 1-t;
	//Float3 ret;
	//ret += p0 * t1 * t1 * t1;
	//ret += p1 * t1 * t1 * t0 * 3.0f;
	//ret += p2 * t1 * t0 * t0 * 3.0f;
	//ret += p3 * t0 * t0 * t0;
	//return ret;
	//}




	public static float RayBoxIntersection(Float3 r0, Float3 rd, Float3 box_center, float box_size){

		Float3 min_p = box_center - Float3(box_size,box_size,box_size) / 2.0f;
		Float3 max_p = box_center + Float3(box_size,box_size,box_size) / 2.0f;

		float t = FLT_MAX;

		float tt[6];

		tt[0] = RayPlaneIntersection(r0,rd,Float4(1,0,0,-min_p.x));
		tt[1] = RayPlaneIntersection(r0,rd,Float4(1,0,0,-max_p.x));
		tt[2] = RayPlaneIntersection(r0,rd,Float4(0,1,0,-min_p.y));
		tt[3] = RayPlaneIntersection(r0,rd,Float4(0,1,0,-max_p.y));
		tt[4] = RayPlaneIntersection(r0,rd,Float4(0,0,1,-min_p.z));
		tt[5] = RayPlaneIntersection(r0,rd,Float4(0,0,1,-max_p.z));

		if(tt[0] > 0){
			Float3 curp = r0 + rd * tt[0];
			if(curp.y >= min_p.y && curp.y <= max_p.y && curp.z >= min_p.z && curp.z <= max_p.z){
				t = Min(t,tt[0]);
			}
		}
		if(tt[1] > 0){
			Float3 curp = r0 + rd * tt[1];
			if(curp.y >= min_p.y && curp.y <= max_p.y && curp.z >= min_p.z && curp.z <= max_p.z){
				t = Min(t,tt[1]);
			}
		}

		if(tt[2] > 0){
			Float3 curp = r0 + rd * tt[2];
			if(curp.x >= min_p.x && curp.x <= max_p.x && curp.z >= min_p.z && curp.z <= max_p.z){
				t = Min(t,tt[2]);
			}
		}
		if(tt[3] > 0){
			Float3 curp = r0 + rd * tt[3];
			if(curp.x >= min_p.x && curp.x <= max_p.x && curp.z >= min_p.z && curp.z <= max_p.z){
				t = Min(t,tt[3]);
			}
		}

		if(tt[4] > 0){
			Float3 curp = r0 + rd * tt[4];
			if(curp.x >= min_p.x && curp.x <= max_p.x && curp.y >= min_p.y && curp.y <= max_p.y){
				t = Min(t,tt[4]);
			}
		}
		if(tt[5] > 0){
			Float3 curp = r0 + rd * tt[5];
			if(curp.x >= min_p.x && curp.x <= max_p.x && curp.y >= min_p.y && curp.y <= max_p.y){
				t = Min(t,tt[5]);
			}
		}

		return t;
	}


	public static Float3 RayQuadIntersection(Float3 r0, Float3 rd, Float3 q[4]){
		Float4 pln = CalculatePlane(q[0],q[1],q[2]);
		float t = RayPlaneIntersection(r0,rd,pln);
		if(t == FLT_MAX){ return VEX3_INVALID; }
		if(t < 0 || t > 1){ return VEX3_INVALID; }
		Float3 pnt = r0 + rd * t;

		Float3 n0 = -cross((pnt-q[0]).UnitVector(),(q[1]-q[0]).UnitVector());
		Float3 n1 = -cross((pnt-q[1]).UnitVector(),(q[2]-q[1]).UnitVector());
		Float3 n2 = -cross((pnt-q[2]).UnitVector(),(q[3]-q[2]).UnitVector());
		Float3 n3 = -cross((pnt-q[3]).UnitVector(),(q[0]-q[3]).UnitVector());

		Float4 f;

		f.x = dot(pln.xyz(),n0);
		f.y = dot(pln.xyz(),n1);
		f.z = dot(pln.xyz(),n2);
		f.w = dot(pln.xyz(),n3);

		if(f.x < 0 || f.y < 0 || f.z < 0 || f.w < 0){
			return VEX3_INVALID;
		}

		return pnt;
	}


	public static float LineBezierIntersection(Float3 l0, Float3 ld, Float3 p0, Float3 p1, Float3 p2){
		float a = dot( (p0 + p2 - p1*2), ld );
		float b = dot( (p1 - p0), ld ) * 2;
		float c = dot( (p0 - l0), ld );

		float t0,t1;
		int sols = QuadraticEquation( a, b, c, t0, t1 );

		float ret = 0.5f;

		ret = (sols > 1 && t1 >= 0 && t1 <= 1) ? t1 : ret;
		ret = (sols > 0 && t0 >= 0 && t0 <= 1) ? t0 : ret;

		return ret;
	}
	*/
}
