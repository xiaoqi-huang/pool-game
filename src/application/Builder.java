package application;

import java.util.ArrayList;

public interface Builder {

    void createBall(BallData data, double radius);

    ArrayList getResult();
}
