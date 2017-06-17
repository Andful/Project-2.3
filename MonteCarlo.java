import java.util.ArrayList;
import java.util.List;

/**
 * Created by Savvas on 6/14/2017.
 */
public class MonteCarlo<E> {

    protected Node current;
    protected Node initialState; //root
    protected Node terminalState;

    public MonteCarlo(Node initialState,Node terminalState){
        this.initialState = initialState;
        this.terminalState = terminalState;
    }

    /*
    public void treeSearch(){

        current.set(initialState);

        if(isLeafNode(current)){

            if(current.visits==0){
                //rollout();
            } else {
                current.setChildren(addNewStates());
                current.set(current.children.get(0));
                //rollout();
            }
        } else {
            current.set(findNodeMaxUCB1());
        }
    }
    */

    public void rollout(){

        while(true){
            //TO DO
        }
    }

    public double simulate(){
        //TO DO
        //returns the value
        return 0;
    }

    public boolean isLeafNode(Node node){
        return node.getChildren().isEmpty();
    }


    /*
    private List<Node> addNewStates(){

        List<Node> newStates = new ArrayList<>();

        for(int i=0; i<current.actions.size(); i++){
           for(int j=0; j<current.actions.get(i).size(); j++){
               Node node = new Node(current);
               node.set(node.takeAction(current.actions.get(i).get(j)));
               newStates.add(node);
           }

        }

        return newStates;
    }
    */


    private Node findNodeMaxUCB1(){

        for(int i=0; i<current.children.size(); i++){
            current.children.get(i).getUCB1(initialState.visits);
        }

        double maxUCB1 = current.children.get(0).ucb1;
        int index = 0;

        for(int i=0; i<current.children.size(); i++){
            if(maxUCB1 < current.children.get(i).ucb1){
                maxUCB1 = current.children.get(i).ucb1;
                index = i;
            }
        }

        Node node = new Node(current.children.get(index));

        return node;
    }
}
