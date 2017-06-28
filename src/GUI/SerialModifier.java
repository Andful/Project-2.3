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
}
