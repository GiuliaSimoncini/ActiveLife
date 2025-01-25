package tests;
import project.GamingSession;
import static org.junit.Assert.*;
import org.junit.Test;

public class GamingSessionTest {
    @Test
    public void constructorExceptionTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new GamingSession("validTitle", -5));
        assertEquals("Duration can not be a negative number or 0", exception.getMessage());
    }

    @Test
    public void platformExceptionTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new GamingSession("validTitle", 10)
                .platform(""));
        assertEquals("The name of a platform can not be empty", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> new GamingSession("validTitle", 10)
                .platform(null));
        assertEquals("The name of a platform can not be null", exception.getMessage());
    }

    @Test
    public void multiplayerExceptionTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new GamingSession("validTitle", 10)
                .multiplayer(null));
        assertEquals("The multiplayer value can not be null", exception.getMessage());
    }
}