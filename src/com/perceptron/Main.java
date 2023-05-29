package com.perceptron;

import java.util.Arrays;

/**
 * Course Work 2 Steps
 * - Implement KNN first because that's easier or should be easier.
 * - Try using Support Vector Machines inorder to get a higher mark.
 */

public class Main {
    public static void main(String[] args) {
        int[][] train = {
                {0, 4}, {1, 1}, {2, 4}, {3, 1}, {4, 4},
                {5, 1}, {6, 4}, {7, 1}, {8, 4}, {9, 1},
        };

        int[] label = {1, -1, 1, -1, 1, -1, 1, -1, 1, -1};

        int index = 8;

        PerceptronND pnd = new PerceptronND();
        pnd.fit(train, label);
        int prediction = pnd.categorise(train[index]);
        boolean isCorrect = prediction == label[index];
        System.out.println("Weights " + Arrays.toString(pnd.w));
        System.out.println("Predicted " + Arrays.toString(train[index]) + " " + label[index] + " to be " + prediction + " which is " + isCorrect);

//        Perceptron perceptron = new Perceptron();
//        perceptron.setLearningRate(0.1);
//        perceptron.setMaxIteration(20);
//        perceptron.setW(new double[]{-1, 0, 2});
//        perceptron.fit(xTrain, yTrain, dTrain);
//
//        int testIndex = (int) Math.round(Math.random() * xTest.length);
//        int output = perceptron.categorise(xTest[testIndex], yTest[testIndex]);
//        boolean isCorrect = output  == dTest[testIndex];
//        System.out.println(xTest[testIndex] + " " + yTest[testIndex] + " - " + output +  " ---> " + isCorrect);
    }

}
