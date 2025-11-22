package model;

public class OccupiedException extends Exception {
    final TrainSegment occupiedBy;
    final Navigable where;

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
