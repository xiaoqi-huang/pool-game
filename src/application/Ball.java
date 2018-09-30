package application;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Ball extends Circle {

    private double velocityX;
    private double velocityY;
    private double mass;

    public Ball(String colour, double positionX, double positionY, double radius, double velocityX, double velocityY, double mass) {

        super(positionX, positionY, radius);
        setFill(Paint.valueOf(colour));
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.mass = mass;
    }

    // TODO: setter
    public void setVelocityX(double velocityX) { this.velocityX = velocityX; }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getMass() {
        return mass;
    }

    public void move(TableData table) {

        double acc = table.getFriction() / mass;

        // UPDATE
        velocityX = updateVelocity(velocityX, acc);
        velocityY = updateVelocity(velocityY, acc);

        double positionX = getCenterX();
        double positionY = getCenterY();
        double radius = getRadius();
        double rHole =  1.6 * 2 * radius;

        positionX += velocityX;
        if ((positionX + radius >= table.getX()) && (positionY > rHole) && (positionY < table.getY() - rHole)) {
            positionX = table.getX() - radius;
            velocityX *= -1;
        } else if ((positionX - radius < 0) && (positionY > rHole) && (positionY < table.getY() - rHole)) {
            positionX = 0 + radius;
            velocityX *= -1;
        }

        positionY += velocityY;
        if ((positionY + radius >= table.getY()) && ((positionX > rHole && positionX < table.getX() / 2 - rHole / 2) || (positionX > table.getX() / 2 + rHole / 2 && positionX < table.getX() - rHole))) {
            positionY = table.getY() - radius;
            velocityY *= -1;
        } else if ((positionY - radius < 0) && ((positionX > rHole && positionX < table.getX() / 2 - rHole / 2) || (positionX > table.getX() / 2 + rHole / 2 && positionX < table.getX() - rHole))) {
            positionY = 0 + radius;
            velocityY *= -1;
        }

        // RENDER
        setCenterX(positionX);
        setCenterY(positionY);
    }

    public boolean overlap(Ball ball) {

        Double rA = getRadius();
        Double rB = ball.getRadius();

        return distance(ball) <= (rA + rB);
    }

    public double distance(Ball ball) {

        Double xA = getCenterX();
        Double yA = getCenterY();
        Double xB = ball.getCenterX();
        Double yB = ball.getCenterY();

        return Math.sqrt(Math.pow(xA - xB, 2) + Math.pow(yA - yB, 2));
    }

    public boolean isMoving() {
        return (velocityX != 0) && (velocityY != 0);
    }

    public Double updateVelocity(Double vel, Double acc) {
        if (vel > 0) {
            vel = vel - acc * (Math.abs(vel) / Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)));
            vel = (vel < 0) ? 0 : vel;
        } else if (vel < 0) {
            vel = vel + acc * (Math.abs(vel) / Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)));
            vel = (vel > 0) ? 0 : vel;
        }
        return vel;
    }
}
