package puzzles.common.solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * The main solver class that uses Breadth First Search to solve a variety of puzzles
 *
 * @author Matt Kingston
 */

public class Solver {

    /**
     * A method that uses BFS to solve a puzzle, given a starting configuration
     * Adds a configurations neighbors to a queue and goes through the queue to search for solutions
     * Repeats this process until a solution is found or all unique configurations are visited
     *
     * @param start starting configuration
     * @return returns a collection of configurations that represent the shortest path taken to the goal
     */

    private static int total; //Global variables to avoid printing during BFS method
    private static int unique;

    public static Collection<Configuration> BFS(Configuration start) {
        total = 1; unique = 1; //total config and unique config counts start at 1
        Configuration end = null; //Ending configuration starts at null
        LinkedList<Configuration> queue = new LinkedList<>(); //Queue of configurations
        queue.add(start); //Adds the starting config to the queue
        HashMap<Configuration, Configuration> predecessors = new HashMap<>(); //Map of visited configs
        predecessors.put(start, start); //Adds starting config to predecessors
        while(!queue.isEmpty()) { //While there are still configs in the queue
            Configuration current = queue.remove(0); //Checking next config in queue
            if(current.isSolution()) {
                end = current; //sets current config to end if it's the solution and breaks out of the while loop
                break;
            }
            for (Configuration nbr: current.getNeighbors()) { //For every neighbor the current config has
                total++; //Increase total amount of configs
                if(!predecessors.containsKey(nbr)) { //If the neighbor is not a predecessor
                    predecessors.put(nbr, current);
                    queue.add(nbr); //Add it to predecessors and queue
                    unique++; //Increase unique number of configs
                }
            }
        }
        return makePath(start, end, predecessors); //Returns the path taken
    }

    private static Collection<Configuration> makePath(Configuration start, Configuration end, HashMap<Configuration, Configuration> predecessors) {
        ArrayList<Configuration> path = new ArrayList<>(); //List that represents the path
        if(end==null) return null; //If no solution was found, return no path
        if(predecessors.containsKey(end)) {
            Configuration current = end; //Start at final config
            while(current != start) { //Until config is back at the start
                path.add(0, current); //Add config to the front of the list
                current = predecessors.get(current); //New config is the value of the current key
            }
            path.add(0, start); //Add the starting config to path
        }
        return path; //Return path
    }

    public static void printConfigs() { //Prints configs
        System.out.println("Total configs: " + total); //Print total configs
        System.out.println("Unique configs: " + unique); //Print unique configs
    }
}
