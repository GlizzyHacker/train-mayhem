package model;

/**
 * A train that stops at any platform
 */
public class StopAnywhereTrain extends Train {
    boolean hasStopped = false;

    public StopAnywhereTrain(Navigable start, int numSegments) {
        super(start, numSegments, Colors.WHITE);
    }

    /**
     * @return True if it has stopped anywhere
     */
    @Override
    public boolean isCompleted() {
        return hasStopped;
    }

    /**
     * Stops regardless of the color of the platform
     * @param platform The platform this train arrived at
     */
    @Override
    public void arrivedAtPlatform(Platform platform) {
        if (hasStopped){
            return;
        }
        stopFor(3);
        hasStopped = true;
    }
}
