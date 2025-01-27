package project.DAO;

import project.StudySession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLStudySessionDAO implements DAO<StudySession, String> {
    private StudySession getHelper(Connection connection, String title) throws SQLException {
        PreparedStatement getLogic = connection.prepareStatement("SELECT id, duration FROM study_session " +
                "WHERE title = ?");
        getLogic.setString(1, title);
        ResultSet result = getLogic.executeQuery();
        if (!result.next())
            throw new IllegalArgumentException("No Study session found with title " + title);
        int id = result.getInt("id");
        int duration = result.getInt("duration");
        result.close();
        StudySession studySession = new StudySession(title, duration);
        getLogic = connection.prepareStatement("SELECT subject_name FROM study_session_subjects " +
                "WHERE study_session_id = ?");
        getLogic.setInt(1, id);
        result = getLogic.executeQuery();
        while (result.next()) {
            studySession.addSubject(result.getString("subject_name"));
        }
        return studySession;
    }

    @Override
    public StudySession get(String title) throws SQLException {
        Connection connection = Database.getConnection();
        StudySession studySession = getHelper(connection, title);
        Database.closeConnection(connection);
        return studySession;
    }

    @Override
    public List<StudySession> getAll() throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement getAllLogic = connection.prepareStatement("SELECT id FROM study_session");
        ResultSet result = getAllLogic.executeQuery();
        List<Integer> ids = new ArrayList<Integer>();
        while (result.next()) {
            ids.add(result.getInt("id"));
        }
        List<StudySession> studySessions = new ArrayList<StudySession>();
        for (int id : ids) {
            PreparedStatement getTitle = connection.prepareStatement("SELECT title FROM study_session " +
                    "WHERE id = ?");
            getTitle.setInt(1, id);
            ResultSet innerResult = getTitle.executeQuery();
            innerResult.next();
            studySessions.add(getHelper(connection, innerResult.getString("title")));
        }
        Database.closeConnection(connection);
        return studySessions;
    }

    @Override
    public void insert(StudySession objectToAdd) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement insertLogic = connection.prepareStatement("INSERT INTO study_session(title, duration)" +
                "VALUES(?, ?)");
        insertLogic.setString(1, objectToAdd.getTitle());
        insertLogic.setInt(2, objectToAdd.getDuration());
        insertLogic.executeUpdate();
        insertLogic = connection.prepareStatement("SELECT MAX(id) FROM study_session WHERE title = ?");
        insertLogic.setString(1, objectToAdd.getTitle());
        ResultSet result = insertLogic.executeQuery();
        int idInsert = 0;
        if (result.next()) {
            idInsert = result.getInt(1);
        }
        result.close();
        List<String> newSubjects = objectToAdd.streamSubjects().toList();
        List<String> oldSubjects = new ArrayList<String>();
        insertLogic = connection.prepareStatement("SELECT name FROM subjects");
        result = insertLogic.executeQuery();
        while (result.next()) {
            oldSubjects.add(result.getString("name"));
        }
        for (String subject : newSubjects) {
            if (!oldSubjects.contains(subject)) {
                PreparedStatement insert = connection.prepareStatement("INSERT INTO subjects(name) VALUES(?)");
                insert.setString(1, subject);
                insert.executeUpdate();
            }
            PreparedStatement insert = connection.prepareStatement("INSERT INTO study_session_subjects (study_session_id, subject_name) "
                    + "VALUES(?,?)");
            insert.setInt(1, idInsert);
            insert.setString(2, subject);
            insert.executeUpdate();
        }
        Database.closeConnection(connection);
    }

    @Override
    public void update(StudySession updatedObject, String title) throws SQLException {
        this.remove(title);
        this.insert(updatedObject);
    }

    @Override
    public void remove(String title) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement removeLogic = connection.prepareStatement("SELECT id FROM study_session WHERE title = ?");
        removeLogic.setString(1, title);
        ResultSet result = removeLogic.executeQuery();
        if (!result.next())
            throw new IllegalArgumentException("No Study session found with title " + title);
        int id = result.getInt("id");
        result.close();
        removeLogic = connection.prepareStatement("SELECT subject_name FROM study_session_subjects " +
                "WHERE study_session_id = ?");
        removeLogic.setInt(1, id);
        result = removeLogic.executeQuery();
        List<String> subjectsToRemove = new ArrayList<String>();
        while (result.next()) {
            subjectsToRemove.add(result.getString("subject_name"));
        }
        removeLogic = connection.prepareStatement("DELETE FROM study_session_subjects " +
                "WHERE study_session_id = ?");
        removeLogic.setInt(1, id);
        removeLogic.executeUpdate();
        removeLogic = connection.prepareStatement("DELETE FROM study_session " +
                "WHERE id = ?");
        removeLogic.setInt(1, id);
        removeLogic.executeUpdate();
        removeLogic = connection.prepareStatement("SELECT subject_name FROM study_session_subjects");
        result = removeLogic.executeQuery();
        List<String> subjectsPresent = new ArrayList<String>();
        while (result.next()) {
            subjectsPresent.add(result.getString("subject_name"));
        }
        for (String subject : subjectsToRemove) {
            if (!subjectsPresent.contains(subject)) {
                PreparedStatement innerLogic = connection.prepareStatement("DELETE FROM subjects " +
                        "WHERE name = ?");
                innerLogic.setString(1, subject);
                innerLogic.executeUpdate();
            }
        }
        Database.closeConnection(connection);
    }
}

