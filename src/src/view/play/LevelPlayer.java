package view.play;

import model.Level;
import simulation.LevelSimulator;
import simulation.SimulationResult;
import view.painter.LevelPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class LevelPlayer extends JFrame {
    public LevelPlayer(Level level) {
        LevelSimulator sim = new LevelSimulator(level);
        sim.setStepInterval(500);
        sim.addListener(() -> {
            if (sim.isFinished() && isVisible()) {
                JOptionPane.showMessageDialog(this, sim.getReport().result() == SimulationResult.WIN ? "You have won!" : "Game over");
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
            repaint();
        });
        Thread simThread = new Thread(sim);

        this.setLayout(new BorderLayout());
        setTitle(level.getName());
        JButton button = new JButton("Step");
        this.add(button, BorderLayout.NORTH);
        button.addActionListener(e -> {
            this.repaint();
            sim.step();
        });

        this.add(new LevelPainter(level), BorderLayout.CENTER);
        if (0 == JOptionPane.showConfirmDialog(this, "Begin level", "Play", JOptionPane.OK_CANCEL_OPTION)) {
            simThread.start();
        }
        else {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }
}
