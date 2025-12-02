package view.editor;

import model.Coordinates;
import model.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.function.Supplier;

/**
 * List for picking a component to place
 */
public class ComponentPicker extends JList<ComponentPicker.ComponentListItem> {
    /**
     * Mouse listener that selects the item based on the click and clears the selection afterward
     * This is necessary to be able to select the same item twice
     */
    class ListMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int index = locationToIndex(e.getPoint());
            if (index == -1) {
                return;
            }
            items.get(index).selectCallback.get();
            clearSelection();
        }

    }

    /**
     * This is necessary because component positions are final, so creating a component and then changing its position is not an option
     */
    public interface ComponentPlacer {
        /** Creates the component at the given position
         * @param position The positon where the components top left corner will be
         * @return The new component at the position
         */
        LevelComponent place(Coordinates position);
    }

    /**
     *  List item to create a component
     */
    class ComponentListItem {
        String name;
        //Doesn't look that good
        Supplier<?> selectCallback;

        /**
         * @param name A readable name that the toString method and the UI uses
         * @param selectCallback A lambda that handles the component creation after selecting this item
         */
        public ComponentListItem(String name, Supplier<?> selectCallback) {
            this.name = name;
            this.selectCallback = selectCallback;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Interface for listening for component place events
     * Used by this class to let listeners know when the user wants to place a component from this list
     */
    public interface PlaceComponentListener {
        void sendPlacer(ComponentPlacer placer);
    }

    Vector<ComponentListItem> items = new Vector<>();

    List<PlaceComponentListener> listeners = new LinkedList<>();

    /**
     * Creates the list and fills it with components that can be placed
     * Some components require more than just their location to create. For these inputs dialogs are shown.
     */
    public ComponentPicker() {
        items.add(new ComponentListItem("Entrance", () -> {
            List<TrainArrival> trains = new LinkedList<>();
            notifyListeners(pos -> new Entrance(pos, trains));
            return null;

        }));
        items.add(new ComponentListItem("Exit", () -> {
            notifyListeners(Exit::new);
            return null;
        }));

        items.add(new ComponentListItem("Track", () -> {
            Integer[] possibilities = {1, 2, 3, 4, 5};
            Object size = JOptionPane.showInputDialog(this, "Enter width", "Create track", JOptionPane.PLAIN_MESSAGE, null, possibilities, 1);
            if (size == null){
                return null;
            }

            notifyListeners(pos -> new Track((Integer) size, pos));
            return null;
        }));

        items.add(new ComponentListItem("Platform", () -> {
            Integer[] possibilities = {1, 2, 3, 4, 5};
            Object size = JOptionPane.showInputDialog(this, "Enter width", "Create platform", JOptionPane.PLAIN_MESSAGE, null, possibilities, 3);
            if (size == null){
                return null;
            }

            Colors[] colorPossibilities = {Colors.RED, Colors.BLUE, Colors.GREEN};
            Object color = JOptionPane.showInputDialog(this, "Enter color", "Create platform", JOptionPane.PLAIN_MESSAGE, null, colorPossibilities, null);
            if (color == null){
                return null;
            }

            notifyListeners(pos -> new Platform((Integer) size, pos, (Colors) color));
            return null;
        }));

        items.add(new ComponentListItem("Switch", () -> {
            Integer[] possibilities = {1, 2, 3, 4, 5};
            Object size = JOptionPane.showInputDialog(this, "Enter height", "Create track", JOptionPane.PLAIN_MESSAGE, null, possibilities, 2);
            if (size == null){
                return null;
            }
            notifyListeners(pos -> new Switch(pos, (Integer) size));
            return null;
        }));

        items.add(new ComponentListItem("Junction", () -> {
            Integer[] possibilities = {1, 2, 3, 4, 5};
            Object size = JOptionPane.showInputDialog(this, "Enter height", "Create track", JOptionPane.PLAIN_MESSAGE, null, possibilities, 2);
            if (size == null){
                return null;
            }
            notifyListeners(pos -> new Junction(pos,(Integer) size));
            return null;
        }));
        setListData(items);
        addMouseListener(new ListMouseListener());
    }

    public void addPlaceListener(PlaceComponentListener listener) {
        listeners.add(listener);
    }

    /**
     * Helper method for notifying every listener
     */
    void notifyListeners(ComponentPlacer placer) {
        for (PlaceComponentListener listener : listeners) {
            listener.sendPlacer(placer);
        }
    }
}
