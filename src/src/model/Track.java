package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Track extends LevelComponent implements Navigable {
    final Map<Integer, TrainSegment> segmentsOnTrack = new HashMap<>();
    Navigable next;

    public Track(int width, Coordinates position) {
        super(width, 1, position);
    }

    public Map<Integer, TrainSegment> getObjectsOnTrack() {
        return segmentsOnTrack;
    }

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

    @Override
    public Navigable navigate(TrainSegment o) throws OccupiedException {
        Optional<Map.Entry<Integer, TrainSegment>> segmentEntry = segmentsOnTrack.entrySet().stream().filter(entry -> entry.getValue().equals(o)).findFirst();

        int nextIndex = segmentEntry.isPresent() ? segmentEntry.get().getKey() + 1 : 0;
        if (nextIndex >= width) {
            segmentsOnTrack.remove(Math.max(nextIndex - 1, 0));
            next.navigate(o);
            return next;
        }
        if (segmentsOnTrack.containsKey(nextIndex)) {
            throw new OccupiedException(segmentsOnTrack.get(nextIndex), this);
        }
        //nextIndex might be 0
        segmentsOnTrack.remove(Math.max(nextIndex - 1, 0));
        segmentsOnTrack.put(nextIndex, o);
        return this;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
