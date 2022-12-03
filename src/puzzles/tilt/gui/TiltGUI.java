package puzzles.tilt.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.File;

/**
 * TiltGUI class that displays the TiltModel in visually
 * Contains all the methods for the GUI and Model to interact properly
 *
 * @author Matt Kingston
 */

public class TiltGUI extends Application implements Observer<TiltModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    private TiltModel model; //The model
    private BorderPane b2; //Border pane, so it can be updated throughout the game
    private GridPane g; //Grid pane, so it can be updated throughout the game/
    private Label l; //Label, so it can be updated throughout the game

    /** Presets used to represent the types of spaces on a borad **/
    private final Image greenDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green.png"), 50, 50, false, false);
    private final Image block = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"block.png"), 50, 50, false, false);
    private final Image hole = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"hole.png"), 50, 50, false, false);
    private final Image blueDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"blue.png"), 50, 50, false, false);
    private final String empty = "-fx-background-color: white; -fx-border-color: lightgray";

    /**
     * Initializes the gui by creating a model and making the gui an observer of it
     */
    public void init() {
        String filename = getParameters().getRaw().get(0);
        this.model = new TiltModel(filename);
        model.addObserver(this);
        System.out.println("init:   Initialize and connect to model!");
    }

    /**
     * Creates the base window of the game
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane main = new BorderPane();

        l = new Label(); //Creates the label
        l.setMaxWidth(225+model.getDimension()*100);
        l.setMinHeight(50);
        l.setAlignment(Pos.CENTER);
        l.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        l.setText("Loaded: " + model.getFileName()); //Starts by loading the given filename
        main.setTop(l); //Sets the label to the top

        VBox v = new VBox(); //Vbox to contain the load, hint and reset buttons
        v.setMaxHeight(200);
        v.setMinWidth(90);
        v.setSpacing(50);
        Button load = new Button("Load");
        load.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        load.setMinWidth(75);
        load.setOnAction(event -> loadFromFile()); //Sets action of load button to the load method
        Button reset = new Button("Reset");
        reset.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        reset.setMinWidth(75);
        reset.setOnAction(event -> model.reset());
        Button hint = new Button("Hint");
        hint.setOnAction(event -> model.getHint());
        hint.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        hint.setMinWidth(75);
        v.getChildren().addAll(load, reset, hint); //Adds all the buttons after each is initialized
        main.setRight(v); //Sets the vbx to the right of the main pane

        b2 = new BorderPane(); //Creates the main border pane
        sizeBoard(); //Calls method to create the board
        main.setCenter(b2); //Sets the border pane to the center

        stage.setScene(new Scene(main));
        stage.setTitle("Tilt");
        stage.show(); //Shows the window
    }

    /**
     * Used to initialize the board and be able to change the size of the board
     */
    public void sizeBoard() {
        g = new GridPane();
        g.setAlignment(Pos.CENTER);
        makeBoard(); //Calls method to make the board on the grid
        b2.setCenter(g);

        //Creates each directional move button and adds each to its respective spot
        Button n = new Button("^");
        n.setMinSize(460,50);
        n.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        n.setOnAction(event -> model.move('n'));
        b2.setTop(n);
        Button e = new Button(">");
        e.setMinSize(50,350);
        e.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        e.setOnAction(event -> model.move('e'));
        b2.setRight(e);
        Button s = new Button("v");
        s.setMinSize(460, 50);
        s.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        s.setOnAction(event -> model.move('s'));
        b2.setBottom(s);
        Button w = new Button("<");
        w.setMinSize(50,350);
        w.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        w.setOnAction(event -> model.move('w'));
        b2.setLeft(w);
        if(model.getDimension()>5) { //If the board is bigger than the default size, change the size of the directionals
            n.setMinSize(110+model.getDimension()*70,50);
            e.setMinSize(50, model.getDimension()*70);
            s.setMinSize(110+model.getDimension()*70,50);
            w.setMinSize(50, model.getDimension()*70);
        }
    }

    /**
     * Creates the board where the actual game is shown
     */
    public void makeBoard() {
        for(int c=0;c<model.getDimension();c++) {
            for(int r=0;r<model.getDimension();r++) {
                Button b = new Button(); //Creates a button for each spot
                char p = model.getBoard().board[r][c];
                switch (p) { //If the space isnt empty, display the respective object
                    case '*' -> b.setGraphic(new ImageView(block));
                    case 'O' -> b.setGraphic(new ImageView(hole));
                    case 'B' -> b.setGraphic(new ImageView(blueDisk));
                    case 'G' -> b.setGraphic(new ImageView(greenDisk));
                }
                b.setMinSize(70, 70); //Sets size of button
                b.setStyle(empty); //Cleans up the button visuals to have the same background and border
                g.add(b, c, r); //Adds to respective spot on grid
            }
        }
    }

    /**
     * Pulls up a window where the user can seect the file to be loaded
     */
    public void loadFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load a Game Board");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/data/tilt")); //Base directory of tilt game files
        File selectedFile = fileChooser.showOpenDialog(null);
        model.loadFromFile(selectedFile); //Calls model's load file function
    }

    /**
     * Updtes the board based on what the model does and sends to the interface
     * @param tiltModel the object that wishes to inform this object
     *                about something that has happened.
     * @param msg optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(TiltModel tiltModel, String msg) {
        if(model.getBoard().isSolution()) { //If the current config is a solution, dont make a move
            l.setText("You Won!");
            makeBoard();
            return;
        }
        switch (msg) {
            case "Loaded" -> { //When a game is loaded
                l.setText("Loaded: " + model.getFileName());
                sizeBoard(); //Re do the border pane to ensure the size is correct
            }
            case "LoadFailed" -> l.setText("Failed to load: " + model.getFileName()); //If load failed, do nothing
            case "NoHint" -> l.setText("Current Board is unsolvable. Please restart."); //If board is unsolvable, tell the user to restart
            case "Hint" -> { //When a hint is given
                l.setText("Next Step!");
                makeBoard(); //Show the next move
            }
            case "Moved" -> { //When a move is made
                makeBoard(); //Update the board to show the move
                l.setText("");
            }
            case "Invalid" -> l.setText("Invalid move! A slider will fall in or nothing moves."); //When a move is invalid
            case "Reset" -> { //When the board is reset
                l.setText("Board Reset!");
                makeBoard(); //Show the updated board
            }
        }
    }

    /**
     * Main program to launch the appliaction and send in the given file to be loaded
     * @param args the file given to be loaded by the model
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltGUI filename");
        } else {
            Application.launch(args);
        }
    }
}
