package application;

import javafx.scene.paint.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * A Configure Reader that only reads data about Balls in a configure file
 */
public class BallsConfigReader extends ConfigReader {

    // The radius is not specified in the specification.
    private double radius = 10.0;

    /**
     * This method looks into the configure file, extracts data about Balls.
     * The data of each Ball is stored in its own BallData.
     * Then all BallDatas are put in a BallsData.
     * @param path This is the path for the JSON file.
     * @return BallsData containing all data read from the JSON file
     */
    @Override
    public Data parse(String path) {

        BallsData balls = new BallsData();

        JSONParser parser = new JSONParser();

        try {

            Object object = parser.parse(new FileReader(path));

            // convert Object to JSONObject
            JSONObject jsonObject = (JSONObject) object;

            // reading the "Balls" section:
            JSONObject jsonBalls = (JSONObject) jsonObject.get("Balls");

            // reading the "Balls: ball" array:
            JSONArray jsonBallsBall = (JSONArray) jsonBalls.get("ball");

            // reading from the array:
            for (Object obj : jsonBallsBall) {

                JSONObject jsonBall = (JSONObject) obj;

                // the ball colour is a String
                String colour = (String) jsonBall.get("colour");

                // the ball position, velocity, mass are all doubles
                Double positionX = (Double) ((JSONObject) jsonBall.get("position")).get("x");
                Double positionY = (Double) ((JSONObject) jsonBall.get("position")).get("y");

                Double velocityX = (Double) ((JSONObject) jsonBall.get("velocity")).get("x");
                Double velocityY = (Double) ((JSONObject) jsonBall.get("velocity")).get("y");

                Double mass = (Double) jsonBall.get("mass");

                BallData ball = new BallData(Color.valueOf(colour), positionX, positionY, radius, velocityX, velocityY, mass);
                balls.add(ball);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return balls;
    }
}
