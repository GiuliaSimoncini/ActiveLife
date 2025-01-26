package tests.filtering;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import project.ExerciseSession;
import project.Task;
import project.filtering.TitleKeywordFilteringStrategy;

public class TitleKeywordFilteringStrategyTest {
    private TitleKeywordFilteringStrategy filtering;
    private Task task;

    @Before
    public void setup() {
        filtering = new TitleKeywordFilteringStrategy("Ch")
                .addKeyword("te")
                .addKeyword("Cardio");
        task = new Task("firstTask");
    }

    @Test
    public void constructorException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new TitleKeywordFilteringStrategy(null));
        assertEquals("Keyword can not be null", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> new TitleKeywordFilteringStrategy(""));
        assertEquals("Keyword can not be empty", exception.getMessage());
    }

    @Test
    public void addKeywordException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> filtering.addKeyword(""));
        assertEquals("Keyword can not be empty", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> filtering.addKeyword(null));
        assertEquals("Keyword can not be null", exception.getMessage());
    }

    @Test
    public void filterToDoNoMatchTest() {
        task.addToDo(new Task("secondTask")
                        .addToDo(new Task("InnerTask")))
                .addToDo(new Task("ThirdTasks"))
                .filteringStrategy(filtering);
        assertEquals(0, task.getFilteredStream().count());
    }

    @Test
    public void filterToDoTest() {
        task.addToDo(new Task("secondTask")
                        .addToDo(new Task("InnerTask")))
                .addGamingSession("Chess", 20)
                .addToDo(new Task("ThirdTask"))
                .addToDo(new ExerciseSession("Cardio"))
                .filteringStrategy(filtering
                        .addKeyword("sec"));
        assertEquals(3, task.getFilteredStream().count());
        assertTrue(task.getFilteredStream()
                .anyMatch(toDo -> "Chess".equals(toDo.getTitle())));
        assertFalse(task.getFilteredStream()
                .anyMatch(toDo -> "ThirdTask".equals(toDo.getTitle())));
    }

    @Test
    public void getFilteredDescriptionNoMatchTest() {
        task.addToDo(new Task("secondTask")
                        .addToDo(new Task("InnerTask")))
                .addToDo(new Task("ThirdTasks"))
                .filteringStrategy(filtering);
        assertEquals("No task contains any of the keywords"
                +"\nKeywords:"
                +"\n\tCh"
                +"\n\tte"
                +"\n\tCardio"
                +"\n", task.getFilteredDescription());
    }

    @Test
    public void getFilteredDescriptionTest() {
        task.addToDo(new Task("secondTask")
                        .addToDo(new Task("InnerTask")))
                .addGamingSession("Chess", 20)
                .addToDo(new Task("ThirdTask"))
                .addToDo(new ExerciseSession("Cardio"))
                .filteringStrategy(filtering
                        .addKeyword("sec"));
        assertEquals("secondTask"
                +"\nChess"
                +"\nCardio"
                +"\nKeywords:"
                +"\n\tCh"
                +"\n\tte"
                +"\n\tCardio"
                +"\n\tsec"
                +"\n", task.getFilteredDescription());
    }
}
