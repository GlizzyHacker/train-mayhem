package model;

import java.util.List;

public class Entrance extends DotComponent implements Navigable {
    List<TrainArrival> trains;
    int countdown = 0;

    Navigable next;
    TrainSegment waiting;

    public Entrance(Coordinates position, List<TrainArrival> trains) {
        super(position);
        this.trains = trains;
    }

    public int getCountdown() {
        return countdown;
    }

    public List<TrainArrival> getTrains() {
        return trains;
    }

    public TrainSegment getWaiting() {
        return waiting;
    }

    @Override
    public void verifyConstraints(Level level) throws ComponentConstraintException {
        super.verifyConstraints(level);

        LevelComponent component = level.getComponentAt(getTopLeftCorner().transform(1,0));
        if ( component instanceof Navigable navigable) {
            next = navigable;
        }
        else {
            throw new ComponentConstraintException(this, "Dead end");
        }
    }

    public Navigable navigate(TrainSegment o) throws OccupiedException {
        if (waiting == null){
            waiting = o;
        }
        else if (waiting.equals(o)){
            next.navigate(o);
            waiting = null;
            return next;
        }
        return this;
    }

    public void step() {
        countdown++;
        if (!trains.isEmpty() && countdown >= trains.getFirst().timeOffset){
            TrainArrival arrival = trains.getFirst();
            trains.removeFirst();
            Train newTrain = arrival.train;
            level.getTrains().add(newTrain);
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
