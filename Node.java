import java.util.LinkedList;
import java.util.List;

/**
 * Created by Savvas on 6/14/2017.
 */
public class Node<E> {

    protected E element; //State
    protected int visits; //Number of visits to this node
    protected double value; //Score
    protected double ucb1;
    protected List<Node<E>> children;
    protected Node<E> parent;
    protected boolean terminal; //true if the node is a terminal state

    public Node(E element){
        this.element = element;
        this.children = new LinkedList<>();
    }

    public Node(E element, Node<E> parent){
        this.element = element;
        this.parent = parent;
    }

    public Node(E element, Node<E> parent, List<Node<E>> children){
        this.element = element;
        this.parent = parent;
        this.children = children;
    }

    //calculates and returns the ucb1 value
    public double getUCB1(int totalVisits){

        if(totalVisits == 0){
            this.ucb1 = Integer.MAX_VALUE;
            return ucb1;
        }

        ucb1 = value + Math.sqrt(Math.log(totalVisits)/visits);

        return ucb1;
    }

    public E getElement(){
        return element;
    }

    public void setElement(E element){
        this.element = element;
    }

    public void setParent(Node<E> parent){
        this.parent = parent;
    }

    public void setChildren(List<Node<E>> children){
        this.children = children;
    }

    public List<Node<E>> getChildren(){
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
        return "Element: " + element;
    }
}
