package AI;

import Util.Array3D;
import org.joml.Vector3i;

import javax.vecmath.Vector3f;
import java.util.*;

import static java.lang.Math.*;
import static AI.PathFindingAlgorithm.*;

public class BFS implements PathFindingAlgorithm
{
    public final static int EMPTY=Integer.MAX_VALUE-2;
    public final static int OBSTACLE=Integer.MAX_VALUE-1;
    public final static int AGENT=Integer.MAX_VALUE;
    public static class Agent<Integer>
    {
        public Agent(Integer id, Vector3i pos)
        {
            this.id=id;
            this.pos=pos;
        }
        public Integer id;
        public Vector3i pos;
        public Object clone()
        {
            return new Agent<Integer>(id,pos);
        }
        public int hashCode()
        {
            return pos.hashCode();
        }
        public boolean equals(Object o)
        {
            return pos.equals(((Agent)o).pos);
        }
    }
    @Override
    public void compute(Vector3f _size,List<Vector3f> _startPosition,List<Vector3f> _endPosition,List<Vector3f> _obstacle,List<List<Movement>> result)
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
            obstacle.add(new Vector3i(round(vec.x),round(vec.y),round(vec.z)));
        }

        compute(size,startPosition,endPosition,obstacle,result);
    }
    public void compute(List<Agent<Integer>> agents,List<Agent<Integer>> movableAgents,Array3D<Integer> blocks,List<Vector3i> endPosition,List<List<Movement>> result)
    {
        for(Vector3i end:endPosition)
        {
            Array3D<Integer> distance = generateDistanceArray(blocks.size(), blocks, end);
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
    public void compute(Vector3i size, List<Vector3i> startPosition, List<Vector3i> endPosition, List<Vector3i> obstacle, List<List<PathFindingAlgorithm.Movement>> result)
    {
        List<Agent<Integer>> agents=new ArrayList<>(startPosition.size());
        {
            Iterator<Vector3i> posIter=startPosition.iterator();
            int i=0;
            do
            {
                Vector3i vec=posIter.next();
                agents.add(new Agent<>(i,vec));
                i++;
            }
            while(posIter.hasNext());
        }

        List<Agent<Integer>> movableAgents = new LinkedList<>();
        for(Agent agent:agents)
        {
            movableAgents.add(agent);
        }
        Array3D<Integer> blocks= generateBlockArray(size,startPosition,obstacle);
        compute(agents,movableAgents,blocks,endPosition,result);
        /*for(Vector3i end:endPosition)
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
        }*/
    }
    public boolean pathFindSingleAgent(Agent<Integer> agent,Vector3i to,Array3D<Integer> obstacles,List<List<Movement>> result)
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
                List<Movement> a=new LinkedList<>();
                Vector3i newPos=path.get(i);
                a.add(new Movement(agent.id,new Vector3f(newPos.x,newPos.y,newPos.z)));
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
    private Vector3i getArrivePosition(Agent<Integer> head, Array3D<Integer> distance, Array3D<Integer> obstacles)
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

    public static Queue<Agent> getHeadPriorityQueue(Array3D<Integer> distance,List<Agent<Integer>> agents)
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

    public Queue<Agent> getTailPriorityQueue(Array3D<Integer> distance,List<Agent<Integer>> agents,Agent head)
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

    public static Array3D<Integer> generateDistanceArray(Vector3i size, Array3D<Integer> blocks, Vector3i end)
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

    private static void bfsOneStep(Queue<Vector3i>toAdd,Vector3i v,Array3D<Integer> result,Array3D<Integer> blocks,int length)
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
