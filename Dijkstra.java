package assign07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Finds minimum-weight paths using Dijkstra's algorithm and a sorted list.
 *
 * @author Ava Murphy & Yujia Zhao
 * @version October 27, 2026
 */
public final class Dijkstra {
    private Dijkstra() { }

    /**
     * Finds a minimum-weight path between two vertices in a weighted graph.
     * Uses a list sorted by descending distance and removes its last element.
     *
     * @param <Type> the type of data stored in each vertex
     * @param graph the graph to search
     * @param source the data of the starting vertex
     * @param destination the data of the destination vertex
     * @return a list of vertex data along a minimum-weight path, including both endpoints
     * @throws IllegalArgumentException if the graph or vertices are invalid, an edge weight is invalid,
     *                                  or the destination is unreachable
     */
    public static <Type> List<Type> search(Graph<Type> graph, Type source, Type destination) {
        if (graph == null || !graph.vertices.containsKey(source)
                || !graph.vertices.containsKey(destination)) {
            throw new IllegalArgumentException("Source or destination is missing");
        }

        for (Vertex<Type> vertex : graph.vertices.values()) {
            vertex.distance = Double.POSITIVE_INFINITY;
            vertex.previous = null;
        }

        Vertex<Type> start = graph.vertices.get(source);
        Vertex<Type> goal = graph.vertices.get(destination);
        start.distance = 0.0;

        List<Vertex<Type>> unvisited = new ArrayList<>(graph.vertices.values());
        Set<Vertex<Type>> visited = new HashSet<>();
        while (!unvisited.isEmpty()) {
            // Descending order: remove the final element (smallest distance).
            unvisited.sort(Comparator.comparingDouble((Vertex<Type> v) -> v.distance).reversed());
            Vertex<Type> current = unvisited.remove(unvisited.size() - 1);
            if (Double.isInfinite(current.distance)) {
                break;
            }
            if (current == goal) {
                break;
            }
            visited.add(current);
            for (Edge<Type> edge : current.outgoing) {
                if (edge.weight < 0 || !Double.isFinite(edge.weight)) {
                    throw new IllegalArgumentException("Dijkstra requires finite nonnegative weights");
                }
                Vertex<Type> next = edge.destination;
                if (!visited.contains(next)) {
                    double candidate = current.distance + edge.weight;
                    if (candidate < next.distance) {
                        next.distance = candidate;
                        next.previous = current;
                    }
                }
            }
        }

        if (Double.isInfinite(goal.distance)) {
            throw new IllegalArgumentException("Destination is unreachable");
        }
        List<Type> path = new ArrayList<>();
        for (Vertex<Type> at = goal; at != null; at = at.previous) {
            path.add(at.data);
        }
        Collections.reverse(path);
        return path;
    }
}
