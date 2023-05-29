package com.tsp.input;

import com.tsp.graph.Graph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Reader {
    public static Graph parseTSVFile(String fileName) throws IOException {
        String[] lines = Files.readString(Paths.get(fileName)).trim().split("\n");
        int[][] nodes = new int[lines.length][2];

        for (int i = 0; i < lines.length; i++) {
            var line = lines[i].trim();

            String[] features = line.split("\\s+");

            if (features.length != 3) {
                System.out.println(
                        "Skipping line: Expected 3 features but received " + features.length + " on line " + i
                );
            }

            int c = Integer.parseInt(features[0]);
            int x = Integer.parseInt(features[1]);
            int y = Integer.parseInt(features[2]);

            nodes[c-1] = new int[] {x, y};
        }

        return new Graph(nodes);
    }
}