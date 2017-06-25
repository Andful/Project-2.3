import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.joml.Vector3i;

public class Node {
	
	private List<Vector3i> agentConfiguration;
	private List<Vector3i> endConfiguration;
	private List<Vector3i> obstacleConfiguration;
	private List<List<Vector3i>> possibleActions;
	private List<List<Vector3i>> optimalActions;
	
	public static void main(String[] args){

        List<Vector3i> endConfiguration = new ArrayList<>();
        endConfiguration.add(new Vector3i(0,0,4));
        endConfiguration.add(new Vector3i(0,0,3));
		
		List<Vector3i> startConfiguration = new ArrayList<>();
		startConfiguration.add(new Vector3i(0,0,0));
		startConfiguration.add(new Vector3i(0,0,1));
		
        List<Vector3i> obstacleConfiguration = new ArrayList<>();
        
        Node firstState = new Node(startConfiguration,endConfiguration,obstacleConfiguration);
      
      //  System.out.println(firstState.getActions().get(0));
      //  System.out.println(firstState.getOptimalActions().get(0));
        System.out.println(firstState.getActions().get(1));
        System.out.println(firstState.getOptimalActions().get(1));
	}
	
	public Node(List<Vector3i> startConfiguration, List<Vector3i> endConfiguration, List<Vector3i> obstacleConfiguration){
		this.agentConfiguration = startConfiguration;
		this.endConfiguration = endConfiguration;
		this.obstacleConfiguration = obstacleConfiguration;
		this.possibleActions = getAvailableActions();
		this.optimalActions = findOptimalActions();
	}
	
	public Node(Node node){
		this.agentConfiguration = node.getAgentConfiguration();
		this.endConfiguration = node.getEndConfiguration();
		this.obstacleConfiguration = node.getObstacleConfiguration();
		this.possibleActions = node.getActions();
		this.optimalActions = node.getOptimalActions();
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
							
							if(!checkFrontBottom(agentConfiguration.get(i))){
								
								if(!checkFrontBottomObstacle(agentConfiguration.get(i))){
										actions.add(new Vector3i(0,-1,1));									
								}
							} else {
								actions.add(new Vector3i(0,0,1));
							}
						}
					}
					
					if(checkRear(agentConfiguration.get(i))){
						
						if(!checkRearTop(agentConfiguration.get(i))){
							actions.add(new Vector3i(0,1,-1));
						}
						
					} else {
						
						if(!checkRearObstacle(agentConfiguration.get(i))){
							
							if(!checkRearBottom(agentConfiguration.get(i))){
								
								if(!checkRearBottomObstacle(agentConfiguration.get(i))){
										actions.add(new Vector3i(0,-1,-1));
									
								}
							} else {
								actions.add(new Vector3i(0,0,-1));
							}
						}
					}
					
					if(checkLeft(agentConfiguration.get(i))){
						
						if(!checkLeftTop(agentConfiguration.get(i))){
							actions.add(new Vector3i(-1,1,0));
						}
					} else {
						
						if(!checkLeftObstacle(agentConfiguration.get(i))){
							
							if(!checkLeftBottom(agentConfiguration.get(i))){
								
								if(!checkLeftBottomObstacle(agentConfiguration.get(i))){
									actions.add(new Vector3i(-1,-1,0));
								}
							} else {
								actions.add(new Vector3i(-1,0,0));
							}
							
						}
					}
					
					if(checkRight(agentConfiguration.get(i))){
						
						if(!checkRightTop(agentConfiguration.get(i))){
							actions.add(new Vector3i(1,1,0));
						}
					} else {
						
						if(!checkRightObstacle(agentConfiguration.get(i))){
							
							if(!checkRightBottom(agentConfiguration.get(i))){
								
								if(!checkRightBottomObstacle(agentConfiguration.get(i))){
									actions.add(new Vector3i(1,-1,0));
								}
							} else {
								actions.add(new Vector3i(1,0,0));
							}
						}
					}
				} else {
					
					if(!checkBottomObstacle(agentConfiguration.get(i))){
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
	
	public Vector3i getIdealMove(){
		
		
		
		for(int i=0;i<optimalActions.size(); i++){
			
		}
		
		return null;
	}
	
	private List<List<Vector3i>> findOptimalActions(){
		
		List<List<Vector3i>> optimalActions = new ArrayList<>();
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			List<Vector3i> actions = new ArrayList<>();
			
			if(Math.abs(agentConfiguration.get(i).x - endConfiguration.get(i).x) == 0){
				
				Vector3i front = new Vector3i(0,0,1);
				Vector3i rear = new Vector3i(0,0,-1);
				Vector3i frontUp = new Vector3i(0,1,1);
				Vector3i rearUp = new Vector3i(0,1,-1);
				Vector3i frontDown = new Vector3i(0,-1,1);
				Vector3i rearDown = new Vector3i(0,-1,-1);
				
				for(int j=0; j<possibleActions.get(i).size(); j++){
					
					if(endConfiguration.get(i).y - agentConfiguration.get(i).y > 0){
						
						if(endConfiguration.get(i).z - agentConfiguration.get(i).z > 0){
							
							if(possibleActions.get(i).get(j).equals(frontUp)){
								
								actions.add(frontUp);
							}
						}
						
						if(agentConfiguration.get(i).z - endConfiguration.get(i).z > 0){
							
							if(possibleActions.get(i).get(j).equals(rearUp)){
								
								actions.add(rearUp);
							}
						}
							
					}
					
					else if(agentConfiguration.get(i).y - endConfiguration.get(i).y > 0){
						
						if(endConfiguration.get(i).z - agentConfiguration.get(i).z > 0){
							
							if(possibleActions.get(i).get(j).equals(frontDown)){
								
								actions.add(frontDown);
							}
						}
						
						if(agentConfiguration.get(i).z - endConfiguration.get(i).z > 0){
							
							if(possibleActions.get(i).get(j).equals(rearDown)){
								
								actions.add(rearDown);
							}
						}
							
					} else {
						
						if(endConfiguration.get(i).z - agentConfiguration.get(i).z > 0){
							
							if(possibleActions.get(i).get(j).equals(front)){
								actions.add(front);
							}
						}
						
						if(agentConfiguration.get(i).z - endConfiguration.get(i).z > 0){
							
							if(possibleActions.get(i).get(j).equals(rear)){
								actions.add(rear);
							}
						}
					}
				}
				
			}
			
			if(Math.abs(agentConfiguration.get(i).z - endConfiguration.get(i).z) == 0){
				
				Vector3i right = new Vector3i(1,0,0);
				Vector3i left = new Vector3i(-1,0,0);
				Vector3i rightUp = new Vector3i(1,1,0);
				Vector3i leftUp = new Vector3i(-1,1,0);
				Vector3i rightDown = new Vector3i(1,-1,0);
				Vector3i leftDown = new Vector3i(-1,-1,0);
				
				for(int j=0; j<possibleActions.get(i).size(); j++){
					
					if(endConfiguration.get(i).y - agentConfiguration.get(i).y > 0){
						
						if(endConfiguration.get(i).x - agentConfiguration.get(i).x > 0){
							
							if(possibleActions.get(i).get(j).equals(rightUp)){
								actions.add(rightUp);
							}
						}
						
						if(agentConfiguration.get(i).x - endConfiguration.get(i).x > 0){
							
							if(possibleActions.get(i).get(j).equals(leftUp)){
								actions.add(leftUp);
							}
						}
					}
					
					else if(agentConfiguration.get(i).y - endConfiguration.get(i).y > 0){
						
						if(endConfiguration.get(i).x - agentConfiguration.get(i).x > 0){
							
							if(possibleActions.get(i).get(j).equals(rightDown)){
								actions.add(rightDown);
							}
						}
						
						if(agentConfiguration.get(i).x - endConfiguration.get(i).x > 0){
							
							if(possibleActions.get(i).get(j).equals(leftDown)){
								actions.add(leftDown);
							}
						}
					} else {
						
						if(endConfiguration.get(i).x - agentConfiguration.get(i).x > 0){
							
							if(possibleActions.get(i).get(j).equals(right)){
								actions.add(right);
							}
						}
						
						if(agentConfiguration.get(i).x - endConfiguration.get(i).x > 0){
							
							if(possibleActions.get(i).get(j).equals(left)){
								actions.add(left);
							}
						}
						
					}
				}
			}
			
			if((!(Math.abs(endConfiguration.get(i).z - agentConfiguration.get(i).z) == 0)) 
					&& (!(Math.abs(endConfiguration.get(i).x - agentConfiguration.get(i).x)==0))){
				
				Vector3i frontUp = new Vector3i(0,1,1);
				Vector3i rearUp = new Vector3i(0,1,-1);
				Vector3i rightUp = new Vector3i(1,1,0);
				Vector3i leftUp = new Vector3i(-1,1,0);
				Vector3i frontDown = new Vector3i(0,-1,1);
				Vector3i rearDown = new Vector3i(0,-1,-1);
				Vector3i rightDown = new Vector3i(1,-1,0);
				Vector3i leftDown = new Vector3i(-1,-1,0);
				Vector3i rear = new Vector3i(0,0,-1);
				Vector3i front = new Vector3i(0,0,1);
				Vector3i right = new Vector3i(1,0,0);
				Vector3i left = new Vector3i(-1,0,0);
				
				if(endConfiguration.get(i).y - agentConfiguration.get(i).y > 0){
					
					int differenceOfXs = Math.abs(agentConfiguration.get(i).x - endConfiguration.get(i).x);
					int differenceOfZs = Math.abs(agentConfiguration.get(i).z - endConfiguration.get(i).z);
					
					if(differenceOfZs > differenceOfXs){
						
						if(agentConfiguration.get(i).z - endConfiguration.get(i).z > 0){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(rearUp)){
									actions.add(rearUp);
								}
							}
						}
						
						if(endConfiguration.get(i).z - agentConfiguration.get(i).z > 0){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(frontUp)){
									actions.add(frontUp);
								}
							}
						}
					} else {
						
						if(agentConfiguration.get(i).x - endConfiguration.get(i).x > 0){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(leftUp)){
									actions.add(leftUp);
								}
							}
						}
						
						if(endConfiguration.get(i).x - agentConfiguration.get(i).x > 0){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(rightUp)){
									actions.add(rightUp);
								}
							}
							
						}
					}
				}
				
				else if(agentConfiguration.get(i).y - endConfiguration.get(i).y > 0){
					
					int differenceOfXs = Math.abs(agentConfiguration.get(i).x - endConfiguration.get(i).x);
					int differenceOfZs = Math.abs(agentConfiguration.get(i).z - endConfiguration.get(i).z);
					
					if(differenceOfZs > differenceOfXs){
						
						if(agentConfiguration.get(i).z - endConfiguration.get(i).z > 0){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(rearDown)){
									actions.add(rearDown);
								}
							}
						}
						
						if(endConfiguration.get(i).z - agentConfiguration.get(i).z > 0){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(frontDown)){
									actions.add(frontDown);
								}
							}
						}
					} else {
						
						if(agentConfiguration.get(i).x - endConfiguration.get(i).x > 0){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(leftDown)){
									actions.add(leftDown);
								}
							}
						}
						
						if(endConfiguration.get(i).x - agentConfiguration.get(i).x > 0){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(rightDown)){
									actions.add(rightDown);
								}
							}
							
						}
					}
				} else {
					
					int differenceOfXs = Math.abs(agentConfiguration.get(i).x - endConfiguration.get(i).x);
					int differenceOfZs = Math.abs(agentConfiguration.get(i).z - endConfiguration.get(i).z);
					
					if(differenceOfZs > differenceOfXs){
						
						if(agentConfiguration.get(i).z - endConfiguration.get(i).z > 0){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(rear)){
									actions.add(rear);
								}
							}
						}
						
						if(endConfiguration.get(i).z - agentConfiguration.get(i).z > 0){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(front)){
									actions.add(front);
								}
							}
						}
					} else {
						
						if(agentConfiguration.get(i).x - endConfiguration.get(i).x > 0){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(left)){
									actions.add(left);
								}
							}
						}
						
						if(endConfiguration.get(i).x - agentConfiguration.get(i).x > 0){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(right)){
									actions.add(right);
								}
							}
							
						}
					}
				}
				
				
			}
		
		possibleActions.add(actions);
		}
		
		return possibleActions;
	}
	
	//WORKS
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
	
	//WORKS
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
	
	//WORKS
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

	//WORKS
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
	
	//WORKS
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
	
	//WORKS
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
	
	//WORKS
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
	
	//WORKS
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
	
	//WORKS
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
	
	//WORKS
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
	
	//WORKS
	private boolean checkBottomObstacle(Vector3i agent){
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if((Math.abs(agent.z - obstacleConfiguration.get(i).z) == 0) && (Math.abs(agent.x - obstacleConfiguration.get(i).x) == 0)){
				
				if(agent.y - obstacleConfiguration.get(i).y == 1){
					return true;
				}
			}
		}
		return false;
		
	}
	
	//WORKS
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
	
	//WORKS
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
	

	
	//WORKS
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

	
	//WORKS
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

	
	//WORKS
	private boolean checkFrontBottom(Vector3i agent){
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if(Math.abs(agentConfiguration.get(i).x - agent.x) == 0){
				
				if((agent.y - agentConfiguration.get(i).y == 1) && (agentConfiguration.get(i).z - agent.z == 1)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//WORKS!!! (small bug in case two agents have to fall down on one another, they remain in the air)
	private void applyGravity(Vector3i agent){
		
		int dummy = -1;
		int index = -1;
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
			
			try{
				
				if(agent.y - obstacleConfiguration.get(index).y > 0){
					agent.y = obstacleConfiguration.get(index).y + 1;
				} 
			} catch(IndexOutOfBoundsException e) {
				
				agent.y = 0;
			}
		}  else {
			
			try{
				
				if(agent.y - agentConfiguration.get(index).y > 0){
					agent.y = agentConfiguration.get(index).y + 1;
				}
			} catch(IndexOutOfBoundsException e) {
				agent.y = 0;
			}
		}
		
	}
	
	//WORKS
	private boolean checkFrontBottomObstacle(Vector3i agent){
		
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			
			if(Math.abs(obstacleConfiguration.get(i).x - agent.x) == 0){
				
				if((agent.y - obstacleConfiguration.get(i).y == 1) && (obstacleConfiguration.get(i).z - agent.z == 1)){
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	//WORKS
	private boolean checkRearBottom(Vector3i agent){
		
		boolean flag = false;
		int index = 0;
		int dummy = 0;
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if(Math.abs(agentConfiguration.get(i).x - agent.x) == 0){
				
				if((agent.y - agentConfiguration.get(i).y == 1) && (agent.z - agentConfiguration.get(i).z == 1)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//WORKS
	private boolean checkRearBottomObstacle(Vector3i agent){
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if(Math.abs(obstacleConfiguration.get(i).x - agent.x) == 0){
				
				if((agent.y - obstacleConfiguration.get(i).y == 1) && (agent.z - obstacleConfiguration.get(i).z == 1)){
					return true;
				}
			}
		}
		
		return false;
	}

	//WORKS
	private boolean checkRightBottom(Vector3i agent){
		
		for(int i=0; i<agentConfiguration.size(); i++){
		
				
				if(Math.abs(agent.z - agentConfiguration.get(i).z) == 0){			
				
					if((agent.y - agentConfiguration.get(i).y == 1) && (agentConfiguration.get(i).x - agent.x == 1)){
						return true;
					}
			
		}
		
	}
	
		return false;
	}
	
	//WORKS
	private boolean checkRightBottomObstacle(Vector3i agent){
		
		int index = 0;
		int dummy = 0;
		boolean flag = false;
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if(Math.abs(agent.z - obstacleConfiguration.get(i).z) == 0){
				
				
				if((agent.y - obstacleConfiguration.get(i).y == 1) && (obstacleConfiguration.get(i).x - agent.x == 1)){
					return true;
				}
			}
		}
		
		return false;
	}

	//WORKS
	private boolean checkLeftBottom(Vector3i agent){
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if(Math.abs(agent.z - agentConfiguration.get(i).z) == 0){
				
				if((agent.x - agentConfiguration.get(i).x == 1) && (agent.y - agentConfiguration.get(i).y == 1)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//WORKS
	private boolean checkLeftBottomObstacle(Vector3i agent){
		
		for(int i=0; i<obstacleConfiguration.size(); i++){
			
			if(Math.abs(agent.z - obstacleConfiguration.get(i).z) == 0){
				
				if((agent.x - obstacleConfiguration.get(i).x == 1) && (agent.y - obstacleConfiguration.get(i).y == 1)){
					return true;
				}
			}
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

	public void setPossibleActions(List<List<Vector3i>> possibleActions) {
		this.possibleActions = possibleActions;
	}

	
	public List<List<Vector3i>> getOptimalActions() {
		return optimalActions;
	}

	public void setOptimalActions(List<List<Vector3i>> optimalActions) {
		this.optimalActions = optimalActions;
	}

	
}
