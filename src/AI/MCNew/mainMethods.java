package AI.MCNew;

import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class mainMethods {

	public void branch(ConflictNode n, Conflict c){
		
		Constraint left = new Constraint(c.getI(), c.getTimeStep());
		Constraint right = new Constraint(c.getJ(), c.getTimeStep());
		
		n.getLeftChild().setConstraints(n.getConstraints());
		n.getRightChild().setConstraints(n.getConstraints());
		
		n.getLeftChild().getConstraints().add(left);
		n.getRightChild().getConstraints().add(right);
	}
	
	static int whoCounter = 0;
	static int numberOfClusters;
	
	static List<List<Agent>> clusterArrayLists(List<Agent> all){
		
        findClusters(all);
    	
    	List<Agent> ordered = new ArrayList<Agent>();
    	
    	for (int i = 0; i < all.size(); i++){
    		for (int j = 0; j < all.size(); j++){
    		    
    			if (all.get(j).getClusterID() == i){
    				ordered.add(all.get(j));
    			}
    			
    		}
    	}
    	
    	List<List<Agent>> clusters = new ArrayList<List<Agent>>();
    	
    	clusters.add(new ArrayList<Agent>());
    	clusters.get(0).add(ordered.get(0));
    	
    	int numberOfLists = 0;
    	
    	for(int i = 1; i < ordered.size(); i++){
    		
    		if(ordered.get(i).getClusterID() == ordered.get(i - 1).getClusterID()){
    			clusters.get(numberOfLists).add(ordered.get(i));
    		}
    		else{
    			clusters.add(new ArrayList<Agent>());
    			numberOfLists++;
    			clusters.get(numberOfLists).add(ordered.get(i));
    		}
    		
    	}
    	
    	
    	for (int a = 0; a < clusters.size(); a++){
    		for (int b = 0; b < clusters.get(a).size(); b++){
    			System.out.print(clusters.get(a).get(b).getID() + " ");
    			numberOfClusters = a;
    		}
    		System.out.println(" ");
    		
    	}
    	
    //	System.out.println(" LINE 64 ");
    	System.out.println(" ");
    	numberOfClusters++;
    	System.out.println(" number of clusters = " + numberOfClusters );
    	
    	return clusters;
    			
	}
	
	
     static List<startAndEnd> findEndConfig(List<Agent> all, List<List<Agent>> start, List<List<Agent>> end){
		
    	 List<startAndEnd> whoGoesWhere = new ArrayList<startAndEnd>();
    	
    	 for (int h = 0; h < numberOfClusters; h++){		 
	    		whoGoesWhere.add(new startAndEnd(new ArrayList<Vector3i>(), new ArrayList<Vector3i>()));
	    	}
			 
    	  int[] clusterSizes = new int[start.size()];
    	  
    	  for (int i = 0; i < start.size(); i++){
    		  clusterSizes[i] = start.get(i).size();
    		//  System.out.println(clusterSizes[i]);
    	  }
    	  
    	  List<Integer> orderedStartSizes = new ArrayList<Integer>();
    	  
    	 // System.out.println(" ");
    	  
    	  for (int j = 0; j < all.size() + 1; j++){
    		  for (int k = 0; k < clusterSizes.length; k++){
    			    
    			  if(clusterSizes[k] == j){
    				  orderedStartSizes.add(clusterSizes[k]);
    		//		  System.out.println(clusterSizes[k]);
    			  }
    			  
    		  }
    	  }
    	
    	  //System.out.println(" ");
    	  
    	  int[] numberOfClusterSizes = new int[all.size() + 1];
    	  
    	  for (int a = 0; a < all.size() + 1; a++){
    		  int number = 0;
    		  
    		  for (int b = 0; b < orderedStartSizes.size(); b++){
    			  
    			  if(orderedStartSizes.get(b) == a){
    				  number++;
    			  }

    		  }
    		  numberOfClusterSizes[a] = number;
		//	  System.out.println("In the start configuration there are " + number + " clusters of the size: " + a);
    	  }
    	
    	  
          int[] clusterSizesEnd = new int[end.size()];
    	  
    	  for (int i = 0; i < end.size(); i++){
    		  clusterSizesEnd[i] = end.get(i).size();
    	   //   System.out.println(clusterSizesEnd[i]);
    	  }
    	  
    	  List<Integer> orderedEndSizes = new ArrayList<Integer>();
    	  
    	//  System.out.println(" ");
    	  
    	  for (int j = 0; j < all.size() +1; j++){
    		  for (int k = 0; k < clusterSizesEnd.length; k++){
    			  
    			  if(clusterSizesEnd[k] == j){
    				  orderedEndSizes.add(clusterSizesEnd[k]);
    				 // System.out.println(clusterSizesEnd[k]);
    			  }
    			  
    		  }
    	  }
    	
    	 // System.out.println(" LINE 137 ");
    	 // System.out.println(" ");
    	  
    	  int[] numberOfEndClusterSizes = new int[all.size() + 1];
    	  
    	  for (int a = 0; a < all.size() + 1; a++){
    		  int number = 0;
    		  
    		  for (int b = 0; b < orderedEndSizes.size(); b++){
    			  
    			  if(orderedEndSizes.get(b) == a){
    				  number++;
    			  }

    		  }
    		  numberOfEndClusterSizes[a] = number;
		//	  System.out.println("In the end configuration there are " + number + " clusters of the size: " + a);
    	  }
    	  
    	 // System.out.println(" ");
    	
    	  int[] differenceInClusterSize = new int[all.size() + 1];
    	  
    	  for (int i = 0; i < all.size() + 1; i++){
    		  
    	   differenceInClusterSize[i] = numberOfEndClusterSizes[i] - numberOfClusterSizes[i];
    	//	  System.out.println("There are " +differenceInClusterSize[i]  + " more clusters of the size in " + i 
    		//		  + " in the end configuration than in the start");
    	  }
    	  
    	//  System.out.println(" ");
    	  
    	  int[] numOfFits = new int[all.size()+1];
    	
    	  for( int j = 0; j < all.size()+1; j++){
    		  
    		  int number = 0;
    		  
    		  if (numberOfEndClusterSizes[j] == numberOfClusterSizes[j]){
    			  number = numberOfClusterSizes[j];
    		  }
    		  if (numberOfEndClusterSizes[j] < numberOfClusterSizes[j]){
    			  number = numberOfEndClusterSizes[j];
    		  }
    		  if (numberOfEndClusterSizes[j] > numberOfClusterSizes[j]){
    			  number = numberOfClusterSizes[j];
    		  }
    		  
    		  numOfFits[j] = number;
    	//	  System.out.println("There are exactly " + number + " perfect fits for the cluster size " + j);
    	  }
   
    	  System.out.println(" ");
    	  
    	  
    	  for (int i = 0; i < all.size() + 1; i++){
    		  
    		  if(numOfFits[i] > 0){
    			  
    			  List<List<clusterMidPoint>> table = new ArrayList<List<clusterMidPoint>>();
    	    	  table.add(new ArrayList<clusterMidPoint>());
    	    	  table.add(new ArrayList<clusterMidPoint>());
    	    	  
    			  for (int j = 0; j < start.size(); j++){
    				  
    				  if (start.get(j).size() == i){
    					  
    					  Vector3i midPoint = new Vector3i(start.get(j).get(0).getPosition());
    					  
    					  for (int k = 1; k < i; k++){
    						  
    						 int x = (midPoint.x + start.get(j).get(k).getPosition().x)/2;
    						 int y = (midPoint.y + start.get(j).get(k).getPosition().y)/2;
    						 int z = (midPoint.z + start.get(j).get(k).getPosition().z)/2;
    						 
    						 Vector3i newMid = new Vector3i(x, y, z);
    						 midPoint = newMid;
    						
    					  }
    					  
    					  
 						 table.get(0).add(new clusterMidPoint(midPoint, j));
    					  
    				  }
    				  
    			  }
    			  
    			  for (int a = 0; a < end.size(); a++){
    				  
    				  if (end.get(a).size() == i){
    					  
                         Vector3i midPoint = new Vector3i(end.get(a).get(0).getPosition());
    					  
    					  for (int k = 1; k < i; k++){
    						  
    						 int x = (midPoint.x + end.get(a).get(k).getPosition().x)/2;
    						 int y = (midPoint.y + end.get(a).get(k).getPosition().y)/2;
    						 int z = (midPoint.z + end.get(a).get(k).getPosition().z)/2;
    						 
    						 Vector3i newMid = new Vector3i(x, y, z);
    						 midPoint = newMid;
    						
    					  }
    					  
    					  
 						 table.get(1).add(new clusterMidPoint(midPoint, a));
    					  
    				  }
    					     				  
    			  }
    			  
    			 
    			  for (int n = 0; n < table.size(); n++){
    				  for (int m = 0; m < table.get(n).size(); m++){
    					  
    					//  System.out.println(table.get(n).get(m).getMid() + " ");
    					  
    				  }
    				//  System.out.println(" ");
    			  }
    			  
    			  
    			  List<List<Integer>> distances = new ArrayList<List<Integer>>();
    			  
    			  for (int r = 0; r < table.get(0).size(); r++){
    				  distances.add(new ArrayList<Integer>());
    			 
    			  for (int t = 0; t < table.get(1).size(); t++){
    				  
    				  float diffX = table.get(0).get(r).getMid().x - table.get(1).get(t).getMid().x;
    				  float diffY = table.get(0).get(r).getMid().y - table.get(1).get(t).getMid().y;
    				  float diffZ = table.get(0).get(r).getMid().z - table.get(1).get(t).getMid().z;
    				  
    				  float squareX = diffX * diffX;
    				  float squareY = diffY * diffY;
    				  float squareZ = diffZ * diffZ;
    				  
    				  Integer distance = (int) Math.sqrt(squareX + squareY + squareZ);
    				  
    				  distances.get(r).add(distance);
    				  
    			  }
    			 }
    			  
    			  for (int q = 0; q < distances.size(); q++){
    				  for (int w = 0; w < distances.get(q).size(); w++){
    					  
    				//	  System.out.print(distances.get(q).get(w).intValue() + " ");
    					  
    				  }
    				  
    			//	  System.out.println(" ");
    				
    			  }
    		//	  System.out.println(" ");
    			  
    			  List<Vector3i> bestMatches = new ArrayList<Vector3i>();
    			  int smallestDistance = 10000;
    			  
    			 for (int a = 0; a < distances.size(); a++){
    				 
    				 bestMatches.add(new Vector3i (0, 0, 0));
    				 
    				 for (int b = 0; b < distances.get(a).size(); b++){
    					   					 
    					 if (distances.get(a).get(b) < smallestDistance ){
    						 bestMatches.get(a).set(distances.get(a).get(b), a, b);
    					     smallestDistance = distances.get(a).get(b);
    					 }
    					 
    				 }
    				 
    				 for (int d = 0; d < distances.size(); d++){
						 distances.get(d).set((int) bestMatches.get(a).z, 10000);
					 } 
    				 
    				 smallestDistance = 10000;
    				 
    			 }
    			 
    			 
    			 for (int c = 0; c < bestMatches.size(); c++){
    				
    			//	 System.out.println(bestMatches.get(c));
    				 
    			//	System.out.println(start.get(table.get(0).get((int) bestMatches.get(c).y).getIndex()).size());
    			//	System.out.println(end.get(table.get(1).get((int) bestMatches.get(c).y).getIndex()));
    				 
    				
    			 }
    		//	 System.out.println(" ");
    			 
    	    	
    			 
    			 for (int c = 0; c < bestMatches.size(); c++){
    				 
    				 for (int w = 0; w < start.get(table.get(0).get((int) bestMatches.get(c).y).getIndex()).size(); w++){
    					 
    					 whoGoesWhere.get(whoCounter).getStart().add(start.get(table.get(0).get((int) bestMatches.get(c).y).getIndex()).get(w).getPosition());
    				     whoGoesWhere.get(whoCounter).getEnd().add(end.get(table.get(1).get((int) bestMatches.get(c).y).getIndex()).get(w).getPosition());
    				    
    				 }
    				 whoCounter++; 
    			 }
    			
    			 
    		/*	 for (int t = 0; t < whoGoesWhere.size(); t++){
    				 for (int y = 0; y < whoGoesWhere.get(t).size(); y++){
    					 System.out.print(whoGoesWhere.get(t).get(y));
    				 }
    				 System.out.println(" ");
    			 }
    			 */
    		  }
    		
    	  }
    	  

    	 
   
    	return whoGoesWhere;	
    	
    }
    
    
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
			
			if(all.get(element).getPosition().x == all.get(i).getPosition().x  &&
					all.get(element).getPosition().y == all.get(i).getPosition().y && 
					  all.get(element).getPosition().z == all.get(i).getPosition().z - 1){
				all.get(i).setClusterID(currentCluster);
				
		}
			
		
	}
	
	}
	public static List<startAndEnd> getStartAndEnd(List<Vector3i> startConfiguration,List<Vector3i> endConfiguration)
	{
		List<Agent> start= new ArrayList<>(startConfiguration.size());
		List<Agent> end= new ArrayList<>(endConfiguration.size());
		for(Vector3i v:startConfiguration)
		{
			start.add(new Agent(start.size(),v,0));
		}
		for(Vector3i v:endConfiguration)
		{
			end.add(new Agent(end.size(),v,0));
		}
		mainMethods.findClusters(start);
		mainMethods.findClusters(end);
		return mainMethods.findEndConfig(start,mainMethods.clusterArrayLists(start), mainMethods.clusterArrayLists(end));
	}
	
}