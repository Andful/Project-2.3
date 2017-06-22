package physics;

import engine.GameEngine;
import engine.IGameLogic;
import engine.PositionModifier;
import engine.gameObjects.Agent;
import engine.input.special.Keyboard;
import engine.input.special.Mouse;

import javax.swing.*;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.*;
import static java.lang.Math.*;
import static physics.Constants.*;

/**
 * Created by Andrea Nardi on 6/16/2017.
 */
public class Core<AgentId> extends GameEngine<AgentId>
{
    public List<ToUpdate> tbu=new LinkedList<>();
    public Core(IGameLogic<AgentId> gl, List<AgentId> agents, List<Vector3f> position, List<Vector3f> obstacle, Vector2f levelDimension)
    {
        super(gl, agents, position, obstacle, levelDimension);
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

        public void setPosition(AgentId aid, Vector3f v)
        {
            List<Vector3f> blocks = new ArrayList<>(agentPosition.keySet().size());
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
            Vector3f startPosition=new Vector3f(agent.position);
            List<Vector3f> position;
            Vector3f velocityDirection=new Vector3f(v)
            {
                {
                    sub(agent.position);
                    y=0;
                    normalize();
                }
            };
            Vector3f startVelocity=new Vector3f(velocityDirection)
            {
                {
                    scale(
                            (float)sqrt(2*new Vector3f(agent.position)
                            {
                                {
                                    sub(v);
                                    y=0;
                                }
                            }.length())
                    );
                }
            };
            float totalTime=startVelocity.length()/acceleration;
            position=new ArrayList<Vector3f>((int)(totalTime/h)+1);
            for(float i=0;i<totalTime;i+=h)
            {
                final float time=i;
                Vector3f newPosition=new Vector3f(startPosition){{
                    add(new Vector3f(startVelocity){{scale(time);}});
                    add(new Vector3f(velocityDirection){{scale(-time*time/2);}});
                }};
                if(position.size()>0)
                {
                    newPosition.y = position.get(position.size() - 1).y;
                }
                readjustPosition(newPosition,velocityDirection,blocks);
                position.add(newPosition);
            }
            Vector3f newPosition=new Vector3f(v);
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
            System.out.println(position);
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
    static boolean hi=false;
    public static void main(String[] args)
    {
        JFrame frame= new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Core<Integer>(new IGameLogic<Integer>()
        {
            @Override
            public void update(engine.PositionModifier<Integer> ge, Mouse mouse, Keyboard keyboard)
            {
                if(!hi && mouse.getRight())
                {
                    ge.setPosition(0, new Vector3f(0, 0, 0f));
                    hi=true;
                }
            }
        }, new ArrayList<Integer>()
        {
            {
                add(0);
                add(1);
                add(2);
            }
        },
                new ArrayList<Vector3f>()
                {
                    {
                        add(new Vector3f(0, 2, -1));
                        add(new Vector3f(0, 0, -1));
                        add(new Vector3f(0, 1, -1f));
                    }
                }, new ArrayList<Vector3f>(), new Vector2f(10, 10)));
        frame.pack();
        frame.setVisible(true);
    }
}
