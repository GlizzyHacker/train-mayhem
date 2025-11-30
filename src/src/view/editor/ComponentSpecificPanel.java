package view.editor;

import model.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

public class ComponentSpecificPanel extends JPanel implements Visitor {
    class TrainArrivalTableModel extends AbstractTableModel{
        Entrance entrance;

        public TrainArrivalTableModel(Entrance entrance){
            this.entrance = entrance;
        }

        @Override
        public int getRowCount() {
            return entrance.getTrains().size();
        }

        @Override
        public int getColumnCount() {
            //HIDE TRAIN LENGTH
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            TrainArrival arrival = entrance.getTrains().get(rowIndex);
            switch (columnIndex){
                case 0:
                    return arrival.timeOffset;
                case 1:
                    return arrival.train.getColor();
                default:
                    return arrival.train.getSegments().size();
            }
        }

        @Override
        public String getColumnName(int column) {
            switch (column){
                case 0:
                    return "time";
                case 1:
                    return "color";
                default:
                    return "length";
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex){
                case 0:
                    return Integer.class;
                case 1:
                    return Colors.class;
                default:
                    return Integer.class;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            TrainArrival arrival = entrance.getTrains().get(rowIndex);
            switch (columnIndex){
                case 0:
                    arrival.timeOffset = (int) aValue;
                    break;
                case 1:
                    arrival.train = createTrain((Colors) aValue,arrival.train.getSegments().size());
                    break;
                default:
                    arrival.train = createTrain(arrival.train.getColor(),(Integer) aValue);
            }
        }

        public void addTrain(){
            entrance.getTrains().add(new TrainArrival(new StopAtColorTrain(entrance, 3, Colors.BLUE), 0));
            fireTableRowsInserted(entrance.getTrains().size()-1,entrance.getTrains().size()-1);
        }

        Train createTrain(Colors color, int length){
            switch (color){
                case BLACK:
                    return new NonStopTrain(entrance, length);
                case WHITE:
                    return new StopAnywhereTrain(entrance,length);
                default:
                    return new StopAtColorTrain(entrance, length, color);
            }
        }

        void removeLast(){
            entrance.getTrains().removeLast();
            fireTableRowsDeleted(entrance.getTrains().size(),entrance.getTrains().size());
        }
    }

    Component child;

    public ComponentSpecificPanel(){
    }

    public ComponentSpecificPanel(LevelComponent levelComponent){
        setLevelComponent(levelComponent);
    }

    public void setLevelComponent(LevelComponent levelComponent) {
        if (child != null) {
            remove(child);
            child = null;
        }
        if (levelComponent == null){
            return;
        }
        levelComponent.accept(this);
        if (child != null){
            add(child);
        }
    }

    @Override
    public void visit(Entrance component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        TrainArrivalTableModel model = new TrainArrivalTableModel(component);

        JTable table = new JTable(model);
        TableColumn colorColumn = table.getColumnModel().getColumn(1);
        JComboBox<Colors> comboBox = new JComboBox<>(Colors.values());
        colorColumn.setCellEditor(new DefaultCellEditor(comboBox));
        panel.add(table, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        JButton removeButton = new JButton("Remove last");
        removeButton.addActionListener(a -> model.removeLast());
        bottomPanel.add(removeButton);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(a-> model.addTrain());
        bottomPanel.add(addButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        child = new JScrollPane(panel);
    }

    @Override
    public void visit(Exit component) {
    }

    @Override
    public void visit(Platform component) {
    }

    @Override
    public void visit(Switch component) {
        JComboBox<Integer> combo = new JComboBox<>();
        for (int i = 0; i < component.getHeight(); i++) {
            combo.addItem(i);
        }
        combo.addItemListener(itemEvent -> {
            try {
                component.setCurrentExit((Integer) itemEvent.getItem());
            } catch (OccupiedException e) {
                //Should never happen
                throw new RuntimeException(e);
            }
        });
        child = combo;
    }

    @Override
    public void visit(Track component) {

    }

    @Override
    public void visit(Junction component) {

    }
}
