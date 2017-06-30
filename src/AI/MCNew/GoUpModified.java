package AI.MCNew;

import AI.BFS;
import AI.PathFindingAlgorithm;
import Util.Array2D;
import Util.Array3D;
import Util.PathFinding;
import org.joml.Vector2i;
import org.joml.Vector3i;

import javax.vecmath.Vector3f;
import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.round;

/**
 * Created by Andrea Nardi on 6/22/2017.
 */
public class GoUpModified implements PathFindingAlgorithm
{

    public static class MyComparator implements Comparator<Vector3i>
    {
        List<Vector2i> path;
        MyComparator(List<Vector2i> path)
        {
            this.path=path;
        }
        public int compare(Vector3i o1, Vector3i o2)
        {
            if(!path.contains(new Vector2i(o1.x,o1.z)) && !path.contains(new Vector2i(o2.x,o2.z)))
            {
                Vector2i startPath=path.get(0);
                int distannceO1=abs(o1.x-startPath.x)+abs(o1.z-startPath.y);
                int distannceO2=abs(o2.x-startPath.x)+abs(o2.z-startPath.y);
                if(distannceO1==distannceO2)
                {
                    return -(o1.y-o2.y);
                }
                else
                {
                    return -(distannceO1-distannceO2);
                }
            }
            else if(!path.contains(new Vector2i(o1.x,o1.z)))
            {
                return -1;
            }
            else if(!path.contains(new Vector2i(o2.x,o2.z)))
            {
                return 1;
            }
            else
            {
                if(path.indexOf(new Vector2i(o1.x,o1.z))==path.indexOf(new Vector2i(o2.x,o2.z)))
                {
                    return -(o1.y-o2.y);
                }
                else
                {
                    return (path.indexOf(new Vector2i(o1.x,o1.z))-path.indexOf(new Vector2i(o2.x,o2.z)));
                }
            }

        }
    }
    public static class MyNode
    {
        public MyNode(int x,int z,int numberAgents,int distance)
        {
            this.x=x;
            this.z=z;
            this.numberAgents=numberAgents;
            this.distance=distance;
        }
        int x;
        int z;
        int numberAgents;
        int distance;
        public int hashCode()
        {
            return x+31*(z+31*(numberAgents));
        }
        public boolean equals(Object o)
        {
            MyNode that=(MyNode)o;
            return (x==that.x) &&
                    (z==that.z) &&
                    (numberAgents==that.numberAgents);
        }
        public String toString()
        {
            return "[("+x+","+z+") numberAgents="+numberAgents+"]";
        }
    }
    public void compute(Vector3f _size, List<Vector3f> _startPosition, List<Vector3f> _endPosition, List<Vector3f> _obstacle, List<List<Movement>> result)
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
        compute(size,startPosition,mainMethods.getStartAndEnd(startPosition,endPosition),obstacle,result);
    }
    public void compute(Vector3i size,List<Vector3i> _startConfiguration ,List<startAndEnd> configuration, List<Vector3i> obstacle, List<List<Movement>> result)
    {
        List<List<Vector2i>> paths=new ArrayList<>(configuration.size());
        List<List<Agent>> agents=new ArrayList<>(configuration.size());
        Array3D<Block> enviroment = new Array3D<>(size, new Empty());
        {
            int _index=0;
            for (startAndEnd sae : configuration)
            {
                List<Vector3i> startPosition = sae.getStart();
                List<Vector3i> endPosition = sae.getEnd();
                List<Agent> agent = new ArrayList<>(sae.getStart().size());
                {
                    Iterator<Vector3i> posIter = startPosition.iterator();

                    while (posIter.hasNext())
                    {
                        Vector3i vec = posIter.next();
                        agent.add(new Agent(_startConfiguration.indexOf(vec), vec,_index));
                    }

                }
                {
                    int i = 0;
                    for (Agent agen : agent)
                    {
                        enviroment.set(agen.pos, agen);
                        i++;
                    }
                }
                for (Vector3i obstacle1 : obstacle)
                {
                    enviroment.set(obstacle1, new Obstacle());
                }
                Array3D<Integer> distance = generateDistanceArray(size, enviroment, sae.getEnd().get(0));
                Agent closest = getHeadPriorityQueue(distance, agent).remove();

                Array2D<Integer> heights = new Array2D<Integer>(new Vector2i(size.x, size.z));
                for (int x = 0; x < heights.size().x; x++)
                {
                    for (int y = 0; y < heights.size().y; y++)
                    {
                        int localHeight = size.y;
                        while (localHeight > 0 && !enviroment.get(new Vector3i(x, localHeight - 1, y)).isObstacle())
                        {
                            localHeight--;

                        }
                        heights.set(new Vector2i(x, y), localHeight);
                    }
                }

                class MyFunctionSet implements PathFinding.FunctionSet<MyNode>
                {

                    @Override
                    public List<PathFinding.WeightedNode<MyNode>> expand(MyNode myNode)
                    {
                        List<PathFinding.WeightedNode<MyNode>> result = new LinkedList<>();
                        List<Vector3i> adjPos = getAdiacentPosition(heights, new Vector2i(myNode.x, myNode.z));
                        for (Vector3i ap : adjPos)
                        {
                            int nAgents = myNode.numberAgents;
                            if (ap.y == heights.get(new Vector2i(myNode.x, myNode.z)))
                            {
                                nAgents = myNode.numberAgents;
                            } else if (ap.y < heights.get(new Vector2i(myNode.x, myNode.z)))
                            {
                                int d = heights.get(new Vector2i(myNode.x, myNode.z)) - ap.y;
                                if (d <= myNode.numberAgents - 2)
                                {
                                    nAgents = myNode.numberAgents;
                                } else
                                {
                                    nAgents = myNode.numberAgents - 1;
                                }
                            } else if (ap.y > heights.get(new Vector2i(myNode.x, myNode.z)))
                            {
                                int d = ap.y - heights.get(new Vector2i(myNode.x, myNode.z));
                                nAgents = myNode.numberAgents - (d * (d + 1)) / 2;
                            }
                            int distance = myNode.distance + 1;
                            if (nAgents >= sae.getEnd().size() && nAgents > 1)
                            {
                                result.add(new PathFinding.WeightedNode<>(new MyNode(ap.x, ap.z, nAgents, distance), distance, distance));
                            }
                        }
                        return result;
                    }

                    @Override
                    public boolean isEnd(MyNode myNode)
                    {
                        return (myNode.x == sae.getEnd().get(0).x && myNode.z == sae.getEnd().get(0).z);
                    }
                }
                List<Vector2i> path = new LinkedList<>();
                {
                    List<MyNode> _path = new PathFinding<MyNode>().compute(new MyNode(closest.pos.x, closest.pos.z, sae.getStart().size(), 0), new MyFunctionSet());
                    for (MyNode node : _path)
                    {
                        path.add(new Vector2i(node.x, node.z));
                    }
                }
                paths.add(path);
                agents.add(agent);

                for(List<Vector2i> ve:paths)
                {
                    System.out.println(ve);
                }
                _index++;
            }
        }
        //TODO end condition
        LinkedList<List<List<Movement>>> bfsMovments=new LinkedList<>();
        for(startAndEnd sae:configuration)
        {
            bfsMovments.add(null);
        }
        while(true)
        {
            Array3D<Block> oldEnviroment = (Array3D<Block>)enviroment.clone();
            List<Movement> step=new LinkedList<>();
            hi:
            for(int i=0;i<configuration.size();i++)
            {
                if (bfsMovments.get(i) == null)
                {
                    Queue<Agent> toBeMoved = getToBeMoved(agents.get(i), paths.get(i));
                    here:
                    while (true)
                    {
                        Agent agent = toBeMoved.poll();
                        if (agent == null)
                        {
                            continue hi;
                        }
                        List<Vector3i> possibleMove = getPossibleMove(enviroment, agent);
                        for (Vector3i move : possibleMove)
                        {
                            Vector3i under = new Vector3i(move.x, move.y - 1, move.z);
                            if (new MyComparator(paths.get(i)).compare(agent.pos, move) < 0 &&
                                    !(oldEnviroment.get(move).isAgent() && ((Agent) oldEnviroment.get(move)).clusterID != agent.clusterID) &&
                                    !(enviroment.isInBound(under) &&
                                            enviroment.get(under).isAgent() &&
                                            ((Agent) (enviroment.get(under))).clusterID != agent.clusterID))
                            {
                                step.add(new Movement(agent.id, new Vector3f(move.x, move.y, move.z)));
                                enviroment.set(agent.pos, new Empty());
                                enviroment.set(move, agent);
                                agent.pos = move;
                                if (enviroment.get(configuration.get(i).getEnd().get(0)).isAgent())
                                {
                                    configuration.get(i).getEnd().remove(0);
                                    List<Agent> movableAgents = new LinkedList<>();
                                    for (Agent a : agents.get(i))
                                    {
                                        if (!a.equals(agent))
                                        {
                                            movableAgents.add(a);
                                        }
                                    }
                                    //List<List<Movement>> _result=new LinkedList<>();
                                    //new BFS().compute(agents.get(i), movableAgents, enviroment, configuration.get(i).getEnd(), _result, 0);
                                    //bfsMovments.set(i,_result);
                                    //return;
                                    //compute(List<Agent<Integer>> agents,List<Agent<Integer>> movableAgents,Array3D<Integer> blocks,List<Vector3i> endPosition,List<List<Movement>> result)
                                }
                                break here;
                            }
                        }
                        System.out.println("hi");
                        //return;
                        //break;
                    }
                }
                else
                {
                    List<Movement> move=((LinkedList<List<Movement>>)bfsMovments.get(i)).remove();
                    for(Movement mov:move)
                    {
                        step.add(mov);
                    }
                }
                if (step.size() != 0)
                {
                    result.add(step);
                }
            }
        }


    }
    private static List<Vector3i> getPossibleMove(Array3D<Block> enviroment, Agent toMove)
    {
        List<Vector3i> result=new LinkedList<>();
        Vector3i pos=toMove.pos;
        if(enviroment.isInBound(new Vector3i(pos.x,pos.y+1,pos.z)) && enviroment.get(new Vector3i(pos.x,pos.y+1,pos.z)).isAgent())
        {
            return result;
        }
        if(!hasAdiacentAgent(enviroment,toMove))
        {
            return result;
        }
        for(int i=-1;i<=1;i+=2)
        {
            {
                Vector3i toUse=new Vector3i(pos.x+i,pos.y,pos.z);
                if(enviroment.isInBound(toUse) && !enviroment.get(toUse).isObstacle())
                {
                    if (enviroment.get(toUse).isAgent())
                    {
                        Vector3i above = new Vector3i(toUse.x, toUse.y + 1, toUse.z);
                        if(enviroment.isInBound(above) && enviroment.get(above).isEmpty())
                        {
                            result.add(above);
                        }
                    }
                    else
                    {
                        Vector3i below=new Vector3i(toUse.x, toUse.y - 1, toUse.z);
                        while(enviroment.isInBound(below) && enviroment.get(below).isEmpty())
                        {
                            toUse.y--;
                            below.y--;
                        }
                        result.add(toUse);
                    }
                }
            }
            {
                Vector3i toUse=new Vector3i(pos.x,pos.y,pos.z+i);
                if(enviroment.isInBound(toUse) && !enviroment.get(toUse).isObstacle())
                {
                    if (enviroment.get(toUse).isAgent())
                    {
                        Vector3i above = new Vector3i(toUse.x, toUse.y + 1, toUse.z);
                        if(enviroment.isInBound(above) && enviroment.get(above).isEmpty())
                        {
                            result.add(above);
                        }
                    }
                    else
                    {
                        Vector3i below=new Vector3i(toUse.x, toUse.y - 1, toUse.z);
                        while(enviroment.isInBound(below) && enviroment.get(below).isEmpty())
                        {
                            toUse.y--;
                            below.y--;
                        }
                        result.add(toUse);
                    }
                }
            }
        }
        return result;
    }
    private static boolean hasAdiacentAgent(Array3D<Block> enviroment, Agent toMove)
    {
        Vector3i pos=toMove.pos;
        for(int i=-1;i<=1;i+=2)
        {
            {
                Vector3i toUse=new Vector3i(pos.x+i,pos.y,pos.z);
                if(enviroment.isInBound(toUse) && enviroment.get(pos).isAgent())
                {
                    return true;
                }
            }
            {
                Vector3i toUse=new Vector3i(pos.x,pos.y,pos.z+1);
                if(enviroment.isInBound(toUse) && enviroment.get(pos).isAgent())
                {
                    return true;
                }
            }
        }
        {
            Vector3i toUse=new Vector3i(pos.x,pos.y-1,pos.z);
            if(enviroment.isInBound(toUse) && enviroment.get(pos).isAgent())
            {
                return true;
            }
        }
        return false;
    }
    private static Queue<Agent> getToBeMoved(List<Agent> agents, List<Vector2i> path)
    {
        PriorityQueue<Agent> result=new PriorityQueue<Agent>(agents.size(), new Comparator<Agent>()
        {
            Comparator original=new MyComparator(path);
            @Override
            public int compare(Agent o1, Agent o2)
            {
                return original.compare(o1.pos,o2.pos);
            }
        });
        for(Agent a:agents)
        {
            result.add(a);
        }
        return result;
    }

    public static List<Vector3i> getAdiacentPosition(Array2D<Integer> heights,Vector2i pos)
    {
        List<Vector3i> result=new LinkedList<>();
        for(int i=-1;i<=1;i+=2)
        {
            {
                Vector2i newPos = new Vector2i(pos.x+i, pos.y);
                if(heights.isInBound(newPos))
                {
                    Vector3i to = new Vector3i(newPos.x, heights.get(newPos), newPos.y);
                    result.add(to);
                }
            }
            {
                Vector2i newPos = new Vector2i(pos.x, pos.y+i);
                if(heights.isInBound(newPos))
                {
                    Vector3i to = new Vector3i(newPos.x, heights.get(newPos), newPos.y);
                    result.add(to);
                }
            }
        }
        return result;
    }
    public static Array3D<Integer> generateDistanceArray(Vector3i size, Array3D<Block> blocks, Vector3i end)
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
    private static void bfsOneStep(Queue<Vector3i>toAdd,Vector3i v,Array3D<Integer> result,Array3D<Block> blocks,int length)
    {
        if(v.y>0 && !blocks.get(new Vector3i(v.x,v.y-1,v.z)).isObstacle())
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
                    while(blocks.isInBound(toUse) && blocks.get(toUse).isObstacle())
                    {
                        toUse.y++;
                    }
                    if(blocks.isInBound(new Vector3i(toUse.x,toUse.y-1,toUse.z)) && !blocks.get(new Vector3i(toUse.x,toUse.y-1,toUse.z)).isObstacle())
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
                    while(blocks.isInBound(toUse) && blocks.get(toUse).isObstacle())
                    {
                        toUse.y++;
                    }
                    if(blocks.isInBound(new Vector3i(toUse.x,toUse.y-1,toUse.z)) && !blocks.get(new Vector3i(toUse.x,toUse.y-1,toUse.z)).isObstacle())
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
    public static Queue<Agent> getHeadPriorityQueue(Array3D<Integer> distance,List<Agent> agents)
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
}
