package assign07;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a directed, weighted graph using adjacency lists.
 *
 * @param <Type> the type of data stored in each vertex
 * @author Ava Murphy & Yujia Zhao
 * @version October 27, 2026
 */
public class Graph<Type> {
    final Map<Type, Vertex<Type>> vertices;

    /**
     * Constructs an empty directed, weighted graph.
     */
    public Graph() {
        vertices = new LinkedHashMap<>();
    }

    /**
     * Adds a directed edge from the source to the destination.
     * Creates either vertex if it does not already exist.
     *
     * @param source the data of the source vertex
     * @param destination the data of the destination vertex
     * @param weight the nonnegative weight of the edge
     * @throws IllegalArgumentException if either endpoint is null or the weight is negative or non-finite
     */
    public void addEdge(Type source, Type destination, double weight) {
        if (source == null || destination == null || !Double.isFinite(weight) || weight < 0) {
            throw new IllegalArgumentException("Invalid edge endpoint or weight");
        }
        Vertex<Type> from = vertices.computeIfAbsent(source, Vertex::new);
        Vertex<Type> to = vertices.computeIfAbsent(destination, Vertex::new);
        from.outgoing.add(new Edge<>(to, weight));
        to.indegree++;
    }

    /**
     * Finds a shortest path by number of edges using breadth-first search.
     *
     * @param source the data of the starting vertex
     * @param destination the data of the destination vertex
     * @return a list of vertex data along the shortest path, including both endpoints
     * @throws IllegalArgumentException if the vertices are invalid or no path exists
     */
    public List<Type> breadthFirstSearch(Type source, Type destination) {
        return BFS.search(this, source, destination);
    }

    /**
     * Finds a minimum-weight path using Dijkstra's algorithm.
     *
     * @param source the data of the starting vertex
     * @param destination the data of the destination vertex
     * @return a list of vertex data along a minimum-weight path, including both endpoints
     * @throws IllegalArgumentException if the vertices are invalid or no path exists
     */
    public List<Type> dijkstrasAlgorithm(Type source, Type destination) {
        return Dijkstra.search(this, source, destination);
    }

    /**
     * Computes a topological ordering of the graph's vertices.
     *
     * @return a list of vertex data in topological order
     * @throws IllegalArgumentException if the graph contains a directed cycle
     */
    public List<Type> topoSort() {
        return TopologicalSort.sort(this);
    }
}
