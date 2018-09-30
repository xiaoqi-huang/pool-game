package application;

import java.util.ArrayList;

public class BallBuilder implements Builder {

    private ArrayList<Ball> balls = new ArrayList<Ball>();

    public void createBall(BallData data, double radius) {

        Ball ball = new Ball(data.getColour(), data.getPositionX(), data.getPositionY(), radius, data.getVelocityX(), data.getVelocityY(), data.getMass());

        balls.add(ball);
    }

    public ArrayList<Ball> getResult() {
        return balls;
    }

}
