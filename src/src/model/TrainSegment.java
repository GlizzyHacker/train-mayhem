package model;

public class TrainSegment {
    Navigable track;
    Train train;
    boolean isLeading;

    TrainSegment(Train train, Navigable start, boolean isLeading){
        this.track = start;
        this.train = train;
        this.isLeading = isLeading;
    }

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
