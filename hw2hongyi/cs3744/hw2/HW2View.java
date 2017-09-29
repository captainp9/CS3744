package cs3744.hw2;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * HW2 view class.
 * Provides the GUI.
 * Based on the Homework 2 start view class provided by the instructor
 *
 * @author Hongyi Zhen
 * @version 1
 */

public class HW2View extends VBox {
    private VBox content = null;
    private Canvas canvas = null;
    GraphicsContext gc = null;
    private Image lightImage = null;
    private Image backgroundImage = null;
    private ImageCursor lightCursor = null;
    private ImageCursor backgroundCursor = null;
    private ObjectProperty<File> file = null;
    private ObjectProperty<Point2D> point = null;
    private RadioButton lightButton = null;
    private RadioButton backgroundButton = null;
    private ColorPicker cpl = null;
    private ColorPicker cpb = null;
    /**
     * Creates an instance of <code>HW2View</code> class.
     */
    public HW2View() {
        MenuBar menuBar = new MenuBar();
        HW2View view = this;
        file = new SimpleObjectProperty<File>();
        point = new SimpleObjectProperty<Point2D>();

        lightImage = new Image(getClass().getResource("light.png").toExternalForm());
        backgroundImage = new Image(getClass().getResource("background.png").toExternalForm());
        backgroundCursor = new ImageCursor(backgroundImage,5,30);
        lightCursor = new ImageCursor(lightImage, 5, 30);
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
            // Save file, enable Open and disable Close menu item.
                file.set(null);
                openItem.setDisable(false);
                closeItem.setDisable(true);
            }
        });
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        exitItem.setOnAction(new EventHandler<ActionEvent>() { // Gracefully closes the application when the window is closed.
            @Override
            public void handle(ActionEvent event) {
                // Exits
                Platform.exit();
            }
        });
        menuFile.getItems().addAll(openItem, closeItem, new SeparatorMenuItem(), exitItem);

        Menu menuHelp = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(new EventHandler<ActionEvent>() { // Shows the information dialog.
            @Override
            public void handle(ActionEvent event) {
            //  Create Alert dialog.
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("About");
                alert.setHeaderText("About Homework 2");
                alert.setContentText("Homework 2 solution");
                alert.show();
            }
        });
        aboutItem.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        menuHelp.getItems().add(aboutItem);

        menuBar.getMenus().addAll(menuFile, menuHelp);

        lightButton = new RadioButton("Light");
        backgroundButton = new RadioButton("Background");
        lightButton.setGraphic(new ImageView(lightImage));
        backgroundButton.setGraphic(new ImageView(backgroundImage));

        lightButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                backgroundButton.setSelected(false);
                lightButton.setSelected(true);
                canvas.setCursor(lightCursor);
            }
        });
        backgroundButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                backgroundButton.setSelected(true);
                lightButton.setSelected(false);
                canvas.setCursor(backgroundCursor);
            }
        });

        cpl = new ColorPicker();
        cpb = new ColorPicker(Color.BLACK);
        ToolBar toolbar = new ToolBar(lightButton, cpl, new Separator(),backgroundButton,cpb);


        canvas = new Canvas();
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty().subtract(toolbar.heightProperty().add(menuBar.heightProperty())));
        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) { point.setValue(new Point2D(e.getX(), e.getY())); }
        });
        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) { point.setValue(new Point2D(e.getX(), e.getY())); }
        });
        gc = canvas.getGraphicsContext2D();
        content = new VBox();
        content.getChildren().addAll(toolbar, canvas);
        this.getChildren().addAll(menuBar, content);
        lightButton.fire();
    }

    /**
     * This is the drawBackground method that refresh the background.
     * @param r row count
     * @param c column count
     * @param col the background color
     */
    public void drawBackground(double r, double c, Color col) {

        gc.setFill(col);
        gc.fillRect(0,0,getCanvasWidth(),getCanvasHeight());

        for(int i = 1; i < r; i++)
        {
            gc.strokeLine(0,getCanvasHeight()/r*i,getCanvasWidth(),getCanvasHeight()/r*i);
        }

        for(int j = 1; j < c; j++)
        {
            gc.strokeLine(getCanvasWidth()/c*j,0,getCanvasWidth()/c*j,getCanvasHeight());
        }
    }

    /**
     * This is the drawNode method that draws a single rectangle.
     * @param x upperleft x coord
     * @param y upperleft y coord
     * @param r row count
     * @param c column count
     * @param col the node's color
     */
    public void drawNode(double x, double y, double r, double c, Color col) {
        gc.setFill(col);
        gc.fillRect(x,y,getCanvasWidth()/c,getCanvasHeight()/r);
    }


    public ObjectProperty<File> fileProperty() { return file; }

    public ObjectProperty<Point2D> pointProperty() { return point; }

    public double getCanvasWidth() { return canvasWidthProperty().get(); }

    public DoubleProperty canvasWidthProperty() { return canvas.widthProperty(); }

    public double getCanvasHeight() { return canvasHeightProperty().get(); }

    public DoubleProperty canvasHeightProperty() { return canvas.heightProperty(); }

    /**
     * Get the light color picked by the user
     * @return the color
     */
    public Color getCpl() {return cpl.getValue();}

    /**
     * Get the background color picked by the user
     * @return the color
     */
    public Color getCpb() {return cpb.getValue();}

    /**
     * Set the background color
     * @param c the color
     */
    public void setCpb(Color c) {this.cpb.setValue(c);}

    /**
     * Set the light color
     * @param c the color
     */
    public void setCpl(Color c) {this.cpl.setValue(c);}

    /**
     * return the background color picker
     * @return the color picker
     */
    public ColorPicker getCpbProperty() {return this.cpb; }

    /**
     * Get the light color button
     * @return the button
     */
    public RadioButton getLightButton() { return lightButton; }

    /**
     * Get the background color button
     * @return the button
     */
    public RadioButton getBackgroundButton() { return backgroundButton; }
}
