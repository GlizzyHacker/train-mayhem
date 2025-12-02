package view.painter;

import model.Coordinates;
import model.LevelComponent;
import model.Train;

import java.awt.event.MouseEvent;

/**
 * Event for selecting something on a level
 * @param position The position that was selected
 * @param component The component that was selected, null if present
 * @param train The train that was selected, null if not present
 * @param mouseEvent The mouse event associated with the selection if present
 */
public record LevelSelectionEvent(Coordinates position, LevelComponent component, Train train, MouseEvent mouseEvent) {
}
