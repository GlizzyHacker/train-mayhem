package model;

public class Exit extends DotComponent implements Navigable {
    public Exit(Coordinates position) {
        super(position);
    }

    @Override
    public Navigable navigate(TrainSegment o) throws OccupiedException {
        return null;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
