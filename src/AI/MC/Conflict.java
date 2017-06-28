package AI.MC;

import java.util.List;

public class Conflict {

	public Conflict(List<Agent> firstGroup, List<Agent> secondGroup, int ts /* vector3i xyz */){
		i = firstGroup;
		j = secondGroup;
		timeStep = ts;
		// Vector3F xyz;
	}
	
	List<Agent> i;
	List<Agent> j;
	int timeStep;
	// vector3F location;
	public List<Agent> getI() {
		return i;
	}
	public void setI(List<Agent> i) {
		this.i = i;
	}
	public List<Agent> getJ() {
		return j;
	}
	public void setJ(List<Agent> j) {
		this.j = j;
	}
	public int getTimeStep() {
		return timeStep;
	}
	public void setTimeStep(int timeStep) {
		this.timeStep = timeStep;
	}
	
	/* public Vector3F getLocation() {
		return location;
	}
	public void setLocation(Vector3F location) {
		this.location = location;
	}*/
}
