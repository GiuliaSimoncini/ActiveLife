package tests;
import project.*;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.util.Map;

public class TaskTest {
    @Test
    public void priorityTest() {
        Task task = new Task("aTitle");
        assertEquals(0, task.getPriority());
        task.priority(10);
        assertEquals(10, task.getPriority());
    }

    @Test
    public void priorityExceptionTest() {
        Task throwsTask = new Task("Throwing Exception Task");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> throwsTask.priority(-1));
        assertEquals("Priority can not be a negative number or equal to zero", exception.getMessage());
    }

    @Test
    public void removeToDoTest() {
        Task mainTask = new Task("Main Task");
        Task subTask = new Task("subTask");
        Task subSubTask = new Task("subSubTask");
        Task secondSubTask = new Task("secondSubTask");
        GamingSession sess = new GamingSession("Chess", 10);
        GamingSession sess1 = new GamingSession("Tekken", 10);
        mainTask.addToDo(subTask
                        .addToDo(subSubTask))
                .addToDo(sess)
                .addToDo(secondSubTask)
                .addToDo(sess1)
                .removeToDo(subTask)
                .removeToDo(sess1);
        assertFalse(mainTask.isToDoInside(subTask));
        assertFalse(mainTask.isToDoInside(sess1));
        assertEquals(2, mainTask.stream()
                .count());
    }

    @Test
    public void removeByTitleTest() {
        Task mainTask = new Task("Main Task");
        mainTask.addToDo(new Task("subTask")
                        .addToDo(new Task("subSubTask")))
                .addGamingSession("Chess", 10)
                .addToDo(new Task("secondSubTask"))
                .addGamingSession("Tekken", 10)
                .addShoppingSession("Groceries", 10, 100,
                        Map.of("Milk", 2, "Bread", 1))
                .addStudySession("Study", 10, List.of("Math", "Physics"))
                .removeByTitle("Chess")
                .removeByTitle("NotInside")
                .removeByTitle("subTask");
        assertFalse(mainTask.isToDoInside("Chess"));
        assertFalse(mainTask.isToDoInside("subTask"));
        assertEquals(4, mainTask.stream()
                .count());
    }

    @Test
    public void nullFilteringStrategyTest() {
        Task task = new Task("aTitle");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> task.filteringStrategy(null));
        assertEquals("Filtering Strategy can not be null", exception.getMessage());
    }

    @Test
    public void getFilteredStreamExceptionTest() {
        Task task = new Task("aTitle");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> task.getFilteredStream());
        assertEquals("Filtering Strategy can not be null", exception.getMessage());
    }

    @Test
    public void getFilteredDescriptionExceptionTest() {
        Task task = new Task("aTitle");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> task.getFilteredDescription());
        assertEquals("Filtering Strategy can not be null", exception.getMessage());
    }
}
