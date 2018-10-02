package application;

import javafx.scene.paint.Color;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A Configure Reader that only reads data about Table in a configure file
 */
public class TableConfigReader extends ConfigReader {

    /**
     * This method looks into the configure file, extracts data about Table and store them in a TableData.
     * @param path This is the path for the JSON file.
     * @return ArrayList<Data> containing only one TableData read from the JSON file
     */
    @Override
    public ArrayList<Data> parse(String path) {

    	ArrayList<Data> result = new ArrayList<Data>();

        JSONParser parser = new JSONParser();

        try {

            Object object = parser.parse(new FileReader(path));

            // convert Object to JSONObject
            JSONObject jsonObject = (JSONObject) object;

            // reading the Table section:
            JSONObject jsonTable = (JSONObject) jsonObject.get("Table");
            
            // reading a value from the table section
            String colour = (String) jsonTable.get("colour");

            // reading a coordinate from the nested section within the table
            // note that the table x and y are of type Long (i.e. they are integers)
            Long x = (Long) ((JSONObject) jsonTable.get("size")).get("x");
            Long y = (Long) ((JSONObject) jsonTable.get("size")).get("y");

            // getting the friction level.
            // This is a double which should affect the rate at which the balls slow down
            Double friction = (Double) jsonTable.get("friction");

            result.add(new TableData(Color.valueOf(colour), x, y, friction));

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return result;
    }
}
