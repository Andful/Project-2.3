package AI;

import Util.Array2D;
import Util.Array3D;
import Util.PathFinding;
import org.joml.Vector2i;
import org.joml.Vector3i;
import org.omg.CORBA.OBJ_ADAPTER;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.round;
import static AI.BFS.*;

/**
 * Created by Andrea Nardi on 6/22/2017.
 */
public class GoUp implements PathFindingAlgorithm
{

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
            return x+31*(z+31*(numberAgents+31*distance));
        }
        public boolean equals(Object o)
        {
            MyNode that=(MyNode)o;
            return (x==that.x) &&
                    (z==that.z) &&
                    (numberAgents==that.numberAgents) &&
                    (distance==that.distance);
        }
        public String toString()
        {
            return "[("+x+","+z+") numberAgents="+numberAgents+"]";
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
        List<Agent<Integer>> agents=new ArrayList<>(_startPosition.size());
        {
            Iterator<Vector3i> posIter=startPosition.iterator();
            int i=0;
            do
            {
                Vector3i vec=posIter.next();
                agents.add(new BFS.Agent<>(i,vec));
                i++;
            }
            while(posIter.hasNext());
        }
        Array3D<Integer> enviroment=new Array3D<Integer>(size,EMPTY);
        for(Vector3i obstacle1:obstacle)
        {
            enviroment.set(obstacle1,OBSTACLE);
        }
        Agent closest=getHeadPriorityQueue(enviroment,agents).remove();

        Array2D<Integer> heights=new Array2D<Integer>(new Vector2i(size.x,size.z));
        for(int x=0;x<heights.size().x;x++)
        {
            for(int y=0;y<heights.size().y;y++)
            {
                int localHeight=size.y;
                while(localHeight>0 && enviroment.get(new Vector3i(x,localHeight-1,y))!= OBSTACLE)
                {
                    localHeight--;

                }
                heights.set(new Vector2i(x,y),localHeight);
            }
        }

        class MyFunctionSet implements PathFinding.FunctionSet<MyNode>
        {

            @Override
            public List<PathFinding.WeightedNode<MyNode>> expand(MyNode myNode)
            {
                List<PathFinding.WeightedNode<MyNode>> result=new LinkedList<>();
                List<Vector3i> adjPos=getAdiacentPosition(heights,new Vector2i(myNode.x,myNode.z));
                for(Vector3i ap:adjPos)
                {
                    int nAgents=myNode.numberAgents;
                    if(ap.y==heights.get(new Vector2i(myNode.x,myNode.z)))
                    {
                        nAgents=myNode.numberAgents;
                    }
                    else if(ap.y<heights.get(new Vector2i(myNode.x,myNode.z)))
                    {
                        int d=heights.get(new Vector2i(myNode.x,myNode.z))-ap.y;
                        if(d<=myNode.numberAgents-2)
                        {
                            nAgents=myNode.numberAgents;
                        }
                        else
                        {
                            nAgents = myNode.numberAgents - 1;
                        }
                    }
                    else if(ap.y>heights.get(new Vector2i(myNode.x,myNode.z)))
                    {
                        int d=ap.y-heights.get(new Vector2i(myNode.x,myNode.z));
                        nAgents=myNode.numberAgents-(d*(d+1))/2;
                    }
                    int distance=myNode.distance+1;
                    System.out.println(nAgents+" "+endPosition.size());
                    if(nAgents>=endPosition.size() && nAgents>1)
                    {
                        result.add(new PathFinding.WeightedNode<>(new MyNode(ap.x,ap.z,nAgents,distance),distance,distance));
                    }
                }
                return result;
            }

            @Override
            public boolean isEnd(MyNode myNode)
            {
                return (myNode.x==endPosition.get(0).x && myNode.z==endPosition.get(0).z);
            }
        }
        List<MyNode> paths=new PathFinding<MyNode>().compute(new MyNode(closest.pos.x,closest.pos.y,agents.size(),0),new MyFunctionSet());
        System.out.println(paths);

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
}
