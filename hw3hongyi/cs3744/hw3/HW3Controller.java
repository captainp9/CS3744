package cs3744.hw3;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;

/**
 * Homework 3 controller class.
 * Coordinates model and view.
 * Base on the homework2 solution provided by the instructor
 *
 * @author Hongyi Zhen
 * @version 1
 */
public class HW3Controller {
    private HW3Model model = null;
    private HW3View view = null;
    private Stage stage = null;
    private File file = null;
    private static final String DELIMITER = ",";

    /**
     * Creates an instance of <code>HW3Controller</code> class.
     *
     * @param m The model object.
     * @param v The view object.
     * @param s The top level container (used to create a dialog).
     */
    public HW3Controller(HW3Model m, HW3View v, Stage s) {
        model = m;
        view = v;
        stage = s;
        model.nameProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                stage.setTitle("HW3: hongyi" + (newValue.equals("") ? "" : " - " + newValue));
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

        model.getLightMap().addListener(new MapChangeListener<HW3Model.LightKey, Color>() {
            @Override
            public void onChanged(Change<? extends HW3Model.LightKey, ? extends Color> change) {
                draw();
            }
        });
        view.fileProperty().addListener(new ChangeListener<File>() {
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
                    view.getSliderProperty().setValue(0);
                }
            }
        });

        view.canvasHeightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                draw();
            }
        });

        view.canvasWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                draw();
            }
        });

        view.getSliderProperty().addListener(new ChangeListener<Number>() {
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
        view.drawBackground(model.getRowCount(), model.getColumnCount(), model.getBackgroundColor());
        for (HW3Model.LightKey key : model.getLightMap().keySet()) {
            view.drawLight(key.getRow(), key.getColumn(), model.getRowCount(), model.getColumnCount(), model.getLightMap().get(key));
        }
        for (HW3Model.LightKey key : model.getLightMap2().keySet()) {
            if (key.getTime() <= view.getSliderValue())
            {
                view.drawLight(key.getRow(), key.getColumn(), model.getRowCount(), model.getColumnCount(), model.getLightMap2().get(key));
            }
        }
    }

    /**
     * Loads the file.
     *
     * @param f The file.
     */
    public void load(File f) {
        BufferedReader br = null;
        String line = null;
        String[] tokens = null;
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