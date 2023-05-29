package com.tsp.solvers;

import com.tsp.graph.Graph;

import java.util.ArrayList;
import java.util.List;

public abstract class GraphSolver {
    protected final Graph graph;
    protected boolean solved = false;

    protected final List<Integer> tour = new ArrayList<>();
    protected double tourCost = Double.POSITIVE_INFINITY;

    public GraphSolver(Graph graph) {
        this.graph = graph;

        if (graph.size <= 2) throw new IllegalStateException("N <= 2 not yet supported.");
    }

    public List<Integer> getTour() {
        if (!solved) solve();
        return tour;
    }

    public double getTourCost() {
        if (!solved) solve();
        return tourCost;
    }

    abstract void solve();
}
