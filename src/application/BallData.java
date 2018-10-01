package application;

import javafx.scene.paint.Color;

/**
 * BallData stores all data needed fot creating a Ball
 */
public class BallData implements Data {

    private Color colour;
    private double positionX;
    private double positionY;
    private double radius;
    private double velocityX;
    private double velocityY;
    private double mass;

    public BallData(Color colour, double positionX, double positionY, double radius, double velocityX, double velocityY, double mass) {
        this.colour = colour;
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.mass = mass;
    }

    // Setter

    public void setColour(Color colour) { this.colour = colour; }

    public void setPositionX(double positionX) { this.positionX = positionX; }

    public void setPositionY(double positionY) { this.positionY = positionY; }

    public void setRadius(double radius) { this.radius = radius; }

    public void setVelocityX(double velocityX) { this.velocityX = velocityX; }

    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }

    public void setMass(double mass) { this.mass = mass; }

    // Getter

    public Color getColour() { return colour; }

    public double getPositionX() { return positionX; }

    public double getPositionY() { return positionY; }

    public double getRadius() { return radius; }

    public double getVelocityX() { return velocityX; }

    public double getVelocityY() { return velocityY; }

    public double getMass() { return mass; }
}
