package application;

import java.util.ArrayList;

public class Builder {

    private ArrayList<Ball> balls = new ArrayList<Ball>();

    public void createBall(BallData data) {
        Ball ball = new Ball(data.getColour(), data.getPositionX(), data.getPositionY(),10.0, data.getVelocityX(), data.getVelocityY(), data.getMass());
        balls.add(ball);
    }

    public ArrayList<Ball> getResult() {
        return balls;
    }
}
