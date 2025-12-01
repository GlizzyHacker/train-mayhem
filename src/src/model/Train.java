package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Train {
    Colors color;

    List<TrainSegment> segments = new ArrayList<>();
    int wait = 0;
    boolean reachedEnd;

    protected Train(Navigable start, int numSegments, Colors color) {
        this.color = color;
        for (int i = 0; i < numSegments; i++) {
            segments.add(new TrainSegment(this, start, i == 0));
        }
    }

    public List<TrainSegment> getSegments() {
        return segments;
    }

    public Colors getColor() {
        return color;
    }

    public boolean isReachedEnd() {
        return reachedEnd;
    }

    public abstract boolean isCompleted();

    public void move() throws OccupiedException {
        if (wait > 0) {
            wait--;
            return;
        }
        //only reached end when all segments reached end
        reachedEnd = true;
        for (TrainSegment segment : segments) {
            if (!segment.move()){
                reachedEnd = false;
            }
        }
    }

    public void stopFor(int steps) {
        wait = steps;
    }

    public abstract void arrivedAtPlatform(Platform platform);
}
