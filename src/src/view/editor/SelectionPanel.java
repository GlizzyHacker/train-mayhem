package view.editor;

import model.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SelectionPanel extends JPanel {
    public enum SelectionAction {
        MODIFIED, DELETED
    }

    public interface SelectionActionListener {
        void onAction(SelectionAction action);
    }

    LevelComponent selected;

    JLabel name;
    JLabel coordinates;
    JLabel size;
    ComponentSpecificPanel specifics;

    List<SelectionActionListener> listeners = new ArrayList<>();

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
