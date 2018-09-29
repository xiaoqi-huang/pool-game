package application;

import javafx.scene.shape.Circle;

public class Ball {

    private Circle circle;
    private Double velocityX;
    private Double velocityY;
    private Double mass;

    public Ball(Circle circle, Double velocityX, Double velocityY, Double mass) {
        this.circle = circle;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.mass = mass;
    }

    // TODO: setter
    public void setVelocityX(Double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(Double velocityY) {
        this.velocityY = velocityY;
    }

    public Circle getCircle() {
        return circle;
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

    public void move(TableData table) {

        Double acc = table.getFriction() / mass;

        // UPDATE
        if (velocityX > 0) {
            velocityX = velocityX - acc * (Math.abs(velocityX) / Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)));
            velocityX = (velocityX < 0) ? 0 : velocityX;
        } else if (velocityX < 0) {
            velocityX = velocityX + acc * (Math.abs(velocityX) / Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)));
            velocityX = (velocityX > 0) ? 0 : velocityX;
        }

        if (velocityY > 0) {
            velocityY = velocityY - acc * (Math.abs(velocityY) / Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)));
            velocityY = (velocityY < 0) ? 0 : velocityY;
        } else if (velocityY < 0) {
            velocityY = velocityY + acc * (Math.abs(velocityY) / Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)));
            velocityY = (velocityY > 0) ? 0 : velocityY;
        }

        Double positionX = circle.getCenterX();
        Double positionY = circle.getCenterY();
        Double radius = circle.getRadius();

        positionX += velocityX;
        if (positionX + radius >= table.getX()) {
            positionX = table.getX() - radius;
            velocityX *= -1;
        } else if (positionX - radius < 0) {
            positionX = 0 + radius;
            velocityX *= -1;
        }

        positionY += velocityY;
        if (positionY + radius >= table.getY()) {
            positionY = table.getY() - radius;
            velocityY *= -1;
        } else if (positionY - radius < 0) {
            positionY = 0 + radius;
            velocityY *= -1;
        }

        // RENDER
        circle.setCenterX(positionX);
        circle.setCenterY(positionY);
    }
}
