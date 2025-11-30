package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class LevelTest {
    class VerifyTestComponent extends DotComponent {
        boolean verifySucceed;

        VerifyTestComponent(Coordinates position, boolean verifySucceed) {
            super(position);
            this.verifySucceed = verifySucceed;
        }

        @Override
        public void verifyConstraints(Level level) throws ComponentConstraintException {
            if (!verifySucceed){
                throw new ComponentConstraintException(this, "test");
            }
        }

        @Override
        public void accept(Visitor visitor) {

        }
    }

    Level level;

    @BeforeEach
    void createLevel(){
        level = new Level("level", 10, 10, new ArrayList<>());
    }

    @Test
    void testVerifyFail(){
        level.getComponents().add(new VerifyTestComponent(new Coordinates(2, 2), false));

        Assertions.assertThrows(ComponentConstraintException.class,() -> level.verify());
    }

    @Test
    void testVerify(){
        level.getComponents().add(new VerifyTestComponent(new Coordinates(2, 2), true));

        Assertions.assertDoesNotThrow(() -> level.verify());
    }

    @Test
    void testGetComponentAtSimple(){
        Exit exit = new Exit(new Coordinates(2,2));
        level.getComponents().add(exit);

        Assertions.assertSame(exit, level.getComponentAt(new Coordinates(2,2)));
    }

    @Test
    void testGetComponentAtComplex(){
        Track track = new Track(4, new Coordinates(2,2));
        level.getComponents().add(track);

        Assertions.assertSame(track, level.getComponentAt(new Coordinates(3,2)));
    }
}
