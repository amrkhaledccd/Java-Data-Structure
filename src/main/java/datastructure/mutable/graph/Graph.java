package datastructure.mutable.graph;

import java.util.LinkedList;


public abstract class Graph {

    protected int vertices;
    protected LinkedList<Integer>[] adjacencyList;

    public abstract void addEdge(int src, int dest);

    public void print()
    {
        for(int v = 0; v < vertices; v++)
        {
            System.out.println("Adjacency list of vertex "+ v);
            System.out.print("head");
            for(Integer pCrawl: adjacencyList[v]){
                System.out.print(" -> "+pCrawl);
            }
            System.out.println("\n");
        }
    }
}
