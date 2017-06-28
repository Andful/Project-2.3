package physics;

import engine.gameObjects.Agent;

import javax.vecmath.Vector3f;
import java.util.List;

/**
 * Created by Andrea Nardi on 6/16/2017.
 */
public class Constants
{
    static final float acceleration=1f;
    static final float dynamicFriction=1f;
    static final float g=0.1f;
    static final float h=0.1f;
    static final float TOL=0.01f;
    static final float GRAVITY=0.1f;
    static final float MASS=1f;

    static Vector3f getAcceleration(Vector3f direction,float surfaceTouching)
    {
        float module=0;
        direction=new Vector3f(direction);
        module=g*dynamicFriction*surfaceTouching;
        direction.normalize();
        direction.scale(module);
        return direction;
    }
}
