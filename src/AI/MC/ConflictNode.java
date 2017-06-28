package AI.MC;

import java.util.List;

public class ConflictNode<E> {

	public ConflictNode(List<Constraint> c, List<E> s, int tc){
		 constraint = c; 
		 List<E> solution = s;
		 int totalCost = tc;
         parent = null;
         leftChild = null;
         rightChild = null;
		 }
	
	   List<Constraint> constraint;
	   List<E> solution;
	   int totalCost = 0;
	   ConflictNode parent;
	   ConflictNode leftChild;
	   ConflictNode rightChild;
	   
	public List<Constraint> getConstraints() {
		return constraint;
	}
	public void setConstraints(List<Constraint> constraint) {
		this.constraint = constraint;
	}
	public List<E> getSolution() {
		return solution;
	}
	public void setSolution(List<E> solution) {
		this.solution = solution;
	}
	public int getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}
	public ConflictNode getParent() {
		return parent;
	}
	public void setParent(ConflictNode parent) {
		this.parent = parent;
	}
	public ConflictNode getLeftChild() {
		return leftChild;
	}
	public void setLeftChild(ConflictNode leftChild) {
		this.leftChild = leftChild;
	}
	public ConflictNode getRightChild() {
		return rightChild;
	}
	public void setRightChild(ConflictNode rightChild) {
		this.rightChild = rightChild;
	}
	   
	}
