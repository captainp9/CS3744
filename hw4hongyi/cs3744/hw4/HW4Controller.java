package cs3744.hw4;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.*;

/**
 * Homework 4 controller class.
 * Coordinates model and view.
 * Base on the homework2 solution provided by the instructor
 *
 * @author Hongyi Zhen
 * @version 1
 */
public class HW4Controller {
    private HW4Model model = null;
    private Parent root = null;
    private Stage stage = null;
    private File file = null;
    private static final String DELIMITER = ",";
    private ObjectProperty<File> fileProperty = null;
    public MenuItem aboutItem = null;
    public MenuItem openItem = null;
    public MenuItem closeItem = null;
    public MenuItem exitItem = null;
    public MenuBar menuBar = null;
    public Slider slider = null;
    public GraphicsContext gc = null;
    public Canvas canvas = null;

    /**
     * Creates an instance of <code>HW4Controller</code> class.
     *
     * @param m The model object.
     * @param s The top level container (used to create a dialog).
     */
    public void init(HW4Model m, Stage s) {
        model = m;
        stage = s;
        fileProperty = new SimpleObjectProperty<>();
        gc = canvas.getGraphicsContext2D();
        canvas.widthProperty().bind(s.widthProperty());
        canvas.heightProperty().bind(s.heightProperty().subtract(slider.heightProperty().add(menuBar.heightProperty())));


        model.nameProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                stage.setTitle("HW4: hongyi" + (newValue.equals("") ? "" : " - " + newValue));
            }
        });
        model.rowCountProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                draw();
            }
        });
        model.columnCountProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                draw();
            }
        });

        model.getLightMap().addListener(new MapChangeListener<HW4Model.LightKey, Color>() {
            @Override
            public void onChanged(Change<? extends HW4Model.LightKey, ? extends Color> change) {
                draw();
            }
        });
        fileProperty.addListener(new ChangeListener<File>() {
            @Override
            public void changed(ObservableValue<? extends File> observable, File oldValue, File newValue) {
                if (newValue != null) {
                    file = newValue;
                    load(file);
                    draw();
                }
                else
                {
                    model.reset();
                    slider.setValue(0);
                }
            }
        });

        canvas.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                draw();
            }
        });

        canvas.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                draw();
            }
        });

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                draw();
            }
        });

    }

    /**
     * Updates the view (draws the canvas).
     */
    public void draw() {
        drawBackground(model.getRowCount(), model.getColumnCount(), model.getBackgroundColor());
        for (HW4Model.LightKey key : model.getLightMap().keySet()) {
            drawLight(key.getRow(), key.getColumn(), model.getRowCount(), model.getColumnCount(), model.getLightMap().get(key));
        }
        for (HW4Model.LightKey key : model.getLightMap2().keySet()) {
            if (key.getTime() <= slider.getValue())
            {
                drawLight(key.getRow(), key.getColumn(), model.getRowCount(), model.getColumnCount(), model.getLightMap2().get(key));
            }
        }
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
     * Function for the about item
     * @param event
     */
    public void handleAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Homework 4 solution.");
        alert.setHeaderText("About Homework 4");
        alert.setTitle("About");
        alert.show();
    }

    /**
     * Function for exit item
     * @param event
     */
    public void handleExit(ActionEvent event) { Platform.exit(); }

    /**
     * Function for close item
     * @param event
     */
    public void handleClose(ActionEvent event) {
        fileProperty.set(null);
        openItem.setDisable(false);
        closeItem.setDisable(true);
    }

    /**
     * Function for open item
     * @param event
     */
    public void handleOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(stage.getScene().getWindow());
        if (selectedFile != null) {
            fileProperty.set(selectedFile);
            openItem.setDisable(true);
            closeItem.setDisable(false);
        }
    }

    /**
     * Loads the file.
     *
     * @param f The file.
     */
    public void load(File f) {
        BufferedReader br = null;
        String line;
        String[] tokens;
        try {
            br = new BufferedReader(new FileReader(f));
            line = br.readLine();
            model.setName(line);
            line = br.readLine();
            tokens = line.split(DELIMITER);
            model.setRowCount(Integer.parseInt(tokens[0]));
            model.setColumnCount(Integer.parseInt(tokens[1]));
            line = br.readLine();
            tokens = line.split(DELIMITER);
            model.setBackgroundColor(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]));
            while ((line = br.readLine()) != null) {
                tokens = line.split(DELIMITER);

                if (tokens.length == 6)
                {
                    model.addLight(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), 0, Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4]), Double.parseDouble(tokens[5]));
                }
                else
                {
                    model.addLight2(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Double.parseDouble(tokens[6]), Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4]), Double.parseDouble(tokens[5]));

                }
            }
            model.getBackgroundColor();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}