package application;

import javafx.scene.shape.Shape;

import java.util.ArrayList;

public abstract class PocketFactory {

    public static PocketFactory getFactory(String type) {

        PocketFactory factory = null;

        if (type.equals("Corner")) {
            factory = new CornerPocketFactory();
        } else if (type.equals("Side")) {
            factory = new SidePocketFactory();
        }

        return factory;
    }

    public abstract ArrayList<Shape> getPockets(double radius, double w, double h);
}
