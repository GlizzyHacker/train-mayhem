package view.painter;

import model.Track;
import model.TrainSegment;

import java.awt.*;
import java.util.Map;

public class TrackComponent extends ComponentPainter<Track> {
    public TrackComponent(Track component, LevelPainter levelPainter) {
        super(component, levelPainter);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawTrack(g, 0, levelPainter.getScale()/2, getWidth(), levelPainter.getScale()/2);
        for (Map.Entry<Integer, TrainSegment> entry : component.getObjectsOnTrack().entrySet()) {
            levelPainter.addSegmentPositition(entry.getValue(), component.getTopLeftCorner().transform(entry.getKey(),0));
        }
    }
}
