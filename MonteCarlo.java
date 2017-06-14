import java.util.List;

/**
 * Created by Savvas on 6/14/2017.
 */
public class MonteCarlo<E> {

    protected Node<E> current;
    protected Node<E> initialState;
    protected List<Node<E>> initialActions; //children of initial state node
    protected Node<E> terminalState;

    public MonteCarlo(Node<E> initialState, List<Node<E>> initialActions,Node<E> terminalState){
        this.initialState = initialState;
        this.initialActions = initialActions;
        this.terminalState = terminalState;
        initialState.setChildren(initialActions);
    }

    public void treeSearch(){
        current = initialState;

        while(!isLeafNode(current)){

            double maxUCB1 = 0;

            for(int i=0; i<current.children.size(); i++){
                current.children.get(i).getUCB1(initialState.visits);
            }

            for(int i=0; i<current.children.size(); i++){
                if(current.children.get(i).ucb1 >= maxUCB1){
                    maxUCB1 = current.children.get(i).ucb1;
                    current = current.children.get(i);
                }
            }
        }

        if(current.visits == 0){
            //rollout
        }else{
            //for each available action from current add a new state to the tree
            //current = first child node
            //rollout
        }
    }

    public void rollout(){

        while(true){

        }
    }

    public boolean isLeafNode(Node<E> node){
        return node.getChildren().isEmpty();
    }
}
