package model;

import java.util.LinkedList;
import java.util.List;

/**
 * A level made up of {@link LevelComponent}
 */
public class Level {
    protected final String name;
    protected final List<LevelComponent> components;
    protected final List<Train> trains;
    protected final int width;
    protected final int height;

    /**
     * @param name The name of the level
     * @param width The width of the level
     * @param height The height of the level
     * @param components The components that make up the level
     */
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

    /**
     * Verifies each component in the level
     * @throws ComponentConstraintException if one of the components' constraints are violated
     */
    public void verify() throws ComponentConstraintException {
        for (LevelComponent component : components) {
            component.verifyConstraints(this);
        }
    }

    /** Finds the component occupying the given coordinates
     * @param coordinates The coordinates to look for
     * @return The component at the coordinates. If there are multiple, then the returned component is arbitrary.
     */
    public LevelComponent getComponentAt(Coordinates coordinates) {
        for (LevelComponent component : components) {
            if (component.getTopLeftCorner().x() + component.getWidth() > coordinates.x() && component.getTopLeftCorner().x() <= coordinates.x() && component.getTopLeftCorner().y() + component.getHeight() > coordinates.y() && component.getTopLeftCorner().y() <= coordinates.y()) {
                return component;
            }
        }
        return null;
    }


    /**
     * Finds all the components that are at least partially in the given rectangle
     * @param coordinates The position of the top left corner of the rectangle
     * @param width The width of the rectangle
     * @param height The height of the rectangle
     * @return A list of components inside the rectangle
     */
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
