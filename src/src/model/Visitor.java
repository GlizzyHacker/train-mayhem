package model;

public interface Visitor {
    void visit(Entrance component);
    void visit(Exit component);
    void visit(Platform component);
    void visit(Switch component);
    void visit(Track component);
    void visit(Junction component);
}
