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

public class JamModel {
    /** the collection of observers of this model */
    private final List<Observer<JamModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private JamConfig currentConfig;
    private JamConfig initialConfig;
    private String loadedGame;

    private boolean selected;
    private String currentCar;
    public String[] prevSelect;
    public String[] commands;

    public JamModel(String file) throws IOException {
        loadedGame = file;
        loadFile(file);
    }

    public void getHint() {
        if(currentConfig.isSolution()) { alertObservers("Won"); return; }
        ArrayList<Configuration> sol = (ArrayList<Configuration>) Solver.BFS(currentConfig);
        if(sol==null) alertObservers("NoHint");
        else {
            currentConfig = (JamConfig) sol.get(1);
            alertObservers("Hint");
        }
    }
    public JamConfig getBoard()
    {
        return currentConfig;
    }

    public String getFilename()
    {
        return loadedGame;
    }

    public void loadFile(String file)
    {
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


    public void move(String dir)
    {
        if(currentConfig.isSolution()) { alertObservers("Won"); return; }
        Car car = currentConfig.getCar(currentCar);
        for (int i = 0; i < currentConfig.getCars().size(); i++)
            System.out.println(currentConfig.getCars().get(i).getLetter());
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

    public void select(String[] commands)
    {
        if (selected)
        {
            this.commands = commands;
            int moveH = Integer.parseInt(commands[2]) - Integer.parseInt(prevSelect[2]); //swapped numbers
            int moveV = Integer.parseInt(commands[1]) - Integer.parseInt(prevSelect[1]);
            if ((currentConfig.collision(Integer.parseInt(commands[2]), Integer.parseInt(commands[1]))))
            {
                if (moveH != 0 && moveV == 0)
                {
                    if (moveH > 0 && (!(Integer.parseInt(commands[2]) >= currentConfig.getWidth()))) move("R");
                    else if ((!(Integer.parseInt(commands[2]) < 0))) move("L");
                }
                else if (moveH == 0 && moveV != 0)
                {
                    if (moveV > 0 && (!(Integer.parseInt(commands[1]) >= currentConfig.getHeight()))) move("D");
                    else if ((!(Integer.parseInt(commands[1]) < 0))) move("U");
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
            else
                alertObservers("NoCar");
        }
    }
    public void reset() {
        currentConfig = initialConfig;
        alertObservers("Reset");
    }
}
