package engine.input.generic;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Andrea Nardi on 6/12/2017.
 */
public class _Keyboard implements KeyListener
{
    private boolean mem[];
    public _Keyboard()
    {
        mem=new boolean[Character.MAX_VALUE+1];
    }
    public boolean get(int i)
    {
        return mem[i];
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e)
    {
        mem[e.getKeyCode()]=true;
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        mem[e.getKeyCode()]=false;
    }
}
