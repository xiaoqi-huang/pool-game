package application;

import java.util.ArrayList;

public class Memento {

    private ArrayList<BallData> balls;
    private int count;

    public Memento(ArrayList<BallData> balls, int count) {

        this.balls = balls;
        this.count = count;
    }

    public ArrayList<BallData> getState() {
        return balls;
    }

    public int getCount() { return count; }
}
