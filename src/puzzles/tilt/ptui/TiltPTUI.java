package puzzles.tilt.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import java.util.Scanner;

/**
 * Main text ui class for the tilt game
 *
 * @author Matt Kingston
 */

public class TiltPTUI implements Observer<TiltModel, String> {

    /** commands that can be performed **/
    private static final String COMMANDS = "h(int) ---> hints next move\n" + "l(oad) filename ---> loads new file\n" +
                                           "t(ilt) [N,E,S,W] ---> tilt board in given direction\n" + "q(uit) --> quits the game\n" +
                                            "r(eset) ---> resets the current game";

    private TiltModel model; //Model of the game
    private Scanner in; //Reads in the commands
    private boolean running; //If the game is running or not

    /**
     * Constructor for PTUI that creates and connects to the model, makes running true
     * @param fileName name of the file to be loaded
     */
    public TiltPTUI(String fileName) {
        model = new TiltModel(fileName);
        model.addObserver(this);
        running = true;
        in = new Scanner( System.in );
    }

    /**
     * Recieves and acts based on what the model performed
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param msg optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(TiltModel model, String msg) {
        if(model.getBoard().isSolution()) { //If the current config is a solution
            System.out.println("Already Won!"); //Indicate the user
            displayBoard(); //Display the board
            return;
        }
        switch (msg) {
            case "Loaded": //If the model loaded a file
                System.out.println("Loaded: " + model.getFileName());
                break;
            case "LoadFailed": //If the model failed to load a file
                System.out.println("Failed to load: " + model.getFileName());
                break;
            case "NoHint": //If no hint can be given
                System.out.println("Current Board is unsolvable. Please restart.");
                break;
            case "Hint": //If a hint can be given
                System.out.println("Next Step!");
                break;
            case "Moved": //If the model performed a move
                break;
            case "Invalid": //If the move is invalid
                System.out.println("Invalid move!");
                break;
            case "Reset": //If the model reset the board
                System.out.println("Board Reset!");
                break;
        }
        displayBoard(); //Always displays the current board at the end
    }

    /**
     * Main run method to run the gui while the game is being played
     */
    public void run() {
        System.out.println("Loaded: " + model.getFileName()); //Prints the intial config loaded
        displayBoard();
        System.out.println(COMMANDS); //Prints the commands
        boolean b = true; //boolean to tell if the game is looping
        while(b) {
            b = gameLoop();
        }
    }

    /**
     * Main game loop that runs while the game is being played
     * @return true while the game is running, false when not
     */
    public boolean gameLoop() {
        while(running) {
            String[] commands = in.nextLine().strip().toLowerCase().split("\\s+"); //Commands given by the user
            if(commands.length==1) {
                if(commands[0].equals("q") || commands[0].equals("quit"))  { //If the user wants to quit
                    running = false; //Set running to false, break out of loop
                    return false;
                }
                else if(commands[0].equals("h")||commands[0].equals("hint")) { //If the user wants a hint
                    model.getHint();
                }
                else if(commands[0].equals("r")||commands[0].equals("reset")) { //If the user wants to reset
                    model.reset();
                }
                else System.out.println(COMMANDS); //If not a proper command, print the commands
            }
            else if(commands.length==2) {
                if(commands[0].equals("l")||commands[0].equals("load")) model.loadFromFile(commands[1]); //Load the file given by the user
                else if((commands[0].equals("t") && commands[1].length()==1)||(commands[0].equals("tilt") && commands[1].length()==1)) {
                    char dir = commands[1].charAt(0);
                    model.move(dir); //Move in the direction given by the user
                }
                else System.out.println(COMMANDS);
            }
            else System.out.println(COMMANDS);
        }
        return true; //Return true to keep the game running
    }

    /**
     * Displays the current board configuration
     */
    public void displayBoard() {
        System.out.println(model.getBoard());
    }

    /**
     * Main method that creates a text ui and initializes using the given filename, then calls run to start the interface
     * @param args filename to be loaded
     */
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
