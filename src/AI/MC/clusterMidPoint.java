package AI.MC;


import org.joml.Vector3i;

public class clusterMidPoint {

	
	public clusterMidPoint(Vector3i m, int i){
	
		mid = m;
		index = i;
		
	}
	
	Vector3i mid = new Vector3i(0,0,0);
	int index;
	public Vector3i getMid() {
		return mid;
	}
	public void setMid(Vector3i mid) {
		this.mid = mid;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

}
