package model;

public class NonStopTrain extends Train{
    public NonStopTrain(Navigable start, int numSegments) {
        super(start, numSegments, Colors.BLACK);
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    public void arrivedAtPlatform(Platform platform) {
        // Doesn't stop anywhere
    }
}
