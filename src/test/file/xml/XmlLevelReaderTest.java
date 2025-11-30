package file.xml;

import file.LevelReader;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

class XmlLevelReaderTest {

    static File file;
    static Level read;

    @BeforeAll
    static void findTestFile() throws IOException {
        URL resource = XmlLevelReaderTest.class.getResource("/test.xml");
        if (resource == null) {
            throw new IOException("No test.xml file provided");
        }
        file = new File(resource.getFile());
    }

    @BeforeEach
    void readLevel() throws IOException {
        LevelReader reader = new XmlLevelReader(file.getPath());
        read = reader.getLevel();
    }

    @Test
    void testLevelProperties() {
        Assertions.assertEquals("Test", read.getName());
        Assertions.assertEquals(10, read.getWidth());
        Assertions.assertEquals(10, read.getHeight());
    }

    @Test
    void testEntrance() {
        LevelComponent entrance = read.getComponentAt(new Coordinates(0, 1));

        Assertions.assertNotNull(entrance);
        Assertions.assertEquals(Entrance.class, entrance.getClass());

        List<TrainArrival> arrivals = ((Entrance) entrance).getTrains();
        Assertions.assertEquals(0, arrivals.getFirst().timeOffset);
        assertTrain(new StopAtColorTrain((Entrance) entrance, 3, Colors.BLUE), arrivals.getFirst().train);

        Assertions.assertEquals(2, arrivals.get(1).timeOffset);
        assertTrain(new NonStopTrain((Entrance) entrance, 5), arrivals.get(1).train);

        Assertions.assertEquals(5, arrivals.get(2).timeOffset);
        assertTrain(new StopAnywhereTrain((Entrance) entrance, 3), arrivals.get(2).train);
    }

    @Test
    void testExit() {
        LevelComponent exit = read.getComponentAt(new Coordinates(9, 1));

        Assertions.assertNotNull(exit);
        Assertions.assertEquals(Exit.class, exit.getClass());
    }

    @Test
    void testTrack() {
        LevelComponent track = read.getComponentAt(new Coordinates(5, 2));

        Assertions.assertNotNull(track);
        Assertions.assertEquals(Track.class, track.getClass());
        Assertions.assertEquals(2, track.getWidth());
    }

    @Test
    void testPlatform() {
        LevelComponent platform = read.getComponentAt(new Coordinates(5, 3));

        Assertions.assertNotNull(platform);
        Assertions.assertEquals(Platform.class, platform.getClass());
        Assertions.assertEquals(2, platform.getWidth());
    }

    @Test
    void testSwitch() {
        LevelComponent aSwitch = read.getComponentAt(new Coordinates(4, 1));

        Assertions.assertNotNull(aSwitch);
        Assertions.assertEquals(Switch.class, aSwitch.getClass());
        Assertions.assertEquals(3, aSwitch.getHeight());
        Assertions.assertEquals(1, ((Switch) aSwitch).getCurrentExit());
    }

    @Test
    void testJunction() {
        LevelComponent junction = read.getComponentAt(new Coordinates(7, 1));

        Assertions.assertNotNull(junction);
        Assertions.assertEquals(Junction.class, junction.getClass());
        Assertions.assertEquals(3, junction.getHeight());
    }

    static void assertTrain(Train expected, Train actual) {
        Assertions.assertEquals(expected.getClass(), actual.getClass());
        Assertions.assertEquals(expected.getColor(), actual.getColor());
        Assertions.assertEquals(expected.getSegments().size(), actual.getSegments().size());
    }

}
