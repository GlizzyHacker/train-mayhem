package view.editor;

import model.Coordinates;
import model.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.function.Supplier;

public class ComponentPicker extends JList<ComponentPicker.ComponentListItem> {
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

    public interface ComponentPlacer {
        LevelComponent place(Coordinates position);
    }

    class ComponentListItem {
        String name;
        //HACK: CHANGE ASAP
        Supplier<?> selectCallback;

        public ComponentListItem(String name, Supplier<?> selectCallback) {
            this.name = name;
            this.selectCallback = selectCallback;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public interface PlaceComponentListener {
        void sendPlacer(ComponentPlacer placer);
    }

    Vector<ComponentListItem> items = new Vector<>();

    List<PlaceComponentListener> listeners = new LinkedList<>();

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

    void notifyListeners(ComponentPlacer placer) {
        for (PlaceComponentListener listener : listeners) {
            listener.sendPlacer(placer);
        }
    }
}
