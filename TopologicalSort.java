package assign07;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Performs Kahn's topological sort on a directed graph using a FIFO queue.
 *
 * @author Ava Murphy and Yujia Zhao
 * @version October 29, 2026
 */
public final class TopologicalSort {

    /**
     * Prevents instantiation of this utility class.
     */
    private TopologicalSort() {
    }

    /**
     * Orders the graph's vertices so that each source precedes its destinations.
     * The remaining indegree is stored in each vertex, preserving its permanent
     * indegree for later calls to this method.
     *
     * @param <Type> the type of data stored in each vertex
     * @param graph the directed graph to sort
     * @return vertex data in a valid topological order
     * @throws IllegalArgumentException if the graph is null or has a cycle
     */
    public static <Type> List<Type> sort(Graph<Type> graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }

        Queue<Vertex<Type>> ready = new LinkedList<>();
        for (Vertex<Type> vertex : graph.vertices.values()) {
            vertex.remainingIndegree = vertex.indegree;
            if (vertex.remainingIndegree == 0) {
                ready.add(vertex);
            }
        }

        List<Type> order = new ArrayList<>();
        while (!ready.isEmpty()) {
            Vertex<Type> current = ready.remove();
            order.add(current.data);

            for (Edge<Type> edge : current.outgoing) {
                Vertex<Type> next = edge.destination;
                next.remainingIndegree--;
                if (next.remainingIndegree == 0) {
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
