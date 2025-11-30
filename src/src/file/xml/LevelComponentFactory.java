package file.xml;

import model.*;
import org.jdom2.DataConversionException;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

class LevelComponentFactory {
    static LevelComponent createLevelComponentFrom(Element element) throws DataConversionException {
        Coordinates position = getCoordinatesFrom(element);
        switch (element.getName()) {
            case "Entrance":
                List<TrainArrival> arrivals = new LinkedList<>();
                Entrance entrance =new Entrance(position, arrivals);
                getTrainsFrom(element, arrivals, entrance);
                return entrance;
            case "Exit":
                return new Exit(position);
            case "Junction":
                return new Junction(position, getHeightFrom(element));
            case "Platform":
                return new Platform(getWidthFrom(element), position, getColorFrom(element));
            case "Switch":
                Switch aSwitch = new Switch(position, getHeightFrom(element));
                try {
                    aSwitch.setCurrentExit(element.getAttribute("exit").getIntValue());
                } catch (OccupiedException e) {
                    //Should never happen
                    throw new RuntimeException(e);
                }
                return aSwitch;
            case "Track":
                return new Track(getWidthFrom(element), position);
            default:
                throw new DataConversionException(element.getQualifiedName(), "Class");
        }
    }

    private static Coordinates getCoordinatesFrom(Element element) throws DataConversionException {
        return new Coordinates(element.getAttribute("x").getIntValue(), element.getAttribute("y").getIntValue());
    }

    private static int getWidthFrom(Element element) throws DataConversionException {
        return element.getAttribute("width").getIntValue();
    }

    private static int getHeightFrom(Element element) throws DataConversionException {
        return element.getAttribute("height").getIntValue();
    }

    private static Colors getColorFrom(Element element) throws DataConversionException {
        try {
            return Colors.valueOf(element.getAttribute("color").getValue());
        }
        catch (IllegalArgumentException _){
            throw new DataConversionException("color", "Colors");
        }
    }

    private  static void getTrainsFrom(Element element, List<TrainArrival> trains, Entrance entrance) throws DataConversionException{
        for (Element child : element.getChildren()){
            int segments = child.getAttribute("segments").getIntValue();
            Colors color = getColorFrom(child);
            Train train;
            switch (color){
                case WHITE:
                    train = new StopAnywhereTrain(entrance, segments);
                    break;
                case BLACK:
                    train = new NonStopTrain(entrance, segments);
                    break;
                default:
                    train = new StopAtColorTrain(entrance, segments, color);
            }
            trains.add(new TrainArrival(train, child.getAttribute("time").getIntValue()));
        }
    }
}
