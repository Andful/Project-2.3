package AI.MultiAgentPathFinding;

import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;


public class testing {

	public static void main(String[] args) {
		
		List<Agent> all = new ArrayList<Agent>();
	    
		Vector3i pos1 = new Vector3i(1,1,0);
		Vector3i pos2 = new Vector3i(1,4,0);
		Vector3i pos3 = new Vector3i(3,3,1);
		Vector3i pos4 = new Vector3i(3,3,0);
		Vector3i pos5 = new Vector3i(1,2,0);
		
		Agent one = new Agent(1, pos1, 0);
		Agent two = new Agent(2, pos2, 0);
		Agent three = new Agent(3, pos3, 0);
		Agent four = new Agent(4, pos4, 0);
		Agent five = new Agent(5, pos5, 0);
		
		all.add(one);
		all.add(two);
		all.add(three);
		all.add(four);
		all.add(five);
		
        List<Agent> end = new ArrayList<Agent>();
	    
		Vector3i end1 = new Vector3i(1,2,0);
		Vector3i end2 = new Vector3i(1,3,0);
		Vector3i end3 = new Vector3i(4,4,0);
		Vector3i end4 = new Vector3i(5,4,0);
		Vector3i end5 = new Vector3i(3,7,0);
		Vector3i end6 = new Vector3i(1,4,0);
		
		Agent firstEnd = new Agent(1, end1, 0);
		Agent secondEnd = new Agent(2, end2, 0);
		Agent thirdEnd = new Agent(3, end3, 0);
		Agent fourthEnd = new Agent(4, end4, 0);
		Agent fifthEnd = new Agent(5, end5, 0);
		Agent sixthEnd = new Agent(5, end6, 0);
		
		end.add(firstEnd);
		end.add(secondEnd);
		end.add(thirdEnd);
		end.add(fourthEnd);
		end.add(fifthEnd);
	//	end.add(sixthEnd);
		
		mainMethods.findClusters(all);
		mainMethods.findClusters(end);
		
	    System.out.println(one.getClusterID());
	    System.out.println(two.getClusterID());
	    System.out.println(three.getClusterID());
	    System.out.println(four.getClusterID());
	    System.out.println(five.getClusterID()); 
	    
	    System.out.println(" ");
	    
	    System.out.println(firstEnd.getClusterID());
	    System.out.println(secondEnd.getClusterID());
	    System.out.println(thirdEnd.getClusterID());
	    System.out.println(fourthEnd.getClusterID());
	    System.out.println(fifthEnd.getClusterID()); 
	    
	    
	    System.out.println(" ");
	    
	//	mainMethods.clusterArrayLists(all);
	//	mainMethods.clusterArrayLists(end);
	
	    List<List<Vector3i>> whoGoesWhere = new ArrayList<List<Vector3i>>();
		    
		whoGoesWhere = mainMethods.findEndConfig(all, mainMethods.clusterArrayLists(all), mainMethods.clusterArrayLists(end));
			
		for (int i = 0; i < whoGoesWhere.size(); i++){
			for (int j = 0; j < whoGoesWhere.get(i).size(); j++){
				System.out.print(whoGoesWhere.get(i).get(j) + " ");
			}
			System.out.println(" ");
		}
	}

}
