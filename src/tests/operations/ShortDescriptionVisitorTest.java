package tests.operations;
import static org.junit.Assert.*;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import project.*;
import project.operations.ShortDescriptionVisitor;

public class ShortDescriptionVisitorTest {
    private ShortDescriptionVisitor visitor;
    private Task task;

    @Before
    public void setup() {
        this.visitor = new ShortDescriptionVisitor();
        this.task = new Task("aTask");
    }

    @Test
    public void shortDescriptionNoSessionsTest(){
        task.addToDo(new Task("aSecondTask")
                        .addToDo(new Task("aInnerTask")))
                .addToDo(new Task("anotherTask"))
                .accept(visitor);
        assertEquals("aTask"
                +"\n\taSecondTask"
                +"\n\t\taInnerTask"
                +"\n\tanotherTask", visitor.getShortDescription());
    }

    @Test
    public void shortDescriptionSimpleTaskTest() {
        task.addToDo(new ExerciseSession("Strength")
                        .addExercise("Push up", 10)
                        .addExercise("Squat", 5))
                .addGamingSession("Tekken", 120)
                .accept(visitor);
        assertEquals("aTask"
                +"\n\tStrength"
                +"\n\tTekken", visitor.getShortDescription());
    }

    @Test
    public void shortDescriptionNestedTaskTest() {
        Map<String, Integer> exercises = new LinkedHashMap<String, Integer>();
        exercises.put("Biking", 20);
        exercises.put("Running", 45);
        task.addGamingSession("Tekken", 30)
                .addExerciseSession("Cardio", exercises)
                .addToDo(new Task("aNestedTask")
                        .addToDo(new GamingSession("Chess", 35)
                                .multiplayer(true)
                                .platform("Pc"))
                        .priority(5))
                .addToDo(new GamingSession("F1 Simulator", 20)
                        .multiplayer(false)
                        .platform("Playstation"))
                .addToDo(new ShoppingSession("Grocery", 30, 100)
                        .addItem("Apple", 5)
                        .addItem("Banana", 10)
                        .setPlace("Conad"))
                .addToDo(new StudySession("Studying", 60)
                        .addSubject("Math"))
                .accept(visitor);
        assertEquals("aTask"
                +"\n\tTekken"
                +"\n\tCardio"
                +"\n\taNestedTask"
                +"\n\t\tChess"
                +"\n\tF1 Simulator"
                +"\n\tGrocery"
                +"\n\tStudying", visitor.getShortDescription());
    }
}