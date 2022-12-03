package puzzles.tilt.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for the model of tilt interfaces
 * Communicates actions between the interfaces
 * @author Matt Kingston
 */

public class TiltModel {

    /** the collection of observers of this model */
    private final List<Observer<TiltModel, String>> observers = new LinkedList<>();
    /** the current configuration */
    private TiltConfig currentConfig; //Current configuration
    private TiltConfig initialLoad; //Initial configuration of a loaded file
    private String loadedGame; //The current loaded game

    public TiltModel(String fileName) {
        loadedGame = fileName; //Stores the name of the given file
        loadFromFile(fileName); //Loads the given file
    }

    public int getDimension() { return currentConfig.getDIM(); } //Returns the dimension of the board

    public String getFileName() { return loadedGame; } //Returns the current loaded file's name

    public TiltConfig getBoard() { return currentConfig; } //Returns the current configurations

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<TiltModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /**
     * Converts the filename to a file and sends to the other load from file method
     * @param fileName the string of the filename
     */
    public void loadFromFile(String fileName) {
        loadFromFile(new File(fileName));
    }

    /**
     * Loads the board from the given file
     * @param file the file being loaded
     */
    public void loadFromFile(File file){
        loadedGame = file.getName(); //Stores the file name
        try { //If the file is found
            Scanner in = new Scanner(file); //Reads in data from the file
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
            initialLoad = new TiltConfig(DIM, board); //Sets initial load to the generated board
            currentConfig = new TiltConfig(DIM, board); //Sets current config to the generated board
            alertObservers("Loaded"); //Announce to the interface that a game was loaded
        }
        catch (FileNotFoundException f) {
            alertObservers("LoadFailed"); //Announce to the interface that the load failed
        }

    }

    /**
     * Checks to see if the game is solvable, if not, announce so
     * Otherwise, set current config to the next step and go to the next move
     */
    public void getHint() {
        if(currentConfig.isSolution()) { alertObservers("Won"); return; }
        ArrayList<Configuration> sol = (ArrayList<Configuration>) Solver.BFS(currentConfig);
        if(sol==null) alertObservers("NoHint");
        else {
            currentConfig = (TiltConfig) sol.get(1);
            alertObservers("Hint");
        }
    }

    /**
     * Checks if the move is a solution, and alerts observes so
     * Otherwise, move in the given direction, and then alert the observes so
     * @param dir direction of move to be perfromed
     */
    public void move(char dir) {
        if(currentConfig.isSolution()) { alertObservers("Won"); return; }
        TiltConfig n = new TiltConfig();
        switch (dir) {
            case 'n': n = currentConfig.tiltNorth(); break;
            case 'e': n = currentConfig.tiltEast(); break;
            case 's': n = currentConfig.tiltSouth(); break;
            case 'w': n = currentConfig.tiltWest();
        }
        if(n!=null) { currentConfig=n; alertObservers("Moved"); }
        else alertObservers("Invalid");
    }

    /**
     * Resets the board to the initial load and alerts observers so
     */
    public void reset() {
        currentConfig = initialLoad;
        alertObservers("Reset");
    }
}
