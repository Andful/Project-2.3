package engine;

import com.sun.j3d.utils.universe.SimpleUniverse;
import engine.input.special.Keyboard;
import engine.input.special.Mouse;

import javax.media.j3d.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

/**
 * Created by Andrea Nardi on 6/11/2017.
 */
public class GameEngine<AgentId> extends Canvas3D
{
    IGameLogic<AgentId> gl;

    SimpleUniverse universe;
    Camera camera=new Camera();

    Mouse mouse=new Mouse();
    Keyboard keyboard=new Keyboard();

    Map<AgentId, Agent> agentPosition=new HashMap<>();

    public GameEngine(IGameLogic<AgentId> gl,List<AgentId> agents,List<Vector3f> position,List<Vector3f> obstacle,Vector2f levelDimension)
    {
        super(SimpleUniverse.getPreferredConfiguration());
        Agent.loadMesh(this);
        this.gl=gl;
        setPreferredSize(new Dimension(640,480));
        universe = new SimpleUniverse(this);
        BranchGroup group = new BranchGroup();
        group.addChild(camera);
        {
            Iterator<AgentId> aid=agents.iterator();
            Iterator<Vector3f> pos=position.iterator();
            while(aid.hasNext())
            {
                AgentId a=aid.next();
                Vector3f p=pos.next();
                Agent ag=new Agent();

                ag.position=p;
                ag.update();
                agentPosition.put(a,ag);
                camera.addChild(ag);
            }
            for(Vector3f bp:obstacle)
            {
                Agent ag=new Agent();
                ag.position=bp;
                ag.update();
                camera.addChild(ag);
            }
        }
        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(group);

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        requestFocus();
        addKeyListener(keyboard);

        new Timer((int)Math.round(1000.0 / 60.0),(ActionEvent e)->update()).start();
    }
    public void update()
    {
        Vector3f toAdd=new Vector3f((keyboard.right()?1:0)+(keyboard.left()?-1:0),(keyboard.up()?1:0)+(keyboard.down()?-1:0),(keyboard.back()?1:0)+(keyboard.front()?-1:0));
        toAdd.scale(0.1f);
        camera.position.add(toAdd);

        if(mouse.getLeft())
        {
            Vector3f toRotate = new Vector3f(mouse.getDy(),mouse.getDx(), 0);
            toRotate.scale(0.01f);
            camera.rotation.add(toRotate);
        }

        camera.update();
        gl.update(this,mouse,keyboard);
    }
    public void setPosition(AgentId aid,Vector3f v)
    {
        Agent a=agentPosition.get(aid);
        a.position=new Vector3f(v);
        a.update();
    }
    public static void main(String[] args)
    {
        JFrame frame= new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GameEngine<Integer>(new IGameLogic<Integer>()
        {
            @Override
            public void update(GameEngine<Integer> ge, Mouse mouse, Keyboard keyboard)
            {

            }
        }, new ArrayList<Integer>()
        {
            {
                add(0);
                add(1);
            }
        },
                new ArrayList<Vector3f>()
                {
                    {
                        add(new Vector3f(0, 10, 0));
                        add(new Vector3f(0, 11, 0));
                    }
                }, new ArrayList<Vector3f>(), new Vector2f(10, 10)));
        frame.pack();
        frame.setVisible(true);
    }
}

