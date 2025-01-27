package tests.DAO;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import project.DAO.Database;
import project.DAO.SQLStudySessionDAO;
import project.StudySession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.*;

public class SQLStudySessionDAOTest {
    private void initialization() throws SQLException {
        SQLStudySessionDAO dao = new SQLStudySessionDAO();
        StudySession s1 = new StudySession("Math", 60)
                .addSubject("Algebra")
                .addSubject("Geometry");
        dao.insert(s1);
        StudySession s2 = new StudySession("Physics", 120)
                .addSubject("Mechanics")
                .addSubject("Thermodynamics")
                .addSubject("Particle Physics");
        dao.insert(s2);
        StudySession s3 = new StudySession("Physics", 90)
                .addSubject("Thermodynamics")
                .addSubject("Optics");
        dao.insert(s3);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        Database.initDatabase();
    }

    @Test
    public void getTest() throws SQLException {
        initialization();
        SQLStudySessionDAO dao = new SQLStudySessionDAO();
        StudySession s1 = dao.get("Math");
        assertEquals("Math", s1.getTitle());
        assertEquals(60, s1.getDuration());
        assertEquals(2, s1.streamSubjects().count());
        assertTrue(s1.streamSubjects().anyMatch(s -> s.equals("Algebra")));
        assertTrue(s1.streamSubjects().anyMatch(s -> s.equals("Geometry")));
        StudySession s2 = dao.get("Physics");
        assertEquals("Physics", s2.getTitle());
        assertEquals(120, s2.getDuration());
        assertEquals(3, s2.streamSubjects().count());
        assertTrue(s2.streamSubjects().anyMatch(s -> s.equals("Mechanics")));
        assertTrue(s2.streamSubjects().anyMatch(s -> s.equals("Thermodynamics")));
        assertTrue(s2.streamSubjects().anyMatch(s -> s.equals("Particle Physics")));
    }

    @Test
    public void getAllTest() throws SQLException {
        initialization();
        SQLStudySessionDAO dao = new SQLStudySessionDAO();
        List<StudySession> studySessions = dao.getAll();
        assertEquals(3, studySessions.size());
        assertTrue(studySessions.stream().anyMatch(s -> s.getTitle().equals("Math")));
        assertTrue(studySessions.stream().anyMatch(s -> s.getTitle().equals("Physics")));
    }

    @Test
    public void removeTest() throws SQLException {
        initialization();
        SQLStudySessionDAO dao = new SQLStudySessionDAO();
        dao.remove("Physics");
        StudySession s1 = dao.get("Math");
        assertEquals("Math", s1.getTitle());
        assertEquals(60, s1.getDuration());
        assertEquals(2, s1.streamSubjects().count());
        assertTrue(s1.streamSubjects().anyMatch(s -> s.equals("Algebra")));
        assertTrue(s1.streamSubjects().anyMatch(s -> s.equals("Geometry")));
        StudySession s2 = dao.get("Physics");
        assertEquals("Physics", s2.getTitle());
        assertEquals(90, s2.getDuration());
        assertEquals(2, s2.streamSubjects().count());
        assertTrue(s2.streamSubjects().anyMatch(s -> s.equals("Optics")));
        assertTrue(s2.streamSubjects().anyMatch(s -> s.equals("Thermodynamics")));
    }

    @Test
    public void updateTest() throws SQLException {
        initialization();
        SQLStudySessionDAO dao = new SQLStudySessionDAO();
        StudySession s1 = new StudySession("Advanced Math", 90)
                .addSubject("Calculus")
                .addSubject("Linear Algebra")
                .addSubject("Differential Equations")
                .addSubject("Geometry");
        dao.update(s1, "Math");
        StudySession s2 = dao.get("Advanced Math");
        assertEquals("Advanced Math", s2.getTitle());
        assertEquals(90, s2.getDuration());
        assertEquals(4, s2.streamSubjects().count());
        assertTrue(s2.streamSubjects().anyMatch(s -> s.equals("Calculus")));
        assertTrue(s2.streamSubjects().anyMatch(s -> s.equals("Linear Algebra")));
        assertTrue(s2.streamSubjects().anyMatch(s -> s.equals("Differential Equations")));
        assertTrue(s2.streamSubjects().anyMatch(s -> s.equals("Geometry")));
    }

    @After
    public void tearDown() throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement tearDown = connection.prepareStatement("DELETE FROM study_session_subjects");
        tearDown.executeUpdate();
        tearDown = connection.prepareStatement("DELETE FROM study_session");
        tearDown.executeUpdate();
        tearDown = connection.prepareStatement("DELETE FROM subjects");
        tearDown.executeUpdate();
        Database.closeConnection(connection);
    }
}
