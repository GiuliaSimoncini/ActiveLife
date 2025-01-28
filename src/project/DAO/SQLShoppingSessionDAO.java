package project.DAO;

import project.ShoppingSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class SQLShoppingSessionDAO implements DAO<ShoppingSession, String> {
    private ShoppingSession getHelper(Connection connection, String title) throws SQLException {
        PreparedStatement getLogic = connection.prepareStatement("SELECT id, duration, max_budget, place FROM shopping_session " +
                "WHERE title = ?");
        getLogic.setString(1, title);
        ResultSet result = getLogic.executeQuery();
        if (!result.next())
            throw  new IllegalArgumentException("No shopping session found with title " + title);
        int id = result.getInt("id");
        int duration = result.getInt("duration");
        int maxBudget = result.getInt("max_budget");
        ShoppingSession shoppingSession = new ShoppingSession(title, duration, maxBudget);
        if (result.getString("place") != null)
            shoppingSession.setPlace(result.getString("place"));
        result.close();
        getLogic = connection.prepareStatement("SELECT item_name, item_quantity FROM shopping_session_shopping_list " +
                "WHERE shopping_session_id = ?");
        getLogic.setInt(1, id);
        result = getLogic.executeQuery();
        while (result.next()) {
            shoppingSession.addItem(result.getString("item_name"), result.getInt("item_quantity"));
        }
        return shoppingSession;
    }

    @Override
    public ShoppingSession get(String title) throws SQLException {
        Connection connection = Database.getConnection();
        ShoppingSession shoppingSession = getHelper(connection, title);
        Database.closeConnection(connection);
        return shoppingSession;
    }

    @Override
    public List<ShoppingSession> getAll() throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement getAllLogic = connection.prepareStatement("SELECT id FROM shopping_session");
        ResultSet result = getAllLogic.executeQuery();
        List<Integer> ids = new ArrayList<Integer>();
        while (result.next()) {
            ids.add(result.getInt("id"));
        }
        List<ShoppingSession> shoppingSessions = new ArrayList<ShoppingSession>();
        for (int id : ids) {
            PreparedStatement getTitle = connection.prepareStatement("SELECT title FROM shopping_session " +
                    "WHERE id = ?");
            getTitle.setInt(1, id);
            ResultSet innerResult = getTitle.executeQuery();
            innerResult.next();
            shoppingSessions.add(getHelper(connection, innerResult.getString("title")));
        }
        Database.closeConnection(connection);
        return shoppingSessions;
    }

    @Override
    public void insert(ShoppingSession objectToAdd) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement insertLogic = connection.prepareStatement("INSERT INTO shopping_session " +
                "(title, duration, max_budget, place) VALUES (?, ?, ?, ?)");
        insertLogic.setString(1, objectToAdd.getTitle());
        insertLogic.setInt(2, objectToAdd.getDuration());
        insertLogic.setDouble(3, objectToAdd.getMaxBudget());
        if (objectToAdd.getPlace() != null)
            insertLogic.setString(4, objectToAdd.getPlace());
        else
            insertLogic.setNull(4, java.sql.Types.VARCHAR);
        insertLogic.executeUpdate();
        insertLogic = connection.prepareStatement("SELECT MAX(id) FROM shopping_session " +
                "WHERE title = ?");
        insertLogic.setString(1, objectToAdd.getTitle());
        ResultSet result = insertLogic.executeQuery();
        int idInsert = 0;
        if (result.next()) {
            idInsert = result.getInt(1);
        }
        result.close();
        List<Map.Entry<String,Integer>> newItems = objectToAdd.streamShopping().toList();
        Map<String, Integer> oldItems = new HashMap<String, Integer>();
        insertLogic = connection.prepareStatement("SELECT item, quantity FROM shopping_list");
        result = insertLogic.executeQuery();
        while (result.next()) {
            oldItems.put(result.getString("item"), result.getInt("quantity"));
        }
        for(Map.Entry<String,Integer> item : newItems) {
            if(!oldItems.entrySet().contains(item)) {
                PreparedStatement insert = connection.prepareStatement("INSERT INTO shopping_list (item, quantity) " +
                        "VALUES (?, ?)");
                insert.setString(1, item.getKey());
                insert.setInt(2, item.getValue());
                insert.executeUpdate();
            }
            PreparedStatement insert = connection.prepareStatement("INSERT INTO shopping_session_shopping_list " +
                    "(shopping_session_id, item_name, item_quantity) VALUES (?, ?, ?)");
            insert.setInt(1, idInsert);
            insert.setString(2, item.getKey());
            insert.setInt(3, item.getValue());
            insert.executeUpdate();
        }
        Database.closeConnection(connection);
    }

    @Override
    public void update(ShoppingSession updatedObject, String title) throws SQLException {
        this.remove(title);
        this.insert(updatedObject);
    }

    @Override
    public void remove(String title) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement removeLogic = connection.prepareStatement("SELECT id FROM shopping_session " +
                "WHERE title = ?");
        removeLogic.setString(1, title);
        ResultSet result = removeLogic.executeQuery();
        if (!result.next())
            throw new IllegalArgumentException("No shopping session found with title " + title);
        int id = result.getInt("id");
        result.close();
        removeLogic = connection.prepareStatement("SELECT item_name, item_quantity FROM shopping_session_shopping_list " +
                "WHERE shopping_session_id = ?");
        removeLogic.setInt(1, id);
        result = removeLogic.executeQuery();
        Map<String, Integer> itemsToRemove = new HashMap<String, Integer>();
        while (result.next()) {
            itemsToRemove.put(result.getString("item_name"), result.getInt("item_quantity"));
        }
        removeLogic = connection.prepareStatement("DELETE FROM shopping_session_shopping_list " +
                "WHERE shopping_session_id = ?");
        removeLogic.setInt(1, id);
        removeLogic.executeUpdate();
        removeLogic = connection.prepareStatement("DELETE FROM shopping_session WHERE id = ?");
        removeLogic.setInt(1, id);
        removeLogic.executeUpdate();
        removeLogic = connection.prepareStatement("SELECT item_name, item_quantity FROM shopping_session_shopping_list");
        result = removeLogic.executeQuery();
        Map<String, Integer> itemsPresent = new HashMap<String, Integer>();
        while (result.next()) {
            itemsPresent.put(result.getString("item_name"), result.getInt("item_quantity"));
        }
        for (Map.Entry<String, Integer> item : itemsToRemove.entrySet()) {
            if (!itemsPresent.entrySet().contains(item)) {
                PreparedStatement innerLogic = connection.prepareStatement("DELETE FROM shopping_list " +
                        "WHERE item = ? AND quantity = ?");
                innerLogic.setString(1, item.getKey());
                innerLogic.setInt(2, item.getValue());
                innerLogic.executeUpdate();
            }
        }
        Database.closeConnection(connection);
    }
}

