package AI;

import Util.Array3D;
import org.joml.Vector3i;

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
public class GoUp
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
            endPosition.add(new Vector3i(round(vec.x),round(vec.y),round(vec.z)));
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

    }
}
