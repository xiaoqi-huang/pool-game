package application;

public class TableData implements Data {

    private String colour;
    private Long x;
    private Long y;
    private Double friction;

    public TableData(String colour, Long x, Long y, Double friction) {
        this.colour = colour;
        this.x = x;
        this.y = y;
        this.friction = friction;
    }

    public void setColour(String colour) {
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

    public String getColour() {
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
