package application;

import javafx.scene.paint.Color;

public class ConcreteBallBuilder implements BallBuilder {

    private Color colour;
    private double posX;
    private double posY;
    private double radius;
    private double velX;
    private double velY;
    private double mass;

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public Ball getBall() {
        return new Ball(colour, posX, posY, radius, velX, velY, mass);
    }
}
