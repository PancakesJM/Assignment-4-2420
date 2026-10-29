package assign07;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/** Kahn's topological sorting algorithm. */
public final class TopologicalSort {
    private TopologicalSort() { }

    /** Returns a topological ordering; rejects directed cycles. */
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
