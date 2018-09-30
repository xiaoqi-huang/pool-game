package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;

public class Main extends Application {

    private TableData table = null;
    private ArrayList<Ball> balls = null;
    private Ball cueBall = null;
    private Stick stick;
    private Double radius = 10.0;

    private long startTime;
    private Double mouseX;
    private Double mouseY;
    public Group root;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception{
        Parent h = FXMLLoader.load(getClass().getResource("application.fxml"));

        root = new Group();

        Scene scene = setupScene(root);

        primaryStage.setTitle("Pool Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        hit(scene);

        AnimationTimer animator = new AnimationTimer() {
            @Override
            public void handle(long arg0) {

                Iterator<Ball> iter = balls.iterator();

                while (iter.hasNext()) {

                    Ball ball = iter.next();

                    int res = ball.move(table, balls);

                    switch (res) {
                        case 0:
                            break;
                        case 1:
                            cueBall = null;
                            iter.remove();
                            root.getChildren().remove(ball);
                            System.out.println("Oops! Cue ball gets into a pocket!");
                            stop();
                            break;
                        case 2:
                            iter.remove();
                            root.getChildren().remove(ball);
                            System.out.println("One ball gets into the pocket!");
                            if (clean()) {
                                System.out.println("SUCCESS!");
                                stop();
                            }
                            break;
                    }
                }
            }
        };

        animator.start();

    }

    private Scene setupScene(Group root) {

        String path = "config.json";

        // Set table
        TableConfigReader tableConfigReader = (TableConfigReader) ConfigReader.getConfigReader("Table");
        table = (TableData) tableConfigReader.parse(path);
        Scene scene = new Scene(root, table.getX(), table.getY());
        scene.setFill(Paint.valueOf(table.getColour()));

        // Create pockets
        root.getChildren().addAll(getPockets());

        // Create balls
        BallsConfigReader ballsConfigReader = (BallsConfigReader) ConfigReader.getConfigReader("Balls");
        BallsData ballsData = (BallsData) ballsConfigReader.parse(path);
        ballsData.setRadius(radius);

        Director director = new Director();
        BallBuilder builder = new BallBuilder();
        director.createBalls(builder, ballsData);
        balls = builder.getResult();

        root.getChildren().addAll(balls);

        // Set the cue ball
        setCueBall();

        return scene;
    }

    private void hit(Scene scene) {

        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (!cue_ball_moving()) {

                    startTime = System.currentTimeMillis();
                    mouseX = event.getX();
                    mouseY = event.getY();

                    if (cueBall != null) {
                        stick = new Stick(cueBall, mouseX, mouseY);
                        root.getChildren().add(stick);
                    }
                }
            }
        });

        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                long endTime = System.currentTimeMillis();
                if (startTime < 0) {
                    return;
                }
                long duration = endTime - startTime;
                startTime = -1;

                double velocity = 0.05 * duration;
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

    private void setCueBall() {

        for (Ball ball : balls) {
            if (ball.getFill() == Color.WHITE) {
                cueBall = ball;
            }
        }
    }

    private ArrayList<Shape> getPockets() {

        double x = table.getX();
        double y = table.getY();

        PocketFactory cornerPocketFactory = PocketFactory.getFactory("Corner");
        ArrayList<Shape> pockets = cornerPocketFactory.getPockets(radius, x, y);

        PocketFactory sidePocketFactory = PocketFactory.getFactory("Side");
        pockets.addAll(sidePocketFactory.getPockets(radius, x, y));

        return pockets;
    }

    private boolean clean() {
        return balls.size() == 1;
    }
}
