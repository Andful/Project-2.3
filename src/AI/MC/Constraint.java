package AI.MC;

import java.util.List;

public class Constraint {
 
	public Constraint(List<Agent> b, int ts /* Vector3f location */){
		a = b;
		timeStep = ts;
		// Vector3F xyz;
	}
	
	List<Agent> a;
	int timeStep;
	// vector3F location;
	
	public List<Agent> getA() {
		return a;
	}
	public void setA(List<Agent> a) {
		this.a = a;
	}
	
	public int getTimeStep() {
		return timeStep;
	}
	public void setTimeStep(int timeStep) {
		this.timeStep = timeStep;
	}
	
	
}
