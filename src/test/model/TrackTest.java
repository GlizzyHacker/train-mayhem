package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

class TrackTest {
    static Track track;

    @BeforeEach
    void createTrack(){
        track = new Track(2, new Coordinates(1,0));
    }

    @Test
    void testVerifyFail(){
        Assertions.assertThrows(ComponentConstraintException.class ,() -> track.verifyConstraints(new Level("name",10, 10,new LinkedList<>())));
    }

    @Test
    void testVerify(){
        List<LevelComponent> components = new LinkedList<>();
        components.add(new Track(1, new Coordinates(3,0)));

        Assertions.assertDoesNotThrow(() -> track.verifyConstraints(new Level("name",10, 10,components)));
    }

    @Test
    void testOccupied() {
        TrainSegment segment1 = new TrainSegment(null, null, true);
        TrainSegment segment2 = new TrainSegment(null, null, true);
        Assertions.assertDoesNotThrow(() -> track.navigate(segment1));
        Assertions.assertThrows(OccupiedException.class, ()-> track.navigate(segment2));
    }

    @Test
    void testNotOccupied() {
        TrainSegment segment1 = new TrainSegment(null, null, true);
        TrainSegment segment2 = new TrainSegment(null, null, true);
        Assertions.assertDoesNotThrow(() -> track.navigate(segment1));
        Assertions.assertDoesNotThrow(() -> track.navigate(segment1));
        Assertions.assertDoesNotThrow(()-> track.navigate(segment2));
        //HASN'T BEEN VERIFIED AND THUS DOESN'T HAVE A NEXT
        Assertions.assertThrows(NullPointerException.class,() -> track.navigate(segment1));
        Assertions.assertDoesNotThrow(()-> track.navigate(segment2));
    }
}
