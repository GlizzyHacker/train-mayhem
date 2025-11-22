package model;

import java.util.Arrays;
import java.util.Objects;

public class Switch extends LevelComponent implements Navigable {

    int currentExit = 0;
    Navigable[] exits;
    TrainSegment objectOnTrack;

    public Switch(Coordinates position, int height) {
        super(1, height, position);
        if (height < 2) {
            throw new IllegalArgumentException();
        }
        exits = new Navigable[height];
    }

    public TrainSegment getObjectOnTrack() {
        return objectOnTrack;
    }

    public int getCurrentExit() {
        return currentExit;
    }

    public void setCurrentExit(int currentExit) throws OccupiedException {
        if (currentExit >= getHeight() || currentExit < 0) {
            throw new IllegalArgumentException();
        }
        if (objectOnTrack != null) {
            throw new OccupiedException(objectOnTrack, this);
        }
        this.currentExit = currentExit;
    }

    @Override
    public void verifyConstraints(Level level) throws ComponentConstraintException {
        super.verifyConstraints(level);

        for (int i = 0; i < getHeight(); i++) {
            LevelComponent component = level.getComponentAt(getTopLeftCorner().transform(1,i));
            if (component instanceof Navigable navigable){
                exits[i] = navigable;
            }
        }

        if (Arrays.stream(exits).anyMatch(Objects::isNull)) {
            throw new ComponentConstraintException(this, "Dead end");
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Navigable navigate(TrainSegment o) throws OccupiedException {
        if (objectOnTrack == null){
            objectOnTrack = o;
            return this;
        }
        else if (o.equals(objectOnTrack)) {
            exits[currentExit].navigate(o);
            objectOnTrack = null;
            return exits[currentExit];
        }
        throw new OccupiedException(objectOnTrack, this);
    }
}
