package view;

import file.LevelReader;
import file.xml.XmlLevelReader;
import model.*;
import view.editor.LevelEditor;
import view.play.LevelPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;

/**
 * Main menu
 */
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends javax.swing.JFrame {
    public static void main(String[] args) {
        javax.swing.JFrame window = new Main();
        window.pack();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public Main() {
        super("Train Mayhem");
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,4));

        add(panel, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel,BoxLayout.PAGE_AXIS));

        JButton importButton = new JButton("Play from file");
        importButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        importButton.addActionListener(_ -> playFromFile());
        sidePanel.add(importButton);
        JButton editorButton = new JButton("Editor");
        editorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        editorButton.addActionListener(_ -> editor());
        sidePanel.add(editorButton);
        add(sidePanel, BorderLayout.EAST);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        int i = 1;
        InputStream resource = this.getClass().getResourceAsStream("/levels/"+i+".xml");
        while (resource != null) {

            try {
                LevelReader reader = new XmlLevelReader(resource);
                JButton button = new JButton(reader.readLevel().getName());
                button.addActionListener(_ -> {
                    try {
                        runLevel(reader.readLevel());
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this,e, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            panel.add(button);
                resource.close();
                i++;
                resource = this.getClass().getResourceAsStream("/levels/"+i+".xml");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,e, "Error", JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
    }

    /**
     * Shows a file chooser dialog and runs the level in the file
     */
    void playFromFile(){
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        if (chooser.showDialog(this, "Import") != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            LevelReader reader = new XmlLevelReader(chooser.getSelectedFile().getPath());
            runLevel(reader.readLevel());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Verifies and runs a level.
     * Blocks the main menu while the player is open.
     * @param level Level to run
     */
    void runLevel(Level level) {
        try {
            level.verify();
        } catch (ComponentConstraintException e) {
            JOptionPane.showMessageDialog(this,String.format("%s: %s", e.getComponent(), e.getMessage()), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LevelPlayer player = new LevelPlayer(level);
        player.addWindowListener(new WindowBlockerListener(this));
        player.pack();
        player.setVisible(true);
    }

    /**
     * Opens the editor
     */
    void editor(){
        LevelEditor editor = new LevelEditor(new Level("New level", 10, 10, new LinkedList<>()));
        editor.pack();
        editor.setVisible(true);
    }
}