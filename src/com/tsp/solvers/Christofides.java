package com.tsp.solvers;

import com.tsp.graph.Graph;

import java.util.*;
import java.util.stream.Collectors;


public class Christofides extends GraphSolver {
    public Christofides(Graph graph) {
        super(graph);
    }

    /**
     *
     */
    public void solve() {
        var mst = getMinimumSpanningTree();
        var oddVertices = getOddVertices(mst);
        var mstEdges = getMinimumWeightMatching(mst, graph, oddVertices);
        calculateTour(mstEdges);
        calculateCost();
        solved = true;
    }

    /**
     *
     * @param mstEdges
     */
    private void calculateTour(List<Edge> mstEdges) {
        var neighbours = getNeighbourMap(mstEdges);

        var startVertex = mstEdges.get(0).from;
        tour.add(neighbours.get(startVertex).get(0));

        while(!mstEdges.isEmpty()) {
            int i, v = 0;

            for (i = 0; i < tour.size(); i++) {
                v = tour.get(i);
                if (neighbours.get(v).size() > 0)
                    break;
            }

            // check guard to avoid an infinite loop.
            // if no tour path exists, it will quit
            if (i >= tour.size()) {
                break;
            }

            while (neighbours.get(v).size() > 0) {
                int w = neighbours.get(v).get(0);

                // temporary variable for usage in "streams" or lambda
                int finalV = v;

                // removing this edge from the mst.
                mstEdges = mstEdges
                        .stream()
                        .filter(e -> e.from != finalV && e.to != w)
                        .collect(Collectors.toList());

                // removing the city from the neighbour map
                neighbours.put(v, neighbours
                        .get(v)
                        .stream()
                        .filter(city -> city != w)
                        .collect(Collectors.toList()));

                // removing the destination from the neighbour map
                neighbours.put(w, neighbours
                        .get(w)
                        .stream()
                        .filter(city -> city != finalV)
                        .collect(Collectors.toList()));

                i += 1;

                // adding the city to the tour list
                tour.add(i, w);

                // updating the origin city for the next iteration.
                v = w;
            }
        }

        tour.add(tour.get(0));
        tour.replaceAll(integer -> integer + 1);
    }

    /**
     * Creates a map from one city to its neighbours. It's more like an
     * adjaceny matrix, but with the co
     * @param mstEdges minimum spanning tree edges.
     * @return a map from one city to its neighbours.
     */
    private HashMap<Integer, List<Integer>>  getNeighbourMap(List<Edge> mstEdges) {
        var neighbours = new HashMap<Integer, List<Integer>>();

        for (var edge : mstEdges) {
            if (!neighbours.containsKey(edge.from)) {
                neighbours.put(edge.from, new ArrayList<>());
            }

            if (!neighbours.containsKey(edge.to)) {
                neighbours.put(edge.to, new ArrayList<>());
            }

            neighbours.get(edge.from).add(edge.to);
            neighbours.get(edge.to).add(edge.from);
        }

        return neighbours;
    }

    /**
     *
     */
    private void calculateCost() {
        tourCost = 0;
        var prev = tour.get(0);

        for (var i : tour) {
            tourCost += graph.getWeight(prev - 1, i - 1);
            prev = i;
        }
    }

    /**
     *
     * @param mst
     * @param graph
     * @param oddVertices
     * @return
     */
    private List<Edge> getMinimumWeightMatching(int[] mst, Graph graph, int[] oddVertices) {
        // converting mst to an edge list
        var edgeList = convertMSTToEdgeList(mst);
        var queue = Arrays.stream(oddVertices).boxed().collect(Collectors.toList());

        while(!queue.isEmpty()) {
            var index = queue.size() - 1;
            var u = queue.get(index); // origin
            queue.remove(index);

            var length = Double.POSITIVE_INFINITY;
            var closest = 0;

            for(var v : oddVertices) {
                if (u != v && graph.getWeight(u, v) < length) {
                    length = graph.getWeight(u, v);
                    closest = v;
                }
            }

            // removing vertex
            int finalClosest = closest;
            queue = queue.stream().filter(i -> i != finalClosest).collect(Collectors.toList());

            edgeList.add(new Edge(u, closest));
        }

        return edgeList;
    }

    /**
     *
     * @param mst
     * @return
     */
    private List<Edge> convertMSTToEdgeList(int[] mst) {
        var list = new ArrayList<Edge>();

        for (int v = 0 ; v < mst.length; v++) {
            int u = mst[v];
            if (u == -1) continue;
            list.add(new Edge(u, v));
        }

        return list;
    }

    /**
     *
     * @param vertices
     * @return
     */
    private int[] getOddVertices(int[] vertices) {
        int[] count = new int[vertices.length];

        for (int u : vertices) {
            if (u == -1) continue;
            count[u] += 1;
        }

        int total = 0;

        for (int v : count) {
            if (v % 2 != 0) continue;
            total += 1;
        }

        int[] ooo = new int[total];

        for (int i = 0, j = 0; i < count.length; i++) {
            if (count[i] % 2 != 0) continue;
            ooo[j] = i;
            j += 1;
        }

        return ooo;
    }

    /**
     * Creates a minimum spanning tree.
     * @return
     */
    private int[] getMinimumSpanningTree() {
        int[] mst = new int[graph.size];
        // keeps track of the cost of a vertex
        double[] verticesCost = new double[graph.size];

        // keeps track of included mst vertices
        boolean[] includedVertices = new boolean[graph.size];

        // initialises keys to infinite
        for (int i = 0; i < graph.size; i++) {
            verticesCost[i] = Integer.MAX_VALUE;
            includedVertices[i] = false;
        }

        verticesCost[0] = 0;
        mst[0] = -1; // doesn't have a parent node

        for (int count = 0; count < graph.size - 1; count++) {
            int u = getMinCostVertex(verticesCost, includedVertices);
            includedVertices[u] = true;
            for(int v = 0; v < graph.size; v++) {
                if (graph.getWeight(u, v) != 0 && !includedVertices[v]
                && graph.getWeight(u, v) < verticesCost[v]) {
                    mst[v] = u;
                    verticesCost[v] = graph.getWeight(u, v);
                }
            }
        }

        return mst;
    }

    /**
     * Gets the vertex with the minimum cost.
     * @param verticesCost list of vertices cost
     * @param includedVertices vertices that are included.
     * @return the minimum index.
     */
    private int getMinCostVertex(double[] verticesCost, boolean[] includedVertices) {
        double min = Double.MAX_VALUE;
        int minIndex = -1;

        for (int v = 0; v < graph.size; v++) {
            if (!includedVertices[v] && verticesCost[v] < min) {
                min = verticesCost[v];
                minIndex = v;
            }
        }

        return minIndex;
    }


    // Static classes.

    /**
     * Represents an immutable graph edge
     */
    static class Edge {
        /**
         * origin city
         */
        final int from;
        /**
         * Destination city
         */
        final int to;

        /**
         * Creates an edge
         * @param from origin city
         * @param to destination city
         */
        Edge(int from, int to) {
            this.from = from;
            this.to = to;
        }

        /**
         * Converts the edges to a readable format for debugging.
         * @return
         */
        @Override
        public String toString() {
            return "Edge { " + from + " - " + to + " }";
        }
    }
}
