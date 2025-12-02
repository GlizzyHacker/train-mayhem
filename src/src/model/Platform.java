package model;

/**
 * A component where trains may stop. Functions like a track
 */
public class Platform extends Track {
    Colors color;

    /**
     * @param width The width of the component
     * @param position The top left corner of the component
     * @param color The color of the platform
     */
    public Platform(int width, Coordinates position, Colors color) {
        super(width, position);
        this.color = color;
    }

    public Colors getColor() {
        return color;
    }

    /**
     * Functions like a {@link Track} except it tells the train it arrived at the platform when their leading segments reach the platform end.
     */
    @Override
    public Navigable navigate(TrainSegment segment) throws OccupiedException {
        Navigable result = super.navigate(segment);
        TrainSegment segmentAtEnd = getSegmentsOnTrack().get(getWidth()-1);
        if (segmentAtEnd != null && segmentAtEnd.isLeading()){
            segmentAtEnd.getTrain().arrivedAtPlatform(this);
        }
        return result;
    }

    @Override
    public void accept(Visitor visitor) {
         visitor.visit(this);
    }
}
