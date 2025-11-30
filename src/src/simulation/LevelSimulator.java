package simulation;

import model.*;

import java.util.LinkedList;
import java.util.List;

//copy and run simulate on simulationstep components and trains
public class LevelSimulator implements Visitor, Runnable {
    Level level;

    // in millis
    int stepInterval = 1000;
    boolean isFinished;
    boolean hasTrainsInEntrance;
    List<SimulationListener> listeners = new LinkedList<>();
    SimulationReport report;

    public LevelSimulator(Level level) {
        this.level = level;
    }

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

    public void setStepInterval(int stepInterval) {
        this.stepInterval = stepInterval;
    }

    public void addListener(SimulationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SimulationListener listener) {
        listeners.remove(listener);
    }

    public SimulationReport getReport() {
        return report;
    }

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
