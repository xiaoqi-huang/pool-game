package application;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;

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

    public int move(TableData table, ArrayList<Ball> balls) {

        double acc = table.getFriction() / mass;

        // UPDATE
        velocityX = updateVelocity(velocityX, acc);
        velocityY = updateVelocity(velocityY, acc);

        double posX = getCenterX();
        double posY = getCenterY();
        double radius = getRadius();

        double width = table.getX();
        double height = table.getY();
        double rc =  (1.6 * 2 * radius) / Math.sqrt(2); // Radius of corner pockets
        double rs = 1.6 * radius; // Radius of side pockets

        posX += velocityX;
        if ((posX + radius >= width) && (posY > rc) && (posY < height - rc)) {
            posX = width - radius;
            velocityX *= -1;
        } else if ((posX - radius < 0) && (posY > rc) && (posY < height - rc)) {
            posX = radius;
            velocityX *= -1;
        }

        posY += velocityY;
        if ((posY + radius >= height) && ((posX > rc && posX < (width / 2 - rs)) || (posX > (width / 2 + rs) && posX < width - rc))) {
            posY = height - radius;
            velocityY *= -1;
        } else if ((posY - radius < 0) && ((posX > rc && posX < (width / 2 - rs)) || (posX > (width / 2 + rs) && posX < width - rc))) {
            posY = radius;
            velocityY *= -1;
        }

        // RENDER
        setCenterX(posX);
        setCenterY(posY);

        if (in_hole(table)) {
            if (isCueBall()) {
                return 1;
            } else {
                return 2;
            }
        }


        for (Ball ball : balls) {

            if (this == ball) {
                continue;
            }

            if (overlap(ball)) {

                Point2D posA = new Point2D(getCenterX(), getCenterY());
                Point2D velA = new Point2D(getVelocityX(), getVelocityY());
                double massA = getMass();

                Point2D posB = new Point2D(ball.getCenterX(), ball.getCenterY());
                Point2D velB = new Point2D(ball.getVelocityX(), ball.getVelocityY());
                double massB = ball.getMass();


                Pair<Point2D, Point2D> vels = PhysicsUtility.calculateCollision(posA, velA, massA, posB, velB, massB);

                setVelocityX(vels.getKey().getX());
                setVelocityY(vels.getKey().getY());
                ball.setVelocityX(vels.getValue().getX());
                ball.setVelocityY(vels.getValue().getY());
            }
        }

        return 0;
    }

    private boolean overlap(Ball ball) {

        Double rA = getRadius();
        Double rB = ball.getRadius();

        return distance(ball) <= (rA + rB);
    }

    private double distance(Ball ball) {

        Double xA = getCenterX();
        Double yA = getCenterY();
        Double xB = ball.getCenterX();
        Double yB = ball.getCenterY();

        return Math.sqrt(Math.pow(xA - xB, 2) + Math.pow(yA - yB, 2));
    }

    boolean isMoving() {
        return (velocityX != 0) && (velocityY != 0);
    }

    private Double updateVelocity(Double vel, Double acc) {
        if (vel > 0) {
            vel = vel - acc * (Math.abs(vel) / Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)));
            vel = (vel < 0) ? 0 : vel;
        } else if (vel < 0) {
            vel = vel + acc * (Math.abs(vel) / Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)));
            vel = (vel > 0) ? 0 : vel;
        }
        return vel;
    }

    private boolean in_hole(TableData table) {

        Double x = getCenterX();
        Double y = getCenterY();

        return (x < 0) || (x > table.getX()) || (y < 0) || (y > table.getY());
    }

    private boolean isCueBall() {
        return getFill() == Color.WHITE;
    }
}
