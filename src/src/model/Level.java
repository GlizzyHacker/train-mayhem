package model;

import java.util.LinkedList;
import java.util.List;

public class Level {
    protected final String name;
    protected final List<LevelComponent> components;
    protected final List<Train> trains;
    protected final int width;
    protected final int height;

    public Level(String name, int width, int height, List<LevelComponent> components) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.components = components;
        this.trains = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<LevelComponent> getComponents() {
        return components;
    }

    public List<Train> getTrains() {
        return trains;
    }

    public void verify() throws ComponentConstraintException {
        for (LevelComponent component : components) {
            component.verifyConstraints(this);
        }
    }

    public LevelComponent getComponentAt(Coordinates coordinates) {
        for (LevelComponent component : components) {
            if (component.getTopLeftCorner().x() + component.getWidth() > coordinates.x() && component.getTopLeftCorner().x() <= coordinates.x() && component.getTopLeftCorner().y() + component.getHeight() > coordinates.y() && component.getTopLeftCorner().y() <= coordinates.y()) {
                return component;
            }
        }
        return null;
    }

    public List<LevelComponent> getComponentsInRect(Coordinates coordinates, int width, int height) {
        List<LevelComponent> inside = new LinkedList<>();
        for (LevelComponent component : components) {
            if (component.getTopLeftCorner().x() + component.getWidth() > coordinates.x() && component.getTopLeftCorner().x() < coordinates.x() + width && component.getTopLeftCorner().y() + component.getHeight() > coordinates.y() && component.getTopLeftCorner().y() < coordinates.y() + height) {
                inside.add(component);
            }
        }
        return inside;
    }
}
