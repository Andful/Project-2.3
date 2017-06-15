package AI;

import org.joml.Vector3i;

import javax.vecmath.Vector3f;
import java.util.List;

/**
 * Created by Andrea Nardi on 6/15/2017.
 */
public interface PathFindingAlgorithm<AgentId>
{
    static class Movement<AgentId>
    {
        public Movement(AgentId id,Vector3f direction)
        {
            this.id=id;
            this.direction=direction;
        }
        AgentId id;
        Vector3f direction;
    }
    public void compute(Vector3f size,List<AgentId> agentIds, List<Vector3f> startPosition, List<Vector3f> endPosition,List<Vector3f> obstacle,List<Movement<AgentId>> result);


}
