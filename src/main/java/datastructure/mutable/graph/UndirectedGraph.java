package datastructure.mutable.graph;


import java.util.LinkedList;

public class UndirectedGraph extends Graph {

    public UndirectedGraph(int vertices) {
        this.vertices = vertices;
        adjacencyList = new LinkedList[vertices];

        for(int i = 0; i < vertices; i++) {
            adjacencyList[i] = new LinkedList<>();
        }
    }

    public void addEdge(int src, int dest) {
        adjacencyList[src].addFirst(dest);
        adjacencyList[dest].addFirst(src);
    }
}
