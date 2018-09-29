package application;

public class Director {

    public void createBalls(Builder builder, BallsData data) {

        for (BallData d : data.getBalls()) {
            builder.createBall(d);
        }
    }
}
