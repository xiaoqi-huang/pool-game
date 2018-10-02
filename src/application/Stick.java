package application;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

/**
 * Stick is used in the game for hitting the cue ball.
 */
public class Stick extends Line {

    private double distance = 30;   // Distance to the closest point on the surface of the cue ball
    private double length = 300;    // Length of a Stick

    /**
     * Constructor for Stick
     * The created Stick is always aiming at the cue ball.
     * @param ball This is the cue ball.
     * @param mouseX This is the x-coordination when the mouse is pressed.
     * @param mouseY This is the y-coordination when the mouse is pressed.
     */
    public Stick(Ball ball, double mouseX, double mouseY) {

        double ballX = ball.getCenterX();
        double ballY = ball.getCenterY();
        double radius = ball.getRadius();

        double ratioX = (mouseX - ballX) / Math.hypot(ballX - mouseX, ballY - mouseY);
        double ratioY = (mouseY - ballY) / Math.hypot(ballX - mouseX, ballY - mouseY);

        setStartX(ballX + (radius + distance) * ratioX);
        setStartY(ballY + (radius + distance) * ratioY);

        setEndX(ballX + (radius + distance + length) * ratioX);
        setEndY(ballY + (radius + distance + length) * ratioY);

        setStroke(Color.BEIGE);
        setStrokeWidth(5);
    }

    /**
     * This method can perform the hitting animation when the mouse is released.
     * The duration of the animation is determined by the velocity given to the cue ball.
     * The velocity is larger, the animation is shorter.
     * @param velocity This is the velocity given to the cue ball.
     * @return Timeline
     */
    public Timeline hit(double velocity) {

        double x1 = getStartX();
        double y1 = getStartY();
        double x2 = getEndX();
        double y2 = getEndY();

        double ratioX = (x1 - x2) / Math.hypot(x1 - x2, y1 - y2);
        double ratioY = (y1 - y2) / Math.hypot(x1 - x2, y1 - y2);

        double newX = getLayoutX() + distance * ratioX;
        double newY = getLayoutY() + distance * ratioY;

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.2 - (velocity / 200)),
                        new KeyValue(this.layoutXProperty(), newX),
                        new KeyValue(this.layoutYProperty(), newY)
        ));

        timeline.setCycleCount(1);

        timeline.play();

        return timeline;
    }
}
