package model;

import java.util.List;

/**
 * Component where two or more tracks converge into one
 */
public class Junction extends LevelComponent implements Navigable {
    Navigable next;
    TrainSegment segmentOnTrack;

    /**
     * @param position The coordinates of the top left corner
     * @param height The height of the component
     */
    public Junction(Coordinates position, int height) {
        super(1, height, position);
    }

    public TrainSegment getSegmentOnTrack() {
        return segmentOnTrack;
    }

    /**
     * Verifies that there is only a single exit
     * @param level The level that the component is a part of
     * @throws ComponentConstraintException If there is no or multiple exits
     */
    @Override
    public void verifyConstraints(Level level) throws ComponentConstraintException {
        List<LevelComponent> neighbours = level.getComponentsInRect(getTopLeftCorner().transform(1, 0), 1, getHeight());
        neighbours.removeIf(neighbour -> !(neighbour instanceof Navigable));
        if (neighbours.isEmpty()) {
            throw new ComponentConstraintException(this, "Dead end");
        }
        if (neighbours.size() > 1) {
            throw new ComponentConstraintException(this, "Ambiguous exit");
        }
        next = (Navigable) neighbours.getFirst();
    }

    /**
     * This component has room for one segment at a time
     * @param segment The segment that navigates this object
     * @return The next component
     * @throws OccupiedException If a segment navigates here if one is already inside
     */
    @Override
    public Navigable navigate(TrainSegment segment) throws OccupiedException {
        if (segmentOnTrack == null) {
            segmentOnTrack = segment;
            return this;
        }
        if (segment.equals(segmentOnTrack)) {
            next.navigate(segment);
            segmentOnTrack = null;
            return next;
        }
        throw new OccupiedException(segmentOnTrack, this);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
