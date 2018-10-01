package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Table in the Pool Game
 */
public class Table extends Rectangle {

    private double friction;

    public Table(Color color, double width, double height, double friction) {

        super(width, height, color);

        this.friction = friction;
    }

    public void setFriction(double friction) { this.friction = friction; }

    public double getFriction() { return friction; }
}
