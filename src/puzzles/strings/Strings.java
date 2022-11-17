package puzzles.strings;

import puzzles.common.solver.Solver;
import puzzles.common.solver.Configuration;

import java.util.ArrayList;

/**
 * Main class for the strings puzzle.
 *
 * @author Matt Kingston
 */
public class Strings {
    /**
     * Run an instance of the strings puzzle.
     *
     * @param args [0]: the starting string;
     *             [1]: the finish string.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        }
        else {
            StringsConfiguration start = new StringsConfiguration(args[0], args[1]); //Creates the starting config
            System.out.println("Start: " + args[0] + ", End: " + args[1]); //Prints the starting string and goal string
            ArrayList<Configuration> path = (ArrayList<Configuration>) Solver.BFS(start); //Solves the puzzle sending it to the Solver class and returns the shortest path
            if(path==null) System.out.println("No solution"); //Print no solution if no path was found
            else {
                for (int i = 0; i < path.size(); i++) {
                    System.out.println("Step " + i + ": " + path.get(i)); //Prints out the path
                }
            }
        }
    }
}
