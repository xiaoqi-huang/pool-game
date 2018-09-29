package application;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Builder {

    private ArrayList<Ball> balls = new ArrayList<Ball>();

    public void createBall(BallData data) {

        Circle circle = new Circle();
        circle.setCenterX(data.getPositionX());
        circle.setCenterY(data.getPositionY());
        circle.setRadius(5);
        circle.setFill(Paint.valueOf(data.getColour()));

        Ball ball = new Ball(circle, data.getVelocityX(), data.getVelocityY(), data.getMass());
        balls.add(ball);
    }

    public ArrayList<Ball> getResult() {
        return balls;
    }
}
