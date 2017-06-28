package engine.input;

import com.sun.j3d.utils.geometry.ColorCube;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

/**
 * Created by Andrea Nardi on 6/12/2017.
 */
public class Block extends TransformGroup
{
    public Vector3f position;
    public Block()
    {
        setCapability(ALLOW_TRANSFORM_WRITE);
        addChild(new ColorCube());
    }
    public void update()
    {
        setTransform(new Transform3D(){{setTranslation(position);}});
    }
}
