package assign07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Dijkstra's algorithm using a sorted list instead of a priority queue. */
public final class Dijkstra {
    private Dijkstra() { }

    /** Finds a minimum-weight path in a graph with nonnegative edge weights. */
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
