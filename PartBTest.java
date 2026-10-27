package assign07;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

/** Tests for the Part B implementation; do not submit with assignment sources. */
public class PartBTest {
    private Graph<String> weightedGraph() {
        Graph<String> g = new Graph<>();
        g.addEdge("A", "B", 9);
        g.addEdge("A", "C", 2);
        g.addEdge("C", "B", 1);
        g.addEdge("B", "D", 3);
        g.addEdge("C", "D", 10);
        return g;
    }

    @Test void dijkstraChoosesLowestWeightNotFewestEdges() {
        assertEquals(List.of("A", "C", "B", "D"), weightedGraph().dijkstrasAlgorithm("A", "D"));
    }

    @Test void dijkstraSameSourceAndDestination() {
        assertEquals(List.of("A"), weightedGraph().dijkstrasAlgorithm("A", "A"));
    }

    @Test void dijkstraResetsStateBetweenCalls() {
        Graph<String> g = weightedGraph();
        assertEquals(List.of("A", "C", "B", "D"), g.dijkstrasAlgorithm("A", "D"));
        assertEquals(List.of("C", "B", "D"), g.dijkstrasAlgorithm("C", "D"));
    }

    @Test void dijkstraRejectsMissingOrUnreachableVertices() {
        Graph<String> g = weightedGraph();
        g.addEdge("Z", "Y", 1);
        assertThrows(IllegalArgumentException.class, () -> g.dijkstrasAlgorithm("A", "Z"));
        assertThrows(IllegalArgumentException.class, () -> g.dijkstrasAlgorithm("A", "missing"));
    }

    @Test void graphRejectsNegativeAndNonFiniteWeights() {
        Graph<String> g = new Graph<>();
        assertThrows(IllegalArgumentException.class, () -> g.addEdge("A", "B", -1));
        assertThrows(IllegalArgumentException.class, () -> g.addEdge("A", "B", Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> g.addEdge("A", "B", Double.POSITIVE_INFINITY));
    }

    @Test void topoRespectsDependenciesAndCanRunTwice() {
        Graph<String> g = weightedGraph();
        List<String> first = g.topoSort();
        assertEquals(4, first.size());
        assertEquals(Set.of("A", "B", "C", "D"), new HashSet<>(first));
        for (Vertex<String> from : g.vertices.values()) {
            for (Edge<String> edge : from.outgoing) {
                assertTrue(first.indexOf(from.data) < first.indexOf(edge.destination.data));
            }
        }
        assertEquals(first, g.topoSort());
    }

    @Test void topoRejectsCyclesIncludingSelfLoops() {
        Graph<String> cycle = new Graph<>();
        cycle.addEdge("A", "B", 1);
        cycle.addEdge("B", "A", 1);
        assertThrows(IllegalArgumentException.class, cycle::topoSort);
        Graph<String> self = new Graph<>();
        self.addEdge("X", "X", 1);
        assertThrows(IllegalArgumentException.class, self::topoSort);
    }

    @Test void topoEmptyGraphReturnsEmptyList() {
        assertEquals(List.of(), new Graph<String>().topoSort());
    }
}
