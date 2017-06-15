package Util;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Andrea Nardi on 5/19/2017.
 */
public class PathFinding<Node>
{
    public static interface FunctionSet<Node>
    {
        List<WeightedNode<Node>> expand(Node node);
    }
    public static class WeightedNode<Node>
    {
        public WeightedNode(Node node,double weight,long distance)
        {
            this.weight=weight;
            this.node=node;
            this.distance=distance;
        }
        public boolean equals(Object o)
        {
            if(o instanceof WeightedNode)
            {
                return node.equals(((WeightedNode)o).node);
            }
            return false;
        }
        public int hashCode(){return node.hashCode();}
        private WeightedNode<Node> parent;
        public long distance;
        public double weight;
        public Node node;
    }
    List<Node> compute(Node start,Node end,FunctionSet<Node> fs)
    {
        class TheComparator implements Comparator<WeightedNode>
        {
            public int compare(WeightedNode o1, WeightedNode o2)
            {
                if(o1.weight<o2.weight) {return -1;}
                if(o1.weight>o2.weight) {return 1;}
                return 0;
            }
        }
        PriorityQueue<WeightedNode<Node>> pq=new PriorityQueue<>(1,new TheComparator());
        HashMap<Node,WeightedNode<Node>> map=new HashMap<>();
        {
            WeightedNode<Node> wn=new WeightedNode<>(start,0,0);
            map.put(wn.node,wn);
            pq.add(wn);
        }
        while(!pq.isEmpty())
        {
            WeightedNode<Node> current=pq.remove();
            if(current.node.equals(end))
            {
                LinkedList<Node> result=new LinkedList<>();
                WeightedNode<Node> temp=current;
                while(temp!=null)
                {
                    result.add(0,temp.node);
                    temp=temp.parent;
                }
                return result;
            }
            List<WeightedNode<Node>> toAdd=fs.expand(current.node);
            for(WeightedNode<Node> wn:toAdd)
            {
                wn.parent=current;
                WeightedNode<Node> wnToCorrect=map.get(wn.node);
                if(wnToCorrect==null)
                {
                    pq.add(wn);
                    map.put(wn.node,wn);
                }
                else
                {
                    if(wnToCorrect.distance>wn.distance)
                    {
                        pq.remove(wnToCorrect);
                        map.put(wn.node,wn);
                        pq.add(wn);
                    }
                }
            }
        }
        return null;
    }
}
