package model;

/**
 * Represents a point in a {@link Level}
 * @param x The horizontal component
 * @param y The vertical component
 */
public record Coordinates(
        int x,
        int y) {
    /**
     * Moves the point by the given offsets
     * @param dx The horizontal offset
     * @param dy The vertical offset
     * @return A new Coordinates object with the changed values
     */
    public Coordinates transform(int dx, int dy){
        return new Coordinates(x+dx, y+dy);
    }
}
