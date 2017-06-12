package physics;

import numericalmethods.Function;

import javax.vecmath.Vector3d;
import java.util.List;

/**
 * Created by Savvas on 6/12/2017.
 */
public class Physics {

    List<Vector3d> agents;
    Vector3d agent;
    VelocityFunction velocity = new VelocityFunction(agents, agent);

    public double getImpulseVelocity(double time){
        return velocity.getY(time);
    }



}
