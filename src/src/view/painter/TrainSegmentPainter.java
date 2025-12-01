package view.painter;

import model.Colors;
import model.TrainSegment;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TrainSegmentPainter extends ModelPainter<TrainSegment> {
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

    @Override
    public void paint(Graphics g) {
        if (component.getTrain().isReachedEnd()){
            return;
        }
        Color color = getColorForTrain(component.getTrain().getColor());
        g.setColor(color);
        if (previousLocation != null) {
            drawSegment(g, 0, previousLocation.y - getLocation().y + (levelPainter.getScale() / 2), getWidth(), actualLocation.y - getLocation().y + levelPainter.getScale() / 2);
        }
    }

    public void moveToLocation(int x, int y) {
        //For some reason this function is called more than once for each move so this guards against overriding the previous location
        if (actualLocation != null && actualLocation.x == x && actualLocation.y == y) {
            return;
        }
        previousLocation = actualLocation;
        actualLocation = new Point(x, y);

        if (previousLocation == null) {
            previousLocation = actualLocation;
        }
        setBounds(x, Math.min(previousLocation.y, y), levelPainter.getScale(), levelPainter.getScale() + Math.abs(previousLocation.y - y));

        if (pressed){
            component.getTrain().stopFor(1);
        }
    }

    void drawSegment(Graphics g, int x1, int y1, int x2, int y2) {
        Polygon polygon = new Polygon();
        int width = levelPainter.getScale()/4;
        int length = levelPainter.getScale();
        double alpha = y2-y1 == 0 ? Math.PI/2 : Math.atan((double) (x2 - x1) /(y2-y1));
        double diagonal = length/Math.sin(alpha);
        double a = (diagonal-length)/2;
        Point start = new Point(x1 + (int) (a*Math.sin(alpha)), y1 + (int) (a*Math.cos(alpha)));
        Point end = new Point(x2- (int) (a*Math.sin(alpha)),y2- (int) (a*Math.cos(alpha)));

        double beta = Math.atan(((double) width / 2) / ((double) length /6));
        double b = (double) width/2 / Math.sin(beta);
        int bottomX = (int) (b * Math.cos(alpha - beta));
        int bottomY = (int) (b * Math.sin(alpha - beta));

        int topX = (int) (b * Math.cos(Math.PI/2 - alpha - beta));
        int topY = (int) (b * Math.sin(Math.PI/2 - alpha - beta));

        polygon.addPoint(start.x, start.y);
        polygon.addPoint(start.x + bottomY, start.y + bottomX);
        polygon.addPoint(end.x - topX, end.y - topY);
        polygon.addPoint(end.x, end.y);
        polygon.addPoint(end.x - bottomY, end.y - bottomX);
        polygon.addPoint(start.x + topX, start.y + topY);
        polygon.addPoint(start.x,start.y);

        if (component.isLeading()) {
            g.setColor(g.getColor().darker());
        }
        g.fillPolygon(polygon);
    }
}
