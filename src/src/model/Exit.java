package model;

/**
 * The component where trains leave the level
 */
public class Exit extends DotComponent implements Navigable {
    TrainSegment waiting;

    public Exit(Coordinates position) {
        super(position);
    }

    public TrainSegment getWaiting() {
        return waiting;
    }

    /**
     * Acts as a sing for train segments. Has room for one segment at a time
     * @param segment The segment that navigates this object
     * @return This if there is room for a segment or null if the segment is already on this.
     */
    @Override
    public Navigable navigate(TrainSegment segment) {
        if (waiting == null){
            waiting = segment;
            return this;
        }
        if (waiting.equals(segment)){
            waiting = null;
        }
        return null;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
