package engine;

import com.sun.j3d.utils.universe.SimpleUniverse;
import engine.gameObjects.*;
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
    protected volatile IGameLogic<AgentId> gl;

    private SimpleUniverse universe;
    private Camera camera=new Camera();

    private Mouse mouse=new Mouse();
    private Keyboard keyboard=new Keyboard();

    protected Map<AgentId, Agent> agentPosition=new HashMap<>();
    protected List<Vector3f> obstacle;

    public PositionModifier<AgentId> pm=new PositionModifier<AgentId>()
    {
        public void setPosition(AgentId aid,Vector3f v)
        {
            Agent a=agentPosition.get(aid);
            a.position=new Vector3f(v);
            a.update();
        }
        public void setColor(AgentId aid,boolean moved)
        {
            Agent a=agentPosition.get(aid);
            a.moved(moved);
        }
    };

    public GameEngine(IGameLogic<AgentId> gl,List<AgentId> agents,List<Vector3f> position,List<Vector3f> obstacle,List<Vector3f> endPosition,Vector2f levelDimension)
    {
        super(SimpleUniverse.getPreferredConfiguration());
        Agent.loadMesh(this);
        Obstacle.loadMesh(this);
        Start.loadMesh(this);
        this.gl=gl;
        this.obstacle=obstacle;
        setPreferredSize(new Dimension(640,480));
        universe = new SimpleUniverse(this);
        BranchGroup group = new BranchGroup();
        camera.addChild(new Floor(new Vector2f(levelDimension.x,levelDimension.y),this));
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

                Start st=new Start();
                st.position=p;
                st.update();
                camera.addChild(st);
            }
            for(Vector3f bp:obstacle)
            {
                Obstacle ag=new Obstacle(bp);
                camera.addChild(ag);
            }
            End.loadMesh(this);
            for(Vector3f end:endPosition)
            {
                End e=new End();
                e.position=end;
                e.update();
                camera.addChild(e);
            }
        }
        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(group);

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        requestFocus();
        addKeyListener(keyboard);
    }
    public void start()
    {
        System.out.println("timer started");
        new Timer((int)Math.round(1000.0 / 60.0),(ActionEvent e)->update()).start();
    }
    protected  void update()
    {
        Vector3f toAdd=new Vector3f((float)Math.cos(camera.rotation.y)*((keyboard.right()?1:0)+(keyboard.left()?-1:0))+(float)Math.sin(camera.rotation.y)*((keyboard.front()?1:0)+(keyboard.back()?-1:0)),(keyboard.up()?1:0)+(keyboard.down()?-1:0),(float)Math.sin(camera.rotation.y)*((keyboard.right()?1:0)+(keyboard.left()?-1:0))-(float)Math.cos(camera.rotation.y)*((keyboard.front()?1:0)+(keyboard.back()?-1:0)));
        toAdd.scale(0.1f);
        camera.position.add(toAdd);

        if(mouse.getLeft())
        {
            Vector3f toRotate = new Vector3f(mouse.getDy(),mouse.getDx(), 0);
            toRotate.scale(0.01f);
            camera.rotation.add(toRotate);
        }

        camera.update();
        gl.update(pm,mouse,keyboard);
    }
}

