package utils;

import java.util.List;

/**
 * Interface for a simple directed graph.
 */
public interface MyGraph<V> {
    void addVertex(V vertex);
    void addEdge(V from, V to);
    boolean hasVertex(V vertex);
    boolean hasEdge(V from, V to);
    List<V> getAdjacent(V vertex);
    List<V> bfs(V start);
}
