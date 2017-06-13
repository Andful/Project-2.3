package engine;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

/**
 * Created by Andrea Nardi on 6/11/2017.
 */
public class Camera extends TransformGroup
{
    public Vector3f position;
    public Vector3f rotation;
    public Camera()
    {
        position=new Vector3f();
        rotation=new Vector3f();
        setCapability(ALLOW_TRANSFORM_WRITE);
    }
    public void update()
    {
        Transform3D result=new Transform3D();
        result.mul(new Transform3D(){{rotX(rotation.x);}});
        result.mul(new Transform3D(){{rotY(rotation.y);}});
        result.mul(new Transform3D(){{setTranslation(new Vector3f(position){{scale(-1);}});}});
        setTransform(result);
    }
}
