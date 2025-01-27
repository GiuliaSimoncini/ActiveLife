package project.DAO;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T, ID>{
    T get(ID title) throws SQLException;
    List<T> getAll() throws SQLException;
    void insert(T objectToAdd) throws SQLException;
    void update(T updatedObject, ID title) throws SQLException;
    void remove(ID title) throws SQLException;
}