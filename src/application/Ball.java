package application;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import java.util.ArrayList;

public class Ball extends Circle {

    private double velX;
    private double velY;
    private double mass;

    public Ball(Color colour, double positionX, double positionY, double radius, double velX, double velY, double mass) {

        super(positionX, positionY, radius);

        setFill(colour);
        this.velX = velX;
        this.velY = velY;
        this.mass = mass;
    }

    public void setVelocityX(double velX) { this.velX = velX; }

    public void setVelocityY(double velY) {
        this.velY = velY;
    }

    public void setMass(double mass) { this.mass = mass; }

    public double getVelocityX() {
        return velX;
    }

    public double getVelocityY() { return velY; }

    public double getMass() {
        return mass;
    }

    public int move(Table table, ArrayList<Ball> balls) {

        double acc = table.getFriction() / mass;

        // UPDATE
        velX = updateVelocity(velX, acc);
        velY = updateVelocity(velY, acc);

        double posX = getCenterX();
        double posY = getCenterY();
        double radius = getRadius();

        double width = table.getWidth();
        double height = table.getHeight();
        double rc =  (1.6 * 2 * radius) / Math.sqrt(2); // Radius of corner pockets
        double rs = 1.6 * radius; // Radius of side pockets

        posX += velX;
        if ((posX + radius >= width) && (posY > rc) && (posY < height - rc)) {
            posX = width - radius;
            velX *= -1;
        } else if ((posX - radius < 0) && (posY > rc) && (posY < height - rc)) {
            posX = radius;
            velX *= -1;
        }

        posY += velY;
        if ((posY + radius >= height) && ((posX > rc && posX < (width / 2 - rs)) || (posX > (width / 2 + rs) && posX < width - rc))) {
            posY = height - radius;
            velY *= -1;
        } else if ((posY - radius < 0) && ((posX > rc && posX < (width / 2 - rs)) || (posX > (width / 2 + rs) && posX < width - rc))) {
            posY = radius;
            velY *= -1;
        }

        // RENDER
        setCenterX(posX);
        setCenterY(posY);

        if (inPocket(table)) {
            if (isCueBall()) {
                return 1;
            } else {
                return 2;
            }
        }


        for (int index = balls.indexOf(this); index < balls.size(); index++) {

            Ball ball = balls.get(index);

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

        double rA = getRadius();
        double rB = ball.getRadius();

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
        return (velX != 0) && (velY != 0);
    }

    private Double updateVelocity(Double vel, Double acc) {
        if (vel > 0) {
            vel = vel - acc * (Math.abs(vel) / Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2)));
            vel = (vel < 0) ? 0 : vel;
        } else if (vel < 0) {
            vel = vel + acc * (Math.abs(vel) / Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2)));
            vel = (vel > 0) ? 0 : vel;
        }
        return vel;
    }

    private boolean inPocket(Table table) {

        Double x = getCenterX();
        Double y = getCenterY();

        return (x < 0) || (x > table.getWidth()) || (y < 0) || (y > table.getHeight());
    }

    private boolean isCueBall() {
        return getFill() == Color.WHITE;
    }

    public void pocket(Table table, int count) {
         setCenterX(count * 2 * getRadius() + getRadius() + 1);
         setCenterY(table.getHeight() + getRadius() + 3);
    }

    public void copy(BallData data) {
        setFill(data.getColour());
        setCenterX(data.getPositionX());
        setCenterY(data.getPositionY());
        setRadius(data.getRadius());
        setVelocityX(data.getVelocityX());
        setVelocityY(data.getVelocityY());
        setMass(data.getMass());
    }

    public boolean isPocketed(Table table) {
        return getCenterY() > table.getHeight();
    }
}
