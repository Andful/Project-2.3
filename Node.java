import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3i;

public class Node {
	
	private List<Vector3i> agentConfiguration;
	private List<Vector3i> endConfiguration;
	private List<Vector3i> obstacleConfiguration;
	private List<List<Vector3i>> possibleActions;
	private double value;
	
	public static void main(String[] args){

        List<Vector3i> endConfiguration = new ArrayList<>();
        endConfiguration.add(new Vector3i(0,0,4));
		
		List<Vector3i> startConfiguration = new ArrayList<>();
        startConfiguration.add(new Vector3i(0,1,1));
        startConfiguration.add(new Vector3i(0,0,1));
        startConfiguration.add(new Vector3i(0,0,0));

        List<Vector3i> obstacleConfiguration = new ArrayList<>();
        obstacleConfiguration.add(new Vector3i(0,0,2));
        
        Node node = new Node(startConfiguration,endConfiguration,obstacleConfiguration);
        
        //System.out.println(node.checkBottom(node.getAgentConfiguration().get(0)));
        System.out.println(node.checkFrontBottom(node.getAgentConfiguration().get(0)));
        System.out.println(node.getAgentConfiguration().get(0));
        
	}
	
	public Node(List<Vector3i> startConfiguration, List<Vector3i> endConfiguration, List<Vector3i> obstacleConfiguration){
		this.agentConfiguration = startConfiguration;
		this.endConfiguration = endConfiguration;
		this.obstacleConfiguration = obstacleConfiguration;
		this.possibleActions = getAvailableActions();
		//calculateValue();
	}
	
	public Node(Node node){
		this.agentConfiguration = node.getAgentConfiguration();
		this.endConfiguration = node.getEndConfiguration();
		this.obstacleConfiguration = node.getObstacleConfiguration();
		this.possibleActions = node.getPossibleActions();
		this.value = node.getValue();
	}
	
	/*
	public Vector3i getOptimalAction(){
		
		double dummyValue = 0;
		int index = 0;
		int actIndex = 0;
		
		for(int i=0; i<agentConfiguration.size(); i++){
			for(int j=0; j<possibleActions.get(i).size(); j++){
				Node node = new Node(this);
				node.getAgentConfiguration().get(i).add(possibleActions.get(i).get(j));
				//node.calculateValue();
				
				if(dummyValue<node.getValue()){
					dummyValue = node.getValue();
					index = i;
					actIndex = j;
				}
			}
		}
		
		Vector3i optimalAction = new Vector3i(possibleActions.get(index).get(actIndex));
		
		return optimalAction;
	}
	*/
	
	//must check both obstacle and agent cases to work properly!!!
	private List<List<Vector3i>> getAvailableActions(){
		
		List<List<Vector3i>> possibleActions = new ArrayList<>();
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			List<Vector3i> actions = new ArrayList<>();
			
			if(!checkFront(agentConfiguration.get(i))){
				actions.add(new Vector3i(0,0,1));
			}
			
			if(!checkRear(agentConfiguration.get(i))){
				actions.add(new Vector3i(0,0,-1));
			}
			
			if(!checkLeft(agentConfiguration.get(i))){
				actions.add(new Vector3i(-1,0,0));
			}
			
			if(!checkRight(agentConfiguration.get(i))){
				actions.add(new Vector3i(1,0,0));
			}
			
			possibleActions.add(actions);
		}
		
		return possibleActions;
	}
	
	//works
	/*
	private double calculateValue(){
		
		double sumOfHeuristics = 0;
		
		for(int i=0; i<agentConfiguration.size(); i++){
			sumOfHeuristics = Math.abs(agentConfiguration.get(i).x - endConfiguration.get(i).x) 
					+ Math.abs(agentConfiguration.get(i).z - endConfiguration.get(i).z)
					+ Math.abs(agentConfiguration.get(i).y - agentConfiguration.get(i).y);
		}
		
		this.value = 1/sumOfHeuristics;
		
		return 1/sumOfHeuristics;
	}
	*/
	
	//works
	private boolean checkFront(Vector3i agent){
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if((Math.abs(agentConfiguration.get(i).y - agent.y) == 0) && (Math.abs(agentConfiguration.get(i).x - agent.x) ==0)){
				
				if(agentConfiguration.get(i).z - agent.z == 1){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//works
	private boolean checkFrontObstacle(Vector3i agent){
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if((Math.abs(obstacleConfiguration.get(i).y - agent.y) == 0) && (Math.abs(obstacleConfiguration.get(i).x - agent.x) ==0)){
				
				if(obstacleConfiguration.get(i).z - agent.z == 1){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//works
	private boolean checkRear(Vector3i agent){
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if((Math.abs(agentConfiguration.get(i).y - agent.y) == 0) && (Math.abs(agentConfiguration.get(i).x - agent.x) ==0)){
				
				if(agent.z - agentConfiguration.get(i).z == 1){
					return true;
				}
			}
		}
		
		return false;
	}

	//works
	private boolean checkRearObstacle(Vector3i agent){
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if((Math.abs(obstacleConfiguration.get(i).y - agent.y) == 0) && (Math.abs(obstacleConfiguration.get(i).x - agent.x) ==0)){
				
				if(agent.z - obstacleConfiguration.get(i).z == 1){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//works
	private boolean checkRight(Vector3i agent){
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if((Math.abs(agentConfiguration.get(i).y -agent.y) == 0) && (Math.abs(agentConfiguration.get(i).z - agent.z) == 0)){
				
				if(agentConfiguration.get(i).x - agent.x == 1){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//works
	private boolean checkRightObstacle(Vector3i agent){
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if((Math.abs(obstacleConfiguration.get(i).y -agent.y) == 0) && (Math.abs(obstacleConfiguration.get(i).z - agent.z) == 0)){
				
				if(obstacleConfiguration.get(i).x - agent.x == 1){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//works
	private boolean checkLeft(Vector3i agent){
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if((Math.abs(agentConfiguration.get(i).y -agent.y) == 0) && (Math.abs(agentConfiguration.get(i).z - agent.z) == 0)){
				
				if(agent.x - agentConfiguration.get(i).x == 1){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//works
	private boolean checkLeftObstacle(Vector3i agent){
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if((Math.abs(obstacleConfiguration.get(i).y -agent.y) == 0) && (Math.abs(obstacleConfiguration.get(i).z - agent.z) == 0)){
				
				if(agent.x - obstacleConfiguration.get(i).x == 1){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//works
	private boolean checkTop(Vector3i agent){
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if((Math.abs(agentConfiguration.get(i).x - agent.x) == 0) && (Math.abs(agentConfiguration.get(i).z - agent.z) == 0)){
				
				if(agentConfiguration.get(i).y - agent.y == 1){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//works
	private boolean checkTopObstacle(Vector3i agent){
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if((Math.abs(obstacleConfiguration.get(i).x - agent.x) == 0) && (Math.abs(obstacleConfiguration.get(i).z - agent.z) == 0)){
				
				if(obstacleConfiguration.get(i).y - agent.y == 1){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//works
	private boolean checkBottom(Vector3i agent){
		
		int index = 0;
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if((Math.abs(agentConfiguration.get(i).x - agent.x) == 0) && (Math.abs(agentConfiguration.get(i).z - agent.z) == 0)){
				
				if(agent.y - agentConfiguration.get(i).y > 0){
					index = i;
				}
			}
		}
		
		if(agent.y - agentConfiguration.get(index).y > 0){
			agent.y = agentConfiguration.get(index).y + 1;
			return true;
		} else {
			agent.y = 0;
		}
		
		return false;
	}
	
	//works
	private boolean checkBottomObstacle(Vector3i agent){
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if((Math.abs(obstacleConfiguration.get(i).x - agent.x) == 0) && (Math.abs(obstacleConfiguration.get(i).z - agent.z) == 0)){
				
				if(agent.y - obstacleConfiguration.get(i).y == 1){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//works
	private boolean checkFrontTop(Vector3i agent){
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if((Math.abs(agentConfiguration.get(i).x - agent.x) ==0)){
				
				if((agentConfiguration.get(i).z - agent.z == 1) && (agentConfiguration.get(i).y - agent.y == 1)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//works
	private boolean checkFrontTopObstacle(Vector3i agent){
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if((Math.abs(obstacleConfiguration.get(i).x - agent.x) ==0)){
				
				if((obstacleConfiguration.get(i).z - agent.z == 1) && (obstacleConfiguration.get(i).y - agent.y == 1)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//works
	private boolean checkRearTop(Vector3i agent){
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if((Math.abs(agentConfiguration.get(i).x - agent.x) ==0)){
				
				if((agent.z - agentConfiguration.get(i).z == 1) && (agentConfiguration.get(i).y - agent.y == 1)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//works
	private boolean checkRearTopObstacle(Vector3i agent){
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if((Math.abs(obstacleConfiguration.get(i).x - agent.x) ==0)){
				
				if((agent.z - obstacleConfiguration.get(i).z == 1) && (obstacleConfiguration.get(i).y - agent.y == 1)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//works
	private boolean checkRightTop(Vector3i agent){
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if(Math.abs(agentConfiguration.get(i).z - agent.z) == 0){
				
				if((agentConfiguration.get(i).x - agent.x == 1) && (agentConfiguration.get(i).y - agent.y == 1)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//works
	private boolean checkRightTopObstacle(Vector3i agent){
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if(Math.abs(obstacleConfiguration.get(i).z - agent.z) == 0){
				
				if((obstacleConfiguration.get(i).x - agent.x == 1) && (obstacleConfiguration.get(i).y - agent.y == 1)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//works
	private boolean checkLeftTop(Vector3i agent){
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if(Math.abs(agentConfiguration.get(i).z - agent.z) == 0){
				
				if((agent.x - agentConfiguration.get(i).x == 1) && (agentConfiguration.get(i).y - agent.y == 1)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//works
	private boolean checkLeftTopObstacle(Vector3i agent){
			
		for(int i=0; i<obstacleConfiguration.size(); i++){
				
			if(Math.abs(obstacleConfiguration.get(i).z - agent.z) == 0){
					
				if((agent.x - obstacleConfiguration.get(i).x == 1) && (obstacleConfiguration.get(i).y - agent.y == 1)){
					return true;
				}
			}
		}
			
		return false;
	}
	
	//works
	private boolean checkFrontBottom(Vector3i agent){
		
		int index = 0;
		
		boolean flag = false;
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if((Math.abs(agentConfiguration.get(i).x - agent.x)) == 0){
				
				if(agentConfiguration.get(i).z - agent.z == 1){
					
					if(agent.y - agentConfiguration.get(i).y > 0){
						index = i;
					}
					
					if(agent.y - agentConfiguration.get(i).y == 1){
						flag = true;
					}
				}
			}
		}
		
		if(agent.y - agentConfiguration.get(index).y > 1){
			
			if((Math.abs(agent.x - agentConfiguration.get(index).x) == 0) && (Math.abs(agent.z - agentConfiguration.get(index).z) == 0)){
				agent.y = agentConfiguration.get(index).y + 1;
			} else {
				agent.y = 0;
			}
			return flag;
		} else if(agent.y - agentConfiguration.get(index).y <= 1) {
			agent.y = 0;
		} 
		
		return false;
	}
	
	//works
	private boolean checkFrontBottomObstacle(Vector3i agent){
		
		int index = 0;
		
		boolean flag = false;
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if((Math.abs(obstacleConfiguration.get(i).x - agent.x)) == 0){
				
				if(obstacleConfiguration.get(i).z - agent.z == 1){
					
					if(agent.y - obstacleConfiguration.get(i).y > 0){
						index = i;
					}
					
					if(agent.y - obstacleConfiguration.get(i).y == 1){
						flag = true;
					}
				}
			}
		}
		
		if(agent.y - obstacleConfiguration.get(index).y > 1){
			
			if((Math.abs(agent.x - obstacleConfiguration.get(index).x) == 0) && (Math.abs(agent.z - obstacleConfiguration.get(index).z) == 0)){
				agent.y = obstacleConfiguration.get(index).y + 1;
			} else {
				agent.y = 0;
			}
			return flag;
		} else if(agent.y - obstacleConfiguration.get(index).y <= 1) {
			agent.y = 0;
		} 
		
		return false;
	}
	
	private boolean checkRearBottom(Vector3i agent){
		
		int index = 0;
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if((Math.abs(agentConfiguration.get(i).x - agent.x) == 0)){
				
				if(agent.z - agentConfiguration.get(i).z == 1){
					
					if(agent.y - agentConfiguration.get(i).y > 0){
						index = i;
					}
				}
			}
		}
		
		if(agent.y - agentConfiguration.get(index).y > 0){
			agent.y = agentConfiguration.get(index).y + 1;
			return true;
		} else {
			agent.y = 0;
		}
		
		return false;
	}
	
	public List<Vector3i> getAgentConfiguration() {
		return agentConfiguration;
	}

	public void setAgentConfiguration(List<Vector3i> agentConfiguration) {
		this.agentConfiguration = agentConfiguration;
	}

	public List<Vector3i> getEndConfiguration() {
		return endConfiguration;
	}

	public void setEndConfiguration(List<Vector3i> endConfiguration) {
		this.endConfiguration = endConfiguration;
	}

	public List<Vector3i> getObstacleConfiguration() {
		return obstacleConfiguration;
	}

	public void setObstacleConfiguration(List<Vector3i> obstacleConfiguration) {
		this.obstacleConfiguration = obstacleConfiguration;
	}

	public List<List<Vector3i>> getActions() {
		return possibleActions;
	}

	public void setActions(List<List<Vector3i>> actions) {
		this.possibleActions = actions;
	}

	public List<List<Vector3i>> getPossibleActions() {
		return possibleActions;
	}

	public void setPossibleActions(List<List<Vector3i>> possibleActions) {
		this.possibleActions = possibleActions;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	
	
	
	

}
