package application;

import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class SidePocketFactory extends PocketFactory {

    @Override
    public ArrayList<Shape> getPockets(double radius, double w, double h) {

        ArrayList<Shape> pockets = new ArrayList<Shape>();

        // Radius of pockets
        double r = 1.6 * radius;

        // Upper pocket, then lower pocket
        pockets.add(new SidePocket(w / 2 - r, 0, w / 2 + r, 0));
        pockets.add(new SidePocket(w / 2 - r, h, w / 2 + r, h));

        return pockets;
    }
}
