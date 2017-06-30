import AI.BFS;
import AI.GoUp;
//import AI.MultiAgentPathFinding.MultiAgent;
import AI.MC.*;
import AI.MCNew.*;
import AI.Node;
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
        new Main().run(new BFS(),"res\\levels\\1\\");
    }
    public void run(PathFindingAlgorithm pfa,String enviromentPath) throws Exception
    {
        EnvironmentFloat enviroment=new EnvironmentFloat(new java.io.File(enviromentPath));
        run(pfa,enviroment);
    }
    public void run(PathFindingAlgorithm pfa,EnvironmentFloat enviroment)
    {

        JFrame frame= new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Core core;
        frame.add(core=new Core<Integer>(new IGameLogic<Integer>()
        {
            {
                new Thread(){public void run()
                {
                    long time=System.currentTimeMillis();
                    pfa.compute(enviroment.environmentSize,enviroment.agentStartConfigurations,enviroment.agentEndConfigurations,enviroment.obstaclesPositions,result);
                    System.out.println("time="+(System.currentTimeMillis()-time));
                }
                }.start();
            }
            public void update(engine.PositionModifier<Integer> ge, Mouse mouse, Keyboard keyboard)
            {
                sm.update();
                while(count<result.size() && mouse.getRight())
                {
                    List<PathFindingAlgorithm.Movement> movment = result.get(count);
                    sm.setPosition(movment);
                    count++;
                }
            }
        }, new ArrayList<Integer>()
        {
            {
                for(int i=0;i<enviroment.agentStartConfigurations.size();i++)
                {
                    add(i);
                }
            }
        },
                enviroment.agentStartConfigurations, enviroment.obstaclesPositions,enviroment.agentEndConfigurations,new Vector2f(enviroment.environmentSize.x,enviroment.environmentSize.z)));
        frame.pack();
        frame.setVisible(true);
        sm=new SerialModifier(core);
        core.start();
    }
    public static void main(String[] args) throws Exception
    {
        new Main().run(new AI.MCNew.GoUpModified(),"res\\levels\\1");
    }
}
