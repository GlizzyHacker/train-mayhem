package view.editor;

import file.LevelWriter;
import file.LevelReader;
import file.xml.XmlLevelReader;
import file.xml.XmlLevelWriter;
import model.*;
import view.WindowBlockerListener;
import view.painter.LevelPainter;
import view.play.LevelPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class LevelEditor extends javax.swing.JFrame {

    private Level level;
    private LevelComponent selected;
    private SelectionPanel toolbar;
    private ComponentPicker picker;
    private Label bottomLabel;

    LevelPainter levelPainter;

    ComponentPicker.ComponentPlacer componentToPlace;

    public LevelEditor(Level level) {
        this.setLayout(new GridBagLayout());
        this.level = level;
        setTitle(level.getName() + " (editor)");

        JMenuBar bar = new JMenuBar();
        JMenuItem miNew = new JMenuItem("New");
        miNew.addActionListener(_ -> openLevel(new Level("New Level", 10, 10, new LinkedList<>())));
        JMenuItem miImport = new JMenuItem("Import");
        miImport.addActionListener(_ -> importLevel());
        JMenuItem miExport = new JMenuItem("Export");
        miExport.addActionListener(_ -> exportLevel());
        JMenuItem miResize = new JMenuItem("Resize");
        miResize.addActionListener(_ -> resizeLevel());
        JMenuItem miVerify = new JMenuItem("Verify");
        miVerify.addActionListener(_ -> verifyLevel());
        JMenuItem miRun = new JMenuItem("Run");
        miRun.addActionListener(_ -> runLevel());
        JMenuItem miGrid = new JCheckBoxMenuItem("Grid", true);
        miGrid.addActionListener(_ -> levelPainter.setGridVisible(miGrid.isSelected()));
        JMenu mFile = new JMenu("File");
        JMenu mLevel = new JMenu("Level");
        JMenu mView = new JMenu("View");
        mFile.add(miNew);
        mFile.add(miImport);
        mFile.add(miExport);
        mLevel.add(miVerify);
        mLevel.add(miRun);
        mLevel.add(miResize);
        mView.add(miGrid);
        bar.add(mFile);
        bar.add(mLevel);
        bar.add(mView);
        setJMenuBar(bar);

        levelPainter = new LevelPainter(level);
        levelPainter.addSelectionListener(event -> {
            if (componentToPlace != null && event.component() != null) {
                JOptionPane.showMessageDialog(this,"overlaps with existing component", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (componentToPlace != null && event.component() == null) {
                LevelComponent newComponent = componentToPlace.place(event.position());
                level.getComponents().add(newComponent);
                levelPainter.addComponent(newComponent);
                if (!event.mouseEvent().isShiftDown()) {
                    componentToPlace = null;
                    bottomLabel.setText("");
                }
            }
            if (componentToPlace == null && event.component() != null) {
                selectComponent(event.component());
            }
        });
        levelPainter.setGridVisible(true);
        levelPainter.setAllowInteract(false);
        GridBagConstraints levelConstraints = new GridBagConstraints();
        levelConstraints.weightx = 1;
        levelConstraints.weighty = 1;
        levelConstraints.gridheight = 2;
        levelConstraints.fill = GridBagConstraints.BOTH;
        levelConstraints.anchor = GridBagConstraints.CENTER;
        this.add(levelPainter, levelConstraints);

        bottomLabel = new Label();
        GridBagConstraints labelConstraint = new GridBagConstraints();
        labelConstraint.gridx = 0;
        labelConstraint.gridy = 2;
        labelConstraint.fill = GridBagConstraints.HORIZONTAL;
        labelConstraint.anchor = GridBagConstraints.SOUTHWEST;
        this.add(bottomLabel, labelConstraint);

        toolbar = new SelectionPanel();
        GridBagConstraints toolbarConstraint = new GridBagConstraints();
        toolbarConstraint.weighty = 0.5;
        toolbarConstraint.weightx = 0.5;
        toolbarConstraint.fill = GridBagConstraints.BOTH;
        toolbarConstraint.gridx = 1;
        toolbarConstraint.gridy = 0;
        toolbarConstraint.anchor = GridBagConstraints.NORTHEAST;
        toolbar.addSelectionActionListener(a -> {
            switch (a) {
                case DELETED:
                    level.getComponents().remove(selected);
                    levelPainter.removeComponent(selected);
                    selectComponent(null);
                    repaint();
                    break;
                case MODIFIED:
                    repaint();
                    break;
            }
        });
        this.add(toolbar, toolbarConstraint);

        picker = new ComponentPicker();
        GridBagConstraints pickerConstraint = new GridBagConstraints();
        pickerConstraint.anchor = GridBagConstraints.SOUTHEAST;
        pickerConstraint.weighty = 0.5;
        pickerConstraint.fill = GridBagConstraints.BOTH;
        pickerConstraint.gridx = 1;
        pickerConstraint.gridy = 1;
        picker.addPlaceListener(this::placeComponent);
        this.add(new JScrollPane(picker), pickerConstraint);
    }

    public void openLevel(Level newLevel) {
        if (0 == JOptionPane.showConfirmDialog(this, "Any unsaved changes will be lost", "Changes", JOptionPane.YES_NO_OPTION)) {
            level = newLevel;
            reopenEditor();
        }
    }

    void placeComponent(ComponentPicker.ComponentPlacer placer) {
        componentToPlace = placer;
        bottomLabel.setText("Placing");
        repaint();
    }

    void selectComponent(LevelComponent component) {
        selected = component;
        toolbar.setSelected(selected);
        levelPainter.setHighlighted(component);
    }

    boolean verifyLevel() {
        try {
            level.verify();
            JOptionPane.showMessageDialog(this, "Level verified", "Verify", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (ComponentConstraintException e) {
            selectComponent(e.getComponent());
            JOptionPane.showMessageDialog(this, String.format("%s at (%d, %d)", e.getMessage(), e.getComponent().getTopLeftCorner().x(), e.getComponent().getTopLeftCorner().y()), "Verify", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }

    void runLevel() {
        //write to temporary file and read to deep copy level
        if (verifyLevel()) {
            try {
                LevelWriter writer = new XmlLevelWriter("temp");
                writer.writeLevel(level);
                LevelReader reader = new XmlLevelReader("temp");
                Level levelCopy = reader.readLevel();
                levelCopy.verify();
                LevelPlayer player = new LevelPlayer(levelCopy);
                player.addWindowListener(new WindowBlockerListener(this));
                player.pack();
                player.setVisible(true);
                new File("temp").delete();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,e, "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ComponentConstraintException e) {
                //Should never happen
                throw new RuntimeException(e);
            }
        }
    }

    void resizeLevel() {
        try {
            int width = Integer.parseInt((String) JOptionPane.showInputDialog(this, "Enter width", "Resize", JOptionPane.PLAIN_MESSAGE, null, null, level.getWidth()));

            int height = Integer.parseInt((String) JOptionPane.showInputDialog(this, "Enter width", "Resize", JOptionPane.PLAIN_MESSAGE, null, null, level.getHeight()));

            List<LevelComponent> componentsOutside = new LinkedList<>();
            for (LevelComponent component : level.getComponents()) {
                if (component.getTopLeftCorner().y() >= height && component.getTopLeftCorner().x() >= width) {
                    componentsOutside.add(component);
                }
            }
            if (componentsOutside.isEmpty() || 0 == JOptionPane.showConfirmDialog(this, String.format("Resizing would remove %d components", componentsOutside.size()), "Resize", JOptionPane.YES_NO_OPTION)) {
                level.getComponents().removeAll(componentsOutside);
                level = new Level(level.getName(), width, height, level.getComponents());
                reopenEditor();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //because levels are immutable if you want to modify the level you have to create a new one, but LevelPainter cant handle that so the whole frame has to be recreated
    //not a perfect solution
    void reopenEditor() {
        JFrame newEditor = new LevelEditor(level);
        newEditor.pack();
        newEditor.setVisible(true);
        newEditor.setDefaultCloseOperation(getDefaultCloseOperation());
        setVisible(false);
    }

    void importLevel() {
        //get file
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        if (chooser.showDialog(this, "Import") != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            LevelReader reader = new XmlLevelReader(chooser.getSelectedFile().getPath());
            openLevel(reader.readLevel());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void exportLevel() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        if (chooser.showDialog(this, "Export") != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            LevelWriter writer = new XmlLevelWriter(chooser.getSelectedFile().getPath());
            writer.writeLevel(level);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
