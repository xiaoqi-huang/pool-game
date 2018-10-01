package application;

import javafx.scene.paint.Color;

/**
 * TableData is used for storing all data needed to create a Table
 */

public class TableData implements Data {

    private Color colour;
    private Long width;
    private Long height;
    private Double friction;

    public TableData(Color colour, Long width, Long height, Double friction) {
        this.colour = colour;
        this.width = width;
        this.height = height;
        this.friction = friction;
    }

    /* Setter */
    public void setColour(Color colour) { this.colour = colour; }

    public void setWidth(Long width) { this.width = width; }

    public void setHeight(Long height) { this.height = height; }

    public void setFriction(Double friction) { this.friction = friction; }

    /* Getter */
    public Color getColour() { return colour; }

    public Long getWidth() { return width; }

    public Long getHeight() { return height; }

    public Double getFriction() { return friction; }
}
