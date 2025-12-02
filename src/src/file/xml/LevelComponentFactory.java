package file.xml;

import model.*;
import org.jdom2.DataConversionException;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * Provides static a static method for constructing a {@link LevelComponent} from XML DOM elements
 * For the required format see {@link file.xml.XmlLevelWriter}
 */
class LevelComponentFactory {
    /**
     * Creates a component from an element
     * @param element The XML DOM element used to create the component.
     * @return A subclass of LevelComponent appropriate for the given element
     * @throws DataConversionException If the format of the element is incorrect
     */
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

    /**
     * Extracts coordinates from a DOM element
     * @param element An element representing a LevelComponent
     * @return A new Coordinates object
     * @throws DataConversionException If the element doesn't contain coordinates
     */
    private static Coordinates getCoordinatesFrom(Element element) throws DataConversionException {
        return new Coordinates(element.getAttribute("x").getIntValue(), element.getAttribute("y").getIntValue());
    }

    /**
     * Extracts width from a DOM element
     * @param element An element representing a LevelComponent
     * @return Width
     * @throws DataConversionException If the element doesn't have a width
     */
    private static int getWidthFrom(Element element) throws DataConversionException {
        return element.getAttribute("width").getIntValue();
    }

    /**
     * Extracts height from a DOM element
     * @param element An element representing a LevelComponent
     * @return Height
     * @throws DataConversionException If the element doesn't have a height
     */
    private static int getHeightFrom(Element element) throws DataConversionException {
        return element.getAttribute("height").getIntValue();
    }

    /**
     * Extracts color from a DOM element
     * @param element An element representing a LevelComponent
     * @return Color
     * @throws DataConversionException If the element doesn't have a color
     */
    private static Colors getColorFrom(Element element) throws DataConversionException {
        try {
            return Colors.valueOf(element.getAttribute("color").getValue());
        }
        catch (IllegalArgumentException _){
            throw new DataConversionException("color", "Colors");
        }
    }

    /**
     * Extracts train arrivals from a DOM element's children.
     * Populates the given list with the extracted arrivals.
     * @param element An element representing an {@link Entrance} component
     * @param trains The list of train arrivals that the method will fill. Doesn't delete existing items
     * @param entrance The entrance object that the element represents
     * @throws DataConversionException If the element doesn't have a height
     */
    private static void getTrainsFrom(Element element, List<TrainArrival> trains, Entrance entrance) throws DataConversionException{
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
