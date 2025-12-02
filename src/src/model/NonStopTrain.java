package model;

/**
 * A train that doesn't stop at any {@link Platform}
 */
public class NonStopTrain extends Train{
    public NonStopTrain(Navigable start, int numSegments) {
        super(start, numSegments, Colors.BLACK);
    }


    /**
     * @return This train is always completed
     */
    @Override
    public boolean isCompleted() {
        return true;
    }

    /**
     * Doesn't do anything as this train doesn't stop anywhere
     * @param platform The platform this train arrived at
     */
    @Override
    public void arrivedAtPlatform(Platform platform) {
        // Doesn't stop anywhere
    }
}
