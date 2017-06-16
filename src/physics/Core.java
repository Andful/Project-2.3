package physics;

import engine.GameEngine;
import engine.IGameLogic;
import engine.PositionModifier;
import engine.input.special.Keyboard;
import engine.input.special.Mouse;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.*;

/**
 * Created by Andrea Nardi on 6/16/2017.
 */
public class Core<AgentId> extends GameEngine<AgentId>
{
    public static class Agent<AgentId>
    {
        Agent(AgentId id, Vector3f pos)
        {
            this.id=id;
            this.pos=pos;
        }
        AgentId id;
        Vector3f pos;
    }

    public static class PhysicsGameLogic<AgentId> implements PositionModifier<AgentId>
    {
        PositionModifier<AgentId> pm;

        PhysicsGameLogic(PositionModifier<AgentId> pm)
        {
            this.pm=pm;
        }

        public void setPosition(AgentId aid, Vector3f v)
        {
            Core.this.
        }
    }

    private List<Agent<AgentId>> agents;
    private Map<AgentId,Agent> map=new HashMap<>();

    public Core(IGameLogic<AgentId> gl, List<AgentId> agents, List<Vector3f> position, List<Vector3f> obstacle, Vector2f levelDimension)
    {
        super(gl,agents,position,obstacle,levelDimension);
        this.agents=new ArrayList<Agent<AgentId>>(agents.size());
        {
            Iterator<AgentId> ai=agents.iterator();
            Iterator<Vector3f> pos=position.iterator();
            while(ai.hasNext())
            {
                Agent<AgentId> agent=new Agent<>(ai.next(),pos.next());
                this.agents.add(agent);
                map.put(agent.id,agent);
            }
        }
    }
    public void update()
    {

    }

}
