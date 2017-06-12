package engine.input.special;

import engine.input.generic._Keyboard;

/**
 * Created by Andrea Nardi on 6/12/2017.
 */
public class Keyboard extends _Keyboard
{
    public boolean up()
    {
        return get(81);
    }
    public boolean down()
    {
        return get(90);
    }
    public boolean left()
    {
        return get(65);
    }
    public boolean right()
    {
        return get(68);
    }
    public boolean front()
    {
        return get(87);
    }
    public boolean back()
    {
        return get(83);
    }
    public boolean get(int i)
    {
        return super.get(i);
    }
}
