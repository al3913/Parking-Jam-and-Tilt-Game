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

public class TiltModel {

    /** the collection of observers of this model */
    private final List<Observer<TiltModel, String>> observers = new LinkedList<>();
    /** the current configuration */
    private TiltConfig currentConfig;
    private TiltConfig initialLoad;
    private String loadedGame;

    public TiltModel(String fileName) {
        loadedGame = fileName;
        loadFromFile(fileName);
    }

    public int getDimension() { return currentConfig.getDIM(); }

    public String getFileName() { return loadedGame; }

    public TiltConfig getBoard() { return currentConfig; }

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

    public void loadFromFile(String fileName) {
        loadFromFile(new File(fileName));
    }

    public void loadFromFile(File file){
        try {
            Scanner in = new Scanner(file);
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
            initialLoad = new TiltConfig(DIM, board);
            currentConfig = new TiltConfig(DIM, board);
            alertObservers("Loaded");
        }
        catch (FileNotFoundException f) {
            alertObservers("LoadFailed");
        }

    }

    public void getHint() {
        if(currentConfig.isSolution()) { alertObservers("Won"); return; }
        ArrayList<Configuration> sol = (ArrayList<Configuration>) Solver.BFS(currentConfig);
        if(sol==null) alertObservers("NoHint");
        else {
            currentConfig = (TiltConfig) sol.get(1);
            alertObservers("Hint");
        }
    }

    public void move(char dir) {
        if(currentConfig.isSolution()) { alertObservers("Won"); return; }
        new TiltConfig();
        TiltConfig n = switch (dir) {
            case 'n' -> currentConfig.tiltNorth();
            case 'e' -> currentConfig.tiltEast();
            case 's' -> currentConfig.tiltSouth();
            case 'w' -> currentConfig.tiltWest();
            default -> new TiltConfig();
        };
        if(n!=null) { currentConfig=n; alertObservers("Moved"); }
        else alertObservers("Invalid");
    }

    public void reset() {
        currentConfig = initialLoad;
        alertObservers("Reset");
    }
}
