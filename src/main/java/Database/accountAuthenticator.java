package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class accountAuthenticator {

    public static int authenticateUser(String username, String password){
        //Creating prepared statement strings
        String sqlUsername = "Select * from users where username = ? and password_hash = ?";
        String sqlEmail = "Select * from users where email = ? and password_hash = ?";



        //Establish Database Connector, run prepd statements
        try{
            Connection conn = DBConnector.getConnection();
            PreparedStatement stmt = null;

            if(username.contains("@")){
                stmt = conn.prepareStatement(sqlEmail);
            }
            else{
                stmt = conn.prepareStatement(sqlUsername);
            }

            //format statements for user authentication && run query
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeQuery();


        }catch(SQLException e){
            System.out.println("Error in authenticateUser");;
        }

        return 0;
    }
}
