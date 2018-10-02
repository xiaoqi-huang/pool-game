package application;

import java.util.ArrayList;

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

    abstract public ArrayList<Data> parse(String path);
}
