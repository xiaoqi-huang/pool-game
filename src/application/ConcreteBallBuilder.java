package application;

import javafx.scene.paint.Color;

/**
 * Concrete Ball Builder
 */
public class ConcreteBallBuilder implements BallBuilder {

    private Color colour;
    private double positionX;
    private double positionY;
    private double radius;
    private double velocityX;
    private double velocityY;
    private double mass;

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public Ball getBall() {
        return new Ball(colour, positionX, positionY, radius, velocityX, velocityY, mass);
    }
}
