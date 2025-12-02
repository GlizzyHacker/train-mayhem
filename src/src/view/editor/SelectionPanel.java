package view.editor;

import model.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A panel that displays general information about a selected component
 */
public class SelectionPanel extends JPanel {
    /**
     * An enum that lists all the possible actions a user can do with a selected component
     */
    public enum SelectionAction {
        MODIFIED, DELETED
    }

    /**
     * A listener for selection actions
     */
    public interface SelectionActionListener {
        void onAction(SelectionAction action);
    }

    LevelComponent selected;

    JLabel name;
    JLabel coordinates;
    JLabel size;
    ComponentSpecificPanel specifics;

    List<SelectionActionListener> listeners = new ArrayList<>();

    /**
     * Creates a panel with no component selected
     */
    public SelectionPanel() {
        super();

        name = new JLabel("Nothing selected");
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        coordinates = new JLabel();
        size = new JLabel();
        specifics = new ComponentSpecificPanel();
        JButton delete = new JButton("Delete");
        delete.addActionListener(a -> listeners.forEach(l -> l.onAction(SelectionAction.DELETED)));
        add(name);
        add(coordinates);
        add(size);
        add(delete);
        add(specifics);
    }

    /**
     * Changes the selected component and updates all the labels to show information about the selected component
     * @param selected The new selected component
     */
    public void setSelected(LevelComponent selected) {
        this.selected = selected;
        if (selected == null) {
            name.setText("Nothing selected");
            coordinates.setText("");
            size.setText("");
            return;
        }
        name.setText(selected.getClass().getName().split("\\.")[1]);
        coordinates.setText("x:" + selected.getTopLeftCorner().x() + " y:" + selected.getTopLeftCorner().y());
        size.setText("width:" + selected.getWidth() + " height:" + selected.getHeight());
        specifics.setLevelComponent(selected);
    }

    public void addSelectionActionListener(SelectionActionListener listener) {
        listeners.add(listener);
    }
}
