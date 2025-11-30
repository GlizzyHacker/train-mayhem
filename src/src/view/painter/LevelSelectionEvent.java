package view.painter;

import model.Coordinates;
import model.LevelComponent;
import model.Train;

import java.awt.event.MouseEvent;

//mouseEvent: the mouse event associated with the selection if present
public record LevelSelectionEvent(Coordinates position, LevelComponent component, Train train, MouseEvent mouseEvent) {
}
