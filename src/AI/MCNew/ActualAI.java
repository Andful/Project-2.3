package AI.MCNew;

import AI.PathFindingAlgorithm;
import org.joml.Vector3i;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

/**
 * Created by Andrea Nardi on 6/28/2017.
 */
public class ActualAI implements PathFindingAlgorithm
{
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

    private void compute(Vector3i size, List<Vector3i> startPosition, List<Vector3i> endPosition, List<Vector3i> obstacle, List<List<Movement>> result)
    {
        List<startAndEnd> sae=mainMethods.getStartAndEnd(startPosition,endPosition);
    }
}
