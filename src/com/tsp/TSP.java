/*
 * TSP
 * Entry file for tsp.
 *
 * The program uses one of two different algorithm to solve the traveling salesman problem.
 * It determines which algorithm to use based on the number of cities it receives. It uses
 * Chritofides for inputs greater than 24 and Dynamic Programming for inputs lesser than 24.
 *
 * Christofides and Dynamic programming implementations are in separate files.
 *
 * HOW TO RUN:
 * - Modify the file name to match the name of the input file.
 * - Click on the run button in eclipse.
 */

package com.tsp;

import com.tsp.solvers.Christofides;
import com.tsp.solvers.DP;
import com.tsp.graph.Graph;
import com.tsp.input.Reader;
import com.tsp.solvers.GraphSolver;

import java.io.IOException;

/**
 * Move code to eclipse
 * Create christofides
 * Create an interface to read in files
 *
 */

public class TSP {
    /**
     * Input file names
     */
    public static final String TEST_FILE_1 = "./test.txt";
    public static final String TEST_FILE_2 = "./test2.txt";
    public static final String TEST_FILE_3 = "./test3.txt";
    public static final String TEST_FILE_4 = "./test4.txt";

    public static void main(String[] args) throws IOException {
        Graph graph = Reader.parseTSVFile(TSP.TEST_FILE_1);

        GraphSolver gs;

        if (graph.size < 24) {
            gs = new DP(graph);
        } else {
            gs = new Christofides(graph);
        }

        var startTime = System.currentTimeMillis();

        var tour = gs.getTour();
        var cost = gs.getTourCost();

        var duration = System.currentTimeMillis() - startTime ;
        var efficiency = (cost * cost) * duration;

        System.out.println("Tour: " + tour);
        System.out.println("Cost: " + cost);
        System.out.println("Duration: " + duration + "ms");
        System.out.println("Efficiency: " + efficiency);

        /*
         * Good variable names
         * named constants
         * shorter functions - should fit on the screen.
         * comments every function.
         *      - add a block comments on every file
         *      - add comments at the beginning on each function.
         * describe the algorithms used at the beginning of each file used.
         */
    }
}
