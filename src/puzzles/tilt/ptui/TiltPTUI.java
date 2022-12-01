package puzzles.tilt.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class TiltPTUI implements Observer<TiltModel, String> {

    private static final char GREEN = 'G';
    private static final char BLUE = 'B';
    private static final char BLOCK = '*';
    private static final char EMPTY = '.';
    private static final char HOLE = 'O';
    private static final String COMMANDS = "h(int) ---> hints next move\n" + "l(oad) filename ---> loads new file\n" +
                                           "t(ilt) [N,E,S,W] ---> tilt board in given direction\n" + "q(uit) --> quits the game\n" +
                                            "r(eset) ---> resets the current game";

    private TiltModel model;
    private Scanner in;
    private boolean running;

    public TiltPTUI(String fileName) {
        model = new TiltModel(fileName);
        model.addObserver(this);
        running = true;
        in = new Scanner( System.in );
    }

    @Override
    public void update(TiltModel model, String msg) {
        if(model.getBoard().isSolution()) {
            System.out.println("Already Won!");
            displayBoard();
            return;
        }
        switch (msg) {
            case "Loaded":
                System.out.println("Loaded: " + model.getFileName());
                break;
            case "LoadFailed":
                System.out.println("Failed to load: " + model.getFileName());
                break;
            case "NoHint":
                System.out.println("Current Board is unsolvable. Please restart.");
                break;
            case "Hint":
                System.out.println("Next Step!");
                break;
            case "Moved":
                break;
            case "Invalid":
                System.out.println("Invalid move!");
                break;
            case "Reset":
                System.out.println("Board Reset!");
                break;
        }
        displayBoard();
    }

    public void run() {
        System.out.println("Loaded: " + model.getFileName());
        displayBoard();
        System.out.println(COMMANDS);
        boolean b = true;
        while(b) {
            b = gameLoop();
        }
    }

    public boolean gameLoop() {
        while(running) {
            String[] commands = in.nextLine().strip().toLowerCase().split("\\s+");
            if(commands.length==1) {
                if(commands[0].equals("q") || commands[0].equals("quit"))  {
                    running = false;
                    return false;
                }
                else if(commands[0].equals("h")||commands[0].equals("hint")) {
                    model.getHint();
                }
                else if(commands[0].equals("r")||commands[0].equals("reset")) {
                    model.reset();
                }
                else System.out.println(COMMANDS);
            }
            else if(commands.length==2) {
                if(commands[0].equals("l")||commands[0].equals("load")) model.loadFromFile(commands[1]);

                else if((commands[0].equals("t") && commands[1].length()==1)||(commands[0].equals("tilt") && commands[1].length()==1)) {
                    char dir = commands[1].charAt(0);
                    model.move(dir);
                }
                else System.out.println(COMMANDS);
            }
            else System.out.println(COMMANDS);
        }
        return true;
    }

    public void displayBoard() {
        System.out.println(model.getBoard());
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltPTUI filename");
        }
        else {
            TiltPTUI ui = new TiltPTUI(args[0]);
            ui.run();
        }
    }
}
