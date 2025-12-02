package model;

import java.util.List;

/**
 * The component where trains will enter the level
 */
public class Entrance extends DotComponent implements Navigable {
    List<TrainArrival> trains;
    int countdown = 0;

    Navigable next;
    TrainSegment waiting;

    /**
     * @param position The position of the entrance
     * @param trains The train arrivals at this entrance
     */
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

    /**
     * Verifies that there is somewhere for the trains to go from here
     * @param level The level that the component is in
     * @throws ComponentConstraintException If this is a dead end
     */
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

    /**
     * The Entrance can be occupied by one segment at a time.
     * If this slot is filled then segments have to wait for that segment to move, only then can they enter the level.
     */
    public Navigable navigate(TrainSegment segment) throws OccupiedException {
        if (waiting == null){
            waiting = segment;
        }
        else if (waiting.equals(segment)){
            next.navigate(segment);
            waiting = null;
            return next;
        }
        return this;
    }

    /**
     * Advances the arrival countdown by one. If it reaches the arrival time offset of the next train, it will enter the level and adds it to the level's trains list.
     */
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
