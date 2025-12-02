package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

class EntranceTest {
    static Entrance entrance;

    @BeforeEach
    void createEntrance(){
        List<TrainArrival> trains = new LinkedList<>();
        entrance = new Entrance(new Coordinates(1,0),trains);

        trains.add(new TrainArrival(new StopAtColorTrain(entrance, 3, Colors.BLUE), 1));
    }

    @Test
    void testVerifyFail(){
        Assertions.assertThrows(ComponentConstraintException.class ,() -> entrance.verifyConstraints(new Level("name",10, 10,new LinkedList<>())));
    }

    @Test
    void testVerify(){
        List<LevelComponent> components = new LinkedList<>();
        components.add(new Track(1, new Coordinates(2,0)));

        Assertions.assertDoesNotThrow(() -> entrance.verifyConstraints(new Level("name",10, 10,components)));
    }

    @Test
    void testTrainArrival() throws ComponentConstraintException {
        List<LevelComponent> components = new LinkedList<>();
        components.add(new Track(1, new Coordinates(2,0)));
        Level level = new Level("name",10, 10,components);

        entrance.verifyConstraints(level);

        entrance.step();
        Assertions.assertTrue(entrance.getTrains().isEmpty());
        Assertions.assertEquals(1, level.getTrains().size());
    }
}
