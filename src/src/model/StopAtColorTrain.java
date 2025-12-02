package model;

/**
 * A train that only stops at platforms matching its own color.
 */
public class StopAtColorTrain extends Train {
    boolean hasStopped = false;

    public StopAtColorTrain(Navigable start, int numSegments, Colors color) {
        super(start, numSegments, color);
    }

    /** Stops only if the color of the platform is equal to the trains color
     * @param platform The platform this train arrived at
     */
    @Override
    public void arrivedAtPlatform(Platform platform) {
        if (hasStopped){
            return;
        }
        if (platform.color.equals(color)){
            stopFor(3);
            hasStopped = true;
        }
    }
    
    /**
     * @return True if it has stopped anywhere
     */
    @Override
    public boolean isCompleted() {
        return hasStopped;
    }
}
