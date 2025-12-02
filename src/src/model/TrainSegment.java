package model;

/**
 * A segment of a {@link Train}
 * Segments only know which component they are on, but not where exactly they are coordinates wise, that is up to the components themselves.
 */
public class TrainSegment {
    Navigable track;
    Train train;
    boolean isLeading;

    /**
     * @param train The train that this segment is a part of
     * @param start The starting component of the segment, usually an {@link Entrance}
     * @param isLeading Whether this segment is the first in the train
     */
    TrainSegment(Train train, Navigable start, boolean isLeading){
        this.track = start;
        this.train = train;
        this.isLeading = isLeading;
    }

    /**
     * Navigates the segment on the track they are on
     * @return True if the segment reached the end of the track
     * @throws OccupiedException If the segment tries to navigate where another segment already is
     */
    public boolean move() throws OccupiedException {
        if (track == null){
            return true;
        }
        track = track.navigate(this);
        return track == null;
    }

    public Train getTrain() {
        return train;
    }

    public boolean isLeading() {
        return isLeading;
    }
}
