package puzzles.jam.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.solver.Jam;
import puzzles.tilt.model.TiltConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *  Model class for the Model-View-Controller Structure of the PTUI and GUI
 *
 * @author Andy Lin
 */
public class JamModel {
    /** the collection of observers of this model */
    private final List<Observer<JamModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private JamConfig currentConfig;
    private JamConfig initialConfig;
    public String loadedGame;

    private boolean selected;
    private String currentCar;
    public String[] prevSelect;
    public String[] commands;

    /**
     * JamModel constructor
     * @param file file loaded representing the game
     * @throws IOException
     */
    public JamModel(String file) throws IOException {
        loadedGame = file;
        loadFile(file);
    }

    /**
     * Gets the next move and commences the move
     */
    public void getHint() {
        if(currentConfig.isSolution()) { alertObservers("Won"); return; }
        ArrayList<Configuration> sol = (ArrayList<Configuration>) Solver.BFS(currentConfig);
        if(sol==null) alertObservers("NoHint");
        else {
            currentConfig = (JamConfig) sol.get(1);
            alertObservers("Hint");
        }
    }

    /**
     * Gets the current config
     * @return currentConfig
     */
    public JamConfig getBoard()
    {
        return currentConfig;
    }

    /**
     * Gets the filename
     * @return filename/loadedGame
     */
    public String getFilename()
    {
        return loadedGame;
    }

    /**
     * Initially Loads the file based on the String form and assigns the configs
     * @param file the file
     */
    public void loadFile(String file)
    {
        System.out.println(loadedGame);
        try
        {
            initialConfig = new JamConfig(file);
            currentConfig = new JamConfig(file);
            alertObservers("Loaded");
        }
        catch (IOException ioe)
        {
            alertObservers("LoadFailed");
        }
    }


    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<JamModel, String> observer) {
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
     * Handles Creation of the new config whenever a move is made
     * @param dir represents the direction that the user is trying to move
     */
    public void move(String dir)
    {
        if(currentConfig.isSolution()) { alertObservers("Won"); return; }
        Car car = currentConfig.getCar(currentCar);
        int i = currentConfig.getCars().indexOf(car);
        int startR = car.getStartRow();
        int endR = car.getEndRow();
        int startC = car.getStartCol();
        int endC = car.getEndCol();
        switch(dir)
        {
            case "R": currentConfig = new JamConfig(startR,endR,startC+1,endC+1, currentConfig, i, dir); break;
            case "L": currentConfig = new JamConfig(startR,endR,startC-1,endC-1, currentConfig, i, dir); break;
            case "U": currentConfig = new JamConfig(startR+1, endR+1, startC, endC, currentConfig, i, dir); break;
            case "D": currentConfig = new JamConfig(startR - 1, endR - 1, startC, endC, currentConfig, i, dir); break;
        }
        alertObservers("Moved");

    }

    /**
     * Represents when the user selects a cell to move
     * @param commands contains the information on what cell was selected
     */
    public void select(String[] commands)
    {
        if (selected)
        {
            this.commands = commands;
            int moveH = Integer.parseInt(commands[2]) - Integer.parseInt(prevSelect[2]); //swapped numbers
            int moveV = Integer.parseInt(commands[1]) - Integer.parseInt(prevSelect[1]);
            String orient = currentConfig.getCar(currentCar).getDirection();
            if ((currentConfig.collision(Integer.parseInt(commands[2]), Integer.parseInt(commands[1]))))
            {
                if (moveH != 0 && moveV == 0 && orient.equals("H"))
                {
                    if (moveH > 0 && (!(Integer.parseInt(commands[2]) >= currentConfig.getWidth()))) move("R");
                    else if ((!(Integer.parseInt(commands[2]) < 0))) move("L");
                }
                else if (moveH == 0 && moveV != 0 && orient.equals("V"))
                {
                    if (moveV > 0 && (!(Integer.parseInt(commands[1]) >= currentConfig.getHeight()))) move("U");
                    else if ((!(Integer.parseInt(commands[1]) < 0))) move("D");
                }
            }
            else
                alertObservers("Invalid");

            selected = false;
        }
        else
        {
            if(currentConfig.getGrid()[Integer.parseInt(commands[1])][Integer.parseInt(commands[2])] != '.')
            {
                selected = true;
                prevSelect = commands;
                alertObservers("Select");
                currentCar = String.valueOf(currentConfig.getGrid()[Integer.parseInt(commands[1])][Integer.parseInt(commands[2])]);
            }
            else {
                prevSelect = commands;
                alertObservers("NoCar");
            }
        }
    }

    /**
     * Resets the current config to original config
     */
    public void reset() {
        currentConfig = initialConfig;
        alertObservers("Reset");
    }
}