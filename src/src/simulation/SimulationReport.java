package simulation;

import model.Train;

import java.util.List;

public record SimulationReport(
        SimulationResult result,
        List<Train> trainsInvolved
) { }