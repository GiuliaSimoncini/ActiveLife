package project.DAO;

import project.GamingSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLGamingSessionDAO implements DAO<GamingSession, String> {
    @Override
    public GamingSession get(String title) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement getLogic = connection.prepareStatement("SELECT id FROM gaming_session "
                + "WHERE title = ?");
        getLogic.setString(1, title);
        ResultSet result = getLogic.executeQuery();
        if (!result.next())
            throw new IllegalArgumentException("No gaming session found with title " + title);
        int id = result.getInt("id");
        getLogic = connection.prepareStatement("SELECT title, duration, multiplayer, platform " +
                "FROM gaming_session WHERE id = ?");
        getLogic.setInt(1, id);
        result.close();
        result = getLogic.executeQuery();
        result.next();
        GamingSession gamingSession = new GamingSession(result.getString("title"),
                result.getInt("duration"));
        Boolean multiplayer = result.getBoolean("multiplayer");
        if (!result.wasNull())
            gamingSession.multiplayer(multiplayer);
        String platform = result.getString("platform");
        if (!result.wasNull())
            gamingSession.platform(platform);
        Database.closeConnection(connection);
        return gamingSession;
    }

    @Override
    public List<GamingSession> getAll() throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement getAllLogic = connection.prepareStatement("SELECT * FROM gaming_session");
        ResultSet results = getAllLogic.executeQuery();
        List<GamingSession> gamingSessions = new ArrayList<GamingSession>();
        while (results.next()) {
            GamingSession gamingSession = new GamingSession(results.getString("title"),
                    results.getInt("duration"));

            Boolean multiplayer = results.getBoolean("multiplayer");
            if (!results.wasNull())
                gamingSession.multiplayer(multiplayer);
            String platform = results.getString("platform");
            if (!results.wasNull())
                gamingSession.platform(platform);
            gamingSessions.add(gamingSession);
        }
        Database.closeConnection(connection);
        return gamingSessions;
    }

    @Override
    public void insert(GamingSession objectToAdd) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement checkingDuplicates = connection.prepareStatement("SELECT COUNT(*) FROM gaming_session " +
                "WHERE title = ? AND duration = ? AND multiplayer = ? AND platform = ?");
        checkingDuplicates.setString(1, objectToAdd.getTitle());
        checkingDuplicates.setInt(2, objectToAdd.getDuration());
        if (objectToAdd.getMultiplayer() != null)
            checkingDuplicates.setBoolean(3, objectToAdd.getMultiplayer());
        else
            checkingDuplicates.setNull(3, java.sql.Types.BOOLEAN);
        if (objectToAdd.getPlatform() != null)
            checkingDuplicates.setString(4, objectToAdd.getPlatform());
        else
            checkingDuplicates.setNull(4, java.sql.Types.VARCHAR);
        ResultSet result = checkingDuplicates.executeQuery();
        int count = 0;
        if (result.next())
            count = result.getInt(1);
        if (count > 0) {
            Database.closeConnection(connection);
            return;
        }
        PreparedStatement insertLogic = connection.prepareStatement("INSERT INTO gaming_session " +
                "(title, duration, multiplayer, platform) VALUES  (?,?,?,?)");
        insertLogic.setString(1, objectToAdd.getTitle());
        insertLogic.setInt(2, objectToAdd.getDuration());
        if (objectToAdd.getMultiplayer() != null)
            insertLogic.setBoolean(3, objectToAdd.getMultiplayer());
        else
            insertLogic.setNull(3, java.sql.Types.BOOLEAN);
        if (objectToAdd.getPlatform() != null)
            insertLogic.setString(4, objectToAdd.getPlatform());
        else
            insertLogic.setNull(4, java.sql.Types.VARCHAR);
        insertLogic.executeUpdate();
        Database.closeConnection(connection);
    }

    @Override
    public void update(GamingSession updatedObject, String title) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement updateLogic = connection.prepareStatement("SELECT id FROM gaming_session " +
                "WHERE title = ?");
        updateLogic.setString(1, title);
        ResultSet result = updateLogic.executeQuery();
        if (!result.next())
            throw new IllegalArgumentException("No gaming session found with title " + title);
        int id = result.getInt("id");
        updateLogic = connection.prepareStatement("UPDATE gaming_session SET (title, duration, multiplayer, platform) "
                + "= (?, ?, ?, ? ) WHERE id = ?");
        updateLogic.setString(1, updatedObject.getTitle());
        updateLogic.setInt(2, updatedObject.getDuration());
        if (updatedObject.getMultiplayer() != null)
            updateLogic.setBoolean(3, updatedObject.getMultiplayer());
        else
            updateLogic.setNull(3, java.sql.Types.BOOLEAN);
        if (updatedObject.getPlatform() != null)
            updateLogic.setString(4, updatedObject.getPlatform());
        else
            updateLogic.setNull(4, java.sql.Types.VARCHAR);
        updateLogic.setInt(5, id);
        updateLogic.executeUpdate();
        Database.closeConnection(connection);
    }

    @Override
    public void remove(String title) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement removeLogic = connection.prepareStatement("SELECT id FROM gaming_session " +
                "WHERE title = ?");
        removeLogic.setString(1, title);
        ResultSet result = removeLogic.executeQuery();
        if (!result.next())
            throw new IllegalArgumentException("No gaming session found with title " + title);
        int id = result.getInt("id");
        removeLogic = connection.prepareStatement("DELETE FROM gaming_session " +
                "WHERE title = ? AND id = ?");
        removeLogic.setString(1, title);
        removeLogic.setInt(2, id);
        removeLogic.executeUpdate();
        Database.closeConnection(connection);
    }
}

