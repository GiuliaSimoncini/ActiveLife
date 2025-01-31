package tests.DAO;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import project.DAO.Database;
import project.DAO.SQLTaskDAO;
import project.*;
import project.operations.DescriptionVisitor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.*;

public class SQLTaskDAOTest {
    private void initialization() throws SQLException {
        SQLTaskDAO dao = new SQLTaskDAO();
        Task task = new Task("Morning tasks")
                .addToDo(new GamingSession("Tekken", 120)
                        .platform("PlayStation 5"))
                .addToDo(new ShoppingSession("Groceries", 60, 30)
                        .addItem("Potatoes", 4)
                        .addItem("Milk", 1))
                .addToDo(new StudySession("Morning study session", 180)
                        .addSubject("Analysis II"))
                .addToDo(new ExerciseSession("HIIT")
                        .addExercise("Squats", 10)
                        .addExercise("Push ups", 10))
                .addToDo(new Task("Early Morning tasks")
                        .priority(10)
                        .addToDo(new GamingSession("FIFA", 20)
                                .platform("PlayStation 5")
                                .multiplayer(true))
                        .addToDo(new ExerciseSession("Yoga")
                                .addExercise("Sun salutation", 10)
                                .addExercise("Warrior pose", 10)))
                .addGamingSession("League of Legends", 30)
                .addToDo(new Task("InnerTask")
                        .addToDo(new Task("InnerInnerTask")));
        dao.insert(task);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        Database.initDatabase();
    }

    @Test
    public void getTest() throws SQLException {
        initialization();
        SQLTaskDAO dao = new SQLTaskDAO();
        DescriptionVisitor visitor = new DescriptionVisitor();
        Task task = dao.get("Morning tasks");
        assertEquals("Morning tasks", task.getTitle());
        assertEquals(7, task.stream().count());
        assertEquals(1, task.stream().filter(t -> t.getTitle().equals("InnerTask")).count());
        task.accept(visitor);
        assertEquals("Morning tasks\n" +
                "SubTasks:\n" +
                "\tTekken - 120 mins\n" +
                "\t\tPlaying on: PlayStation 5\n" +
                "\tLeague of Legends - 30 mins\n" +
                "\tGroceries - 60 mins\n" +
                "\t\tMax budget: 30\n" +
                "\t\t\tPotatoes - 4\n" +
                "\t\t\tMilk - 1\n" +
                "\tHIIT\n" +
                "\t\tSquats - 10 mins\n" +
                "\t\tPush ups - 10 mins\n" +
                "\tMorning study session - 180 mins\n" +
                "\t\tAnalysis II\n" +
                "\tEarly Morning tasks - Priority: 10\n" +
                "\tSubTasks:\n" +
                "\t\tFIFA - 20 mins\n" +
                "\t\t\tPlaying on: PlayStation 5\n" +
                "\t\t\tMultiplayer: true\n" +
                "\t\tYoga\n" +
                "\t\t\tSun salutation - 10 mins\n" +
                "\t\t\tWarrior pose - 10 mins\n" +
                "\tInnerTask\n" +
                "\tSubTasks:\n" +
                "\t\tInnerInnerTask",visitor.getDescription());
    }

    @Test
    public void getAllTest() throws SQLException {
        initialization();
        SQLTaskDAO dao = new SQLTaskDAO();
        List<Task> tasks = dao.getAll();
        assertEquals(4, tasks.size());
        assertEquals(7, tasks.get(0).stream().count());
        assertTrue(tasks.stream().anyMatch(t -> t.getTitle().equals("Morning tasks")));
        assertTrue(tasks.stream().anyMatch(t -> t.getTitle().equals("InnerTask")));
        assertTrue(tasks.stream().anyMatch(t -> t.getTitle().equals("InnerInnerTask")));
        assertTrue(tasks.stream().anyMatch(t -> t.getTitle().equals("Early Morning tasks")));
        Task task = tasks.stream()
                .filter(t -> t.getTitle().equals("Early Morning tasks"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Task not found"));
        DescriptionVisitor visitor = new DescriptionVisitor();
        task.accept(visitor);
        assertEquals("Early Morning tasks - Priority: 10\n" +
                "SubTasks:\n" +
                "\tFIFA - 20 mins\n" +
                "\t\tPlaying on: PlayStation 5\n" +
                "\t\tMultiplayer: true\n" +
                "\tYoga\n" +
                "\t\tSun salutation - 10 mins\n" +
                "\t\tWarrior pose - 10 mins", visitor.getDescription());
    }

    @Test
    public void removeTest() throws SQLException {
        initialization();
        SQLTaskDAO dao = new SQLTaskDAO();
        Task task1 = new Task("test task");
        dao.insert(task1.addToDo(new GamingSession("test game", 30)));
        dao.remove("Early Morning tasks");
        assertEquals(4, dao.getAll().size());
        assertTrue(dao.getAll().stream().anyMatch(t -> t.getTitle().equals("Morning tasks")));
        assertTrue(dao.getAll().stream().anyMatch(t -> t.getTitle().equals("test task")));
        assertTrue(dao.getAll().stream().anyMatch(t -> t.getTitle().equals("InnerTask")));
        assertTrue(dao.getAll().stream().anyMatch(t -> t.getTitle().equals("InnerInnerTask")));
        DescriptionVisitor visitor = new DescriptionVisitor();
        dao.get("Morning tasks").accept(visitor);
        assertEquals("Morning tasks\n" +
                "SubTasks:\n" +
                "\tTekken - 120 mins\n" +
                "\t\tPlaying on: PlayStation 5\n" +
                "\tLeague of Legends - 30 mins\n" +
                "\tGroceries - 60 mins\n" +
                "\t\tMax budget: 30\n" +
                "\t\t\tPotatoes - 4\n" +
                "\t\t\tMilk - 1\n" +
                "\tHIIT\n" +
                "\t\tSquats - 10 mins\n" +
                "\t\tPush ups - 10 mins\n" +
                "\tMorning study session - 180 mins\n" +
                "\t\tAnalysis II\n" +
                "\tInnerTask\n" +
                "\tSubTasks:\n" +
                "\t\tInnerInnerTask", visitor.getDescription());
    }

    @Test
    public void updateTest() throws SQLException {
        initialization();
        SQLTaskDAO dao = new SQLTaskDAO();
        dao.update(new Task("Updated Morning tasks")
                .addToDo(new Task("Updated Inner Task")
                        .addGamingSession("Updated Gaming", 20))
                .addToDo(new ExerciseSession("Updated Exercise")
                        .addExercise("Running", 10)
                        .addExercise("Jumping", 25)), "Morning tasks");
        Task task2 = dao.get("Updated Morning tasks");
        DescriptionVisitor visitor = new DescriptionVisitor();
        task2.accept(visitor);
        assertEquals("Updated Morning tasks\n" +
                "SubTasks:\n" +
                "\tUpdated Exercise\n" +
                "\t\tRunning - 10 mins\n" +
                "\t\tJumping - 25 mins\n" +
                "\tUpdated Inner Task\n" +
                "\tSubTasks:\n" +
                "\t\tUpdated Gaming - 20 mins", visitor.getDescription());
    }

    @After
    public void tearDown() throws SQLException {
        Connection connection = Database.getConnection();
        //GAMING SESSION
        PreparedStatement tearDown = connection.prepareStatement("DELETE FROM task_gaming_session");
        tearDown.executeUpdate();
        tearDown = connection.prepareStatement("DELETE FROM gaming_session");
        tearDown.executeUpdate();
        //SHOPPING SESSION
        tearDown = connection.prepareStatement("DELETE FROM task_shopping_session");
        tearDown.executeUpdate();
        tearDown = connection.prepareStatement("DELETE FROM shopping_session_shopping_list");
        tearDown.executeUpdate();
        tearDown = connection.prepareStatement("DELETE FROM shopping_session");
        tearDown.executeUpdate();
        tearDown = connection.prepareStatement("DELETE FROM shopping_list");
        tearDown.executeUpdate();
        //STUDY SESSION
        tearDown = connection.prepareStatement("DELETE FROM task_study_session");
        tearDown.executeUpdate();
        tearDown = connection.prepareStatement("DELETE FROM study_session_subjects");
        tearDown.executeUpdate();
        tearDown = connection.prepareStatement("DELETE FROM study_session");
        tearDown.executeUpdate();
        tearDown = connection.prepareStatement("DELETE FROM subjects");
        tearDown.executeUpdate();
        //EXERCISE SESSION
        tearDown = connection.prepareStatement("DELETE FROM task_exercise_session");
        tearDown.executeUpdate();
        tearDown = connection.prepareStatement("DELETE FROM exercise_session_exercises");
        tearDown.executeUpdate();
        tearDown = connection.prepareStatement("DELETE FROM exercises");
        tearDown.executeUpdate();
        tearDown = connection.prepareStatement("DELETE FROM exercise_session");
        tearDown.executeUpdate();
        //TASK
        tearDown = connection.prepareStatement("DELETE FROM task_task");
        tearDown.executeUpdate();
        tearDown = connection.prepareStatement("DELETE FROM task");
        tearDown.executeUpdate();

        Database.closeConnection(connection);
    }
}
