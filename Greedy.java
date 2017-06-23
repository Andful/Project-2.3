package src.AI;

import javax.vecmath.Vector3f;
import java.util.List;
import static AI.PathFindingAlgorithm;
/**
 * Created by Savvas on 6/23/2017.
 */
public class Greedy implements PathFindingAlgorithm {

    private List<Vector3f> startConfiguration;
    private List<Vector3f> endConfiguration;
    private List<Vector3f> obstacleConfiguration;

    public Greedy(List<Vector3f> startConfiguration, List<Vector3f> endConfiguration, List<Vector3f> obstacleConfiguration){
        this.startConfiguration = startConfiguration;
        this.endConfiguration = endConfiguration;
        this.obstacleConfiguration = obstacleConfiguration;
    }


}
