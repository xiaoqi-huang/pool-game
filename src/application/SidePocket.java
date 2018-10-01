package application;

import javafx.scene.shape.Line;

public class SidePocket extends Line implements Pocket {

    public SidePocket(double x1, double y1, double x2, double y2) {

        super();

        setStartX(x1);
        setStartY(y1);
        setEndX(x2);
        setEndY(y2);

        setStrokeWidth(4.0);
    }
}
