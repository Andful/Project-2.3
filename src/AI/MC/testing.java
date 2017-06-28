package AI.MC;

import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class testing {

	public static void main(String[] args) {
		
		List<Agent> agents = new ArrayList<Agent>();
	    
		Vector3i pos1 = new Vector3i(1,1,0);
		Vector3i pos2 = new Vector3i(1,2,0);
		Vector3i pos3 = new Vector3i(1,3,0);
		Vector3i pos4 = new Vector3i(1,4,0);
		Vector3i pos5 = new Vector3i(1,5,0);
		
		Agent one = new Agent(1, pos1, 0);
		Agent two = new Agent(2, pos2, 0);
		Agent three = new Agent(3, pos3, 0);
		Agent four = new Agent(4, pos4, 0);
		Agent five = new Agent(5, pos5, 0);

		agents.add(one);
		agents.add(two);
		agents.add(three);
		agents.add(four);
		agents.add(five);

		mainMethods.findClusters(agents);

		List<List<Agent>> clusters=new LinkedList<>();
		{
			HashMap<Integer,List<Agent>> map=new HashMap<>();
			for(Agent agent:agents)
			{
				map.putIfAbsent(agent.getClusterID(),new LinkedList<>());
				map.get(agent.getClusterID()).add(agent);
			}
			for(int clusterId:map.keySet())
			{
				clusters.add(map.get(clusterId));
			}
		}

		System.out.println(clusters.size());
		
	}

}
