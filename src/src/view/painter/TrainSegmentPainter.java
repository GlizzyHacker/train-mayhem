package view.painter;

import model.Colors;
import model.TrainSegment;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Draws a TrainSegment
 * Requires a {@link LevelPainter} to manage it
 */
public class TrainSegmentPainter extends ModelPainter<TrainSegment> {
    /**
     * Mouse listener that listens to mouse releases and presses to determine whether the mouse is currently pressed down over the segment
     */
    class TrainMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            pressed = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            pressed = false;
        }
    }

    boolean pressed;

    Point previousLocation;
    Point actualLocation;

    /**
     * Helper method that turns Colors enum to a color usable by swing
     */
    public static Color getColorForTrain(Colors col) {
        Color color;
        switch (col) {
            case BLUE:
                color = Color.blue;
                break;
            case GREEN:
                color = Color.green;
                break;
            case RED:
                color = Color.RED;
                break;
            case WHITE:
                color = Color.white;
                break;
            default:
                color = Color.black;
                break;
        }
        return color;
    }

    public TrainSegmentPainter(TrainSegment segment, LevelPainter levelPainter) {
        super(segment, levelPainter);
        setBounds(0, 0, levelPainter.getScale(), levelPainter.getScale());
        addMouseListener(new TrainMouseListener());
    }

    /**
     * Draws the segment with the trains color
     */
    @Override
    public void paint(Graphics g) {
        if (component.getTrain().isReachedEnd()) {
            return;
        }
        Color color = getColorForTrain(component.getTrain().getColor());
        g.setColor(color);
        if (previousLocation != null) {
            drawSegment(g, 0, previousLocation.y - getLocation().y + (levelPainter.getScale() / 2), getWidth(), actualLocation.y - getLocation().y + levelPainter.getScale() / 2);
        }
    }

    /**
     * Moves the segment to the given position
     * It uses this to draw the segment between the new position and the old position
     *
     * @param x The level coordinate x
     * @param y The level coordinate y
     */
    public void moveToLocation(int x, int y) {
        //For some reason this function is called more than once for each move so this guards against overwriting the previous location
        if (actualLocation != null && actualLocation.x == x && actualLocation.y == y) {
            return;
        }
        previousLocation = actualLocation;
        actualLocation = new Point(x, y);

        if (previousLocation == null) {
            previousLocation = actualLocation;
        }
        setBounds(x, Math.min(previousLocation.y, y), levelPainter.getScale(), levelPainter.getScale() + Math.abs(previousLocation.y - y));

        if (pressed) {
            component.getTrain().stopFor(1);
        }
    }

    /**
     * Helper method to draw a train segment between two coordinates.
     * Segments are drawn as a rectangle between two triangles.
     * Leading segments are given a darker color.
     */
    void drawSegment(Graphics g, int x1, int y1, int x2, int y2) {
        Polygon polygon = new Polygon();
        int width = levelPainter.getScale() / 4;
        int length = levelPainter.getScale();

        //angle between segment and the vertical axis
        double alpha = y2 - y1 == 0 ? Math.PI / 2 : Math.atan((double) (x2 - x1) / (y2 - y1));

        //length of the diagonal between the two points
        double diagonal = length / Math.sin(alpha);

        //distance between the point and the start of the segment
        double a = (diagonal - length) / 2;

        //positions the segment in the middle of the diagonal
        Point start = new Point(x1 + (int) (a * Math.sin(alpha)), y1 + (int) (a * Math.cos(alpha)));
        Point end = new Point(x2 - (int) (a * Math.sin(alpha)), y2 - (int) (a * Math.cos(alpha)));

        double triangleHeight = (double) length / 6;

        //the angle of the triangle
        double beta = Math.atan(((double) width / 2) / triangleHeight);

        //the length of the triangles sides
        double b = (double) width / 2 / Math.sin(beta);

        //first triangle offsets
        int firstX = (int) (b * Math.cos(alpha - beta));
        int firstY = (int) (b * Math.sin(alpha - beta));

        //second triangle offsets
        int secondX = (int) (b * Math.cos(Math.PI / 2 - alpha - beta));
        int secondY = (int) (b * Math.sin(Math.PI / 2 - alpha - beta));

        polygon.addPoint(start.x, start.y);
        polygon.addPoint(start.x + firstY, start.y + firstX);
        polygon.addPoint(end.x - secondX, end.y - secondY);
        polygon.addPoint(end.x, end.y);
        polygon.addPoint(end.x - firstY, end.y - firstX);
        polygon.addPoint(start.x + secondX, start.y + secondY);
        polygon.addPoint(start.x, start.y);

        if (component.isLeading()) {
            g.setColor(g.getColor().darker());
        }
        g.fillPolygon(polygon);
    }
}
