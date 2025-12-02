package model;

import java.util.Arrays;
import java.util.Objects;

/**
 * A component that has multiple exits, but only one is selected at a time.
 */
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

    public TrainSegment getSegmentOnTrack() {
        return objectOnTrack;
    }

    /**
     * @return The index of the currently selected exit. 0 by default
     */
    public int getCurrentExit() {
        return currentExit;
    }

    /**
     * Changes the currently selected exit
     * @param currentExit The index of the newly selected exit
     * @throws OccupiedException If there is a train currently on the component
     */
    public void setCurrentExit(int currentExit) throws OccupiedException {
        if (currentExit >= getHeight() || currentExit < 0) {
            throw new IllegalArgumentException();
        }
        if (objectOnTrack != null) {
            throw new OccupiedException(objectOnTrack, this);
        }
        this.currentExit = currentExit;
    }

    /**
     * Verifies that there is a component that could serve as an exit for each coordinate right of this component.
     * @param level The level that the component is a part of
     * @throws ComponentConstraintException If there is an exit that leads to a dead end.
     */
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

    /** Navigate the segment already on this component to the component corresponding to the currently selected exit.
     * @param segment The segment that navigates this object
     * @return This if not already on this component or The component at the currently selected exit.
     * @throws OccupiedException If entering and there is already another segment here.
     */
    @Override
    public Navigable navigate(TrainSegment segment) throws OccupiedException {
        if (objectOnTrack == null){
            objectOnTrack = segment;
            return this;
        }
        else if (segment.equals(objectOnTrack)) {
            exits[currentExit].navigate(segment);
            objectOnTrack = null;
            return exits[currentExit];
        }
        throw new OccupiedException(objectOnTrack, this);
    }
}
