package model;

/**
 * Exception thrown by {@link Navigable} components if a segment navigates where another one is already present
 */
public class OccupiedException extends Exception {
    final TrainSegment occupiedBy;
    final Navigable where;

    /**
     * @param occupiedBy The segment that is already present
     * @param where The component where this happened
     */
    OccupiedException(TrainSegment occupiedBy, Navigable where){
        this.occupiedBy = occupiedBy;
        this.where = where;
    }

    public Navigable getWhere() {
        return where;
    }

    public TrainSegment getOccupiedBy() {
        return occupiedBy;
    }
}
