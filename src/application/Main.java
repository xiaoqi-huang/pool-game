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
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;

public class Main extends Application {

    private TableData table = null;
    private ArrayList<Ball> balls = null;

    private long startTime;
    private Double mouseX;
    private Double mouseY;
    private Group root;
    private Line stick;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));

        setup(primaryStage);

        AnimationTimer animator = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                for (Ball ball : balls) {
                    ball.move(table);

                    for (Ball b : balls) {

                        if (ball == b) {
                            continue;
                        }

                        if (!bounce(ball, b)) {
                            continue;
                        }
                        Point2D posA = new Point2D(ball.getCircle().getCenterX(), ball.getCircle().getCenterY());
                        Point2D velA = new Point2D(ball.getVelocityX(), ball.getVelocityY());
                        double massA = ball.getMass();

                        Point2D posB = new Point2D(b.getCircle().getCenterX(), b.getCircle().getCenterY());
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

        BallsConfigReader ballsConfigReader = (BallsConfigReader) ConfigReader.getConfigReader("Balls");
        BallsData ballsData = (BallsData) ballsConfigReader.parse(path);

        Builder builder = new Builder();
        Director director = new Director();
        director.createBalls(builder, ballsData);
        balls = builder.getResult();

        for (Ball ball : balls) {
            root.getChildren().add(ball.getCircle());
        }

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

                    stick = setStick();
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
                Ball cueBall = balls.get(0);
                cueBall.setVelocityX(velocity * ((cueBall.getCircle().getCenterX() - mouseX) / (Math.sqrt(Math.pow(mouseX - cueBall.getCircle().getCenterX(), 2) + Math.pow(mouseY - cueBall.getCircle().getCenterY(), 2)))));
                cueBall.setVelocityY(velocity * ((cueBall.getCircle().getCenterY() - mouseY) / (Math.sqrt(Math.pow(mouseX - cueBall.getCircle().getCenterX(), 2) + Math.pow(mouseY - cueBall.getCircle().getCenterY(), 2)))));

                root.getChildren().remove(stick);
            }
        });
    }

    private boolean bounce(Ball ballA, Ball ballB) {

        Double xA = ballA.getCircle().getCenterX();
        Double yA = ballA.getCircle().getCenterY();
        Double rA = ballA.getCircle().getRadius();
        Double xB = ballB.getCircle().getCenterX();
        Double yB = ballB.getCircle().getCenterY();
        Double rB = ballB.getCircle().getRadius();

        return Math.sqrt(Math.pow(xA - xB, 2) + Math.pow(yA - yB, 2)) < (rA + rB);
    }

    private boolean cue_ball_moving() {
        return (balls.get(0).getVelocityX() != 0) || (balls.get(0).getVelocityY() != 0);
    }

    private Line setStick() {

        Ball ball = getCueBall();
        Double ballX = ball.getCircle().getCenterX();
        Double ballY = ball.getCircle().getCenterY();

        Double radioX = (mouseX - ballX) / Math.sqrt(Math.pow(ballX - mouseX, 2) + Math.pow(ballY - mouseY, 2));
        Double radioY = (mouseY - ballY) / Math.sqrt(Math.pow(ballX - mouseX, 2) + Math.pow(ballY - mouseY, 2));

        Line line = new Line();
        line.setStartX(ballX + 20 * radioX);
        line.setStartY(ballY + 20 * radioY);
        line.setEndX(ballX + 320 * radioX);
        line.setEndY(ballY + 320 * radioY);
        line.setStroke(Color.BROWN);
        line.setStrokeWidth(5);
        return line;
    }

    private Ball getCueBall() {
        return balls.get(0);
    }
}
