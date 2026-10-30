package assign07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Finds the shortest path in an unweighted directed graph using BFS.
 * Edge weights are ignored when searching for a path.
 *
 * @author Ava Murphy and Yujia Zhao
 * @version October 29, 2026
 */
public final class BFS {

    /**
     * Prevents creation of a BFS object.
     */
    private BFS() {
    }

    /**
     * Finds a shortest path from source to destination by the number of edges.
     * Uses a queue to visit vertices in breadth-first order.
     *
     * @param <Type> the type of data in the graph
     * @param graph the graph to search
     * @param source the starting vertex data
     * @param destination the ending vertex data
     * @return the shortest path from source to destination
     * @throws IllegalArgumentException if a vertex is missing or no path exists
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
        start.distance = 0;

        Queue<Vertex<Type>> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Vertex<Type> current = queue.remove();
            if (current == goal) {
                break;
            }

            for (Edge<Type> edge : current.outgoing) {
                Vertex<Type> next = edge.destination;
                if (Double.isInfinite(next.distance)) {
                    next.distance = current.distance + 1;
                    next.previous = current;
                    queue.add(next);
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
