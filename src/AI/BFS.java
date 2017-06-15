package AI;

import org.joml.Vector3i;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static java.lang.Math.*;

/**
 * Created by Andrea Nardi on 6/15/2017.
 */
public class BFS<AgentId> implements PathFindingAlgorithm<AgentId>
{
    public static class Agent<AgentId>
    {
        Agent(AgentId id, Vector3i pos)
        {
            this.id=id;
            this.pos=pos;
        }
        AgentId id;
        Vector3i pos;
    }
    @Override
    public void compute(Vector3f size,List<AgentId> agentIds, List<Vector3f> startPosition, List<Vector3f> endPosition,List<Vector3f> obstacle,List<Movement<AgentId>> result)
    {
        List<Agent> agents=new ArrayList<>(agentIds.size());
        {
            Iterator<AgentId> idsIter=agentIds.iterator();
            Iterator<Vector3f> posIter=startPosition.iterator();
            do
            {
                AgentId ids=idsIter.next();
                Vector3f vec=posIter.next();
                agents.add(new Agent(ids,new Vector3i(round(vec.x),round(vec.y),round(vec.z))));
            }
            while(idsIter.hasNext());
        }
    }
}
