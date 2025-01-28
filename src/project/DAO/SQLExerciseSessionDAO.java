package project.DAO;

import project.ExerciseSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SQLExerciseSessionDAO implements DAO<ExerciseSession, String> {
    private ExerciseSession getHelper(Connection connection, String title) throws SQLException {
        PreparedStatement getLogic = connection.prepareStatement("SELECT id FROM exercise_session " +
                "WHERE title = ?");
        getLogic.setString(1, title);
        ResultSet result = getLogic.executeQuery();
        if (!result.next())
            throw  new IllegalArgumentException("No exercise session found with title " + title);
        int id = result.getInt("id");
        result.close();
        ExerciseSession exerciseSession = new ExerciseSession(title);
        getLogic = connection.prepareStatement("SELECT exercise_name, exercise_duration FROM exercise_session_exercises " +
                "WHERE exercise_session_id = ?");
        getLogic.setInt(1, id);
        result = getLogic.executeQuery();
        while (result.next()) {
            exerciseSession.addExercise(result.getString("exercise_name"), result.getInt("exercise_duration"));
        }
        return exerciseSession;
    }

    @Override
    public ExerciseSession get(String title) throws SQLException {
        Connection connection = Database.getConnection();
        ExerciseSession exerciseSession = getHelper(connection, title);
        Database.closeConnection(connection);
        return exerciseSession;
    }

    @Override
    public List<ExerciseSession> getAll() throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement getAllLogic = connection.prepareStatement("SELECT id FROM exercise_session");
        ResultSet result = getAllLogic.executeQuery();
        List<Integer> ids = new ArrayList<Integer>();
        while (result.next()) {
            ids.add(result.getInt("id"));
        }
        List<ExerciseSession> exerciseSessions = new ArrayList<ExerciseSession>();
        for (int id : ids) {
            PreparedStatement getTitle = connection.prepareStatement("SELECT title FROM exercise_session " +
                    "WHERE id = ?");
            getTitle.setInt(1, id);
            ResultSet innerResult = getTitle.executeQuery();
            innerResult.next();
            exerciseSessions.add(getHelper(connection, innerResult.getString("title")));
        }
        Database.closeConnection(connection);
        return exerciseSessions;
    }

    @Override
    public void insert(ExerciseSession objectToAdd) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement insertLogic = connection.prepareStatement("INSERT INTO exercise_session (title) " +
                "VALUES (?)");
        insertLogic.setString(1, objectToAdd.getTitle());
        insertLogic.executeUpdate();
        insertLogic = connection.prepareStatement("SELECT MAX(id) FROM  exercise_session WHERE title = ?");
        insertLogic.setString(1, objectToAdd.getTitle());
        ResultSet result = insertLogic.executeQuery();
        int idInsert = 0;
        if (result.next())
            idInsert = result.getInt(1);
        result.close();
        List<Map.Entry<String,Integer>> newExercises = objectToAdd.streamExercises().toList();
        Map<String, Integer> oldExercises = new HashMap<String, Integer>();
        insertLogic = connection.prepareStatement("SELECT name, duration FROM exercises");
        result = insertLogic.executeQuery();
        while (result.next()) {
            oldExercises.put(result.getString("name"), result.getInt("duration"));
        }
        for (Map.Entry<String, Integer> exercise : newExercises) {
            if (!oldExercises.entrySet().contains(exercise)) {
                PreparedStatement insert = connection.prepareStatement("INSERT INTO exercises (name, duration) " +
                        "VALUES (?,?)");
                insert.setString(1, exercise.getKey());
                insert.setInt(2, exercise.getValue());
                insert.executeUpdate();
            }
            PreparedStatement insert = connection.prepareStatement("INSERT INTO exercise_session_exercises " +
                    "(exercise_session_id, exercise_name, exercise_duration) " +
                    "VALUES (?,?,?)");
            insert.setInt(1, idInsert);
            insert.setString(2, exercise.getKey());
            insert.setInt(3, exercise.getValue());
            insert.executeUpdate();
        }
        Database.closeConnection(connection);
    }

    @Override
    public void update(ExerciseSession updatedObject, String title) throws SQLException {
        this.remove(title);
        this.insert(updatedObject);
    }

    @Override
    public void remove(String title) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement removeLogic = connection.prepareStatement("SELECT id FROM exercise_session " +
                "WHERE title = ?");
        removeLogic.setString(1, title);
        ResultSet result = removeLogic.executeQuery();
        if (!result.next())
            throw new IllegalArgumentException("No exercise session found with title " + title);
        int id = result.getInt("id");
        result.close();
        removeLogic = connection.prepareStatement("SELECT exercise_name, exercise_duration FROM exercise_session_exercises " +
                "WHERE exercise_session_id = ?");
        removeLogic.setInt(1, id);
        result = removeLogic.executeQuery();
        Map<String, Integer> exercisesToRemove = new HashMap<String, Integer>();
        while (result.next())
            exercisesToRemove.put(result.getString("exercise_name"), result.getInt("exercise_duration"));
        removeLogic = connection.prepareStatement("DELETE FROM exercise_session_exercises " +
                "WHERE exercise_session_id = ?");
        removeLogic.setInt(1, id);
        removeLogic.executeUpdate();
        removeLogic = connection.prepareStatement("DELETE FROM exercise_session WHERE id = ?");
        removeLogic.setInt(1, id);
        removeLogic.executeUpdate();
        removeLogic = connection.prepareStatement("SELECT exercise_name, exercise_duration FROM exercise_session_exercises");
        result = removeLogic.executeQuery();
        Map<String, Integer> exercisesPresent = new HashMap<String, Integer>();
        while (result.next())
            exercisesPresent.put(result.getString("exercise_name"), result.getInt("exercise_duration"));
        for (Map.Entry<String, Integer> exercise : exercisesToRemove.entrySet()) {
            if (!exercisesPresent.entrySet().contains(exercise)) {
                PreparedStatement innerLogic = connection.prepareStatement("DELETE FROM exercises " +
                        "WHERE name = ? AND duration = ?");
                innerLogic.setString(1, exercise.getKey());
                innerLogic.setInt(2, exercise.getValue());
                innerLogic.executeUpdate();
            }
        }
        Database.closeConnection(connection);
    }
}
