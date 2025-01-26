package tests.operations;
import static org.junit.Assert.*;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import project.*;
import project.operations.DescriptionVisitor;

public class DescriptionVisitorTest {
    private DescriptionVisitor visitor;
    private Task task;

    @Before
    public void setup() {
        this.visitor = new DescriptionVisitor();
        this.task = new Task("aTask");
    }

    @Test
    public void descriptionNoSessionsTest(){
        task.addToDo(new Task("aSecondTask")
                        .addToDo(new Task("aInnerTask")))
                .addToDo(new Task("anotherTask"))
                .priority(10)
                .accept(visitor);
        assertEquals("aTask - Priority: 10"
                +"\nSubTasks:"
                +"\n\taSecondTask"
                +"\n\tSubTasks:"
                +"\n\t\taInnerTask"
                +"\n\tanotherTask", visitor.getDescription());
    }

    @Test
    public void descriptionSimpleTaskTest() {
        task.addToDo(new ExerciseSession("Strength")
                        .addExercise("Push up", 10)
                        .addExercise("Squat", 5))
                .addGamingSession("Tekken", 120)
                .accept(visitor);
        assertEquals("aTask"
                +"\nSubTasks:"
                +"\n\tStrength"
                +"\n\t\tPush up - 10 mins"
                +"\n\t\tSquat - 5 mins"
                +"\n\tTekken - 120 mins", visitor.getDescription());
    }

    @Test
    public void descriptionNestedTaskTest() {
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
                +"\nSubTasks:"
                +"\n\tTekken - 30 mins"
                +"\n\tCardio"
                +"\n\t\tBiking - 20 mins"
                +"\n\t\tRunning - 45 mins"
                +"\n\taNestedTask - Priority: 5"
                +"\n\tSubTasks:"
                +"\n\t\tChess - 35 mins"
                +"\n\t\t\tPlaying on: Pc"
                +"\n\t\t\tMultiplayer: true"
                +"\n\tF1 Simulator - 20 mins"
                +"\n\t\tPlaying on: Playstation"
                +"\n\t\tMultiplayer: false"
                +"\n\tGrocery - 30 mins"
                +"\n\t\tShopping at: Conad"
                +"\n\t\tMax budget: 100"
                +"\n\t\t\tApple - 5"
                +"\n\t\t\tBanana - 10"
                +"\n\tStudying - 60 mins"
                +"\n\t\tMath", visitor.getDescription());
    }
}
