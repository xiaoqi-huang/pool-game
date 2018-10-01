package application;

import java.util.ArrayList;

/**
 * Balls Data can store a collection of Ball Data.
 */
public class BallsData implements Data {

    ArrayList<BallData> balls = new ArrayList<BallData>();

    public BallsData() { }

    public void add(BallData ball) { balls.add(ball); }

    public ArrayList<BallData> getBalls() { return balls; }
}
