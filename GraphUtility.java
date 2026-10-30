package assign07;

import java.util.List;

/**
 * Provides static entry points for building directed graphs and running
 * shortest-path or topological-sort algorithms on their vertices.
 *
 * @author Ava Murphy and Yujia Zhao
 * @version October 29, 2026
 */
public final class GraphUtility {

    /**
     * Prevents instantiation of this static utility class.
     */
    private GraphUtility() {
    }

    /**
     * Finds a shortest unweighted path by breadth-first search. Each pair of
     * entries at the same index describes one directed edge.
     *
     * @param <Type> the type of data stored in each graph vertex
     * @param sources the source endpoints of the directed edges
     * @param destinations the destination endpoints of the directed edges
     * @param srcData the data of the desired starting vertex
     * @param dstData the data of the desired destination vertex
     * @return a shortest path ordered from srcData to dstData
     * @throws IllegalArgumentException if the edge lists have different sizes,
     *         a requested vertex is missing, or no path exists
     */
    public static <Type> List<Type> shortestPath(List<Type> sources,
            List<Type> destinations, Type srcData, Type dstData) {
        Graph<Type> graph = buildUnweightedGraph(sources, destinations);
        List<Type> path = graph.breadthFirstSearch(srcData, dstData);
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Destination is unreachable");
        }
        return path;
    }

    /**
     * Finds a path of minimum total weight using Dijkstra's algorithm.
     * Each weight corresponds to the edge at the same index in sources and
     * destinations.
     *
     * @param <Type> the type of data stored in each graph vertex
     * @param sources the source endpoints of the directed edges
     * @param destinations the destination endpoints of the directed edges
     * @param weights the nonnegative weights of the matching directed edges
     * @param srcData the data of the desired starting vertex
     * @param dstData the data of the desired destination vertex
     * @return the minimum-weight path ordered from srcData to dstData
     * @throws IllegalArgumentException if list lengths differ, a weight is
     *         invalid, either requested vertex is missing, or no path exists
     */
    public static <Type> List<Type> shortestWeightedPath(List<Type> sources,
            List<Type> destinations, List<Double> weights, Type srcData,
            Type dstData) {
        validateEdgeLists(sources, destinations);
        if (weights == null || weights.size() != sources.size()) {
            throw new IllegalArgumentException("Edge and weight list sizes must match");
        }

        Graph<Type> graph = new Graph<>();
        for (int i = 0; i < sources.size(); i++) {
            Double weight = weights.get(i);
            if (weight == null) {
                throw new IllegalArgumentException("Edge weights cannot be null");
            }
            graph.addEdge(sources.get(i), destinations.get(i), weight);
        }
        return graph.dijkstrasAlgorithm(srcData, dstData);
    }

    /**
     * Produces a valid topological ordering of the vertices described by the
     * matching source and destination edge lists.
     *
     * @param <Type> the type of data stored in each graph vertex
     * @param sources the source endpoints of the directed edges
     * @param destinations the destination endpoints of the directed edges
     * @return vertex data in topological order
     * @throws IllegalArgumentException if list lengths differ or the graph
     *         contains a directed cycle
     */
    public static <Type> List<Type> sort(List<Type> sources,
            List<Type> destinations) {
        return buildUnweightedGraph(sources, destinations).topoSort();
    }

    /**
     * Builds an unweighted graph in which each directed edge has weight 1.0.
     *
     * @param <Type> the type of data stored in each graph vertex
     * @param sources the source endpoints
     * @param destinations the destination endpoints
     * @return the resulting directed graph
     * @throws IllegalArgumentException if either list is null or sizes differ
     */
    private static <Type> Graph<Type> buildUnweightedGraph(List<Type> sources,
            List<Type> destinations) {
        validateEdgeLists(sources, destinations);
        Graph<Type> graph = new Graph<>();
        for (int i = 0; i < sources.size(); i++) {
            graph.addEdge(sources.get(i), destinations.get(i));
        }
        return graph;
    }

    /**
     * Validates the two parallel edge lists before building a graph.
     *
     * @param sources the source endpoints
     * @param destinations the destination endpoints
     * @throws IllegalArgumentException if either list is null or sizes differ
     */
    private static void validateEdgeLists(List<?> sources, List<?> destinations) {
        if (sources == null || destinations == null
                || sources.size() != destinations.size()) {
            throw new IllegalArgumentException("Source and destination list sizes must match");
        }
    }
}
