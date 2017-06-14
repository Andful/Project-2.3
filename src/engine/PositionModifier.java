package engine;

import javax.vecmath.Vector3f;

/**
 * Created by Andrea Nardi on 6/13/2017.
 */
public interface PositionModifier<AgentId>
{
    public void setPosition(AgentId aid,Vector3f v);
}
