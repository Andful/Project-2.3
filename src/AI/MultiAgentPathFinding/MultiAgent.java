package AI.MultiAgentPathFinding;

import AI.PathFindingAlgorithm;
import Util.Array3D;
import Util.PathFinding;
import org.joml.Vector3i;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.round;
import static AI.BFS.*;

/**
 * Created by Andrea Nardi on 6/25/2017.
 */
public class MultiAgent implements PathFindingAlgorithm
{
    public class AgentList extends ArrayList<Agent<Integer>>
    {
        public AgentList(int size)
        {
            super(size);
        }
        public Object clone()
        {
            AgentList result=new AgentList(this.size());
            for(Agent<Integer> agent:this)
            {
                result.add(agent);
            }
            return result;
        }
    }
    public void compute(Vector3f _size, List<Vector3f> _startPosition, List<Vector3f> _endPosition, List<Vector3f> _obstacle, List<List<PathFindingAlgorithm.Movement>> result)
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
    public void compute(Vector3i size, List<Vector3i> startPosition, List<Vector3i> endPosition, List<Vector3i> obstacle, List<List<PathFindingAlgorithm.Movement>> result)
    {
        Array3D<Integer> enviroment=getEnviroment(size,startPosition,obstacle);
        AgentList agents=new AgentList(startPosition.size());
        for(int i=0;i<startPosition.size();i++)
        {
            agents.add(new Agent<Integer>(i,startPosition.get(i)));
        }

        Vector3f avrageEndPoint=getAvragePoint(endPosition);

        class MyNode
        {
            int distance;
            AgentList agentList;
            Array3D<Integer> enviroment;
            Movement movment;
            MyNode(int distance,
                    AgentList agentList,
                    Array3D<Integer> enviroment,
                    Movement movment)
            {
                this.distance=distance;
                this.agentList=agentList;
                this.enviroment=enviroment;
                this.movment=movment;
            }
            public int hashCode()
            {
                return agentList.hashCode();
            }
            public boolean equals(Object o)
            {
                return agentList.equals(((MyNode)o).agentList);
            }
        }

        class MyFunctionSet implements PathFinding.FunctionSet<MyNode>
        {
            public List<PathFinding.WeightedNode<MyNode>> expand(MyNode myNode)
            {
                List<PathFinding.WeightedNode<MyNode>> result=new LinkedList<>();
                for(int i=0;i<myNode.agentList.size();i++)
                {
                    Agent<Integer> agent=myNode.agentList.get(i);
                    List<Vector3i> possibleMoves=getPossibleMove(agent,myNode.enviroment);
                    for(Vector3i move :possibleMoves)
                    {
                        AgentList nAgentList=(AgentList)(myNode.agentList.clone());
                        nAgentList.set(i,new Agent<Integer>(agent.id,move));
                        Array3D<Integer> nEnviroment=(Array3D<Integer>)myNode.enviroment.clone();
                        nEnviroment.set(agent.pos,EMPTY);
                        nEnviroment.set(move,AGENT);
                        Movement nMovement=new Movement(agent.id,new Vector3f(move.x,move.y,move.z));
                        MyNode nNode=new MyNode(myNode.distance+1,nAgentList,nEnviroment,nMovement);
                        long distance=myNode.distance+1;
                        double manattanDistance=new Vector3f(avrageEndPoint){{sub(getAvragePoint(nAgentList));}}.length();
                        result.add(new PathFinding.WeightedNode<MyNode>(nNode,manattanDistance+distance,distance));
                    }

                }
                return result;
            }
            public boolean isEnd(MyNode myNode)
            {
                for(Vector3i end:endPosition)
                {
                    if(myNode.enviroment.get(end)!=AGENT)
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        List<MyNode> res=new PathFinding<MyNode>().compute(new MyNode(0,agents,enviroment,null),new MyFunctionSet());
        for(MyNode a:res)
        {
            System.out.println(a.movment);
            if(a.movment!=null)
            {
                result.add(new ArrayList<Movement>(1){{add(a.movment);}});
            }
        }
    }
    public List<Vector3i> getPossibleMove(Agent<Integer> agent,Array3D<Integer> enviroment)
    {
        LinkedList<Vector3i> result=new LinkedList<>();
        Vector3i pos=agent.pos;
        {
            for(int i=-1;i<=1;i+=2)
            {
                {
                    Vector3i to=getPosition(pos,new Vector3i(pos.x+i,pos.y,pos.z),enviroment);
                    if(to!=null)
                    {
                        result.add(to);
                    }
                }
                {
                    Vector3i to=getPosition(pos,new Vector3i(pos.x,pos.y,pos.z+i),enviroment);
                    if(to!=null)
                    {
                        result.add(to);
                    }
                }
            }
        }
        return result;
    }

    private Vector3i getPosition(Vector3i from, Vector3i to,Array3D<Integer> enviroment)
    {

        to = new Vector3i(to);
        {
            Vector3i toCheck=new Vector3i(from.x,from.y+1,from.z);
            if(enviroment.isInBound(toCheck) && enviroment.get(toCheck)==AGENT)
            {
                return null;
            }
        }
        if(!enviroment.isInBound(to))
        {
            return null;
        }
        else if(enviroment.get(to)==OBSTACLE)
        {
            return null;
        }
        else if(enviroment.get(to)==AGENT)
        {
            Vector3i toCheck=new Vector3i(to.x,to.y+1,to.z);
            if(enviroment.isInBound(toCheck))
            {
                if(enviroment.get(toCheck)!=EMPTY)
                {
                    return null;
                }
                else
                {
                    return toCheck;
                }
            }
            else
            {
                return null;
            }
        }
        else
        {
            Vector3i toNotCheck=new Vector3i(to).sub(from).mul(-1).add(from);
            LinkedList<Vector3i> toCheck=new LinkedList<>();
            for(int i=-1;i<=1;i+=2)
            {
                {
                    Vector3i toAdd = new Vector3i(from.x+i, from.y, from.z);
                    if(!toAdd.equals(toNotCheck))
                    {
                        toCheck.add(toAdd);
                    }
                }
                {
                    Vector3i toAdd = new Vector3i(from.x, from.y, from.z+i);
                    if(!toAdd.equals(toNotCheck))
                    {
                        toCheck.add(toAdd);
                    }
                }
            }
            {
                Vector3i under=new Vector3i(from.x,from.y-1,from.z);
                toCheck.add(under);
            }
            for(Vector3i v:toCheck)
            {
                if(enviroment.isInBound(v) && enviroment.get(v)==AGENT)
                {
                    Vector3i under=new Vector3i(to.x,to.y-1,to.z);
                    while(under.y>=0 && enviroment.get(under)==EMPTY)
                    {
                        to.y--;
                        under.y--;
                    }
                    return to;
                }
            }
            return null;
        }
    }

    public Array3D<Integer> getEnviroment(Vector3i size, List<Vector3i> startPosition,List<Vector3i> obstacle)
    {
        Array3D<Integer> result=new Array3D<Integer>(size,EMPTY);
        for(Vector3i v:startPosition)
        {
            result.set(v,AGENT);
        }
        for(Vector3i v:obstacle)
        {
            result.set(v,OBSTACLE);
        }
        return result;
    }

    public Vector3f getAvragePoint(List<Vector3i> endPosition)
    {
        Vector3f result=new Vector3f();
        for(Vector3i v:endPosition)
        {
            result.x+=v.x;
            result.y+=v.y;
            result.z+=v.z;
        }
        result.scale(1.0f/endPosition.size());
        return result;
    }
    public Vector3f getAvragePoint(AgentList endPosition)
    {
        Vector3f result=new Vector3f();
        for(Agent<Integer> a:endPosition)
        {
            Vector3i v=a.pos;
            result.x+=v.x;
            result.y+=v.y;
            result.z+=v.z;
        }
        result.scale(1.0f/endPosition.size());
        return result;
    }
}
