package assign07;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Computes topological orderings of directed graphs using Kahn's algorithm.
 *
 * @author Ava Murphy & Yujia Zhao
 * @version October 27, 2026
 */
public final class TopologicalSort {
    private TopologicalSort() { }

    /**
     * Returns a topological ordering of the graph using vertex indegrees.
     * Uses local indegree counts so the graph can be sorted repeatedly.
     *
     * @param <Type> the type of data stored in each vertex
     * @param graph the graph to sort
     * @return a list of vertex data in topological order
     * @throws IllegalArgumentException if the graph is null or contains a directed cycle
     */
    public static <Type> List<Type> sort(Graph<Type> graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }

        // Work on local counts so repeated calls do not damage stored indegrees.
        Map<Vertex<Type>, Integer> remaining = new HashMap<>();
        Queue<Vertex<Type>> ready = new LinkedList<>();
        for (Vertex<Type> vertex : graph.vertices.values()) {
            remaining.put(vertex, vertex.indegree);
            if (vertex.indegree == 0) {
                ready.add(vertex);
            }
        }

        List<Type> order = new ArrayList<>();
        while (!ready.isEmpty()) {
            Vertex<Type> current = ready.remove();
            order.add(current.data);
            for (Edge<Type> edge : current.outgoing) {
                Vertex<Type> next = edge.destination;
                int count = remaining.get(next) - 1;
                remaining.put(next, count);
                if (count == 0) {
                    ready.add(next);
                }
            }
        }
        if (order.size() != graph.vertices.size()) {
            throw new IllegalArgumentException("Graph contains a directed cycle");
        }
        return order;
    }
}
