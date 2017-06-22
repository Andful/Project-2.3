import AI.BFS;
import AI.PathFindingAlgorithm;
import GUI.SerialModifier;
import Util.EnvironmentFloat;
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

/**
 * Created by Andrea Nardi on 6/20/2017.
 */
public class Main
{
    int count=0;
    List<List<PathFindingAlgorithm.Movement>> result=new LinkedList<>();
    PathFindingAlgorithm pfa=new BFS();
    SerialModifier sm;
    public void run() throws Exception
    {
        EnvironmentFloat enviroment=new EnvironmentFloat(new java.io.File("res\\levels\\1\\"));
        JFrame frame= new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Core core;
        frame.add(core=new Core<Integer>(new IGameLogic<Integer>()
        {
            {
                new Thread(){public void run()
                    {
                        pfa.compute(enviroment.environmentSize,enviroment.agentStartConfigurations,enviroment.agentEndConfigurations,enviroment.obstaclesPositions,result);

                    }
                }.start();
            }
            public void update(engine.PositionModifier<Integer> ge, Mouse mouse, Keyboard keyboard)
            {
                if(sm!=null)
                {
                    sm.update();
                    while(count<result.size() && mouse.getRight())
                    {
                        List<PathFindingAlgorithm.Movement> movment=result.get(count);

                        sm.setPosition(movment);
                        count++;
                    }
                }
            }
        }, new ArrayList<Integer>()
        {
            {
                add(0);
                add(1);
                add(2);
                add(3);
            }
        },
                enviroment.agentStartConfigurations, enviroment.obstaclesPositions,new Vector2f(enviroment.environmentSize.x,enviroment.environmentSize.z)));
        frame.pack();
        frame.setVisible(true);
        sm=new SerialModifier(core);
    }
    public static void main(String[] args) throws Exception
    {
        new Main().run();
    }
}
