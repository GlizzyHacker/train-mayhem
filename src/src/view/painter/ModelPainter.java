package view.painter;

import javax.swing.*;
import java.awt.*;

/**
 * Superclass for drawing a {@link model.LevelComponent} or a {@link model.TrainSegment} with low level graphics
 * @param <T>
 */
public abstract class ModelPainter<T> extends JComponent {
    T component;
    LevelPainter levelPainter;

    public ModelPainter(T component, LevelPainter levelPainter){
        this.component = component;
        this.levelPainter = levelPainter;
    }

    @Override
    public abstract void paint(Graphics g);
}
