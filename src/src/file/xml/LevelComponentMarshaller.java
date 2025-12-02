package file.xml;

import model.*;
import org.jdom2.Element;

/**
 * Provides a static method for creating XML DOM elements from {@link LevelComponent}
 * Implements {@link Visitor} to give class specific behaviour to each component and to give compile time coverage guarantee.
 */
public class LevelComponentMarshaller implements Visitor {
    private Element result;

    /**
     * Creates an XML DOM element from a component
     * @param component the component to represent in the DOM
     * @return the DOM element representing the component
     */
    public static Element elementFrom(LevelComponent component) {
        LevelComponentMarshaller marshaller = new LevelComponentMarshaller();
        component.accept(marshaller);
        return marshaller.result;
    }

    @Override
    public void visit(Entrance component) {
        createElement("Entrance", component);
        for (TrainArrival arrival : component.getTrains()) {
            Element child = new Element("Train");
            child.setAttribute("time", String.valueOf(arrival.timeOffset));
            child.setAttribute("color", String.valueOf(arrival.train.getColor()));
            child.setAttribute("segments", String.valueOf(arrival.train.getSegments().size()));
            result.addContent(child);
        }
    }

    @Override
    public void visit(Exit component) {
        createElement("Exit", component);
    }

    @Override
    public void visit(Platform component) {
        createElement("Platform", component);
        result.setAttribute("color", String.valueOf(component.getColor()));
        result.setAttribute("width", String.valueOf(component.getWidth()));
    }

    @Override
    public void visit(Switch component) {
        createElement("Switch", component);
        result.setAttribute("height", String.valueOf(component.getHeight()));
        result.setAttribute("exit", String.valueOf(component.getCurrentExit()));
    }

    @Override
    public void visit(Track component) {
        createElement("Track", component);
        result.setAttribute("width", String.valueOf(component.getWidth()));
    }

    @Override
    public void visit(Junction component) {
        createElement("Junction", component);
        result.setAttribute("height", String.valueOf(component.getHeight()));
    }

    void createElement(String name, LevelComponent component) {
        result = new Element(name);
        result.setAttribute("x", String.valueOf(component.getTopLeftCorner().x()));
        result.setAttribute("y", String.valueOf(component.getTopLeftCorner().y()));
    }
}
