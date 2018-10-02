package application;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Ball in Pool Game
 */
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

    // Setter

    public void setVelocityX(double velX) { this.velX = velX; }

    public void setVelocityY(double velY) { this.velY = velY; }

    public void setMass(double mass) { this.mass = mass; }

    // Getter

    public double getVelocityX() { return velX; }

    public double getVelocityY() { return velY; }

    public double getMass() { return mass; }


    /**
     * This method moves the Ball on the table.
     * Main steps:
     *  1, Update the velocity because of the friction
     *  2. Update the position of the Ball according to the velocity
     *  3. Bounce off bounds of the Table if the Ball hits bounds (updating position & velocity)
     *  4. Check whether the Ball is pocketed -> remove the Ball from the Table if pocketed -> return
     *  5. Bounce off other Balls if the Ball hits other Balls (updating velocity)
     * @param table This is the Table where the Ball is moving.
     * @param balls This is the collection of all Balls in the game.
     * @param count This is the number of pocketed Balls.
     * @return MoveResult This is can be POCKETED or NOTPOCKETED.
     */
    BallState move(Table table, ArrayList<Ball> balls, int count) {

        // 1. Update the velocity according to the friction
        double acc = table.getFriction() / mass;

        velX = updateVelocity(velX, acc);
        velY = updateVelocity(velY, acc);


        double posX = getCenterX();
        double posY = getCenterY();
        double radius = getRadius();

        double width = table.getWidth();
        double height = table.getHeight();
        double rc =  (1.6 * 2 * radius) / Math.sqrt(2);     // Radius of corner pockets
        double rs = 1.6 * radius;                           // Radius of side pockets


        // 2. Update the position of the Ball according to the velocity
        posX += velX;
        posY += velY;


        // 3. Bounce off the Table if the Ball hits bounds (updating position & velocity)
        if ((posX + radius >= width) && (posY > rc) && (posY < height - rc)) {
            posX = width - radius;
            velX *= -1;
        } else if ((posX - radius < 0) && (posY > rc) && (posY < height - rc)) {
            posX = radius;
            velX *= -1;
        }


        if ((posY + radius >= height) && ((posX > rc && posX < (width / 2 - rs)) || (posX > (width / 2 + rs) && posX < width - rc))) {
            posY = height - radius;
            velY *= -1;
        } else if ((posY - radius < 0) && ((posX > rc && posX < (width / 2 - rs)) || (posX > (width / 2 + rs) && posX < width - rc))) {
            posY = radius;
            velY *= -1;
        }

        setCenterX(posX);
        setCenterY(posY);


        // 4. Check whether the Ball is pocketed
        //    POCKETED -> remove the Ball from the Table -> return
        if (inPocket(table)) {
            pocket(table, count);
            return BallState.POCKETED;
        }


        // 5. Bounce off other Balls if the Ball hits other Balls (updating velocity)
        for (int index = balls.indexOf(this); index < balls.size(); index++) {

            Ball ball = balls.get(index);

            // No need to check pocketed Ball
            if (ball.isPocketed(table)) {
                continue;
            }

            // Bounce off the Ball only if overlapping with it
            if (overlap(ball)) {

                Point2D posA = new Point2D(posX, posY);
                Point2D velA = new Point2D(velX, velY);
                double massA = mass;

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

        return BallState.NOTPOCKETED;
    }


    /**
     * This calculates the distance between this Ball and another Ball.
     * @param ball This is another Ball.
     * @return The distance
     */
    private double distance(Ball ball) {

        Double xA = getCenterX();
        Double yA = getCenterY();
        Double xB = ball.getCenterX();
        Double yB = ball.getCenterY();

        return Math.hypot(xA - xB, yA - yB);
    }


    /**
     * This can check whether this Ball overlaps with another Ball.
     * @param ball This is another Ball.
     * @return boolean result
     */
    private boolean overlap(Ball ball) {

        double rA = getRadius();
        double rB = ball.getRadius();

        return distance(ball) < (rA + rB);
    }


    /**
     * This checks whether this Ball is movinf.
     * @return boolean result
     */
    boolean isMoving() {
        return (velX != 0) && (velY != 0);
    }


    /**
     * This method updates the velocity according to the acceleration.
     * @param vel This is the current velocity.
     * @param acc This is the the acceleration.
     * @return The updated velocity
     */
    private Double updateVelocity(Double vel, Double acc) {
        // The positive velocity cannot be negative after updating.
        // The negative velocity cannot be positive after updating.
        // Because the acceleration here is caused by the friction.
        if (vel > 0) {
            vel = vel - acc * (Math.abs(vel) / Math.hypot(velX, velY));
            vel = (vel < 0) ? 0 : vel;
        } else if (vel < 0) {
            vel = vel + acc * (Math.abs(vel) / Math.hypot(velX, velY));
            vel = (vel > 0) ? 0 : vel;
        }
        return vel;
    }


    /**
     * This checks whether this Ball is in Pockets.
     * @param table This is the Table where the game runs.
     * @return boolean result
     */
    private boolean inPocket(Table table) {

        double x = getCenterX();
        double y = getCenterY();

        return (x < 0) || (x > table.getWidth()) || (y < 0) || (y > table.getHeight());
    }


    /**
     * This method pockets this Ball.
     * The Ball will be placed below the Table.
     * @param table This is the Table where the game runs.
     * @param count This is the number of pocketed Balls.
     */
    private void pocket(Table table, int count) {
        setCenterX(count * 2 * getRadius() + getRadius() + 1);
        setCenterY(table.getHeight() + getRadius() + 3);
    }


    /**
     * This checks whether the Ball is pocketed.
     * The Pocketed Ball is placed below the Table.
     * @param table This is the Table where the game runs.
     * @return boolean result
     */
    boolean isPocketed(Table table) {
        return getCenterY() > table.getHeight();
    }


    /**
     * This restores the state of the Ball;
     * @param data This is BallData which contains the state the Ball will be restored to.
     */
    void restore(BallData data) {
        setFill(data.getColour());
        setCenterX(data.getPositionX());
        setCenterY(data.getPositionY());
        setRadius(data.getRadius());
        setVelocityX(data.getVelocityX());
        setVelocityY(data.getVelocityY());
        setMass(data.getMass());
    }
}
