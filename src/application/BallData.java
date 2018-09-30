package application;

import javafx.scene.paint.Color;

public class BallData implements Data {

    private Color colour;
    private Double positionX;
    private Double positionY;
    private Double velocityX;
    private Double velocityY;
    private Double mass;

    public BallData(Color colour, Double positionX, Double positionY, Double velocityX, Double velocityY, Double mass) {
        this.colour = colour;
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.mass = mass;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public void setPositionXX(Double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(Double positionY) {
        this.positionY = positionY;
    }

    public void setVelocityX(Double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(Double velocityY) {
        this.velocityY = velocityY;
    }

    public void setMass(Double mass) {
        this.mass = mass;
    }

    public Color getColour() {
        return colour;
    }

    public Double getPositionX() {
        return positionX;
    }

    public Double getPositionY() {
        return positionY;
    }

    public Double getVelocityX() {
        return velocityX;
    }

    public Double getVelocityY() {
        return velocityY;
    }

    public Double getMass() {
        return mass;
    }
}
