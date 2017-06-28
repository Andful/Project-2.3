package physics;

import engine.GameEngine;
import engine.IGameLogic;
import engine.gameObjects.Agent;
import engine.input.special.Keyboard;
import engine.input.special.Mouse;

import javax.swing.*;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

import static java.lang.Math.*;
import static physics.Constants.*;

/**
 * Created by Andrea Nardi on 6/16/2017.
 */
public class Core2<AgentId> extends GameEngine<AgentId>
{
    public List<ToUpdate> tbu=new LinkedList<>();
    public Core2(IGameLogic<AgentId> gl, List<AgentId> agents, List<Vector3f> position, List<Vector3f> obstacle,List<Vector3f> endConfiguration, Vector2f levelDimension)
    {
        super(gl, agents, position, obstacle,endConfiguration, levelDimension);
        pm=new PositionModifier(pm);
    }
    public void update()
    {
        {
            ListIterator<ToUpdate> iter = tbu.listIterator();
            while (iter.hasNext())
            {
                ToUpdate tu = iter.next();
                if (!tu.update(pm))
                {
                    iter.remove();
                }
            }
        }
        super.update();
    }
    public class ToUpdate
    {
        AgentId ai;
        int timeStep;
        List<Vector3f> position;
        ToUpdate(AgentId ai,List<Vector3f> position)
        {
            this.ai=ai;
            this.position=position;
            timeStep=-1;
        }
        public boolean update(engine.PositionModifier<AgentId> pm)
        {
            timeStep++;
            if(timeStep>=position.size())
            {
                return false;
            }
            Vector3f a=position.get(timeStep);
            ((PositionModifier)pm).pm.setPosition(ai,a);
            return true;
        }
    }
    public class PositionModifier implements engine.PositionModifier<AgentId>
    {
        engine.PositionModifier<AgentId> pm;

        PositionModifier(engine.PositionModifier<AgentId> pm)
        {
            this.pm = pm;
        }

        public void setPosition(AgentId aid, Vector3f endPosition)
        {
            List<Vector3f> blocks = new ArrayList<>(agentPosition.keySet().size()+obstacle.size());
            for (AgentId agent : agentPosition.keySet())
            {
                if(!aid.equals(agent))
                {
                    blocks.add(agentPosition.get(agent).position);
                }
            }
            for(Vector3f obst:obstacle)
            {
                blocks.add(obst);
            }
            Agent agent = agentPosition.get(aid);
            Vector3f velocityDirection=new Vector3f(endPosition)
            {
                {
                    sub(agent.position);
                    y=0;
                    normalize();
                }
            };
            float squareDistance=(float)(pow(agent.position.x-endPosition.x,2)+pow(agent.position.z-endPosition.z,2));
            float h=-Constants.h;
            float t=0;
            List<Vector3f> position=new LinkedList<Vector3f>(){{add(endPosition);}};
            List<Vector3f> velocity=new LinkedList<Vector3f>(){{add(new Vector3f());}};
            Function<Vector3f,Vector3f> a=new Function<Vector3f, Vector3f>()
            {
                public Vector3f apply(Vector3f pos)
                {
                    return getAcceleration(pos,velocityDirection,blocks);
                }
            };
            {
                while(position.size()<3)
                {
                    {
                        Vector3f p0=position.get(position.size()-1);
                        Vector3f v0=velocity.get(velocity.size()-1);
                        Vector3f k0=new Vector3f(a.apply(p0)){{scale(h);}};
                        Vector3f l0=new Vector3f(v0){{scale(h);}};
                        Vector3f k1=new Vector3f(a.apply(new Vector3f(p0){{add(new Vector3f());}})){{scale(h);}};
                        Vector3f l1=new Vector3f(position.get(position.size())){{add(new Vector3f(k0){{scale(0.5f);}});scale(h);}};
                        //Vector3f k2=new Vector3f(acceleration){{scale(h);}};
                        Vector3f l2=new Vector3f(position.get(position.size())){{add(new Vector3f(k1){{scale(0.5f);}});scale(h);}};
                        //Vector3f k3=new Vector3f(acceleration){{scale(h);}};
                        //Vector3f l3=new Vector3f(position.get(position.size())){{add(new Vector3f(k2){{scale(0.5f);}});scale(h);}};
                    }
                }
                t+=h;
            }
            do
            {

                //readjustPosition(newPosition,velocityDirection,blocks);
            }
            while(pow(agent.position.x-endPosition.x,2)+pow(agent.position.z-endPosition.z,2)<squareDistance);
            Vector3f newPosition=new Vector3f();
            newPosition.y=agent.position.y;
            if(position.size()>0)
            {
                newPosition.y = position.get(position.size() - 1).y;
            }
            readjustPosition(newPosition,velocityDirection,blocks);
            position.add(newPosition);

            here:
            while(true)
            {
                int hasBottom=0;
                Vector3f thePosition=null;
                Vector3f lastPosition=position.get(position.size()-1);
                if(lastPosition.y<0)
                {
                    lastPosition.y=0;
                    hasBottom=2;
                }
                if(hasBottom==0)
                {
                    for (Vector3f block : blocks)
                    {
                        if (abs(lastPosition.x - block.x) < 1 && abs(lastPosition.z - block.z) < 1 && abs(lastPosition.y - (block.y + 1)) < TOL)
                        {
                            thePosition=block;
                            hasBottom++;
                            if(hasBottom>1)
                            {
                                break;
                            }
                        }
                    }
                }
                if(hasBottom==1)
                {
                    if(!(abs(lastPosition.x-thePosition.x)<0.5 && abs(lastPosition.z-thePosition.z)<0.5))
                    {
                        Vector3f d=new Vector3f(thePosition){{sub(lastPosition);}};
                        d.y=0;
                        Vector3f toMoveTo;
                        if(abs(d.x)>abs(d.z))
                        {
                            toMoveTo=new Vector3f(thePosition){{x-=signum(d.x);}};
                        }
                        else
                        {
                            toMoveTo=new Vector3f(thePosition){{z-=signum(d.z);}};
                        }
                        for(Vector3f block:blocks)
                        {
                            if(abs(toMoveTo.x-block.x)<1 && abs(toMoveTo.y-block.y)<1 && abs(toMoveTo.z-block.z)<1)
                            {
                                break here;
                            }
                        }
                        position.add(toMoveTo);
                        continue;
                    }
                    else
                    {
                        break;
                    }
                }
                else if(hasBottom>1)
                {
                    break;
                }
                else
                {
                    position.add(new Vector3f(lastPosition){{y-=g;}});
                }
            }
            Vector3f pos=position.get(position.size()-1);
            pos.y=Math.round(pos.y);
            tbu.add(new ToUpdate(aid,position));
        }
        public boolean readjustPosition(Vector3f position,Vector3f velocityDirection,List<Vector3f> agents)
        {
            if(!putHigher(position,agents))
            {
                return false;
            }
            boolean hasBottom=false;
            if(position.y<0)
            {
                hasBottom=true;
                position.y=0;
            }
            else
            {
                for (Vector3f agent : agents)
                {
                    if (abs(position.x - agent.x) < 1 && abs(position.z - agent.z) < 1 && abs(position.y - (agent.y - 1)) < TOL)
                    {
                        hasBottom = true;
                        break;
                    }
                }
            }
            if(!hasBottom)
            {
                position.y-=g;
                if(position.y<0)
                {
                    position.y=0;
                }
                for(Vector3f agent:agents)
                {
                    if(abs(position.x-agent.x)<1 && abs(position.y-agent.y)<1 &&abs(position.z-agent.z)<1)
                    {
                        position.y=agent.y+1;
                    }
                }
            }
            return true;
        }
        public boolean putHigher(Vector3f position,List<Vector3f> agents)
        {
            for(Vector3f agent:agents)
            {
                if(abs(position.x-agent.x)<1 && abs(position.y-agent.y)<1 &&abs(position.z-agent.z)<1)
                {
                    position.y+=1;
                    for(Vector3f agent2:agents)
                    {
                        if(abs(position.x-agent.x)<1 && abs(position.y-agent.y)<1 &&abs(position.z-agent.z)<1)
                        {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return true;
        }
    }
    public Vector3f getAcceleration(Vector3f position,Vector3f velocityDirection,List<Vector3f> blocks)
    {
        Vector3f accelerationDirection=new Vector3f(velocityDirection){{scale(-1);}};
        float surfaceArea=0;
        for(Vector3f block:blocks)
        {
            if(abs(position.y-block.y)>1-TOL && abs(position.y-block.y)>1+TOL)
            {
                if(abs(position.x-block.x)<1 && abs(position.z-block.z)<1)
                {
                    surfaceArea+=abs(position.x-block.x)*abs(position.z-block.z);
                }
            }
        }
        float fourceModule=GRAVITY*MASS*dynamicFriction/surfaceArea;
        float accelerationModule=fourceModule/MASS;
        return new Vector3f(accelerationDirection){{scale(accelerationModule);}};
    }
}
