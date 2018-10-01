package application;

/**
 * Director that directs the BallBuilder
 */
public class Director {

    public void constructBall(BallBuilder ballBuilder, BallData data) {

        ballBuilder.setColour(data.getColour());
        ballBuilder.setPositionX(data.getPositionX());
        ballBuilder.setPositionY(data.getPositionY());
        ballBuilder.setRadius(data.getRadius());
        ballBuilder.setVelocityX(data.getVelocityX());
        ballBuilder.setVelocityY(data.getVelocityY());
        ballBuilder.setMass(data.getMass());
    }
}
