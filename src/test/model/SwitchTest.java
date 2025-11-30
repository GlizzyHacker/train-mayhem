package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

class SwitchTest {
    static Switch aSwitch;

    @BeforeEach
    void createSwitch(){
        aSwitch = new Switch(new Coordinates(1,0), 2);
    }

    @Test
    void testVerifyFail(){
        Assertions.assertThrows(ComponentConstraintException.class ,() -> aSwitch.verifyConstraints(new Level("name",10, 10,new LinkedList<>())));
    }

    @Test
    void testVerify(){
        List<LevelComponent> components = new LinkedList<>();
        components.add(new Track(1, new Coordinates(2,0)));
        components.add(new Track(1, new Coordinates(2,1)));

        Assertions.assertDoesNotThrow(() -> aSwitch.verifyConstraints(new Level("name",10, 10,components)));
    }

    @Test
    void testOccupied() {
        TrainSegment segment1 = new TrainSegment(null, null, true);
        TrainSegment segment2 = new TrainSegment(null, null, true);
        Assertions.assertDoesNotThrow(() -> aSwitch.navigate(segment1));
        Assertions.assertThrows(OccupiedException.class, ()-> aSwitch.navigate(segment2));
    }

    @Test
    void testSwitchNotOccupied() {
        Assertions.assertDoesNotThrow( ()-> aSwitch.setCurrentExit(1));
    }

    @Test
    void testSwitchOccupied() throws OccupiedException {
        TrainSegment segment = new TrainSegment(null, null, true);
        aSwitch.navigate(segment);
        Assertions.assertThrows(OccupiedException.class, ()-> aSwitch.setCurrentExit(1));
    }
}
