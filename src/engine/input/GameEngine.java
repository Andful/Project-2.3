package engine.input;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;
import engine.Camera;
import engine.input.generic._Keyboard;
import engine.input.generic._Mouse;
import engine.input.special.Keyboard;
import engine.input.special.Mouse;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Andrea Nardi on 6/11/2017.
 */
public class GameEngine<AgentId> extends Canvas3D
{
    SimpleUniverse universe;
    Camera camera=new Camera();

    Mouse mouse=new Mouse();
    Keyboard keyboard=new Keyboard();
    public GameEngine()
    {
        super(SimpleUniverse.getPreferredConfiguration());
        setPreferredSize(new Dimension(640,480));
        universe = new SimpleUniverse(this);
        BranchGroup group = new BranchGroup();
        group.addChild(camera);
        Shape3D cube=new ColorCube(0.3);
        camera.addChild(cube);
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
        toAdd.scale(0.01f);
        camera.position.add(toAdd);

        if(mouse.getLeft())
        {
            Vector3f toRotate = new Vector3f(mouse.getDy(),mouse.getDx(), 0);
            toRotate.scale(0.01f);
            camera.rotation.add(toRotate);
        }

        camera.update();
    }
    public static void main(String[] args)
    {
        JFrame frame= new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GameEngine());
        frame.pack();
        frame.setVisible(true);
    }
}

