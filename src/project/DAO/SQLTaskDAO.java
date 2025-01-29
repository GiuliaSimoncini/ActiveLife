package project.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import project.*;

public class SQLTaskDAO implements DAO<Task, String> {
    @Override
    public Task get(String title) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement getLogic = connection.prepareStatement("SELECT id FROM task WHERE title = ?");
        getLogic.setString(1, title);
        ResultSet result = getLogic.executeQuery();
        if (!result.next())
            throw new IllegalArgumentException("Task not found with title " + title);
        int taskId = result.getInt("id");
        result.close();
        getLogic = connection.prepareStatement("SELECT priority FROM task WHERE id = ?");
        getLogic.setInt(1, taskId);
        result = getLogic.executeQuery();
        result.next();
        int priority = result.getInt("priority");
        Task task = new Task(title);
        if (priority > 0)
            task.priority(priority);

        //GAMING SESSION:
        getLogic = connection.prepareStatement("SELECT gaming_session_id FROM task_gaming_session " +
                "WHERE task_id = ?");
        getLogic.setInt(1, taskId);
        result = getLogic.executeQuery();
        while (result.next()) {
            PreparedStatement getGamingSession = connection.prepareStatement("SELECT title FROM gaming_session " +
                    "WHERE id = ?");
            getGamingSession.setInt(1, result.getInt("gaming_session_id"));
            ResultSet resultGamingSession = getGamingSession.executeQuery();
            SQLGamingSessionDAO gamingSessionDAO = new SQLGamingSessionDAO();
            if (resultGamingSession.next()) {
                task.addToDo(gamingSessionDAO.get(resultGamingSession.getString("title")));
            }
        }

        //SHOPPING SESSION:
        getLogic = connection.prepareStatement("SELECT shopping_session_id FROM task_shopping_session " +
                "WHERE task_id = ?");
        getLogic.setInt(1, taskId);
        result = getLogic.executeQuery();
        while (result.next()) {
            PreparedStatement getShoppingSession = connection.prepareStatement("SELECT title FROM shopping_session " +
                    "WHERE id = ?");
            getShoppingSession.setInt(1, result.getInt("shopping_session_id"));
            ResultSet resultShoppingSession = getShoppingSession.executeQuery();
            SQLShoppingSessionDAO shoppingSessionDAO = new SQLShoppingSessionDAO();
            if (resultShoppingSession.next()) {
                task.addToDo(shoppingSessionDAO.get(resultShoppingSession.getString("title")));
            }
        }

        //EXERCISE SESSION:
        getLogic = connection.prepareStatement("SELECT exercise_session_id FROM task_exercise_session " +
                "WHERE task_id = ?");
        getLogic.setInt(1, taskId);
        result = getLogic.executeQuery();
        while (result.next()) {
            PreparedStatement getExerciseSession = connection.prepareStatement("SELECT title FROM exercise_session " +
                    "WHERE id = ?");
            getExerciseSession.setInt(1, result.getInt("exercise_session_id"));
            ResultSet resultExerciseSession = getExerciseSession.executeQuery();
            SQLExerciseSessionDAO exerciseSessionDAO = new SQLExerciseSessionDAO();
            if (resultExerciseSession.next()) {
                task.addToDo(exerciseSessionDAO.get(resultExerciseSession.getString("title")));
            }
        }

        //STUDY SESSION:
        getLogic = connection.prepareStatement("SELECT study_session_id FROM task_study_session " +
                "WHERE task_id = ?");
        getLogic.setInt(1, taskId);
        result = getLogic.executeQuery();
        while (result.next()) {
            PreparedStatement getStudySession = connection.prepareStatement("SELECT title FROM study_session " +
                    "WHERE id = ?");
            getStudySession.setInt(1, result.getInt("study_session_id"));
            ResultSet resultStudySession = getStudySession.executeQuery();
            SQLStudySessionDAO studySessionDAO = new SQLStudySessionDAO();
            if (resultStudySession.next()) {
                task.addToDo(studySessionDAO.get(resultStudySession.getString("title")));
            }
        }

        //TASK:
        getLogic = connection.prepareStatement("SELECT task_id2 FROM task_task " +
                "WHERE task_id = ?");
        getLogic.setInt(1, taskId);
        result = getLogic.executeQuery();
        while (result.next()) {
            PreparedStatement getTask = connection.prepareStatement("SELECT title FROM task " +
                    "WHERE id = ?");
            getTask.setInt(1, result.getInt("task_id2"));
            ResultSet resultTask = getTask.executeQuery();
            if (resultTask.next()) {
                Task task2 = this.get(resultTask.getString("title"));
                task.addToDo(task2);
            }
        }
        Database.closeConnection(connection);
        return task;
    }

    @Override
    public List<Task> getAll() throws SQLException {
        List<Task> tasks = new ArrayList<Task>();
        Connection connection = Database.getConnection();
        PreparedStatement getAllLogic = connection.prepareStatement("SELECT * FROM task");
        ResultSet result = getAllLogic.executeQuery();
        while (result.next()) {
            tasks.add(this.get(result.getString("title")));
        }
        Database.closeConnection(connection);
        return tasks;
    }

    @Override
    public void insert(Task objectToAdd) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement insertLogic = connection.prepareStatement("INSERT INTO task (title, priority) VALUES (?, ?)");
        insertLogic.setString(1, objectToAdd.getTitle());
        insertLogic.setInt(2, objectToAdd.getPriority());
        insertLogic.executeUpdate();
        insertLogic = connection.prepareStatement("SELECT MAX(id) FROM task");
        ResultSet result = insertLogic.executeQuery();
        result.next();
        int taskId = result.getInt(1);
        SQLGamingSessionDAO gamingSessionDAO = new SQLGamingSessionDAO();
        SQLShoppingSessionDAO shoppingSessionDAO = new SQLShoppingSessionDAO();
        SQLExerciseSessionDAO exerciseSessionDAO = new SQLExerciseSessionDAO();
        SQLStudySessionDAO studySessionDAO = new SQLStudySessionDAO();
        objectToAdd.stream().forEach(toDo -> {
            //GAMING SESSION:
            if (toDo instanceof GamingSession) {
                try {
                    gamingSessionDAO.insert((GamingSession) toDo);
                    PreparedStatement insertGamingSession = connection.prepareStatement("SELECT MAX(id) " +
                            "FROM gaming_session");
                    ResultSet resultGamingSession = insertGamingSession.executeQuery();
                    resultGamingSession.next();
                    int gamingSessionId = resultGamingSession.getInt(1);
                    PreparedStatement insertTaskGamingSession = connection.prepareStatement("INSERT INTO task_gaming_session " +
                            "(task_id, gaming_session_id) VALUES (?, ?)");
                    insertTaskGamingSession.setInt(1, taskId);
                    insertTaskGamingSession.setInt(2, gamingSessionId);
                    insertTaskGamingSession.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            //SHOPPING SESSION:
            if (toDo instanceof ShoppingSession) {
                try {
                    shoppingSessionDAO.insert((ShoppingSession) toDo);
                    PreparedStatement insertShoppingSession = connection.prepareStatement("SELECT MAX(id) " +
                            "FROM shopping_session");
                    ResultSet resultShoppingSession = insertShoppingSession.executeQuery();
                    resultShoppingSession.next();
                    int shoppingSessionId = resultShoppingSession.getInt(1);
                    PreparedStatement insertTaskShoppingSession = connection.prepareStatement("INSERT INTO task_shopping_session " +
                            "(task_id, shopping_session_id) VALUES (?, ?)");
                    insertTaskShoppingSession.setInt(1, taskId);
                    insertTaskShoppingSession.setInt(2, shoppingSessionId);
                    insertTaskShoppingSession.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            //EXERCISE SESSION:
            if (toDo instanceof ExerciseSession) {
                try {
                    exerciseSessionDAO.insert((ExerciseSession) toDo);
                    PreparedStatement insertExerciseSession = connection.prepareStatement("SELECT MAX(id) " +
                            "FROM exercise_session");
                    ResultSet resultExerciseSession = insertExerciseSession.executeQuery();
                    resultExerciseSession.next();
                    int exerciseSessionId = resultExerciseSession.getInt(1);
                    PreparedStatement insertTaskExerciseSession = connection.prepareStatement("INSERT INTO task_exercise_session " +
                            "(task_id, exercise_session_id) VALUES (?, ?)");
                    insertTaskExerciseSession.setInt(1, taskId);
                    insertTaskExerciseSession.setInt(2, exerciseSessionId);
                    insertTaskExerciseSession.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            //STUDY SESSION:
            if (toDo instanceof StudySession) {
                try {
                    studySessionDAO.insert((StudySession) toDo);
                    PreparedStatement insertStudySession = connection.prepareStatement("SELECT MAX(id) " +
                            "FROM study_session");
                    ResultSet resultStudySession = insertStudySession.executeQuery();
                    resultStudySession.next();
                    int studySessionId = resultStudySession.getInt(1);
                    PreparedStatement insertTaskStudySession = connection.prepareStatement("INSERT INTO task_study_session " +
                            "(task_id, study_session_id) VALUES (?, ?)");
                    insertTaskStudySession.setInt(1, taskId);
                    insertTaskStudySession.setInt(2, studySessionId);
                    insertTaskStudySession.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            //TASK:
            if (toDo instanceof Task) {
                try {
                    this.insert((Task) toDo);
                    PreparedStatement insertTask = connection.prepareStatement("SELECT id " +
                            "FROM task WHERE title = ?");
                    insertTask.setString(1, toDo.getTitle());
                    ResultSet resultTask = insertTask.executeQuery();
                    resultTask.next();
                    int subTaskId = resultTask.getInt(1);
                    PreparedStatement insertTaskTask = connection.prepareStatement("INSERT INTO task_task " +
                            "(task_id, task_id2) VALUES (?, ?)");
                    insertTaskTask.setInt(1, taskId);
                    insertTaskTask.setInt(2, subTaskId);
                    insertTaskTask.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Database.closeConnection(connection);
    }

    @Override
    public void update(Task updatedObject, String title) throws SQLException {
        this.remove(title);
        this.insert(updatedObject);
    }

    private void removeHelper(Connection connection, String title) throws SQLException {
        Task task = this.get(title);
        PreparedStatement removeLogic = connection.prepareStatement("SELECT id FROM task WHERE title = ?");
        removeLogic.setString(1, title);
        ResultSet result = removeLogic.executeQuery();
        if (!result.next())
            throw new IllegalArgumentException("Task not found with title " + title);
        int taskId = result.getInt("id");
        result.close();
        removeLogic = connection.prepareStatement("DELETE FROM task_task WHERE task_id = ?");
        removeLogic.setInt(1, taskId);
        removeLogic.executeUpdate();
        SQLGamingSessionDAO gamingSessionDAO = new SQLGamingSessionDAO();
        SQLShoppingSessionDAO shoppingSessionDAO = new SQLShoppingSessionDAO();
        SQLExerciseSessionDAO exerciseSessionDAO = new SQLExerciseSessionDAO();
        SQLStudySessionDAO studySessionDAO = new SQLStudySessionDAO();
        task.stream().forEach(toDo -> {
            //GAMING SESSION:
            if (toDo instanceof GamingSession) {
                try {
                    PreparedStatement removeTaskGamingSession = connection.prepareStatement("SELECT id FROM gaming_session " +
                            "WHERE title = ?");
                    removeTaskGamingSession.setString(1, toDo.getTitle());
                    ResultSet resultGamingSession = removeTaskGamingSession.executeQuery();
                    if (!resultGamingSession.next())
                        throw new IllegalArgumentException("Gaming session not found with title " + toDo.getTitle());
                    int gamingSessionId = resultGamingSession.getInt("id");
                    resultGamingSession.close();
                    removeTaskGamingSession = connection.prepareStatement("DELETE FROM task_gaming_session " +
                            "WHERE gaming_session_id = ?");
                    removeTaskGamingSession.setInt(1, gamingSessionId);
                    removeTaskGamingSession.executeUpdate();
                    gamingSessionDAO.remove(toDo.getTitle());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            //SHOPPING SESSION:
            if (toDo instanceof ShoppingSession) {
                try {
                    PreparedStatement removeTaskShoppingSession = connection.prepareStatement("SELECT id FROM shopping_session " +
                            "WHERE title = ?");
                    removeTaskShoppingSession.setString(1, toDo.getTitle());
                    ResultSet resultShoppingSession = removeTaskShoppingSession.executeQuery();
                    if (!resultShoppingSession.next())
                        throw new IllegalArgumentException("Shopping session not found with title " + toDo.getTitle());
                    int shoppingSessionId = resultShoppingSession.getInt("id");
                    resultShoppingSession.close();
                    removeTaskShoppingSession = connection.prepareStatement("DELETE FROM task_shopping_session " +
                            "WHERE shopping_session_id = ?");
                    removeTaskShoppingSession.setInt(1, shoppingSessionId);
                    removeTaskShoppingSession.executeUpdate();
                    shoppingSessionDAO.remove(toDo.getTitle());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            //EXERCISE SESSION:
            if (toDo instanceof ExerciseSession) {
                try {
                    PreparedStatement removeTaskExerciseSession = connection.prepareStatement("SELECT id FROM exercise_session " +
                            "WHERE title = ?");
                    removeTaskExerciseSession.setString(1, toDo.getTitle());
                    ResultSet resultExerciseSession = removeTaskExerciseSession.executeQuery();
                    if (!resultExerciseSession.next())
                        throw new IllegalArgumentException("Exercise session not found with title " + toDo.getTitle());
                    int exerciseSessionId = resultExerciseSession.getInt("id");
                    resultExerciseSession.close();
                    removeTaskExerciseSession = connection.prepareStatement("DELETE FROM task_exercise_session " +
                            "WHERE exercise_session_id = ?");
                    removeTaskExerciseSession.setInt(1, exerciseSessionId);
                    removeTaskExerciseSession.executeUpdate();
                    exerciseSessionDAO.remove(toDo.getTitle());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            //STUDY SESSION:
            if (toDo instanceof StudySession) {
                try {
                    PreparedStatement removeTaskStudySession = connection.prepareStatement("SELECT id FROM study_session " +
                            "WHERE title = ?");
                    removeTaskStudySession.setString(1, toDo.getTitle());
                    ResultSet resultStudySession = removeTaskStudySession.executeQuery();
                    if (!resultStudySession.next())
                        throw new IllegalArgumentException("Study session not found with title " + toDo.getTitle());
                    int studySessionId = resultStudySession.getInt("id");
                    resultStudySession.close();
                    removeTaskStudySession = connection.prepareStatement("DELETE FROM task_study_session " +
                            "WHERE study_session_id = ?");
                    removeTaskStudySession.setInt(1, studySessionId);
                    removeTaskStudySession.executeUpdate();
                    studySessionDAO.remove(toDo.getTitle());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            //TASK:
            if (toDo instanceof Task) {
                try {
                    this.removeHelper(connection, toDo.getTitle());
                    PreparedStatement removeTask = connection.prepareStatement("SELECT id FROM task " +
                            "WHERE title = ?");
                    removeTask.setString(1, toDo.getTitle());
                    ResultSet resultTask = removeTask.executeQuery();
                    if (!resultTask.next())
                        throw new IllegalArgumentException("Task not found with title " + toDo.getTitle());
                    int subTaskId = resultTask.getInt("id");
                    resultTask.close();
                    removeTask = connection.prepareStatement("DELETE FROM task_task " +
                            "WHERE task_id2 = ?");
                    removeTask.setInt(1, subTaskId);
                    removeTask.executeUpdate();
                    removeTask = connection.prepareStatement("DELETE FROM task " +
                            "WHERE id = ?");
                    removeTask.setInt(1, subTaskId);
                    removeTask.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void remove(String title) throws SQLException {
        Connection connection = Database.getConnection();
        this.removeHelper(connection, title);
        PreparedStatement removeLogic = connection.prepareStatement("SELECT id FROM task WHERE title = ?");
        removeLogic.setString(1, title);
        ResultSet result = removeLogic.executeQuery();
        if (!result.next())
            throw new IllegalArgumentException("Task not found with title " + title);
        int taskId = result.getInt("id");
        result.close();
        removeLogic = connection.prepareStatement("DELETE FROM task_task WHERE task_id = ? OR task_id2 = ?");
        removeLogic.setInt(1, taskId);
        removeLogic.setInt(2, taskId);
        removeLogic.executeUpdate();
        removeLogic = connection.prepareStatement("DELETE FROM task WHERE id = ?");
        removeLogic.setInt(1, taskId);
        removeLogic.executeUpdate();
        Database.closeConnection(connection);
    }
}
