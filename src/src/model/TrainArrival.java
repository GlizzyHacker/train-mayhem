package model;

import java.util.ArrayList;
import java.util.List;

public class TrainArrival {
    List<Train> possibleTrains = new ArrayList<>();
    int timeOffset;

    public TrainArrival(Train train, int time){
        this.possibleTrains.add(train);
        this.timeOffset = time;
    }
}
