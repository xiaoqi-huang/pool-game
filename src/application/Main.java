package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;

public class Main extends Application {

    private TableData table = null;
    private ArrayList<Ball> balls = null;
    private Ball cueBall = null;
    private Double radius = 10.0;

    private long startTime;
    private Double mouseX;
    private Double mouseY;
    private Group root;
    private Stick stick;

    @Override
    public void start(final Stage primaryStage) throws Exception{
        Parent h = FXMLLoader.load(getClass().getResource("application.fxml"));

        setup(primaryStage);

        AnimationTimer animator = new AnimationTimer() {
            @Override
            public void handle(long arg0) {

                Iterator<Ball> iter = balls.iterator();

                while (iter.hasNext()) {

                    Ball ball = iter.next();

                    ball.move(table);

                    if (in_hole(ball)) {
                        // Remove the ball that goes into a pocket
                        iter.remove();
                        root.getChildren().remove(ball);

                        if (ball == cueBall) {
                            cueBall = null;
                            System.out.println("Oops! Cue ball gets into a pocket!");
                            stop();
                        } else {
                            System.out.println("One ball gets into the pocket!");
                            if (clean()) {
                                System.out.println("SUCCESS!");
                                stop();
                            }
                            continue;
                        }
                    }

                    Iterator<Ball> iter2 = balls.iterator();

                    while (iter2.hasNext()) {

                        Ball b = iter2.next();

                        if (ball == b) {
                            continue;
                        }

                        if (ball.overlap(b)) {

                            Point2D posA = new Point2D(ball.getCenterX(), ball.getCenterY());
                            Point2D velA = new Point2D(ball.getVelocityX(), ball.getVelocityY());
                            double massA = ball.getMass();

                            Point2D posB = new Point2D(b.getCenterX(), b.getCenterY());
                            Point2D velB = new Point2D(b.getVelocityX(), b.getVelocityY());
                            double massB = b.getMass();


                            Pair<Point2D, Point2D> vels = PhysicsUtility.calculateCollision(posA, velA, massA, posB, velB, massB);

                            ball.setVelocityX(vels.getKey().getX());
                            ball.setVelocityY(vels.getKey().getY());
                            b.setVelocityX(vels.getValue().getX());
                            b.setVelocityY(vels.getValue().getY());
                        }
                    }
                }
            }
        };

        animator.start();

    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setup(Stage stage) {

        root = new Group();

        String path = "config.json";

        TableConfigReader tableConfigReader = (TableConfigReader) ConfigReader.getConfigReader("Table");
        table = (TableData) tableConfigReader.parse(path);
        System.out.println(table.getColour());
        Scene scene = new Scene(root, table.getX(), table.getY());
        scene.setFill(Paint.valueOf(table.getColour()));

        // Set holes
        root.getChildren().addAll(holes());

        // Set balls
        BallsConfigReader ballsConfigReader = (BallsConfigReader) ConfigReader.getConfigReader("Balls");
        BallsData ballsData = (BallsData) ballsConfigReader.parse(path);

        Builder builder = new Builder();
        Director director = new Director();
        director.createBalls(builder, ballsData);
        balls = builder.getResult();

        for (Ball ball : balls) {
            root.getChildren().add(ball);
        }

        // Set the cue ball
        cueBall = getCueBall();

        stage.setTitle("Pool Game");
        stage.setScene(scene);
        stage.show();

        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (!cue_ball_moving()) {

                    startTime = System.currentTimeMillis();
                    mouseX = event.getX();
                    mouseY = event.getY();

                    stick = new Stick(cueBall, mouseX, mouseY);
                    root.getChildren().add(stick);
                }
            }
        });

        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;

                Double velocity = 0.05 * duration;
                velocity = (velocity > 20) ? 20 : velocity;

                Ball cueBall = balls.get(0);
                cueBall.setVelocityX(velocity * ((cueBall.getCenterX() - mouseX) / (Math.sqrt(Math.pow(mouseX - cueBall.getCenterX(), 2) + Math.pow(mouseY - cueBall.getCenterY(), 2)))));
                cueBall.setVelocityY(velocity * ((cueBall.getCenterY() - mouseY) / (Math.sqrt(Math.pow(mouseX - cueBall.getCenterX(), 2) + Math.pow(mouseY - cueBall.getCenterY(), 2)))));

                root.getChildren().remove(stick);
            }
        });
    }

    private boolean cue_ball_moving() {
        return cueBall.isMoving();
    }

    private Ball getCueBall() {
        return balls.get(0);
    }

    private ArrayList<Shape> holes() {

        ArrayList<Shape> holes = new ArrayList<Shape>();
        Long width = table.getX();
        Long height = table.getY();

        Polyline hole1 = new Polyline();
        hole1.getPoints().addAll(new Double[]{
                0.0, 1.6 * 2 * radius,
                0.0, 0.0,
                1.6 * 2 * radius, 0.0
        });
        hole1.setStrokeWidth(5);

        Line hole2 = new Line();
        hole2.setStartX(width / 2 - 1.6 * 2 * radius / 2);
        hole2.setStartY(0);
        hole2.setEndX(width / 2 + 1.6 * 2 * radius / 2);
        hole2.setEndY(0);
        hole2.setStrokeWidth(5);

        Polyline hole3 = new Polyline();
        hole3.getPoints().addAll(new Double[]{
                Double.valueOf(width - 1.6 * 2 * radius), 0.0,
                Double.valueOf(width), 0.0,
                Double.valueOf(width), 1.6 * 2 * radius,
        });
        hole3.setStrokeWidth(5);

        Polyline hole4 = new Polyline();
        hole4.getPoints().addAll(new Double[]{
                0.0, Double.valueOf(height - 1.6 * 2 * radius),
                0.0, Double.valueOf(height),
                1.6 * 2 * radius, Double.valueOf(height)
        });
        hole4.setStrokeWidth(5);

        Line hole5 = new Line();
        hole5.setStartX(width / 2 - 1.6 * 2 * radius / 2);
        hole5.setStartY(height);
        hole5.setEndX(width / 2 + 1.6 * 2 * radius / 2);
        hole5.setEndY(height);
        hole5.setStrokeWidth(5);

        Polyline hole6 = new Polyline();
        hole6.getPoints().addAll(new Double[]{
                Double.valueOf(width - 1.6 * 2 * radius), Double.valueOf(height),
                Double.valueOf(width), Double.valueOf(height),
                Double.valueOf(width), Double.valueOf(height - 1.6 * 2 * radius),
        });
        hole6.setStrokeWidth(5);

        holes.add(hole1);
        holes.add(hole2);
        holes.add(hole3);
        holes.add(hole4);
        holes.add(hole5);
        holes.add(hole6);
        return holes;
    }

    public boolean in_hole(Ball ball) {

        Double x = ball.getCenterX();
        Double y = ball.getCenterY();

        return (x < 0) || (x > table.getX()) || (y < 0) || (y > table.getY());
    }

    public boolean clean() {
        return balls.size() == 1;
    }
}
