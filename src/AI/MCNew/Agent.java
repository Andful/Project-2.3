package AI.MCNew;

import org.joml.Vector3i;
public class Agent extends Block {

	public Agent(int id, Vector3i pos, int clId){
		this.id = id;
		clusterID = clId;
		this.pos = pos;
	}

	public boolean isAgent()
	{
		return true;
	}

	int id;
	int clusterID = 0;
	Vector3i pos;
	
	public int getID() {
		return id;
	}
	public void setID(int iD) {
		id = iD;
	}
	public int getClusterID() {
		return clusterID;
	}
	public void setClusterID(int clusterID) {
		this.clusterID = clusterID;
	}
	public Vector3i getPosition() {
		return pos;
	}
	public void setPosition(Vector3i position) {
		this.pos = position;
	}
}
