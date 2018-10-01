package application;

import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * Concrete factory that produces only pockets at corners of a Table
 */
public class CornerPocketFactory extends PocketFactory {

    /**
     * This produces four corner pockets.
     * The radius of pockets is determined by the radius of the ball.
     * @param radius This is the radius of balls.
     * @param width This is the width of the Table.
     * @param height This is the height of the Table.
     * @return ArrayList<Shape> of four corner pockets
     */
    @Override
    public ArrayList<Shape> getPockets(double radius, double width, double height) {

        ArrayList<Shape> pockets = new ArrayList<>();

        // Radius of pockets
        double r = (1.6 * 2 * radius) / Math.sqrt(2);

        // Create pockets clockwise
        pockets.add(new CornerPocket(0, r, 0, 0, r, 0));
        pockets.add(new CornerPocket(width - r, 0, width, 0, width, r));
        pockets.add(new CornerPocket(width, height - r, width, height, width - r, height));
        pockets.add(new CornerPocket(r, height, 0, height, 0, height - r));

        return pockets;
    }
}
