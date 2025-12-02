package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Component representing a straight track
 */
public class Track extends LevelComponent implements Navigable {
    final Map<Integer, TrainSegment> segmentsOnTrack = new HashMap<>();
    Navigable next;

    public Track(int width, Coordinates position) {
        super(width, 1, position);
    }

    /** This component can hold multiple segments
     * @return The segments on track mapped to their horizontal offset from the top left corner.
     */
    public Map<Integer, TrainSegment> getSegmentsOnTrack() {
        return segmentsOnTrack;
    }

    /** Verifies that this is not a dead end
     * @param level The level that the component is a part of
     * @throws ComponentConstraintException If there is no next component
     */
    @Override
    public void verifyConstraints(Level level) throws ComponentConstraintException {
        super.verifyConstraints(level);

        LevelComponent component = level.getComponentAt(getTopLeftCorner().transform(getWidth(), 0));
        if (component instanceof Navigable navigable) {
            next = navigable;
        }

        if (next == null) {
            throw new ComponentConstraintException(this, "Dead end");
        }
    }

    /**
     * Navigates the segments entering and already on this component. Moves them along horizontally by one tile.
     * @param segment The segment that navigates this object
     * @return This if the segment remains on this track or the next component.
     * @throws OccupiedException If a segment navigates where another segment is already at.
     */
    @Override
    public Navigable navigate(TrainSegment segment) throws OccupiedException {
        Optional<Map.Entry<Integer, TrainSegment>> segmentEntry = segmentsOnTrack.entrySet().stream().filter(entry -> entry.getValue().equals(segment)).findFirst();

        int nextIndex = segmentEntry.isPresent() ? segmentEntry.get().getKey() + 1 : 0;
        if (nextIndex >= width) {
            segmentsOnTrack.remove(Math.max(nextIndex - 1, 0));
            next.navigate(segment);
            return next;
        }
        if (segmentsOnTrack.containsKey(nextIndex)) {
            throw new OccupiedException(segmentsOnTrack.get(nextIndex), this);
        }
        //nextIndex might be 0
        segmentsOnTrack.remove(Math.max(nextIndex - 1, 0));
        segmentsOnTrack.put(nextIndex, segment);
        return this;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
