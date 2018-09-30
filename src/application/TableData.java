package application;

import javafx.scene.paint.Color;

public class TableData implements Data {

    private Color colour;
    private Long x;
    private Long y;
    private Double friction;

    public TableData(Color colour, Long x, Long y, Double friction) {
        this.colour = colour;
        this.x = x;
        this.y = y;
        this.friction = friction;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public void setFriction(Double friction) {
        this.friction = friction;
    }

    public Color getColour() {
        return colour;
    }

    public Long getX() {
        return x;
    }

    public Long getY() {
        return y;
    }

    public Double getFriction() {
        return friction;
    }
}
