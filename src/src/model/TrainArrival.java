package model;
public class TrainArrival {
    public Train train;
    public int timeOffset;

    public TrainArrival(Train train, int time){
        this.train = train;
        this.timeOffset = time;
    }
}
