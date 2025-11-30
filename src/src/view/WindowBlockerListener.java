package view;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowBlockerListener extends WindowAdapter {
    private final JFrame frame;

    public WindowBlockerListener(JFrame frame) {
        this.frame = frame;
        frame.setEnabled(false);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        frame.setEnabled(true);
    }
}
