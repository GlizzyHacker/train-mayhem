package view.painter;

import model.LevelComponent;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

public abstract class ComponentPainter<T extends LevelComponent> extends ModelPainter<T> {
    class ComponentMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            for (LevelSelectionListener listener : listeners) {
                listener.sendSelected(new LevelSelectionEvent(component.getTopLeftCorner(), component, null, e));
            }
        }
    }

    transient List<LevelSelectionListener> listeners = new LinkedList<>();

    public ComponentPainter(T component, LevelPainter levelPainter) {
        super(component, levelPainter);
        this.setBounds(component.getTopLeftCorner().x() * levelPainter.getScale(), component.getTopLeftCorner().y() * levelPainter.getScale(), component.getWidth() * levelPainter.getScale(), component.getHeight() * levelPainter.getScale());
        addMouseListener(new ComponentMouseListener());
    }

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

    static void drawTrack(Graphics g, Color color, int x1, int y1, int x2, int y2, int thickness) {
        double alpha = y2-y1 == 0 ? Math.PI/2 : Math.atan((double) (x2 - x1) /(y2-y1));

        int x = (int) ((double) thickness /2 * Math.cos(alpha));
        int y = (int) ((double) thickness /2 * Math.sin(alpha));

        g.setColor(color);

        Polygon polygon = new Polygon();
        polygon.addPoint(x1 + x, y1 - y);
        polygon.addPoint(x1 - x, y1 + y);
        polygon.addPoint(x2 - x, y2 + y);
        polygon.addPoint(x2 + x, y2 - y);
        polygon.addPoint(x1 + x, y1 - y);
        g.fillPolygon(polygon);

        Polygon fill1 = new Polygon();
        fill1.addPoint(x1, y1 - thickness/2);
        fill1.addPoint(x1 + x, y1 - y);
        fill1.addPoint(x1, y1 + thickness/2);
        fill1.addPoint(x1 - x, y1 + y);
        fill1.addPoint(x1, y1 - thickness/2);
        g.fillPolygon(fill1);

        Polygon fill2 = new Polygon();
        fill2.addPoint(x2, y2 - thickness/2);
        fill2.addPoint(x2 + x, y2 - y);
        fill2.addPoint(x2, y2 + thickness/2);
        fill2.addPoint(x2 - x, y2 + y);
        fill2.addPoint(x2, y2 - thickness/2);
        g.fillPolygon(fill2);
    }

    void drawTrack(Graphics g, int x1, int y1, int x2, int y2) {
        drawTrack(g, Color.orange, x1, y1, x2, y2, levelPainter.getScale() / 6);
    }

    @Override
    public void paint(Graphics g) {
        //DYNAMIC RESIZE
        this.setBounds(component.getTopLeftCorner().x() * levelPainter.getScale(), component.getTopLeftCorner().y() * levelPainter.getScale(), component.getWidth() * levelPainter.getScale(), component.getHeight() * levelPainter.getScale());
    }
}
