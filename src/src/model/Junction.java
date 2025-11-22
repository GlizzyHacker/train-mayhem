package model;

import java.util.List;

public class Junction extends LevelComponent implements Navigable {
    Navigable next;
    TrainSegment segmentOnTrack;

    public Junction(Coordinates position, int height) {
        super(1, height, position);
    }

    public TrainSegment getSegmentOnTrack() {
        return segmentOnTrack;
    }

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

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

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
}
