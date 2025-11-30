package file;

import model.Level;

import java.io.IOException;

public interface LevelWriter {
    void writeLevel(Level level) throws IOException;
}
