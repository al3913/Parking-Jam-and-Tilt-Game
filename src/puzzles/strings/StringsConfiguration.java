package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Main StringConfiguration class that is used when solving a puzzle
 * Implements configuration interface
 *
 * @author Matt Kingston
 */
public class StringsConfiguration implements Configuration {

    private final String start; //Starting string
    private static String goal; //Ending string

    /**
     * Creates a new string config
     * @param start current string value
     * @param goal goal string value
     */
    public StringsConfiguration(String start, String goal) {
        this.start = start;
        StringsConfiguration.goal = goal;
    }

    /**
     * Checks if current config is a solution
     * @return true if it is a solution, false if not
     */
    @Override
    public boolean isSolution() {
        return start.equals(goal);
    }

    /**
     * Gets neighbors of a string
     * For each character in a string, changes the letter to one before and after the current
     * @return Array List of neighboring configs
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> neighbors = new ArrayList<>();
        for(int i=0;i<start.length();i++) {
            int high = start.charAt(i) + 1; //ASCII value of character after
            int low = start.charAt(i) - 1; //ASCII value of character before
            if (low < 65) low = 90; //if char after is A, make it go to Z
            if (high > 90) high = 65; //if char before is Z, make it go to A
            String up = start.substring(0, i) + (char) (high) + start.substring(i + 1); //Creates new substrings with new chars
            String down = start.substring(0, i) + (char) (low) + start.substring(i + 1);
            neighbors.add(new StringsConfiguration(down, goal)); //Adds new configs to neighbors list
            neighbors.add(new StringsConfiguration(up, goal));
        }
        return neighbors; //return the list
    }

    /**
     * Returns the current value of the string
     * @return string value
     */
    @Override
    public String toString() {
        return this.start;
    }

    /**
     * Returns hash code of the string
     * @return java implemented hashcode for strings
     */
    @Override
    public int hashCode() {
        return start.hashCode();
    }

    /**
     * Compares to string config values to see if they are equal
     * @param other string config being compared to
     * @return true if strings are equal, false if not
     */
    @Override
    public boolean equals(Object other) {
        boolean b = false;
        if (other instanceof StringsConfiguration string) {
            b = string.start.equals(this.start);
        }
        return b;
    }
}
