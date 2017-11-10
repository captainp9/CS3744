package cs3744.hw4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Homework 4 main class.
 * Base on the homework 2 solution provided by the instructor
 *
 * @author Hongyi Zhen
 * @version 1
 */
public class HW4 extends Application {

    /**
     * Creates GUI and shows the application window.
     *
     * @param stage Top level container.
     */
    public void start(Stage stage) {
        FXMLLoader loader = null;
        HW4Model model = new HW4Model();
        HW4Controller controller = null;
        Scene scene = null;
        Parent view = null;
        try {
            loader = new FXMLLoader(getClass().getResource("HW4.fxml"));
            view = loader.load();
            controller = loader.getController();
            controller.init(model,stage);
            scene = new Scene(view, 800, 600);
            stage.getIcons().add(new Image(getClass().getResource("hw4title.png").toExternalForm()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        stage.setTitle("HW4: hongyi");
        stage.setScene(scene);


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