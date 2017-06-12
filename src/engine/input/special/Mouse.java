package engine.input.special;

import engine.input.generic._Mouse;

import java.awt.event.MouseEvent;

/**
 * Created by Andrea Nardi on 6/12/2017.
 */
public class Mouse extends _Mouse
{
    volatile int oldX;
    volatile int oldY;
    public void mousePressed(MouseEvent e)
    {
        oldX=getX();
        oldY=getY();
        super.mousePressed(e);
    }
    public void mouseDragged(MouseEvent e)
    {
        oldX=getX();
        oldY=getY();
        super.mouseDragged(e);
    }
    public void mouseMoved(MouseEvent e)
    {
        oldX=getX();
        oldY=getY();
        super.mouseMoved(e);
    }
    public int getDx()
    {
        int tempOldX=oldX;
        oldX=getX();
        return getX()-tempOldX;
    }
    public int getDy()
    {
        int tempOldY=oldY;
        oldY=getY();
        return getY()-tempOldY;
    }
}
