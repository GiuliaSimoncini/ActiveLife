package tests;
import org.junit.Before;
import org.junit.Test;
import project.ShoppingSession;
import static org.junit.Assert.*;

public class ShoppingSessionTest {
    private ShoppingSession shoppingSession;

    @Before
    public void setUp() {
        shoppingSession = new ShoppingSession("Shopping", 60, 100);
    }

    @Test
    public void addItemExceptionTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingSession.addItem(null, 1);
        });
        assertEquals("Item can not be null", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingSession.addItem("", 1);
        });
        assertEquals("Item can not be empty", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingSession.addItem("item", 0);
        });
        assertEquals("Quantity must be a positive number", exception.getMessage());
    }

    @Test
    public void addItemTest() {
        shoppingSession.addItem("item1", 1);
        shoppingSession.addItem("item2", 2);
        assertEquals(2, shoppingSession.streamShopping().count());
    }

    @Test
    public void setPlaceExceptionTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingSession.setPlace(null);
        });
        assertEquals("Place can not be null", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingSession.setPlace("");
        });
        assertEquals("Place can not be empty", exception.getMessage());
    }
}
