package physics;

import numericalmethods.Function;

import javax.vecmath.Vector3d;
import java.util.List;

/**
 * Created by Savvas on 6/12/2017.
 */
public class VelocityFunction extends Function {

    List<Vector3d> agents;
    Vector3d agent;
    public static final double NORMAL_FORCE = 1.622;

    public VelocityFunction(List<Vector3d> agents, Vector3d agent){
        this.agents = agents;
        this.agent = agent;
    }

    @Override
    public double getY(double time) {

        double m = 0;

        for(int i=0; i<agents.size(); i++){
            if(Math.abs(agents.get(i).y - agent.y) <= 1){
                m = Math.abs((1-Math.abs(agents.get(i).x - agent.x))*(1-Math.abs(agents.get(i).z - agent.z))); //friction constant
            }
        }

        double y = m*NORMAL_FORCE*time;

        return y;
    }
}
