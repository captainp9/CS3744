package cs3744.hw3;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Homework 3 view class.
 * Provides the GUI.
 * Base on the homework2 solution provided by the instructor
 *
 * @author Hongyi Zhen
 * @version 1
 */
public class HW3View extends VBox {
    private ObjectProperty<File> file = null;
    private VBox content = null;
    private Canvas canvas = null;
    private GraphicsContext gc = null;
    private Slider slider = null;

    /**
     * Creates an instance of <code>HW3View</code> class.
     */
    public HW3View() {
        MenuBar menuBar = new MenuBar();
        HW3View view = this;
        file = new SimpleObjectProperty<File>();

        Menu menuFile = new Menu("File");
        MenuItem openItem = new MenuItem("Open...");
        MenuItem closeItem = new MenuItem("Close");
        openItem.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        openItem.setOnAction(new EventHandler<ActionEvent>() { // shows the file chooser
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                        new FileChooser.ExtensionFilter("All Files", "*.*"));
                File selectedFile = fileChooser.showOpenDialog(view.getScene().getWindow());
                if (selectedFile != null) {
                    file.set(selectedFile);
                    openItem.setDisable(true);
                    closeItem.setDisable(false);
                }
            }
        });
        closeItem.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
        closeItem.setDisable(true);
        closeItem.setOnAction(new EventHandler<ActionEvent>() { // sets the text focus to the text field.
            @Override
            public void handle(ActionEvent event) {
                file.set(null);
                openItem.setDisable(false);
                closeItem.setDisable(true);
            }
        });
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        exitItem.setOnAction(new EventHandler<ActionEvent>() { // Gracefully closes the application when the window is closed.
            @Override
            public void handle(ActionEvent event) { Platform.exit(); }
        });
        menuFile.getItems().addAll(openItem, closeItem, new SeparatorMenuItem(), exitItem);

        Menu menuHelp = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(new EventHandler<ActionEvent>() { // Shows the information dialog.
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Homework 3 solution.");
                alert.setHeaderText("About Homework 3");
                alert.setTitle("About");
                alert.show();
            }
        });
        aboutItem.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        menuHelp.getItems().add(aboutItem);

        menuBar.getMenus().addAll(menuFile, menuHelp);


        slider = new Slider(0,100,0);

        canvas = new Canvas();
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty().subtract(slider.heightProperty().add(menuBar.heightProperty())));

        gc = canvas.getGraphicsContext2D();
        content = new VBox();
        content.getChildren().addAll(slider, canvas);
        this.getChildren().addAll(menuBar, content);
    }

    /**
     * Draw the background and the grid.
     *
     * @param r The number of rows in the grid.
     * @param c The number of columns in the grid.
     * @param col The background color of the grid.
     * */
    public void drawBackground(int r, int c, Color col) {
        gc.setFill(col);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.strokeRect(0,0, canvas.getWidth(), canvas.getHeight());
        for (int i = 1; i < r; i++) {
            gc.strokeLine(0, i * canvas.getHeight() / r,  canvas.getWidth() - 1 , i * canvas.getHeight() / r);
        }
        for (int i = 1; i < c; i++) {
            gc.strokeLine(i * canvas.getWidth() / c,  0,i * canvas.getWidth() / c, canvas.getHeight() - 1);
        }
    }

    /**
     * Draw a light.
     *
     * @param x The row index of the node.
     * @param y The column index of the node.
     * @param r The number of rows in the grid.
     * @param c The number of columns in the grid.
     * @param col The background color of the grid.
     * */
    public void drawLight(int x, int y, int r, int c, Color col) {
        gc.setFill(col);
        gc.fillRect(y * canvas.getWidth() / c, x * canvas.getHeight() / r, canvas.getWidth() / c, canvas.getHeight() / r);
    }

    /**
     * Gets the file property.
     *
     * @return The file property.
     */
    public ObjectProperty<File> fileProperty() { return file; }

    /**
     * Gets the canvas width property.
     *
     * @return The canvas width property.
     */
    public DoubleProperty canvasWidthProperty() { return canvas.widthProperty(); }

    /**
     * Gets the canvas height property.
     *
     * @return The canvas height property.
     */
    public DoubleProperty canvasHeightProperty() { return canvas.heightProperty(); }

    /**
     * Gets the value of the slider
     *
     * @return The slider's current value.
     */
    public double getSliderValue(){ return slider.getValue();}

    /**
     * Gets the property of the slider
     *
     * @return The slider's property.
     */
    public DoubleProperty getSliderProperty(){ return slider.valueProperty();}
}
