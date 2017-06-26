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
	private Action idealAction;
	
	public static void main(String[] args){
		
        List<Vector3i> endConfiguration = new ArrayList<>();
        endConfiguration.add(new Vector3i(2,0,2));
        endConfiguration.add(new Vector3i(3,0,2));
        endConfiguration.add(new Vector3i(3,1,2));
		
		List<Vector3i> startConfiguration = new ArrayList<>();
		startConfiguration.add(new Vector3i(0,0,0));
		startConfiguration.add(new Vector3i(0,0,3));
		startConfiguration.add(new Vector3i(1,0,0));
		
        List<Vector3i> obstacleConfiguration = new ArrayList<>();
        
        Node firstState = new Node(startConfiguration,endConfiguration,obstacleConfiguration);
        
        
	}
	
	public Node(List<Vector3i> startConfiguration, List<Vector3i> endConfiguration, List<Vector3i> obstacleConfiguration){
		this.agentConfiguration = startConfiguration;
		this.endConfiguration = endConfiguration;
		this.obstacleConfiguration = obstacleConfiguration;
		this.possibleActions = getAvailableActions();
		this.optimalActions = getOptimalMoves();
	}
	
	public Node(Node node){
		this.agentConfiguration = node.getAgentConfiguration();
		this.endConfiguration = node.getEndConfiguration();
		this.obstacleConfiguration = node.getObstacleConfiguration();
		this.possibleActions = node.getActions();
		this.optimalActions = node.getOptimalActions();
	}
	
	private Action findIdealAction(){
		
		Action idealAction = null;
		
		List<Integer> heuristics = new ArrayList<>(); //manhattan heuristic value of each cube
		
		for(int i=0; i<agentConfiguration.size(); i++){
		
			int heuristic = Math.abs(agentConfiguration.get(i).x - endConfiguration.get(i).x)
					+ Math.abs(agentConfiguration.get(i).z - endConfiguration.get(i).z)
					+ Math.abs(agentConfiguration.get(i).y - endConfiguration.get(i).y);
			
			heuristics.add(heuristic);
		}
		
		int dummyIndex = 0;
		int dummyHeuristic = 0;
		boolean checker = false;
		
		for(int i=0; i<heuristics.size(); i++){
			
			if(heuristics.get(i) > dummyHeuristic){
				dummyHeuristic = heuristics.get(i);
				dummyIndex = i;
				checker = true;
			}
		}
		
		if(checker){
			
			for(int i=0; i<optimalActions.get(dummyIndex).size(); i++){
				
				Action testingAction = new Action(optimalActions.get(dummyIndex).get(i), dummyIndex);
				
				idealAction = simulate(testingAction);
				
				if(idealAction == null)
					continue;
				else
					return idealAction;
			}
		} else {
			
			int randomIndex = (int) (Math.random()*agentConfiguration.size());
			
			for(int i=0; i<optimalActions.get(randomIndex).size(); i++){
				
				Action testingAction = new Action(optimalActions.get(randomIndex).get(i), randomIndex);
				
				idealAction = simulate(testingAction);
				
				if(idealAction == null)
					continue;
				else
					return idealAction;
			}
			
		}
		
		if(idealAction == null){
			
			try{
				
				int randomIndex = (int) (Math.random()*agentConfiguration.size());
				int randomActionIndex = (int) (Math.random()*optimalActions.get(randomIndex).size());
				
				Action randomAction = new Action(optimalActions.get(randomIndex).get(randomActionIndex), randomIndex);
				
				idealAction.set(randomAction);
			} catch(IndexOutOfBoundsException e){
				System.out.println("No possible optimal actions");
				return null;
			}
		}
		
		return idealAction;
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

	//WORKS FOR DIAGONAL THUS FAR
	private List<List<Vector3i>> getOptimalMoves(){
		
		Vector3i rearDown = new Vector3i(0,-1,-1);
		Vector3i rear = new Vector3i(0,0,-1);
		Vector3i rearUp = new Vector3i(0,1,-1);
		Vector3i frontDown = new Vector3i(0,-1,1);
		Vector3i front = new Vector3i(0,0,1);
		Vector3i frontUp = new Vector3i(0,1,1);
		Vector3i leftDown = new Vector3i(-1,-1,0);
		Vector3i left = new Vector3i(-1,0,0);
		Vector3i leftUp = new Vector3i(-1,1,0);
		Vector3i rightDown = new Vector3i(1,-1,0);
		Vector3i right = new Vector3i(1,0,0);
		Vector3i rightUp = new Vector3i(1,1,0);
		
		List<List<Vector3i>> optimalMoves = new ArrayList<>();
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			List<Vector3i> moves = new ArrayList<>();
			
			if(agentConfiguration.get(i).x == endConfiguration.get(i).x){
				
				if(agentConfiguration.get(i).z > endConfiguration.get(i).z){
					
					if(agentConfiguration.get(i).y > endConfiguration.get(i).y){
						
						for(int j=0; j<getActions().get(i).size(); j++){
							
							if(getActions().get(i).get(j).equals(rearDown))
								moves.add(rearDown);
							else if(getActions().get(i).get(j).equals(rear))
								moves.add(rear);
							else if(getActions().get(i).get(j).equals(rearUp))
								moves.add(rearUp);

						}
					}
					else if(agentConfiguration.get(i).y == endConfiguration.get(i).y){
						
						for(int j=0; j<getActions().get(i).size(); j++){
							
							if(getActions().get(i).get(j).equals(rear))
								moves.add(rear);
							else if(getActions().get(i).get(j).equals(rearUp))
								moves.add(rearUp);
							else if(getActions().get(i).get(j).equals(rearDown))
								moves.add(rearDown);
						}
					} else if(agentConfiguration.get(i).y < endConfiguration.get(i).y){
						
						for(int j=0; j<getActions().get(i).size(); j++){
							
							if(getActions().get(i).get(j).equals(rearUp))
								moves.add(rearUp);
							else if(getActions().get(i).get(j).equals(rear))
								moves.add(rear);
							else if(getActions().get(i).get(j).equals(rearDown))
								moves.add(rearDown);
						}
						
					}
				}
				
				if(agentConfiguration.get(i).z < endConfiguration.get(i).z){
					
					if(agentConfiguration.get(i).y > endConfiguration.get(i).y){
						
						for(int j=0; j<getActions().get(i).size(); j++){
							
							if(getActions().get(i).get(j).equals(frontDown))
								moves.add(frontDown);
							else if(getActions().get(i).get(j).equals(front))
								moves.add(front);
							else if(getActions().get(i).get(j).equals(frontDown))
								moves.add(frontDown);
						}
					}
					else if(agentConfiguration.get(i).y == endConfiguration.get(i).y){
						
						for(int j=0; j<getActions().get(i).size(); j++){
							
							if(getActions().get(i).get(j).equals(front))
								moves.add(front);
							else if(getActions().get(i).get(j).equals(frontUp))
								moves.add(frontUp);
							else if(getActions().get(i).get(j).equals(frontDown))
								moves.add(frontDown);
						}
					} else if(agentConfiguration.get(i).y < endConfiguration.get(i).y){
						
						for(int j=0; j<getActions().get(i).size(); j++){
							
							if(getActions().get(i).get(j).equals(frontUp))
								moves.add(frontUp);
							else if(getActions().get(i).get(j).equals(front))
								moves.add(front);
							else if(getActions().get(i).get(j).equals(frontDown))
								moves.add(frontDown);
						}
						
					}
				}
			}
			
			if(agentConfiguration.get(i).z == endConfiguration.get(i).z){
				
				if(agentConfiguration.get(i).x > endConfiguration.get(i).x){
					
					if(agentConfiguration.get(i).y > endConfiguration.get(i).y){
						
						for(int j=0; j<getActions().get(i).size(); j++){
							
							if(getActions().get(i).get(j).equals(leftDown))
								moves.add(leftDown);
							else if(getActions().get(i).get(j).equals(left))
								moves.add(left);
							else if(getActions().get(i).get(j).equals(leftUp))
								moves.add(leftUp);
						}
					}
					else if(agentConfiguration.get(i).y == endConfiguration.get(i).y){
						
						for(int j=0; j<getActions().get(i).size(); j++){
							
							if(getActions().get(i).get(j).equals(left))
								moves.add(left);
							else if(getActions().get(i).get(j).equals(leftUp))
								moves.add(leftUp);
							else if(getActions().get(i).get(j).equals(leftDown))
								moves.add(leftDown);
						}
					} else if(agentConfiguration.get(i).y < endConfiguration.get(i).y){
						
						for(int j=0; j<getActions().get(i).size(); j++){
							
							if(getActions().get(i).get(j).equals(leftUp))
								moves.add(leftUp);
							else if(getActions().get(i).get(j).equals(left))
								moves.add(left);
							else if(getActions().get(i).get(j).equals(leftDown))
								moves.add(leftDown);
						}
						
					}
				}
				
				if(agentConfiguration.get(i).x < endConfiguration.get(i).x){
					
					if(agentConfiguration.get(i).y > endConfiguration.get(i).y){
						
						for(int j=0; j<getActions().get(i).size(); j++){
							
							if(getActions().get(i).get(j).equals(rightDown))
								moves.add(rightDown);
							else if(getActions().get(i).get(j).equals(right))
								moves.add(right);
							else if(getActions().get(i).get(j).equals(rightUp))
								moves.add(rightUp);
						}
					}
					else if(agentConfiguration.get(i).y == endConfiguration.get(i).y){
						
						for(int j=0; j<getActions().get(i).size(); j++){
							
							if(getActions().get(i).get(j).equals(right))
								moves.add(right);
							else if(getActions().get(i).get(j).equals(rightUp))
								moves.add(rightUp);
							else if(getActions().get(i).get(j).equals(rightDown))
								moves.add(rightDown);
						}
					} else if(agentConfiguration.get(i).y < endConfiguration.get(i).y){
						
						for(int j=0; j<getActions().get(i).size(); j++){
							
							if(getActions().get(i).get(j).equals(rightUp))
								moves.add(rightUp);
							else if(getActions().get(i).get(j).equals(right))
								moves.add(right);
							else if(getActions().get(i).get(j).equals(rightDown))
								moves.add(rightDown);
						}
						
					}
				}
			}
			
			if((agentConfiguration.get(i).z != endConfiguration.get(i).z) || (agentConfiguration.get(i).x != agentConfiguration.get(i).x)){
				
				if((agentConfiguration.get(i).x < endConfiguration.get(i).x) && (agentConfiguration.get(i).z < endConfiguration.get(i).z)){
					
					int diffX = Math.abs(agentConfiguration.get(i).x - endConfiguration.get(i).x);
					int diffZ = Math.abs(agentConfiguration.get(i).z - endConfiguration.get(i).z);
					
					if(diffZ > diffX){
						
						if(agentConfiguration.get(i).y > endConfiguration.get(i).y){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(frontDown))
									moves.add(frontDown);
								else if(possibleActions.get(i).get(j).equals(front))
									moves.add(front);
								else if(possibleActions.get(i).get(j).equals(frontUp))
									moves.add(frontUp);
							}
						}
						else if(agentConfiguration.get(i).y < endConfiguration.get(i).y){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(frontUp))
									moves.add(frontUp);
								else if(possibleActions.get(i).get(j).equals(front))
									moves.add(front);
								else if(possibleActions.get(i).get(j).equals(frontDown))
									moves.add(frontDown);
							}
							
						} else {
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(front))
									moves.add(front);
								else if(possibleActions.get(i).get(j).equals(frontUp))
									moves.add(frontUp);
								else if(possibleActions.get(i).get(j).equals(frontDown))
									moves.add(frontDown);
							}
						}
					}
					else if(diffX >= diffZ){
						
						if(agentConfiguration.get(i).y > endConfiguration.get(i).y){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(rightDown))
									moves.add(rightDown);
								else if(possibleActions.get(i).get(j).equals(right))
									moves.add(right);
								else if(possibleActions.get(i).get(j).equals(rightUp))
									moves.add(rightUp);
							}
						}
						else if(agentConfiguration.get(i).y < endConfiguration.get(i).y){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(rightUp))
									moves.add(rightUp);
								else if(possibleActions.get(i).get(j).equals(right))
									moves.add(right);
								else if(possibleActions.get(i).get(j).equals(rightDown))
									moves.add(rightDown);
								else if(possibleActions.get(i).get(j).equals(frontUp)) //DELETE THIS IF IT MESSES THE REST!
									moves.add(frontUp);
							}
						} else {
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(right))
									moves.add(right);
								else if(possibleActions.get(i).get(j).equals(rightUp))
									moves.add(rightUp);
								else if(possibleActions.get(i).get(j).equals(rightDown))
									moves.add(rightDown);
							}
						}
					}
				}
				
				else if((agentConfiguration.get(i).x < endConfiguration.get(i).x) && (agentConfiguration.get(i).z > endConfiguration.get(i).z)){
					
					int diffX = Math.abs(agentConfiguration.get(i).x - endConfiguration.get(i).x);
					int diffZ = Math.abs(agentConfiguration.get(i).z - endConfiguration.get(i).z);
					
					if(diffZ > diffX){
						
						if(agentConfiguration.get(i).y > endConfiguration.get(i).y){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(rearDown))
									moves.add(rearDown);
								else if(possibleActions.get(i).get(j).equals(rear))
									moves.add(rear);
								else if(possibleActions.get(i).get(j).equals(rearUp))
									moves.add(rearUp);
							}
						}
						else if(agentConfiguration.get(i).y < endConfiguration.get(i).y){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(rearUp))
									moves.add(rearUp);
								else if(possibleActions.get(i).get(j).equals(rear))
									moves.add(rear);
								else if(possibleActions.get(i).get(j).equals(rearDown))
									moves.add(rearDown);
							}
							
						} else {
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(rear))
									moves.add(rear);
								else if(possibleActions.get(i).get(j).equals(rearUp))
									moves.add(rearUp);
								else if(possibleActions.get(i).get(j).equals(rearDown))
									moves.add(rearDown);
							}
						}
					}
					else if(diffX >= diffZ){
						
						if(agentConfiguration.get(i).y > endConfiguration.get(i).y){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(rightDown))
									moves.add(rightDown);
								else if(possibleActions.get(i).get(j).equals(right))
									moves.add(right);
								else if(possibleActions.get(i).get(j).equals(rightUp))
									moves.add(rightUp);
							}
						}
						else if(agentConfiguration.get(i).y < endConfiguration.get(i).y){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(rightUp))
									moves.add(rightUp);
								else if(possibleActions.get(i).get(j).equals(right))
									moves.add(right);
								else if(possibleActions.get(i).get(j).equals(rightDown))
									moves.add(rightDown);
							}
						} else {
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(right))
									moves.add(right);
								else if(possibleActions.get(i).get(j).equals(rightUp))
									moves.add(rightUp);
								else if(possibleActions.get(i).get(j).equals(rightDown))
									moves.add(rightDown);
							}
						}
					}
				}
				
				else if((agentConfiguration.get(i).x > endConfiguration.get(i).x) && (agentConfiguration.get(i).z < endConfiguration.get(i).z)){
					
					int diffX = Math.abs(agentConfiguration.get(i).x - endConfiguration.get(i).x);
					int diffZ = Math.abs(agentConfiguration.get(i).z - endConfiguration.get(i).z);
					
					if(diffZ > diffX){
						
						if(agentConfiguration.get(i).y > endConfiguration.get(i).y){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(frontDown))
									moves.add(frontDown);
								else if(possibleActions.get(i).get(j).equals(front))
									moves.add(front);
								else if(possibleActions.get(i).get(j).equals(frontUp))
									moves.add(frontUp);
							}
						}
						else if(agentConfiguration.get(i).y < endConfiguration.get(i).y){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(frontUp))
									moves.add(frontUp);
								else if(possibleActions.get(i).get(j).equals(front))
									moves.add(front);
								else if(possibleActions.get(i).get(j).equals(frontDown))
									moves.add(frontDown);
							}
							
						} else {
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(front))
									moves.add(front);
								else if(possibleActions.get(i).get(j).equals(frontUp))
									moves.add(frontUp);
								else if(possibleActions.get(i).get(j).equals(frontDown))
									moves.add(frontDown);
							}
						}
					}
					else if(diffX >= diffZ){
						
						if(agentConfiguration.get(i).y > endConfiguration.get(i).y){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(leftDown))
									moves.add(leftDown);
								else if(possibleActions.get(i).get(j).equals(left))
									moves.add(left);
								else if(possibleActions.get(i).get(j).equals(leftUp))
									moves.add(leftUp);
							}
						}
						else if(agentConfiguration.get(i).y < endConfiguration.get(i).y){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(leftUp))
									moves.add(leftUp);
								else if(possibleActions.get(i).get(j).equals(left))
									moves.add(left);
								else if(possibleActions.get(i).get(j).equals(leftDown))
									moves.add(leftDown);
							}
						} else {
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(left))
									moves.add(left);
								else if(possibleActions.get(i).get(j).equals(leftUp))
									moves.add(leftUp);
								else if(possibleActions.get(i).get(j).equals(leftDown))
									moves.add(leftDown);
							}
						}
					}
				}
				
				else if((agentConfiguration.get(i).x > endConfiguration.get(i).x) && (agentConfiguration.get(i).z > endConfiguration.get(i).z)){
					
					int diffX = Math.abs(agentConfiguration.get(i).x - endConfiguration.get(i).x);
					int diffZ = Math.abs(agentConfiguration.get(i).z - endConfiguration.get(i).z);
					
					if(diffZ > diffX){
						
						if(agentConfiguration.get(i).y > endConfiguration.get(i).y){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(rearDown))
									moves.add(rearDown);
								else if(possibleActions.get(i).get(j).equals(rear))
									moves.add(rear);
								else if(possibleActions.get(i).get(j).equals(rearUp))
									moves.add(rearUp);
							}
						}
						else if(agentConfiguration.get(i).y < endConfiguration.get(i).y){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(rearUp))
									moves.add(rearUp);
								else if(possibleActions.get(i).get(j).equals(rear))
									moves.add(rear);
								else if(possibleActions.get(i).get(j).equals(rearDown))
									moves.add(rearDown);
							}
							
						} else {
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(rear))
									moves.add(rear);
								else if(possibleActions.get(i).get(j).equals(rearUp))
									moves.add(rearUp);
								else if(possibleActions.get(i).get(j).equals(rearDown))
									moves.add(rearDown);
							}
						}
					}
					else if(diffX >= diffZ){
						
						if(agentConfiguration.get(i).y > endConfiguration.get(i).y){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(leftDown))
									moves.add(leftDown);
								else if(possibleActions.get(i).get(j).equals(left))
									moves.add(left);
								else if(possibleActions.get(i).get(j).equals(leftUp))
									moves.add(leftUp);
							}
						}
						else if(agentConfiguration.get(i).y < endConfiguration.get(i).y){
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(leftUp))
									moves.add(leftUp);
								else if(possibleActions.get(i).get(j).equals(left))
									moves.add(left);
								else if(possibleActions.get(i).get(j).equals(leftDown))
									moves.add(leftDown);
							}
						} else {
							
							for(int j=0; j<possibleActions.get(i).size(); j++){
								
								if(possibleActions.get(i).get(j).equals(left))
									moves.add(left);
								else if(possibleActions.get(i).get(j).equals(leftUp))
									moves.add(leftUp);
								else if(possibleActions.get(i).get(j).equals(leftDown))
									moves.add(leftDown);
							}
						}
					}
				}
			}
		optimalMoves.add(moves);
		}
		return optimalMoves;
	}
	
	private Action simulate(Action action){
		
		List<Vector3i> dummy = new ArrayList<>();
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			dummy.add(agentConfiguration.get(i));
		}
		
		dummy.get(action.getAgentIndex()).add(action.getAction());
		
		Node simulationState = new Node(dummy,endConfiguration,obstacleConfiguration);
		
		if(simulationState.agentsAreTogether()){
			return action;
		}
		
		return null;
		
	}
	
	private boolean agentsAreTogether(){
		
		boolean[] checker = new boolean[agentConfiguration.size()];
		boolean finalCheck = true;
		
		for(int i=0; i<agentConfiguration.size(); i++){
			
			if(checkBottom(agentConfiguration.get(i)) && checkFront(agentConfiguration.get(i))){
				checker[i] = true;
				continue;
			}
			
			if(checkBottom(agentConfiguration.get(i)) && checkRear(agentConfiguration.get(i))){
				checker[i] = true;
				continue;
			}
			
			if(checkBottom(agentConfiguration.get(i)) && checkLeft(agentConfiguration.get(i))){
				checker[i] = true;
				continue;
			}
				
			if(checkBottom(agentConfiguration.get(i)) && checkRight(agentConfiguration.get(i))){
				checker[i] = true;
				continue;
			}
			
			if(checkTop(agentConfiguration.get(i)) && checkRear(agentConfiguration.get(i))){
				checker[i] = true;
				continue;
			}
			
			if(checkTop(agentConfiguration.get(i)) && checkLeft(agentConfiguration.get(i))){
				checker[i] = true;
				continue;
			}
			
			if(checkTop(agentConfiguration.get(i)) && checkRight(agentConfiguration.get(i))){
				checker[i] = true;
				continue;
			}
			
			if(checkTop(agentConfiguration.get(i)) && checkFront(agentConfiguration.get(i))){
				checker[i] = true;
				continue;
			}
			
			if(checkLeft(agentConfiguration.get(i)) && checkRight(agentConfiguration.get(i))){
				checker[i] = true;
				continue;
			}
			
			if(checkFront(agentConfiguration.get(i)) && checkRear(agentConfiguration.get(i))){
				checker[i] = true;
				continue;
			}
			
			if(checkLeft(agentConfiguration.get(i)) && checkRight(agentConfiguration.get(i))){
				checker[i] = true;
				continue;
			}
			
			if(checkLeft(agentConfiguration.get(i)) && checkFront(agentConfiguration.get(i))){
				checker[i] = true;
				continue;
			}
			
			if(checkLeft(agentConfiguration.get(i)) && checkRear(agentConfiguration.get(i))){
				checker[i] = true;
				continue;
			}
			
			if(checkRight(agentConfiguration.get(i)) && checkFront(agentConfiguration.get(i))){
				checker[i] = true;
				continue;
			}
			
			if(checkRight(agentConfiguration.get(i)) && checkRear(agentConfiguration.get(i))){
				checker[i] = true;
				continue;
			}
			
		}
		
		for(int i=0; i<checker.length; i++){
			
			if(checker[i] == false){
				
				if(checkTop(agentConfiguration.get(i))){
					checker[i] = true;
					continue;
				}
				
				if(checkBottom(agentConfiguration.get(i))){
					checker[i] = true;
					continue;
				}
				
				if(checkFront(agentConfiguration.get(i))){
					checker[i] = true;
					continue;
				}
				
				if(checkRear(agentConfiguration.get(i))){
					checker[i] = true;
					continue;
				}
				
				if(checkLeft(agentConfiguration.get(i))){
					checker[i] = true;
					continue;
				}
				
				if(checkRight(agentConfiguration.get(i))){
					checker[i] = true;
					continue;
				}
			}
		}
		
		for(int i=0; i<checker.length; i++){
			
			if(checker[i] == false){
				finalCheck = false;
			}
		}
		
		return finalCheck;
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

	public Action getIdealAction() {
		return idealAction;
	}

	public void setIdealAction(Action idealAction) {
		this.idealAction = idealAction;
	}



	private class Action{
		
		private Vector3i action;
		private int agentIndex;
		
		public Action(Vector3i action, int agentIndex){
			this.action = action;
			this.agentIndex = agentIndex;
		}

		public Vector3i getAction() {
			return action;
		}

		public void setAction(Vector3i action) {
			this.action = action;
		}

		public int getAgentIndex() {
			return agentIndex;
		}

		public void setAgentIndex(int agentIndex) {
			this.agentIndex = agentIndex;
		}
		
		public void set(Action a){
			this.action = a.getAction();
			this.agentIndex = a.getAgentIndex();
		}
		
	}
	
}
