package engine.gameObjects;

import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;
import java.awt.*;

/**
 * Created by Andrea Nardi on 6/13/2017.
 */
public class Obstacle extends TransformGroup
{
    public static Node shape;
    public Node thisShape;
    public static void loadMesh(Component component)
    {
        shape= Agent.cubeMesh(component,"res\\obstacle.png");
    }
    public Obstacle(Vector3f position)
    {
        thisShape=shape.cloneNode(true);
        setCapability(ALLOW_TRANSFORM_WRITE);
        addChild(thisShape);
        setTransform(new Transform3D(){{setTranslation(position);}});
    }
}
