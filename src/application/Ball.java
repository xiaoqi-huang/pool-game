package application;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Ball extends Circle {

    private Double velocityX;
    private Double velocityY;
    private Double mass;

    public Ball(String colour, Double positionX, Double positionY, Double radius, Double velocityX, Double velocityY, Double mass) {

        super(positionX, positionY, radius);
        setFill(Paint.valueOf(colour));
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

        Double positionX = getCenterX();
        Double positionY = getCenterY();
        Double radius = getRadius();

        positionX += velocityX;
        if ((positionX + radius >= table.getX()) && (positionY > 16) && (positionY < table.getY() - 16)) {
            positionX = table.getX() - radius;
            velocityX *= -1;
        } else if ((positionX - radius < 0) && (positionY > 16) && (positionY < table.getY() - 16)) {
            positionX = 0 + radius;
            velocityX *= -1;
        }

        positionY += velocityY;
        if ((positionY + radius >= table.getY()) && ((positionX > 16 && positionX < table.getX() / 2 - 8) || (positionX > table.getX() / 2 + 8 && positionX < table.getX() - 16))) {
            positionY = table.getY() - radius;
            velocityY *= -1;
        } else if ((positionY - radius < 0) && ((positionX > 16 && positionX < table.getX() / 2 - 8) || (positionX > table.getX() / 2 + 8 && positionX < table.getX() - 16))) {
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

    public Double distance(Ball ball) {

        Double xA = getCenterX();
        Double yA = getCenterY();
        Double xB = ball.getCenterX();
        Double yB = ball.getCenterY();

        return Math.sqrt(Math.pow(xA - xB, 2) + Math.pow(yA - yB, 2));
    }
}
