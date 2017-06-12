import javax.vecmath.Vector3f;

/**
 * Created by Savvas on 6/11/2017.
 */

/*
Takes the impulse in Newtons. Acceleration is equal to impulse and considered constant.

 */
public class Motion {

    protected static float impulse;
    protected static float acceleration;

    public Motion(float impulse){
        this.impulse = impulse;
        this.acceleration = impulse;
    }

    public void setImpulse(float impulse){
        this.impulse = impulse;
        this.acceleration = impulse;
    }

    //r = r0 + 0.5*a*t^2
    public static Vector3f getPosition(Vector3f initialPosition, float time){

        Vector3f position = new Vector3f(initialPosition);

        float dummy = 0.5f*acceleration*time*time;

        position.x+=dummy;
        position.y+=dummy;
        position.z+=dummy;

        return position;
    }

    public static void main(String[] args){

        Motion motion = new Motion(10);

        Vector3f test = new Vector3f(getPosition(new Vector3f(0,0,0), 1));

        System.out.print(test); //works

    }
}
