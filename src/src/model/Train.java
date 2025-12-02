package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Made up of {@link TrainSegment}
 * The train itself does not move, and it doesn't know exactly where it is, only its segments move and know.
 */
public abstract class Train {
    Colors color;

    List<TrainSegment> segments = new ArrayList<>();
    int wait = 0;
    boolean reachedEnd;

    /**
     * @param start       The starting component of the train, normally an {@link Entrance}
     * @param numSegments The number of segments the train has
     * @param color       The color of the train
     */
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

    /**
     * @return True if the train reached an {@link Exit}
     */
    public boolean isReachedEnd() {
        return reachedEnd;
    }

    /**
     * @return True if the train has completed it's objective, specified by subclasses.
     */
    public abstract boolean isCompleted();

    /** moves the train's segments along the track. Navigating them one by one.
     * @throws OccupiedException If a segment navigates an occupied component
     */
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

    /**
     * Stops the train for the specified steps
     * @param steps the steps to stop for. A step is a single time the move method was called.
     */
    public void stopFor(int steps) {
        wait = steps;
    }

    /**
     * @param platform The platform this train arrived at
     */
    public abstract void arrivedAtPlatform(Platform platform);
}
