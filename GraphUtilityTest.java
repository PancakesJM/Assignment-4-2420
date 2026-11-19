package assign10;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

/**
 * Tests the priority queue version of Dijkstra's algorithm.
 *
 * @author Ava Murphy and Yujia Zhao
 * @version November 18, 2026
 */
public class GraphUtilityTest {

	/** Tests a graph with a single edge. */
	@Test
	public void testSingleEdge() {
		assertEquals(List.of("A", "B"), GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of("A"), List.of("B"), List.of(2.0), "A", "B"));
	}

	/** Tests that the path with the lowest weight is selected instead of the fewest edges. */
	@Test
	public void testWeightedPath() {
		assertEquals(List.of("A", "B", "C", "D"), GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of("A", "A", "B", "C"), List.of("D", "B", "C", "D"),
				List.of(20.0, 2.0, 3.0, 4.0), "A", "D"));
	}

	/** Tests a vertex whose tentative distance improves multiple times. */
	@Test
	public void testRepeatedUpdates() {
		assertEquals(List.of("A", "C", "D", "B", "E"), GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of("A", "A", "A", "C", "C", "D", "B"),
				List.of("B", "C", "D", "B", "D", "B", "E"),
				List.of(30.0, 1.0, 10.0, 15.0, 2.0, 1.0, 40.0), "A", "E"));
	}

	/** Tests that decreasing a queued vertex's distance does not break heap order. */
	@Test
	public void testUpdatedHeapPriority() {
		assertEquals(List.of("S", "X", "A", "T"), GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of("S", "S", "S", "S", "X", "A"),
				List.of("X", "T", "B", "A", "A", "T"),
				List.of(1.0, 10.0, 20.0, 30.0, 1.0, 1.0), "S", "T"));
	}

	/** Tests a graph with cycles and a self-loop. */
	@Test
	public void testCycles() {
		assertEquals(List.of("A", "B", "C", "D"), GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of("A", "B", "B", "C", "C", "A"),
				List.of("B", "A", "C", "C", "D", "D"),
				List.of(1.0, 2.0, 1.0, 0.0, 1.0, 10.0), "A", "D"));
	}

	/** Tests zero-weight edges. */
	@Test
	public void testZeroWeights() {
		assertEquals(List.of("A", "B", "C"), GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of("A", "B", "A"), List.of("B", "C", "C"),
				List.of(0.0, 0.0, 1.0), "A", "C"));
	}

	/** Tests parallel edges with different weights. */
	@Test
	public void testParallelEdges() {
		assertEquals(List.of("A", "B", "C"), GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of("A", "A", "A", "B"), List.of("B", "B", "C", "C"),
				List.of(10.0, 1.0, 5.0, 2.0), "A", "C"));
	}

	/** Tests fractional edge weights and integer vertex data. */
	@Test
	public void testFractionalWeights() {
		assertEquals(List.of(1, 2, 3), GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of(1, 1, 2), List.of(3, 2, 3), List.of(1.5, 0.25, 0.5), 1, 3));
	}

	/** Tests a valid finite distance equal to the largest representable double. */
	@Test
	public void testMaximumFiniteWeight() {
		assertEquals(List.of("A", "B"), GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of("A"), List.of("B"), List.of(Double.MAX_VALUE), "A", "B"));
	}

	/** Tests two equally short paths without assuming a tie-breaking order. */
	@Test
	public void testEqualPaths() {
		List<String> path = GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of("A", "A", "B", "C"), List.of("B", "C", "D", "D"),
				List.of(1.0, 1.0, 2.0, 2.0), "A", "D");
		assertTrue(path.equals(List.of("A", "B", "D")) || path.equals(List.of("A", "C", "D")));
	}

	/** Tests that the starting and ending vertices can be the same. */
	@Test
	public void testSameVertex() {
		assertEquals(List.of("A"), GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of("A"), List.of("B"), List.of(1.0), "A", "A"));
	}

	/** Tests that an edge cannot be traversed in the reverse direction. */
	@Test
	public void testDirectedEdge() {
		assertThrows(IllegalArgumentException.class, () -> GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of("A"), List.of("B"), List.of(1.0), "B", "A"));
	}

	/** Tests an existing but unreachable destination. */
	@Test
	public void testDisconnectedGraph() {
		assertThrows(IllegalArgumentException.class, () -> GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of("A", "C"), List.of("B", "D"), List.of(1.0, 1.0), "A", "D"));
	}

	/** Tests a starting vertex that is not in the graph. */
	@Test
	public void testMissingStart() {
		assertThrows(IllegalArgumentException.class, () -> GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of("A"), List.of("B"), List.of(1.0), "X", "B"));
	}

	/** Tests an ending vertex that is not in the graph. */
	@Test
	public void testMissingEnd() {
		assertThrows(IllegalArgumentException.class, () -> GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of("A"), List.of("B"), List.of(1.0), "A", "X"));
	}

	/** Tests mismatched edge list sizes. */
	@Test
	public void testMismatchedLists() {
		assertThrows(IllegalArgumentException.class, () -> GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of("A", "B"), List.of("B"), List.of(1.0, 2.0), "A", "B"));
		assertThrows(IllegalArgumentException.class, () -> GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of("A"), List.of("B"), List.of(), "A", "B"));
	}

	/** Tests an empty graph. */
	@Test
	public void testEmptyGraph() {
		assertThrows(IllegalArgumentException.class, () -> GraphUtility.shortestWeightedPathWithPriorityQueue(
				List.of(), List.of(), List.of(), "A", "B"));
	}

	/** Compares both Dijkstra implementations on a larger graph with a unique shortest path. */
	@Test
	public void testLargerGraph() {
		List<Integer> sources = new ArrayList<Integer>();
		List<Integer> destinations = new ArrayList<Integer>();
		List<Double> weights = new ArrayList<Double>();
		List<Integer> expected = new ArrayList<Integer>();
		Random random = new Random(2420);
		for(int i = 0; i < 100; i++) {
			expected.add(i);
			if(i < 99) {
				sources.add(i);
				destinations.add(i + 1);
				weights.add(1.0);
			}
			for(int j = 0; j < 3; j++) {
				sources.add(i);
				destinations.add(random.nextInt(100));
				weights.add(100.0 + random.nextInt(100));
			}
		}
		assertEquals(expected, GraphUtility.shortestWeightedPathWithPriorityQueue(
				sources, destinations, weights, 0, 99));
		assertEquals(expected, GraphUtility.shortestWeightedPathWithSorting(
				sources, destinations, weights, 0, 99));
	}
}
