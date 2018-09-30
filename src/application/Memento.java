package application;

import java.util.ArrayList;

public class Memento {

    private ArrayList<BallData> balls;

    public Memento(ArrayList<BallData> balls) {
        this.balls = balls;
    }

    public ArrayList<BallData> getState() {
        return balls;
    }
}
