package tests.DAO;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import project.DAO.Database;
import project.DAO.SQLGamingSessionDAO;
import project.GamingSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SQLGamingSessionDAOTest {
    private void initialization() throws SQLException {
        SQLGamingSessionDAO dao = new SQLGamingSessionDAO();
        GamingSession marioKart = new GamingSession("MarioKart", 120)
                .platform("PC")
                .multiplayer(true);
        GamingSession marioKart2 = new GamingSession("MarioKart",120);
        GamingSession marioBros = new GamingSession("MarioBros", 30)
                .platform("Nintendo Switch");
        dao.insert(marioKart);
        dao.insert(marioKart2);
        dao.insert(marioBros);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        Database.initDatabase();
    }

    @Test
    public void getTest() throws SQLException {
        initialization();
        SQLGamingSessionDAO dao = new SQLGamingSessionDAO();
        GamingSession getMarioKart = dao.get("MarioKart");
        assertEquals("MarioKart", getMarioKart.getTitle());
        assertEquals(120, getMarioKart.getDuration());
        assertEquals("PC", getMarioKart.getPlatform());
        assertEquals(true, getMarioKart.getMultiplayer());
    }

    @Test
    public void getAllTest() throws SQLException {
        initialization();
        SQLGamingSessionDAO dao = new SQLGamingSessionDAO();
        List<GamingSession> gamingSessions = dao.getAll();
        assertEquals(3, gamingSessions.size());
        assertTrue(gamingSessions.stream()
                .allMatch(gs -> gs.getTitle()
                        .equals("MarioKart") || gs.getTitle().equals("MarioBros")));
    }

    @Test
    public void removeTest() throws SQLException {
        initialization();
        SQLGamingSessionDAO dao = new SQLGamingSessionDAO();
        dao.remove("MarioKart");
        GamingSession getMarioKart = dao.get("MarioKart");
        assertEquals("MarioKart", getMarioKart.getTitle());
        assertEquals(120, getMarioKart.getDuration());
        assertNull(getMarioKart.getPlatform());
        assertNull(getMarioKart.getMultiplayer());
        List<GamingSession> gamingSessions = dao.getAll();
        assertEquals(2, gamingSessions.size());
    }

    @Test
    public void updateTest() throws SQLException {
        initialization();
        SQLGamingSessionDAO dao = new SQLGamingSessionDAO();
        GamingSession updatedGamingSession = new GamingSession("UpdatedMarioKart", 50);
        dao.update(updatedGamingSession, "MarioKart");
        GamingSession getUpdated = dao.get("UpdatedMarioKart");
        assertEquals("UpdatedMarioKart", getUpdated.getTitle());
        assertEquals(50, getUpdated.getDuration());
        assertNull(getUpdated.getPlatform());
        assertNull(getUpdated.getMultiplayer());
    }

    @Test
    public void updateThrowsTest() throws SQLException {
        initialization();
        SQLGamingSessionDAO dao = new SQLGamingSessionDAO();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> dao.update(new GamingSession("notPresent", 2), "notPresent"));
        assertEquals("No gaming session found with title notPresent", exception.getMessage());
    }

    @After
    public void tearDown() throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement tearDown = connection.prepareStatement("DELETE FROM gaming_session");
        tearDown.executeUpdate();
        Database.closeConnection(connection);
    }
}