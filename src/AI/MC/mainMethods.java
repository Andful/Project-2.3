package AI.MC;

import java.util.List;

public class mainMethods {

	private void branch(ConflictNode n, Conflict c){
		
		Constraint left = new Constraint(c.getI(), c.getTimeStep());
		Constraint right = new Constraint(c.getJ(), c.getTimeStep());
		
		n.getLeftChild().setConstraints(n.getConstraints());
		n.getRightChild().setConstraints(n.getConstraints());
		
		n.getLeftChild().getConstraints().add(left);
		n.getRightChild().getConstraints().add(right);
	}
	
	/* my idea for cluster detection is we have a list of all the agents,
	 *  we number the first agent number 1, we go through the list to check if another agent has
	 *   the same but x+1 or x-1 or y+1 or y-1 or z+1 or z-1 coordinates and if we find one 
	 *   we give that cluster number 1 we then do the same for all the ones we just found 
	 *   until we go through them all, if there is still 
	 * something left in the list we give it cluster number 2 and repeat */
	
	static void findClusters(List<Agent> all){
		int size = all.size();
		int element = 0;
		int currentCluster = 1;
				
		while(isEmpty(all)){
		  all.get(element).setClusterID(currentCluster);
		
		  for (int j = 0; j < size; j++){

		       for (int i = 0; i < size; i++){
		    	   
	                  if(all.get(i).getClusterID() == currentCluster){                	  
	    	              setSurroundingCluster(currentCluster, i, all, size);
	    	              
	                  }
	             }
             }
		  
		  for (int k = 0; k < size; k++){
			if (all.get(k).getClusterID() == 0){
				element = k;
				k = size;
			}
		  }
		  
		  currentCluster++;
	   }
			
	}
		
	private static boolean isEmpty(List<Agent> all){	
		for (int i = 0; i < all.size(); i++){
			if ( all.get(i).getClusterID() == 0){
				return true;
			}
		}
		return false;
	}
	
	private static void setSurroundingCluster(int currentCluster, int element, List<Agent> all, int size){
		
	for (int i = 0; i < size; i++){
			
			if(all.get(element).getPosition().x == all.get(i).getPosition().x + 1 &&
					all.get(element).getPosition().y == all.get(i).getPosition().y &&
					  all.get(element).getPosition().z == all.get(i).getPosition().z ){
				all.get(i).setClusterID(currentCluster);	
		}
			
			if(all.get(element).getPosition().x == all.get(i).getPosition().x - 1 &&
					all.get(element).getPosition().y == all.get(i).getPosition().y &&
					  all.get(element).getPosition().z == all.get(i).getPosition().z ){
				all.get(i).setClusterID(currentCluster);		
		
		}
			
			if(all.get(element).getPosition().x == all.get(i).getPosition().x &&
					all.get(element).getPosition().y == all.get(i).getPosition().y + 1 &&
					  all.get(element).getPosition().z == all.get(i).getPosition().z ){
				all.get(i).setClusterID(currentCluster);		
				
		}
		
			if(all.get(element).getPosition().x == all.get(i).getPosition().x  &&
					all.get(element).getPosition().y == all.get(i).getPosition().y - 1 &&
					  all.get(element).getPosition().z == all.get(i).getPosition().z ){
				all.get(i).setClusterID(currentCluster);	
			
		}
			
			if(all.get(element).getPosition().x == all.get(i).getPosition().x &&
					all.get(element).getPosition().y == all.get(i).getPosition().y &&
					  all.get(element).getPosition().z == all.get(i).getPosition().z + 1){
				all.get(i).setClusterID(currentCluster);	
				
		}
			
			if(all.get(element).getPosition().x == all.get(i).getPosition().x + 1 &&
					all.get(element).getPosition().y == all.get(i).getPosition().y &&
					  all.get(element).getPosition().z == all.get(i).getPosition().z - 1){
				all.get(i).setClusterID(currentCluster);
				
		}
			
		
	}
	
	}
	
}