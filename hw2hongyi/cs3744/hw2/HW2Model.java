package cs3744.hw2;

import javafx.beans.property.*;
import javafx.scene.paint.Color;

import java.util.HashMap;

/**
 * Homework 2 Model class.
 * Based on the Homework 2 start model class provided by the instructor
 *
 * @author Hongyi Zhen
 * @version 1
 */

public class HW2Model {
    private StringProperty name = null;
    private IntegerProperty rowCount = null;
    private IntegerProperty columnCount = null;
    private ObjectProperty<Color> backgroundColor = null;
    // list of lights
    private HashMap<String, LightCell> lightsMap;

    /**
     * This is a private class to represent the light cell
     */
    public static class LightCell{
        private int xcoord;
        private int ycoord;
        private Color color;

        public LightCell(int x, int y, Color c){
            xcoord = x;
            ycoord = y;
            color = c;
        }

        /**
         * The cell's x coord
         * @return x coord
         */
        public int getXcoord(){ return xcoord;}

        /**
         * The cell's y coord
         * @return y coord
         */
        public int getYcoord(){ return ycoord;}

        /**
         * The color of the cell
         * @return the color
         */
        public Color getColor(){return color;}
    }

    /**
     * Constructor
     */
    public HW2Model() {
        name = new SimpleStringProperty();
        rowCount = new SimpleIntegerProperty();
        columnCount = new SimpleIntegerProperty();
        backgroundColor = new SimpleObjectProperty<Color>();
    //       initialize light list
        lightsMap = new HashMap<>();
    }

    public HashMap<String, LightCell> getLightsMap() { return lightsMap; }

    public String getName() { return name.get(); }

    public void setName(String n) { name.set(n); }

    public StringProperty nameProperty() { return name; }


    public int getRowCount() { return rowCount.get(); }

    public void setRowCount(int r) { rowCount.set(r); }

    public IntegerProperty rowCountProperty() { return rowCount; }


    public int getColumnCount() { return columnCount.get(); }

    public void setColumnCount(int c) { columnCount.set(c); }

    public IntegerProperty columnCountProperty() { return columnCount; }


    public void setBackgroundColor(Color dc) { backgroundColor.set(dc); }

    public Color getBackgroundColor() { return backgroundColor.get(); }

    public ObjectProperty<Color> backgroundColorProperty() { return backgroundColor; }
}