package tests.filtering;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import project.Task;
import project.filtering.PriorityFilteringStrategy;
import project.filtering.FilteringStrategy;

public class PriorityFilteringStrategyTest {
    private Task task;
    private FilteringStrategy filtering;

    @Before
    public void setup() {
        task = new Task("aTask");
        filtering = new PriorityFilteringStrategy(4);
    }

    @Test
    public void constructorExceptionTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new PriorityFilteringStrategy(-1));
        assertEquals("The minimum priority can not be a negative number", exception.getMessage());
    }

    @Test
    public void filterToDoNoPriorityTest() {
        task.addToDo(new Task("aSecondTask")
                        .addToDo(new Task("aInnerTask")))
                .addToDo(new Task("aThirdTask"))
                .filteringStrategy(filtering);
        assertEquals(0, task.getFilteredStream()
                .count());
    }

    @Test
    public void filterToDoTest() {
        task.addToDo(new Task("aSecondTask")
                        .priority(3)
                        .addToDo(new Task ("aInnerTask")
                                .priority(2)))
                .addToDo(new Task ("aThirdTask")
                        .priority(5))
                .addGamingSession("Chess", 20)
                .filteringStrategy(filtering);
        assertEquals(1, task.getFilteredStream()
                .count());
        assertFalse(task.getFilteredStream()
                .anyMatch(toDo -> "aInnerTask".equals(toDo.getTitle())));
        assertTrue(task.getFilteredStream()
                .anyMatch(toDo -> "aThirdTask".equals(toDo.getTitle())));
        assertFalse(task.getFilteredStream()
                .anyMatch(toDo -> "aSecondTask".equals(toDo.getTitle())));
    }

    @Test
    public void getFilteredDescriptionNoPriorityTest() {
        task.addToDo(new Task("aSecondTask")
                        .addToDo(new Task("aInnerTask"))
                        .addGamingSession("Chess", 20))
                .addToDo(new Task("aThirdTask"))
                .filteringStrategy(filtering);
        assertEquals("No task has a high enough priority", task.getFilteredDescription());
    }

    @Test
    public void getFilteredDescriptionTest() {
        task.addToDo(new Task("aSecondTask")
                        .addToDo(new Task("aInnerTask")
                                .priority(4))
                        .priority(1))
                .addToDo(new Task("aThirdTask")
                        .priority(5))
                .addGamingSession("Chess", 20)
                .priority(6)
                .filteringStrategy(filtering);
        assertEquals("aTask"
                +"\naThirdTask\n", task.getFilteredDescription());
    }
}

