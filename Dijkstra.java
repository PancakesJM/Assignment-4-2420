package assign07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Finds shortest weighted paths with Dijkstra's algorithm using a sorted list
 * of unvisited vertices rather than a priority queue.
 *
 * @author Ava Murphy and Yujia Zhao
 * @version October 29, 2026
 */
public final class Dijkstra {

    /**
     * Prevents instantiation of this utility class.
     */
    private Dijkstra() {
    }

    /**
     * Finds a minimum-weight directed path between two existing vertices.
     * On each iteration, the unvisited list is sorted by descending distance,
     * and its last vertex (the smallest distance) is visited next.
     *
     * @param <Type> the type of data stored in the graph vertices
     * @param graph the graph to search
     * @param source the starting vertex data
     * @param destination the destination vertex data
     * @return the minimum-weight path from source to destination
     * @throws IllegalArgumentException if the graph or either vertex is missing,
     *         the destination cannot be reached, or an edge weight is invalid
     */
    public static <Type> List<Type> search(Graph<Type> graph, Type source,
            Type destination) {
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
            // Descending order places the smallest distance at the end.
            unvisited.sort(Comparator.comparingDouble(
                    (Vertex<Type> vertex) -> vertex.distance).reversed());
            Vertex<Type> current = unvisited.remove(unvisited.size() - 1);

            if (Double.isInfinite(current.distance)) {
                break;
            }
            if (current == goal) {
                break;
            }

            visited.add(current);
            for (Edge<Type> edge : current.outgoing) {
                if (!Double.isFinite(edge.weight) || edge.weight < 0) {
                    throw new IllegalArgumentException(
                            "Dijkstra requires finite, nonnegative edge weights");
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
        for (Vertex<Type> current = goal; current != null;
                current = current.previous) {
            path.add(current.data);
        }
        Collections.reverse(path);
        return path;
    }
}
