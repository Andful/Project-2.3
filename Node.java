import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3i;

public class Node {
	
	private List<Vector3i> agentConfiguration;
	private List<Vector3i> endConfiguration;
	private List<Vector3i> obstacleConfiguration;
	private List<List<Vector3i>> possibleActions;
	
	public static void main(String[] args){

        List<Vector3i> endConfiguration = new ArrayList<>();
        endConfiguration.add(new Vector3i(0,0,4));
		
		List<Vector3i> startConfiguration = new ArrayList<>();
		startConfiguration.add(new Vector3i(0,1,0));
		startConfiguration.add(new Vector3i(0,0,0));
		
        List<Vector3i> obstacleConfiguration = new ArrayList<>();
        
        Node firstState = new Node(startConfiguration,endConfiguration,obstacleConfiguration);
        
        System.out.println(firstState.getAgentConfiguration().get(0)); //print coordinates of first agent
        System.out.println(firstState.getPossibleActions().get(0));
        
       // Node secondState = new Node(firstState);
        //secondState.getAgentConfiguration().get(0).add(secondState.getActions().get(0).get(0));
        
      //  System.out.println(secondState.getAgentConfiguration().get(0));
	}
	
	public Node(List<Vector3i> startConfiguration, List<Vector3i> endConfiguration, List<Vector3i> obstacleConfiguration){
		this.agentConfiguration = startConfiguration;
		this.endConfiguration = endConfiguration;
		this.obstacleConfiguration = obstacleConfiguration;
		this.possibleActions = getAvailableActions();
	}
	
	public Node(Node node){
		this.agentConfiguration = node.getAgentConfiguration();
		this.endConfiguration = node.getEndConfiguration();
		this.obstacleConfiguration = node.getObstacleConfiguration();
		this.possibleActions = node.getPossibleActions();
	}
	
	
	//must check both obstacle and agent cases to work properly!!!
	//WORKS FOR AGENTS!!!
	private List<List<Vector3i>> getAvailableActions(){
		
		List<List<Vector3i>> possibleActions = new ArrayList<>();
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			List<Vector3i> actions = new ArrayList<>();
			
			if(checkTop(agentConfiguration.get(i))){
				possibleActions.add(actions);
				continue;
			} else{
				applyGravity(agentConfiguration.get(i));
				if(checkBottom(agentConfiguration.get(i))){
					
					if(checkFront(agentConfiguration.get(i))){
						
						if(!checkFrontTop(agentConfiguration.get(i))){
							actions.add(new Vector3i(0,1,1));
						}
						
					} else{
						
						
						if(!checkFrontObstacle(agentConfiguration.get(i))){
							
							if(checkFrontBottom(agentConfiguration.get(i))){
								
								actions.add(new Vector3i(0,-1,1));
							}
						}
					}
					
					if(checkRear(agentConfiguration.get(i))){
						
						if(!checkRearTop(agentConfiguration.get(i))){
							actions.add(new Vector3i(0,1,-1));
						}
						
					} else {
						
						if(!checkRearObstacle(agentConfiguration.get(i))){
							
							if(checkRearBottom(agentConfiguration.get(i))){
								actions.add(new Vector3i(0,-1,-1));
							}
						}
					}
					
					if(checkLeft(agentConfiguration.get(i))){
						
						if(!checkLeftTop(agentConfiguration.get(i))){
							actions.add(new Vector3i(-1,1,0));
						}
					} else {
						
						if(!checkLeftObstacle(agentConfiguration.get(i))){
							
							if(checkLeftBottom(agentConfiguration.get(i))){
								actions.add(new Vector3i(-1,-1,0));
							}
							
						}
					}
					
					if(checkRight(agentConfiguration.get(i))){
						
						if(!checkRightTop(agentConfiguration.get(i))){
							actions.add(new Vector3i(1,1,0));
						}
					} else {
						
						if(!checkRightObstacle(agentConfiguration.get(i))){
							
							if(checkRightBottom(agentConfiguration.get(i))){
								actions.add(new Vector3i(1,-1,0));
							}
						}
					}
				} else {
					
					if(checkBottomObstacle(agentConfiguration.get(i))){
						
						if(checkFrontBottom(agentConfiguration.get(i))){
							actions.add(new Vector3i(0,-1,1));
						}
						
						if(checkRearBottom(agentConfiguration.get(i))){
							actions.add(new Vector3i(0,-1,-1));
						}
						
						if(checkLeftBottom(agentConfiguration.get(i))){
							actions.add(new Vector3i(-1,-1,0));
						}
						
						if(checkRightBottom(agentConfiguration.get(i))){
							actions.add(new Vector3i(1,-1,0));
						}
					} else {
						
						if(checkFront(agentConfiguration.get(i))){
							
							if(!checkFrontTop(agentConfiguration.get(i))){
								actions.add(new Vector3i(0,1,1));
							}
						}
						
						if(checkRear(agentConfiguration.get(i))){
							
							if(!checkRearTop(agentConfiguration.get(i))){
								actions.add(new Vector3i(0,1,-1));
							}
						}
						
						if(checkLeft(agentConfiguration.get(i))){
							
							if(!checkLeftTop(agentConfiguration.get(i))){
								actions.add(new Vector3i(-1,1,0));
							}
						}
						
						if(checkRight(agentConfiguration.get(i))){
							
							if(!checkRightTop(agentConfiguration.get(i))){
								actions.add(new Vector3i(1,1,0));
							}
						}
					}
				}
			}
		
		possibleActions.add(actions);
		}
		
		return possibleActions;
	}
	
	
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
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if((Math.abs(agent.z - agentConfiguration.get(i).z) == 0) && (Math.abs(agent.x - agentConfiguration.get(i).x) == 0)){
				
				if(agent.y - agentConfiguration.get(i).y == 1){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean checkBottomObstacle(Vector3i agent){
		
		int index = 0;
		int dummy = -1;		
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if((Math.abs(obstacleConfiguration.get(i).x - agent.x) == 0) && (Math.abs(obstacleConfiguration.get(i).z - agent.z) == 0)){
				
				if(agent.y - obstacleConfiguration.get(i).y >= 0){
					
					if(obstacleConfiguration.get(i).y >= dummy){
						dummy = obstacleConfiguration.get(i).y;						
						index = i;
						
					}
				} 
			}
		}		
		try{
			
			if(agent.y - obstacleConfiguration.get(index).y >= 0){
				
				agent.y = obstacleConfiguration.get(index).y + 1;
				return true;
			} else {
				agent.y = 0;
			}
			
		} catch(IndexOutOfBoundsException e) {}
				
					
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
	
	//bug has not affected getAvailableActions thus far, gravity works correctly
	private boolean checkFrontBottom(Vector3i agent){
		
		boolean flag = false;
		int index = 0;
		int dummy = 0;
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if(Math.abs(agentConfiguration.get(i).x - agent.x) == 0){
				
				if(Math.abs(agent.z - agentConfiguration.get(i).z) == 0){
					
					if(agent.y - agentConfiguration.get(i).y > 0){
						
						if(agentConfiguration.get(i).y >= dummy){
							dummy = agentConfiguration.get(i).y;
							index = i;
						}
					}
				}
			}
			
			if(agent.y - agentConfiguration.get(index).y > 0){
				agent.y = agentConfiguration.get(index).y + 1;
				
			} else {
				agent.y = 0;
				
			}
			
			if(Math.abs(agentConfiguration.get(i).x - agent.x) == 0){
				if((agent.y - agentConfiguration.get(i).y == 1) && (agentConfiguration.get(i).z - agent.z == 1)){
					flag = true;
				}
			}
		}
		
		
		
		return flag;
	}
	
	private void applyGravity(Vector3i agent){
		
		int dummy = -1;
		int index = 0;
		boolean checker = false;
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if((Math.abs(agentConfiguration.get(i).x - agent.x) == 0) && (Math.abs(agentConfiguration.get(i).z - agent.z)) == 0){
				
				if(agent.y - agentConfiguration.get(i).y > 0){
					
					if(agentConfiguration.get(i).y >= dummy){
						dummy = agentConfiguration.get(i).y;
						index = i;
					}
				}
			}			
		}
		
		if(dummy == -1){
			
			for(int i=0; i<obstacleConfiguration.size(); i++){
				
				if((Math.abs(obstacleConfiguration.get(i).x - agent.x) == 0) && (Math.abs(obstacleConfiguration.get(i).z - agent.z)) == 0){
					
					if(agent.y - obstacleConfiguration.get(i).y > 0){
						
						if(obstacleConfiguration.get(i).y >= dummy){
							dummy = obstacleConfiguration.get(i).y;
							index = i;
							checker = true;
						}
					}
				
			}
		}
	}
		
		if(checker){
			
			if(agent.y - obstacleConfiguration.get(index).y > 0){
				agent.y = obstacleConfiguration.get(index).y + 1;
			} else {
				agent.y = 0;
			}
		}  else {
			
			if(agent.y - agentConfiguration.get(index).y > 0){
				agent.y = agentConfiguration.get(index).y + 1;
			} else {
				agent.y = 0;
			}
		}
		
		
	}
	
	private boolean checkFrontBottomObstacle(Vector3i agent){
		
		boolean flag = false;
		int index = 0;
		int dummy = 0;
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if(Math.abs(obstacleConfiguration.get(i).x - agent.x) == 0){
				
				if(Math.abs(agent.z - obstacleConfiguration.get(i).z) == 0){
					
					if(agent.y - obstacleConfiguration.get(i).y > 0){
						
						if(obstacleConfiguration.get(i).y >= dummy){
							dummy = obstacleConfiguration.get(i).y;
							index = i;
						}
					}
				}
			}
			
			if(Math.abs(obstacleConfiguration.get(i).x - agent.x) == 0){
				
				if((agent.y - obstacleConfiguration.get(i).y == 1) && (obstacleConfiguration.get(i).z - agent.z == 1)){
					
					flag = true;
				}
			}
		}
		
		if(agent.y - obstacleConfiguration.get(index).y > 0){
			agent.y = obstacleConfiguration.get(index).y + 1;
			return flag;
		} else {
			agent.y = 0;
			return flag;
		}
	}
	
	//bug has not affected getAvailableActions thus far, gravity works correctly
	private boolean checkRearBottom(Vector3i agent){
		
		boolean flag = false;
		int index = 0;
		int dummy = 0;
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if(Math.abs(agentConfiguration.get(i).x - agent.x) == 0){
				
				if(Math.abs(agent.z - agentConfiguration.get(i).z) == 0){
					
					if(agent.y - agentConfiguration.get(i).y > 0){
						
						if(agentConfiguration.get(i).y >= dummy){
							dummy = agentConfiguration.get(i).y;
							index = i;
						}
					}
				}
				
				if((agent.y - agentConfiguration.get(i).y == 1) && (agent.z - agentConfiguration.get(i).z == 1)){
					flag = true;
				}
			}
		}
		
		if(agent.y - agentConfiguration.get(index).y > 0){
			agent.y = agentConfiguration.get(index).y + 1;
			return flag;
		} else {
			agent.y = 0;
			return flag;
		}
	}
	
	private boolean checkRearBottomObstacle(Vector3i agent){
		
		boolean flag = false;
		int index = 0;
		int dummy = 0;
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if(Math.abs(obstacleConfiguration.get(i).x - agent.x) == 0){
				
				if(Math.abs(agent.z - obstacleConfiguration.get(i).z) == 0){
					
					if(agent.y - obstacleConfiguration.get(i).y > 0){
						
						if(obstacleConfiguration.get(i).y >= dummy){
							dummy = obstacleConfiguration.get(i).y;
							index = i;
						}
					}
				}
				
				if((agent.y - obstacleConfiguration.get(i).y == 1) && (agent.z - obstacleConfiguration.get(i).z == 1)){
					flag = true;
				}
			}
		}
		
		if(agent.y - obstacleConfiguration.get(index).y > 0){
			agent.y = obstacleConfiguration.get(index).y + 1;
			return flag;
		} else {
			agent.y = 0;
			return flag;
		}
	}

	//bug has not affected getAvailableActions thus far, gravity works correctly
	private boolean checkRightBottom(Vector3i agent){
		
		int index = 0;
		int dummy = 0;
		boolean flag = false;
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if(Math.abs(agent.z - agentConfiguration.get(i).z) == 0){
				
				if(Math.abs(agent.x - agentConfiguration.get(i).x) == 0){
					
					if(agent.y - agentConfiguration.get(i).y > 0){
						
						if(agentConfiguration.get(i).y >= dummy){
							dummy = agentConfiguration.get(i).y;
							index = i;
						}
					}
				}
				
				if((agent.y - agentConfiguration.get(i).y == 1) && (agentConfiguration.get(i).x - agent.x == 1)){
					flag = true;
				}
			}
		}
		
		if(agent.y - agentConfiguration.get(index).y > 0){
			agent.y = agentConfiguration.get(index).y + 1;
			return flag;
		} else {
			agent.y = 0;
			return flag;
		}
	}
	
	private boolean checkRightBottomObstacle(Vector3i agent){
		
		int index = 0;
		int dummy = 0;
		boolean flag = false;
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if(Math.abs(agent.z - obstacleConfiguration.get(i).z) == 0){
				
				if(Math.abs(agent.x - obstacleConfiguration.get(i).x) == 0){
					
					if(agent.y - obstacleConfiguration.get(i).y > 0){
						
						if(obstacleConfiguration.get(i).y >= dummy){
							dummy = obstacleConfiguration.get(i).y;
							index = i;
						}
					}
				}
				
				if((agent.y - obstacleConfiguration.get(i).y == 1) && (obstacleConfiguration.get(i).x - agent.x == 1)){
					flag = true;
				}
			}
		}
		
		if(agent.y - obstacleConfiguration.get(index).y > 0){
			agent.y = obstacleConfiguration.get(index).y + 1;
			return flag;
		} else {
			agent.y = 0;
			return flag;
		}
	}

	//bug has not affected getAvailableActions thus far, gravity works correctly
	private boolean checkLeftBottom(Vector3i agent){
		
		int dummy = 0;
		int index = 0;
		boolean flag = false;
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if(Math.abs(agent.z - agentConfiguration.get(i).z) == 0){
				
				if(Math.abs(agent.x - agentConfiguration.get(i).x) == 0){
					
					if(agent.y - agentConfiguration.get(i).y > 0){
						
						if(agentConfiguration.get(i).y >= dummy){
							dummy = agentConfiguration.get(i).y;
							index = i;
						}
					}
				}
				
				if((agent.x - agentConfiguration.get(i).x == 1) && (agent.y - agentConfiguration.get(i).y == 1)){
					flag = true;
				}
			}
		}
		
		if(agent.y - agentConfiguration.get(index).y > 0){
			agent.y = agentConfiguration.get(index).y + 1;
			return flag;
		} else {
			agent.y = 0;
			return flag;
		}
	}
	
	private boolean checkLeftBottomObstacle(Vector3i agent){
		
		int dummy = 0;
		int index = 0;
		boolean flag = false;
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if(Math.abs(agent.z - obstacleConfiguration.get(i).z) == 0){
				
				if(Math.abs(agent.x - obstacleConfiguration.get(i).x) == 0){
					
					if(agent.y - obstacleConfiguration.get(i).y > 0){
						
						if(obstacleConfiguration.get(i).y >= dummy){
							dummy = obstacleConfiguration.get(i).y;
							index = i;
						}
					}
				}
				
				if((agent.x - obstacleConfiguration.get(i).x == 1) && (agent.y - obstacleConfiguration.get(i).y == 1)){
					flag = true;
				}
			}
		}
		
		if(agent.y - obstacleConfiguration.get(index).y > 0){
			agent.y = obstacleConfiguration.get(index).y + 1;
			return flag;
		} else {
			agent.y = 0;
			return flag;
		}
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

}
