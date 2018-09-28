package datastructure.mutable.graph;


import java.util.LinkedList;

public class DirectedGraph extends Graph{

    public DirectedGraph(int vertices) {
        this.vertices = vertices;
        adjacencyList = new LinkedList[vertices];

        for(int i = 0; i < vertices; i++) {
            adjacencyList[i] = new LinkedList<>();
        }
    }

    public void addEdge(int src, int dest) {
        adjacencyList[src].addFirst(dest);
    }
}
