package view.painter;

import model.Junction;

import java.awt.*;

public class JunctionComponent extends ComponentPainter<Junction> {
    int exitY;
    boolean[] entrances;

    /**
     * @param exitY The vertical offset of the exit compared to the junction's location
     * @param entrances List as long as the junction's height where the i. value is true if there is an entrance at the i vertical offset compared to the junction's location
     */
    public JunctionComponent(Junction component, LevelPainter levelPainter, int exitY, boolean[] entrances) {
        super(component, levelPainter);
        this.exitY = exitY;
        this.entrances = entrances;
    }

    /**
     * Draws a track between every entrance and the exit
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.orange);

        //Even if junction is not connected to anything still show something
        boolean hasEntrance = false;
        for (boolean entrance : entrances) {
            if (entrance) {
                hasEntrance = true;
                break;
            }
        }

        for (int i = 0; i < entrances.length; i++) {
            if (entrances[i] || !hasEntrance) {
                drawTrack(g, 0, i * levelPainter.getScale() + levelPainter.getScale() / 2, getWidth(), exitY * levelPainter.getScale() + levelPainter.getScale() / 2);
            }
        }
        if (component.getSegmentOnTrack() != null) {
            levelPainter.addSegmentPositition(component.getSegmentOnTrack(), component.getTopLeftCorner().transform(0, exitY));
        }
    }
}
