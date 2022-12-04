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
        if(model.getBoard().isSolution()) {
            System.out.println("Already Won!");
            displayBoard();
            return;
        }
        switch (msg) {
            case "Loaded" -> System.out.println("> Loaded: " + model.getFilename());
            case "LoadFailed" -> System.out.println("> Failed to load: " + model.getFilename());
            case "NoHint" -> System.out.println("Current Board is unsolvable. Please restart.");
            case "Hint" -> System.out.println("> Next Step!");
            case "Moved" ->
                    System.out.println("> Moved from (" + model.prevSelect[1] + "," + model.prevSelect[2] + ")" + "  to (" + model.commands[1] + "," + model.commands[2] + ")");
            case "Selected" -> System.out.println("> Selected (" + model.prevSelect[1] + "," + model.prevSelect[2] + ")");
            case "Invalid" ->
                    System.out.println("> Can't moved from (" + model.prevSelect[1] + "," + model.prevSelect[2] + ")" + "  to (" + model.commands[1] + "," + model.commands[2] + ")");
            case "NoCar" -> System.out.println("> No car at (" + model.prevSelect[1] + "," + model.prevSelect[2] + ")");
            case "Reset" -> System.out.println("Puzzle reset!");
        }
        displayBoard();
    }

    public void run()
    {
        System.out.println("Loaded: " + model.getFilename());
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
                if (commands[0].equals("q") || commands[0].equals("quit")) {
                    status = false;
                    return false;
                } else if (commands[0].equals("h") || commands[0].equals("hint")) {
                    model.getHint();
                } else if (commands[0].equals("r") || commands[0].equals("reset")) {
                    model.reset();
                } else System.out.println(COMMANDS);
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
        System.out.println(model.getBoard());
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
