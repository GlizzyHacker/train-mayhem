package model;

public interface Navigable {
    //returns null if reached end
    Navigable navigate(TrainSegment o) throws OccupiedException;
}
