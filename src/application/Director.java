package application;

public class Director {

    public void constructBall(BallBuilder ballBuilder, BallData data) {

        ballBuilder.setColour(data.getColour());
        ballBuilder.setPosX(data.getPositionX());
        ballBuilder.setPosY(data.getPositionY());
        ballBuilder.setRadius(data.getRadius());
        ballBuilder.setVelX(data.getVelocityX());
        ballBuilder.setVelY(data.getVelocityY());
        ballBuilder.setMass(data.getMass());
    }
}
