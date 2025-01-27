package project.DAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class Database {
    private static String name = "to_do_tracker";

    private Database() {}

    public static void setName(String dbName) {
        name = dbName;
    }

    public static Connection getConnection(String dbName) throws SQLException {
        String url = "jdbc:postgresql:" + dbName;
        return DriverManager.getConnection(url, "postgres", "postgres");
    }

    public static Connection getConnection() throws SQLException {
        return getConnection(name);
    }

    public static void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public static void initDatabase() throws IOException, SQLException {
        StringBuilder srt = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader("src/project/database/schema.sql"));
        String line;
        while ((line = br.readLine()) != null) {
            srt.append(line).append("\n");
        }

        Connection connection = getConnection();
        Statement stmt = getConnection().createStatement();
        int row = stmt.executeUpdate(srt.toString());

        stmt.close();
        closeConnection(connection);
    }
}

