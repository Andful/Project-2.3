import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Savvas on 6/14/2017.
 */
public class Node {

    protected List<Vector3f> state; //current state
    protected int visits; //Number of visits to this node
    protected double value; //Score
    protected double ucb1;
    protected List<Node> children;
    protected Node parent;
    protected List<List<Vector3f>> actions;

    public Node(Node node){
        this.state = node.state;
        this.visits = node.visits;
        this.value = node.value;
        this.ucb1 = node.ucb1;
        this.children = node.children;
        this.parent = node.parent;
        this.actions = node.actions;
    }

    public Node(List<Vector3f> state){
        this.state = state;
        actions = getPossibleActions();
        this.children = new LinkedList<>();
    }

    public Node(List<Vector3f> state, List<List<Vector3f>> actions){
        this.state = state;
        //actions = getAvailableActions(); TO DO
        this.actions = actions;
        this.children = new LinkedList<>();
    }

    public void set(Node node){
        this.state = node.state;
        this.visits = node.visits;
        this.value = node.value;
        this.ucb1 = node.ucb1;
        this.children = node.children;
        this.parent = node.parent;
        this.actions = node.actions;
    }

    //returns the state after an action has been applied


    //Returns the new state after an action is taken
    //Action is defined as the simultaneous movement of the agents
    //Needs to be checked so that they are no clashing!
    public Node takeAction(){
        return null;
    }


    public Node(List<Vector3f> state, Node parent){
        this.state = state;
        this.parent = parent;
    }

    public Node(List<Vector3f> state, Node parent, List<Node> children){
        this.state = state;
        this.parent = parent;
        this.children = children;
    }

    public List<List<Vector3f>> getPossibleActions() {

        //each position in the list represents a list of possible actions for the agent in position with same index of agents list
        List<List<Vector3f>> actions = new ArrayList<>();

        for(int i=0; i<state.size(); i++){
            boolean[] possibilities = new boolean[6];
            possibilities[0] = top(state.get(i));
            possibilities[1] = bottom(state.get(i));
            possibilities[2] = front(state.get(i));
            possibilities[3] = rear(state.get(i));
            possibilities[4] = left(state.get(i));
            possibilities[5] = right(state.get(i));



            if (possibilities[0] == true){
                actions.add(new ArrayList<>());
                continue;
            }

            List<Vector3f> possibleMovements = new ArrayList<>();
            if(possibilities[2]==false)
                possibleMovements.add(new Vector3f(0,0,1));
            if(possibilities[3] == false)
                possibleMovements.add(new Vector3f(0,0,-1));
            if(possibilities[4] == false)
                possibleMovements.add(new Vector3f(-1,0,0));
            if(possibilities[5] == false)
                possibleMovements.add(new Vector3f(1,0,0));
            //CAN TAKE MORE POSSIBLE ACTIONS

            actions.add(possibleMovements);

        }

        return actions;
    }

    private boolean top(Vector3f currentAgent){

        for(int i=0; i<state.size(); i++){
            if(state.get(i).y - currentAgent.y == 1){
                return true;
            }
        }

        return false;
    }

    private boolean bottom(Vector3f currentAgent){

        for(int i=0; i<state.size(); i++){
            if(currentAgent.y - state.get(i).y == 1){
                return true;
            }
        }

        return false;
    }

    private boolean front(Vector3f currentAgent){

        for(int i=0; i<state.size(); i++){
            if(state.get(i).z - currentAgent.z == 1){
                return true;
            }
        }

        return false;
    }

    private boolean rear(Vector3f currentAgent){

        for(int i=0; i<state.size(); i++){
            if(currentAgent.z - state.get(i).z == 1){
                return true;
            }
        }

        return false;
    }

    private boolean left(Vector3f currentAgent){

        for(int i=0; i<state.size(); i++){
            if(currentAgent.y - state.get(i).y == 1){
                return true;
            }
        }

        return false;
    }

    private boolean right(Vector3f currentAgent){

        for(int i=0; i<state.size(); i++){
            if(state.get(i).y - currentAgent.y == 1){
                return true;
            }
        }

        return false;
    }



    //calculates and returns the ucb1 value
    public double getUCB1(int totalVisits){

        if(totalVisits == 0){
            this.ucb1 = Integer.MAX_VALUE;
            return ucb1;
        }

        this.ucb1 = value + Math.sqrt(Math.log(totalVisits)/visits);

        return ucb1;
    }

    public List<Vector3f> getState(){
        return state;
    }

    public void setState(List<Vector3f> state){
        this.state = state;
    }

    public void setParent(Node parent){
        this.parent = parent;
    }

    public void setChildren(List<Node> children){
        this.children = children;
    }

    public List<Node> getChildren(){
        return children;
    }

    public int getVisits(){
        return visits;
    }

    public void setVisits(int visits){
        this.visits = visits;
    }

    public double getValue(){
        return value;
    }

    public void setValue(double value){
        this.value = value;
    }

    public String toString(){
        return state.toString();
    }

    public void setActions(List<List<Vector3f>> actions){
        this.actions = actions;
    }
}
