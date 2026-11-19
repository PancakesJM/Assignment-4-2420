package assign10;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Solves the shortest path problem for generic, directed, weighted, sparse graphs
 * using two versions of Dijkstra's algorithm.
 * 
 * @author CS 2420 course staff, Ava Murphy, and Yujia Zhao
 * @version November 18, 2026
 */
public class GraphUtility {
	/**
	 * Gets the shortest weighted path from one vertex (starting vertex) to another 
	 * (ending vertex) in a directed graph defined by the given lists.
	 * 
	 * Uses the version of Dijkstra's algorithm implemented in Assignment 7.
	 * 
	 * @param <Type> the type of vertex data
	 * @param sources - list of data for the source vertices of edges in the graph being defined
	 * @param destinations - list of data for the destination vertices of edges in the graph being defined
	 * @param weights - list of weights for the edges in the graph being defined
	 * @param srcData - data of the starting vertex of the path being sought
	 * @param dstData - data of the ending vertex of the path being sought
	 * @return ordered list of data for the vertices that make up the shortest weighted path
	 * @throws IllegalArgumentException if any of the given lists are ill-formatted or there is no such path
	 */
	public static <Type> List<Type> shortestWeightedPathWithSorting(List<Type> sources, List<Type> destinations, 
			List<Double> weights, Type srcData, Type dstData) throws IllegalArgumentException {
		return buildGraph(sources, destinations, weights).shortestWeightedPathWithSorting(srcData, dstData);
	}
	
	/**
	 * Gets the shortest weighted path using a binary min heap and Dijkstra's algorithm.
	 * Assumes that edge weights are nonnegative.
	 *
	 * @param <Type> the type of vertex data
	 * @param sources - list of source vertices of the edges
	 * @param destinations - list of destination vertices of the edges
	 * @param weights - list of weights for the edges
	 * @param srcData - data of the starting vertex
	 * @param dstData - data of the ending vertex
	 * @return ordered list of data for the vertices in the shortest weighted path
	 * @throws IllegalArgumentException if the lists have different sizes, a vertex is missing, or no such path exists
	 */
	public static <Type> List<Type> shortestWeightedPathWithPriorityQueue(List<Type> sources, List<Type> destinations, 
			List<Double> weights, Type srcData, Type dstData) throws IllegalArgumentException {
		return buildGraph(sources, destinations, weights).shortestWeightedPathWithPriorityQueue(srcData, dstData);
	}
	
	/**
	 * Constructs a directed graph, as defined by the given lists.
	 * 
	 * @param <Type> the type of vertex data
	 * @param sources - list of data for the source vertices of edges in the graph being defined
	 * @param destinations - list of data for the destination vertices of edges in the graph being defined
	 * @param weights - list of weights for the edges in the graph being defined
	 * @return the directed graph defined by the given lists
	 * @throws IllegalArgumentException if the lists have different sizes
	 */
	private static <Type> Graph<Type> buildGraph(List<Type> sources, List<Type> destinations, List<Double> weights) {
		if(sources.size() != destinations.size() || sources.size() != weights.size())
			throw new IllegalArgumentException();
		
		Graph<Type> g = new Graph<Type>();
		for(int i = 0; i < sources.size(); i++)
			g.addEdge(sources.get(i), destinations.get(i), weights.get(i));
		return g;
	}
	
	/**
	 * Represents a generic, directed, weighted, sparse graph.
	 *
	 * @param <GraphType> the type of vertex data
	 */
	private static class Graph<GraphType> {
		/**
		 * Represents a single vertex in a generic, directed, weighted, sparse graph.
		 *
		 * @param <VertexType> the type of vertex data
		 */
		private static class Vertex<VertexType> {
			public VertexType data;
			public LinkedList<Edge<VertexType>> adjacencyList;
			public Vertex<VertexType> previous;	
			public double distanceFromStart;
			public boolean visited;
			
			/**
			 * Creates a vertex that contains the given data.
			 * 
			 * @param data - data to be contained in this vertex
			 */
			public Vertex(VertexType data) {
				this.data = data;
				this.adjacencyList = new LinkedList<Edge<VertexType>>();
				this.previous = null;
				this.distanceFromStart = Double.MAX_VALUE;
				this.visited = false;
			}

			/**
			 * Adds a directed edge from this vertex to the given vertex.
			 * 
			 * @param destination - vertex to which the edge points
			 * @param weight - cost of traversing the edge
			 */
			public void addEdge(Vertex<VertexType> destination, double weight) {
				this.adjacencyList.add(new Edge<VertexType>(destination, weight));
			}
		}

		/**
		 * Represents a single edge in a generic, directed, weighted, sparse graph.
		 *
		 * @param <EdgeType> the type of vertex data
		 */
		private static class Edge<EdgeType> {
			public Vertex<EdgeType> destination;
			public double weight;

			/**
			 * Creates an edge to the given vertex with the given weight.
			 * 
			 * @param destination - vertex to which this edge points
			 * @param weight - cost of traversing this edge
			 */
			public Edge(Vertex<EdgeType> destination, double weight) {
				this.destination = destination;
				this.weight = weight;
			}
		}

		/**
		 * Represents a queued vertex with its distance at the time of insertion.
		 * The saved distance does not change when the vertex is updated.
		 *
		 * @param <EntryType> the type of vertex data
		 */
		private static class QueueEntry<EntryType> {
			private final Vertex<EntryType> vertex;
			private final double distance;

			/**
			 * Creates an entry for the given vertex using its current distance.
			 *
			 * @param vertex - vertex being added to the priority queue
			 */
			public QueueEntry(Vertex<EntryType> vertex) {
				this.vertex = vertex;
				this.distance = vertex.distanceFromStart;
			}
		}

		private HashMap<GraphType, Vertex<GraphType>> vertices;

		/**
		 * Creates an empty graph.
		 */
		public Graph() {
			vertices = new HashMap<GraphType, Vertex<GraphType>>();
		}

		/**
		 * Adds a directed edge to this graph.
		 * 
		 * @param srcData - data of the source vertex for the edge
		 * @param dstData - data of the destination vertex for the edge
		 * @param weight - weight for the edge
		 */
		public void addEdge(GraphType srcData, GraphType dstData, double weight) {
			Vertex<GraphType> srcVertex;
			if(vertices.containsKey(srcData))
				srcVertex = vertices.get(srcData);
			else {
				srcVertex = new Vertex<GraphType>(srcData);
				vertices.put(srcData, srcVertex);
			}

			Vertex<GraphType> dstVertex;
			if(vertices.containsKey(dstData))
				dstVertex = vertices.get(dstData);
			else {
				dstVertex = new Vertex<GraphType>(dstData);
				vertices.put(dstData, dstVertex);
			}

			srcVertex.addEdge(dstVertex, weight);
		}
		
		/**
		 * Determines the shortest weighted path from one vertex (starting vertex) to another 
		 * (ending vertex) in this graph, using the version of Dijkstra's algorithm implemented 
		 * in Assignment 7.
		 * 
		 * While the list of unvisited vertices is not empty, sort the list by distance from 
		 * start (descending order) and use the last vertex as the one with the smallest distance 
		 * from start, removing the vertex from the list as it is "visited".  
		 * Leverages that using Java's sort method ensures efficiency for nearly and partially sorted lists. 
		 * 
		 * @param startData - data of the starting vertex 
		 * @param endData - data of the ending vertex
		 * @return ordered list of data for the vertices that make up the shortest weighted path
		 * @throws IllegalArgumentException if a vertex is missing or no path exists
		 */
		private List<GraphType> shortestWeightedPathWithSorting(GraphType startData, GraphType endData) {	
			Vertex<GraphType> startingVertex = vertices.get(startData);
			if(startingVertex == null)
				throw new IllegalArgumentException("Vertex " + startData + " does not exist.");
			Vertex<GraphType> endingVertex = vertices.get(endData);
			if(endingVertex == null)
				throw new IllegalArgumentException("Vertex " + endData + " does not exist.");

			List<Vertex<GraphType>> unvisitedVertices = new ArrayList<Vertex<GraphType>>();
			for(Vertex<GraphType> vertex : vertices.values())
				unvisitedVertices.add(vertex);
			startingVertex.distanceFromStart = 0;		
			
			while(!unvisitedVertices.isEmpty()) {
				unvisitedVertices.sort((v1, v2) -> Double.compare(v2.distanceFromStart, v1.distanceFromStart));
				Vertex<GraphType> currentVertex = unvisitedVertices.get(unvisitedVertices.size() - 1);
				if(currentVertex.data.equals(endData))
					return generatePath(endingVertex, startData);
				for(Edge<GraphType> edge : currentVertex.adjacencyList) 
					if(currentVertex.distanceFromStart + edge.weight < edge.destination.distanceFromStart) {
						edge.destination.distanceFromStart = currentVertex.distanceFromStart + edge.weight;
						edge.destination.previous = currentVertex;
					}
				unvisitedVertices.remove(unvisitedVertices.size() - 1);			
			}
			
			throw new IllegalArgumentException("There is no path between vertex " + startData + " and vertex " + startData + ".");
		}
		
		/**
		 * Determines the shortest weighted path using a binary min heap.
		 * Adds a new entry each time a vertex's distance improves and skips
		 * entries for vertices that have already been visited.
		 * Assumes that edge weights are nonnegative.
		 *
		 * @param startData - data of the starting vertex
		 * @param endData - data of the ending vertex
		 * @return ordered list of data for the vertices in the shortest weighted path
		 * @throws IllegalArgumentException if a vertex is missing or no path exists
		 */
		public List<GraphType> shortestWeightedPathWithPriorityQueue(GraphType startData, GraphType endData) {
			Vertex<GraphType> startingVertex = vertices.get(startData);
			if(startingVertex == null)
				throw new IllegalArgumentException("Vertex " + startData + " does not exist.");
			Vertex<GraphType> endingVertex = vertices.get(endData);
			if(endingVertex == null)
				throw new IllegalArgumentException("Vertex " + endData + " does not exist.");

			// Use infinity for this version without changing the provided sorting version.
			for(Vertex<GraphType> vertex : vertices.values())
				vertex.distanceFromStart = Double.POSITIVE_INFINITY;

			BinaryMinHeap<QueueEntry<GraphType>> queue = new BinaryMinHeap<QueueEntry<GraphType>>(
					(e1, e2) -> Double.compare(e1.distance, e2.distance));
			startingVertex.distanceFromStart = 0;
			queue.add(new QueueEntry<GraphType>(startingVertex));

			while(!queue.isEmpty()) {
				Vertex<GraphType> currentVertex = queue.extract().vertex;
				if(currentVertex.visited)
					continue;
				currentVertex.visited = true;
				if(currentVertex == endingVertex)
					return generatePath(endingVertex, startData);

				for(Edge<GraphType> edge : currentVertex.adjacencyList) {
					double newDistance = currentVertex.distanceFromStart + edge.weight;
					if(!edge.destination.visited && newDistance < edge.destination.distanceFromStart) {
						edge.destination.distanceFromStart = newDistance;
						edge.destination.previous = currentVertex;
						queue.add(new QueueEntry<GraphType>(edge.destination));
					}
				}
			}

			throw new IllegalArgumentException("There is no path between vertex " + startData + " and vertex " + endData + ".");
		}
		
		/**
		 * Generates an ordered list of data for the vertices that make up the path defined by 
		 * each vertex's "previous" attribute.
		 * 
		 * @param endVertex - vertex at the end of this path
		 * @param startData - data of the starting vertex in the path
		 * @return ordered list of data for the vertices that make up the path
		 * @throws IllegalArgumentException if the path does not reach the starting vertex
		 */
		private List<GraphType> generatePath(Vertex<GraphType> endVertex, GraphType startData) {
			LinkedList<GraphType> path = new LinkedList<GraphType>();
			Vertex<GraphType> currentVertex = endVertex;
			while(!currentVertex.data.equals(startData)) {
				if(currentVertex.previous == null)
					throw new IllegalArgumentException("There is no path between vertex " + startData + " and vertex " + endVertex.data + ".");
				path.addFirst(currentVertex.data);
				currentVertex = currentVertex.previous;
			}
			path.addFirst(startData);	
			return path;
		}
	}
}
