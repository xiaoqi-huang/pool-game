package application;

import java.util.ArrayList;

public class BallsData implements Data {

    ArrayList<BallData> balls = new ArrayList<BallData>();

    public BallsData() { }

    public BallsData(ArrayList<BallData> balls) {
        this.balls = balls;
    }

    public void add(BallData ball) { balls.add(ball); }

    public ArrayList<BallData> getBalls() {
        return balls;
    }
}
