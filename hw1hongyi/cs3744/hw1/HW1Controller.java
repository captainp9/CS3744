package cs3744.hw1;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;

/**
 * Homework 1 controller class.
 * Based on the Homework 1 start controller class provided by the instructor.
 * Coordinates model and view.
 *
 * @author Hongyi Zhen
 * @version 1
 */
public class HW1Controller {
    private HW1Model model = null;
    private HW1View view = null;

    /**
     * A class that describes a change listener for the model's color property.
     */
    private class ModelListener implements ChangeListener<Color> {
        @Override
        public void changed(ObservableValue<? extends Color> o, Color oldVal, Color newVal) {
            view.setColor(newVal, model.getRed(), model.getGreen(), model.getBlue());
        }
    };

    /**
     * A class that describes a change listener for the view's red property.
     */
    private class RedListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> o, Number oldVal, Number newVal) {
            model.setRed(newVal.doubleValue());
        }
    }

    /**
     * A class that describes a change listener for the view's green property.
     */
    private class GreenListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> o, Number oldVal, Number newVal) {
            model.setGreen(newVal.doubleValue());
        }
    };

    /**
     * A class that describes a change listener for the view's blue property.
     */
    private class BlueListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> o, Number oldVal, Number newVal) {
            model.setBlue(newVal.doubleValue());
        }
    };

    /**
     * Creates an instance of <code>HW1Controller</code> class.
     *
     * @param m The model object.
     * @param v The view object.
     */
    public HW1Controller(HW1Model m, HW1View v, double red, double green, double blue) {
        model = m;
        view = v;
        ModelListener modelListener = new ModelListener();
        RedListener redListener = new RedListener();
        GreenListener greenListener = new GreenListener();
        BlueListener blueListener = new BlueListener();

        model.colorProperty().addListener(modelListener);
        view.redProperty().addListener(redListener);
        view.greenProperty().addListener(greenListener);
        view.blueProperty().addListener(blueListener);
        view.setRed(red);
        view.setGreen(green);
        view.setBlue(blue);
    }

}
