package file;

import model.Level;

import java.io.IOException;

/**
 * Interface for writing a level to an external storage medium
 */
public interface LevelWriter {
    /**
     * Writes a level to the external medium
     * @param level The level to write
     * @throws IOException If there is a problem when writing
     */
    void writeLevel(Level level) throws IOException;
}
