package view.painter;

import model.Entrance;

import java.awt.*;

public class EntranceComponent extends ComponentPainter<Entrance> {
    public EntranceComponent(Entrance component, LevelPainter levelPainter) {
        super(component, levelPainter);
    }

    /**
     * Draws a track and an indicator of when the next train is coming
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawTrack(g, 0, levelPainter.getScale() / 2, getWidth(), levelPainter.getScale() / 2);
        if (component.getWaiting() != null) {
            levelPainter.addSegmentPositition(component.getWaiting(), component.getTopLeftCorner());
        }
        if (component.getTrains().isEmpty()) {
            return;
        }
        int dots = Math.max(4 - (component.getTrains().getFirst().timeOffset - component.getCountdown()), 0);
        for (int i = 0; i < dots; i++) {
            g.setColor(TrainSegmentPainter.getColorForTrain(component.getTrains().getFirst().train.getColor()));
            g.fillOval(i * levelPainter.getScale() / 3, 0, levelPainter.getScale() / 4, levelPainter.getScale() / 4);
            g.setColor(Color.BLACK);
            g.drawOval(i * levelPainter.getScale() / 3, 0, levelPainter.getScale() / 4, levelPainter.getScale() / 4);
        }
    }
}
