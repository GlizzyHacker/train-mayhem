package view.painter;

import model.OccupiedException;
import model.Switch;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SwitchComponent extends ComponentPainter<Switch> {
    class SwitchMouseListener extends MouseAdapter {
        /**
         * Switches the current exit when clicked
         * Increases it until it overflows to 0
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                component.setCurrentExit((component.getCurrentExit() + 1) % component.getHeight());
                repaint();
            } catch (OccupiedException _) {
                //Maybe play a sound effect
            }
        }
    }

    boolean[] entrances;

    /**
     * @param entrances List as long as the junction's height where the i. value is true if there is an entrance at the i vertical offset compared to the junction's location
     */
    public SwitchComponent(Switch component, LevelPainter levelPainter, boolean[] entrances) {
        super(component, levelPainter);
        addMouseListener(new SwitchMouseListener());

        this.entrances = entrances;
    }

    /**
     * Draws a track between the entrances and the selected exit
     * Draws a gray track between the entrances and the other exits
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        //Even if switch is not connected to anything still show something
        boolean hasEntrance = false;
        for (boolean entrance : entrances) {
            if (entrance) {
                hasEntrance = true;
                break;
            }
        }

        for (int i = 0; i < entrances.length; i++) {
            if (entrances[i] || !hasEntrance) {
                for (int j = 0; j < component.getHeight(); j++) {
                    drawTrack(g, component.getCurrentExit() == j ? Color.orange : Color.gray, 0, i * levelPainter.getScale() + levelPainter.getScale() / 2, getWidth(), j * levelPainter.getScale() + levelPainter.getScale() / 2, levelPainter.getScale() / 6);
                }
            }
        }
        //use width for height too because height changes with the number of exits
        if (component.getSegmentOnTrack() != null) {
            levelPainter.addSegmentPositition(component.getSegmentOnTrack(), component.getTopLeftCorner().transform(0, component.getCurrentExit()));
        }
    }
}
