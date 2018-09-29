package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

abstract public class ConfigReader {

    public static ConfigReader getConfigReader(String type) {

        ConfigReader reader = null;

        if (type.equals("Table")) {
            reader = new TableConfigReader();
        } else if (type.equals("Balls")) {
            reader = new BallsConfigReader();
        }

        return reader;
    }

    abstract public Data parse(String path);
}
