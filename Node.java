import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

public class Node {
	
	private List<Vector3f> agents;
	private List<List<Vector3f>> actions;
	private double ucb1; //value + sqrt(lnN/n) where N= total visits and n = visits of this node
	private double value; // 1 divided by sum of manhattan heuristic of each agent
	private double visits;
	private static final double TOL = 0.001;
	private List<Node> children;
	private Node parent;
	
	public Node(List<Vector3f> agents){
		this.agents = agents;
		this.actions = getAvailableActions();
	}
	
	//returns ucb1 and also sets the value for the node
	public double getUCB1(int totalVisits){
		
		if(this.visits == 0){
			this.ucb1 = Double.MAX_VALUE;
			return ucb1;
		} else {
			double dummy = value + Math.sqrt((Math.log(totalVisits))/visits);
			this.ucb1 = dummy;
			return ucb1;
		}
	}
	
	public void addChild(Node child){
		children.add(child);
		child.setParent(this);
	}
	
	//PRIVATE METHODS
	private List<List<Vector3f>> getAvailableActions(){
		
		List<List<Vector3f>> possibleActions = new ArrayList<>();
		
		for(int i=0; i<agents.size(); i++){
			List<Vector3f> actions = new ArrayList<>();
			
			if(checkTop(agents.get(i))){
				possibleActions.add(actions);
				continue;
			} else {
				
				if(checkBottom(agents.get(i))){					
					
					if(checkFront(agents.get(i))){
						
						if(checkTopFront(agents.get(i))){
							//does not move
						} else {
							Vector3f action = new Vector3f(0,1,1);
							actions.add(action);
						}
					} else {
						
						if(checkBottomFront(agents.get(i))){
							Vector3f action = new Vector3f(0,0,1);
							actions.add(action);
						} else {
							//does not move
						}
					}
					
					
					if(checkRear(agents.get(i))){
						
						if(checkTopRear(agents.get(i))){
							
						} else {
							Vector3f action = new Vector3f(0,1,-1);
							actions.add(action);
						}						
					} else {
						
						if(checkBottomRear(agents.get(i))){
							Vector3f action = new Vector3f(0,0,-1);
							actions.add(action);
						} else {
							//does not move
						}
					}
					
					if(checkLeft(agents.get(i))){
						
						if(checkTopLeft(agents.get(i))){
							
						} else {
							Vector3f action = new Vector3f(-1,1,0);
							actions.add(action);
						}
					} else {
						
						if(checkBottomLeft(agents.get(i))){
							Vector3f action = new Vector3f(-1,0,0);
							actions.add(action);
						} else {
							//does not move
						}
					}
					
					
					if(checkRight(agents.get(i))){
						
						if(checkTopRight(agents.get(i))){
							//does not move
						} else {
							Vector3f action = new Vector3f(1,1,0);
							actions.add(action);
						}						
						
					} else {
						
						if(checkBottomRight(agents.get(i))){
							Vector3f action = new Vector3f(1,-1,0);
							actions.add(action);
						} else {
							//does not move
						}
					}
					
				} else {
					
					if(checkFront(agents.get(i))){
						
						if(checkTopFront(agents.get(i))){
							//can't move
						} else {
							Vector3f action = new Vector3f(0,1,1);
							actions.add(action);
						}
					} else {
						Vector3f action = new Vector3f(0,0,1);
						actions.add(action);
					}
					
					if(checkRear(agents.get(i))){
						
						if(checkTopRear(agents.get(i))){
							//can't move
						} else {
							Vector3f action = new Vector3f(0,0,-1);
							actions.add(action);
						}
					} else {
						Vector3f action = new Vector3f(0,0,-1);
						actions.add(action);
					}
					
					if(checkLeft(agents.get(i))){
						
						if(checkTopLeft(agents.get(i))){
							//can't move
						} else {
							Vector3f action = new Vector3f(-1,1,0);
							actions.add(action);
						}
					} else {
						Vector3f action = new Vector3f(-1,0,0);
						actions.add(action);
					}
					
					if(checkRight(agents.get(i))){
						
						if(checkTopRight(agents.get(i))){
							//can't move
						} else {
							Vector3f action = new Vector3f(1,1,0);
							actions.add(action);
						}
					} else {
						Vector3f action = new Vector3f(1,0,0);
						actions.add(action);
					}
				}
			}
			
			possibleActions.add(actions);
		}
		
		return possibleActions;
	}
	
	private boolean checkTop(Vector3f agent){
		
		for(int i=0; i<agents.size(); i++){
			
			if (Math.abs(agents.get(i).x - agent.x) < TOL){
				if(Math.abs(agents.get(i).z - agent.z) < TOL){
					if (Math.abs(agents.get(i).y - (agent.y - 1)) < TOL){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean checkBottom(Vector3f agent){
		
		for(int i=0; i<agents.size(); i++){
			
			if (Math.abs(agents.get(i).x - agent.x) < TOL){
				if(Math.abs(agents.get(i).z - agent.z) < TOL){
					if (Math.abs(agent.y - (agents.get(i).y - 1)) < TOL){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean checkRight(Vector3f agent){
		
		for(int i=0; i<agents.size(); i++){
			
			if (Math.abs(agents.get(i).y - agent.y) < TOL){
				if(Math.abs(agents.get(i).z - agent.z) < TOL){
					if (Math.abs(agents.get(i).x - (agent.x-1)) < TOL){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean checkLeft(Vector3f agent){
		
		for(int i=0; i<agents.size(); i++){
			
			if (Math.abs(agents.get(i).y - agent.y) < TOL){
				if(Math.abs(agents.get(i).z - agent.z) < TOL){
					if (Math.abs(agent.x - (agents.get(i).x - 1)) < TOL){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	
	private boolean checkTopFront(Vector3f agent){
		
		for(int i=0; i<agents.size(); i++){
			
			if(Math.abs(agents.get(i).x - agent.x) < TOL){
				if(Math.abs(agents.get(i).y - (agent.y - 1)) < TOL){
					if(Math.abs(agents.get(i).z - (agent.z - 1)) < TOL){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean checkTopRear(Vector3f agent){
		
		for(int i=0; i<agents.size(); i++){
			
			if(Math.abs(agents.get(i).x - agent.x) < TOL){
				if(Math.abs(agents.get(i).y - (agent.y - 1)) < TOL){
					if(Math.abs(agent.z - (agents.get(i).z - 1)) < TOL){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean checkTopRight(Vector3f agent){
		
		for(int i=0; i<agents.size(); i++){
			
			if(Math.abs(agents.get(i).z - agent.z) < TOL){
				if(Math.abs(agents.get(i).y - (agent.y - 1))< TOL){
					if(Math.abs(agents.get(i).x - (agent.x - 1)) <TOL){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean checkTopLeft(Vector3f agent){
		
		for(int i=0; i<agents.size(); i++){
			
			if(Math.abs(agents.get(i).z - agent.z) < TOL){
				if(Math.abs(agents.get(i).y - (agent.y - 1))< TOL){
					if(Math.abs(agent.x - (agents.get(i).x - 1)) <TOL){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean checkFront(Vector3f agent){
		
		for(int i=0; i<agents.size(); i++){
			if(Math.abs(agents.get(i).x - agent.x) < TOL){
				if(Math.abs(agents.get(i).y - agent.y) < TOL){
					if(Math.abs(agents.get(i).z - (agent.z - 1)) < TOL){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean checkRear(Vector3f agent){
		
		for(int i=0; i<agents.size(); i++){
			if(Math.abs(agents.get(i).x - agent.x) < TOL){
				if(Math.abs(agents.get(i).y - agent.y) < TOL){
					if(Math.abs(agent.z - (agents.get(i).z - 1)) < TOL){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean checkBottomFront(Vector3f agent){
		
		for(int i=0; i<agents.size(); i++){
			if(Math.abs(agents.get(i).x - agent.x) < TOL){
				if(Math.abs(agent.y - (agents.get(i).y - 1))< TOL){
					if(Math.abs(agents.get(i).z - (agent.z - 1))< TOL){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean checkBottomRear(Vector3f agent){
		
		for(int i=0; i<agents.size(); i++){
			if(Math.abs(agents.get(i).x - agent.x) < TOL){
				if(Math.abs(agent.y - (agents.get(i).y - 1))< TOL){
					if(Math.abs(agent.z - (agents.get(i).z - 1))< TOL){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean checkBottomLeft(Vector3f agent){
		
		for(int i=0; i<agents.size(); i++){
			if(Math.abs(agents.get(i).z - agent.z) < TOL){
				if(Math.abs(agent.y - (agents.get(i).y - 1))< TOL){
					if(Math.abs(agent.x - (agents.get(i).x - 1))< TOL){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private boolean checkBottomRight(Vector3f agent){
		
		for(int i=0; i<agents.size(); i++){
			if(Math.abs(agents.get(i).z - agent.z) < TOL){
				if(Math.abs(agent.y - (agents.get(i).y - 1))< TOL){
					if(Math.abs(agents.get(i).x - (agent.x - 1))< TOL){
						return true;
					}
				}
			}
		}
		
		return false;
	}

	//GETTERS AND SETTERS
	
	public List<Vector3f> getAgents() {
		return agents;
	}

	public void setAgents(List<Vector3f> agents) {
		this.agents = agents;
	}

	public List<List<Vector3f>> getActions() {
		return actions;
	}

	public void setActions(List<List<Vector3f>> actions) {
		this.actions = actions;
	}

	public double getUcb1() {
		return ucb1;
	}

	public void setUcb1(double ucb1) {
		this.ucb1 = ucb1;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getVisits() {
		return visits;
	}

	public void setVisits(double visits) {
		this.visits = visits;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public static double getTol() {
		return TOL;
	}	
}
