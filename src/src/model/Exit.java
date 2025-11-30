package model;

public class Exit extends DotComponent implements Navigable {
    TrainSegment waiting;

    public Exit(Coordinates position) {
        super(position);
    }

    public TrainSegment getWaiting() {
        return waiting;
    }

    @Override
    public Navigable navigate(TrainSegment o) throws OccupiedException {
        if (waiting == null){
            waiting = o;
            return this;
        }
        if (waiting.equals(o)){
            waiting = null;
        }
        return null;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
