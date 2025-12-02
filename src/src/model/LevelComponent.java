package model;

/**
 * Component of a {@link Level}
 * This is an abstract class. Subclasses inherit from this to provide unique behaviour
 */
public abstract class LevelComponent {
    protected final int width;
    protected final int height;
    protected final Coordinates position;
    protected Level level;

    /**
     * Creates a new component
     * @param width The width of the component
     * @param height The height of the component
     * @param position The coordinates where the top left corner of the component is
     */
    LevelComponent(int width, int height, Coordinates position){
        this.width = width;
        this.height = height;
        this.position=position;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Coordinates getTopLeftCorner(){
        return position;
    }

    /**
     * Components may have specific requirement that depend on other components near them.
     * This method allows a level to make sure all the components inside them have their constraints met.
     * By default, all components must fit in the level
     * @param level The level that the component is a part of
     * @throws ComponentConstraintException If a constraint is violated
     */
    public void verifyConstraints(Level level) throws ComponentConstraintException {
        this.level = level;

        if (width <= 0){
            throw new ComponentConstraintException(this, "Width must be larger than 0");
        }

        if (height <= 0){
            throw new ComponentConstraintException(this, "Height must be larger than 0");
        }

        if (position.x() + width > level.getWidth()){
            throw new ComponentConstraintException(this, "Component doesn't fit on map");
        }

        if (position.y() + height > level.getHeight()){
            throw new ComponentConstraintException(this, "Component doesn't fit on map");
        }
    }

    /** Every subclass has to implement this and visit the visitor with themselves as their subclass
     * @param visitor The visitor to visit
     */
    public abstract void accept(Visitor visitor);
}
