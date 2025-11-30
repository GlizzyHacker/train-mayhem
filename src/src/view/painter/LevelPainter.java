package view.painter;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LevelPainter extends JPanel implements Visitor {
    class LevelPainterMouseListener extends MouseAdapter{
        //If this is called it means no component captured the mouse event
        @Override
        public void mouseClicked(MouseEvent e) {
            LevelSelectionEvent event = new LevelSelectionEvent(new Coordinates(e.getX()/scale, e.getY()/scale), null, null, e);
            for (LevelSelectionListener listener : listeners) {
                listener.sendSelected(event);
            }
        }
    }
    private final Level level;
    private boolean gridVisible;
    private LevelComponent highlighted;
    private boolean allowInteract = true;
    List<LevelSelectionListener> listeners = new LinkedList<>();

    private int scale = 40;
    private final Map<TrainSegment, TrainSegmentPainter> segmentPainters = new HashMap<>();


    public LevelPainter(Level level) {
        this.level = level;
        this.setLayout(null);
        this.setMinimumSize(new Dimension((level.getWidth()) * scale, (level.getHeight()) * scale));
        this.setPreferredSize(getMinimumSize());
        this.addMouseListener(new LevelPainterMouseListener());
        for (LevelComponent component : level.getComponents()) {
            addComponent(component);
        }
    }

    public int getScale() {
        return scale;
    }

    public void setGridVisible(boolean val) {
        gridVisible = val;
        repaint();
    }

    public boolean getAllowInteract(){
        return allowInteract;
    }

    public void setAllowInteract(boolean val){
        allowInteract = val;
    }

    public void setHighlighted(LevelComponent component){
        highlighted = component;
        repaint();
    }

    public void addSegmentPositition(TrainSegment segment, Coordinates coordinates) {
        if (!segmentPainters.containsKey(segment)) {
            TrainSegmentPainter segmentPainter = new TrainSegmentPainter(segment, this);
            this.add(segmentPainter);
            //Draw on top of level components
            setComponentZOrder(segmentPainter, 0);
            segmentPainters.put(segment, segmentPainter);
        }
        segmentPainters.get(segment).moveToLocation(coordinates.x() * scale, coordinates.y() * scale);
    }

    public void addSelectionListener(LevelSelectionListener listener) {
        listeners.add(listener);
    }

    public void addComponent(LevelComponent component){
        component.accept(this);
        repaint();
    }

    //WHAT happens to trains?
    public void removeComponent(LevelComponent component){
        for(Component child : getComponents()){
            if (child instanceof ComponentPainter<?> componentPainter && componentPainter.component.equals(component)){
                    this.remove(componentPainter);
                }

        }
        repaint();
    }

    void addComponentPainter(ComponentPainter<?> painter) {
        painter.addSelectionListener(event -> listeners.forEach(l -> l.sendSelected(event)));
        this.add(painter);
    }

    @Override
    public void paint(Graphics g) {
        //DYNAMIC RESIZE
        scale = Math.max(40, Math.min(getHeight()/level.getHeight(), getWidth()/level.getWidth()));

        super.paint(g);
        if (gridVisible) {
            g.drawRect(0, 0, level.getWidth() * scale, level.getHeight() * scale);
            for (int i = 0; i < level.getWidth(); i++) {
                for (int j = 0; j < level.getHeight(); j++) {
                    g.drawOval(i * scale, j * scale, 1, 1);
                }
            }
        }
        if (highlighted != null){
            g.setColor(Color.pink);
            g.drawRect((highlighted.getTopLeftCorner().x() * scale), highlighted.getTopLeftCorner().y() * scale, highlighted.getWidth() * scale, highlighted.getHeight() * scale);
        }
    }

    @Override
    public void visit(Entrance component) {
        this.addComponentPainter(new EntranceComponent(component, this));
    }

    @Override
    public void visit(Exit component) {
        this.addComponentPainter(new ExitComponent(component, this));
    }

    @Override
    public void visit(Platform component) {
        this.addComponentPainter(new PlatformComponent(component, this));
    }

    @Override
    public void visit(Switch component) {
        boolean[] entrances = new boolean[component.getHeight()];
        for (int i = 0; i < entrances.length; i++) {
            LevelComponent neighbour = level.getComponentAt(component.getTopLeftCorner().transform(-1, i));
            if (neighbour instanceof Navigable) {
                entrances[i] = true;
            }
        }
        this.addComponentPainter(new SwitchComponent(component, this, entrances));
    }

    @Override
    public void visit(Track component) {
        this.addComponentPainter(new TrackComponent(component, this));
    }

    @Override
    public void visit(Junction component) {
        boolean[] entrances = new boolean[component.getHeight()];
        int exitY = 0;
        for (int i = 0; i < entrances.length; i++) {
            LevelComponent neighbourLeft = level.getComponentAt(component.getTopLeftCorner().transform(-1, i));
            LevelComponent neighbourRight = level.getComponentAt(component.getTopLeftCorner().transform(1, i));
            if (neighbourLeft instanceof Navigable) {
                entrances[i] = true;
            }
            if (neighbourRight instanceof Navigable) {
                exitY = i;
            }
        }
        this.addComponentPainter(new JunctionComponent(component, this, exitY, entrances));
    }
}
