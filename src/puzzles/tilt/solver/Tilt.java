package puzzles.tilt.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.model.TiltConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class for the Tilt puzzle
 *
 * @author Matt Kingston
 */

public class Tilt {
    /**
     * Reads in the given file and converts it to a tilt configuration
     * Uses BFS solver to find the shortest amount of moves
     * @param args the file being read in
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.out.println("Usage: java Tilt filename");
        }
        else {
            System.out.println("File: " + args[0]); //Prints what file is being loaded
            Scanner in = new Scanner(new File(args[0])); //Reads in dimension of board and 2D array representation of game
            int DIM = Integer.parseInt(in.nextLine());
            char[][] board = new char[DIM][DIM];
            while (in.hasNext()) {
                for(int r=0; r<DIM; r++) {
                    String[] fields = in.nextLine().split("\\s+");
                    for(int c=0;c<fields.length;c++) {
                        board[r][c] = fields[c].charAt(0);
                    }
                }
            }
            TiltConfig t = new TiltConfig(DIM, board); //uses that data to create a config
            System.out.println(t);
            ArrayList<Configuration> path = (ArrayList<Configuration>) Solver.BFS(t); //Solves the puzzle sending it to the Solver class and returns the shortest path
            Solver.printConfigs(); //Prints out number of total and unique configs
            if(path==null) System.out.println("No solution"); //Print no solution if no path was found
            else {
                for (int i = 0; i < path.size(); i++) {
                    System.out.println("Step " + i + ":\n" + path.get(i)); //Prints out the path
                }
            }
        }
    }
}
