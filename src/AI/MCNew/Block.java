package AI.MCNew;

/**
 * Created by Andrea Nardi on 6/28/2017.
 */
public abstract class Block
{
    public boolean isEmpty()
    {
        return false;
    }
    public boolean isAgent()
    {
        return false;
    }
    public boolean isObstacle()
    {
        return false;
    }
}

class Obstacle extends Block
{
    @Override
    public boolean isObstacle()
    {
        return true;
    }
}

class Empty extends Block
{
    public boolean isEmpty()
    {
        return true;
    }
}
