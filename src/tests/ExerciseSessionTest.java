package tests;
import project.ExerciseSession;
import static org.junit.Assert.*;
import org.junit.Test;

public class ExerciseSessionTest {
    @Test
    public void addExerciseTest() {
        ExerciseSession exerciseSession = new ExerciseSession("Cardio");
        exerciseSession.addExercise("Running", 10)
                .addExercise("Biking", 20)
                .addExercise("Jumping Rope", 15);
        assertEquals(3, exerciseSession.streamExercises()
                .count());
        assertTrue(exerciseSession.isExerciseInside("Running", 10));
    }

    @Test
    public void addExerciseExceptionTest() {
        ExerciseSession exerciseSession = new ExerciseSession("Cardio");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> exerciseSession.addExercise("", 10));
        assertEquals("The name of an exercises can not be empty", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> exerciseSession.addExercise(null, 10));
        assertEquals("The name of an exercises can not be null", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> exerciseSession.addExercise("aValidTitle", 0));
        assertEquals("The minutes spent on an exercise can not be 0 or less than 0", exception.getMessage());
    }

    @Test
    public void modifyMinsExerciseTest() {
        ExerciseSession exerciseSession = new ExerciseSession("Cardio")
                .addExercise("Running", 10)
                .modifyMinsExercise("Running", 25);
        assertTrue(exerciseSession.isExerciseInside("Running", 25));
        assertFalse(exerciseSession.isExerciseInside("Running", 10));
    }

    @Test
    public void modifyMinsExerciseExceptionTest() {
        ExerciseSession exerciseSession = new ExerciseSession("Cardio")
                .addExercise("Running", 10);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> exerciseSession.modifyMinsExercise("Biking", 60));
        assertEquals("The exercise in input has not been inserted yet", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> exerciseSession.modifyMinsExercise("Running", -2));
        assertEquals("The minutes spent on an exercise can not be 0 or less than 0", exception.getMessage());
    }
}
