package application;

import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class CornerPocketFactory extends PocketFactory {

    @Override
    public ArrayList<Shape> getPockets(double radius, double w, double h) {

        ArrayList<Shape> pockets = new ArrayList<Shape>();

        // Radius of pockets
        double r = (1.6 * 2 * radius) / Math.sqrt(2);

        // Create pockets clockwise
        pockets.add(new CornerPocket(0, r, 0, 0, r, 0));
        pockets.add(new CornerPocket(w - r, 0, w, 0, w, r));
        pockets.add(new CornerPocket(w, h - r, w, h, w - r, h));
        pockets.add(new CornerPocket(r, h, 0, h, 0, h - r));

        return pockets;
    }
}
