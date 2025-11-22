package model;

public class ComponentConstraintException extends Exception {
    final LevelComponent component;

    ComponentConstraintException(LevelComponent component, String message){
        super(message);
        this.component = component;
    }

    public LevelComponent getComponent() {
        return component;
    }
}
