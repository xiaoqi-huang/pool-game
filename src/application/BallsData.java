package application;

import java.util.ArrayList;

public class BallsData implements Data {

    ArrayList<BallData> balls = new ArrayList<BallData>();

    public void addBall(String colour, Double positionX, Double positionY, Double velocityX, Double velocityY, Double mass) {
        BallData ball = new BallData(colour, positionX, positionY, velocityX, velocityY, mass);
        balls.add(ball);
    }

    public ArrayList<BallData> getBalls() {
        return balls;
    }
}
