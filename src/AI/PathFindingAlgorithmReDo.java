package AI;

import org.joml.Vector3i;
import org.lwjglb.AI.pathFindingAlgorithms.PathFindingAlgorithm;import org.lwjglb.Util.Array3D;

import java.util.*;

/**
 * Created by Andrea Nardi on 5/7/2017.
 */
public class PathFindingAlgorithmReDo<AgentId>
{
    private int turn=-1;
    private final static int EMPTY=Integer.MAX_VALUE-2;
    private final static int OBSTACLE=Integer.MAX_VALUE-1;
    private final static int AGENT=Integer.MAX_VALUE;
    public class Agent
    {
        AgentId id;
        Vector3i pos;
        public Agent(AgentId id,Vector3i pos)
        {
            this.id=id;
            this.pos=pos;
        }

        @Override
        public int hashCode()
        {
            return pos.hashCode();
        }
        public boolean equals(Object o)
        {
            return this.pos.equals(o);
        }
    }
    @Override
    public void computeMovments(Vector3i sizeEnviroment, List<Vector3i> startConfiguration, List<AgentId> agentsId, List<Vector3i> endConfiguration, List<Vector3i> obstacle, List<List<Movment<AgentId>>> result)
    {
        Array3D<Integer> blocks= generateBlockArray(sizeEnviroment,startConfiguration,obstacle);
        List<Agent> agents=new LinkedList<Agent>();
        List<Agent> movableAgents=new LinkedList<>();
        {
            Iterator<AgentId> ids=agentsId.iterator();
            Iterator<Vector3i> positions=startConfiguration.iterator();
            while(ids.hasNext())
            {
                AgentId id=ids.next();
                Vector3i position=positions.next();
                Agent agent=new Agent(id,position);
                agents.add(agent);
                movableAgents.add(agent);
            }
        }
        for(Vector3i end:endConfiguration)
        {
            Array3D<Integer> distance = generateDistanceArray(sizeEnviroment, blocks, end);
            finish:
            while(true)
            {
                turn++;
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
    public boolean pathFindSingleAgent(Agent agent,Vector3i to,Array3D<Integer> obstacles,List<List<Movment<AgentId>>> result)
    {
        obstacles.set(agent.pos,EMPTY);
        Array3D<Vector3i> cameFrom=new Array3D<Vector3i>(obstacles.size());
        Queue<Vector3i> first=new LinkedList<>();
        Queue<Vector3i> second=new LinkedList<>();
        first.add(agent.pos);
        cameFrom.set(agent.pos,new Vector3i(-1,-1,-1));
        here:
        do
        {
            do
            {
                Vector3i current=first.remove();
                if(hasAdiacentAgent(current,obstacles) && obstacles.get(new Vector3i(current.x,current.y+1,current.z))==EMPTY)
                {
                    for (int i = -1; i <= 1; i += 2)
                    {
                        {
                            Vector3i toUse = new Vector3i(current.x + i, current.y, current.z);
                            if (obstacles.isInBound(toUse) &&
                                    obstacles.get(toUse) == AGENT)
                            {
                                toUse.y++;
                            }
                            while (obstacles.isInBound(new Vector3i(toUse.x, toUse.y - 1, toUse.z)) &&
                                    obstacles.get(new Vector3i(toUse.x, toUse.y - 1, toUse.z)) == EMPTY)
                            {
                                toUse.y--;
                            }
                            if(obstacles.isInBound(toUse) && obstacles.get(toUse)==EMPTY && cameFrom.get(toUse)==null)
                            {
                                second.add(toUse);
                                cameFrom.set(toUse,current);
                                if(toUse.equals(to))
                                {
                                    break here;
                                }
                            }
                        }
                        {
                            Vector3i toUse = new Vector3i(current.x, current.y, current.z +i);
                            if (obstacles.isInBound(toUse) &&
                                    obstacles.get(toUse) == AGENT)
                            {
                                toUse.y++;
                            }
                            while (obstacles.isInBound(new Vector3i(toUse.x, toUse.y - 1, toUse.z)) &&
                                    obstacles.get(new Vector3i(toUse.x, toUse.y - 1, toUse.z)) == EMPTY)
                            {
                                toUse.y--;
                            }
                            if(obstacles.isInBound(toUse) && obstacles.get(toUse)==EMPTY && cameFrom.get(toUse)==null)
                            {
                                second.add(toUse);
                                cameFrom.set(toUse,current);
                                if(toUse.equals(to))
                                {
                                    break here;
                                }
                            }
                        }
                    }
                }
            }
            while(!first.isEmpty());
            first=second;
            second=new LinkedList<>();
        }
        while(!first.isEmpty());
        if(cameFrom.get(to)!=null)
        {
            List<Vector3i> path=new LinkedList<>();
            Vector3i temp=to;
            while(temp.x!=-1 || temp.y!=-1 || temp.z!=-1)
            {
                path.add(temp);
                temp=cameFrom.get(temp);
            }
            Vector3i oldPos=agent.pos;
            for(int i=path.size()-2;i>=0;i--)
            {
                List<Movment<AgentId>> a=new LinkedList<>();
                a.add(new Movment<AgentId>(agent.id,oldPos,path.get(i)));
                result.add(a);
                oldPos=path.get(i);
            }
            obstacles.set(to,AGENT);
            agent.pos=to;
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean hasAdiacentAgent(Vector3i agent,Array3D<Integer> obstacles)
    {
        for(int i=-1;i<=1;i+=2)
        {
            {
                Vector3i toUse=new Vector3i(agent.x+i,agent.y,agent.z);
                if(obstacles.isInBound(toUse) && obstacles.get(toUse)==AGENT)
                {
                    return true;
                }
            }
            {
                Vector3i toUse=new Vector3i(agent.x,agent.y+i,agent.z);
                if(obstacles.isInBound(toUse) && obstacles.get(toUse)==AGENT)
                {
                    return true;
                }
            }
            {
                Vector3i toUse=new Vector3i(agent.x,agent.y,agent.z+i);
                if(obstacles.isInBound(toUse) && obstacles.get(toUse)==AGENT)
                {
                    return true;
                }
            }
        }
        return false;
    }
    public Vector3i getArrivePosition(Agent head,Array3D<Integer> distance,Array3D<Integer> obstacles)
    {
        Vector3i result=null;
        int clousestDistance=Integer.MAX_VALUE;
        {
            Vector3i toUse=new Vector3i(head.pos.x,head.pos.y+1,head.pos.z);
            if(obstacles.isInBound(toUse) && obstacles.get(toUse)==EMPTY && clousestDistance>distance.get(toUse))
            {
                result=toUse;
                clousestDistance=distance.get(toUse);
            }
        }
        for(int i=-1;i<=1;i+=2)
        {
            {
                Vector3i toUse=new Vector3i(head.pos.x+i,head.pos.y+1,head.pos.z);
                while(obstacles.isInBound(new Vector3i(toUse.x,toUse.y-1,toUse.z)) && obstacles.get(new Vector3i(toUse.x,toUse.y-1,toUse.z))==EMPTY)
                {
                    toUse.y--;
                }
                if(obstacles.isInBound(toUse) && obstacles.get(toUse)==EMPTY && clousestDistance>distance.get(toUse))
                {
                    result=toUse;
                    clousestDistance=distance.get(toUse);
                }
            }
            {
                Vector3i toUse=new Vector3i(head.pos.x,head.pos.y+1,head.pos.z+i);
                while(obstacles.isInBound(new Vector3i(toUse.x,toUse.y-1,toUse.z)) && obstacles.get(new Vector3i(toUse.x,toUse.y-1,toUse.z))==EMPTY)
                {
                    toUse.y--;
                }
                if(obstacles.isInBound(toUse) && obstacles.get(toUse)==EMPTY && clousestDistance>distance.get(toUse))
                {
                    result=toUse;
                    clousestDistance=distance.get(toUse);
                }
            }
        }
        if(result==null)
        {
            throw new RuntimeException("arrive point is null");
        }
        return result;
    }
    public Queue<Agent> getTailPriorityQueue(Array3D<Integer> distance,List<Agent> agents,Agent head)
    {
        LinkedList<Agent> result=new LinkedList<>();
        for(Agent agent:agents)
        {
            if(agent.pos.equals(head))
            {
                result.add(agent);
            }
            ListIterator<Agent> iter=result.listIterator();
            while(true)
            {
                if(!iter.hasNext())
                {
                    iter.add(agent);
                    break;
                }
                else
                {
                    Agent toCompare=iter.next();
                    if(toCompare.pos.equals(head))
                    {
                        iter.previous();
                        iter.add(agent);
                        break;
                    }
                    else if(distance.get(agent.pos)>distance.get(toCompare.pos))
                    {
                        iter.previous();
                        iter.add(agent);
                        break;
                    }
                }
            }

        }
        return result;
    }
    public Queue<Agent> getHeadPriorityQueue(Array3D<Integer> distance,List<Agent> agents)
    {
        LinkedList<Agent> result=new LinkedList<>();
        for(Agent agent:agents)
        {
            ListIterator<Agent> iter=result.listIterator();
            while(true)
            {
                if(!iter.hasNext())
                {
                    iter.add(agent);
                    break;
                }
                else
                {
                    Agent toCompare=iter.next();
                    if(agent.pos.equals(new Vector3i(2,0,3)) && toCompare.pos.equals(new Vector3i(1,0,3)))
                    {
                        System.out.println("error is here");
                    }
                    if(distance.get(agent.pos)<distance.get(toCompare.pos))
                    {
                        iter.previous();
                        iter.add(agent);
                        break;
                    }
                }
            }

        }
        return result;
    }
    public static Array3D<Integer> generateDistanceArray(Vector3i sizeEnviroment,Array3D<Integer> blocks,Vector3i end)
    {
        Array3D<Integer> result= new Array3D<Integer>(sizeEnviroment,Integer.MAX_VALUE);
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
    public static void bfsOneStep(Queue<Vector3i>toAdd,Vector3i v,Array3D<Integer> result,Array3D<Integer> blocks,int length)
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
    public static Array3D<Integer> generateBlockArray(Vector3i sizeEnviroment,List<Vector3i> startConfiguration,List<Vector3i> obstacle)
    {
        Array3D<Integer> result=new Array3D<>(sizeEnviroment,EMPTY);
        for(Vector3i v:startConfiguration)
        {
            result.set(v,AGENT);
        }
        for(Vector3i v:obstacle)
        {
            result.set(v,OBSTACLE);
        }
        return result;
    }
}
