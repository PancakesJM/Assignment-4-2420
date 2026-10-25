/*
 * Assignment 7 - Team Interface Agreement
 * Documentation only. Do not submit this file as part of the assignment.
 * All implementation files use: package assign07;
 *
 * PART A: Vertex.java, Edge.java, BFS.java, GraphUtility.java
 * PART B: Graph.java, Dijkstra.java, TopologicalSort.java
 *
 * SHARED STRUCTURE (fields are package-private)
 *
 * Vertex<Type>
 *   final Type data;
 *   final List<Edge<Type>> outgoing;  // initialized as ArrayList
 *   double distance;                  // initially POSITIVE_INFINITY
 *   Vertex<Type> previous;            // initially null
 *   int indegree;                     // initially 0
 *   public Vertex(Type data);
 *
 * Edge<Type>
 *   final Vertex<Type> destination;
 *   final double weight;
 *   public Edge(Vertex<Type> destination, double weight);
 *
 * Graph<Type>
 *   final Map<Type, Vertex<Type>> vertices;  // LinkedHashMap
 *   public Graph();
 *   public void addEdge(Type source, Type destination, double weight);
 *   public List<Type> breadthFirstSearch(Type source, Type destination);
 *   public List<Type> dijkstrasAlgorithm(Type source, Type destination);
 *   public List<Type> topoSort();
 *
 * ALGORITHM ENTRY POINTS (Graph delegates to these)
 *   BFS.search(Graph<Type> graph, Type source, Type destination)
 *   Dijkstra.search(Graph<Type> graph, Type source, Type destination)
 *   TopologicalSort.sort(Graph<Type> graph)
 *   All three are public static generic methods returning List<Type>.
 *
 * GRAPHUTILITY PUBLIC STATIC GENERIC METHODS (return List<Type>)
 *   shortestPath(List<Type> sources, List<Type> destinations,
 *                Type srcData, Type dstData)
 *   shortestWeightedPath(List<Type> sources, List<Type> destinations,
 *                        List<Double> weights, Type srcData, Type dstData)
 *   sort(List<Type> sources, List<Type> destinations)
 *
 * IMPORTANT AGREEMENTS
 *   - addEdge creates missing vertices, adds a directed edge to source.outgoing,
 *     and increments destination.indegree. Duplicate edges are allowed.
 *   - GraphUtility builds the graph and calls Graph methods (no algorithm code).
 *   - BFS and TopologicalSort use Queue + LinkedList.
 *   - Dijkstra sorts unvisited vertices by DESCENDING distance and removes LAST;
 *     do not use PriorityQueue.
 *   - Reset search state for each call; do not permanently alter indegree.
 *   - Use the assignment's required exceptions and add Javadoc.
 *   - Keep these shared names/signatures unchanged; internal helpers are flexible.
 *
 * This is our proposed team design, not an instructor-mandated file structure.
 */
