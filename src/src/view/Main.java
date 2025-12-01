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
import java.net.URL;
import java.util.LinkedList;

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
        panel.setLayout(new GridLayout(1,8));

        URL resource = this.getClass().getResource("/levels");
        if (resource == null) {
            return;
        }
        File file = new File(resource.getFile());
        for (String fileName : file.list()) {
            try {
                LevelReader reader = new XmlLevelReader(file.getPath() + "\\" + fileName);
                //remove extension
                JButton button = new JButton(fileName.split("\\.")[0]);
                button.addActionListener(_ -> {
                    try {
                        runLevel(reader.readLevel());
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this,e, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            panel.add(button);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

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
    }

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

    void editor(){
        LevelEditor editor = new LevelEditor(new Level("New level", 10, 10, new LinkedList<>()));
        editor.pack();
        editor.setVisible(true);
    }
}