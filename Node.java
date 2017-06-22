package AI;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

public class Node {

    private List<Vector3f> agents;
    private List<Vector3f> endConfiguration;
    private List<List<Vector3f>> actions;
    private double ucb1; //value + sqrt(lnN/n) where N= total visits and n = visits of this node
    private double value; // 1 divided by sum of manhattan heuristic of each agent
    private double visits;
    private static final double TOL = 0.001;
    private List<Node> children;
    private Node parent;

    public Node(List<Vector3f> agents, List<Vector3f> endConfiguration){
        this.agents = agents;
        this.endConfiguration = endConfiguration;
        this.actions = getAvailableActions();
        this.value = calculateValue();
    }

    public Node(Node node){
        this.agents = node.agents;
        this.endConfiguration = node.endConfiguration;
        this.actions = node.actions;
        this.ucb1 = node.ucb1;
        this.visits = node.visits;
        this.children = node.children;
        this.parent = node.parent;
    }

    public double calculateValue(){

        double sumOfHeuristics = 0;

        for(int i=0; i<agents.size(); i++){
            double heuristicX = Math.abs(endConfiguration.get(i).x - agents.get(i).x);
            double heuristicY = Math.abs(endConfiguration.get(i).y - agents.get(i).y);
            double heuristicZ= Math.abs(endConfiguration.get(i).z - agents.get(i).z);

            sumOfHeuristics = sumOfHeuristics + heuristicX + heuristicY + heuristicZ;
        }

        return sumOfHeuristics;
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

                        if(!checkTopFront(agents.get(i))){
                            Vector3f action = new Vector3f(0,1,1);
                            actions.add(action);
                        }

                    } else {

                        if(checkBottomFront(agents.get(i))){
                            Vector3f action = new Vector3f(0,0,1);
                            actions.add(action);
                        }
                    }

                    if(checkRear(agents.get(i))){

                        if(!checkTopRear(agents.get(i))){
                            Vector3f action = new Vector3f(0,1,-1);
                            actions.add(action);
                        }

                    } else {

                        if(checkBottomRear(agents.get(i))){
                            Vector3f action = new Vector3f(0,0,-1);
                            actions.add(action);
                        }

                    }

                    if(checkLeft(agents.get(i))){

                        if(!checkTopLeft(agents.get(i))){
                            Vector3f action = new Vector3f(-1,1,0);
                            actions.add(action);
                        }

                    } else {

                        if(checkBottomLeft(agents.get(i))){
                            Vector3f action = new Vector3f(-1,0,0);
                            actions.add(action);
                        }
                    }


                    if(checkRight(agents.get(i))){

                        if(!checkTopRight(agents.get(i))){
                            Vector3f action = new Vector3f(1,1,0);
                            actions.add(action);
                        }

                    } else {

                        if(checkBottomRight(agents.get(i))){
                            Vector3f action = new Vector3f(1,-1,0);
                            actions.add(action);
                        }
                    }

                } else {

                    if(checkFront(agents.get(i))){

                        if(!checkTopFront(agents.get(i))){
                            Vector3f action = new Vector3f(0,1,1);
                            actions.add(action);
                        }

                    } else {
                        Vector3f action = new Vector3f(0,0,1);
                        actions.add(action);
                    }

                    if(checkRear(agents.get(i))){

                        if(!checkTopRear(agents.get(i))){
                            Vector3f action = new Vector3f(0,0,-1);
                            actions.add(action);
                        }

                    } else {
                        Vector3f action = new Vector3f(0,0,-1);
                        actions.add(action);
                    }

                    if(checkLeft(agents.get(i))){

                        if(!checkTopLeft(agents.get(i))){
                            Vector3f action = new Vector3f(-1,1,0);
                            actions.add(action);
                        }

                    } else {
                        Vector3f action = new Vector3f(-1,0,0);
                        actions.add(action);
                    }

                    if(checkRight(agents.get(i))){

                        if(!checkTopRight(agents.get(i))){
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

    //okay
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

    //okay
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

    //okay
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

    //okay
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

    //okay
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

    //okay
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

    //okay
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

    //okay
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

    //okay
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

    //okay
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

    //okay
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

    //okay
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

    //okay
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

    //okay
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
