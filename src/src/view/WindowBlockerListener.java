package view;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Window listener that disables a frame until the windows is closed
 */
public class WindowBlockerListener extends WindowAdapter {
    private final JFrame frame;

    /**
     * @param frame Frame to block until the window closes
     */
    public WindowBlockerListener(JFrame frame) {
        this.frame = frame;
        frame.setEnabled(false);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        frame.setEnabled(true);
    }
}
