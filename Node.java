package src.AI;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Savvas on 6/22/2017.
 */
public class Node {

    public List<Vector3f> startConfiguration;
    public List<Vector3f> endConfiguration;
    public List<List<Vector3f>> actions;
    public double value; //1/heuristic
    public static final double TOL = 0.0001;

    public Node(List<Vector3f> startConfiguration, List<Vector3f> endConfiguration){
        this.startConfiguration = startConfiguration;
        this.endConfiguration = endConfiguration;
        this.actions = getAvailableActions();
        this.value = calculateValue();
    }

    private List<List<Vector3f>> getAvailableActions(){

        List<List<Vector3f>> availableActions = new ArrayList<>();

        for(int i=0; i<startConfiguration.size(); i++){
            if(!checkFront(startConfiguration.get(i))){
                availableActions.get(i).add(new Vector3f(0,0,1));
            }
            if(!checkRear(startConfiguration.get(i))){
                availableActions.get(i).add(new Vector3f(0,0,-1));
            }
            if(!checkRight(startConfiguration.get(i))){
                availableActions.get(i).add(new Vector3f(1,0,1));
            }
            if(!checkLeft(startConfiguration.get(i))){
                availableActions.get(i).add(new Vector3f(-1,0,0));
            }
        }

        return availableActions;
    }

    private double calculateValue(){

        double heuristicValue = Math.abs(startConfiguration.get(0).x - endConfiguration.get(0).x)
                + Math.abs(startConfiguration.get(0).y - endConfiguration.get(0).y)
                + Math.abs(startConfiguration.get(0).z - endConfiguration.get(0).z);

        return 1/heuristicValue;
    }

    private boolean checkFront(Vector3f agent){

        for(int i=0; i<startConfiguration.size(); i++){

            if((Math.abs((agent.x - startConfiguration.get(i).x)) < TOL) && (Math.abs(agent.y - startConfiguration.get(i).y)<TOL)){

                if(((startConfiguration.get(i).z - agent.z)> 1.0f - TOL) && ((startConfiguration.get(i).z - agent.z)< 1.0f - TOL)){
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkRear(Vector3f agent){

        for(int i=0; i<startConfiguration.size(); i++){

            if((Math.abs((agent.x - startConfiguration.get(i).x)) < TOL) && (Math.abs(agent.y - startConfiguration.get(i).y)<TOL)){

                if(((agent.z - startConfiguration.get(i).z)> 1.0f - TOL) && ((agent.z - startConfiguration.get(i).z)< 1.0f - TOL)){
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkRight(Vector3f agent){

        for(int i=0; i<startConfiguration.size(); i++){

            if((Math.abs((agent.z - startConfiguration.get(i).z)) < TOL) && (Math.abs(agent.y - startConfiguration.get(i).y)<TOL)){

                if(((startConfiguration.get(i).x - agent.x)> 1.0f - TOL) && ((startConfiguration.get(i).x - agent.x)< 1.0f - TOL)){
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkLeft(Vector3f agent){

        for(int i=0; i<startConfiguration.size(); i++){

            if((Math.abs((agent.z - startConfiguration.get(i).z)) < TOL) && (Math.abs(agent.y - startConfiguration.get(i).y)<TOL)){

                if(((agent.x - startConfiguration.get(i).x)> 1.0f - TOL) && ((agent.x - startConfiguration.get(i).x)< 1.0f - TOL)){
                    return true;
                }
            }
        }

        return false;
    }
}
