package AI.MultiAgentPathFinding;

import org.joml.Vector3i;

public class Agent {

	public Agent(int id, Vector3i pos, int clId){
		ID = id;
		clusterID = clId;
		position = pos;
	}

	int ID;
	int clusterID = 0;
	Vector3i position;
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getClusterID() {
		return clusterID;
	}
	public void setClusterID(int clusterID) {
		this.clusterID = clusterID;
	}
	public Vector3i getPosition() {
		return position;
	}
	public void setPosition(Vector3i position) {
		this.position = position;
	}
}
