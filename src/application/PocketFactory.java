package application;

import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * Abstract factory that can produced two types of pocket factory
 */
public abstract class PocketFactory {

    /**
     * This returns a concrete pocket factory according to the type asked.
     * @param type This is the type of pockets required.
     * @return PocketFactory of specified type
     */
    public static PocketFactory getFactory(PocketType type) {

        PocketFactory factory = null;

        switch (type) {
            case CORNER:
                factory = new CornerPocketFactory();
                break;
            case SIDE:
                factory = new SidePocketFactory();
                break;
        }

        return factory;
    }

    public abstract ArrayList<Shape> getPockets(double radius, double w, double h);
}
