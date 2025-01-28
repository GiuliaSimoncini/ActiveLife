package tests.DAO;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.After;
import project.DAO.Database;
import project.DAO.SQLExerciseSessionDAO;
import project.ExerciseSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.*;

public class SQLExerciseSessionDAOTest {
    @BeforeClass
    public static void setUp() throws Exception {
        Database.initDatabase();
    }

    private void initialization() throws SQLException {
        SQLExerciseSessionDAO dao = new SQLExerciseSessionDAO();
        ExerciseSession es = new ExerciseSession("Morning Exercise")
                .addExercise("Push-ups", 10)
                .addExercise("Squats", 15)
                .addExercise("Plank", 30);
        dao.insert(es);
        ExerciseSession es2 = new ExerciseSession("Night Exercise")
                .addExercise("Squats", 15)
                .addExercise("Push-ups", 10)
                .addExercise("Lunges", 15);
        dao.insert(es2);
        ExerciseSession es3 = new ExerciseSession("Night Exercise")
                .addExercise("Jumping Jacks", 10)
                .addExercise("Lunges", 15)
                .addExercise("Side Plank", 30);
        dao.insert(es3);
    }

    @Test
    public void getTest() throws SQLException{
        initialization();
        SQLExerciseSessionDAO dao = new SQLExerciseSessionDAO();
        ExerciseSession exerciseSession = dao.get("Night Exercise");
        assertEquals("Night Exercise", exerciseSession.getTitle());
        assertEquals(3, exerciseSession.streamExercises().count());
        assertTrue(exerciseSession.isExerciseInside("Squats",15));
        assertTrue(exerciseSession.isExerciseInside("Push-ups", 10));
        assertTrue(exerciseSession.isExerciseInside("Lunges", 15));
    }

    @Test
    public void getAllTest() throws SQLException {
        initialization();
        SQLExerciseSessionDAO dao = new SQLExerciseSessionDAO();
        List<ExerciseSession> exerciseSessions = dao.getAll();
        assertEquals(3, exerciseSessions.size());
        assertTrue(exerciseSessions.stream()
                .anyMatch(e -> e.getTitle().equals("Morning Exercise")));
        assertTrue(exerciseSessions.stream()
                .anyMatch(e -> e.getTitle().equals("Night Exercise")));
    }

    @Test
    public void removeTest() throws SQLException {
        initialization();
        SQLExerciseSessionDAO dao = new SQLExerciseSessionDAO();
        dao.remove("Night Exercise");
        List<ExerciseSession> exerciseSessions = dao.getAll();
        assertEquals(2, exerciseSessions.size());
        assertTrue(exerciseSessions.stream()
                .anyMatch(e -> e.getTitle().equals("Morning Exercise")));
        assertTrue(exerciseSessions.stream()
                .anyMatch(e -> e.getTitle().equals("Night Exercise")));
        ExerciseSession morningExercise = dao.get("Morning Exercise");
        assertTrue(morningExercise.isExerciseInside("Push-ups",10));
        assertTrue(morningExercise.isExerciseInside("Squats", 15));
        assertTrue(morningExercise.isExerciseInside("Plank", 30));
        ExerciseSession nightExercise = dao.get("Night Exercise");
        assertTrue(nightExercise.isExerciseInside("Jumping Jacks",10));
        assertTrue(nightExercise.isExerciseInside("Lunges", 15));
        assertTrue(nightExercise.isExerciseInside("Side Plank", 30));
    }

    @Test
    public void updateTest() throws SQLException {
        initialization();
        SQLExerciseSessionDAO dao = new SQLExerciseSessionDAO();
        dao.update(new ExerciseSession("Cardio")
                .addExercise("Running", 17)
                .addExercise("Biking", 54),"Night Exercise");
        List<ExerciseSession> exerciseSessions = dao.getAll();
        assertEquals(3, exerciseSessions.size());
        assertTrue(exerciseSessions.stream()
                .anyMatch(e -> e.getTitle().equals("Morning Exercise")));
        assertTrue(exerciseSessions.stream()
                .anyMatch(e -> e.getTitle().equals("Night Exercise")));
        assertTrue(exerciseSessions.stream()
                .anyMatch(e -> e.getTitle().equals("Cardio")));
        ExerciseSession cardio = dao.get("Cardio");
        assertTrue(cardio.isExerciseInside("Running",17));
        assertTrue(cardio.isExerciseInside("Biking", 54));
    }

    @After
    public void tearDown() throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM exercise_session_exercises");
        preparedStatement.executeUpdate();
        preparedStatement = connection.prepareStatement("DELETE FROM exercise_session");
        preparedStatement.executeUpdate();
        preparedStatement = connection.prepareStatement("DELETE FROM exercises");
        preparedStatement.executeUpdate();
        Database.closeConnection(connection);
    }
}