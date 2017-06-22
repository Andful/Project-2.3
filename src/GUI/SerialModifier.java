package GUI;

import engine.GameEngine;
import engine.IGameLogic;
import engine.input.special.Keyboard;
import engine.input.special.Mouse;
import physics.Core;

import javax.swing.*;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import static AI.PathFindingAlgorithm.*;

/**
 * Created by Andrea Nardi on 6/19/2017.
 */
public class SerialModifier
{
    Core<Integer> ge;
    Queue<List<Movement>> queue = new LinkedList<>();
    public SerialModifier(Core<Integer> ge)
    {
        this.ge=ge;
    }
    public void setPosition(Integer a,Vector3f position)
    {
        queue.add(new LinkedList<Movement>(){{add(new Movement(a,position));}});
    }
    public void setPosition(List<Movement> list)
    {
        queue.add(list);
    }
    public void update()
    {
        if(ge.tbu.isEmpty() && !queue.isEmpty())
        {
            for(Movement m:queue.remove())
            {
                ge.pm.setPosition(m.id,m.to);
            }
        }
    }
    public static void main(String[] args)
    {
        JFrame frame= new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Core core;
        final List<SerialModifier> sm=new LinkedList<>();
        frame.add(core=new Core<Integer>(new IGameLogic<Integer>()
        {
            boolean hi=false;
            public void update(engine.PositionModifier<Integer> ge, Mouse mouse, Keyboard keyboard)
            {
                if(!sm.isEmpty())
                {
                    sm.get(0).update();
                    if(!hi && mouse.getRight())
                    {
                        hi=true;
                        sm.get(0).setPosition(2,new Vector3f(0,0,0));
                        sm.get(0).setPosition(1,new Vector3f(0,0,0));
                        sm.get(0).setPosition(0,new Vector3f(0,0,0));
                    }
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
                        add(new Vector3f(0, 0, -1));
                        add(new Vector3f(0, 1, -1));
                        add(new Vector3f(0, 2, -1f));
                    }
                }, new ArrayList<Vector3f>(), new Vector2f(10, 10)));
        frame.pack();
        frame.setVisible(true);
        sm.add(new SerialModifier(core));
    }
}
