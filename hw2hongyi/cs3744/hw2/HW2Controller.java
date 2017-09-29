package cs3744.hw2;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;

/**
 * HW2 Controller class.
 * Based on the Homework 2 start controller class provided by the instructor
 *
 * @author Hongyi Zhen
 * @version 1
 */

public class HW2Controller {
    private HW2Model model = null;
    private HW2View view = null;
    private Stage stage = null;
    private File file = null;
    private static final String DELIMITER = ",";

    public HW2Controller(HW2Model m, HW2View v, Stage s) {
        model = m;
        view = v;
        stage = s;
        model.nameProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) { stage.setTitle("HW2: hongyi" + (newValue.equals("") ? "" : " - " + newValue)); }
        });
        view.fileProperty().addListener(new ChangeListener<File>() {
            @Override
            public void changed(ObservableValue<? extends File> observable, File oldValue, File newValue) {
                if (newValue != null) {
                    file = newValue;
                    load(file);
                    draw();
                }
                else {
                    save(file);

                }
            }
        });
        view.pointProperty().addListener(new ChangeListener<Point2D>() {
            @Override
            public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
                if (view.getLightButton().isSelected()) {
                    int xc = (int) (newValue.getX() / view.getCanvasWidth() * model.getColumnCount());
                    int yc = (int) (newValue.getY() / view.getCanvasHeight() * model.getRowCount());
                    HW2Model.LightCell newCell = new HW2Model.LightCell(xc, yc, view.getCpl());
                    String newKey = Integer.toString(xc) + ',' + Integer.toString(yc);

                    if (model.getLightsMap().containsKey(newKey)) {
                        HW2Model.LightCell oldCell = model.getLightsMap().get(newKey);
                        model.getLightsMap().replace(newKey, oldCell, newCell);
                    } else {
                        model.getLightsMap().put(newKey, newCell);
                    }
                    draw();
                }

            }
        });
        view.canvasHeightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) { draw(); }
        });
        view.canvasWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) { draw(); }
        });

        view.getCpbProperty().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (view.getBackgroundButton().isSelected())
                {
                    draw();
                }
            }
        });
    }

    /**
     * This is the draw method that refresh the canvas
     */
    public void draw() {
        view.drawBackground(model.getRowCount(), model.getColumnCount(), view.getCpb());
        for(int i = 0; i < model.getColumnCount(); i++)
        {
            for(int j = 0; j < model.getRowCount(); j++)
            {
                String tkey = Integer.toString(i) + ','+Integer.toString(j);
                HW2Model.LightCell temp = model.getLightsMap().get(tkey);
                if(temp != null)
                {
                    view.drawNode(temp.getXcoord()*view.getCanvasWidth()/model.getRowCount(),
                            temp.getYcoord()*view.getCanvasHeight()/model.getColumnCount(),
                            model.getRowCount(),model.getColumnCount(),temp.getColor());
                }
            }
        }

    }

    /**
     * This is the close method that setting up everything back to initial after saving.
     */
    public void close() {
        view.setCpb(Color.BLACK);
        view.setCpl(Color.WHITE);
        model.setColumnCount(0);
        model.setColumnCount(0);
        model.setName("");
        model.getLightsMap().clear();
        draw();
    }

    /**
     * This is the save method that save the light configuration that made by the user.
     * @param f the file edited.
     */

    public void save(File f) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(f));
            bw.write(model.getName());
        // Save the reminder of the model data
            bw.newLine();
            String size = Integer.toString(model.getRowCount()) + ',' + Integer.toString(model.getColumnCount());
            bw.write(size);
            bw.newLine();
            String backgroundColor = Double.toString(view.getCpb().getRed()*100) +','+Double.toString(view.getCpb().getGreen()*100) + ','
                    + Double.toString(view.getCpb().getBlue()*100) + ',' + Double.toString(view.getCpb().getOpacity()*100);
            bw.write(backgroundColor);
            bw.newLine();
            for(int i = 0 ; i < model.getColumnCount(); i++)
            {
                for(int j = 0; j < model.getRowCount(); j++)
                {
                    String key = Integer.toString(i)+','+Integer.toString(j);
                    if(model.getLightsMap().containsKey(key))
                    {
                        HW2Model.LightCell lightCell = model.getLightsMap().get(key);
                        String line = Integer.toString(lightCell.getYcoord()) + ',' + Integer.toString(lightCell.getXcoord())+','
                                +Double.toString(lightCell.getColor().getRed()*100)+','+Double.toString(lightCell.getColor().getGreen()*100)+','
                                +Double.toString(lightCell.getColor().getBlue()*100)+','+Double.toString(lightCell.getColor().getOpacity()*100);
                        bw.write(line);
                        bw.newLine();
                    }
                }
            }
            close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This is the load method that load the light configuration from the .csv file.
     * @param f
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
            // the number of row is Integer.parseInt(tokens[0])
            // the number of clumns is Integer.parseInt(tokens[1])
            model.setRowCount(Integer.parseInt(tokens[0]));
            model.setColumnCount(Integer.parseInt(tokens[1]));

            line = br.readLine();
            tokens = line.split(DELIMITER);
            // the red component is Double.parseDouble(tokens[0])
            // the green component is Double.parseDouble(tokens[1])
            // the blue component is Double.parseDouble(tokens[2])
            // The opacity component is Double.parseDouble(tokens[3]));

            model.setBackgroundColor(new Color(Double.parseDouble(tokens[0])/100, Double.parseDouble(tokens[1])/100, Double.parseDouble(tokens[2])/100, Double.parseDouble(tokens[3])/100));

            // Read the lights
            while ((line = br.readLine()) != null) {
                tokens = line.split(DELIMITER);
            // The light row index is Integer.parseInt(tokens[0])
            // The column row index is Integer.parseInt(tokens[1])
            // The light red component is Double.parseDouble(tokens[2])
            // The light green compoent is Double.parseDouble(tokens[3])
            // The light blue component is Double.parseDouble(tokens[4])
            // The light opacity component is Double.parseDouble(tokens[5]));
                String key = tokens[1]+','+tokens[0];
                HW2Model.LightCell cell = new HW2Model.LightCell(Integer.parseInt(tokens[1]),Integer.parseInt(tokens[0]),
                        new Color(Double.parseDouble(tokens[2])/100, Double.parseDouble(tokens[3])/100, Double.parseDouble(tokens[4])/100,
                                Double.parseDouble(tokens[5])/100));
                model.getLightsMap().put(key, cell);
            }
            view.setCpb(model.getBackgroundColor());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
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