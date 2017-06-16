package AI;

import Util.Array3D;
import org.joml.Vector3i;

import javax.vecmath.Vector3f;
import java.util.*;

import static java.lang.Math.*;

/**
 * Created by Andrea Nardi on 6/15/2017.
 */
public class BFS<AgentId> implements PathFindingAlgorithm<AgentId>
{
    private final static int EMPTY=Integer.MAX_VALUE-2;
    private final static int OBSTACLE=Integer.MAX_VALUE-1;
    private final static int AGENT=Integer.MAX_VALUE;
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
    public void compute(Vector3f _size,List<AgentId> agentIds, List<Vector3f> _startPosition, List<Vector3f> _endPosition,List<Vector3f> _obstacle,List<Movement<AgentId>> result)
    {
        Vector3i size=new Vector3i(round(_size.x),round(_size.y),round(_size.z));
        List<Vector3i> startPosition = new ArrayList<>(_startPosition.size());
        for(Vector3f vec:_startPosition)
        {
            startPosition.add(new Vector3i(round(vec.x),round(vec.y),round(vec.z)));
        }
        List<Vector3i> endPosition = new ArrayList<>(_endPosition.size());
        for(Vector3f vec:_endPosition)
        {
            endPosition.add(new Vector3i(round(vec.x),round(vec.y),round(vec.z)));
        }
        List<Vector3i> obstacle = new ArrayList<>(_obstacle.size());
        for(Vector3f vec:_obstacle)
        {
            endPosition.add(new Vector3i(round(vec.x),round(vec.y),round(vec.z)));
        }
        List<Agent<AgentId>> agents=new ArrayList<>(agentIds.size());
        {
            Iterator<AgentId> idsIter=agentIds.iterator();
            Iterator<Vector3i> posIter=startPosition.iterator();
            do
            {
                AgentId ids=idsIter.next();
                Vector3i vec=posIter.next();
                agents.add(new Agent<>(ids,vec));
            }
            while(idsIter.hasNext());
        }

        Array3D<Integer> blocks= generateBlockArray(size,startPosition,obstacle);
        for(Vector3i end:endPosition)
        {
            Array3D<Integer> distance = generateDistanceArray(size, blocks, end);
            finish:
            while(true)
            {
                Queue<Agent> heads = getHeadPriorityQueue(distance, agents);
                here:
                while (heads.size() > 0)
                {
                    Agent head = heads.remove();
                    Vector3i arrivePosition = getArrivePosition(head, distance, blocks);
                    Queue<Agent> tails = getTailPriorityQueue(distance, movableAgents, head);
                    while (tails.size() > 0)
                    {
                        Agent tail = tails.remove();
                        if (pathFindSingleAgent(tail, arrivePosition, blocks, result))
                        {
                            if(tail.pos.equals(end))
                            {
                                movableAgents.remove(tail);
                                break finish;
                            }
                            else
                            {
                                break here;
                            }
                        }
                    }
                }
            }
        }

    }

    private Queue<Agent> getHeadPriorityQueue(Array3D<Integer> distance, List<Agent<AgentId>> agents)
    {
    }

    private Array3D<Integer> generateDistanceArray(Vector3i size, Array3D<Integer> blocks, Vector3i end)
    {
        Array3D<Integer> result= new Array3D<Integer>(size,Integer.MAX_VALUE);
        Queue<Vector3i> first=new LinkedList<>();
        Queue<Vector3i> second=new LinkedList<>();
        int length=1;
        first.add(end);
        result.set(end,0);
        do
        {
            do
            {
                bfsOneStep(second,first.remove(),result,blocks,length);
            }
            while(!first.isEmpty());
            first=second;
            second=new LinkedList<>();
            length++;
        }
        while(!first.isEmpty());
        return result;
    }

    private void bfsOneStep(Queue<Vector3i>toAdd,Vector3i v,Array3D<Integer> result,Array3D<Integer> blocks,int length)
    {
        if(v.y>0 && blocks.get(new Vector3i(v.x,v.y-1,v.z))!=OBSTACLE)
        {

            Vector3i toUse=new Vector3i(v.x,v.y-1,v.z);
            if(result.isInBound(toUse) && result.get(toUse)==Integer.MAX_VALUE)
            {
                result.set(toUse,length);
                toAdd.add(toUse);
            }
        }

        {
            for (int p = -1; p <= 1; p += 2)
            {
                {
                    Vector3i toUse = new Vector3i(v.x + p, v.y, v.z);
                    while(blocks.isInBound(toUse) && blocks.get(toUse)==OBSTACLE)
                    {
                        toUse.y++;
                    }
                    if(blocks.isInBound(new Vector3i(toUse.x,toUse.y-1,toUse.z)) && blocks.get(new Vector3i(toUse.x,toUse.y-1,toUse.z))!=OBSTACLE)
                    {
                        toUse.y--;
                    }
                    if(result.isInBound(toUse) && result.get(toUse)==Integer.MAX_VALUE)
                    {
                        result.set(toUse,length);
                        toAdd.add(toUse);
                    }
                }
                {
                    Vector3i toUse = new Vector3i(v.x, v.y, v.z + p);
                    while(blocks.isInBound(toUse) && blocks.get(toUse)==OBSTACLE)
                    {
                        toUse.y++;
                    }
                    if(blocks.isInBound(new Vector3i(toUse.x,toUse.y-1,toUse.z)) && blocks.get(new Vector3i(toUse.x,toUse.y-1,toUse.z))!=OBSTACLE)
                    {
                        toUse.y--;
                    }
                    if(result.isInBound(toUse) && result.get(toUse)==Integer.MAX_VALUE)
                    {
                        result.set(toUse,length);
                        toAdd.add(toUse);
                    }
                }
            }
        }
    }

    private Array3D<Integer> generateBlockArray(Vector3i size, List<Vector3i> startPosition, List<Vector3i> obstacle)
    {
        Array3D<Integer> result=new Array3D<Integer>(new Vector3i(round(size.x),round(size.y),round(size.z)),EMPTY);
        for(Vector3i vec:startPosition)
        {
            result.set(new Vector3i(round(vec.x),round(vec.y),round(vec.z)),AGENT);
        }
        for(Vector3i vec:obstacle)
        {
            result.set(new Vector3i(round(vec.x),round(vec.y),round(vec.z)),OBSTACLE);
        }
        return result;
    }
}
