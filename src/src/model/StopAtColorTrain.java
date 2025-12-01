package model;

public class StopAtColorTrain extends Train {
    boolean hasStopped = false;

    public StopAtColorTrain(Navigable start, int numSegments, Colors color) {
        super(start, numSegments, color);
    }

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

    @Override
    public boolean isCompleted() {
        return hasStopped;
    }
}
