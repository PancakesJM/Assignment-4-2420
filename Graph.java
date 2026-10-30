package assign07;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a sparse, directed graph with generic vertex data and weighted edges.
 * This file contains three cooperating classes:
 * Graph manages the collection of vertices and provides public graph operations;
 * Vertex stores data, adjacency lists, and per-vertex algorithm state; and
 * Edge connects a source vertex to a destination with a nonnegative weight.
 * Only Graph is public; Vertex and Edge are package-private helper classes.
 *
 * @param <Type> the type of data stored in each vertex
 * @author Ava Murphy and Yujia Zhao
 * @version October 29, 2026
 */
public class Graph<Type> {

    final Map<Type, Vertex<Type>> vertices;

    /**
     * Constructs an empty directed graph.
     */
    public Graph() {
        vertices = new LinkedHashMap<>();
    }

    /**
     * Adds an unweighted directed edge, with a default weight of 1.0.
     * The source and destination vertices are created if necessary.
     *
     * @param source the data stored in the source vertex
     * @param destination the data stored in the destination vertex
     * @throws IllegalArgumentException if either vertex data value is null
     */
    public void addEdge(Type source, Type destination) {
        addEdge(source, destination, 1.0);
    }

    /**
     * Adds a weighted directed edge from source to destination, creating either
     * endpoint if it does not already exist in the graph.
     *
     * @param source the data stored in the source vertex
     * @param destination the data stored in the destination vertex
     * @param weight the finite, nonnegative weight of the new edge
     * @throws IllegalArgumentException if either endpoint is null or the weight
     *         is negative, infinite, or NaN
     */
    public void addEdge(Type source, Type destination, double weight) {
        if (source == null || destination == null || !Double.isFinite(weight)
                || weight < 0) {
            throw new IllegalArgumentException("Invalid edge endpoint or weight");
        }

        Vertex<Type> from = vertices.computeIfAbsent(source, Vertex::new);
        Vertex<Type> to = vertices.computeIfAbsent(destination, Vertex::new);

        from.outgoing.add(new Edge<>(to, weight));
        to.indegree++;
    }

    /**
     * Finds a shortest path by number of edges using breadth-first search.
     * Edge weights are ignored.
     *
     * @param source the starting vertex data
     * @param destination the destination vertex data
     * @return the vertex data along the path, from source to destination
     * @throws IllegalArgumentException if a specified vertex does not exist
     */
    public List<Type> breadthFirstSearch(Type source, Type destination) {
        validateEndpoints(source, destination);
        // BFS is implemented separately; its code is left unchanged.
        return BFS.search(this, source, destination);
    }

    /**
     * Finds a minimum-weight path using Dijkstra's algorithm.
     *
     * @param source the starting vertex data
     * @param destination the destination vertex data
     * @return the vertex data along the minimum-weight path
     * @throws IllegalArgumentException if a vertex does not exist or no path
     *         connects source to destination
     */
    public List<Type> dijkstrasAlgorithm(Type source, Type destination) {
        validateEndpoints(source, destination);
        return Dijkstra.search(this, source, destination);
    }

    /**
     * Returns a valid topological ordering for this graph.
     *
     * @return the data of all vertices in topological order
     * @throws IllegalArgumentException if the graph contains a directed cycle
     */
    public List<Type> topoSort() {
        return TopologicalSort.sort(this);
    }

    /**
     * Ensures that both endpoint values name vertices in this graph.
     *
     * @param source the starting vertex data
     * @param destination the destination vertex data
     * @throws IllegalArgumentException if either vertex is absent
     */
    private void validateEndpoints(Type source, Type destination) {
        if (!vertices.containsKey(source) || !vertices.containsKey(destination)) {
            throw new IllegalArgumentException("Source or destination vertex is missing");
        }
    }
}

// Package-private graph helpers are kept in this file with Graph.
class Vertex<Type> {

    final Type data;
    final List<Edge<Type>> outgoing;

    double distance;
    Vertex<Type> previous;
    int indegree;
    int remainingIndegree;

    /**
     * Creates a vertex with no outgoing edges and initialized algorithm state.
     *
     * @param data the data represented by this vertex
     */
    Vertex(Type data) {
        this.data = data;
        this.outgoing = new ArrayList<>();
        this.distance = Double.POSITIVE_INFINITY;
        this.previous = null;
        this.indegree = 0;
        this.remainingIndegree = 0;
    }
}

class Edge<Type> {

    final Vertex<Type> destination;
    final double weight;

    /**
     * Constructs a directed edge to the specified vertex.
     *
     * @param destination the vertex this edge points to
     * @param weight the weight of the edge
     */
    Edge(Vertex<Type> destination, double weight) {
        this.destination = destination;
        this.weight = weight;
    }
}
