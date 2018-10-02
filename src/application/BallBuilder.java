package application;

import javafx.scene.paint.Color;

/**
 * Abstract Ball Builder
 */
public interface BallBuilder {

    public void setColour(Color colour);

    public void setPositionX(double positionX);

    public void setPositionY(double positionY);

    public void setVelocityX(double velocityX);

    public void setVelocityY(double velocityY);

    public void setMass(double mass);
}
