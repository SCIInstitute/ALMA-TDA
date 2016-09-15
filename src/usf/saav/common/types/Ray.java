package usf.saav.common.types;


public class Ray {

	Float3 pos;
	Float3 dir;
	
	public Ray( Float3 pos, Float3 dir ){
		this.pos = pos.clone();
		this.dir = dir.clone();
	}

	public Ray( Float3 pos, Double3 dir ){
		this.pos = pos.clone();
		this.dir = dir.toFloat3();
	}

	public Ray( float x, float y, float z, float dx, float dy, float dz ){
		this.pos = new Float3(x,y,z);
		this.dir = new Float3(dx,dy,dz);
	}

	public Float3 getPosition(float t) {
		return pos.add( dir.multiply( t ) );
	}

	public Float3 getDirection() {
		return dir;
	}
	
	public float getMagnitude() {
		return dir.Magnitude();
	}
	
	public Float3 getOrigin(){
		return pos;
	}
	
}

