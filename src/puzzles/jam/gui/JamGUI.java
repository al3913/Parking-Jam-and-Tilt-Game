package puzzles.jam.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.jam.model.Car;
import puzzles.jam.model.JamConfig;
import puzzles.jam.model.JamModel;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class JamGUI extends Application  implements Observer<JamModel, String>  {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    private final static String X_CAR_COLOR = "#DF0101";
    private final static int BUTTON_FONT_SIZE = 20;
    private final static int ICON_SIZE = 75;

    private int rows;
    private int cols;
    private JamModel model;
    private BorderPane bp = new BorderPane();
    private GridPane grid;

    public void init() throws IOException {
        String filename = getParameters().getRaw().get(0);
        this.model = new JamModel(filename);
        model.addObserver(this);
        rows = model.getBoard().getHeight();
        cols = model.getBoard().getWidth();
    }

    @Override
    public void start(Stage stage) throws Exception {
        //Top part of BP
        Label top = new Label();
        top.setAlignment(Pos.CENTER);
        top.setText("Loaded: " + model.getFilename().substring(9));
        //Center of BP
        grid = createBoard();
        //Bottom of BP
        HBox bot = new HBox();
        bot.setAlignment(Pos.CENTER);
        Button load = new Button("Load");
        load.setOnAction(event -> {
            //create a new FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load a game board.");
            //open up a window for the user to interact with.
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null)
            {
                model.loadFile(String.valueOf(selectedFile));
            }
            else
            {
                top.setText("No File Found");
            }
        });
        bot.getChildren().add(load);
        Button reset = new Button("Reset");
        reset.setOnAction(event -> {
            model.reset();
        });
        bot.getChildren().add(reset);
        Button hint = new Button("Hint");
        hint.setOnAction(event -> {
            model.getHint();
        });
        bot.getChildren().add(hint);
        // Assigning
        bp.setTop(top);
        bp.setCenter(grid);
        bp.setBottom(bot);
        Scene s = new Scene(bp);
        stage.setScene(s);
        stage.setTitle("Jam GUI");
        stage.setResizable(false);
        stage.show();
    }

    public GridPane createBoard()
    {
        GridPane result = new GridPane();
        for(int r = 0; r < rows; r++)
            for(int c = 0; c < cols; c++)
            {
                Button b = new Button();
                String letter = String.valueOf(model.getBoard().getGrid()[c][r]);
                if(!letter.equals("."))
                {
                    Car current = model.getBoard().getCar(letter);
                    String color = current.getColor(letter);
                    b.setText(letter);
                    b.setStyle("-fx-font-size: " + BUTTON_FONT_SIZE + ";" + "-fx-background-color: " + color + ";" + "-fx-font-weight: bold;");
                    int finalC = c;
                    int finalR = r;
                    b.setOnAction(event -> {
                        String[] action = new String[] {"s", String.valueOf(finalC), String.valueOf(finalR)};
                        model.select(action);
                    });
                }
                int finalC1 = c;
                int finalR1 = r;
                b.setOnAction(event -> {
                    String[] action = new String[] {"s", String.valueOf(finalC1), String.valueOf(finalR1)};
                    model.select(action);
                });
                b.setMinSize(ICON_SIZE, ICON_SIZE);
                b.setMaxSize(ICON_SIZE, ICON_SIZE);
                // r then c because of how a 2D array works
                result.add(b, r, c);
            }
        return result;
    }

    public void refresh()
    {
        grid = createBoard();
        bp.setCenter(grid);
    }

    @Override
    public void update(JamModel model, String msg) {
        Label top = new Label();
        switch (msg) {
            case "Won" -> top.setText("Already Won!");
            case "Loaded" -> top.setText("> Loaded: " + model.getFilename().substring(9));
            case "LoadFailed" -> top.setText("> Failed to load: " + model.getFilename());
            case "NoHint" -> top.setText("Current Board is unsolvable. Please restart.");
            case "Hint" -> top.setText("> Next step!");
            case "Moved" ->
                    top.setText("> Moved from (" + model.prevSelect[1] + "," + model.prevSelect[2] + ")" + "  to (" + model.commands[1] + "," + model.commands[2] + ")");
            case "Select" -> top.setText("> Selected (" + model.prevSelect[1] + "," + model.prevSelect[2] + ")");
            case "Invalid" ->
                    top.setText("> Can't moved from (" + model.prevSelect[1] + "," + model.prevSelect[2] + ")" + "  to (" + model.commands[1] + "," + model.commands[2] + ")");
            case "NoCar" -> top.setText("> No car at (" + model.prevSelect[1] + "," + model.prevSelect[2] + ")");
            case "Reset" -> top.setText("Puzzle reset!");
        }
        bp.setTop(top);
        refresh();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamGUI filename");
        } else {
            Application.launch(args);
        }
    }
}
