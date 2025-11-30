package file;

import model.Level;

import java.io.IOException;

public interface LevelReader {
    Level getLevel() throws IOException;
}
