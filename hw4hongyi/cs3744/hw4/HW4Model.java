package cs3744.hw4;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;

import java.util.HashMap;

/**
 * Homework 4 model class.
 * Stores the light grid data.
 * Base on the homework2 solution provided by the instructor
 *
 * @author Hongyi Zhen
 * @version 1
 */
public class HW4Model {
    private StringProperty name = null;
    private IntegerProperty rowCount = null;
    private IntegerProperty columnCount = null;
    private ObjectProperty<Color> backgroundColor = null;
    private ObservableMap<LightKey,Color> lightMap = null;
    private ObservableMap<LightKey,Color> lightMap2 = null;

    /**
     * A class that describes a light key in the observable map of lights.
     */
    public class LightKey {
        private int row = 0;
        private int column = 0;
        private double time = 0;

        /**
         * Creates an instance of <code>LightKey</code> class.
         */
        public LightKey(int r, int c, double t) {
            row = r;
            column = c;
            time = t;
        }

        /**
         * Gets the row index of the light.
         *
         * @return The row index.
         */
        public int getRow() { return row; }

        /**
         * Gets the column index of the light.
         *
         * @return The column index.
         */
        public int getColumn() { return column; }

        /**
         * Gets the time of the light
         * @return The time value
         */
        public double getTime() { return time; }

        @Override
        public boolean equals(Object e) { return (row == ((LightKey)e).row && column == ((LightKey)e).column); }

        @Override
        public int hashCode() {
            return (int)(45000 * row + column + 12345*time);
        }
    }

    /**
     * Creates an instance of <code>HW4Model</code> class using default components values.
     */
    public HW4Model() {
        name = new SimpleStringProperty();
        rowCount = new SimpleIntegerProperty();
        columnCount = new SimpleIntegerProperty();
        backgroundColor = new SimpleObjectProperty<Color>();
        lightMap = FXCollections.observableMap(new HashMap<LightKey, Color>());
        lightMap2 = FXCollections.observableMap(new HashMap<LightKey, Color>());
    }

    /**
     * Resets the model to the initial state (after invoking constructor).
     */
    public void reset() {
        setName("");
        setRowCount(0);
        setColumnCount(0);
        setBackgroundColor(Color.BLACK);
        lightMap.clear();
        lightMap2.clear();
    }

    /**
     * Sets the background color.
     *
     * @param r The background color red component percentage.
     * @param g The background color green component percentage.
     * @param b The background color blue component percentage.
     * @param o The background color opacity component percentage.
     */
    public void setBackgroundColor(double r, double g, double b, double o) { setBackgroundColor(convertColor(r, g, b, o)); }

    /**
     * Adds the light to map1.
     *
     * @param row The row index.
     * @param col The column index.
     * @param r The light color red component percentage.
     * @param g The light color green component percentage.
     * @param b The light color blue component percentage.
     * @param o The light color opacity component percentage.
     */
    public void addLight(int row, int col, double time ,double r, double g, double b, double o) { lightMap.put(new LightKey(row, col, time), convertColor(r, g, b, o)); }

    /**
     * Adds the light to map2.
     *
     * @param row The row index.
     * @param col The column index.
     * @param r The light color red component percentage.
     * @param g The light color green component percentage.
     * @param b The light color blue component percentage.
     * @param o The light color opacity component percentage.
     */
    public void addLight2(int row, int col, double time ,double r, double g, double b, double o) { lightMap2.put(new LightKey(row, col, time), convertColor(r, g, b, o)); }

    /**
     * Convert the background color componnets percentages to a <code>Color</code> object.
     *
     * @param r The background color red component percentage.
     * @param g The background color green component percentage.
     * @param b The background color blue component percentage.
     * @param o The background color opacity component percentage.
     * @return The resulting <code>Color</code> object.
     */
    private Color convertColor(double r, double g, double b, double o) { return new Color(r/100, g/100, b/100, o/100); }

    /**
     * Sets the name.
     *
     * @param n The name.
     */
    public void setName(String n) { name.set(n); }

    /**
     * Gets the name property.
     *
     * @return The name property.
     */
    public StringProperty nameProperty() { return name; }


    /**
     * Gets the number of rows in the grid.
     *
     * @return The number of rows.
     */
    public int getRowCount() { return rowCount.get(); }

    /**
     * Sets the number of rpws in the grid.
     *
     * @param r The number of rows.
     */
    public void setRowCount(int r) { rowCount.set(r); }

    /**
     * Gets the number of rows in the grid property.
     *
     * @return The number of rows in the grid property.
     */
    public IntegerProperty rowCountProperty() { return rowCount; }


    /**
     * Gets the number of columns in the grid.
     *
     * @return The number of columns.
     */
    public int getColumnCount() { return columnCount.get(); }

    /**
     * Sets the number of columns in the grid.
     *
     * @param c The number of columns.
     */
    public void setColumnCount(int c) { columnCount.set(c); }

    /**
     * Gets the number of columns in the grid property.
     *
     * @return The number of columns in the grid property.
     */
    public IntegerProperty columnCountProperty() { return columnCount; }

    /**
     * Sets the background color.
     *
     * @param c The background color.
     */
    public void setBackgroundColor(Color c) { backgroundColor.set(c); }

    /**
     * Gets the background color.
     *
     * @return The background color.
     */
    public Color getBackgroundColor() { return backgroundColor.get(); }

    /**
     * Gets the lights.
     *
     * @return The light map.
     */
    public ObservableMap<LightKey, Color> getLightMap() { return lightMap; }

    /**
     * Gets the lights.
     *
     * @return The light map2.
     */
    public ObservableMap<LightKey, Color> getLightMap2() { return lightMap2; }

}
