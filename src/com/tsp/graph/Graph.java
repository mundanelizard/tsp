package com.tsp.graph;


import java.util.Arrays;

/**
 * A graph is immutable because it's built to be shared
 * across different path of the application, that's why
 * the adjacency matrix is placed in a wrapper 'Graph'.
 */
public class Graph {
    /**
     * Graph is represented internally as an adjacency matrix.
     * I thought it will be a go idea here, since all nodes
     * are connected to each other.
     */
    private final double[][] graph;
    public final int size;

    public Graph(int[][] nodes) {
        size = nodes.length;
        double[][] graph = new double[size][size];

        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes.length; j++) {
                int[] x = nodes[i];
                int[] y = nodes[j];

                graph[i][j] = distance(x, y);
            }
        }

        this.graph = graph;
    }

    public double getWeight(int i, int j) {
        return graph[i][j];
    }

    public void print() {
        for (int i = 0; i < size; i++) {
            System.out.print(i + "\t\t\t");
            for (int j = 0; j < size; j++) {
                System.out.print(j + ":" + getWeight(i,j) + "\t\t");
            }
            System.out.println();
        }
    }

    private double distance(int[] x, int[] y) {
        if(x[0] == y[0] && x[1] == y[1]) {
            return 0.0;
        }

        double dx = x[0] - y[0];
        double cx = dx * dx;

        double dy = x[1] - y[1];
        double cy = dy * dy;

        return Math.sqrt(cy + cx);
    }
}
