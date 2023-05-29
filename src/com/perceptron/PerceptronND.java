package com.perceptron;

public class PerceptronND {
    final private double LEARNING_RATE;
    final private int MAX_ITERATION;
    public double[] w = {0, 0, 0};

    public PerceptronND(double lr, int mi) {
        this.MAX_ITERATION = mi;
        this.LEARNING_RATE = lr;
    }

    public PerceptronND(double lr) {
        this(lr, 100);
    }

    public PerceptronND(int mi) {
        this(0.05, mi);
    }

    public PerceptronND() {
        this(0.05, 100);
    }


    public void fit(int[][] feature, int[] label) {
        if (feature.length != label.length) {
            throw new IllegalArgumentException("'feature' and 'label' has different lengths.");
        }

        w = new double[feature[0].length + 1];

        for (int i = 0; i < MAX_ITERATION; i++) {
            double sse = train(feature, label);
            if(sse < 0.01) {
                System.out.println("Model fitted in " + i + " iterations");
                break;
            }
        }
    }

    private double train(int[][] feature, int[] label) {
        int sse = 0;

        for (int i = 0; i < feature.length; i++) {
            int category = categorise(feature[i]);

            if (category != label[i]) {
                int d = (label[i] - category) / 2;

                for (int j = 0; j < w.length; j++) {

                    if(j == 0) {
                        w[j] = w[j] + d * 1 * LEARNING_RATE;
                        continue;
                    }

                    w[j] = w[j] + d * feature[i][j - 1] * LEARNING_RATE;
                }

                sse += d * d;
            }
        }

        return sse;
    }

    public int categorise(int[] feature) {
        double output = w[0];

        for (int i = 1; i < w.length; i++) {
            output += w[i] * feature[i - 1];
        }

        return output > 0 ? 1 : -1;
    }
}
