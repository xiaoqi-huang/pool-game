package application;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.util.ArrayList;

public class Main extends Application {

    private Group root = new Group();
    private Table table = null;
    private ArrayList<Ball> balls = new ArrayList<>();
    private Ball cueBall = null;
    private Stick stick = null;

    private int count = 0;      // The number of pocketed balls

    private long startTime;     // System time when the mouse is pressed
    private Double mouseX;      // x-coordinate when the mouse is pressed
    private Double mouseY;      // y-coordinate when the mouse is pressed

    // Gatekeeper for a stack of Mementos
    private Caretaker caretaker = new Caretaker();

    // The main animator updating movement of balls
    private AnimationTimer animator;

    // Player for the the sound effect
    final private AudioClip clip = new AudioClip(new File("sound.wav").toURI().toString());


    public static void main(String[] args) { launch(args); }

    /**
     * This method is the key code for the Pool Game. It setups the sceme and tracks movement of Balls.
     * Main steps:
     * 1. Setup the scene by calling setupScene()
     * 2. Enable hitting functionality
     * 3. Run the animator that can track positions of Balls and check whether the game ends.
     * @param primaryStage This the the primary stage where the scene is added.
     * @throws Exception This is possible to be threw when calling end(State state);
     */
    @Override
    public void start(final Stage primaryStage) throws Exception{
        Parent h = FXMLLoader.load(getClass().getResource("application.fxml"));


        // Set the scene -> set the primary stage
        Scene scene = setupScene();

        primaryStage.setTitle("Pool Game");
        primaryStage.setScene(scene);
        primaryStage.show();


        // If the cue ball is not provided, an error will be reported.
        if (cueBall == null || table == null) { end(GameState.ERROR, primaryStage); }


        // Enable hitting the cue ball
        enableHit();

        // Show tips when the game starts
        showTip();

        // Track positions of Balls & Check whether the game ends
        animator = new AnimationTimer() {
            @Override
            public void handle(long arg0) {

                // Compute Balls in sequence
                for (Ball ball : balls) {

                    // Skip the pocketed Ball
                    if (ball.isPocketed(table)) {
                        continue;
                    }

                    // Update the position & velocity of the Ball
                    // The state of the Ball is returned here.
                    BallState result = ball.move(table, balls, count);


                    // According to the state of the Ball, this method checks whether the game ends.
                    switch (result) {
                        case POCKETED:
                            // If the pocketed Ball is the cue ball, then the game fails.
                            if (ball == cueBall) {
                                try {
                                    end(GameState.FAILURE, primaryStage);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            // If the pocketed Ball is not the cue ball, increase the counter by 1.
                            count++;

                            // If the table is cleared, then the game succeeds.
                            if (cleared()) {
                                try {
                                    end(GameState.SUCCESS, primaryStage);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case NOTPOCKETED:
                            break;
                    }
                }
            }
        };

        animator.start();
    }

    /**
     * This method is used to setup the scene.
     * 1. Read config.json
     * 2. Create table, pockets, balls, button -> attach them to the root
     * @return A Scene containing the root
     */
    private Scene setupScene() {

        // Load configure file
        String path = "config.json";

        ConfigReader tConfigReader = ConfigReader.getConfigReader("Table");
        ConfigReader bConfigReader = ConfigReader.getConfigReader("Balls");

        ArrayList<Data> tData = tConfigReader.parse(path);
        ArrayList<Data> bData = bConfigReader.parse(path);


        // Set table
        root.getChildren().add(getTable(tData));


        // Create pockets
        root.getChildren().addAll(getPockets());


        // Create balls (builder design pattern)
        Director director = new Director();
        ConcreteBallBuilder builder = new ConcreteBallBuilder();
        for (Data d : bData) {
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
     * When the cue ball is hit, the states of all balls is recorded for the UNDO functionality.
     * The direction of the force is determined by the position where the mouse is pressed;
     * The magnitude of the force is determined by the length of the time the mouse is pressed.
     */
    private void enableHit() {

        // When the mouse is pressed,
        // -> Record the position & the system time
        // -> Add a Stick to the Table
        table.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {

            // The cue ball can be hit when it exists and it is not moving.
            if ((cueBall != null) && (!cueBall.isMoving())) {

                // Save the current state of Balls
                save();

                startTime = System.currentTimeMillis();
                mouseX = event.getX();
                mouseY = event.getY();

                stick = new Stick(cueBall, mouseX, mouseY);
                root.getChildren().add(stick);
            }
        });

        // When the mouse is released,
        // -> Calculate the velocity given to the cue ball
        // -> Perform the "Stick hitting Cue Ball" animation
        table.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {

            // Calculate the velocity
            // This is used to avoid hitting the cue ball when the cue ball is moving.
            if (startTime < 0) { return; }

            long duration = System.currentTimeMillis() - startTime;
            startTime = -1;

            double velocity = 0.05 * duration;
            velocity = (velocity > 20) ? 20 : velocity;

            // Update the velocity of the cue ball
            cueBall.setVelocityX(velocity * ((cueBall.getCenterX() - mouseX) / Math.hypot(mouseX - cueBall.getCenterX(), mouseY - cueBall.getCenterY())));
            cueBall.setVelocityY(velocity * ((cueBall.getCenterY() - mouseY) / Math.hypot(mouseX - cueBall.getCenterX(), mouseY - cueBall.getCenterY())));

            // Run the "Stick hitting Cue Ball" animation
            animator.stop();
            Timeline hit = stick.hit(velocity);
            hit.setOnFinished(
                    event1 -> {
                        clip.play();
                        animator.start();
                        root.getChildren().remove(stick);
                    }
            );
        });
    }


    /**
     * This creates the Table.
     * @param data This is the ArrayList containing TableData needed for creating the Table.
     * @return A Table
     */
    private Table getTable(ArrayList<Data> data) {
    	// The TableData needed is the first element.
    	TableData d = (TableData) data.get(0);
        table = new Table(d.getColour(), d.getWidth(), d.getHeight(), d.getFriction());
        return table;
    }


    /**
     * This creates six pockets on the Table.
     * @return A ArrayList<Shape> containing six pockets
     */
    private ArrayList<Shape> getPockets() {

        double width = table.getWidth();
        double height = table.getHeight();
        double radius = 10.0;

        // Create corner pockets
        PocketFactory cornerPocketFactory = PocketFactory.getFactory(PocketType.CORNER);
        ArrayList<Shape> pockets = cornerPocketFactory.getPockets(radius, width, height);

        // Create side pockets
        PocketFactory sidePocketFactory = PocketFactory.getFactory(PocketType.SIDE);
        pockets.addAll(sidePocketFactory.getPockets(radius, width, height));

        return pockets;
    }


    /**
     * This creates the UNDO button.
     * When it is clicked, undo() is invoked.
     * @return The UNDO button
     */
    private Button getButton() {

        Button button = new Button("UNDO");

        button.setPrefSize(80, 10);
        button.setLayoutX(table.getWidth() - 80);
        button.setLayoutY(table.getHeight());
        button.setStyle("-fx-background-color: beige; -fx-text-fill: black");

        // undo() is called when the button is clicked
        button.setOnAction(event -> undo());

        // Add effect to the button
        // The text turns to white when the mouse hovers on it
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: beige; -fx-text-fill: whitesmoke"));
        // The text turns to black when the mouse is not on it
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: beige; -fx-text-fill: black"));

        return button;
    }


    /**
     * This searches for the cue ball.
     */
    private void setCueBall() {

        for (Ball ball : balls) {
            if (ball.getFill() == Color.WHITE) {
                cueBall = ball;
            }
        }
    }


    /**
     * This check whether the table is cleared.
     * A cleared has only the cue ball.
     * @return boolean result
     */
    private boolean cleared() {
        return (balls.size() - count) == 1;
    }


    /**
     * This store current states of all Balls.
     */
    private void save() {

        ArrayList<BallData> data = new ArrayList<>();

        for (Ball ball : balls) {

            BallData d = new BallData((Color) ball.getFill(), ball.getCenterX(), ball.getCenterY(), ball.getRadius(), ball.getVelocityX(), ball.getVelocityY(), ball.getMass());
            data.add(d);
        }

        caretaker.addMemento(new Memento(data, count));
    }


    /**
     * This method restore the state of all Balls to the previous state before the hit.
     */
    private void undo() {

        if (caretaker.isEmpty()) { return; }

        Memento memento = caretaker.getMemento();

        Pair<Integer, ArrayList<BallData>> state = memento.getState();

        count = state.getKey();

        ArrayList<BallData> data = state.getValue();
        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);
            ball.restore(data.get(i));
        }
    }


    /**
     * This method shows tips for the Pool Game.
     */
    private void showTip() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tips");
        alert.setHeaderText(null);
        alert.setContentText("Hi!\n" +
                "This is a simple Pool Game. Try to clear the Table!\n\n" +
                "Tips:\n" +
                "1. The force of the hit is determined by the position and the length of time the mouse is pressed.\n" +
                "2. You can click the UNDO button to go back to the previous hits.");
        alert.show();
    }


    /**
     * This method will end the game.
     * 1. Pop up a dialog box according to the state of the game.
     * 2. Close windows
     * @param state This is the state of the game when the game ends.
     * @param stage This is the stage where the game is presented.
     * @throws Exception This is possible to be threw by stop().
     */
    private void end(GameState state, Stage stage) throws Exception {

        stop();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);

        switch (state) {
            case FAILURE:
                alert.setContentText("Oops! The cue ball is pocketed!");
                break;
            case SUCCESS:
                alert.setContentText("Good game! The table is cleared!");
                break;
            case ERROR:
                alert.setContentText("The cue ball cannot be found.");
        }

        alert.setOnHidden(event -> stage.close());

        alert.show();
    }
}
