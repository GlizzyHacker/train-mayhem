package file;

import model.Level;

import java.io.IOException;

/**
 * Interface for reading a {@link Level} from an outside source
 */
public interface LevelReader {
    /**
     * Reads the {@link Level} from the source.
     * Implementations should always return a new instance of the level freshly constructed from the source and not cache the result.
     * @return A newly constructed level
     * @throws IOException if the source is not formatted correctly
     */
    Level readLevel() throws IOException;
}
