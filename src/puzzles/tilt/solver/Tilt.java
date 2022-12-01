package puzzles.tilt.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.model.TiltConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Tilt {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.out.println("Usage: java Tilt filename");
        }
        else {
            System.out.println("File: " + args[0]);
            Scanner in = new Scanner(new File(args[0]));
            int DIM = Integer.parseInt(in.nextLine());
            char[][] board = new char[DIM][DIM];
            while (in.hasNext()) {
                for(int r=0; r<DIM; r++) {
                    String[] fields = in.nextLine().split("\\s+");
                    for(int c=0;c<fields.length;c++) {;
                        board[r][c] = fields[c].charAt(0);
                    }
                }
            }
            TiltConfig t = new TiltConfig(DIM, board);
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
