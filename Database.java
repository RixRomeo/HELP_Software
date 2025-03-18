package progetto_help;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
  static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
  static final String DB_URL = "jdbc:mysql://localhost/database_help";
  static final String USER = "root";
  static final String PASS = "admin";

  
  public static Connection getConnection() throws SQLException, ClassNotFoundException {
    Class.forName(JDBC_DRIVER);
    Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
    return conn;
  }
  
  
}

