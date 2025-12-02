package simulation;

import model.*;

import java.util.LinkedList;
import java.util.List;


/**
 * Simulates a level at a specified speed.
 * This class is meant to be run as a thread.
 * Implements the visitor interface to simulate each component at every step as needed.
 * Creating a copy is not the responsibility of this class. The simulation will irrevocably alter the level.
 * Make sure only one simulation is run on a level at a time.
 */
public class LevelSimulator implements Visitor, Runnable {
    Level level;

    // in millis
    int stepInterval = 1000;
    boolean isFinished;
    boolean hasTrainsInEntrance;
    List<SimulationListener> listeners = new LinkedList<>();
    SimulationReport report;

    /**
     * @param level The level to simulate. The level is not copied and will be altered.
     */
    public LevelSimulator(Level level) {
        this.level = level;
    }

    /**
     * A simulation step.Moves trains first, then accepts itself as a visitor on all components.
     * Notifies simulation listeners of the step
     */
    public void step() {
        for (Train train : level.getTrains()) {
            try {
                train.move();
                if (train.isReachedEnd() && !train.isCompleted()) {
                    isFinished = true;
                    report = new SimulationReport(SimulationResult.TRAIN_INCOMPLETE, List.of(train));
                }

            } catch (OccupiedException e) {
                isFinished = true;
                report = new SimulationReport(SimulationResult.COLLISION, List.of(train,e.getOccupiedBy().getTrain()));
            }
        }
        hasTrainsInEntrance = false;
        for (LevelComponent component : level.getComponents()) {
            component.accept(this);
        }

        if (!hasTrainsInEntrance && level.getTrains().stream().allMatch(train -> train.isCompleted() && train.isReachedEnd())) {
            isFinished = true;
            report = new SimulationReport(SimulationResult.WIN, List.of());
        }
    }

    /**
     * Begin the simulation.
     * Starts a loop that has busy waiting inside that only stops if the simulation is finished.
     */
    @Override
    public void run() {
        while (!isFinished) {
            try {
                Thread.sleep(stepInterval);
            } catch (InterruptedException _) {
                Thread.currentThread().interrupt();
            }
            step();
            listeners.forEach(SimulationListener::onStep);
        }
    }

    public boolean isFinished() {
        return isFinished;
    }

    public int getStepInterval() {
        return stepInterval;
    }

    /**
     * Changes the waiting time between simulation steps
     * @param stepInterval The new step interval in milliseconds
     */
    public void setStepInterval(int stepInterval) {
        this.stepInterval = stepInterval;
    }

    public void addListener(SimulationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SimulationListener listener) {
        listeners.remove(listener);
    }

    /**
     * @return A report describing why the simulation ended or null if the simulation is not finished.
     */
    public SimulationReport getReport() {
        return report;
    }

    /**
     * Advances the entrances countdown.
     * @param component the entrance to step
     */
    @Override
    public void visit(Entrance component) {
        component.step();
        if (!component.getTrains().isEmpty()){
            hasTrainsInEntrance = true;
        }
    }

    @Override
    public void visit(Exit component) {
    }

    @Override
    public void visit(Platform component) {
    }

    @Override
    public void visit(Switch component) {

    }

    @Override
    public void visit(Track component) {

    }

    @Override
    public void visit(Junction component) {

    }
}
