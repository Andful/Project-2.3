package AI.MC;

import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class startAndEnd {

	public startAndEnd(List<Vector3i> s, List<Vector3i> e){
		
		start = s;
		end = e;
		
	}
	
	List<Vector3i> start = new ArrayList<Vector3i>();
	List<Vector3i> end = new ArrayList<Vector3i>();
	
	public List<Vector3i> getStart() {
		return start;
	}
	public void setStart(List<Vector3i> start) {
		this.start = start;
	}
	public List<Vector3i> getEnd() {
		return end;
	}
	public void setEnd(List<Vector3i> end) {
		this.end = end;
	}
	
	public String toString(){
		
		return "[ start="+ start.toString()+" end ="+end.toString()+"]";
	}
	
	
}
