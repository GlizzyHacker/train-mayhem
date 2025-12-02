package model;

/** Represent a train arriving at a specified time after the start of a level
 *
 */
public class TrainArrival {
    public Train train;
    public int timeOffset;

    /**
     * @param train The train that will arrive
     * @param time The steps after the start of the level, when the train will arrive
     */
    public TrainArrival(Train train, int time){
        this.train = train;
        this.timeOffset = time;
    }
}
