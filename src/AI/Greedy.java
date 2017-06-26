package AI;

import org.joml.Vector3i;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Savvas on 6/26/2017.
 */
public class Greedy implements PathFindingAlgorithm {

    private Node firstState;
    private List<Vector3i> agentConfiguration;
    private List<Vector3i> endConfiguration;
    private List<Vector3i> obstacleConfiguration;
    private List<Node> path;

    public static void main(String[] args){

        List<Vector3i> startConfiguration = new ArrayList<>();
        startConfiguration.add(new Vector3i(0,0,0));
        startConfiguration.add(new Vector3i(1,0,0));

        List<Vector3i> endConfiguration = new ArrayList<>();
        endConfiguration.add(new Vector3i(5,0,2));
        endConfiguration.add(new Vector3i(5,0,3));

        List<Vector3i> obstacleConfiguration = new ArrayList<>();

        Greedy pathfinder = new Greedy(startConfiguration,endConfiguration,obstacleConfiguration);



    }

    public Greedy(List<Vector3i> agentConfiguration, List<Vector3i> endConfiguration, List<Vector3i> obstacleConfiguration){
        this.agentConfiguration = agentConfiguration;
        this.endConfiguration = endConfiguration;
        this.obstacleConfiguration = obstacleConfiguration;
        this.firstState = new Node(agentConfiguration,endConfiguration,obstacleConfiguration);
        this.path = computePath();
    }

    @Override
    public void compute(Vector3f enviromentSize, List<Vector3f> startConfiguration, List<Vector3f> endConfiguration, List<Vector3f> obstacleConfiguration, List<List<Movement>> result) {

        for(int i=0; i<path.size(); i++){
           int agentIndex = path.get(i).getIdealAction().getAgentIndex();

           Vector3i agent = agentConfiguration.get(agentIndex);
           Vector3i action = path.get(i).getIdealAction().getAction();

           Vector3f floatAgent = new Vector3f(agent.x, agent.y , agent.z);
           Vector3f floatAction = new Vector3f(action.x , action.y , action.z);

           Vector3f to = new Vector3f(floatAgent);
           to.add(floatAction);

           Movement mvt = new Movement(agentIndex, to);
           List<Movement> movement = new ArrayList<>();
           movement.add(mvt);

           result.add(movement);
        }
    }

    private List<Vector3f> castListToFloats(List<Vector3i> integerConfiguration){

        List<Vector3f> floatConfiguration = new ArrayList<>();

        for(int i=0; i<integerConfiguration.size(); i++){

            floatConfiguration.add(new Vector3f((float) integerConfiguration.get(i).x, (float) integerConfiguration.get(i).y, (float) integerConfiguration.get(i).z));
        }

        return floatConfiguration;
    }

    public List<Node> computePath(){

        int iterations = 0;
        List<Node> path = new ArrayList<>();

        Node current = new Node(firstState);

        while(!current.getAgentConfiguration().equals(endConfiguration) && iterations < 100){
            iterations++;

            Node.Action action = current.getIdealAction();

            current.getAgentConfiguration().get(action.getAgentIndex()).add(action.getAction());

            path.add(current);
        }

        return path;
    }
}
