package engine.input.generic;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by Andrea Nardi on 6/11/2017.
 */
public class _Mouse implements MouseListener,MouseMotionListener
{
    private volatile boolean left;
    private volatile boolean middle;
    private volatile boolean right;
    private volatile int x;
    private volatile int y;
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e)
    {
        if(e.getButton()==MouseEvent.BUTTON1)
        {
            left=true;
        }
        else if(e.getButton()==MouseEvent.BUTTON2)
        {
            middle=true;
        }
        else if(e.getButton()==MouseEvent.BUTTON3)
        {
            right=true;
        }
    }
    public void mouseReleased(MouseEvent e)
    {
        if(e.getButton()==MouseEvent.BUTTON1)
        {
            left=false;
        }
        else if(e.getButton()==MouseEvent.BUTTON2)
        {
            middle=false;
        }
        else if(e.getButton()==MouseEvent.BUTTON3)
        {
            right=false;
        }
    }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseDragged(MouseEvent e)
    {
        x=e.getX();
        y=e.getY();
    }
    public void mouseMoved(MouseEvent e)
    {
        x=e.getX();
        y=e.getY();
    }
    public boolean getLeft()
    {
        return left;
    }
    public boolean getMiddle()
    {
        return middle;
    }
    public boolean getRight()
    {
        return right;
    }
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
}
