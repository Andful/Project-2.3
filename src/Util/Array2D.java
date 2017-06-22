package Util;

import org.joml.Vector2i;

/**
 * Created by Andrea Nardi on 5/6/2017.
 */
public class Array2D<E>
{
    private final Vector2i size;
    private final E[][] memory;

    public Array2D(Vector2i size)
    {
        memory=(E[][]) new Object[size.x][size.y];
        this.size=size;
    }
    public Array2D(Vector2i size, E toUse)
    {
        this(size);
        for(int x=0;x<size.x;x++)
        {
            for(int y=0;y<size.y;y++)
            {
                memory[x][y]=toUse;
            }
        }
    }
    public void set(Vector2i pos, E element)
    {
        memory[pos.x][pos.y]=element;
    }
    public E get(Vector2i pos)
    {
        return memory[pos.x][pos.y];
    }
    public boolean isInBound(Vector2i pos)
    {
        return(pos.x>=0 && pos.y>=0 &&  pos.x<memory.length && pos.y<memory[0].length);
    }
    public Vector2i size()
    {
        return size;
    }

    public static void main(String[] args)
    {
        Array2D<Integer> a= new Array2D<>(new Vector2i(5,5));
        a.set(new Vector2i(),5);
        System.out.println(a.get(new Vector2i()));
    }
}
