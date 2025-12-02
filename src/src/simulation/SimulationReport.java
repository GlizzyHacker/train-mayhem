package simulation;

import model.Train;

import java.util.List;

/**
 * Details about the result of a simulation
 * @param result The result
 * @param trainsInvolved Trains that caused the result, null if not applicable.
 */
public record SimulationReport(
        SimulationResult result,
        List<Train> trainsInvolved
) { }