package view.play;

import model.Level;
import simulation.LevelSimulator;
import view.painter.LevelPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * Runs a simulation on a level and draws every step
 */
public class LevelPlayer extends JFrame {
    public LevelPlayer(Level level) {
        LevelSimulator sim = new LevelSimulator(level);
        sim.setStepInterval(500);
        sim.addListener(() -> {
            if (sim.isFinished() && isVisible()) {
                String message;
                switch (sim.getReport().result()){
                    case COLLISION:
                        message = "Two trains collided!\nHow could you let this happen?";
                        break;
                    case TRAIN_INCOMPLETE:
                        message = "Some passengers missed their stop!\nShame on you!";
                        break;
                    default:
                        message = "You have won!";
                        break;
                }
                JOptionPane.showMessageDialog(this, message);
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
        if (0 == JOptionPane.showConfirmDialog(this, "Begin level " + level.getName(), "Play", JOptionPane.OK_CANCEL_OPTION)) {
            simThread.start();
        }
        else {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }
}
