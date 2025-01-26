package tests.operations;
import static org.junit.Assert.*;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import project.*;
import project.operations.DurationVisitor;

public class DurationVisitorTest {
    private DurationVisitor visitor;
    private Task task;

    @Before
    public void setup() {
        this.visitor = new DurationVisitor();
        this.task = new Task("aTask");
    }

    @Test
    public void durationNoSessionsTest() {
        task.addToDo(new Task("aSecondTask")
                        .addToDo(new Task("aInerTask")))
                .addToDo(new Task("anotherTask"))
                .accept(visitor);
        assertEquals(0, visitor.getDuration());
    }

    @Test
    public void durationSimpleTaskTest() {
        task.addToDo(new ExerciseSession("Strength")
                        .addExercise("Push up", 10)
                        .addExercise("Squat", 5))
                .addGamingSession("Tekken", 120)
                .accept(visitor);
        assertEquals(135, visitor.getDuration());
    }

    @Test
    public void durationNestedTaskTest() {
        Map<String, Integer> exercises = new LinkedHashMap<String, Integer>();
        exercises.put("Biking", 20);
        exercises.put("Running", 45);
        task.addToDo(new Task("aSecondTask")
                        .addGamingSession("Tekken", 30)
                        .addExerciseSession("Cardio", exercises)
                        .addToDo(new Task("aNestedTask")
                                .addGamingSession("Chess", 30)))
                .addToDo(new ExerciseSession("Strength")
                        .addExercise("Push up", 10)
                        .addExercise("Squat", 5))
                .addToDo(new StudySession("Math", 60))
                .addToDo(new ShoppingSession("Grocery", 30, 100))
                .accept(visitor);
        assertEquals(230, visitor.getDuration());
    }
}
