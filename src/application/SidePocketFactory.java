package application;

import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * Concrete factory that produces only pockets on longer sides of a Table
 */
public class SidePocketFactory extends PocketFactory {

    /**
     * This produces two side pockets.
     * One is the side pocket on the upper side, another is the side pocket on the lower side.
     * The radius of pockets is determined by the radius of the ball.
     * @param radius This is the radius of balls.
     * @param width This is the width of the Table.
     * @param height This is the height of the Table.
     * @return ArrayList<Shape> of two side pockets
     */
    @Override
    public ArrayList<Shape> getPockets(double radius, double width, double height) {

        ArrayList<Shape> pockets = new ArrayList<>();

        // Radius of pockets
        double r = 1.6 * radius;

        // Upper pocket, then lower pocket
        pockets.add(new SidePocket(width / 2 - r, 0, width / 2 + r, 0));
        pockets.add(new SidePocket(width / 2 - r, height, width / 2 + r, height));

        return pockets;
    }
}
