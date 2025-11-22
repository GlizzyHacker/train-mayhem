package model;

public class StopAnywhereTrain extends Train {
    boolean hasStopped = false;

    public StopAnywhereTrain(Navigable start, int numSegments) {
        super(start, numSegments, Colors.WHITE);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    @Override
    public void arrivedAtPlatform(Platform platform) {
        if (hasStopped){
            return;
        }
        stopFor(3);
        hasStopped = true;
    }
}
