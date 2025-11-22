package model;

public abstract class LevelComponent {
    protected final int width;
    protected final int height;
    protected final Coordinates position;
    protected Level level;

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

    public abstract void accept(Visitor visitor);
}
