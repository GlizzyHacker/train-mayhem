package file.xml;

import file.LevelWriter;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class XmlLevelWriterTest {

    static File file;
    static List<String> lines;

    @BeforeAll
    static void createTestFile() throws IOException, OccupiedException {
        URL resource = XmlLevelReaderTest.class.getResource("/");
        if (resource == null) {
            throw new IOException("No test.xml file provided");
        }
        file = new File(resource.getFile()+"\\test2.xml");

        LevelWriter writer = new XmlLevelWriter(file.getPath());Level level = new Level("level",10, 10, new ArrayList<>());

        List<LevelComponent> components = level.getComponents();

        List<TrainArrival> arrivals = new ArrayList<>();
        Entrance entrance = new Entrance(new Coordinates(0,1),arrivals);
        arrivals.add(new TrainArrival(new StopAtColorTrain(entrance,3, Colors.BLUE),0));
        arrivals.add(new TrainArrival(new NonStopTrain(entrance,5),2));
        arrivals.add(new TrainArrival(new StopAnywhereTrain(entrance,3),5));

        components.add(entrance);
        model.Switch aSwitch = new Switch(new Coordinates(4,1), 3);
        aSwitch.setCurrentExit(1);
        components.add(aSwitch);
        components.add(new Track(2,new Coordinates(5,2)));
        components.add(new Platform(2,new Coordinates(5,3),Colors.BLUE));
        components.add(new Junction(new Coordinates(7,1), 3));
        components.add(new Exit(new Coordinates(9,1)));

        writer.writeLevel(level);
    }

    @BeforeEach
    void readTestFile() throws IOException {
        lines = new LinkedList<>();
        InputStream inputStream = new FileInputStream(file);
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()){
            lines.add(scanner.nextLine().strip());
        }
    }

    @Test
    void testLevelProperties() {
        String line = lines.stream().filter(s -> s.startsWith("<Level")).toList().getFirst();
        Assertions.assertEquals("<Level width=\"10\" height=\"10\" name=\"test2\">", line);
    }

    @Test
    void testEntrance() {
        String line = lines.stream().filter(s -> s.startsWith("<Entrance")).toList().getFirst();
        Assertions.assertEquals("<Entrance x=\"0\" y=\"1\">", line);

        Assertions.assertEquals("<Train time=\"0\" color=\"BLUE\" segments=\"3\" />" ,lines.get(lines.indexOf(line)+1));
        Assertions.assertEquals("<Train time=\"2\" color=\"BLACK\" segments=\"5\" />" ,lines.get(lines.indexOf(line)+2));
        Assertions.assertEquals("<Train time=\"5\" color=\"WHITE\" segments=\"3\" />" ,lines.get(lines.indexOf(line)+3));
    }

    @Test
    void testExit() {
        String line = lines.stream().filter(s -> s.startsWith("<Exit")).toList().getFirst();
        Assertions.assertEquals("<Exit x=\"9\" y=\"1\" />", line);
    }

    @Test
    void testTrack() {
        String line = lines.stream().filter(s -> s.startsWith("<Track")).toList().getFirst();
        Assertions.assertEquals("<Track x=\"5\" y=\"2\" width=\"2\" />", line);
    }

    @Test
    void testPlatform() {
        String line = lines.stream().filter(s -> s.startsWith("<Platform")).toList().getFirst();
        Assertions.assertEquals("<Platform x=\"5\" y=\"3\" color=\"BLUE\" width=\"2\" />", line);
    }

    @Test
    void testSwitch() {
        String line = lines.stream().filter(s -> s.startsWith("<Switch")).toList().getFirst();
        Assertions.assertEquals("<Switch x=\"4\" y=\"1\" height=\"3\" exit=\"1\" />", line);
    }

    @Test
    void testJunction() {
        String line = lines.stream().filter(s -> s.startsWith("<Junction")).toList().getFirst();
        Assertions.assertEquals("<Junction x=\"7\" y=\"1\" height=\"3\" />", line);
    }
}
