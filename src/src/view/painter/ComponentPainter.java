package view.painter;

import model.LevelComponent;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

/** Abstract class for painting a level component with low level graphics
 * Must be managed by a {@link LevelPainter} that is the direct parent of this
 * @param <T> The component to paint
 */
public abstract class ComponentPainter<T extends LevelComponent> extends ModelPainter<T> {
    /**
     * A mouse listener for selecting the component
     * Subclasses should use this to add interactivity to components
     */
    class ComponentMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            for (LevelSelectionListener listener : listeners) {
                listener.sendSelected(new LevelSelectionEvent(component.getTopLeftCorner(), component, null, e));
            }
        }
    }

    transient List<LevelSelectionListener> listeners = new LinkedList<>();

    /**
     * Creates a new component painter
     * @param component The component to paint
     * @param levelPainter The level painter managing this painter
     */
    public ComponentPainter(T component, LevelPainter levelPainter) {
        super(component, levelPainter);
        this.setBounds(component.getTopLeftCorner().x() * levelPainter.getScale(), component.getTopLeftCorner().y() * levelPainter.getScale(), component.getWidth() * levelPainter.getScale(), component.getHeight() * levelPainter.getScale());
        addMouseListener(new ComponentMouseListener());
    }

    /**
     * Same as the superclass implementation except that when the parent levelPainter doesn't allow interactions it ignores all ComponentMouseListeners
     * @param e the mouse event
     */
    @Override
    protected void processMouseEvent(MouseEvent e) {
        //NOT A VERY CLEAN SOLUTION
        MouseListener[] oldListeners = getMouseListeners();
        if (!levelPainter.getAllowInteract()) {
            for (MouseListener oldListener : oldListeners) {
                if (!(oldListener instanceof ComponentPainter.ComponentMouseListener)) {
                    removeMouseListener(oldListener);
                }
            }
        }
        super.processMouseEvent(e);
        if (!levelPainter.getAllowInteract()) {
            for (MouseListener oldListener : oldListeners) {
                if (!(oldListener instanceof ComponentPainter.ComponentMouseListener)) {
                    addMouseListener(oldListener);
                }
            }
        }
    }

    public void addSelectionListener(LevelSelectionListener listener) {
        listeners.add(listener);
    }

    /**
     * Helper class to draw a track between two points
     * Draws a fixed width line between the points at any angle
     * @param color The color of the track
     * @param thickness The width of the track
     */
    static void drawTrack(Graphics g, Color color, int x1, int y1, int x2, int y2, int thickness) {
        //Angle between the track and the vertical axis
        double alpha = y2-y1 == 0 ? Math.PI/2 : Math.atan((double) (x2 - x1) /(y2-y1));

        //The x offset from the points
        int x = (int) ((double) thickness /2 * Math.cos(alpha));
        //The y offset from the points
        int y = (int) ((double) thickness /2 * Math.sin(alpha));

        g.setColor(color);

        //The line
        Polygon polygon = new Polygon();
        polygon.addPoint(x1 + x, y1 - y);
        polygon.addPoint(x1 - x, y1 + y);
        polygon.addPoint(x2 - x, y2 + y);
        polygon.addPoint(x2 + x, y2 - y);
        polygon.addPoint(x1 + x, y1 - y);
        g.fillPolygon(polygon);

        //Extra polygon to fill gaps between straight and angled tracks
        Polygon fill1 = new Polygon();
        fill1.addPoint(x1, y1 - thickness/2);
        fill1.addPoint(x1 + x, y1 - y);
        fill1.addPoint(x1, y1 + thickness/2);
        fill1.addPoint(x1 - x, y1 + y);
        fill1.addPoint(x1, y1 - thickness/2);
        g.fillPolygon(fill1);

        //Extra polygon to fill gaps between straight and angled tracks
        Polygon fill2 = new Polygon();
        fill2.addPoint(x2, y2 - thickness/2);
        fill2.addPoint(x2 + x, y2 - y);
        fill2.addPoint(x2, y2 + thickness/2);
        fill2.addPoint(x2 - x, y2 + y);
        fill2.addPoint(x2, y2 - thickness/2);
        g.fillPolygon(fill2);
    }

    /**
     * Draws a track with fewer arguments
     * Uses default color and width
     */
    void drawTrack(Graphics g, int x1, int y1, int x2, int y2) {
        drawTrack(g, Color.orange, x1, y1, x2, y2, levelPainter.getScale() / 6);
    }

    /**
     * Resizes the component at every paint to stay accurate when the level painter is resized by the user
     */
    @Override
    public void paint(Graphics g) {
        //DYNAMIC RESIZE
        this.setBounds(component.getTopLeftCorner().x() * levelPainter.getScale(), component.getTopLeftCorner().y() * levelPainter.getScale(), component.getWidth() * levelPainter.getScale(), component.getHeight() * levelPainter.getScale());
    }
}
