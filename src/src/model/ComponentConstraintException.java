package model;

/**
 * An exception thrown by {@link LevelComponent}'s verify method
 */
public class ComponentConstraintException extends Exception {
    final LevelComponent component;

    /**
     * Creates the exception with all the necessary information
     * @param component the component that caused the exception
     * @param message the message describing the cause
     */
    ComponentConstraintException(LevelComponent component, String message){
        super(message);
        this.component = component;
    }

    public LevelComponent getComponent() {
        return component;
    }
}
