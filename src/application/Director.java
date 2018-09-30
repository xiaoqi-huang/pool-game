package application;

public class Director {

    public void createBalls(Builder builder, BallsData data) {

        double radius = data.getRadius();

        for (BallData d : data.getBalls()) {
            ((BallBuilder) builder).createBall(d, radius);
        }
    }
}
