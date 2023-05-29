package com.perceptron;

public class Perceptron {
    private double LEARNING_RATE = 0.05;
    private int MAX_ITERATION = 100;
    public double[] w = {0, 0, 0};

    public void setW(double[] w) {
        if (w.length != 3) {
            throw new IllegalArgumentException("'w' is meant to be an array of 3 elements.");
        }
        this.w = w;
    }

    public void setLearningRate(double lr) {
        this.LEARNING_RATE = lr;
    }

    public void setMaxIteration(int mi) {
        if (mi == 0) {
            throw new IllegalArgumentException("MAX_ITERATION can not be set to Zero.");
        }

        this.MAX_ITERATION = mi;
    }

    public void fit(int[] x, int[] y, int[] z) {
        if (x.length != y.length && z.length != y.length) {
            throw new IllegalArgumentException("Arguments are to be of the same length.");
        }

        int j;
        for (j = 0; j < MAX_ITERATION; j++) {
            double sse = train(x, y, z);
            if (sse < 0.01) {
                System.out.println("Model fitted in " + j + " iterations.");
                break;
            }
        }
    }

    private double train(int[] x, int[] y, int[] z) {
        int sse = 0;
        for (int i = 0; i < x.length; i++) {
            int category = categorise(x[i], y[i]);

            if (category != z[i]) {
                // difference or error
                // d is 1 if the line should go upper
                // d is -1 if the line should go lower
                int d = (z[i] - category) / 2;

                w[0] = w[0] + d * 1 * LEARNING_RATE;
                w[1] = w[1] + d * x[i] * LEARNING_RATE;
                w[2] = w[2] + d * y[i] * LEARNING_RATE;

                sse += d * d;
            }
        }
        return sse;
    }

    public int categorise(int x, int y) {
        double output = w[0] + w[1] * x + w[2] * y;
        return output > 0 ? 1 : -1;
    }
}
