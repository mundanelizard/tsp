package com.tsp.solvers;

import com.tsp.graph.Graph;

import java.util.ArrayList;
import java.util.List;

public class DP extends GraphSolver {
    private final Graph graph;

    public DP(Graph graph) {
        super(graph);
        this.graph = graph;

        // Java can't track data in 32 bit integer.
        if (graph.size > 32) {
            throw new IllegalArgumentException(
                    "Graph is too large to track in 32 bit integer.");
        }
    }

    /**
     * TODO: write out comment
     */
    public void solve() {
        if (solved) return;

        final int END_STATE = (1 << graph.size) - 1;
        // runs out of heap space when it gets to 24
        double[][] memo = new double[graph.size][1 << graph.size];

        int start = 0;
        for (int end = 0; end < graph.size; end++) {
            if (end == start) continue;
            memo[end][(1 << start) | (1 << end)] = graph.getWeight(start, end);
        }

        for (int r = 3; r <= graph.size; r++) {
            for(int subset : generateCombinations(r, graph.size)) {
                if (notIn(start, subset)) continue;
                for (int next = 0; next < graph.size; next++) {
                    if (next == start || notIn(next, subset)) continue;
                    int subsetWithoutNext = subset ^ (1 << next);
                    double minDist = Double.POSITIVE_INFINITY;
                    for (int end = 0; end < graph.size; end++) {
                        if (end == start || end == next || notIn(end, subset)) continue;
                        double newDistance = memo[end][subsetWithoutNext] + graph.getWeight(end, next);
                        if (newDistance < minDist) {
                            minDist = newDistance;
                        }
                    }
                    memo[next][subset] = minDist;
                }
            }
        }

        // calculates the tour cost
        calculateTourCost(start, memo, END_STATE);

        // Reconstructs tour path
        reconstructTour(start, memo, END_STATE);

        solved = true;
    }

    /**
     * TODO: write out comment
     * @param start starting node
     * @param memo memo of the tour
     * @param state ending state.
     */
    private void reconstructTour(int start, double[][] memo, int state) {
        int lastIndex = start;
        // Adding one to convert city name to match the input document
        tour.add(start + 1);

        // count it a random iterator used keep track of the loop.
        // it runs through the for the length of the graph
        for (int count = 1; count < graph.size; count++) {

            // initializing the best distance and index.
            int bestIndex = -1;
            double bestDist = Double.POSITIVE_INFINITY;

            // find the best cost for the current city 'origin'
            for (int dest = start; dest < graph.size; dest++) {
                // if the destination isn't in the state we'll skip it.
                if (dest == start || notIn(dest, state)) continue;

                // we get the previous cost to that dest at that state and adds the
                // cost from that state to the last city this gives us the total cost
                double newDist = memo[dest][state] + graph.getWeight(dest, lastIndex);

                // if the total cost is the best for the city we'll update it.
                if (newDist < bestDist) {
                    bestIndex = dest;
                    bestDist = newDist;
                }
            }

            // adding the best city to the index.
            // adding one to convert from zero index to one index.
            tour.add(bestIndex + 1);
            // remove the current best city form the state.
            state = state ^ (1 << bestIndex);
            // update the last index
            lastIndex = bestIndex;
        }

        // adding one to convert from zero index to one index.
        tour.add(start + 1);
    }

    /**
     * Calculates the cost of a tour.
     * @param start starting node
     * @param memo memo of the tour
     * @param endState ending state
     */
    private void calculateTourCost(int start, double[][] memo, int endState) {
        // Calculating tour cost
        for (int i = 0; i < graph.size; i++) {
            if (i == start) continue;
            double tourCost = memo[i][endState] + graph.getWeight(i, start);
            if(tourCost < this.tourCost) {
                this.tourCost = tourCost;
            }
        }
    }

    /**
     * Checks if a city is in a subset or is in state.
     * @param city index of the city
     * @param subset combinations subset
     * @return a boolean when it exists
     */
    private boolean notIn(int city, int subset) {
        return ((1 << city) & subset) == 0;
    }

    /**
     * Generates combinations of subsets.
     * @param r index of the city
     * @param n number of cities in the graph
     * @return combinations of subsets for city r
     */
    private List<Integer> generateCombinations(int r, int n) {
        List<Integer> subsets = new ArrayList<>();
        generateCombinations(0, 0, r, n, subsets);
        return subsets;
    }

    /**
     * Generate combinations for subsets.
     * @param set
     * @param at
     * @param r
     * @param n
     * @param subsets
     */
    private static void generateCombinations(int set, int at, int r, int n, List<Integer> subsets) {
        int elementsLeftToPick = n - at;
        if (elementsLeftToPick < r) return;

        if (r == 0) {
            subsets.add(set);
        } else {
            for (int i = at; i < n; i++) {
                set ^= (1 << i);

                generateCombinations(set, i + 1, r - 1, n, subsets);

                set ^= (1 << i);
            }
        }
    }
}
