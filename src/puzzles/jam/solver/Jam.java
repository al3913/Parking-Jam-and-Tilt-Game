package puzzles.jam.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.model.JamConfig;

import java.io.IOException;
import java.util.ArrayList;

public class Jam {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Jam filename");
        }
        else
        {
            System.out.println("File: " + args[0]);
            JamConfig j = new JamConfig(args[0]);
            System.out.println(j);
            ArrayList<Configuration> path = (ArrayList<Configuration>) Solver.BFS(j); //Solves the puzzle sending it to the Solver class and returns the shortest path
            Solver.printConfigs(); //Prints out number of total and unique configs
            if(path==null) System.out.println("No solution"); //Print no solution if no path was found
            else {
                for (int i = 0; i < path.size(); i++) {
                    System.out.println("Step " + i + ":\n" + path.get(i) + "\n"); //Prints out the path
                }
            }
        }
    }
}