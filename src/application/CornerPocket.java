package application;

import javafx.scene.shape.Polyline;

public class CornerPocket extends Polyline implements Pocket {

    public CornerPocket(double x1, double y1, double x2, double y2, double x3, double y3) {

        super();

        getPoints().addAll(new Double[] {
                x1, y1,
                x2, y2,
                x3, y3
        });

        setStrokeWidth(5.0);
    }
}
