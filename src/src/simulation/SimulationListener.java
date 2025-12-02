package simulation;

/**
 * Interface for listening to a simulations' steps
 */
public interface SimulationListener {
    /**
     * Called when the simulation finishes a step
     */
   void onStep();
}
