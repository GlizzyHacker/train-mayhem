package model;

public class Platform extends Track {
    Colors color;

    public Platform(int width, Coordinates position, Colors color) {
        super(width, position);
        this.color = color;
    }

    public Colors getColor() {
        return color;
    }

    @Override
    public Navigable navigate(TrainSegment o) throws OccupiedException {
        Navigable result = super.navigate(o);
        TrainSegment segmentAtEnd = getObjectsOnTrack().get(getWidth()-1);
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
