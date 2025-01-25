package tests.factory;
import org.junit.Before;
import org.junit.Test;
import project.*;
import project.factory.ConcreteToDoFactory;
import static org.junit.Assert.*;

public class ConcreteToDoFactoryTest {
    private ConcreteToDoFactory concreteToDoFactory;

    @Before
    public void setUp() {
        concreteToDoFactory = new ConcreteToDoFactory();
    }

    @Test
    public void createTaskTest() {
        Task task = concreteToDoFactory.createTask("Task");
        assertEquals("Task", task.getTitle());
    }

    @Test
    public void createGamingSessionTest() {
        GamingSession gamingSession = concreteToDoFactory.createGamingSession("GamingSession", 60);
        assertEquals("GamingSession", gamingSession.getTitle());
        assertEquals(60, gamingSession.getDuration());
    }

    @Test
    public void createExerciseSessionTest() {
        ExerciseSession exerciseSession = concreteToDoFactory.createExerciseSession("ExerciseSession");
        assertEquals("ExerciseSession", exerciseSession.getTitle());
    }

    @Test
    public void createStudySessionTest() {
        StudySession studySession = concreteToDoFactory.createStudySession("StudySession", 60);
        assertEquals("StudySession", studySession.getTitle());
        assertEquals(60, studySession.getDuration());
    }

    @Test
    public void createShoppingSessionTest() {
        ShoppingSession shoppingSession = concreteToDoFactory.createShoppingSession("ShoppingSession", 60, 100);
        assertEquals("ShoppingSession", shoppingSession.getTitle());
        assertEquals(60, shoppingSession.getDuration());
        assertEquals(100, shoppingSession.getMaxBudget());
    }
}