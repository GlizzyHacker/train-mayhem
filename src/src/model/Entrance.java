package model;

import java.util.List;

public class Entrance extends DotComponent implements Navigable {
    List<TrainArrival> trains;
    int countdown;

    Navigable next;
    Object waiting;

    public Entrance(Coordinates position, List<TrainArrival> trains) {
        super(position);
        this.trains = trains;
        countdown = trains.isEmpty() ?0: trains.getFirst().timeOffset;
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
        countdown--;
        if (countdown <= 0 && !trains.isEmpty()){
            TrainArrival arrival = trains.getFirst();
            trains.removeFirst();
            Train newTrain = arrival.possibleTrains.getFirst();
            level.getTrains().add(newTrain);
            if (!trains.isEmpty()){
                countdown = trains.getFirst().timeOffset;
            }
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
