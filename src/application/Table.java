package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Table extends Rectangle {

    private double friction;

    public Table(Color color, double width, double height, double friction) {

        super(width, height, color);

        this.friction = friction;
    }

    public double getFriction() {
        return friction;
    }
}
