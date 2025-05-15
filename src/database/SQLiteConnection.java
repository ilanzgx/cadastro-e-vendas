package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    private static final String DatabaseURI = "jdbc:sqlite:database.db";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(DatabaseURI);
    }
}
