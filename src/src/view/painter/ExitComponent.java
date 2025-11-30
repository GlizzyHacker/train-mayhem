package view.painter;

import model.Exit;

import java.awt.*;

public class ExitComponent  extends ComponentPainter<Exit> {
    public ExitComponent(Exit component, LevelPainter levelPainter) {
        super(component, levelPainter);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawTrack(g, 0,levelPainter.getScale()/2, getWidth(), levelPainter.getScale()/2);
        if (component.getWaiting() != null) {
            levelPainter.addSegmentPositition(component.getWaiting(), component.getTopLeftCorner());
        }
    }
}
