package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//How to structure?????

//DB package & DB Connection Class & Create Account Class & get Account class???

public class DBConnector {

    static String url =  "jdbc:mysql://mysql-249f2790-nicksproject.k.aivencloud.com:19286/NicksProjectDB?ssl-mode=REQUIRED";
    static String user = "avnadmin";
    static String password = System.getenv("DB_Password");


    public static Connection getConnection(){
        try{
            return DriverManager.getConnection(url, user, password);
        } catch(SQLException e){
            throw new RuntimeException("Database Connection Failed: " + e.getMessage());
        }
    }
}