package model;

public record Coordinates(
        int x,
        int y) {
    public Coordinates transform(int dx, int dy){
        return new Coordinates(x+dx, y+dy);
    }
}
