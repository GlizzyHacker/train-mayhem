package simulation;

/**
 * The possible results of a simulation
 */
public enum SimulationResult{
    /**
     * If the simulation ended in success
     */
    WIN,
    /**
     * If the simulation ended with a collision
     */
    COLLISION,
    /**
     * If the simulation ended because a train left without completing its objective
     */
    TRAIN_INCOMPLETE
}
