package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Stick extends Line {

    public Stick(Ball ball, Double x, Double y) {

        Double ballX = ball.getCenterX();
        Double ballY = ball.getCenterY();

        Double radioX = (x - ballX) / Math.sqrt(Math.pow(ballX - x, 2) + Math.pow(ballY - y, 2));
        Double radioY = (y - ballY) / Math.sqrt(Math.pow(ballX - x, 2) + Math.pow(ballY - y, 2));

        setStartX(ballX + 20 * radioX);
        setStartY(ballY + 20 * radioY);
        setEndX(ballX + 320 * radioX);
        setEndY(ballY + 320 * radioY);
        setStroke(Color.BROWN);
        setStrokeWidth(5);
    }
}
