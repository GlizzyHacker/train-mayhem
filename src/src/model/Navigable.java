package model;

/**
 * Interface for {@link LevelComponent} that support {@link TrainSegment} moving through it.
 */
public interface Navigable {
    /**
     * @param segment The segment that navigates this object
     * @return The component that the segment will be in after the move. Returns null only if the segment reached the end of the track
     * @throws OccupiedException If where the segment moves is occupied by another segment
     */
    Navigable navigate(TrainSegment segment) throws OccupiedException;
}
