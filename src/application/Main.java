package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    private Table table = null;
    private ArrayList<Ball> balls = new ArrayList<Ball>();
    private Ball cueBall = null;
    private Stick stick;
    private Double radius = 10.0;

    private int count = 0; // The number of pocketed balls
    private long startTime;
    private Double mouseX;
    private Double mouseY;
    public Group root;
    private Caretaker caretaker = new Caretaker();

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

        // Enable hitting the cue ball
        hit();

        // Update positions of balls
        AnimationTimer animator = new AnimationTimer() {
            @Override
            public void handle(long arg0) {

                for (Ball ball : balls) {

                    if (ball.isPocketed(table)) {
                        continue;
                    }

                    int res = ball.move(table, balls);

                    switch (res) {
                        case 0:
                            break;
                        case 1:
                            ball.pocket(table, count);
                            cueBall = null;
                            System.out.println("Oops! Cue ball gets into a pocket!");

                            stop();
                            primaryStage.close();
                            break;
                        case 2:
                            ball.pocket(table, count);
                            count++;
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

    /**
     * This method is used to setup the scene.
     * Read config.json first.
     * Then, create table, pockets, balls, button
     * @param root This is the root to which nodes are attached
     * @return a Scene containing the root
     */
    private Scene setupScene(Group root) {

        // Load configure file
        String path = "config.json";

        TableConfigReader tConfigReader = (TableConfigReader) ConfigReader.getConfigReader("Table");
        BallsConfigReader bConfigReader = (BallsConfigReader) ConfigReader.getConfigReader("Balls");

        TableData tData = (TableData) tConfigReader.parse(path);
        BallsData bData = (BallsData) bConfigReader.parse(path);


        // Set table
        root.getChildren().add(getTable(tData));


        // Create pockets
        root.getChildren().addAll(getPockets());


        // Create balls (builder design pattern)
        Director director = new Director();
        ConcreteBallBuilder builder = new ConcreteBallBuilder();
        for (BallData d : bData.getBalls()) {
            director.constructBall(builder, d);
            Ball ball = builder.getBall();
            balls.add(ball);
        }
        root.getChildren().addAll(balls);


        // Set the cue ball
        setCueBall();


        // Create UNDO button
        root.getChildren().add(getButton());


        Scene scene = new Scene(root, table.getWidth(), table.getHeight() + 24);
        scene.setFill(Color.BEIGE);
        return scene;
    }

    /**
     * This method allows the player to hit the cue ball with the cue stick.
     * The cue ball can be hit only when it is not moving.
     * When the cue ball is hit, the states of all balls is recorded for the undo functionality.
     * The direction of the force is determined by the position where the mouse is pressed;
     * The magnitude of the force is determined by the length of the time the mouse is pressed.
     */
    private void hit() {

        table.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (!cue_ball_moving()) {

                    save();

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

        table.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
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

    private Table getTable(TableData data) {
        table = new Table(data.getColour(), data.getX(), data.getY(), data.getFriction());
        return table;
    }

    private ArrayList<Shape> getPockets() {

        double width = table.getWidth();
        double height = table.getHeight();

        PocketFactory cornerPocketFactory = PocketFactory.getFactory("Corner");
        ArrayList<Shape> pockets = cornerPocketFactory.getPockets(radius, width, height);

        PocketFactory sidePocketFactory = PocketFactory.getFactory("Side");
        pockets.addAll(sidePocketFactory.getPockets(radius, width, height));

        return pockets;
    }

    private Button getButton() {

        Button button = new Button("UNDO");

        button.setPrefSize(80, 10);
        button.setLayoutX(table.getWidth() - 80);
        button.setLayoutY(table.getHeight());
        button.setStyle("-fx-background-color: beige");

        // undo function is call when the button is clicked
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                undo();
            }
        });

        return button;
    }

    private boolean clean() {
        return balls.size() == 1;
    }

    private void save() {

        ArrayList<BallData> data = new ArrayList<BallData>();

        for (Ball ball : balls) {

            BallData d = new BallData((Color) ball.getFill(), ball.getCenterX(), ball.getCenterY(), ball.getRadius(), ball.getVelocityX(), ball.getVelocityY(), ball.getMass());
            data.add(d);
        }

        caretaker.addMemento(new Memento(data, count));
    }

    private void undo() {

        if (caretaker.isEmpty()) {
            return;
        }

        Memento memento = caretaker.getMemento();

        ArrayList<BallData> data = memento.getState();
        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);
            ball.copy(data.get(i));
        }

        count = memento.getCount();
    }
}
