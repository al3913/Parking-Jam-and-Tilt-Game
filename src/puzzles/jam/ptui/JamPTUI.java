package puzzles.jam.ptui;

import puzzles.common.Observer;
import puzzles.jam.model.JamModel;
import puzzles.jam.solver.Jam;

import java.io.IOException;
import java.util.Scanner;

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

    public JamPTUI(String file) throws IOException {
        model = new JamModel(file);
        model.addObserver(this);
        in = new Scanner(System.in);
        status = true;
    }

    @Override
    public void update(JamModel model, String msg) {
        /*if(model.getBoard().isSolution()) {
            System.out.println("Already Won!");
            displayBoard();
            return;
        }*/
        switch (msg) {
            case "Won" -> System.out.println("Already Won!");
            case "Loaded" -> System.out.println("> Loaded: " + model.getFilename().substring(9));
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
