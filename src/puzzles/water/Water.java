package puzzles.water;

import puzzles.common.solver.Solver;
import puzzles.common.solver.Configuration;

import java.util.ArrayList;


/**
 * Main class for the water buckets puzzle.
 *
 * @author Matt Kingston
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     *
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Water amount bucket1 bucket2 ..."));
        }
        else {
            int[] config = new int[args.length-1]; //Creates new array to store max values
            String s = "["; //String to print out max values of each bucket
            for(int i=1; i<args.length; i++) {
                config[i-1] = Integer.parseInt(args[i]); //Adds value to list and string
                s += args[i];
                if(i<args.length-1) s+=", "; //Adds space between each bucket
            }
            int[] curr = new int[config.length]; //Initial values of buckets are 0
            WaterConfiguration start = new WaterConfiguration(Integer.parseInt(args[0]), config, curr); //Creates starting config
            System.out.println("Amount: " + args[0] + ", Buckets: " + s + "]");
            ArrayList<Configuration> path = (ArrayList<Configuration>) Solver.BFS(start); //Solves for solution, returns the shortest path
            Solver.printConfigs();
            if(path==null) System.out.println("No solution"); //If no path found, print no solution
            else {
                for (int i = 0; i < path.size(); i++) {
                    System.out.println("Step " + i + ": " + path.get(i)); //Prints the pathway
                }
            }
        }
    }
}
