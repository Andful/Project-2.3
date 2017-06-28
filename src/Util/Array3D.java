package Util;

import org.joml.Vector3i;

/**
 * Created by Andrea Nardi on 5/6/2017.
 */
public class Array3D<E>
{
    private final Vector3i size;
    private final E[][][] memory;

    public Array3D(Vector3i size)
    {
        memory=(E[][][]) new Object[size.x][size.y][size.z];
        this.size=size;
    }
    public Array3D(Vector3i size, E toUse)
    {
        this(size);
        for(int x=0;x<size.x;x++)
        {
            for(int y=0;y<size.y;y++)
            {
                for(int z=0;z<size.z;z++)
                {
                    memory[x][y][z]=toUse;
                }
            }
        }
    }
    public void set(Vector3i pos, E element)
    {
        memory[pos.x][pos.y][pos.z]=element;
    }
    public E get(Vector3i pos)
    {
        return memory[pos.x][pos.y][pos.z];
    }
    public boolean isInBound(Vector3i pos)
    {
        return(pos.x>=0 && pos.y>=0 && pos.z>=0 && pos.x<memory.length && pos.y<memory[0].length && pos.z<memory[0][0].length);
    }
    public Vector3i size()
    {
        return size;
    }

    public static void main(String[] args)
    {
        Array3D<Integer> a= new Array3D<>(new Vector3i(5,5,5));
        a.set(new Vector3i(),5);
        System.out.println(a.get(new Vector3i()));
    }

    public Object clone()
    {
        Array3D<E> result=new Array3D<E>(size);
        for(int i=0;i<size.x;i++)
        {
            for(int j=0;j<size.y;j++)
            {
                for(int k=0;k<size.z;k++)
                {
                    result.memory[i][j][k]=memory[i][j][k];
                }
            }
        }
        return result;
    }
    public int hashCode()
    {
        return memory[0][0][0].hashCode();
    }
    public boolean equals(Object o)
    {
        Array3D<E> toCompare=(Array3D<E>)o;
        for(int i=0;i<size.x;i++)
        {
            for(int j=0;j<size.y;j++)
            {
                for (int k = 0; k < size.z; k++)
                {
                    if(memory[i][j][k]!=toCompare.memory[i][j][k])
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
