package Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//How to structure?????

//DB package & DB Connection Class & Create Account Class & get Account class???

public class DBConnector {
    static String url =  "jdbc:mysql://localhost:3306/nicksprojectdb";
    static String user = "root";
    static String password = "N3i1c9k5";

    public static Connection getConnection(){
        try{
            return DriverManager.getConnection(url, user, password);
            } catch(SQLException e){
                throw new RuntimeException("Database Connection Failed: " + e.getMessage());
        }
    }
}