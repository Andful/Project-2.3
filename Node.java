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
    protected boolean terminal; //true if the node is a terminal state
    protected List<Vector3f> actions;

    public Node(Node node){
        this.state = node.state;
        this.visits = node.visits;
        this.value = node.value;
        this.ucb1 = node.ucb1;
        this.children = node.children;
        this.parent = node.parent;
        this.terminal = node.terminal;
        this.actions = node.actions;
    }

    public Node(List<Vector3f> state){
        this.state = state;
        //actions = getAvailableActions(); TO DO
        this.children = new LinkedList<>();
    }

    public Node(List<Vector3f> state, List<Vector3f> actions){
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
        this.terminal = node.terminal;
        this.actions = node.actions;
    }

    //returns the state after an action has been applied
    public Node takeAction(Vector3f action){

        List<Vector3f> nState = new ArrayList<>(state);
        for(int i=0; i<state.size(); i++){
            nState.get(i).add(action);
        }

        Node newState = new Node(nState);

        return newState;
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

    public void getAvailableActions() { //TO DO
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

    public void setTerminal(boolean terminal){
        this.terminal = terminal;
    }

    public void setActions(List<Vector3f> actions){
        this.actions = actions;
    }
}
