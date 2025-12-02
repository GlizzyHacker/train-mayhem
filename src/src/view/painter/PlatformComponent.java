package view.painter;

import model.Platform;
import model.TrainSegment;

import java.awt.*;
import java.util.Map;

public class PlatformComponent extends ComponentPainter<Platform> {
    public PlatformComponent(Platform component, LevelPainter levelPainter) {
        super(component, levelPainter);
    }

    /**
     * Draws a track and a platform with the platform's color
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(TrainSegmentPainter.getColorForTrain(component.getColor()));
        g.fillRoundRect(0,0, getWidth(), Math.toIntExact(Math.round(levelPainter.getScale() * 0.4)), levelPainter.getScale()/4, levelPainter.getScale()/4);
        drawTrack(g, 0, levelPainter.getScale()/2, getWidth(), levelPainter.getScale()/2);
        for (Map.Entry<Integer, TrainSegment> entry : component.getSegmentsOnTrack().entrySet()) {
            levelPainter.addSegmentPositition(entry.getValue(), component.getTopLeftCorner().transform(entry.getKey(), 0));
        }
    }
}
