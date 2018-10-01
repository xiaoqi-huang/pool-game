package application;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Memento stores the snapshot of state of the game
 */
public class Memento {

    private int count;
    private ArrayList<BallData> balls;

    public Memento(ArrayList<BallData> balls, int count) {

        this.count = count;
        this.balls = balls;
    }

    public Pair<Integer, ArrayList<BallData>> getState() {
        return new Pair<>(count, balls);
    }
}
