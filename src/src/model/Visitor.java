package model;

/**
 * An interface for providing Component specific behaviour.
 * Every subclass of LevelComponent calls their respective method in this interface if this is passed in the accept method.
 */
public interface Visitor {
    void visit(Entrance component);
    void visit(Exit component);
    void visit(Platform component);
    void visit(Switch component);
    void visit(Track component);
    void visit(Junction component);
}
