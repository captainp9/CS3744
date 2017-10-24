package cs3744.hw3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Homework 3 main class.
 * Base on the homework 2 solution provided by the instructor
 *
 * @author Hongyi Zhen
 * @version 1
 */
public class HW3 extends Application {

    /**
     * Creates GUI and shows the application window.
     *
     * @param stage Top level container.
     */
    public void start(Stage stage) {
        HW3Model model = new HW3Model();
        HW3View view = new HW3View();
        Scene scene = new Scene(view, 800, 600);

        try {
            scene.getStylesheets().add(getClass().getResource("hw3.css").toExternalForm());
            stage.getIcons().add(new Image(getClass().getResource("hw3title.png").toExternalForm()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        stage.setTitle("HW3: hongyi");
        stage.setScene(scene);
        HW3Controller controller = new HW3Controller(model, view, stage);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) { Platform.exit(); }
        });
        stage.show();
    }

    /**
     * Invokes the program from the command line.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

}