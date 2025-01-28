package tests.DAO;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import project.DAO.Database;
import project.DAO.SQLShoppingSessionDAO;
import project.ShoppingSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.*;

public class SQLShoppingSessionDAOTest {
    private void initialization() throws SQLException {
        SQLShoppingSessionDAO dao = new SQLShoppingSessionDAO();
        ShoppingSession s1 = new ShoppingSession("Groceries", 60, 100)
                .addItem("Apple", 2)
                .addItem("Banana", 3)
                .setPlace("Supermarket");
        dao.insert(s1);
        ShoppingSession s2 = new ShoppingSession("Electronics", 120, 200)
                .addItem("Smartwatch", 1)
                .addItem("Smartphone", 1);
        dao.insert(s2);
        ShoppingSession s3 = new ShoppingSession("Electronics", 90, 300)
                .addItem("Smartphone", 1)
                .addItem("Laptop", 1)
                .setPlace("Tech Store");
        dao.insert(s3);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        Database.initDatabase();
    }

    @Test
    public void getTest() throws SQLException {
        initialization();
        SQLShoppingSessionDAO dao = new SQLShoppingSessionDAO();
        ShoppingSession s1 = dao.get("Groceries");
        assertEquals("Groceries", s1.getTitle());
        assertEquals(60, s1.getDuration());
        assertEquals(100, s1.getMaxBudget());
        assertEquals("Supermarket", s1.getPlace());
        assertEquals(2, s1.streamShopping().count());
        assertTrue(s1.streamShopping().anyMatch(e -> e.getKey().equals("Apple") && e.getValue() == 2));
        assertTrue(s1.streamShopping().anyMatch(e -> e.getKey().equals("Banana") && e.getValue() == 3));
        ShoppingSession s2 = dao.get("Electronics");
        assertEquals("Electronics", s2.getTitle());
        assertEquals(120, s2.getDuration());
        assertEquals(200, s2.getMaxBudget());
        assertNull(s2.getPlace());
        assertEquals(2, s2.streamShopping().count());
        assertTrue(s2.streamShopping().anyMatch(e -> e.getKey().equals("Smartwatch") && e.getValue() == 1));
        assertTrue(s2.streamShopping().anyMatch(e -> e.getKey().equals("Smartphone") && e.getValue() == 1));
    }

    @Test
    public void getAllTest() throws SQLException {
        initialization();
        SQLShoppingSessionDAO dao = new SQLShoppingSessionDAO();
        List<ShoppingSession> shoppingSessions = dao.getAll();
        assertEquals(3, shoppingSessions.size());
        assertTrue(shoppingSessions.stream()
                .anyMatch(e -> e.getTitle().equals("Groceries")));
        assertTrue(shoppingSessions.stream()
                .anyMatch(e -> e.getTitle().equals("Electronics")));
    }

    @Test
    public void removeTest() throws SQLException {
        initialization();
        SQLShoppingSessionDAO dao = new SQLShoppingSessionDAO();
        dao.remove("Electronics");
        List<ShoppingSession> shoppingSessions = dao.getAll();
        assertEquals(2, shoppingSessions.size());
        assertTrue(shoppingSessions.stream()
                .anyMatch(e -> e.getTitle().equals("Groceries")));
        assertTrue(shoppingSessions.stream()
                .anyMatch(e -> e.getTitle().equals("Electronics")));
        ShoppingSession s1 = dao.get("Groceries");
        assertEquals("Groceries", s1.getTitle());
        assertEquals(60, s1.getDuration());
        assertEquals(100, s1.getMaxBudget());
        assertEquals("Supermarket", s1.getPlace());
        assertEquals(2, s1.streamShopping().count());
        assertTrue(s1.streamShopping().anyMatch(e -> e.getKey().equals("Apple") && e.getValue() == 2));
        assertTrue(s1.streamShopping().anyMatch(e -> e.getKey().equals("Banana") && e.getValue() == 3));
        ShoppingSession s2 = dao.get("Electronics");
        assertEquals("Electronics", s2.getTitle());
        assertEquals(90, s2.getDuration());
        assertEquals(300, s2.getMaxBudget());
        assertEquals("Tech Store", s2.getPlace());
        assertEquals(2, s2.streamShopping().count());
        assertTrue(s2.streamShopping().anyMatch(e -> e.getKey().equals("Smartphone") && e.getValue() == 1));
        assertTrue(s2.streamShopping().anyMatch(e -> e.getKey().equals("Laptop") && e.getValue() == 1));
    }

    @Test
    public void updateTest() throws SQLException {
        initialization();
        SQLShoppingSessionDAO dao = new SQLShoppingSessionDAO();
        ShoppingSession s1 = new ShoppingSession("Electronics 2", 15, 10000)
                .addItem("Smartwatch", 1)
                .addItem("Laptop", 1)
                .addItem("Television", 10)
                .setPlace("Tech Shop");
        dao.update(s1, "Electronics");
        ShoppingSession s2 = dao.get("Electronics 2");
        assertEquals("Electronics 2", s2.getTitle());
        assertEquals(15, s2.getDuration());
        assertEquals(10000, s2.getMaxBudget());
        assertEquals("Tech Shop", s2.getPlace());
        assertEquals(3, s2.streamShopping().count());
        assertTrue(s2.streamShopping().anyMatch(e -> e.getKey().equals("Smartwatch") && e.getValue() == 1));
        assertTrue(s2.streamShopping().anyMatch(e -> e.getKey().equals("Laptop") && e.getValue() == 1));
        assertTrue(s2.streamShopping().anyMatch(e -> e.getKey().equals("Television") && e.getValue() == 10));
    }

    @After
    public void tearDown() throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement tearDown = connection.prepareStatement("DELETE FROM shopping_session_shopping_list");
        tearDown.executeUpdate();
        tearDown = connection.prepareStatement("DELETE FROM shopping_session");
        tearDown.executeUpdate();
        tearDown = connection.prepareStatement("DELETE FROM shopping_list");
        tearDown.executeUpdate();
        Database.closeConnection(connection);
    }
}
