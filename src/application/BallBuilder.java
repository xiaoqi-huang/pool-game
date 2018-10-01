package application;

import javafx.scene.paint.Color;

public interface BallBuilder {

    public void setColour(Color colour);

    public void setPosX(double posX);

    public void setPosY(double posY);

    public void setRadius(double radius);

    public void setVelX(double velX);

    public void setVelY(double velY);

    public void setMass(double mass);
}
