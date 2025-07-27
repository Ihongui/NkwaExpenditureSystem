package utils;

import java.util.*;

/**
 * Adjacency list implementation of a directed graph.
 */
public class AdjacencyListGraph<V> implements MyGraph<V> {
    private final MyMap<V, MyList<V>> adjList;

    public AdjacencyListGraph() {
        this.adjList = new MyHashMap<>();
    }

    @Override
    public void addVertex(V vertex) {
        if (!adjList.containsKey(vertex)) {
            adjList.put(vertex, new MyArrayList<>());
        }
    }

    @Override
    public void addEdge(V from, V to) {
        addVertex(from);
        addVertex(to);
        adjList.get(from).add(to);
    }

    @Override
    public boolean hasVertex(V vertex) {
        return adjList.containsKey(vertex);
    }

    @Override
    public boolean hasEdge(V from, V to) {
        if (!adjList.containsKey(from)) return false;
        MyList<V> neighbors = adjList.get(from);
        for (int i = 0; i < neighbors.size(); i++) {
            if (neighbors.get(i).equals(to)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<V> getAdjacent(V vertex) {
        List<V> result = new ArrayList<>();
        if (adjList.containsKey(vertex)) {
            MyList<V> neighbors = adjList.get(vertex);
            for (int i = 0; i < neighbors.size(); i++) {
                result.add(neighbors.get(i));
            }
        }
        return result;
    }

    @Override
    public List<V> bfs(V start) {
        List<V> visited = new ArrayList<>();
        Set<V> seen = new HashSet<>();
        Queue<V> queue = new LinkedList<>();

        if (!adjList.containsKey(start)) return visited;

        queue.add(start);
        seen.add(start);

        while (!queue.isEmpty()) {
            V current = queue.poll();
            visited.add(current);
            MyList<V> neighbors = adjList.get(current);
            for (int i = 0; i < neighbors.size(); i++) {
                V neighbor = neighbors.get(i);
                if (!seen.contains(neighbor)) {
                    queue.add(neighbor);
                    seen.add(neighbor);
                }
            }
        }
        return visited;
    }
}
