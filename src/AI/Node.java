package AI;


import org.joml.Vector3i;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.round;

/**
 * Created by Savvas on 6/26/2017.
 */

public class Node implements  PathFindingAlgorithm {

    protected List<Vector3i> agentConfiguration;
    protected List<Vector3i> endConfiguration;
    protected List<Vector3i> obstacleConfiguration;
    protected List<List<Vector3i>> possibleActions;
    protected List<List<Vector3i>> optimalActions;
    protected Action idealAction;

    //getIdealActions should also check for being in bounds of environment the whole list!!!
    public static void main(String[] args){

        //remember that each agent has only one end position!!
        List<Vector3i> endConfiguration = new ArrayList<>();
        endConfiguration.add(new Vector3i(10,0,2));
        endConfiguration.add(new Vector3i(9,0,2));
        endConfiguration.add(new Vector3i(8,0,2));

        List<Vector3i> startConfiguration = new ArrayList<>();
        startConfiguration.add(new Vector3i(7,0,0));
        startConfiguration.add(new Vector3i(9,0,0));
        startConfiguration.add(new Vector3i(8,0,0));

        List<Vector3i> obstacleConfiguration = new ArrayList<>();
        obstacleConfiguration.add(new Vector3i(5,0,1));
        obstacleConfiguration.add(new Vector3i(7,0,1));
        obstacleConfiguration.add(new Vector3i(8,0,1));
        obstacleConfiguration.add(new Vector3i(8,1,1));
        obstacleConfiguration.add(new Vector3i(9,0,1));
        obstacleConfiguration.add(new Vector3i(10,0,1));


        compute(new Vector3i(11,11,11),startConfiguration,endConfiguration,obstacleConfiguration,null);

    }
    public void compute(Vector3f _size, List<Vector3f> _startPosition, List<Vector3f> _endPosition, List<Vector3f> _obstacle, List<List<PathFindingAlgorithm.Movement>> result)
    {
        Vector3i size=new Vector3i(round(_size.x),round(_size.y),round(_size.z));
        List<Vector3i> startPosition = new ArrayList<>(_startPosition.size());
        for(Vector3f vec:_startPosition)
        {
            startPosition.add(new Vector3i(round(vec.x),round(vec.y),round(vec.z)));
        }
        List<Vector3i> endPosition = new ArrayList<>(_endPosition.size());
        for(Vector3f vec:_endPosition)
        {
            endPosition.add(new Vector3i(round(vec.x),round(vec.y),round(vec.z)));
        }
        List<Vector3i> obstacle = new ArrayList<>(_obstacle.size());
        for(Vector3f vec:_obstacle)
        {
            obstacle.add(new Vector3i(round(vec.x),round(vec.y),round(vec.z)));
        }

        compute(size,startPosition,endPosition,obstacle,result);
    }

    public static synchronized void compute(Vector3i size,List<Vector3i> startConfiguration, List<Vector3i> endConfiguration, List<Vector3i> obstacleConfiguration,List<List<PathFindingAlgorithm.Movement>> result)
    {
        System.out.println(startConfiguration);
        List<List<Vector3i>> possibleActions = getAvailableActions(startConfiguration,obstacleConfiguration);
        List<List<Vector3i>> optimalActions = getOptimalMoves(startConfiguration,endConfiguration,possibleActions);
        Action idealAction = findIdealAction(startConfiguration,endConfiguration,obstacleConfiguration,optimalActions,possibleActions);
        Vector3i pos=startConfiguration.get(idealAction.getAgentIndex()).add(idealAction.getAction());
        Movement mov=new Movement(idealAction.getAgentIndex(),new Vector3f(pos.x,pos.y,pos.z));
        result.add(Arrays.asList(mov));
        compute(size,startConfiguration,endConfiguration,obstacleConfiguration,result);
    }

    private static Action findIdealAction(List<Vector3i> agentConfiguration,List<Vector3i> endConfiguration,List<Vector3i> obstacleConfiguration,List<List<Vector3i>> optimalActions,List<List<Vector3i>> possibleActions){

        Action idealAction = null;

        List<Integer> heuristics = new ArrayList<>(); //manhattan heuristic value of each cube

        for(int i=0; i<agentConfiguration.size(); i++){

            int heuristic = Math.abs(agentConfiguration.get(i).x - endConfiguration.get(i).x)
                    + Math.abs(agentConfiguration.get(i).z - endConfiguration.get(i).z)
                    + Math.abs(agentConfiguration.get(i).y - endConfiguration.get(i).y);

            heuristics.add(heuristic);
        }

        int dummyIndex = 0;
        int dummyHeuristic = heuristics.get(0);
        boolean checker = false;

        for(int i=0; i<heuristics.size(); i++){
            ;
            if(heuristics.get(i) > dummyHeuristic){
                dummyHeuristic = heuristics.get(i);
                dummyIndex = i;
                checker = true;
            }
        }

        if(checker){
            //System.out.println("entered 1");
            for(int i=0; i<optimalActions.get(dummyIndex).size(); i++){

                Action testingAction = new Action(optimalActions.get(dummyIndex).get(i), dummyIndex);

                idealAction = simulate(testingAction,agentConfiguration,endConfiguration,obstacleConfiguration);

                if(idealAction == null)
                    continue;
                else
                    return idealAction;
            }
        } else {
            //System.out.println("entered 2");
            int randomIndex = ((int) (Math.random())*agentConfiguration.size());

            for(int i=0; i<optimalActions.get(randomIndex).size(); i++){

                Action testingAction = new Action(optimalActions.get(randomIndex).get(i), randomIndex);

                idealAction = simulate(testingAction,agentConfiguration,endConfiguration,obstacleConfiguration);

                if(idealAction == null){

                    continue;
                }
                else
                    return idealAction;
            }

        }


        if(idealAction == null){
            //System.out.println("entered 3");
            try{

                int randomIndex = (int) (Math.random()*agentConfiguration.size());

                int randomActionIndex = (int) (Math.random()*optimalActions.get(randomIndex).size());

                Action randomAction = new Action(optimalActions.get(randomIndex).get(randomActionIndex), randomIndex);

                return randomAction;

            } catch(IndexOutOfBoundsException e){
                //System.out.println("No possible optimal actions");
                //return null;
            } catch(NullPointerException e){
                //System.out.println("No possible optimal actions");
            }
        }

        while(idealAction == null){
            //System.out.println("entered 4");
            for(int i=0; i<agentConfiguration.size(); i++){

                for(int j=0; j<possibleActions.get(i).size(); j++){

                    Action action = new Action(possibleActions.get(i).get(j), i);

                    return action;
                }
            }
        }

        return idealAction;
    }
    //must check both obstacle and agent cases to work properly!!!
    //WORKS FOR AGENTS!!!
    public static List<List<Vector3i>> getAvailableActions(List<Vector3i> agentConfiguration,List<Vector3i> obstacleConfiguration){

        List<List<Vector3i>> possibleActions = new ArrayList<>();

        for(int i=0; i<agentConfiguration.size(); i++){

            List<Vector3i> actions = new ArrayList<>();

            if(checkTop(agentConfiguration.get(i),agentConfiguration)){
                possibleActions.add(actions);
                continue;
            } else{

                applyGravity(agentConfiguration.get(i),agentConfiguration,obstacleConfiguration);

                if(checkBottom(agentConfiguration.get(i),agentConfiguration)){

                    if(checkFront(agentConfiguration.get(i),agentConfiguration)){

                        if(!checkFrontTop(agentConfiguration.get(i),agentConfiguration)){
                            actions.add(new Vector3i(0,1,1));
                        }

                    } else{


                        if(!checkFrontObstacle(agentConfiguration.get(i),obstacleConfiguration)){

                            if(!checkFrontBottom(agentConfiguration.get(i),agentConfiguration)){

                                if(!checkFrontBottomObstacle(agentConfiguration.get(i),obstacleConfiguration)){
                                    actions.add(new Vector3i(0,-1,1));
                                }
                            } else {
                                actions.add(new Vector3i(0,0,1));
                            }
                        }
                    }

                    if(checkRear(agentConfiguration.get(i),agentConfiguration)){

                        if(!checkRearTop(agentConfiguration.get(i),agentConfiguration)){
                            actions.add(new Vector3i(0,1,-1));
                        }

                    } else {

                        if(!checkRearObstacle(agentConfiguration.get(i),obstacleConfiguration)){

                            if(!checkRearBottom(agentConfiguration.get(i),agentConfiguration)){

                                if(!checkRearBottomObstacle(agentConfiguration.get(i),agentConfiguration)){
                                    actions.add(new Vector3i(0,-1,-1));

                                }
                            } else {
                                actions.add(new Vector3i(0,0,-1));
                            }
                        }
                    }

                    if(checkLeft(agentConfiguration.get(i),agentConfiguration)){

                        if(!checkLeftTop(agentConfiguration.get(i),agentConfiguration)){
                            actions.add(new Vector3i(-1,1,0));
                        }
                    } else {

                        if(!checkLeftObstacle(agentConfiguration.get(i),obstacleConfiguration)){

                            if(!checkLeftBottom(agentConfiguration.get(i),agentConfiguration)){

                                if(!checkLeftBottomObstacle(agentConfiguration.get(i),obstacleConfiguration)){
                                    actions.add(new Vector3i(-1,-1,0));
                                }
                            } else {
                                actions.add(new Vector3i(-1,0,0));
                            }

                        }
                    }

                    if(checkRight(agentConfiguration.get(i),agentConfiguration)){

                        if(!checkRightTop(agentConfiguration.get(i),agentConfiguration)){
                            actions.add(new Vector3i(1,1,0));
                        }
                    } else {

                        if(!checkRightObstacle(agentConfiguration.get(i),agentConfiguration)){

                            if(!checkRightBottom(agentConfiguration.get(i),agentConfiguration)){

                                if(!checkRightBottomObstacle(agentConfiguration.get(i),agentConfiguration)){
                                    actions.add(new Vector3i(1,-1,0));
                                }
                            } else {
                                actions.add(new Vector3i(1,0,0));
                            }
                        }
                    }
                } else {

                    if(!checkBottomObstacle(agentConfiguration.get(i),agentConfiguration)){
                        if(checkFront(agentConfiguration.get(i),agentConfiguration)){

                            if(!checkFrontTop(agentConfiguration.get(i),agentConfiguration)){
                                actions.add(new Vector3i(0,1,1));
                            }
                        }

                        if(checkRear(agentConfiguration.get(i),agentConfiguration)){

                            if(!checkRearTop(agentConfiguration.get(i),agentConfiguration)){
                                actions.add(new Vector3i(0,1,-1));
                            }
                        }

                        if(checkLeft(agentConfiguration.get(i),agentConfiguration)){

                            if(!checkLeftTop(agentConfiguration.get(i),agentConfiguration)){
                                actions.add(new Vector3i(-1,1,0));
                            }
                        }

                        if(checkRight(agentConfiguration.get(i),agentConfiguration)){

                            if(!checkRightTop(agentConfiguration.get(i),agentConfiguration)){
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
    protected static List<List<Vector3i>> getOptimalMoves(List<Vector3i> agentConfiguration,List<Vector3i> endConfiguration,List<List<Vector3i>> possibleActions){

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

                        for(int j=0; j<possibleActions.get(i).size(); j++){

                            if(possibleActions.get(i).get(j).equals(rearDown))
                                moves.add(rearDown);
                            else if(possibleActions.get(i).get(j).equals(rear))
                                moves.add(rear);
                            else if(possibleActions.get(i).get(j).equals(rearUp))
                                moves.add(rearUp);

                        }
                    }
                    else if(agentConfiguration.get(i).y == endConfiguration.get(i).y){

                        for(int j=0; j<possibleActions.get(i).size(); j++){

                            if(possibleActions.get(i).get(j).equals(rear))
                                moves.add(rear);
                            else if(possibleActions.get(i).get(j).equals(rearUp))
                                moves.add(rearUp);
                            else if(possibleActions.get(i).get(j).equals(rearDown))
                                moves.add(rearDown);


                        }
                    } else if(agentConfiguration.get(i).y < endConfiguration.get(i).y){

                        for(int j=0; j<possibleActions.get(i).size(); j++){

                            if(possibleActions.get(i).get(j).equals(rearUp))
                                moves.add(rearUp);
                            else if(possibleActions.get(i).get(j).equals(rear))
                                moves.add(rear);
                            else if(possibleActions.get(i).get(j).equals(rearDown))
                                moves.add(rearDown);
                        }

                    }
                }

                if(agentConfiguration.get(i).z < endConfiguration.get(i).z){

                    if(agentConfiguration.get(i).y > endConfiguration.get(i).y){

                        for(int j=0; j<possibleActions.get(i).size(); j++){

                            if(possibleActions.get(i).get(j).equals(frontDown))
                                moves.add(frontDown);
                            else if(possibleActions.get(i).get(j).equals(front))
                                moves.add(front);
                            else if(possibleActions.get(i).get(j).equals(frontDown))
                                moves.add(frontDown);
                        }
                    }
                    else if(agentConfiguration.get(i).y == endConfiguration.get(i).y){

                        for(int j=0; j<possibleActions.get(i).size(); j++){

                            if(possibleActions.get(i).get(j).equals(front))
                                moves.add(front);
                            else if(possibleActions.get(i).get(j).equals(frontUp))
                                moves.add(frontUp);
                            else if(possibleActions.get(i).get(j).equals(frontDown))
                                moves.add(frontDown);
                            else if(possibleActions.get(i).get(j).equals(leftUp))
                                moves.add(leftUp);
                        }
                    } else if(agentConfiguration.get(i).y < endConfiguration.get(i).y){

                        for(int j=0; j<possibleActions.get(i).size(); j++){

                            if(possibleActions.get(i).get(j).equals(frontUp))
                                moves.add(frontUp);
                            else if(possibleActions.get(i).get(j).equals(front))
                                moves.add(front);
                            else if(possibleActions.get(i).get(j).equals(frontDown))
                                moves.add(frontDown);
                        }

                    }
                }
            }

            if(agentConfiguration.get(i).z == endConfiguration.get(i).z){

                if(agentConfiguration.get(i).x > endConfiguration.get(i).x){

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
                    else if(agentConfiguration.get(i).y == endConfiguration.get(i).y){

                        for(int j=0; j<possibleActions.get(i).size(); j++){

                            if(possibleActions.get(i).get(j).equals(left))
                                moves.add(left);
                            else if(possibleActions.get(i).get(j).equals(leftUp))
                                moves.add(leftUp);
                            else if(possibleActions.get(i).get(j).equals(leftDown))
                                moves.add(leftDown);
                        }
                    } else if(agentConfiguration.get(i).y < endConfiguration.get(i).y){

                        for(int j=0; j<possibleActions.get(i).size(); j++){

                            if(possibleActions.get(i).get(j).equals(leftUp))
                                moves.add(leftUp);
                            else if(possibleActions.get(i).get(j).equals(left))
                                moves.add(left);
                            else if(possibleActions.get(i).get(j).equals(leftDown))
                                moves.add(leftDown);
                        }

                    }
                }

                if(agentConfiguration.get(i).x < endConfiguration.get(i).x){

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
                    else if(agentConfiguration.get(i).y == endConfiguration.get(i).y){

                        for(int j=0; j<possibleActions.get(i).size(); j++){

                            if(possibleActions.get(i).get(j).equals(right))
                                moves.add(right);
                            else if(possibleActions.get(i).get(j).equals(rightUp))
                                moves.add(rightUp);
                            else if(possibleActions.get(i).get(j).equals(rightDown))
                                moves.add(rightDown);
                        }
                    } else if(agentConfiguration.get(i).y < endConfiguration.get(i).y){

                        for(int j=0; j<possibleActions.get(i).size(); j++){

                            if(possibleActions.get(i).get(j).equals(rightUp))
                                moves.add(rightUp);
                            else if(possibleActions.get(i).get(j).equals(right))
                                moves.add(right);
                            else if(possibleActions.get(i).get(j).equals(rightDown))
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
                                else if(possibleActions.get(i).get(j).equals(left))
                                    moves.add(left);
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

                                if(possibleActions.get(i).get(j).equals(rightDown)){

                                    moves.add(rightDown);

                                }
                                else if(possibleActions.get(i).get(j).equals(right)){

                                    moves.add(right);
                                }
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

    private static Action simulate(Action action,List<Vector3i> agentConfiguration ,List<Vector3i> endConfiguration,List<Vector3i> obstacleConfiguration){

        List<Vector3i> dummy = new ArrayList<>();

        for(int i=0; i<agentConfiguration.size(); i++){

            dummy.add(new Vector3i(agentConfiguration.get(i)));
        }

        dummy.get(action.getAgentIndex()).add(action.getAction());

        //SimulationNode simulationState = new SimulationNode(dummy,endConfiguration,obstacleConfiguration);

        if(agentsAreTogetherExt(dummy)){
            return action;
        }

        return null;

    }

    protected static boolean agentsAreTogether(List<Vector3i> agentConfiguration){

        boolean[] checker = new boolean[agentConfiguration.size()];
        boolean finalCheck = true;

        for(int i=0; i<checker.length; i++){

            if(checkBottom(agentConfiguration.get(i),agentConfiguration) && checkFront(agentConfiguration.get(i),agentConfiguration)){
                checker[i] = true;
                continue;
            }

            if(checkBottom(agentConfiguration.get(i),agentConfiguration) && checkRear(agentConfiguration.get(i),agentConfiguration)){
                checker[i] = true;
                continue;
            }

            if(checkBottom(agentConfiguration.get(i),agentConfiguration) && checkLeft(agentConfiguration.get(i),agentConfiguration)){
                checker[i] = true;
                continue;
            }

            if(checkBottom(agentConfiguration.get(i),agentConfiguration) && checkRight(agentConfiguration.get(i),agentConfiguration)){
                checker[i] = true;
                continue;
            }

            if(checkTop(agentConfiguration.get(i),agentConfiguration) && checkRear(agentConfiguration.get(i),agentConfiguration)){
                checker[i] = true;
                continue;
            }

            if(checkTop(agentConfiguration.get(i),agentConfiguration) && checkLeft(agentConfiguration.get(i),agentConfiguration)){
                checker[i] = true;
                continue;
            }

            if(checkTop(agentConfiguration.get(i),agentConfiguration) && checkRight(agentConfiguration.get(i),agentConfiguration)){
                checker[i] = true;
                continue;
            }

            if(checkTop(agentConfiguration.get(i),agentConfiguration) && checkFront(agentConfiguration.get(i),agentConfiguration)){
                checker[i] = true;
                continue;
            }

            if(checkLeft(agentConfiguration.get(i),agentConfiguration) && checkRight(agentConfiguration.get(i),agentConfiguration)){
                checker[i] = true;
                continue;
            }

            if(checkFront(agentConfiguration.get(i),agentConfiguration) && checkRear(agentConfiguration.get(i),agentConfiguration)){
                checker[i] = true;
                continue;
            }

            if(checkLeft(agentConfiguration.get(i),agentConfiguration) && checkRight(agentConfiguration.get(i),agentConfiguration)){
                checker[i] = true;
                continue;
            }

            if(checkLeft(agentConfiguration.get(i),agentConfiguration) && checkFront(agentConfiguration.get(i),agentConfiguration)){
                checker[i] = true;
                continue;
            }

            if(checkLeft(agentConfiguration.get(i),agentConfiguration) && checkRear(agentConfiguration.get(i),agentConfiguration)){
                checker[i] = true;
                continue;
            }

            if(checkRight(agentConfiguration.get(i),agentConfiguration) && checkFront(agentConfiguration.get(i),agentConfiguration)){
                checker[i] = true;
                continue;
            }

            if(checkRight(agentConfiguration.get(i),agentConfiguration) && checkRear(agentConfiguration.get(i),agentConfiguration)){
                checker[i] = true;
                continue;
            }

        }

        for(int i=0; i<checker.length; i++){

            if(checker[i] == false){

                if(checkTop(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkBottom(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkFront(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkRear(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkLeft(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkRight(agentConfiguration.get(i),agentConfiguration)){
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
    private static boolean checkFront(Vector3i agent, List<Vector3i> agentConfiguration){

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
    private static boolean checkFrontObstacle(Vector3i agent,List<Vector3i> obstacleConfiguration){

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
    private static boolean checkRear(Vector3i agent,List<Vector3i> agentConfiguration){

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
    private static boolean checkRearObstacle(Vector3i agent,List<Vector3i> obstacleConfiguration){

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
    private static boolean checkRight(Vector3i agent,List<Vector3i> agentConfiguration){

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
    private static boolean checkRightObstacle(Vector3i agent,List<Vector3i> obstacleConfiguration){

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
    private static boolean checkLeft(Vector3i agent,List<Vector3i> agentConfiguration){

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
    private static boolean checkLeftObstacle(Vector3i agent,List<Vector3i> obstacleConfiguration){

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
    private static boolean checkTop(Vector3i agent,List<Vector3i> agentConfiguration){

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
    private static boolean checkBottom(Vector3i agent,List<Vector3i> agentConfiguration){

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
    private static boolean checkBottomObstacle(Vector3i agent,List<Vector3i> obstacleConfiguration){

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
    private static boolean checkFrontTop(Vector3i agent,List<Vector3i> agentConfiguration){

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
    private static boolean checkRearTop(Vector3i agent,List<Vector3i> agentConfiguration){

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
    private static boolean checkRightTop(Vector3i agent,List<Vector3i> agentConfiguration){

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
    private static boolean checkLeftTop(Vector3i agent,List<Vector3i> agentConfiguration){

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
    private static boolean checkFrontBottom(Vector3i agent,List<Vector3i> agentConfiguration){

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
    private static void applyGravity(Vector3i agent,List<Vector3i> agentConfiguration,List<Vector3i> obstacleConfiguration){

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
    private static boolean checkFrontBottomObstacle(Vector3i agent,List<Vector3i> obstacleConfiguration){


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
    private static boolean checkRearBottom(Vector3i agent, List<Vector3i> agentConfiguration){

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
    private static boolean checkRearBottomObstacle(Vector3i agent, List<Vector3i> obstacleConfiguration){

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
    private static boolean checkRightBottom(Vector3i agent,List<Vector3i> agentConfiguration){

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
    private static boolean checkRightBottomObstacle(Vector3i agent,List<Vector3i> obstacleConfiguration){

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
    private static boolean checkLeftBottom(Vector3i agent,List<Vector3i> agentConfiguration){

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
    private static boolean checkLeftBottomObstacle(Vector3i agent,List<Vector3i> obstacleConfiguration){

        for(int i=0; i<obstacleConfiguration.size(); i++){

            if(Math.abs(agent.z - obstacleConfiguration.get(i).z) == 0){

                if((agent.x - obstacleConfiguration.get(i).x == 1) && (agent.y - obstacleConfiguration.get(i).y == 1)){
                    return true;
                }
            }
        }

        return false;
    }

    //public List<Vector3i> getAgentConfiguration() {return agentConfiguration;}

    //public void setAgentConfiguration(List<Vector3i> agentConfiguration) {this.agentConfiguration = agentConfiguration;}

    //public List<Vector3i> getEndConfiguration() {return endConfiguration;}

    //public void setEndConfiguration(List<Vector3i> endConfiguration) {this.endConfiguration = endConfiguration;}

    //public List<Vector3i> getObstacleConfiguration() {return obstacleConfiguration;}

    //public void setObstacleConfiguration(List<Vector3i> obstacleConfiguration) {this.obstacleConfiguration = obstacleConfiguration;}

    //public List<List<Vector3i>> possibleActions {return possibleActions;}

    //public void setActions(List<List<Vector3i>> actions) {this.possibleActions = actions;}

    //public void setPossibleActions(List<List<Vector3i>> possibleActions) {this.possibleActions = possibleActions;}


    //public List<List<Vector3i>> getOptimalActions() {return optimalActions;}

    //public void setOptimalActions(List<List<Vector3i>> optimalActions) {this.optimalActions = optimalActions;}

    //public Action getIdealAction() {return idealAction;}

    //public void setIdealAction(Action idealAction) {this.idealAction = idealAction;}

    protected static class Action{

        private Vector3i action;
        private int agentIndex;

        public Action(){}

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

        public String toString(){
            return "Action: " + this.getAction() + " Index: " + this.getAgentIndex();
        }

    }
        protected static boolean agentsAreTogetherExt(List<Vector3i> agentConfiguration){

            boolean[] checker = new boolean[agentConfiguration.size()];
            boolean finalCheck = true;

            for(int i=0; i<checker.length; i++){

                if(checkBottom(agentConfiguration.get(i),agentConfiguration) && checkFront(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkBottom(agentConfiguration.get(i),agentConfiguration) && checkRear(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkBottom(agentConfiguration.get(i),agentConfiguration) && checkLeft(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkBottom(agentConfiguration.get(i),agentConfiguration) && checkRight(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkTop(agentConfiguration.get(i),agentConfiguration) && checkRear(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkTop(agentConfiguration.get(i),agentConfiguration) && checkLeft(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkTop(agentConfiguration.get(i),agentConfiguration) && checkRight(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkTop(agentConfiguration.get(i),agentConfiguration) && checkFront(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkLeft(agentConfiguration.get(i),agentConfiguration) && checkRight(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkFront(agentConfiguration.get(i),agentConfiguration) && checkRear(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkLeft(agentConfiguration.get(i),agentConfiguration) && checkRight(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkLeft(agentConfiguration.get(i),agentConfiguration) && checkFront(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkLeft(agentConfiguration.get(i),agentConfiguration) && checkRear(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkRight(agentConfiguration.get(i),agentConfiguration) && checkFront(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

                if(checkRight(agentConfiguration.get(i),agentConfiguration) && checkRear(agentConfiguration.get(i),agentConfiguration)){
                    checker[i] = true;
                    continue;
                }

            }

            for(int i=0; i<checker.length; i++){

                if(checker[i] == false){

                    if(checkTop(agentConfiguration.get(i),agentConfiguration)){
                        checker[i] = true;
                        continue;
                    }

                    if(checkBottom(agentConfiguration.get(i),agentConfiguration)){
                        checker[i] = true;
                        continue;
                    }

                    if(checkFront(agentConfiguration.get(i),agentConfiguration)){
                        checker[i] = true;
                        continue;
                    }

                    if(checkRear(agentConfiguration.get(i),agentConfiguration)){
                        checker[i] = true;
                        continue;
                    }

                    if(checkLeft(agentConfiguration.get(i),agentConfiguration)){
                        checker[i] = true;
                        continue;
                    }

                    if(checkRight(agentConfiguration.get(i),agentConfiguration)){
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

}
