/**
 * Created by Savvas on 6/11/2017.
 */


public class RalstonMethod {

    protected float w0; //initial value approximation
    protected float h; //time step
    Function function;

    public RalstonMethod(float timeStep,Function function, float initialApproximation){
        this.h = timeStep;
        this.function = function;
        this.w0 = initialApproximation;
    }

    public void setTimeStep(float timeStep){
        this.h = timeStep;
    }

    public float getK1(float t, float w){

        float k1 = h*function.getY(t,w);

        return k1;
    }

    public float getK2(float t, float w){

        float k2 = h*function.getY(t + 0.6667f*h, t + 0.6667f*getK1(t,w));

        return k2;
    }

    public float getApproximation(float t, float w){

        float k1 = getK1(t,w);

        float k2 = getK2(t,w);

        float k = 0.25f*k1 + 0.75f*k2;

        float approximation = w0 + k;

        return approximation;
    }


}

