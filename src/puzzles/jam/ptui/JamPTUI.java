package puzzles.jam.ptui;

import puzzles.common.Observer;
import puzzles.jam.model.JamModel;
import puzzles.jam.solver.Jam;

import java.io.IOException;
import java.util.Scanner;

/**
 * JamPTUI class that creates the PTUI version of the Jam game
 *
 * @author Andy Lin
 */
public class JamPTUI implements Observer<JamModel, String> {
    private JamModel model;
    private Scanner in;
    private boolean status;

    private static final String COMMANDS =
            "h(int)              -- hint next move\n" +
            "l(oad) filename     -- load new puzzle file\n" +
            "s(elect) r c        -- select cell at r, c\n" +
            "q(uit)              -- quit the game\n" +
            "r(eset)             -- reset the current game";

    /**
     * Constructor for the PTUI
     * @param file to be sent to the model to be loaded
     * @throws IOException
     */
    public JamPTUI(String file) throws IOException {
        model = new JamModel(file);
        model.addObserver(this);
        in = new Scanner(System.in);
        status = true;
    }

    /**
     * Receives and acts based on what the model performed
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param msg optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(JamModel model, String msg) {
        switch (msg) {
            case "Won" -> System.out.println("Already Won!");
            case "Loaded" -> System.out.println("> Loaded: " + model.getFilename().substring(model.getFilename().indexOf("txt")-6));
            case "LoadFailed" -> System.out.println("> Failed to load: " + model.getFilename());
            case "NoHint" -> System.out.println("Current Board is unsolvable. Please restart.");
            case "Hint" -> System.out.println("> Next step!");
            case "Moved" ->
                    System.out.println("> Moved from (" + model.prevSelect[1] + "," + model.prevSelect[2] + ")" + "  to (" + model.commands[1] + "," + model.commands[2] + ")");
            case "Select" -> System.out.println("> Selected (" + model.prevSelect[1] + "," + model.prevSelect[2] + ")");
            case "Invalid" ->
                    System.out.println("> Can't moved from (" + model.prevSelect[1] + "," + model.prevSelect[2] + ")" + "  to (" + model.commands[1] + "," + model.commands[2] + ")");
            case "NoCar" -> System.out.println("> No car at (" + model.prevSelect[1] + "," + model.prevSelect[2] + ")");
            case "Reset" -> System.out.println("Puzzle reset!");
        }
        displayBoard();
        System.out.println("");
    }

    /**
     * Main run method to run the gui while the game is being played
     */
    public void run()
    {
        System.out.println("Loaded: " + model.getFilename().substring(9));
        displayBoard();
        System.out.println(COMMANDS);
        boolean b = true;
        while(b) {
            b = gameLoop();
        }
    }

    /**
     * Main game loop that runs while the game is being played
     * @return true if the game is still going on
     */
    public boolean gameLoop() {
        while (status) {
            String[] commands = in.nextLine().strip().toLowerCase().split("\\s+");
            if (1 == commands.length) {
                switch (commands[0]) {
                    case "q", "quit" -> {
                        status = false;
                        System.out.print(">");
                        return false;
                    }
                    case "h", "hint" -> model.getHint();
                    case "r", "reset" -> model.reset();
                    default -> System.out.println(COMMANDS);
                }
            } else if (commands.length == 2) {
                if (commands[0].equals("l") || commands[0].equals("load")) model.loadFile(commands[1]);
                else System.out.println(COMMANDS);
            } else if (commands.length == 3) {
                if (commands[0].equals("s") || commands[0].equals("select")) {
                    model.select(commands);
                }
            } else System.out.println(COMMANDS);
        }
        return true;
    }

    /**
     * Display method for printing out the board in the PTUI Form
     */
    public void displayBoard()
    {
        int width = model.getBoard().getWidth();
        int height = model.getBoard().getHeight();
        System.out.print("  ");
        for(int col = 0; col < width; col++)
            System.out.print(col + " ");
        System.out.print("\n ");
        for(int border = 0; border < width; border++)
        {
            System.out.print("--");
        }
        System.out.println();
        //Each Row
        for(int row = 0; row < height; row++)
        {
            System.out.print(row + "|");
            char[][] board = model.getBoard().getGrid();
            for(int col = 0; col < width; col++)
                System.out.print(board[row][col] + " ");
            System.out.println();
        }

    }

    /**
     * Main program to launch the application and send in the given file to be loaded
     * @param args the file to be loaded
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java JamPTUI filename");
        }
        else {
            JamPTUI ui = new JamPTUI(args[0]);
            ui.run();
        }
    }
}
