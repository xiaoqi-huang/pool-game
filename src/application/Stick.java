package application;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.util.Pair;

public class Stick extends Line {

    public Stick(Ball ball, Double x, Double y) {

        double ballX = ball.getCenterX();
        double ballY = ball.getCenterY();

        double radioX = (x - ballX) / Math.sqrt(Math.pow(ballX - x, 2) + Math.pow(ballY - y, 2));
        double radioY = (y - ballY) / Math.sqrt(Math.pow(ballX - x, 2) + Math.pow(ballY - y, 2));

        setStartX(ballX + 40 * radioX);
        setStartY(ballY + 40 * radioY);
        setEndX(ballX + 340 * radioX);
        setEndY(ballY + 340 * radioY);
        setStroke(Color.BEIGE);
        setStrokeWidth(5);
    }

    public Timeline hit() {

        double x1 = getStartX();
        double y1 = getStartY();
        double x2 = getEndX();
        double y2 = getEndY();

        double radioX = (x2 - x1) / Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        double radioY = (y2 - y1) / Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));

        double newX = getLayoutX() - 30 * radioX;
        double newY = getLayoutY() - 30 * radioY;

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.1),
                        new KeyValue(this.layoutXProperty(), newX),
                        new KeyValue(this.layoutYProperty(), newY)
        ));

        timeline.setCycleCount(1);

        timeline.play();

        return timeline;
    }
}
