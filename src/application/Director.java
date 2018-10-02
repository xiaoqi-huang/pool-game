package application;

/**
 * Director that directs the BallBuilder
 */
public class Director {

    public void constructBall(BallBuilder ballBuilder, Data data) {

    	BallData ball = (BallData) data;
    	
        ballBuilder.setColour(ball.getColour());
        ballBuilder.setPositionX(ball.getPositionX());
        ballBuilder.setPositionY(ball.getPositionY());
        ballBuilder.setVelocityX(ball.getVelocityX());
        ballBuilder.setVelocityY(ball.getVelocityY());
        ballBuilder.setMass(ball.getMass());
    }
}
