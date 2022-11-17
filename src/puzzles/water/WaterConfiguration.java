package puzzles.water;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Main WaterConfiguration class that is used when solving a puzzle
 * Implements configuration interface
 *
 * @author Matt Kingston
 */
public class WaterConfiguration implements Configuration {

    private static int goal; //Wanted amount of water in a bucket
    public int[] buckets; //Integer array of buckets, each with an amount of water
    private static int[] MAX; //Integer array that holds the maximum value of each bucket

    /**
     * Create a new WaterConfiguration
     * @param goal Desired amount of water in one bucket
     * @param max Max amount each bucket can hold
     * @param curr current amount in each bucket
     */
    public WaterConfiguration(int goal, int[] max, int[] curr) {
        MAX = max.clone();
        buckets = curr.clone();
        WaterConfiguration.goal = goal;
    }

    /**
     * Fills a bucket to it's max capacity
     * @param ind index of the bucket being filled
     * @return returns the new array of buckets with the updated values
     */
    public int[] fill(int ind) {
        int[] copy = buckets.clone(); //Copies current bucket array in order to not mess up neighbors
        copy[ind] =  MAX[ind];
        return copy;
    }

    /**
     * Empties a bucket completely
     * @param ind index of the bucket being dumped
     * @return returns the new array of buckets with the updated values
     */
    public int[] dump(int ind) {
        int[] copy = buckets.clone(); //Copies current bucket array in order to not mess up neighbors
        copy[ind] = 0;
        return copy;
    }

    /**
     * Pours bucket1 into bucket2
     * @param ind1 index of the bucket being poured
     * @param ind2 index of the bucket being filled
     * @return returns the new array of buckets with the updated values
     */
    public int[] pour(int ind1, int ind2) {
        int[] copy = buckets.clone(); //Copies current bucket array in order to not mess up neighbors
        int diff = MAX[ind2] - copy[ind2]; //How much bucket 2 can be filled
        if(diff > copy[ind1]) diff = copy[ind1]; //If bucket1 has less water in it then bucket 2 can be filled, diff = amount in bucket 1
        copy[ind2] += diff;
        copy[ind1] -= diff;
        return copy;
    }

    /**
     * Checks if one of the buckets has the desired amount
     * @return true if one bucket has the goal value, false if not
     */
    @Override
    public boolean isSolution() {
        for(int i: buckets) {
            if(i==goal) return true;
        }
        return false;
    }

    /**
     * Gets the neighbors of a bucket configuration
     * For each bucket, fills, dumps, and pours into every other bucket
     * @return Returns an array list with every neighbor of a bucket
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> neighbors = new ArrayList<>(); //Creates the list
        for(int i=0;i<buckets.length;i++) { //For every bucket in the list
            neighbors.add(new WaterConfiguration(goal, MAX, fill(i))); //Fills
            neighbors.add(new WaterConfiguration(goal, MAX, dump(i))); //Dumps
            for(int k=0; k<buckets.length;k++) { //For every other bucket in the list
                if(k!=i) neighbors.add(new WaterConfiguration(goal, MAX, pour(i, k))); //Pours
            }
        }
        return neighbors; //Returns list
    }

    /**
     * Returns a hashcode for a bucket configuration
     * @return string that represents the hashcode
     */
    @Override
    public int hashCode() {
        String s = "";
        for(int i: buckets) s += i; //Adds each buckets value to a string
        return s.hashCode(); //Converts string to a hashcode
    }

    /**
     * Compares two WaterConfigurations to see if they are equal
     * @param other bucket array being compared
     * @return true if bucket arrays are equal, false if not
     */
    @Override
    public boolean equals(Object other) {
        boolean b = true;
        if (other instanceof WaterConfiguration water) { //If object is a WaterConfiguration
            for(int i=0;i<buckets.length;i++) { //For each bucket in the array
                if(water.buckets[i] != this.buckets[i]) { //If same buckets have different values, return false and break out of loop
                    b = false;
                    break;
                }
            }
        }
        return b; //Return true if equal
    }

    /**
     * Returns a string representing an array of buckets
     * @return string values for buckets
     */
    @Override
    public String toString() {
        String s ="[";
        for(int i=0;i<buckets.length;i++) {
            s += buckets[i]; //Adds each number to the string
            if(i<buckets.length-1) s+=", "; //When not the last number, make a space with a comma
        }
        return s+="]"; //Close bracket
    }
}
