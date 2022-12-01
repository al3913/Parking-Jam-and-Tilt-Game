package puzzles.tilt.gui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import puzzles.common.Observer;
import puzzles.tilt.model.TiltConfig;
import puzzles.tilt.model.TiltModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class TiltGUI extends Application implements Observer<TiltModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    private TiltModel model;

    // for demonstration purposes
    private final Image greenDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green.png"));
    private final Image block = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"block.png"));
    private final Image hole = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"hole.png"));
    private final Image blueDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"blue.png"));

    public void init() {
        String filename = getParameters().getRaw().get(0);
        this.model = new TiltModel(filename);
        model.addObserver(this);
        System.out.println("init:   Initialize and connect to model!");
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane main = new BorderPane();

        Label l = new Label("Loaded: " + model.getFileName());
        main.setTop(l);



        stage.setScene(new Scene(main));
        stage.setTitle("Tilt");
        stage.show();
    }

    @Override
    public void update(TiltModel tiltModel, String message) {
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltGUI filename");
        } else {
            Application.launch(args);
        }
    }
}
