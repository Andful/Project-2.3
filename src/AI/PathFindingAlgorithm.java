package AI;

import org.joml.Vector3i;

import javax.vecmath.Vector3f;
import java.util.List;

/**
 * Created by Andrea Nardi on 6/15/2017.
 */
public interface PathFindingAlgorithm
{
    static class Movement
    {
        public Movement(int id,Vector3f to)
        {
            this.id=id;
            this.to=to;
        }
        public int id;
        public Vector3f to;
    }
    public void compute(Vector3f enviromentSize,List<Vector3f> startConfiguration,List<Vector3f> endConfiguration,List<Vector3f> obstacleConfiguration,List<List<Movement>> result);
}
