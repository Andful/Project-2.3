package AI;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;
import static AI.PathFindingAlgorithm.*;

/**
 * Created by Savvas on 6/22/2017.
 */
public class Greedy implements PathFindingAlgorithm {

    public void compute(Vector3f environmentSize, List<Vector3f> startConfiguration, List<Vector3f> endConfiguration, List<Vector3f> obstacleConfiguration, List<List<Movement>> result){

        Node current = new Node(startConfiguration, endConfiguration);

        double value = 0;
        int index = 0;
        int actIndex = 0;
        int iterations = 0;

        while(iterations < 100){

            for(int i=0; i<current.getAgents().size(); i++){

                for(int j=0; j<current.getActions().get(i).size(); j++){
                    Node dummy = new Node(current);
                    dummy.getAgents().get(i).add(dummy.getActions().get(i).get(j));
                    double v = dummy.calculateValue();

                    if(v > value){
                        value = v;
                        index = i;
                        actIndex = j;
                    }
                }
            }

            current.getAgents().get(index).add(current.getActions().get(index).get(actIndex));

            Movement mvt = new Movement(index, current.getAgents().get(index));
            System.out.println(current.getAgents().get(index));
            List<Movement> movements = new ArrayList<>();
            movements.add(mvt);
            result.add(movements);

            iterations++;
        }
    }
}
